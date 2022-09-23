package rysavpe1.reads.multiset;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * An implementation of multiset that uses a hash map. The map stores objects as
 * keys and counts as values.
 *
 * @author Petr Ryšavý
 * @param <T> The type of objects stored in the multiset.
 */
public class HashMultiset<T> extends AbstractMultiset<T> implements Cloneable {

    /** This map holds the state of the multiset. */
    private HashMap<T, Integer> map;

    /** Size of this multiset. */
    private int size = 0;

    /** Constructs an empty multiset. */
    public HashMultiset() {
        map = new HashMap<>();
    }

    /**
     * Constructs an empty multiset and fills it with all elements form the
     * collection.
     *
     * @param initialElements Elements to fill the collection with.
     */
    public HashMultiset(Collection<T> initialElements) {
        this(initialElements.size());
        addAll(initialElements);
    }

    /**
     * Constructs an empty multiset with selected initial capacity of the
     * backing hash map.
     *
     * @param initialCapacity The initial capacity.
     */
    public HashMultiset(int initialCapacity) {
        map = new HashMap<>(initialCapacity);
    }

    /**
     * Constructs a new multiset with selected initial capapcity and load
     * factor.
     *
     * @param initialCapacity The initial capacity of the map.
     * @param loadFactor Load factor of the map. Growing the map does not depend
     * on duplicates.
     */
    public HashMultiset(int initialCapacity, float loadFactor) {
        map = new HashMap<>(initialCapacity, loadFactor);
    }

    /**
     * Creates an empty multiset and fills it with the elements from the
     * parameter list.
     *
     * @param elements Elements to put into the multiset.
     */
    public HashMultiset(T... elements) {
        this(elements.length);
        addAll(Arrays.asList(elements));
    }

    /**
     * Constructs a new iterator. The iterator iterates over all element
     * including the duplicates.
     *
     * @return The iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return new MultisetIterator();
    }

    /**
     * {@inheritDoc }
     * Returns number of elements in this multiset including duplicates.
     *
     * @return The number of elements in the multiset.
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Gets number of unique elements in this multiset.
     * @return Number of elements ignoring duplicates.
     */
    @Override
    public int uniqueSize() {
        return map.size();
    }

    /**
     * {@inheritDoc }
     * This implementation sets the size to zero and clears the backing hash
     * map.
     */
    @Override
    public void clear() {
        size = 0;
        map.clear();
    }

    /**
     * {@inheritDoc} This implementation removes a single occurence of the
     * object.
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        Integer count = count(o);
        if (count == 0)
            return false;

        size--;
        if (count > 1)
            map.put((T) o, count - 1);
        else
            map.remove((T) o);
        return true;
    }

    /** {@inheritDoc } */
    @Override
    public boolean removeAllOccurences(Object o) {
        Integer count = count(o);
        if (count == 0)
            return false;

        size -= count;
        map.remove((T) o);
        return true;
    }

    /**
     * {@inheritDoc} Adds a single instance of the object to the multiset.
     */
    @Override
    public boolean add(T e) {
        return add(e, 1);
    }

    /** {@inheritDoc} */
    @Override
    public boolean add(T e, int count) {
        if (count <= 0)
            throw new IllegalArgumentException("Illegal count: " + count);

        Integer currentCount = count(e);
        currentCount += count;
        size += count;
        map.put(e, currentCount);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public Set<T> toSet() {
        return new HashSet<>(map.keySet());
    }

    /** {@inheritDoc} */
    @Override
    public HashMultiset<T> union(Multiset<T> other) {
        HashMultiset<T> copy = this.clone();
        for (T elem : other.toSet())
            copy.add(elem, other.count(elem));
        return copy;
    }

    /** {@inheritDoc} */
    @Override
    public int count(Object o) {
        @SuppressWarnings("element-type-mismatch")
        Integer count = map.get(o);
        if (count == null)
            return 0;
        return count;
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("element-type-mismatch")
    public boolean contains(Object o) {
        return map.containsKey(o);
    }

    /** {@inheritDoc} */
    public boolean addAll(HashMultiset<T> c) {
        boolean added = false;
        for(Map.Entry<T, Integer> en : c.map.entrySet())
            added |= this.add(en.getKey(), en.getValue());
        return added;
    }

    /**
     * {@inheritDoc} Two multisets are equal iff they contain the same elements
     * and all the elements have the same counts in both collections.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final HashMultiset<?> other = (HashMultiset<?>) obj;
        // the sizes of the maps may be equal, but this may be still violated
        if (this.size != other.size)
            return false;
        return Objects.equals(this.map, other.map);
    }

    /**
     * {@inheritDoc }
     * Hash code of the backing hash map.
     */
    @Override
    public int hashCode() {
        return map.hashCode();
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("CloneDeclaresCloneNotSupported")
    protected HashMultiset<T> clone() {
        try {
            @SuppressWarnings("unchecked")
            HashMultiset<T> copy = (HashMultiset<T>) super.clone();
            copy.size = size;
            copy.map = (HashMap<T, Integer>) map.clone();
            return copy;
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(HashMultiset.class.getName()).log(Level.SEVERE, "Cannot clone map");
            throw new RuntimeException();
        }
    }

    @Override
    public String toString() {
        return map.toString();
    }

    /** A multiset iterator that wraps iterator of the backing hash map. */
    private class MultisetIterator implements Iterator<T> {

        /** Iterator of the hash map. */
        private final Iterator<Entry<T, Integer>> iterator = map.entrySet().iterator();
        /** Current entry. */
        private Entry<T, Integer> current = null;
        /** How many times we have returned an element of the current entry. */
        private int count = 0;
        /** Used for the correct behavior of the remove method. */
        private boolean nextCalled = false;

        @Override
        public boolean hasNext() {
            return iterator.hasNext() || (current != null && count < current.getValue());
        }

        @Override
        public T next() {
            // this happens only after the first call.
            if (current == null)
                current = iterator.next();

            // we cannot return the current element anymore, look for the next entry in the map
            if (current.getValue() <= count) {
                current = iterator.next();
                count = 0;
            }

            // increase count and return key
            count++;
            nextCalled = true;
            return current.getKey();
        }

        @Override
        public void remove() {
            // remove can be called only after the next() function, and once
            if (!nextCalled)
                throw new IllegalStateException("Next needs to be called before each remove operation.");

            nextCalled = false;
            size--;
            final Integer currentCount = current.getValue();
            if (currentCount == 1) {
                iterator.remove();
                count = Integer.MAX_VALUE; // we do not want to return the element anymore
                return;
            }

            count--; // since we remove the element, we have to decrease the current count as well
            current.setValue(currentCount - 1);
        }
    }
}
