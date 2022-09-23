package rysavpe1.reads.overlap;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class SimpleReadInContig implements ReadInContig {
    
    private final int readLength;
    private final int contigStart, contigEnd;
    private final int distance;

    public SimpleReadInContig(int readLength, int contigStart, int contigEnd, int distance) {
        this.readLength = readLength;
        this.contigStart = contigStart;
        this.contigEnd = contigEnd;
        this.distance = distance;
    }

    @Override
    public int getReadLength() {
        return readLength;
    }

    @Override
    public int getContigStart() {
        return contigStart;
    }

    @Override
    public int getContigEnd() {
        return contigEnd;
    }

    @Override
    public double getDistance() {
        return distance;
    }

    @Override
    public OverlapRegion reverseB(int contigLength) {
        return new SimpleReadInContig(readLength, contigLength - getContigEnd(), contigLength - getContigStart(), distance);
    }
}
