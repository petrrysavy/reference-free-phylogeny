package rysavpe1.reads.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * An immutable set that contains all elements.
 *
 * @author petr
 * @param <T> The type of objects.
 */
public class AllValuesSet<T> implements Set<T> {

    @Override
    public int size() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean contains(Object o) {
        return true;
    }

    @Override
    public Iterator<T> iterator() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public Object[] toArray() {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public <T> T[] toArray(T[] arg0) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean add(T e) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean remove(Object o) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return true;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException("Not supported.");
    }

}
