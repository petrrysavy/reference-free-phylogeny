package rysavpe1.reads.combined.multiprojected;

import rysavpe1.reads.bio.Nucleotides;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.overlap.OverlapFinder;
import rysavpe1.reads.overlap.OverlapRegion;
import rysavpe1.reads.utils.MathUtils;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class QGramOverlapFinder implements OverlapFinder {

    private final int minLength;
    private final QGramEmbedding embedding;

    public QGramOverlapFinder(int minLength) {
        this.minLength = minLength;
        this.embedding = new QGramEmbedding();
    }

    @Override
    public OverlapRegion getOverlap(Sequence a, Sequence b) {
        if (a.length() < b.length()) {
            final OverlapRegion region = getOverlap(b, a);
            return region == null ? null : region.swapSequences();
        }

//        System.err.println("overlap of "+a+b);
        final int maxQ = MathUtils.roundUp(MathUtils.logBase(Math.min(a.length(), b.length()), Nucleotides.NUCLEOTIDES_COUNT));
        QGramOverlapRegion bestRegion = null;
        double minDistanceToLen = Double.POSITIVE_INFINITY;
        for (int q = 3; q <= maxQ; q++) {
//            System.err.println("q is "+q);
            final int[] aProjected = embedding.projectIV(a, q);
            final int[] bProjected = embedding.projectIV(b, q);
            QGramOverlapRegion region = getBestRegionForQ(aProjected, bProjected, q);
            if (region != null && region.distanceToLenNormalized() < minDistanceToLen) {
                bestRegion = region;
                minDistanceToLen = region.distanceToLenNormalized();
            }
        }
//        System.err.println("and the overlap is :" + bestRegion);
        return bestRegion == null ? null : bestRegion.asSequenceOverlapRegion();
    }

    private QGramOverlapRegion getBestRegionForQ(int[] aProjected, int[] bProjected, int q) {
        final int[] manhattanDist = new QGramMatcher(q).distancesVectorBoth(aProjected, bProjected);

        final int from = Math.max(MathUtils.pow(Nucleotides.NUCLEOTIDES_COUNT, q - 1), minLength) - (q - 1);
        final int to = Math.min(MathUtils.pow(Nucleotides.NUCLEOTIDES_COUNT, q + 1), manhattanDist.length + 1 - minLength) - (q - 1);
        if (from > to) return null;

        int minI = -1;
        double minDistanceToLen = Double.POSITIVE_INFINITY;
        QGramOverlapRegion region = new QGramOverlapRegion(aProjected.length, bProjected.length, 0, 0, q);

        final int startI2 = Math.max(to + 1, manhattanDist.length - 1 - to);
        final int endI2 = manhattanDist.length - 1 - from;

        for (int i = from; i <= endI2; i++) {
            region.setI(i, manhattanDist[i]);
            final double currentRatio = region.distanceToLengthRatio();
            if (currentRatio < minDistanceToLen) {
                minI = i;
                minDistanceToLen = currentRatio;
            }
            else if(minI == -1) {
                System.err.println("WRONG : "+i + " curr ratio "+region.distanceToLengthRatio());
                System.err.println("aProjected " + aProjected.length + " b " + bProjected.length + " q " + q);
                System.err.println("length a " + region.getLengthA() + " b " + region.getLengthB());
            }

            if (i == to) i = startI2 - 1;
        }
        
        if(minI == -1) {
                System.err.println("WRONG : ");
                System.err.println("aProjected " + aProjected.length + " b " + bProjected.length + " q " + q);
                System.err.println("length a " + region.getLengthA() + " b " + region.getLengthB());
                System.err.println("mdLen "+manhattanDist.length);
                System.err.println("from to "+ from + " "+to);
                System.err.println("ends "+startI2+" "+endI2);
        }

        region.setI(minI, manhattanDist[minI]);
        return region;
    }
}
