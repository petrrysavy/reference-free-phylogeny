package rysavpe1.reads.distance.simple;

import java.util.Collection;
import rysavpe1.reads.distance.AbstractMeasure;

/**
 * This measure compares two collections by taking maximum of their sizes. This
 * is sometimes a handy upper bound.
 *
 * @author Petr Ryšavý
 * @param <T> Type of objects in collections.
 */
public class MaxSizeSimilarity<T extends Collection> extends AbstractMeasure<T> {

    @Override
    public Double getSimilarity(T a, T b) {
        throw new UnsupportedOperationException("not normalized");
    }

    @Override
    public Double getDistance(T a, T b) {
        return new Double(Math.max(a.size(), b.size()));
    }

}
