package rysavpe1.reads.combined.embedded;

import rysavpe1.reads.embedded.EmbeddedSequence;
import rysavpe1.reads.overlap.OverlapRegion;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class TripletsIVOverlapFinder implements EmbeddedOverlapFinder<int[]> {
    
    private final int minLength;

    public TripletsIVOverlapFinder(int minLength) {
        this.minLength = minLength;
    }
    
    private final ManhattanTripletsMatcher distance = new ManhattanTripletsMatcher();

    @Override
    public OverlapRegion getOverlap(EmbeddedSequence<int[]> a, EmbeddedSequence<int[]> b) {
        if(a.getObject().length() < b.getObject().length()) {
            final OverlapRegion region = getOverlap(b, a);
            return region == null ? null : region.swapSequences();
        }
        
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
        return region.asSequenceOverlapRegion();
    }
}

