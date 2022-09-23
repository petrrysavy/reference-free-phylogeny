package rysavpe1.reads.wis;

/**
 * Interval has a start time, end time and a value.
 *
 * @author Petr Ryšavý
 */
public interface Interval {

    /** Gets the end time.
     * @return The end time. */
    public int getEnd();

    /** Gets the start time.
     * @return The start time. */
    public int getStart();

    /**
     * Gets the value
     * @return The value.
     */
    public double getValue();
}
