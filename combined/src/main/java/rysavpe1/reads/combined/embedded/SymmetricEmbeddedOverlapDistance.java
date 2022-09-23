package rysavpe1.reads.combined.embedded;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rysavpe1.reads.distance.AbstractMeasure;
import rysavpe1.reads.distance.DistanceCalculator;
import rysavpe1.reads.embedded.EmbeddedReadBagTuple;
import rysavpe1.reads.embedded.EmbeddedReadBag;
import rysavpe1.reads.embedded.EmbeddedSequence;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.utils.MathUtils;
import rysavpe1.reads.overlap.OverlapAdapterAndSequence;
import rysavpe1.reads.overlap.OverlapRegion;
import rysavpe1.reads.overlap.RegionValue;
import rysavpe1.reads.utils.IteratorWrapper;
import rysavpe1.reads.wis.WeightedIntervalScheduling;

/**
 *
 * @author petr
 * @param <T>
 * @param <U>
 */
public class SymmetricEmbeddedOverlapDistance<T, U> extends AbstractMeasure<EmbeddedReadBagTuple<T, U>> {

    private final EmbeddedOverlapFinder<T> overlap;
    private final RegionValue weight;
    private final DistanceCalculator<Sequence, ? extends Number> originalSpaceDistance;
    private final boolean shouldReverse;
    private final boolean shouldComplement;

    public SymmetricEmbeddedOverlapDistance(EmbeddedOverlapFinder<T> overlap, RegionValue weight,
            DistanceCalculator<Sequence, ? extends Number> originalSpaceDistance,
            boolean shouldReverse, boolean shouldComplement) {
        this.overlap = overlap;
        this.weight = weight;
        this.shouldReverse = shouldReverse;
        this.shouldComplement = shouldComplement;
        this.originalSpaceDistance = originalSpaceDistance;
    }

    @Override
    public Double getDistance(EmbeddedReadBagTuple<T, U> aTuple, EmbeddedReadBagTuple<T, U> bTuple) {
        final EmbeddedReadBag<T> a = aTuple.getContigs();
        final EmbeddedReadBag<T> b = bTuple.getContigs();
//        System.err.println("aContigs " + a.size() + " b con " + b.size() + " along " + a.getBag().longestSequence().length());
//        System.err.println("areads " + aTuple.getReads().size() + " b reads " + bTuple.getReads().size() + " along " + aTuple.getReads().getBag().longestSequence().length());
        double sumA = 0.0, sumB = 0.0;
        int matchedLensA = 0, matchedLensB = 0;
        List<OverlapAdapterAndSequence> aRegions = new ArrayList<>(b.size() * (shouldComplement ? 2 : 1) * (shouldReverse ? 2 : 1));
        Map<EmbeddedSequence<T>, List<OverlapAdapterAndSequence>> bRegionsMap = new HashMap<>(b.size());
        final int aListSize = a.size() * (shouldComplement ? 2 : 1) * (shouldReverse ? 2 : 1);
        for (EmbeddedSequence<T> bSeq : new IteratorWrapper<>(b.embeddedIterator()))
            bRegionsMap.put(bSeq, new ArrayList<>(aListSize));
        for (EmbeddedSequence<T> aSeq : new IteratorWrapper<>(a.embeddedIterator())) {
            aRegions.clear();
            for (EmbeddedSequence<T> bSeq : new IteratorWrapper<>(b.embeddedIterator())) {
                final List<OverlapAdapterAndSequence> bRegions = bRegionsMap.get(bSeq);
                calcOverlap(aSeq, bSeq, aRegions, bRegions, false, false);
//                tmpRegion = overlap.getOverlap(aSeq, bSeq));

                if (shouldComplement) {
                    calcOverlap(aSeq, bSeq, aRegions, bRegions, true, true);
//                    regions.add(overlap.getOverlap(aSeq, bSeq.reverseComplement()));
                    if (shouldReverse)
                        //                        regions.add(overlap.getOverlap(aSeq, bSeq.complement()));
                        calcOverlap(aSeq, bSeq, aRegions, bRegions, false, true);
                }
                if (shouldReverse)
                    //                    regions.add(overlap.getOverlap(aSeq, bSeq.reverse()));
                    calcOverlap(aSeq, bSeq, aRegions, bRegions, true, false);

                /*
                for (OverlapRegion region : regions) {
                    aRegions.add(OverlapAdapterAndSequence.overlapAdapterFirstSeq(aSeq.getObject(), bSeq.getObject(), region, weight));
                    bRegions.add(OverlapAdapterAndSequence.overlapAdapterSecondSeq(aSeq.getObject(), bSeq.getObject(), region, weight));
                    if (aTuple.getReads().size() == 188 && bTuple.getReads().size() == 272 && aSeq.getObject().length() == aTuple.getReads().getBag().longestSequence().length() && bSeq.getObject().length() == bTuple.getReads().getBag().longestSequence().length()) {
                        System.err.println("aSeq " + aSeq.getObject().getSequenceString());
                        System.err.println("bSeq " + bSeq.getObject().getSequenceString());
                        System.err.println("region "+region);
                        System.err.println("asFirst "+OverlapAdapterAndSequence.overlapAdapterFirstSeq(aSeq.getObject(), bSeq.getObject(), region, weight));
                        System.err.println("firstProj "+OverlapAdapterAndSequence.overlapAdapterFirstSeq(aSeq.getObject(), bSeq.getObject(), region, weight).projectA());
                        System.err.println("secProj "+OverlapAdapterAndSequence.overlapAdapterFirstSeq(aSeq.getObject(), bSeq.getObject(), region, weight).projectB());
                        final OverlapAdapterAndSequence firstadpt = OverlapAdapterAndSequence.overlapAdapterFirstSeq(aSeq.getObject(), bSeq.getObject(), region, weight);
                        System.err.println("dist "+region.getDistance());
                        System.err.println("projDist "+originalSpaceDistance.getDistance(firstadpt.projectA(), firstadpt.projectB()));
                        System.err.println("projDist "+new EditDistance().getDistance(firstadpt.projectA(), firstadpt.projectB()));
                    }
                }*/
            }
            for (OverlapAdapterAndSequence region : WeightedIntervalScheduling.solve(asArray(aRegions))) {
                sumA += originalSpaceDistance.getDistance(region.projectA(), region.projectB()).doubleValue();
//                sumA += region.getRegion().getDistance();
                matchedLensA += region.getRegion().getLengthMax();
            }
        }
        for (List<OverlapAdapterAndSequence> bRegions : bRegionsMap.values())
            for (OverlapAdapterAndSequence region : WeightedIntervalScheduling.solve(asArray(bRegions))) {
                sumB += originalSpaceDistance.getDistance(region.projectA(), region.projectB()).doubleValue();
//                sumB += region.getRegion().getDistance();
                matchedLensB += region.getRegion().getLengthMax();
            }
        final int lenA = aTuple.getReads().size();
        final int lenB = bTuple.getReads().size();
//        System.err.println("sumA " + sumA + " matchA " + matchedLensA + " sumB " + sumB + " matchB " + matchedLensB + "lenA " + lenA + " lB " + lenB);
        return MathUtils.average(sumA / matchedLensA, sumB / matchedLensB) * Math.max(lenA, lenB);
    }

    private OverlapAdapterAndSequence[] asArray(Collection<OverlapAdapterAndSequence> regions) {
        return regions.toArray(new OverlapAdapterAndSequence[regions.size()]);
    }

    private void calcOverlap(EmbeddedSequence<T> aSeq, EmbeddedSequence<T> bSeq, List<OverlapAdapterAndSequence> aRegions, List<OverlapAdapterAndSequence> bRegions, boolean bReversed, boolean bComplemented) {
        EmbeddedSequence<T> bSeqRC = bReversed ? bSeq.reverse() : bSeq;
        if (bComplemented) bSeqRC = bSeqRC.complement();
        OverlapRegion region = overlap.getOverlap(aSeq, bSeqRC);
        if (region == null) return;
        if (bReversed) region = region.reverseB(bSeq.getObject().length());
        aRegions.add(OverlapAdapterAndSequence.overlapAdapterFirstSeq(aSeq.getObject(), bSeq.getObject(), region, weight, bReversed, bComplemented));
        bRegions.add(OverlapAdapterAndSequence.overlapAdapterSecondSeq(aSeq.getObject(), bSeq.getObject(), region, weight, bReversed, bComplemented));
    }
}
