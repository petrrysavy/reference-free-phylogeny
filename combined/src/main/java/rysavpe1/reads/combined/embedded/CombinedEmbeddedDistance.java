package rysavpe1.reads.combined.embedded;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import rysavpe1.reads.distance.AbstractMeasure;
import rysavpe1.reads.distance.DistanceCalculator;
import rysavpe1.reads.distance.simple.ManhattanDistance;
import rysavpe1.reads.distance.simple.UkkonenDistance;
import rysavpe1.reads.embedded.EmbeddedReadBag;
import rysavpe1.reads.embedded.EmbeddedReadBagTuple;
import rysavpe1.reads.embedded.EmbeddedSequence;
import rysavpe1.reads.embedded.EmbeddingFunction;
import rysavpe1.reads.embedded.TripletsEmbedding;
import rysavpe1.reads.embedded.TripletsIndexVectorEmbedding;
import rysavpe1.reads.margingap.LinearMarginGapPenalty;
import rysavpe1.reads.margingap.MarginGapEditDistance;
import rysavpe1.reads.margingap.MarginGapPenaulty;
import rysavpe1.reads.model.ReadBag;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.overlap.OverlapAdapterAndSequence;
import rysavpe1.reads.overlap.OverlapRegion;
import rysavpe1.reads.overlap.ReadFinder;
import rysavpe1.reads.overlap.ReadInContig;
import rysavpe1.reads.overlap.RegionValue;
import rysavpe1.reads.overlap.TripletsReadInContigFinder;
import rysavpe1.reads.utils.ArrayUtils;
import rysavpe1.reads.utils.IteratorWrapper;
import rysavpe1.reads.utils.MathUtils;
import rysavpe1.reads.utils.Pair;
import rysavpe1.reads.wis.WeightedIntervalScheduling;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class CombinedEmbeddedDistance extends AbstractMeasure<EmbeddedReadBagTuple<int[], int[]>> {

    private static final int SENZITIVITY = 3;
    private final EmbeddedOverlapFinder<int[]> overlapFinder;
    private final ReadFinder<int[], int[]> readFinder;
    private final RegionValue regionValue;
    private final boolean shouldReverse;
    private final boolean shouldComplement;
    private final DistanceCalculator<Sequence, ? extends Number> readDistance;
    private final DistanceCalculator<int[], ? extends Number> readEmbeddedDistance;
    private final DistanceCalculator<Sequence, ? extends Number> originalSpaceContigDistance;
    private final EmbeddedTupleFilter filter;
    private final EmbeddingFunction<Sequence, int[]> contigEmbedding;
    private final int readLength;
    private final double coverage;
    private final int fixedLength;

    public CombinedEmbeddedDistance(int readLength, double coverage, int overlapThreshold, boolean shouldReverse, boolean shouldComplement, int fixedLength) {
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
        this.overlapFinder = new EndReadsOverlapFinder(readLength, new TripletsEmbedding());
        this.originalSpaceContigDistance = new UkkonenDistance();
        this.contigEmbedding = new TripletsIndexVectorEmbedding();
        this.fixedLength = fixedLength;
    }

    @Override
    public Double getDistance(EmbeddedReadBagTuple<int[], int[]> a, EmbeddedReadBagTuple<int[], int[]> b) {
        final int aOriginalReadCount = a.getReads().getBag().size();
        final int bOriginalReadCount = b.getReads().getBag().size();

        final EmbeddedReadBag<int[]> aContigsMS = a.getContigs(); // value1 are the contigs, value2 reads
        final EmbeddedReadBag<int[]> bContigsMS = b.getContigs();
        final ReadBag contigsAOutsideOverlaps = new ReadBag(aContigsMS.size());
        final ReadBag contigsBOutsideOverlaps = new ReadBag(bContigsMS.size());
        int aOverlapLength = 0, bOverlapLength = 0;
        double sumA = 0.0, sumB = 0.0;
        int matchedLensMaxA = 0, matchedLensMaxB = 0;
        List<OverlapAdapterAndSequence> aRegions = new ArrayList<>(bContigsMS.size() * (shouldComplement ? 2 : 1) * (shouldReverse ? 2 : 1));
        Map<EmbeddedSequence<int[]>, List<OverlapAdapterAndSequence>> bRegionsMap = new HashMap<>(bContigsMS.size());
        final int aListSize = aContigsMS.size() * (shouldComplement ? 2 : 1) * (shouldReverse ? 2 : 1);
        for (EmbeddedSequence<int[]> bSeq : new IteratorWrapper<>(bContigsMS.embeddedIterator()))
            bRegionsMap.put(bSeq, new ArrayList<>(aListSize));

        for (EmbeddedSequence<int[]> aSeq : new IteratorWrapper<>(aContigsMS.embeddedIterator())) {
            aRegions.clear();
            for (EmbeddedSequence<int[]> bSeq : new IteratorWrapper<>(bContigsMS.embeddedIterator())) {
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
        for (Entry<EmbeddedSequence<int[]>, List<OverlapAdapterAndSequence>> bRegions : bRegionsMap.entrySet()) {
            final Collection<OverlapAdapterAndSequence> wisSolution = WeightedIntervalScheduling.solve(asArray(bRegions.getValue()));
            for (OverlapAdapterAndSequence region : wisSolution) {
                sumB += originalSpaceContigDistance.getDistance(region.projectA(), region.projectB()).doubleValue();
                matchedLensMaxB += region.getRegion().getLengthMax();
            }
            bOverlapLength += bRegions.getKey().getObject().length() - filter.findNonOverlap(contigsBOutsideOverlaps, wisSolution, bRegions.getKey().getObject());
        }
        final double contigABDistance = aContigsMS.isEmpty() || bContigsMS.isEmpty() ? 0.0 : sumA / matchedLensMaxA;
        final double contigBADistance = aContigsMS.isEmpty() || bContigsMS.isEmpty() ? 0.0 : sumB / matchedLensMaxB;

        final int lengthAEstimate = fixedLength == -1 ? (int) (aOriginalReadCount * readLength / coverage) : fixedLength;
        final int lengthBEstimate = fixedLength == -1 ? (int) (bOriginalReadCount * readLength / coverage) : fixedLength;
//        System.err.println("lenEstimates "+lengthAEstimate +" / "+lengthBEstimate);
        final double percentAInOverlaps = Math.min(((double) aOverlapLength) / lengthAEstimate, 1);
        final double percentBInOverlaps = Math.min(((double) bOverlapLength) / lengthBEstimate, 1);

        a = filter.filterTuple(a);
        b = filter.filterTuple(b);

        final List<EmbeddedSequence<int[]>> aUnmappedReads = a.getReads().toList();
        final List<EmbeddedSequence<int[]>> aContigs = a.getContigs().toList();
        final List<EmbeddedSequence<int[]>> bUnmappedReads = b.getReads().toList();
        final List<EmbeddedSequence<int[]>> bContigs = b.getContigs().toList();
        final List<EmbeddedSequence<int[]>> aContigsNonOverlapL = new EmbeddedReadBag<>(contigsAOutsideOverlaps, contigEmbedding).toList();
        final List<EmbeddedSequence<int[]>> bContigsNonOverlapL = new EmbeddedReadBag<>(contigsBOutsideOverlaps, contigEmbedding).toList();

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

        // for each read find the best matching part of a contig
        for (int i = 0; i < aReadMatches.length; i++)
            for (int j = 0; j < bContigs.size(); j++) {
                final EmbeddedSequence<int[]> read = aUnmappedReads.get(i);
                final EmbeddedSequence<int[]> contig = bContigs.get(j);
                final ReadMatchHolder holder = aReadMatches[i];

                doUnoriented((boolean rev, boolean compl) -> calcReadInContig(read, contig, holder, rev, compl));
            }
        for (int i = 0; i < aContigs.size(); i++)
            for (int j = 0; j < bReadMatches.length; j++) {
                final EmbeddedSequence<int[]> contig = aContigs.get(i);
                final EmbeddedSequence<int[]> read = bUnmappedReads.get(j);
                final ReadMatchHolder holder = bReadMatches[j];
                doUnoriented((boolean rev, boolean compl) -> calcReadInContig(read, contig, holder, rev, compl));
            }

        // for each nonused contig part find the best matching read
        for (int i = 0; i < aReadMatches.length; i++)
            for (int j = 0; j < bContigsBestMatch.length; j++) {
                final EmbeddedSequence<int[]> read = aUnmappedReads.get(i);
                final EmbeddedSequence<int[]> contig = bContigsNonOverlapL.get(j);
                final ContigMatchHolder holder = bContigsBestMatch[j];

                doUnoriented((boolean rev, boolean compl) -> calcReadsForContig(read, contig, holder, rev, compl));
            }
        for (int i = 0; i < aContigsBestMatch.length; i++)
            for (int j = 0; j < bReadMatches.length; j++) {
                final EmbeddedSequence<int[]> read = bUnmappedReads.get(j);
                final EmbeddedSequence<int[]> contig = aContigsNonOverlapL.get(i);
                final ContigMatchHolder holder = aContigsBestMatch[i];

                doUnoriented((boolean rev, boolean compl) -> calcReadsForContig(read, contig, holder, rev, compl));
            }

        // now consolidate the results
        final double[] aReadBestMatch = consolidateReadDistance(aUnmappedReads, aReadMatches);
        final double[] bReadBestMatch = consolidateReadDistance(bUnmappedReads, bReadMatches);

        final Pair<double[], int[]> aContigBestMatch = consolidateContigDistance(aContigsNonOverlapL, aContigsBestMatch);
        final Pair<double[], int[]> bContigBestMatch = consolidateContigDistance(bContigsNonOverlapL, bContigsBestMatch);

        // now calculate average for reads and parts of contigs that do not belong to overlap
        final double readABDist = average(aReadBestMatch, aContigBestMatch) / readLength;
        final double readBADist = average(bReadBestMatch, bContigBestMatch) / readLength;
        System.err.println("contig distance " + contigABDistance + " / " + contigBADistance);
        System.err.println("read distance " + readABDist + " / " + readBADist);
//        System.err.println("best match A "+Arrays.toString(aReadBestMatch) + " , "+Arrays.toString(aContigsBestMatch));
//        System.err.println("best match B "+Arrays.toString(bReadBestMatch) + " , "+Arrays.toString(bContigsBestMatch));
        System.err.println("Percent a in overlaps " + percentAInOverlaps);
        System.err.println("Percent b in overlaps " + percentBInOverlaps);
        System.err.println("a read count : " + aUnmappedReads.size() + " / " + aOriginalReadCount);
        System.err.println("a read count : " + bUnmappedReads.size() + " / " + bOriginalReadCount);
        return MathUtils.average(
                MathUtils.weightedAverageSafeNaN(percentAInOverlaps, contigABDistance, readABDist),
                MathUtils.weightedAverageSafeNaN(percentBInOverlaps, contigBADistance, readBADist)
        ) * Math.max(lengthAEstimate, lengthBEstimate);
//        return MathUtils.average(contigABDistance, contigBADistance) * Math.max(lengthAEstimate, lengthBEstimate);
//        return MathUtils.average(readABDist, readBADist) * Math.max(lengthAEstimate, lengthBEstimate);
    }

    private double average(double[] readDist, Pair<double[], int[]> contigDist) {
        double sum = MathUtils.sum(readDist) + MathUtils.sum(contigDist.value1);
        int count = readDist.length + MathUtils.sum(contigDist.value2);
        return count == 0 ? 0.0 : sum / count;
    }

    private ContigMatchHolder[] initBestMatchListForContigs(List<EmbeddedSequence<int[]>> contigs) {
        final int count = contigs.size();
        final ContigMatchHolder[] bestMatch = new ContigMatchHolder[count];
        for (int i = 0; i < count; i++) {
            final int expectedReads = (int) Math.round(coverage * contigs.get(i).getObject().length() / readLength);
            if (expectedReads == 0)
                bestMatch[i] = ContigMatchHolder.Empty.INSTANCE;
            else
                bestMatch[i] = new ContigMatchHolder(expectedReads * SENZITIVITY);
        }
        return bestMatch;
    }

    private OverlapAdapterAndSequence[] asArray(Collection<OverlapAdapterAndSequence> regions) {
        return regions.toArray(new OverlapAdapterAndSequence[regions.size()]);
    }

    private double[] consolidateReadDistance(List<EmbeddedSequence<int[]>> reads, ReadMatchHolder[] readMatches) {
        final double[] bestDists = ArrayUtils.nTimes(Double.MAX_VALUE, reads.size());
        for (int i = 0; i < bestDists.length; i++) {
            for (ReadMatchHolder.Match match : readMatches[i]) {
                final Sequence seqA = reads.get(i).getObject();
                final Sequence seqB = match.getSequence();
                final double distance = match.isMarginDistance() ? readDistance.getDistance(seqA, seqB).doubleValue()
                        : originalSpaceContigDistance.getDistance(seqA, seqB).doubleValue();
                if (distance < bestDists[i]) bestDists[i] = distance;
            }
        }
        return bestDists;
    }

    private Pair<double[], int[]> consolidateContigDistance(List<EmbeddedSequence<int[]>> contigs, ContigMatchHolder[] contigMatches) {
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

    private void calcOverlap(EmbeddedSequence<int[]> aSeq, EmbeddedSequence<int[]> bSeq, List<OverlapAdapterAndSequence> aRegions, List<OverlapAdapterAndSequence> bRegions, boolean bReversed, boolean bComplemented) {
        EmbeddedSequence<int[]> bSeqRC = bReversed ? bSeq.reverse() : bSeq;
        if (bComplemented) bSeqRC = bSeqRC.complement();
        OverlapRegion region = overlapFinder.getOverlap(aSeq, bSeqRC);
        if (region == null) return;
        if (bReversed) region = region.reverseB(bSeq.getObject().length());
        aRegions.add(OverlapAdapterAndSequence.overlapAdapterFirstSeq(aSeq.getObject(), bSeq.getObject(), region, regionValue, bReversed, bComplemented));
        bRegions.add(OverlapAdapterAndSequence.overlapAdapterSecondSeq(aSeq.getObject(), bSeq.getObject(), region, regionValue, bReversed, bComplemented));
    }

    private void calcReadInContig(EmbeddedSequence<int[]> read, EmbeddedSequence<int[]> contig, ReadMatchHolder holder, boolean contigReversed, boolean contigComplemented) {
        EmbeddedSequence<int[]> contigRC = contigReversed ? contig.reverse() : contig;
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

    private void calcReadsForContig(EmbeddedSequence<int[]> read, EmbeddedSequence<int[]> contig, ContigMatchHolder holder, boolean readReversed, boolean readComplemented) {
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
