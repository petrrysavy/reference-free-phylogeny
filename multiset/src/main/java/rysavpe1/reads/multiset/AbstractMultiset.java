package rysavpe1.reads.multiset;

import java.util.AbstractCollection;
import java.util.HashSet;
import java.util.Set;

/**
 * Abstract implementation of multiset.
 *
 * @author Petr Ryšavý
 * @param <T> The type of objects stored in the multiset.
 */
public abstract class AbstractMultiset<T> extends AbstractCollection<T> implements Multiset<T> {

    /**
     * {@inheritDoc }
     *
     * @return A HashSet that contains all of the objects in this multiset
     * without duplicates.
     */
    @Override
    public Set<T> toSet() {
        return new HashSet<>(this);
    }

    @Override
    public int uniqueSize() {
        return toSet().size();
    }
    

}
