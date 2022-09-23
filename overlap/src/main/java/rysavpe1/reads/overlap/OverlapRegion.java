package rysavpe1.reads.overlap;

/**
 *
 * @author petr
 */
public interface OverlapRegion {

    public double getDistance();

    public default int getLengthA() {
        return getEndA() - getStartA();
    }

    public int getStartA();

    /**
     * Returns (exclusive) end of the region.
     *
     * @return The position after the last character.
     */
    public int getEndA();

    public default int getLengthB() {
        return getEndB() - getStartB();
    }

    public int getStartB();

    /**
     * Returns (exclusive) end of the region.
     *
     * @return The position after the last character.
     */
    public int getEndB();
    
    public default int getLengthMax() {
        return Math.max(getLengthA(), getLengthB());
    }

    public default double distanceToLengthRatio() {
        return getDistance() / getLengthMax();
    }
    
    public OverlapRegion swapSequences();
    
    public OverlapRegion reverseB(int sequenceBLength);
}
