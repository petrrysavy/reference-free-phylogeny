package rysavpe1.reads.utils;

import java.util.Iterator;

/**
 * A base class for custom iterators.
 *
 * @author Petr Ryšavý
 * @param <T> The type of objects that will be iterated over.
 */
public abstract class AbstractIterator<T> implements Iterator<T>, Iterable<T> {

    @Override
    public Iterator<T> iterator() {
        return this;
    }
}
