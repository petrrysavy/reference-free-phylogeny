package rysavpe1.reads.overlap;

import java.util.ArrayList;
import java.util.Collection;
import rysavpe1.reads.distance.simple.AbstractMultisetDistance;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.multiset.Multiset;
import rysavpe1.reads.wis.WeightedIntervalScheduling;

/**
 *
 * @author petr
 */
public class OverlapMongeElkan extends AbstractMultisetDistance<Sequence> {

    private final OverlapFinder overlap;
    private final RegionValue weight;
    private final boolean shouldReverse;
    private final boolean shouldComplement;

    public OverlapMongeElkan(OverlapFinder overlap, RegionValue weight, boolean shouldReverse, boolean shouldComplement) {
        super(null);
        this.overlap = overlap;
        this.weight = weight;
        this.shouldReverse = shouldReverse;
        this.shouldComplement = shouldComplement;
    }

    @Override
    public Double getDistance(Multiset<Sequence> a, Multiset<Sequence> b) {
        double sum = 0.0;
        int matchedLens = 0;
        OverlapAdapterList regions = new OverlapAdapterList();
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
            Collection<OverlapAdapterFirstSeq> solution = WeightedIntervalScheduling.solve(regions.toArray(new OverlapAdapterFirstSeq[regions.size()]));
            for (OverlapAdapterFirstSeq region : solution) {
                sum += region.getRegion().distanceToLengthRatio();
                matchedLens += 1;//region.getRegion().getLengthA();
            }
        }
        return sum / matchedLens;
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
