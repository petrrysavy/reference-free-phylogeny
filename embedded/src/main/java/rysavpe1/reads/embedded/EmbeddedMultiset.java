package rysavpe1.reads.embedded;

import java.util.Iterator;

/**
 * Interface that defines multiset that is embedded to some other space.
 * @author Petr Ryšavý
 * @param <T> Type of the embedded object that is used.
 * @param <K> The original space key.
 * @param <V> The projected key.
 */
public interface EmbeddedMultiset<T extends EmbeddedObject<K, V>, K, V> extends Iterable<K> {

    /** Returns the size of the multiset.
     * @return Size of the multiset. */
    public int size();
    
    /**
     * Gets number of unique elements in this multiset.
     * @return Number of elements ignoring duplicates.
     */
    public int uniqueSize();

    /** The count of a particular object. Should be done in the original space.
     * @param o Object to count.
     * @return Count of {@code o} */
    public int count(Object o);

    /** Iterator of the embedded values.
     * @return Iterator in the embedded space. */
    public Iterator<T> embeddedIterator();

}
