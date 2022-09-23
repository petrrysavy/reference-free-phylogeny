package rysavpe1.reads.embedded;

import rysavpe1.reads.distance.AbstractMeasure;
import rysavpe1.reads.model.Sequence;

/**
 * This is just an adapter class that is used if we need to use a measure
 * between embedded multisets where distance of embedded reads bags would be
 * expected.
 *
 * @author Petr Ryšavý
 * @param <T> The target space of the embedding.
 */
public class EmbeddedReadsBagDistanceAdapter<T> extends AbstractMeasure<EmbeddedReadBag<T>> {

    private final AbstractMeasure<EmbeddedMultiset<EmbeddedSequence<T>, Sequence, T>> me;

    public EmbeddedReadsBagDistanceAdapter(AbstractMeasure<EmbeddedMultiset<EmbeddedSequence<T>, Sequence, T>> me) {
        this.me = me;
    }

    @Override
    public Double getSimilarity(EmbeddedReadBag<T> a, EmbeddedReadBag<T> b) {
        throw new RuntimeException();
    }

    @Override
    public Double getDistance(EmbeddedReadBag<T> a, EmbeddedReadBag<T> b) {
        return me.getDistance(a, b);
    }
}
