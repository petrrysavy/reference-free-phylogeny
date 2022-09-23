package rysavpe1.reads.overlap;

/**
 *
 * @author petr
 */
public interface RegionValue {

    public RegionValue LENGTH = (OverlapRegion region) -> region.getLengthMax();
    public RegionValue LENGTH_OVER_DISTANCE = (OverlapRegion region) -> 1.0 / region.distanceToLengthRatio();
    public RegionValue ONE_OVER_DISTANCE = (OverlapRegion region) -> 1.0 / region.getDistance();

    public double getWeight(OverlapRegion region);
}
