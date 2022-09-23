package rysavpe1.reads.wis;

import java.util.Comparator;

/**
 * Comparator that can be used to sort interval instances by their terminal
 * time.
 *
 * @author Petr Ryšavý
 */
public class IntervalEndComparator implements Comparator<Interval> {

    /** An instance of the comparator. */
    public static final IntervalEndComparator INSTANCE = new IntervalEndComparator();

    /** {@inheritDoc} @return {@code o1.end.compareTo(o2.end)}. */
    @Override
    public int compare(Interval o1, Interval o2) {
        return Integer.compare(o1.getEnd(), o2.getEnd());
    }

}
