package rysavpe1.reads.overlap;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import rysavpe1.reads.distance.AbstractMeasure;
import rysavpe1.reads.model.ReadBagTuple;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.utils.MathUtils;
import rysavpe1.reads.multiset.Multiset;
import rysavpe1.reads.wis.WeightedIntervalScheduling;

/**
 *
 * @author petr
 */
public class SymmetricOverlapDistance extends AbstractMeasure<ReadBagTuple> {

    private final OverlapFinder overlap;
    private final RegionValue weight;
    private final boolean shouldReverse;
    private final boolean shouldComplement;

    public SymmetricOverlapDistance(OverlapFinder overlap, RegionValue weight, boolean shouldReverse, boolean shouldComplement) {
        this.overlap = overlap;
        this.weight = weight;
        this.shouldReverse = shouldReverse;
        this.shouldComplement = shouldComplement;
    }

    @Override
    public Double getDistance(ReadBagTuple aTuple, ReadBagTuple bTuple) {
        final Multiset<Sequence> a = aTuple.getContigs();
        final Multiset<Sequence> b = bTuple.getContigs();
        double sumA = 0.0, sumB = 0.0;
        int matchedLensA = 0, matchedLensB = 0;
        List<OverlapAdapter> aRegions = new ArrayList<>(b.size() * (shouldComplement ? 2 : 1) * (shouldReverse ? 2 : 1));
        Map<Sequence, List<OverlapAdapter>> bRegionsMap = new HashMap<>(b.size());
        final int aListSize = a.size() * (shouldComplement ? 2 : 1) * (shouldReverse ? 2 : 1);
        for (Sequence bSeq : b)
            bRegionsMap.put(bSeq, new ArrayList<>(aListSize));
        for (Sequence aSeq : a) {
            aRegions.clear();
            for (Sequence bSeq : b) {
                final List<OverlapAdapter> bRegions = bRegionsMap.get(bSeq);
                calcOverlap(aSeq, bSeq, aRegions, bRegions, false);

                if (shouldComplement) {
                    calcOverlap(aSeq, bSeq.reverseComplement(), aRegions, bRegions, true);
                    if (shouldReverse)
                        calcOverlap(aSeq, bSeq.complement(), aRegions, bRegions, false);
                }
                if (shouldReverse)
                    calcOverlap(aSeq, bSeq.reverse(), aRegions, bRegions, true);
            }
            for (OverlapAdapter region : WeightedIntervalScheduling.solve(asArray(aRegions))) {
                sumA += region.getRegion().getDistance();
                matchedLensA += region.getRegion().getLengthMax();
            }
        }
        for (List<OverlapAdapter> bRegions : bRegionsMap.values())
            for (OverlapAdapter region : WeightedIntervalScheduling.solve(asArray(bRegions))) {
                sumB += region.getRegion().getDistance();
                matchedLensB += region.getRegion().getLengthMax();
            }
        final int lenA = aTuple.getReads().size();
        final int lenB = bTuple.getReads().size();
//        System.err.println("sumA "+sumA+" matchA "+matchedLensA + " sumB "+sumB + " matchB "+matchedLensB + "lenA "+lenA + " lB "+lenB);
        return MathUtils.average(sumA / matchedLensA, sumB / matchedLensB) * Math.max(lenA, lenB);
    }

    private OverlapAdapter[] asArray(Collection<OverlapAdapter> regions) {
        return regions.toArray(new OverlapAdapter[regions.size()]);
    }
    
    private void calcOverlap(Sequence aSeq, Sequence bSeq, List<OverlapAdapter> aRegions, List<OverlapAdapter> bRegions, boolean bReversed) {
        OverlapRegion region = overlap.getOverlap(aSeq, bSeq);
        if(region == null) return;
        if(bReversed) region = region.reverseB(bSeq.length());
        aRegions.add(new OverlapAdapterFirstSeq(region, weight));
        bRegions.add(new OverlapAdapterSecondSeq(region, weight));
    }
}
