package rysavpe1.reads.overlap;

/**
 *
 * @author petr
 */
public class SimpleOverlapRegion implements OverlapRegion {

    private final double distance;
    private final int lengthA, lengthB;
    private final int endA, endB;

    public SimpleOverlapRegion(double distance, int lengthA, int endA, int lengthB, int endB) {
        this.distance = distance;
        this.lengthA = lengthA;
        this.endA = endA;
        this.lengthB = lengthB;
        this.endB = endB;
    }
    
    public static SimpleOverlapRegion byEnds(double distance, int startA, int endA, int startB, int endB) {
        return new SimpleOverlapRegion(distance, endA - startA, endA, endB - startB, endB);
    }

    @Override
    public double getDistance() {
        return distance;
    }

    @Override
    public int getLengthA() {
        return lengthA;
    }

    @Override
    public int getStartA() {
        return endA - lengthA;
    }

    @Override
    public int getEndA() {
        return endA;
    }

    @Override
    public int getLengthB() {
        return lengthB;
    }

    @Override
    public int getStartB() {
        return endB - lengthB;
    }

    @Override
    public int getEndB() {
        return endB;
    }

    @Override
    public SimpleOverlapRegion swapSequences() {
        return new SimpleOverlapRegion(distance, lengthB, endB, lengthA, endA);
    }
    
    
    
    @Override
    public String toString() {
        return "SimpleOverlapRegion{start=" + getStartA() + "/" + getStartB() + ", end=" + endA + "/" + endB + ", distance=" + distance + ", length=" + lengthA + '/' + lengthB + '}';
    }

    @Override
    public OverlapRegion reverseB(int sequenceBLength) {
        return new SimpleOverlapRegion(distance, lengthA, endA, lengthB, sequenceBLength - getStartB());
    }

}
