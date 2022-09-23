package rysavpe1.reads.utils;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author petr
 * @param <E>
 */
public class NotNullArrayList<E> extends ArrayList<E> {

    public NotNullArrayList(int initialCapacity) {
        super(initialCapacity);
    }

    public NotNullArrayList() {
    }

    public NotNullArrayList(Collection<? extends E> c) {
        super(c);
    }

    @Override
    public void add(int index, E element) {
        if (element != null)
            super.add(index, element);
    }

    @Override
    public boolean add(E e) {
        return e == null ? false : super.add(e);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> c) {
        if (!c.contains(null))
            return super.addAll(index, c);
        boolean retval = false;
        for (E o : c)
            if (o != null) {
                retval = true;
                super.add(index++, (E) o);
            }
        return retval;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        if (!c.contains(null))
            return super.addAll(c);
        boolean retval = false;
        for (E o : c)
            if (o != null)
                retval |= super.add(o);
        return retval;
    }

}
