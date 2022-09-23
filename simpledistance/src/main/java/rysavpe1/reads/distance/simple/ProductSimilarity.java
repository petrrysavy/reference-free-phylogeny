package rysavpe1.reads.distance.simple;

import rysavpe1.reads.distance.AbstractMeasure;

/**
 * This distance is calculated as product of two simpler distances. It can be
 * understood as a fuzzy conjunction, namely the product conjunction.
 *
 * @author Petr Ryšavý
 * @param <T>
 */
public class ProductSimilarity<T> extends AbstractMeasure<T> {

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
    public ProductSimilarity(AbstractMeasure<T> distance1, AbstractMeasure<T> distance2) {
        this.distance1 = distance1;
        this.distance2 = distance2;
    }

    /**
     * {@inheritDoc }
     *
     * @return Product of the two distances.
     */
    @Override
    public Double getDistance(T a, T b) {
        return distance1.getDistance(a, b) * distance2.getDistance(a, b);
    }

    /**
     * {@inheritDoc }
     *
     * @return Product of the two similarities.
     */
    @Override
    public Double getSimilarity(T a, T b) {
        return distance1.getSimilarity(a, b) * distance2.getSimilarity(a, b);
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
