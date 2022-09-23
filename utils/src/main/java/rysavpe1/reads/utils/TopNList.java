package rysavpe1.reads.utils;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.PriorityQueue;

/**
 * This is a list that holds only selected number of elements that are optimal
 * with respect to a comparator. For example, adding numbers to this list with
 * limit of 5 will result having only 5 maximum numbers at the end. The elements
 * are returned in an arbitrary order. For having maximum, use natural order,
 * for having minimum, use reversed order. Removing elements does not mean that
 * some elements forgotten in the past will be again in the queue.
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 * @param <E> The type of the elements in the queue.
 */
public class TopNList<E> extends AbstractCollection<E> {

    /** This is the number of elements we want to store. */
    private final int capacity;

    /** The priority queue that holds the data. */
    private final PriorityQueue<E> queue;

    /**
     * Creates new instance with the specified capacity.
     * @param capacity The number of elements we are interested in.
     * @throws IllegalArgumentException When {@code capacity} is nonpositive.
     */
    public TopNList(int capacity) {
        this(capacity, null);
    }

    /**
     * Creates new instance with the specified capacity.
     * @param capacity The number of elements we are interested in.
     * @param comparator The comparator used for selecting the greater element.
     * @throws IllegalArgumentException When {@code capacity} is nonpositive.
     */
    public TopNList(int capacity, Comparator<? super E> comparator) {
        if (capacity <= 0)
            throw new IllegalArgumentException("Capacity must be positive : " + capacity);
        queue = new PriorityQueue<>(capacity, comparator);
        this.capacity = capacity;
    }

    /**
     * Adds an object to this collection. If there are more than
     * {@code capacity} elements in this collection, the new object is added
     * only if it is greater than the smallest element in the collection. This
     * element is then removed.
     * @param e The new value to add.
     * @return {@code true} if the {@code e} was added to the collection,
     * {@code false} otherwise.
     * @throws NullPointerException When {@code e} is {@code null}.
     */
    @Override
    public boolean add(E e) {
        if (e == null) throw new NullPointerException();
        if (queue.size() < capacity) return queue.add(e);
        else if (compareToPeek(e)) {
            queue.remove();
            return queue.add(e);
        }
        return false;
    }
    
    public boolean isForInsert(E e) {
        return e != null && (queue.size() < capacity || compareToPeek(e));
    }

    @Override
    public Iterator<E> iterator() {
        return queue.iterator();
    }

    @Override
    public int size() {
        return queue.size();
    }

    /**
     * Gets the maximum number of elements that can be stored in this
     * collection.
     * @return The capacity.
     */
    public int getCapacity() {
        return capacity;
    }

    private boolean compareToPeek(E obj) {
        Comparable<? super E> key = (Comparable<? super E>) obj;
        if (queue.comparator() == null)
            return key.compareTo(queue.peek()) > 0;
        return queue.comparator().compare(obj, queue.peek()) > 0;
    }

    @Override
    public int hashCode() {
        return queue.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TopNList)) // explicit: o!=null
            return false;
        TopNList<E> other = (TopNList<E>) o;
        return Objects.equals(queue, other.queue);
    }

    @Override
    public String toString() {
        return queue.toString();
    }

    @Override
    public void clear() {
        super.clear();
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return queue.retainAll(c);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return queue.containsAll(c);
    }

    @Override
    public boolean remove(Object o) {
        return queue.remove(o);
    }

    @Override
    public boolean contains(Object o) {
        return queue.contains(o);
    }
}
