package rysavpe1.reads.utils;

import java.util.AbstractList;
import java.util.List;

/**
 * A list that wraps another one and pretends that the elements are in reversed
 * order. This functionaly holds only of reading.
 *
 * @author Petr Ryšavý
 * @param <T> Type of elements in the list.
 */
public class TransposedList<T> extends AbstractList<T> {

    /** The wrapped list. */
    private final List<T> list;

    /**
     * Creates a new instance of the list.
     * @param list The list that will be virtually reversed.
     */
    public TransposedList(List<T> list) {
        this.list = list;
    }

    @Override
    public T get(int index) {
        return list.get(list.size() - index - 1);
    }

    @Override
    public int size() {
        return list.size();
    }
}
