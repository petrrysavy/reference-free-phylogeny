package rysavpe1.reads.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/**
 * A utility class used for manipulating collections.
 *
 * @author Petr Ryšavý
 */
public final class CollectionUtils {

    /** Do not let anybody to instantiate the class. */
    private CollectionUtils() {
    }

    /**
     * Calculates the number of elements that are common for {@code a} and
     * {@code b}.
     * @param <T> Type of elements in the sets.
     * @param a The first set.
     * @param b The second set.
     * @return The number of elements that are in common for both sets.
     */
    public static <T> int intersectionSize(Set<T> a, Set<T> b) {
        if (a.size() > b.size())
            return intersectionSize(b, a);

        int count = 0;
        for (T t : a)
            if (b.contains(t))
                count++;
        return count;
    }

    /**
     * Finds an alement in the set which is not equal to neither i nor j.
     * @param set A set of integers.
     * @param i
     * @param j
     * @return First element of the set, which is different from both {@code i}
     * and {@code j}.
     */
    public static int getFirstNotIJ(Set<Integer> set, int i, int j) {
        for (int val : set)
            if (val != i && val != j)
                return val;
        throw new NoSuchElementException("Cannot find value different from " + i + " and " + j + " in " + set + ".");
    }

    /**
     * Converts list of arguments to an set.
     * @param vals This will be in the set.
     * @return A set with the values from {@code vals}.
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> Set<T> asSet(T... vals) {
        final HashSet<T> set = new HashSet<>(vals.length);
        set.addAll(Arrays.asList(vals));
        return set;
    }

    /**
     * Converts list of arguments to a list. This is useful if we need a
     * modifiable list. {@code Arrays.asList} returns an immutable list.
     * @param vals This will be in the list.
     * @return A set with the values from {@code vals}.
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> List<T> asList(T... vals) {
        // we need this list to be modifiable - Arrays.asList returns fixed size list
        return new ArrayList<>(Arrays.asList(vals));
    }
    
    public static List<Integer> asBoxedList(int... vals) {
        ArrayList<Integer> list = new ArrayList<>(vals.length);
        for(int i : vals) list.add(i);
        return list;
    }

    /**
     * Creates a list of lists, where none of those lists is {@code null}.
     * @param <T> The type of elements in the lists.
     * @param n Number of lists.
     * @return A list of {@code n} lists.
     */
    public static <T> List<List<T>> nLists(int n) {
        List<List<T>> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++)
            list.add(new ArrayList<>());
        return list;
    }

    /**
     * Clears all of the lists.
     * @param <T> The type of the elements in the lists.
     * @param lists List of lists.
     */
    public static <T> void clear(List<List<T>> lists) {
        for (List l : lists)
            l.clear();
    }

    /**
     * Creates a mapping {@code {(k, v) | k \in keys }}.
     * @param <K> Type of key.
     * @param <V> Type of value.
     * @param keys List of keys.
     * @param value The default value.
     * @return Mapping from any key to the value.
     */
    public static <K, V> Map<K, V> pairWithAll(List<K> keys, V value) {
        final Map<K, V> map = new HashMap<>(keys.size());
        for (K key : keys)
            map.put(key, value);
        return map;
    }

    /**
     * Gets list of {@code { (i,j) | i \in l1, j \in l2}}.
     * @param <T> Type of the first list.
     * @param <U> Type of the second list.
     * @param l1 First elements of the tuples.
     * @param l2 Second elements of the tuples.
     * @return Carthesian product.
     */
    public static <T, U> List<Pair<T, U>> carthesianProduct(List<T> l1, List<U> l2) {
        final List<Pair<T, U>> target = new ArrayList<>(l1.size() * l2.size());
        for (T v1 : l1)
            for (U v2 : l2)
                target.add(new Pair<>(v1, v2));
        return target;
    }
    
    /**
     * Gets list of {@code { (i,j) | i \in l1, j \in l2}}.
     * @param <T> Type of the elements in lists.
     * @param l1 First elements of the tuples.
     * @param l2 Second elements of the tuples.
     * @return Carthesian product.
     */
    public static <T> List<Tuple<T>> carthesianProduct2(List<T> l1, List<T> l2) {
        final List<Tuple<T>> target = new ArrayList<>(l1.size() * l2.size());
        for (T v1 : l1)
            for (T v2 : l2)
                target.add(new Tuple<>(v1, v2));
        return target;
    }

    /**
     * Makes sure that list contains at least {@code size} elements by adding
     * {@code null} values.
     * @param <T> Type of elements in the lists.
     * @param list List to grow.
     * @param size Target size.
     * @throws IllegalArgumentException If list is shorter than {@code size} or
     * {@code size} is negative.
     */
    public static <T> void growToSize(List<T> list, int size) {
        final int grow = size - list.size();

        if (grow < 0)
            throw new IllegalArgumentException("Cannot grow list with " + list.size() + " elements to size " + size);

        for (int i = 0; i < grow; i++)
            list.add(null);
    }

    /**
     * Classic zip operation. Takes first elements of the lists, then seconds,
     * thirds etc.
     * @param <T> Type of the zipped objects.
     * @param list Collection of lists to be zipped.
     * @return List of first elements, second elements, third etc.
     */
    public static <T> List<List<T>> zip(Collection<List<T>> list) {
        if (list.isEmpty())
            throw new IllegalArgumentException("Cannot zip empty list.");

        final int n = list.iterator().next().size();
        final List<List<T>> zipped = new ArrayList<>(n);
        for (List<T> l : list)
            assert (l.size() == n);

        for (int i = 0; i < n; i++) {
            final List<T> oneRow = new ArrayList<>(list.size());
            for (List<T> l : list)
                oneRow.add(l.get(i));
            zipped.add(oneRow);
        }
        return zipped;
    }

    /**
     * Converts a list of lists to a single list by taking elements from each of
     * the sublists and putting them in a row.
     * @param <T> Type of the elements.
     * @param list Collection to flattern.
     * @return All elements of the deep collection in a shallow collection.
     */
    public static <T> List<T> flattern(Collection<? extends Collection<T>> list) {
        final List<T> out = new ArrayList<>(list.size());
        for (Collection<T> col : list)
            out.addAll(col);
        return out;
    }

    /**
     * Gets a list of values by ignoring {@code extremes} largest and lowest
     * elements.
     * @param <T> Type of elements in the collection.
     * @param list List of values.
     * @param extremes How many extreme values to ignore.
     * @return A new list without {@code 2*extremes} values.
     */
    public static <T extends Comparable> List<T> filterOutExtremes(Collection<T> list, int extremes) {
        final List<T> sorted = new ArrayList<>(list);
        Collections.sort(sorted);
        return sorted.subList(extremes, sorted.size() - extremes);
    }
    
     /**
     * Finds an element in an arrray.
     * @param <T> The type of the array.
     * @param list Array to search in.
     * @param obj The object to find.
     * @return Index of the given object in the array. {@code -1} if not found.
     */
    public static <T> int linearSearch(List<T> list, T obj) {
        for (int i = 0; i < list.size(); i++)
            if (Objects.equals(list.get(i), obj))
                return i;
        return -1;
    }
}
