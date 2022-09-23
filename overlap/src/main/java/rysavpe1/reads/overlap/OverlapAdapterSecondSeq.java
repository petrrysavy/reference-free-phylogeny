package rysavpe1.reads.overlap;

/**
 *
 * @author petr
 */
public class OverlapAdapterSecondSeq extends OverlapAdapter {

    public OverlapAdapterSecondSeq(OverlapRegion region, RegionValue value) {
        super(region, value.getWeight(region));
    }

    @Override
    public int getEnd() {
        return region.getEndB();
    }

    @Override
    public int getStart() {
        return region.getStartB();
    }

}
