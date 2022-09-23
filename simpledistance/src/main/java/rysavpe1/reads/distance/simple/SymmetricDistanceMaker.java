package rysavpe1.reads.distance.simple;

import rysavpe1.reads.distance.AbstractMeasure;
import rysavpe1.reads.utils.MathUtils;

/**
 * @author Petr Ryšavý
 * @param <T>
 */
public class SymmetricDistanceMaker<T> extends AbstractMeasure<T> {

    private final AbstractMeasure<T> distance;

    public SymmetricDistanceMaker(AbstractMeasure<T> distance) {
        this.distance = distance;
    }

    @Override
    public Double getDistance(T a, T b) {
        return MathUtils.average(distance.getDistance(a, b), distance.getDistance(b, a));
    }

    @Override
    public Double getSimilarity(T a, T b) {
        return MathUtils.average(distance.getSimilarity(a, b), distance.getSimilarity(b, a));
    }

    @Override
    public boolean isZeroOneNormalized() {
        return distance.isZeroOneNormalized();
    }

}
