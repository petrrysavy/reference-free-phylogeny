package rysavpe1.reads.overlap;

/**
 *
 * @author petr
 */
public class OverlapAdapterFirstSeq extends OverlapAdapter {

    public OverlapAdapterFirstSeq(OverlapRegion region, RegionValue value) {
        super(region, value.getWeight(region));
    }

    @Override
    public int getEnd() {
        return region.getEndA();
    }

    @Override
    public int getStart() {
        return region.getStartA();
    }
}
