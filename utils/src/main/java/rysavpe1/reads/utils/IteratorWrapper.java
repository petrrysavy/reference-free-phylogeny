package rysavpe1.reads.utils;

import java.util.Iterator;

/**
 * The wrapper that enables to use iterator in for loop directly. The code shows
 * how to do that.
 * <pre>
 * for(Type t : new IteratorWrapper<>(iterator))
 * </pre>
 *
 * @param <T> The type of the iterator.
 * @author Petr Ryšavý
 */
public class IteratorWrapper<T> implements Iterable<T> {

    /**
     * The stored refference to the iterator.
     */
    private final Iterator<T> iterator;

    /**
     * Creates new iterator wrapper.
     * @param iterator The iterator to wrap.
     */
    public IteratorWrapper(final Iterator<T> iterator) {
        this.iterator = iterator;
    }

    @Override
    public Iterator<T> iterator() {
        return this.iterator;
    }
}
