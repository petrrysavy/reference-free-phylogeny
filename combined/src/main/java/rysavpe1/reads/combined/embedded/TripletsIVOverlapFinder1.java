package rysavpe1.reads.combined.embedded;

import rysavpe1.reads.distance.simple.UkkonenDistance;
import rysavpe1.reads.embedded.EmbeddedSequence;
import rysavpe1.reads.overlap.OverlapRegion;
import rysavpe1.reads.overlap.SimpleOverlapRegion;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class TripletsIVOverlapFinder1 implements EmbeddedOverlapFinder<int[]> {
    
    private final int minLength;

    public TripletsIVOverlapFinder1(int minLength) {
        this.minLength = minLength;
    }
    
    private final ManhattanTripletsMatcher distance = new ManhattanTripletsMatcher();

    @Override
    public OverlapRegion getOverlap(EmbeddedSequence<int[]> a, EmbeddedSequence<int[]> b) {
        if(a.getObject().length() < b.getObject().length())
            return getOverlap(b, a).swapSequences();
        
        final int[] manhattanDist = distance.distancesVectorBoth(a.getProjected(), b.getProjected());
        int minI = -1;
        double minDistanceToLen = Double.POSITIVE_INFINITY;
        TripletsIVOverlapRegion region = new TripletsIVOverlapRegion(a.getProjected().length, b.getProjected().length, 0, 0);
        for(int i = minLength - 2; i < manhattanDist.length + 1 - minLength; i++) {
            region.setI(i, manhattanDist[i]);
            final double currentRatio = region.distanceToLengthRatio();
            if(currentRatio < minDistanceToLen) {
                minI = i;
                minDistanceToLen = currentRatio;
            }
        }
        if(minI == -1) return null;
        region.setI(minI, manhattanDist[minI]);
        
        final OverlapRegion retval = region.asSequenceOverlapRegion();
        return new SimpleOverlapRegion(new UkkonenDistance().getDistance(a.getObject().subSequence(retval.getStartA(), retval.getEndA()),
                b.getObject().subSequence(retval.getStartB(), retval.getEndB())), retval.getLengthA(), retval.getEndA(), retval.getLengthB(), retval.getEndB());
    }
}

