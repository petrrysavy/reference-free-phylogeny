package rysavpe1.reads.combined.multiprojected;

import rysavpe1.reads.overlap.OverlapRegion;
import rysavpe1.reads.overlap.SimpleOverlapRegion;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
final class QGramOverlapRegion implements OverlapRegion {

    private final int lengthAIV;
    private final int lengthBIV;
    private int i;
    private int distance;
    private final int projectionSize;

    public QGramOverlapRegion(int lengthAIV, int lengthBIV, int i, int distance, int projectionSize) {
        this.lengthAIV = lengthAIV;
        this.lengthBIV = lengthBIV;
        this.projectionSize = projectionSize;
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
    
    /**
     * This method does normalization by q-grams so that this region can be used
     * for comparison with another region with different q-size. One change in
     * edit distance can change up to q values in q-gram distance. Therefore,
     * the calculated distance needs to be normalized by dividing with the
     * {@code q}.
     * @param q
     */
    public double distanceToLenNormalized() {
        return distanceToLengthRatio() / projectionSize;
    }

    private int getLengthInSequences() {
        int len = getLength();
        return len == 0 ? 0 : len + (projectionSize - 1);
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
        if (endA != 0) endA += (projectionSize - 1);
        int endB = getEndB();
//        if (endB == lengthBIV) endB += 2;
        if (endB != 0) endB += (projectionSize - 1);
        return new SimpleOverlapRegion(((double)distance) / projectionSize, length, endA, length, endB);
    }

    @Override
    public OverlapRegion reverseB(int sequenceBLength) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
