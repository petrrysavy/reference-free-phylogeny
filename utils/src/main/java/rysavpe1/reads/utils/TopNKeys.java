package rysavpe1.reads.utils;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 * @param <V>
 */
public class TopNKeys<V> extends AbstractCollection<V> {

    private final List<V>[] arr;
    private int bottom = 0;
    private final boolean storeLowest;

    public TopNKeys(int count, boolean storeLowest) {
        this.bottom = storeLowest ? Integer.MAX_VALUE - count : Integer.MIN_VALUE + count;
        this.arr = (List<V>[]) new List[count];
        this.storeLowest = storeLowest;
    }

    public boolean put(int key, V value) {
        if (storeLowest ? key >= bottom + arr.length : key < bottom)
            return false;

        if (storeLowest && key < bottom)
            setBottom(key);
        else if(!storeLowest && key >= bottom + arr.length)
            setBottom(key - arr.length + 1);

        getList(key).add(value);
        return true;
    }
    
    public boolean isForInsert(int key) {
        return storeLowest ? key < bottom + arr.length : key >= bottom;
    }

    private List<V> getList(int key) {
        final int index = key - bottom;
        if (arr[index] == null)
            arr[index] = new ArrayList<>();
        return arr[index];
    }

    public void setBottom(int newBottom) {
        if (newBottom == bottom)
            return;

        ArrayUtils.rotate(arr, bottom - newBottom);
        bottom = newBottom;
    }

    public int getBottom() {
        return bottom;
    }

    @Override
    public AbstractIterator<V> iterator() {
        return storeLowest ? new ValuesUpIterator() : new ValuesDownIterator();
    }
    
    @Override
    public int size() {
        int size = 0;
        for (List<V> l : arr)
            if(l != null)
                size += l.size();
        return size;
    }
    
    public List<V> asList() {
        final ArrayList<V> list = new ArrayList<>(size());
        for(List<V> l : arr)
            if(l != null)
                list.addAll(l);
        return list;
    }

    private abstract class ValuesIterator extends AbstractIterator<V> {

        int i;
        int next;
        private Iterator<V> currentIterator;

        abstract void calcNext();

        ValuesIterator(int init) {
            i = init;
            calcNext();
            i = next;
            calcNext();
            currentIterator = (i == -1) ? Collections.emptyIterator() : arr[i].iterator();
        }

        @Override
        public boolean hasNext() {
            if (currentIterator.hasNext())
                return true;

            return next != -1;
        }

        @Override
        public V next() {
            if (!currentIterator.hasNext() && next != -1) {
                i = next;
                currentIterator = arr[i].iterator();
                calcNext();
            }

            return currentIterator.next();
        }
    }

    private final class ValuesUpIterator extends ValuesIterator {

        public ValuesUpIterator() {
            super(-1);
        }

        @Override
        void calcNext() {
            for(next = i+1; next < arr.length; next++)
                if(arr[next] != null)
                    return;
            next = -1;
        }
    }

    private final class ValuesDownIterator extends ValuesIterator {

        public ValuesDownIterator() {
            super(arr.length);
        }

        @Override
        void calcNext() {
            for(next = i-1; next >= 0; next--)
                if(arr[next] != null)
                    return;
        }
    }

}
