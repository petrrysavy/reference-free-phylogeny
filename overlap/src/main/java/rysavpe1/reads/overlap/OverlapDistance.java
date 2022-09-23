package rysavpe1.reads.overlap;

import java.util.ArrayList;
import rysavpe1.reads.distance.simple.AbstractMultisetDistance;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.multiset.Multiset;
import rysavpe1.reads.wis.WeightedIntervalScheduling;

/**
 *
 * @author petr
 */
public class OverlapDistance extends AbstractMultisetDistance<Sequence> {

    private final OverlapFinder overlap;
    private final RegionValue weight;
    private final boolean shouldReverse;
    private final boolean shouldComplement;

    public OverlapDistance(OverlapFinder overlap, RegionValue weight, boolean shouldReverse, boolean shouldComplement) {
        super(null);
        this.overlap = overlap;
        this.weight = weight;
        this.shouldReverse = shouldReverse;
        this.shouldComplement = shouldComplement;
    }

    @Override
    public Double getDistance(Multiset<Sequence> a, Multiset<Sequence> b) {
        double sumA = 0.0;
        int matchedLensA = 0;
//        int matchedLensAB = 0;
        OverlapAdapterList regions = new OverlapAdapterList(b.size() * (shouldComplement ? 2 : 1) * (shouldReverse ? 2 : 1));
        for (Sequence aSeq : a) {
            regions.clear();
            for (Sequence bSeq : b) {
                regions.add(overlap.getOverlap(aSeq, bSeq));

                if (shouldComplement) {
                    regions.add(overlap.getOverlap(aSeq, bSeq.reverseComplement()));
                    if (shouldReverse)
                        regions.add(overlap.getOverlap(aSeq, bSeq.complement()));
                }
                if (shouldReverse)
                    regions.add(overlap.getOverlap(aSeq, bSeq.reverse()));
            }
            for (OverlapAdapter region : WeightedIntervalScheduling.solve(regions.toArray(new OverlapAdapterFirstSeq[regions.size()]))) {
                sumA += region.getRegion().getDistance();
                matchedLensA += region.getRegion().getLengthMax();
//                matchedLensA += region.getRegion().getLengthA();
//                matchedLensAB += region.getRegion().getLengthB();
            }
        }
        return sumA / matchedLensA;
    }

    private class OverlapAdapterList extends ArrayList<OverlapAdapterFirstSeq> {

        public OverlapAdapterList(int initialCapacity) {
            super(initialCapacity);
        }

        public OverlapAdapterList() {
        }

        public void add(OverlapRegion e) {
            if (e != null)
                super.add(new OverlapAdapterFirstSeq(e, weight));
        }
    }
}
