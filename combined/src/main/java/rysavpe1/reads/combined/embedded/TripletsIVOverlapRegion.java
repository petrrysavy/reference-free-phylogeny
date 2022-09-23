package rysavpe1.reads.combined.embedded;

import rysavpe1.reads.overlap.OverlapRegion;
import rysavpe1.reads.overlap.SimpleOverlapRegion;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
final class TripletsIVOverlapRegion implements OverlapRegion {

    private final int lengthAIV;
    private final int lengthBIV;
    private int i;
    private int distance;

    public TripletsIVOverlapRegion(int lengthAIV, int lengthBIV, int i, int distance) {
        this.lengthAIV = lengthAIV;
        this.lengthBIV = lengthBIV;
        setI(i, distance);
    }

    public void setI(int i, int distance) {
        this.i = i;
        this.distance = distance;
    }

    @Override
    public double getDistance() {
        return distance;
    }

    @Override
    public int getStartA() {
        return Math.max(0, i - lengthBIV);
    }

    @Override
    public int getEndA() {
        return Math.min(i, lengthAIV);
    }

    @Override
    public int getStartB() {
        return Math.max(0, lengthBIV - i);
    }

    @Override
    public int getEndB() {
        return Math.min(lengthAIV + lengthBIV - i, lengthBIV);
    }

    @Override
    public double distanceToLengthRatio() {
        return ((double) distance) / getLengthInSequences();
    }

    private int getLengthInSequences() {
        int len = getLength();
        return len == 0 ? 0 : len + 2;
    }

    @Override
    public int getLengthMax() {
        return getLength();
    }

    @Override
    public int getLengthB() {
        return getLength();
    }

    @Override
    public int getLengthA() {
        return getLength();
    }

    private int getLength() {
        return getEndA() - getStartA();
    }

    @Override
    public OverlapRegion swapSequences() {
        throw new UnsupportedOperationException("Cannot swap sequences");
    }

    public OverlapRegion asSequenceOverlapRegion() {
        final int length = getLengthInSequences();
        int endA = getEndA();
//        if (endA == lengthAIV) endA += 2;
        if (endA != 0) endA += 2;
        int endB = getEndB();
//        if (endB == lengthBIV) endB += 2;
        if (endB != 0) endB += 2;
        return new SimpleOverlapRegion(distance, length, endA, length, endB);
    }

    @Override
    public OverlapRegion reverseB(int sequenceBLength) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
