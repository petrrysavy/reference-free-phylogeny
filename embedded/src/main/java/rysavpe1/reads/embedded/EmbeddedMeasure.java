package rysavpe1.reads.embedded;

import rysavpe1.reads.distance.AbstractMeasure;

/**
 *
 * @author Petr Ryšavý
 * @param <K>
 * @param <V>
 */
public class EmbeddedMeasure<K, V> extends AbstractMeasure<EmbeddedObject<K,V>> {
    private final AbstractMeasure<V> measure;

    public EmbeddedMeasure(AbstractMeasure<V> measure) {
        this.measure = measure;
    }

    @Override
    public Double getSimilarity(EmbeddedObject<K, V> a, EmbeddedObject<K, V> b) {
        return measure.getSimilarity(a.getProjected(), b.getProjected());
    }

    @Override
    public Double getDistance(EmbeddedObject<K, V> a, EmbeddedObject<K, V> b) {
        return measure.getDistance(a.getProjected(), b.getProjected());
    }

    @Override
    public boolean isZeroOneNormalized() {
        return measure.isZeroOneNormalized();
    }

    @Override
    public boolean isSymmetric() {
        return measure.isSymmetric();
    }
}
