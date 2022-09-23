package rysavpe1.reads.overlap;

import rysavpe1.reads.wis.Interval;

/**
 *
 * @author petr
 */
public abstract class OverlapAdapter implements Interval {

    protected final OverlapRegion region;
    protected final double value;

    public OverlapAdapter(OverlapRegion region, double value) {
        this.region = region;
        this.value = value;
    }

    @Override
    public abstract int getEnd();

    @Override
    public abstract int getStart();

    @Override
    public final double getValue() {
        return value;
    }

    public final OverlapRegion getRegion() {
        return region;
    }

    @Override
    public String toString() {
        return "OverlapAdapter{start=" + getStart() + ", end=" + getEnd() + '}';
    }

}
