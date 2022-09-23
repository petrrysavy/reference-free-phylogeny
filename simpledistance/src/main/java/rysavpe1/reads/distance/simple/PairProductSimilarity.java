package rysavpe1.reads.distance.simple;

import rysavpe1.reads.distance.AbstractMeasure;
import rysavpe1.reads.utils.Tuple;

/**
 *
 * @author petr
 * @param <T>
 */
public class PairProductSimilarity<T> extends AbstractMeasure<Tuple<T>> {

    /** The first distance. */
    private final AbstractMeasure<T> distance1;
    /** The second distance. */
    private final AbstractMeasure<T> distance2;

    /**
     * Constructs new product distance.
     *
     * @param distance1 The first distance.
     * @param distance2 The second distance.
     */
    public PairProductSimilarity(AbstractMeasure<T> distance1, AbstractMeasure<T> distance2) {
        this.distance1 = distance1;
        this.distance2 = distance2;
    }

    /**
     * {@inheritDoc }
     *
     * @return Product of the two distances.
     */
    @Override
    public Double getDistance(Tuple<T> a, Tuple<T> b) {
        return distance1.getDistance(a.value1, b.value1) * distance2.getDistance(a.value2, b.value2);
    }

    /**
     * {@inheritDoc }
     *
     * @return Product of the two similarities.
     */
    @Override
    public Double getSimilarity(Tuple<T> a, Tuple<T> b) {
        return distance1.getSimilarity(a.value1, b.value1) * distance2.getSimilarity(a.value2, b.value2);
    }

    /**
     * {@inheritDoc }
     *
     * @return Product is normalized if both of the distances are normalized.
     */
    @Override
    public boolean isZeroOneNormalized() {
        return distance1.isZeroOneNormalized() && distance2.isZeroOneNormalized();
    }

    /**
     * {@inheritDoc }
     *
     * @return Product is symmetric if both of the distances are symmetric.
     */
    @Override
    public boolean isSymmetric() {
        return distance1.isSymmetric() && distance2.isSymmetric();
    }
}
