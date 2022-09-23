package rysavpe1.reads.wis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 * This class is used to solve the weighted interval schedulling problem. For
 * the problem description and algorithm check: Kleinberg, Jon, and Eva Tardos.
 * Algorithm design. Pearson Education India, 2006., chapter 6.1.
 *
 * Each task is assigned start time, end time and value and our goal is to find
 * a subset of non-overlapping tasks that maximize the sum of the values.
 *
 * @author Petr Ryšavý
 */
public class WeightedIntervalScheduling {

    /** Do not let anybody to instantiate the class. */
    private WeightedIntervalScheduling() {
    }

    private static <T extends Interval> double[] getValueArray(T[] intervals) {
        // fills the DP array
        final double[] value = new double[intervals.length];
        value[0] = intervals[0].getValue();
        for (int i = 1; i < intervals.length; i++) {
            final int lastPos = findLast(intervals, intervals[i].getStart());
            final double valueOfLast = lastPos == -1 ? 0 : value[lastPos];
            value[i] = Math.max(value[i - 1], intervals[i].getValue() + valueOfLast);
        }
        return value;
    }

    /**
     * Calculates the sum of the values of optimally selected tasks.
     * @param <T> Type of the interval.
     * @param intervals List of intervals to select from. This array does not
     * need to be sorted.
     * @return The maximum sum of values assuming that tasks are
     * non-overlapping.
     */
    public static <T extends Interval> double getOptimalValue(T[] intervals) {
        Arrays.sort(intervals, IntervalEndComparator.INSTANCE);
        return getValueArray(intervals)[intervals.length - 1];
    }

    /**
     * Calculates the sum of the values of optimally selected tasks.
     * @param <T> Type of the interval.
     * @param intervals List of intervals to select from. Must be sorted by end
     * time.
     * @return The maximum sum of values assuming that tasks are
     * non-overlapping.
     */
    public static <T extends Interval> double getOptimalValueSorted(T[] intervals) {
        return getValueArray(intervals)[intervals.length - 1];
    }

    /**
     * Gets the list of intervals that are best to select in order to maximize
     * the sum of their values.
     * @param <T> Type of the interval.
     * @param intervals List of intervals to select from. Must be sorted by end
     * time.
     * @return The tasks maximizing the overall sum of values.
     */
    public static <T extends Interval> Collection<T> solveSorted(T[] intervals) {
        if (intervals.length == 0)
            return Collections.EMPTY_LIST;

        final double[] value = getValueArray(intervals);
        final Collection<T> set = new ArrayList<>(intervals.length / 2);
        int i = intervals.length - 1;
        while (i > 0) {
            final int lastPos = findLast(intervals, intervals[i].getStart());
            final double valueOfLast = lastPos == -1 ? 0 : value[lastPos];
            if (value[i] == intervals[i].getValue() + valueOfLast) { // the last element is included ..
                set.add(intervals[i]);
                i = lastPos;
            } else
                i--; // just decrease i
        }
        if (i == 0)
            set.add(intervals[0]);
        return set;
    }

    /**
     * Gets the list of intervals that are best to select in order to maximize
     * the sum of their values.
     * @param <T> Type of the interval.
     * @param intervals List of intervals to select from. This array does not
     * need to be sorted.
     * @return The tasks maximizing the overall sum of values.
     */
    public static <T extends Interval> Collection<T> solve(T[] intervals) {
        Arrays.sort(intervals, IntervalEndComparator.INSTANCE);
        return solveSorted(intervals);
    }

    private static <T extends Interval> int findLast(T[] intervals, int end) {
        // get the last interval that ends before the selected time.
        final int pos = Arrays.binarySearch(intervals, new SimpleInterval(0, end, 0), IntervalEndComparator.INSTANCE);
        return pos < 0 ? -pos - 2 : pos;
    }
}
