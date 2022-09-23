package rysavpe1.reads.combined.multiprojected;

import rysavpe1.reads.combined.embedded.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import rysavpe1.reads.combined.multiprojected.model.CombinedInput;
import rysavpe1.reads.combined.multiprojected.model.QGramContig;
import rysavpe1.reads.combined.multiprojected.model.QGramContigSet;
import rysavpe1.reads.distance.AbstractMeasure;
import rysavpe1.reads.distance.DistanceCalculator;
import rysavpe1.reads.distance.simple.ManhattanDistance;
import rysavpe1.reads.distance.simple.UkkonenDistance;
import rysavpe1.reads.embedded.EmbeddedSequence;
import rysavpe1.reads.margingap.LinearMarginGapPenalty;
import rysavpe1.reads.margingap.MarginGapEditDistance;
import rysavpe1.reads.margingap.MarginGapPenaulty;
import rysavpe1.reads.model.ReadBag;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.overlap.OverlapAdapterAndSequence;
import rysavpe1.reads.overlap.OverlapFinder;
import rysavpe1.reads.overlap.OverlapRegion;
import rysavpe1.reads.overlap.ReadFinder;
import rysavpe1.reads.overlap.ReadInContig;
import rysavpe1.reads.overlap.RegionValue;
import rysavpe1.reads.overlap.TripletsReadInContigFinder;
import rysavpe1.reads.utils.ArrayUtils;
import rysavpe1.reads.utils.IteratorWrapper;
import rysavpe1.reads.utils.MathUtils;
import rysavpe1.reads.utils.Pair;
import rysavpe1.reads.utils.TicTac;
import rysavpe1.reads.wis.WeightedIntervalScheduling;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class CombinedQGramDistance extends AbstractMeasure<CombinedInput> {

    private static final int SENZITIVITY = 3;
    private static final double CONTIG_SENZITIVITY = 1.2;
    private final OverlapFinder overlapFinder;
    private final ReadFinder<int[], int[]> readFinder;
    private final RegionValue regionValue;
    private final boolean shouldReverse;
    private final boolean shouldComplement;
    private final DistanceCalculator<Sequence, ? extends Number> readDistance;
    private final DistanceCalculator<int[], ? extends Number> readEmbeddedDistance;
    private final DistanceCalculator<Sequence, ? extends Number> originalSpaceContigDistance;
    private final EmbeddedTupleFilter filter;
    private final int readLength;
    private final double coverage;

    public CombinedQGramDistance(int readLength, double coverage, int overlapThreshold, boolean shouldReverse, boolean shouldComplement) {
        this.readLength = readLength;
        this.coverage = coverage;
        this.readFinder = new TripletsReadInContigFinder();
        this.regionValue = RegionValue.LENGTH_OVER_DISTANCE;
        this.shouldReverse = shouldReverse;
        this.shouldComplement = shouldComplement;
        this.readEmbeddedDistance = new ManhattanDistance();
        final MarginGapPenaulty gap = new LinearMarginGapPenalty(coverage, readLength, 2);
        this.readDistance = new MarginGapEditDistance(0, 1, 1, gap);
        this.filter = new EmbeddedTupleFilter(readLength);
        this.overlapFinder = new QGramOverlapFinder(20);
        this.originalSpaceContigDistance = new UkkonenDistance();
    }

    @Override
    public Double getDistance(CombinedInput a, CombinedInput b) {
//        TicTac ticTac = new TicTac();
        final ReadBag contigsAOutsideOverlaps = new ReadBag(a.getContigs().size());
        final ReadBag contigsBOutsideOverlaps = new ReadBag(b.getContigs().size());
        int aOverlapLength = 0, bOverlapLength = 0;
        double sumA = 0.0, sumB = 0.0;
        int matchedLensMaxA = 0, matchedLensMaxB = 0;
        List<OverlapAdapterAndSequence> aRegions = new ArrayList<>(b.getContigs().size() * (shouldComplement ? 2 : 1) * (shouldReverse ? 2 : 1));
        Map<QGramContig, List<OverlapAdapterAndSequence>> bRegionsMap = new HashMap<>(b.getContigs().size());
        final int aListSize = a.getContigs().size() * (shouldComplement ? 2 : 1) * (shouldReverse ? 2 : 1);
        for (QGramContig bSeq : new IteratorWrapper<>(b.getContigs().embeddedIterator()))
            bRegionsMap.put(bSeq, new ArrayList<>(aListSize));
        
//        System.err.println("Init: "+ticTac.toctic());

        for (QGramContig aSeq : new IteratorWrapper<>(a.getContigs().embeddedIterator())) {
            aRegions.clear();
            for (QGramContig bSeq : new IteratorWrapper<>(b.getContigs().embeddedIterator())) {
                final List<OverlapAdapterAndSequence> bRegions = bRegionsMap.get(bSeq);
                doUnoriented((boolean rev, boolean compl) -> calcOverlap(aSeq, bSeq, aRegions, bRegions, rev, compl));
            }

            final Collection<OverlapAdapterAndSequence> wisSolution = WeightedIntervalScheduling.solve(asArray(aRegions));
            for (OverlapAdapterAndSequence region : wisSolution) {
                sumA += originalSpaceContigDistance.getDistance(region.projectA(), region.projectB()).doubleValue();
                matchedLensMaxA += region.getRegion().getLengthMax();
            }
            aOverlapLength += aSeq.getObject().length() - filter.findNonOverlap(contigsAOutsideOverlaps, wisSolution, aSeq.getObject());
        }
        for (Entry<QGramContig, List<OverlapAdapterAndSequence>> bRegions : bRegionsMap.entrySet()) {
            final Collection<OverlapAdapterAndSequence> wisSolution = WeightedIntervalScheduling.solve(asArray(bRegions.getValue()));
            for (OverlapAdapterAndSequence region : wisSolution) {
                sumB += originalSpaceContigDistance.getDistance(region.projectA(), region.projectB()).doubleValue();
                matchedLensMaxB += region.getRegion().getLengthMax();
            }
            bOverlapLength += bRegions.getKey().getObject().length() - filter.findNonOverlap(contigsBOutsideOverlaps, wisSolution, bRegions.getKey().getObject());
        }
        final double contigABDistance = a.getContigs().isEmpty() || b.getContigs().isEmpty() ? 0.0 : sumA / matchedLensMaxA;
        final double contigBADistance = a.getContigs().isEmpty() || b.getContigs().isEmpty() ? 0.0 : sumB / matchedLensMaxB;

        final double percentAInOverlaps = Math.min(((double) aOverlapLength) / a.getSequenceLengthEstimate(), 1);
        final double percentBInOverlaps = Math.min(((double) bOverlapLength) / b.getSequenceLengthEstimate(), 1);

        final List<EmbeddedSequence<int[]>> aUnmappedReads = a.getReads().toList();
        final List<QGramContig> aContigs = a.getValue2().toList();
        final List<EmbeddedSequence<int[]>> bUnmappedReads = b.getReads().toList();
        final List<QGramContig> bContigs = b.getValue2().toList();
        final List<QGramContig> aContigsNonOverlapL = new QGramContigSet(contigsAOutsideOverlaps).toList();
        final List<QGramContig> bContigsNonOverlapL = new QGramContigSet(contigsBOutsideOverlaps).toList();
        
//        System.err.println("Contig pairs: "+ticTac.toctic());

//        System.err.println("unmappedReads "+aUnmappedReads + " / "+bUnmappedReads);
//        System.err.println("contigsNonOverlap "+aContigsNonOverlapL + " / "+bContigsNonOverlapL);
        final ReadMatchHolder[] aReadMatches = new ReadMatchHolder[aUnmappedReads.size()];
        for (int i = 0; i < aReadMatches.length; i++)
            aReadMatches[i] = new ReadMatchHolder(SENZITIVITY);
        final ContigMatchHolder[] aContigsBestMatch = initBestMatchListForContigs(aContigsNonOverlapL);
        final ReadMatchHolder[] bReadMatches = new ReadMatchHolder[bUnmappedReads.size()];
        for (int i = 0; i < bReadMatches.length; i++)
            bReadMatches[i] = new ReadMatchHolder(SENZITIVITY);
        final ContigMatchHolder[] bContigsBestMatch = initBestMatchListForContigs(bContigsNonOverlapL);

        // each pair of reads
        for (int i = 0; i < aReadMatches.length; i++)
            for (int j = 0; j < bReadMatches.length; j++) {
                final EmbeddedSequence<int[]> iS = aUnmappedReads.get(i);
                final EmbeddedSequence<int[]> jS = bUnmappedReads.get(j);
                final ReadMatchHolder holderA = aReadMatches[i];
                final ReadMatchHolder holderB = bReadMatches[j];

                doUnoriented((boolean rev, boolean compl) -> calcReadAndRead(iS, jS, holderA, holderB, rev, compl));
            }
//        System.err.println("Read pairs: "+ticTac.toctic());

        // for each read find the best matching part of a contig
        for (int i = 0; i < aReadMatches.length; i++) {
            final EmbeddedSequence<int[]> read = aUnmappedReads.get(i);
            final ReadMatchHolder holder = aReadMatches[i];
            for (int j = 0; j < bContigs.size(); j++) {
                final QGramContig contig = bContigs.get(j);

                doUnoriented((boolean rev, boolean compl) -> calcReadInContig(read, contig, holder, rev, compl));
            }
        }
        for (int i = 0; i < aContigs.size(); i++) {
            final QGramContig contig = aContigs.get(i);
            for (int j = 0; j < bReadMatches.length; j++) {
                final EmbeddedSequence<int[]> read = bUnmappedReads.get(j);
                final ReadMatchHolder holder = bReadMatches[j];
                doUnoriented((boolean rev, boolean compl) -> calcReadInContig(read, contig, holder, rev, compl));
            }
        }
//        System.err.println("Read contig: "+ticTac.toctic());

        // for each nonused contig part find the best matching read
        for (int i = 0; i < aReadMatches.length; i++) {
            final EmbeddedSequence<int[]> read = aUnmappedReads.get(i);
            for (int j = 0; j < bContigsBestMatch.length; j++) {
                final QGramContig contig = bContigsNonOverlapL.get(j);
                final ContigMatchHolder holder = bContigsBestMatch[j];

                doUnoriented((boolean rev, boolean compl) -> calcReadsForContig(read, contig, holder, rev, compl));
            }
        }
        for (int i = 0; i < aContigsBestMatch.length; i++) {
            final QGramContig contig = aContigsNonOverlapL.get(i);
            final ContigMatchHolder holder = aContigsBestMatch[i];
            for (int j = 0; j < bReadMatches.length; j++) {
                final EmbeddedSequence<int[]> read = bUnmappedReads.get(j);

                doUnoriented((boolean rev, boolean compl) -> calcReadsForContig(read, contig, holder, rev, compl));
            }
        }
//        System.err.println("Contig to reads: "+ticTac.toctic());

        // now consolidate the results
        final double[] aReadBestMatch = consolidateReadDistance(aUnmappedReads, aReadMatches);
        final double[] bReadBestMatch = consolidateReadDistance(bUnmappedReads, bReadMatches);

        final Pair<double[], int[]> aContigBestMatch = consolidateContigDistance(aContigsNonOverlapL, aContigsBestMatch);
        final Pair<double[], int[]> bContigBestMatch = consolidateContigDistance(bContigsNonOverlapL, bContigsBestMatch);
//        System.err.println("Consolidate: "+ticTac.toctic());

        // now calculate average for reads and parts of contigs that do not belong to overlap
        final double readABDist = average(aReadBestMatch, aContigBestMatch) / readLength;
        final double readBADist = average(bReadBestMatch, bContigBestMatch) / readLength;
//        System.err.println("contig distance " + contigABDistance + " / " + contigBADistance);
//        System.err.println("read distance " + readABDist + " / " + readBADist);
//        System.err.println("best match A "+Arrays.toString(aReadBestMatch) + " , "+Arrays.toString(aContigsBestMatch));
//        System.err.println("best match B "+Arrays.toString(bReadBestMatch) + " , "+Arrays.toString(bContigsBestMatch));
//        System.err.println("Percent a in overlaps " + percentAInOverlaps);
//        System.err.println("Percent b in overlaps " + percentBInOverlaps);
//        System.err.println("a read count : " + aUnmappedReads.size() + " / " + a.getSequenceLengthEstimate() / readLength * coverage);
//        System.err.println("a read count : " + bUnmappedReads.size() + " / " + b.getSequenceLengthEstimate() / readLength * coverage);
        return MathUtils.average(
                MathUtils.weightedAverageSafeNaN(percentAInOverlaps, contigABDistance, readABDist),
                MathUtils.weightedAverageSafeNaN(percentBInOverlaps, contigBADistance, readBADist)
        ) * Math.max(a.getSequenceLengthEstimate(), b.getSequenceLengthEstimate());
//        return MathUtils.average(contigABDistance, contigBADistance) * Math.max(lengthAEstimate, lengthBEstimate);
//        return MathUtils.average(readABDist, readBADist) * Math.max(lengthAEstimate, lengthBEstimate);
    }

    private double average(double[] readDist, Pair<double[], int[]> contigDist) {
        double sum = MathUtils.sum(readDist) + MathUtils.sum(contigDist.value1);
        int count = readDist.length + MathUtils.sum(contigDist.value2);
        return count == 0 ? 0.0 : sum / count;
    }

    private ContigMatchHolder[] initBestMatchListForContigs(List<QGramContig> contigs) {
        final int count = contigs.size();
        final ContigMatchHolder[] bestMatch = new ContigMatchHolder[count];
        for (int i = 0; i < count; i++) {
            final int expectedReadsSenz = (int) Math.round(coverage * contigs.get(i).getObject().length() * CONTIG_SENZITIVITY / readLength);
            if (expectedReadsSenz == 0)
                bestMatch[i] = ContigMatchHolder.Empty.INSTANCE;
            else
                bestMatch[i] = new ContigMatchHolder(expectedReadsSenz);
        }
        return bestMatch;
    }

    private OverlapAdapterAndSequence[] asArray(Collection<OverlapAdapterAndSequence> regions) {
        return regions.toArray(new OverlapAdapterAndSequence[regions.size()]);
    }

    private double[] consolidateReadDistance(List<EmbeddedSequence<int[]>> reads, ReadMatchHolder[] readMatches) {
        final double[] bestDists = ArrayUtils.nTimes(Double.MAX_VALUE, reads.size());
        for (int i = 0; i < bestDists.length; i++) {
            final Sequence seqA = reads.get(i).getObject();
            for (ReadMatchHolder.Match match : readMatches[i]) {
                final Sequence seqB = match.getSequence();
                final double distance = match.isMarginDistance() ? readDistance.getDistance(seqA, seqB).doubleValue()
                        : originalSpaceContigDistance.getDistance(seqA, seqB).doubleValue();
                if (distance < bestDists[i]) bestDists[i] = distance;
            }
        }
        return bestDists;
    }

    private Pair<double[], int[]> consolidateContigDistance(List<QGramContig> contigs, ContigMatchHolder[] contigMatches) {
        final double[] distSums = new double[contigs.size()];
        final int[] counts = new int[distSums.length];

        for (int i = 0; i < distSums.length; i++) {
            final ContigMatchHolder holder = contigMatches[i];
            if (holder.isEmpty()) continue;
            final Sequence contig = contigs.get(i).getObject();
            final double[] distances = new double[holder.size()];
            int j = 0;
            for (ContigMatchHolder.Match match : holder) {
                final Sequence seqA = match.getSequence();
                final Sequence seqB = match.getContigPart(contig);
                distances[j] = match.isMarginDistance() ? readDistance.getDistance(seqA, seqB).doubleValue()
                        : originalSpaceContigDistance.getDistance(seqA, seqB).doubleValue();
                j++;
            }
            Arrays.sort(distances);
            final int count = (int) Math.round(coverage * contig.length() / readLength);
            distSums[i] = MathUtils.sum(distances, 0, count);
            counts[i] = count;
        }
        return new Pair<>(distSums, counts);
    }

    private void doUnoriented(UnorientedTask t) {
        t.calc(false, false);

        if (shouldComplement) {
            t.calc(true, true);
            if (shouldReverse)
                t.calc(false, true);
        }
        if (shouldReverse)
            t.calc(true, false);
    }

    private void calcOverlap(QGramContig aSeq, QGramContig bSeq, List<OverlapAdapterAndSequence> aRegions, List<OverlapAdapterAndSequence> bRegions, boolean bReversed, boolean bComplemented) {
        QGramContig bSeqRC = bReversed ? bSeq.reverse() : bSeq;
        if (bComplemented) bSeqRC = bSeqRC.complement();
        OverlapRegion region = overlapFinder.getOverlap(aSeq.getObject(), bSeqRC.getObject());
        if (region == null) return;
        if (bReversed) region = region.reverseB(bSeq.getObject().length());
        aRegions.add(OverlapAdapterAndSequence.overlapAdapterFirstSeq(aSeq.getObject(), bSeq.getObject(), region, regionValue, bReversed, bComplemented));
        bRegions.add(OverlapAdapterAndSequence.overlapAdapterSecondSeq(aSeq.getObject(), bSeq.getObject(), region, regionValue, bReversed, bComplemented));
    }

    private void calcReadInContig(EmbeddedSequence<int[]> read, QGramContig contig, ReadMatchHolder holder, boolean contigReversed, boolean contigComplemented) {
        QGramContig contigRC = contigReversed ? contig.reverse() : contig;
        if (contigComplemented) contigRC = contigRC.complement();
        final ReadInContig match = readFinder.findReadInContig(read, contigRC);
        if (match != null && holder.isForInsert((int) match.getDistance()))
            holder.put((int) match.getDistance(), match, contig.getObject(), contigReversed, contigComplemented);
    }

    private void calcReadAndRead(EmbeddedSequence<int[]> readA, EmbeddedSequence<int[]> readB, ReadMatchHolder holderA, ReadMatchHolder holderB, boolean reversed, boolean complemented) {
        EmbeddedSequence<int[]> readBRC = reversed ? readB.reverse() : readB;
        if (complemented) readBRC = readBRC.complement();
        final int distance = readEmbeddedDistance.getDistance(readA.getProjected(), readBRC.getProjected()).intValue();
        if (holderA.isForInsert(distance))
            holderA.put(distance, readB.getObject(), reversed, complemented);
        if (holderB.isForInsert(distance))
            holderB.put(distance, readA.getObject(), reversed, complemented);
    }

    private void calcReadsForContig(EmbeddedSequence<int[]> read, QGramContig contig, ContigMatchHolder holder, boolean readReversed, boolean readComplemented) {
        EmbeddedSequence<int[]> readRC = readReversed ? read.reverse() : read;
        if (readComplemented) readRC = readRC.complement();
        final ReadInContig match = readFinder.findReadInContig(readRC, contig);
        if (match != null)
            holder.add(match, read.getObject(), readReversed, readComplemented, contig.getObject());
    }

    private interface UnorientedTask {

        public void calc(boolean reversed, boolean complemented);
    }
}
