package rysavpe1.reads.utils;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

/**
 * Utility class for manipulating arrays.
 * @author Petr Ryšavý
 */
public final class ArrayUtils {

    /** Do not let anybody to instantiate the class. */
    private ArrayUtils() {
    }

    /**
     * Converts list of arguments to an array.
     * @param <T> Type of values.
     * @param vals This will be in the array.
     * @return Array with the values from {@code vals}.
     */
    @SafeVarargs
    @SuppressWarnings("varargs")
    public static <T> T[] asArray2(T... vals) {
        return vals;
    }

    /**
     * Converts list of arguments to an array.
     * @param array This will be in the array.
     * @return Array with the values from {@code array}.
     */
    public static double[] asArray(double... array) {
        return array;
    }

    /**
     * Converts list of arguments to an array.
     * @param array This will be in the array.
     * @return Array with the values from {@code array}.
     */
    public static int[] asArray(int... array) {
        return array;
    }

    /**
     * Converts list of arguments to an array.
     * @param array This will be in the array.
     * @return Array with the values from {@code array}.
     */
    public static long[] asArray(long... array) {
        return array;
    }

    /**
     * Converts list of arguments to an array.
     * @param array This will be in the array.
     * @return Array with the values from {@code array}.
     */
    public static char[] asArray(char... array) {
        return array;
    }

    /**
     * Converts collection of boxed integers to an array.
     * @param collection Collection.
     * @return The array.
     */
    public static int[] asArrayInt(Collection<Integer> collection) {
        int[] arr = new int[collection.size()];
        int i = 0;
        for (Integer val : collection)
            arr[i++] = val;
        return arr;
    }

    /**
     * Converts collection of boxed doubles to an array.
     * @param collection Collection.
     * @return The array.
     */
    public static double[] asArrayDouble(Collection<Double> collection) {
        double[] arr = new double[collection.size()];
        int i = 0;
        for (Double val : collection)
            arr[i++] = val;
        return arr;
    }

    /**
     * Does boxing of all elements in an array.
     * @param array The array of primitives.
     * @return The array of Objects.
     */
    public static Integer[] asBoxedArray(int... array) {
        final Integer[] result = new Integer[array.length];
        for (int i = 0; i < array.length; i++)
            result[i] = array[i];
        return result;
    }

    /**
     * Converts a list of elements in a collection to an array.
     * @param <T> Type of the elements.
     * @param clazz The class.
     * @param collection Collection with the elements.
     * @return An array with exactly the same.
     */
    public static <T> T[] asGenericArray(Class<T> clazz, Collection<? extends T> collection) {
        final T[] arr = genericArray(clazz, collection.size());
        return collection.toArray(arr);
    }

    /**
     * Creates a generic type array.
     * @param <T> The type of the array.
     * @param clazz The type of the elements in the array.
     * @param length Length of the new array.
     * @return New array.
     */
    public static <T> T[] genericArray(Class<T> clazz, int length) {
        // Use Array native method to create array
        // of a type only known at run time
        @SuppressWarnings("unchecked")
        T[] arr = (T[]) Array.newInstance(clazz, length);
        return arr;
    }

    /**
     * Counts number of elements in a 2D array. Note that the subarrays do not
     * need to have the same length.
     * @param array2D An array.
     * @return The number of elements in {@code array2D}.
     */
    public static int numElements(double[][] array2D) {
        int num = 0;
        for (double[] array : array2D)
            num += array.length;
        return num;
    }

    /**
     * Counts number of elements in a 2D array. Note that the subarrays do not
     * need to have the same length.
     * @param array2D An array.
     * @return The number of elements in {@code array2D}.
     */
    public static int numElements(int[][] array2D) {
        int num = 0;
        for (int[] array : array2D)
            num += array.length;
        return num;
    }

    /**
     * Converts a 2D array to a list by taking elements from each of the
     * subarrays and putting them in a row.
     * @param array2D Array to flattern.
     * @return All elements of the {@code array2D}, however in a 1D vector.
     */
    public static double[] flattern(double[][] array2D) {
        final double[] target = new double[numElements(array2D)];
        int index = 0;
        for (double[] array : array2D) {
            System.arraycopy(array, 0, target, index, array.length);
            index += array.length;
        }
        return target;
    }

    /**
     * Converts a 2D array to a list by taking elements from each of the
     * subarrays and putting them in a row.
     * @param array2D Array to flattern.
     * @return All elements of the {@code array2D}, however in a 1D vector.
     */
    public static int[] flattern(int[][] array2D) {
        final int[] target = new int[numElements(array2D)];
        int index = 0;
        for (int[] array : array2D) {
            System.arraycopy(array, 0, target, index, array.length);
            index += array.length;
        }
        return target;
    }

    /**
     * Creates a new array that contains {@code n}-times {@code value}.
     * @param value Value to be in the array.
     * @param n The count.
     * @return Array that contains {@code n}-times value {@code value}.
     */
    public static double[] nTimes(double value, int n) {
        final double[] arr = new double[n];
        Arrays.fill(arr, value);
        return arr;
    }

    /**
     * Creates a copy of the array that contains values from the last character
     * to the first character.
     * @param arr Array to be reversed.
     * @return Copy of {@code arr} with elements from the end to the beginning.
     */
    public static char[] reversedCopy(char[] arr) {
        final char[] reversed = new char[arr.length];
        for (int i = 0; i < arr.length; i++)
            reversed[i] = arr[arr.length - i - 1];
        return reversed;
    }

    /**
     * Creates a copy of the array that contains values from the last integer to
     * the first integer.
     * @param arr Array to be reversed.
     * @return Copy of {@code arr} with elements from the end to the beginning.
     */
    public static int[] reversedCopy(int[] arr) {
        final int[] reversed = new int[arr.length];
        for (int i = 0; i < arr.length; i++)
            reversed[i] = arr[arr.length - i - 1];
        return reversed;
    }

    /**
     * Makes sure that the returned array is at least {@code length} long.
     * @param arr An array.
     * @param length Minimum length of the array.
     * @return {@code arr} if long enough, otherwise a new array.
     */
    public static double[] ensureLength(double[] arr, int length) {
        if (arr.length > length)
            return arr;
        return new double[length];
    }

    /**
     * Produces an array that contains numbers from zero to {@code n-1}.
     * @param n The length of the array.
     * @return {@code 0, 1, 2, ..., n-1}
     */
    public static int[] identity(int n) {
        final int[] arr = new int[n];
        for (int i = 1; i < n; i++)
            arr[i] = i;
        return arr;
    }

    /**
     * Produces an array that contains numbers from one to {@code n}.
     * @param n The length of the array.
     * @return {@code 1, 2, ..., n}
     */
    public static int[] identityFromOne(int n) {
        final int[] arr = new int[n];
        for (int i = 0; i < n; i++)
            arr[i] = i + 1;
        return arr;
    }

    /**
     * Creates an aritmetic progression going from {@code start} to {@code end}
     * (inclusively) by {@code step}.
     * @param start The first element of the probression.
     * @param end The last element in the progression or some value between the
     * last and the next element.
     * @param step The difference between two neighboring elements of the
     * progression.
     * @return Array with the progression.
     */
    public static int[] fromToByStep(int start, int end, int step) {
        final int[] arr = new int[(end - start) / step + 1];
        for (int i = 0, val = start; i < arr.length; i++, val += step)
            arr[i] = val;
        return arr;
    }

    /**
     * Creates an aritmetic progression going from {@code start} to {@code end}
     * (inclusively) by {@code step}.
     * @param start The first element of the probression.
     * @param end The last element in the progression or some value between the
     * last and the next element.
     * @param step The difference between two neighboring elements of the
     * progression.
     * @return Array with the progression.
     */
    public static double[] fromToByStep(double start, double end, double step) {
        final double[] arr = new double[(int) ((end - start) / step + 1)];
        for (int i = 0; i < arr.length; i++)
            arr[i] = start + i * step;
        return arr;
    }

    /**
     * Finds an element in an arrray.
     * @param <T> The type of the array.
     * @param arr Array to search in.
     * @param obj The object to find.
     * @return Index of the given object in the array. {@code -1} if not found.
     */
    public static <T> int linearSearch(T[] arr, T obj) {
        for (int i = 0; i < arr.length; i++)
            if (Objects.equals(arr[i], obj))
                return i;
        return -1;
    }

    /**
     * Rotates the array.
     * @param <T> The type of objects.
     * @param arr Array with values.
     * @param shift The target shift. {@code ABCD} shift by 1 is to the right,
     * therefore {@code _ABC}. Shift by -1 is {@code ABC_}.
     * @return The shifted array.
     */
    public static <T> T[] rotate(T[] arr, int shift) {
        if (shift == 0) return arr;

        if (shift > 0) {
            int i = arr.length - 1;
            for (; i >= shift; i--)
                arr[i] = arr[i - shift];
            for (; i >= 0; i--)
                arr[i] = null;
        } else {
            int i = 0;
            for (; i < arr.length + shift; i++)
                arr[i] = arr[i - shift];
            for (; i < arr.length; i++)
                arr[i] = null;
        }

        return arr;
    }

    public static <T> T[] addToArray(Class<T> clazz, T[] arr, T... vals) {
        final T[] retval = genericArray(clazz, arr.length + vals.length);
        System.arraycopy(arr, 0, retval, 0, arr.length);
        System.arraycopy(vals, 0, retval, arr.length, vals.length);
        return retval;
    }

    public static <T> boolean contains(T[] arr, T obj) {
        return linearSearch(arr, obj) != -1;
    }

    public static <T> boolean containsNull(T[] arr) {
        return contains(arr, null);
    }
}
