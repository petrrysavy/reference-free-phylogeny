package rysavpe1.reads.overlap;

import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.utils.Tuple;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public abstract class OverlapAdapterAndSequence extends OverlapAdapter {

    private final Sequence sequenceA;
    private final Sequence sequenceB;
    private final boolean bReversed;
    private final boolean bComplemented;

    public OverlapAdapterAndSequence(Sequence sequenceA, Sequence sequenceB, OverlapRegion region, RegionValue value, boolean bReversed, boolean bComplemented) {
        this(sequenceA, sequenceB, region, value.getWeight(region), bReversed, bComplemented);
    }

    public OverlapAdapterAndSequence(Sequence sequenceA, Sequence sequenceB, OverlapRegion region, double value, boolean bReversed, boolean bComplemented) {
        super(region, value);
        this.sequenceA = sequenceA;
        this.sequenceB = sequenceB;
        this.bReversed = bReversed;
        this.bComplemented = bComplemented;
    }

    public Tuple<Sequence> subsequences() {
        return new Tuple<>(sequenceA.subSequence(region.getStartA(), region.getEndA()),
                projectB());
    }

    public Sequence projectA() {
        return sequenceA.subSequence(region.getStartA(), region.getEndA());
    }

    public Sequence projectB() {
        Sequence b = sequenceB.subSequence(region.getStartB(), region.getEndB());
        if(bReversed) b = b.reverse();
        if(bComplemented) b = b.complement();
        return b;
    }

    public static OverlapAdapterAndSequence overlapAdapterFirstSeq(Sequence sequenceA, Sequence sequenceB, OverlapRegion region, RegionValue value, boolean bReversed, boolean bComplemented) {
        return overlapAdapterFirstSeq(sequenceA, sequenceB, region, value.getWeight(region), bReversed, bComplemented);
    }

    public static OverlapAdapterAndSequence overlapAdapterFirstSeq(Sequence sequenceA, Sequence sequenceB, OverlapRegion region, double value, boolean bReversed, boolean bComplemented) {
        if(region == null)
            return null;
        
        return new OverlapAdapterAndSequence(sequenceA, sequenceB, region, value, bReversed, bComplemented) {
            @Override
            public int getEnd() {
                return region.getEndA();
            }

            @Override
            public int getStart() {
                return region.getStartA();
            }
        };
    }

    public static OverlapAdapterAndSequence overlapAdapterSecondSeq(Sequence sequenceA, Sequence sequenceB, OverlapRegion region, RegionValue value, boolean bReversed, boolean bComplemented) {
        return overlapAdapterSecondSeq(sequenceA, sequenceB, region, value.getWeight(region), bReversed, bComplemented);
    }

    public static OverlapAdapterAndSequence overlapAdapterSecondSeq(Sequence sequenceA, Sequence sequenceB, OverlapRegion region, double value, boolean bReversed, boolean bComplemented) {
        if(region == null)
            return null;
        
        return new OverlapAdapterAndSequence(sequenceA, sequenceB, region, value, bReversed, bComplemented) {
            @Override
            public int getEnd() {
                return region.getEndB();
            }

            @Override
            public int getStart() {
                return region.getStartB();
            }
        };
    }
}
