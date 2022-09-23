package rysavpe1.reads.model;

import java.util.Iterator;
import rysavpe1.reads.utils.AbstractIterator;

/**
 * Iterator that returns for a sequence also its complement, reverse and
 * reversed complement.
 *
 * @author Petr Ryšavý
 * @param <T> Type of the sequence.
 */
public class UnorientedIterator<T extends UnorientedObject<T>> extends AbstractIterator<T> {

    /** The wrapped iterator. */
    private final Iterator<T> wrapped;
    /** Current element to return. */
    private T current;
    /** Position - what shall we return of the current sequence now. */
    private int pos;

    /**
     * Creates a new unoriented iterator based on another iterator.
     * @param wrapped We decorate this iterator by enhancing it by complement,
     * reverse and reversed complement.
     */
    public UnorientedIterator(Iterator<T> wrapped) {
        this.wrapped = wrapped;
        pos = 4;
    }

    @Override
    public boolean hasNext() {
        return pos != 4 || wrapped.hasNext();
    }

    @Override
    public T next() {
        if (pos == 4) {
            current = wrapped.next();
            pos = 0;
        }

        T retval = null;
        switch (pos) {
            case 0:
                retval = current;
                break;
            case 1:
                retval = current.reverse();
                break;
            case 2:
                retval = current.complement();
                break;
            case 3:
                retval = current.reverseComplement();
                break;
        }
        pos++;
        return retval;
    }
}
