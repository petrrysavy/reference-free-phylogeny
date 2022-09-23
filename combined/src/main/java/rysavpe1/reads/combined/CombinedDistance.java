package rysavpe1.reads.combined;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import rysavpe1.reads.distance.AbstractMeasure;
import rysavpe1.reads.distance.DistanceCalculator;
import rysavpe1.reads.distance.simple.DistanceUtils;
import rysavpe1.reads.margingap.LinearMarginGapPenalty;
import rysavpe1.reads.margingap.MarginGapEditDistance;
import rysavpe1.reads.margingap.MarginGapPenaulty;
import rysavpe1.reads.model.ReadBag;
import rysavpe1.reads.model.ReadBagTuple;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.overlap.EditDistanceOverlapFinder;
import rysavpe1.reads.overlap.OverlapAdapter;
import rysavpe1.reads.overlap.OverlapAdapterFirstSeq;
import rysavpe1.reads.overlap.OverlapAdapterSecondSeq;
import rysavpe1.reads.overlap.OverlapFinder;
import rysavpe1.reads.overlap.OverlapRegion;
import rysavpe1.reads.overlap.RegionValue;
import rysavpe1.reads.utils.ArrayUtils;
import rysavpe1.reads.utils.MathUtils;
import rysavpe1.reads.utils.NotNullArrayList;
import rysavpe1.reads.utils.TopNList;
import rysavpe1.reads.wis.WeightedIntervalScheduling;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class CombinedDistance extends AbstractMeasure<ReadBagTuple> {

    private final OverlapFinder overlapFinder;
    private final RegionValue regionValue;
    private final boolean shouldReverse;
    private final boolean shouldComplement;
    private final DistanceCalculator<Sequence, ? extends Number> readDistance;
    private final DistanceCalculator<Sequence, ? extends Number> readContigDistance;
    private final TupleFilter filter;
    private final int readLength;
    private final double coverage;
    private final int fixedLength;

    public CombinedDistance(int readLength, double coverage, int overlapThreshold, boolean shouldReverse, boolean shouldComplement, int fixedLength) {
        this.readLength = readLength;
        this.coverage = coverage;
//        this.contigDistance = new SymmetricOverlapDistance(new EditDistanceOverlapFinder(overlapThreshold), RegionValue.LENGTH_OVER_DISTANCE, shouldReverse, shouldComplement);
        this.overlapFinder = new EditDistanceOverlapFinder(overlapThreshold);
        this.regionValue = RegionValue.LENGTH_OVER_DISTANCE;
        this.shouldReverse = shouldReverse;
        this.shouldComplement = shouldComplement;
        final MarginGapPenaulty gap = new LinearMarginGapPenalty(coverage, readLength, 2);
        this.readDistance = DistanceUtils.getOrientedDistance(shouldReverse, shouldComplement, new MarginGapEditDistance(0, 1, 1, gap));
        this.readContigDistance = DistanceUtils.getOrientedDistance(shouldReverse, shouldComplement, new ContigMarginGapEditDistance(0, 1, 1, gap));
        this.filter = new TupleFilter(readLength);
        this.fixedLength = fixedLength;
    }

    @Override
    public Double getDistance(ReadBagTuple a, ReadBagTuple b) {
        final int aOriginalReadCount = a.getReads().size();
        final int bOriginalReadCount = b.getReads().size();

//        final double contigDist = contigDistance.getDistance(a, b);
        final ReadBag aContigsMS = a.getContigs(); // value1 are the contigs, value2 reads
        final ReadBag bContigsMS = b.getContigs();
        final ReadBag contigsAOutsideOverlaps = new ReadBag(aContigsMS.size());
        final ReadBag contigsBOutsideOverlaps = new ReadBag(bContigsMS.size());
        int aOverlapLength = 0, bOverlapLength = 0;
        double sumA = 0.0, sumB = 0.0;
        int matchedLensMaxA = 0, matchedLensMaxB = 0;
        List<OverlapAdapter> aRegions = new NotNullArrayList<>(bContigsMS.size() * (shouldComplement ? 2 : 1) * (shouldReverse ? 2 : 1));
        List<OverlapRegion> regions = new NotNullArrayList<>(4);
        Map<Sequence, List<OverlapAdapter>> bRegionsMap = new HashMap<>(bContigsMS.size());
        final int aListSize = aContigsMS.size() * (shouldComplement ? 2 : 1) * (shouldReverse ? 2 : 1);
        for (Sequence bSeq : bContigsMS)
            bRegionsMap.put(bSeq, new NotNullArrayList<>(aListSize));
        
        for (Sequence aSeq : aContigsMS) {
            aRegions.clear();
            for (Sequence bSeq : bContigsMS) {
                regions.clear();
                regions.add(overlapFinder.getOverlap(aSeq, bSeq));

                if (shouldComplement) {
                    regions.add(overlapFinder.getOverlap(aSeq, bSeq.reverseComplement()));
                    if (shouldReverse)
                        regions.add(overlapFinder.getOverlap(aSeq, bSeq.complement()));
                }
                if (shouldReverse)
                    regions.add(overlapFinder.getOverlap(aSeq, bSeq.reverse()));

                final List<OverlapAdapter> bRegions = bRegionsMap.get(bSeq);
                for (OverlapRegion reg : regions) {
                    aRegions.add(new OverlapAdapterFirstSeq(reg, regionValue));
                    bRegions.add(new OverlapAdapterSecondSeq(reg, regionValue));
                }
            }
            final Collection<OverlapAdapter> wisSolution = WeightedIntervalScheduling.solve(asArray(aRegions));
            for (OverlapAdapter region : wisSolution) {
                sumA += region.getRegion().getDistance();
                matchedLensMaxA += region.getRegion().getLengthMax();
            }
            aOverlapLength += aSeq.length() - filter.findNonOverlap(contigsAOutsideOverlaps, wisSolution, aSeq);
        }
        for (Entry<Sequence, List<OverlapAdapter>> bRegions : bRegionsMap.entrySet()) {
            final Collection<OverlapAdapter> wisSolution = WeightedIntervalScheduling.solve(asArray(bRegions.getValue()));
            for (OverlapAdapter region : wisSolution) {
                sumB += region.getRegion().getDistance();
                matchedLensMaxB += region.getRegion().getLengthMax();
            }
            bOverlapLength += bRegions.getKey().length() - filter.findNonOverlap(contigsBOutsideOverlaps, wisSolution, bRegions.getKey());
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

        final List<Sequence> aUnmappedReads = new ArrayList<>(a.getReads().toSet());
        final List<Sequence> aContigs = new ArrayList<>(a.getContigs().toSet());
        final List<Sequence> bUnmappedReads = new ArrayList<>(b.getReads().toSet());
        final List<Sequence> bContigs = new ArrayList<>(b.getContigs().toSet());
        final List<Sequence> aContigsNonOverlapL = new ArrayList<>(contigsAOutsideOverlaps.toSet());
        final List<Sequence> bContigsNonOverlapL = new ArrayList<>(contigsBOutsideOverlaps.toSet());
        
//        System.err.println("unmappedReads "+aUnmappedReads + " / "+bUnmappedReads);
//        System.err.println("contigsNonOverlap "+aContigsNonOverlapL + " / "+bContigsNonOverlapL);

        final double[] aReadBestMatch = ArrayUtils.nTimes(Double.MAX_VALUE, aUnmappedReads.size());
        final DoubleTopNList[] aContigsBestMatch = initBestMatchListForContigs(aContigsNonOverlapL);
        final double[] bReadBestMatch = ArrayUtils.nTimes(Double.MAX_VALUE, bUnmappedReads.size());
        final DoubleTopNList[] bContigsBestMatch = initBestMatchListForContigs(bContigsNonOverlapL);

        // each pair of reads
        for (int i = 0; i < aReadBestMatch.length; i++)
            for (int j = 0; j < bReadBestMatch.length; j++) {
                final double distance = readDistance.getDistance(aUnmappedReads.get(i), bUnmappedReads.get(j)).doubleValue();
                if (distance < aReadBestMatch[i]) aReadBestMatch[i] = distance;
                if (distance < bReadBestMatch[j]) bReadBestMatch[j] = distance;
            }

        // for each read find the best matching part of a contig
        for (int i = 0; i < aReadBestMatch.length; i++)
            for (int j = 0; j < bContigs.size(); j++) {
                final double distance = readContigDistance.getDistance(aUnmappedReads.get(i), bContigs.get(j)).doubleValue();
                if (distance < aReadBestMatch[i]) aReadBestMatch[i] = distance;
            }
        for (int i = 0; i < aContigs.size(); i++)
            for (int j = 0; j < bReadBestMatch.length; j++) {
                final double distance = readContigDistance.getDistance(aContigs.get(i), bUnmappedReads.get(j)).doubleValue();
                if (distance < bReadBestMatch[j]) bReadBestMatch[j] = distance;
            }

        // for each nonused contig part find the best matching read
        for (int i = 0; i < aReadBestMatch.length; i++)
            for (int j = 0; j < bContigsBestMatch.length; j++) {
                final double distance = readContigDistance.getDistance(aUnmappedReads.get(i), bContigsNonOverlapL.get(j)).doubleValue();
                bContigsBestMatch[j].add(distance);
            }
        for (int i = 0; i < aContigsBestMatch.length; i++)
            for (int j = 0; j < bReadBestMatch.length; j++) {
                final double distance = readContigDistance.getDistance(aContigsNonOverlapL.get(i), bUnmappedReads.get(j)).doubleValue();
                aContigsBestMatch[i].add(distance);
            }
//        // for each read and contig
//        for (int i = 0; i < aReadBestMatch.length; i++)
//            for (int j = 0; j < bContigsBestMatch.length; j++) {
//                final double distance = readContigDistance.getDistance(aReads.get(i), bContigs.get(j)).doubleValue();
//                if (distance < aReadBestMatch[i]) aReadBestMatch[i] = distance;
//                bContigsBestMatch[j].add(distance);
//            }
//        for (int i = 0; i < aContigsBestMatch.length; i++)
//            for (int j = 0; j < bReadBestMatch.length; j++) {
//                final double distance = readContigDistance.getDistance(aContigs.get(i), bReads.get(j)).doubleValue();
//                aContigsBestMatch[i].add(distance);
//                if (distance < bReadBestMatch[j]) bReadBestMatch[j] = distance;
//            }

        // now calculate average for reads and parts of contigs that do not belong to overlap
        final double readABDist = average(aReadBestMatch, aContigsBestMatch) / readLength;
        final double readBADist = average(bReadBestMatch, bContigsBestMatch) / readLength;
        System.err.println("contig distance "+contigABDistance + " / "+contigBADistance);
        System.err.println("read distance "+readABDist + " / "+readBADist);
//        System.err.println("best match A "+Arrays.toString(aReadBestMatch) + " , "+Arrays.toString(aContigsBestMatch));
//        System.err.println("best match B "+Arrays.toString(bReadBestMatch) + " , "+Arrays.toString(bContigsBestMatch));
        System.err.println("Percent a in overlaps "+percentAInOverlaps);
        System.err.println("Percent b in overlaps "+percentBInOverlaps);
        System.err.println("a read count : "+aUnmappedReads.size() + " / "+aOriginalReadCount);
        System.err.println("a read count : "+bUnmappedReads.size() + " / "+bOriginalReadCount);
        return MathUtils.average(
            MathUtils.weightedAverage(percentAInOverlaps, contigABDistance, readABDist),
            MathUtils.weightedAverage(percentBInOverlaps, contigBADistance, readBADist)
        ) * Math.max(lengthAEstimate, lengthBEstimate);
//        return MathUtils.average(contigABDistance, contigBADistance) * Math.max(lengthAEstimate, lengthBEstimate);
    }

    private double average(double[] readDist, DoubleTopNList[] contigDist) {
        double sum = MathUtils.sum(readDist);
        int count = readDist.length;
        for (DoubleTopNList list : contigDist) {
            sum += MathUtils.sum(list);
            count += list.size();
        }
        return count == 0 ? 0.0 : sum / count;
    }

    private DoubleTopNList[] initBestMatchListForContigs(List<Sequence> contigs) {
        final int count = contigs.size();
        final DoubleTopNList[] bestMatch = new DoubleTopNList[count];
        for (int i = 0; i < count; i++) {
            final int expectedReads = (int) Math.round(coverage * contigs.get(i).length() / readLength);
            if(expectedReads == 0)
                bestMatch[i] = new EmptyTopNList();
            else
                bestMatch[i] = new DoubleTopNList(expectedReads, Comparator.reverseOrder());
        }
        return bestMatch;
    }

    private OverlapAdapter[] asArray(Collection<OverlapAdapter> regions) {
        return regions.toArray(new OverlapAdapter[regions.size()]);
    }

    private static class DoubleTopNList extends TopNList<Double> {

        public DoubleTopNList(int capacity) {
            super(capacity);
        }

        public DoubleTopNList(int capacity, Comparator<? super Double> comparator) {
            super(capacity, comparator);
        }

//        @Override
//        public boolean add(Double e) {
////            System.err.println("top n list add "+e);
//            return super.add(e); //To change body of generated methods, choose Tools | Templates.
//        }
    }
    
    private static final class EmptyTopNList extends DoubleTopNList {
        
        public EmptyTopNList() {
            super(1);
        }

        @Override
        public int size() {
            return super.size(); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Iterator<Double> iterator() {
            return Collections.emptyIterator();
        }

        @Override
        public boolean add(Double e) {
            return false;
        }
        
    }
}
