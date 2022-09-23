package rysavpe1.reads.utils;

/**
 * Simple class for measuring time.
 *
 * @author Petr Ryšavý
 */
public final class TicTac {

    /** Last timestamp. */
    private long timestamp;

    /** Creates the new object and starts counting. */
    public TicTac() {
        tic();
    }

    /** Starts counting and updates the timestapt to the current one. */
    public void tic() {
        timestamp = System.currentTimeMillis();
    }

    /**
     * Returns in milliseconds time since the last timestamp.
     * @return The time since the last call of {@code tic()}.
     */
    public long toc() {
        return System.currentTimeMillis() - timestamp;
    }

    /**
     * Returns in milliseconds time since the last timestamp and starts counting
     * again.
     * @return The time since the last call of {@code tic()}.
     */
    public long toctic() {
        final long retval = toc();
        tic();
        return retval;
    }
    
    public String printTime() {
        return Long.toString(this.toc());
    }
}
