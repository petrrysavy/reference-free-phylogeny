package rysavpe1.reads.margingap;

import rysavpe1.reads.distance.DistanceCalculator;
import rysavpe1.reads.distance.simple.DecoratedDistance;

/**
 * Decorator that thresholds the similarity. If dissimilarity is greater than a
 * threshold, we consider the values to be completely different.
 *
 * @author Petr Ryšavý
 * @param <T> The type of compared objects.
 */
public class DistanceThreshold<T> extends DecoratedDistance<T> {

    /** Threshold for comparing. */
    private final double threshold;
    /** If distance is greater than threshold, this will be returned. */
    private final double maxCost;

    /**
     * Thresholding distance.
     * @param innerDistance This distance is decorated.
     * @param threshold Threshold for comparing.
     * @param maxCost If distance is greater than threshold, this will be
     * returned.
     */
    public DistanceThreshold(DistanceCalculator<T, ? extends Number> innerDistance, double threshold, double maxCost) {
        super(innerDistance);
        this.threshold = threshold;
        this.maxCost = maxCost;
    }

    @Override
    public Double getDistance(T a, T b) {
        final double distance = super.getDistance(a, b);
        return distance >= threshold ? maxCost : distance;
    }
}
