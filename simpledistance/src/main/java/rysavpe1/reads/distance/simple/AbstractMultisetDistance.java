package rysavpe1.reads.distance.simple;

import rysavpe1.reads.distance.AbstractMeasure;
import rysavpe1.reads.distance.DistanceCalculator;
import rysavpe1.reads.multiset.Multiset;

/**
 * Base class for distances based on Monge-Elkan distance.
 *
 * @author Petr Ryšavý
 * @param <T> The type of objects that are compared in multisets.
 */
public abstract class AbstractMultisetDistance<T> extends AbstractMeasure<Multiset<T>> {

    /** The distance used for comparing objects within mltisets. */
    protected final DistanceCalculator<T, ? extends Number> innerDistance;

    public AbstractMultisetDistance(DistanceCalculator<T, ? extends Number> innerDistance) {
        this.innerDistance = innerDistance;
    }

    @Override
    public abstract Double getDistance(Multiset<T> a, Multiset<T> b);

    @Override
    public boolean isSymmetric() {
        return false;
    }

    @Override
    public boolean isZeroOneNormalized() {
        return innerDistance.isZeroOneNormalized();
    }
}

