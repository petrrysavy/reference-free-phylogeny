package rysavpe1.reads.multiset;

import java.util.Collection;
import java.util.Set;

/**
 * Interface that defines a multiset. Multiset is a combination of list and set.
 * Similarly to list a multiset can contain duplicates and similarly to a set it
 * does not store the order. Therefore it can be viewed as a set where each
 * object can be stored multiple times.
 *
 * @author Petr Ryšavý
 * @param <T> The type of objects stored in the multiset.
 */
public interface Multiset<T> extends Collection<T> {

    /**
     * Counts the number of occurences of object {@code o}.
     *
     * @param o The object to search.
     * @return Number of occcurences of {@code o} in this multiset.
     */
    public int count(Object o);

    /**
     * Adds an object arbitrary number of times to the multiset.
     *
     * @param object Object to add.
     * @param count How many times should be object added.
     * @return {@code true} if the objects were added.
     */
    public boolean add(T object, int count);

    /**
     * Calculates union of two multisets. Union contains each object which is at
     * least once in this or the other multiset. The frequencies are calculated
     * as sums of the occurencies in the two original multisets. Therefore the
     * size of the resulting multiset is sum of the sizes of the two original
     * multisets.
     *
     * @param t Union with this multiset should be calculated.
     * @return A new multiset that contains all objects of this multiset and
     * {@code t}.
     */
    public Multiset<T> union(Multiset<T> t);

    /**
     * Converts this multiset to a set, which means that it removes the
     * duplicates.
     *
     * @return Multiset converted to a set.
     */
    public Set<T> toSet();
    
    /**
     * Gets number of unique elements in this multiset.
     * @return Number of elements ignoring duplicates.
     */
    public int uniqueSize();
    
    /**
     * Removes all occurences of an object from the set. The new count of the
     * object is zero, no matter what it was before this operation.
     * @param o The object to remove.
     * @return {@code true} if the object was in the multiset, {@code false}
     * otherwise.
     */
    public boolean removeAllOccurences(Object o);

        /** {@inheritDoc } */
        @Override
        public default boolean contains
        (Object o
        
            ) {
        return count(o) != 0;
        }
    }
