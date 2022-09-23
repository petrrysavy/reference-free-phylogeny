package rysavpe1.reads.combined.embedded;

import java.util.List;
import rysavpe1.reads.distance.simple.UkkonenDistance;
import rysavpe1.reads.embedded.EmbeddedSequence;
import rysavpe1.reads.embedded.EmbeddingFunction;
import rysavpe1.reads.embedded.TripletsIndexVectorEmbedding;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.overlap.OverlapRegion;
import rysavpe1.reads.overlap.ReadInContig;
import rysavpe1.reads.overlap.SimpleOverlapRegion;
import rysavpe1.reads.overlap.TripletsReadInContigFinder;
import rysavpe1.reads.utils.NotNullArrayList;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class EndReadsOverlapFinder11 implements EmbeddedOverlapFinder<int[]> {

    private final int readLength;
    private final EmbeddingFunction<Sequence, int[]> readsEmbedding;
    private static final int SENSITIVITY = 3;

    public EndReadsOverlapFinder11(int readLength, EmbeddingFunction<Sequence, int[]> readsEmbedding) {
        this.readLength = readLength;
        this.readsEmbedding = readsEmbedding;
    }

    private final TripletsReadInContigFinder finder = new TripletsReadInContigFinder();

    @Override
    public OverlapRegion getOverlap(EmbeddedSequence<int[]> a, EmbeddedSequence<int[]> b) {
        final int aLen = a.getObject().length();
        final int bLen = b.getObject().length();

        if (aLen <= readLength || bLen <= readLength)
            return null;

        // TODO slow, do it better ... precalculate ... for now good
        final int readIVLength = readLength - 2;
        final int[] aLeft = TripletsIndexVectorEmbedding.window(a.getProjected(), 0, readIVLength);
        final int aProjectedLength = a.getProjected().length;
        final int[] aRight = TripletsIndexVectorEmbedding.window(a.getProjected(), aProjectedLength - readIVLength, aProjectedLength);
        final int[] bLeft = TripletsIndexVectorEmbedding.window(b.getProjected(), 0, readIVLength);
        final int bProjectedLength = b.getProjected().length;
        final int[] bRight = TripletsIndexVectorEmbedding.window(b.getProjected(), bProjectedLength - readIVLength, bProjectedLength);

        final List<SimpleOverlapRegion> regions = new NotNullArrayList<>(4*4*4);
        
        // case when
        // aaaaaaaaaaaaaaaaaaaaa
        //             bbbbbbbbbbbbbbbbbbbbbbbb
        final List<ReadInContig> case1As = finder.findMultipleMatches(bLeft, a.getProjected(), readLength, SENSITIVITY);
        final List<ReadInContig> case1Bs = finder.findMultipleMatches(aRight, b.getProjected(), readLength, SENSITIVITY);
        for(ReadInContig case1A : case1As)
            for(ReadInContig case1B : case1Bs)
                regions.add(getRegion(case1A.getDistance(), case1B.getDistance(),
                        case1A.getContigStart(), aLen,
                        0, case1B.getContigEnd()));

        // bbbbbbbbbbbbbbbbbbbbb
        //              aaaaaaaaaaaaaaaaaaaaaaaa
        final List<ReadInContig> case2As = finder.findMultipleMatches(aLeft, b.getProjected(), readLength, SENSITIVITY);
        final List<ReadInContig> case2Bs = finder.findMultipleMatches(bRight, a.getProjected(), readLength, SENSITIVITY);
        for(ReadInContig case2A : case2As)
            for(ReadInContig case2B : case2Bs)
                regions.add(getRegion(case2A.getDistance(), case2B.getDistance(),
                        0, case2B.getContigEnd(),
                        case2A.getContigStart(), bLen));

        // aaaaaaaaaaaaaaaaaaaaaaaa
        //        bbbbbbbb
        final List<ReadInContig> case3ALs = finder.findMultipleMatches(bLeft, a.getProjected(), readLength, SENSITIVITY);
        final List<ReadInContig> case3ARs = finder.findMultipleMatches(bRight, a.getProjected(), readLength, SENSITIVITY);
        for(ReadInContig case3AL : case3ALs)
            for(ReadInContig case3AR : case3ARs)
                regions.add(getRegion(case3AL.getDistance(), case3AR.getDistance(),
                        case3AL.getContigStart(), case3AR.getContigEnd(),
                        0, bLen));

        //         aaaaaaaa
        // bbbbbbbbbbbbbbbbbbbbbbbb
        final List<ReadInContig> case4BLs = finder.findMultipleMatches(aLeft, b.getProjected(), readLength, SENSITIVITY);
        final List<ReadInContig> case4BRs = finder.findMultipleMatches(aRight, b.getProjected(), readLength, SENSITIVITY);
        for(ReadInContig case4BL : case4BLs)
            for(ReadInContig case4BR : case4BRs)
                regions.add(getRegion(case4BL.getDistance(), case4BL.getDistance(),
                        0, aLen,
                        case4BL.getContigStart(), case4BR.getContigEnd()));

        if(regions.isEmpty()) return null;
        
        /** Replace with ukkonen, just test */
        for(int i = 0; i < regions.size(); i++)
        {
            SimpleOverlapRegion retval = regions.get(i);
            retval = new SimpleOverlapRegion(new UkkonenDistance().getDistance(a.getObject().subSequence(retval.getStartA(), retval.getEndA()),
                b.getObject().subSequence(retval.getStartB(), retval.getEndB())), retval.getLengthA(), retval.getEndA(), retval.getLengthB(), retval.getEndB());
            regions.set(i, retval);
        } // end of test

        SimpleOverlapRegion retval = regions.get(0);
        double optimum = Double.POSITIVE_INFINITY;
        for(int i = 0; i < regions.size(); i++)
            if(regions.get(i).distanceToLengthRatio() < optimum) {
                retval = regions.get(i);
                optimum = retval.distanceToLengthRatio();
            }

        if (optimum == Double.POSITIVE_INFINITY)
            return null;

        return retval;
    }
    
    private SimpleOverlapRegion getRegion(double left, double right, int aLeftEnd, int aRightEnd, int bLeftEnd, int bRightEnd) {
        final double distance = calculateDistance(left, right, aLeftEnd, aRightEnd, bLeftEnd, bRightEnd);
        if(distance == Double.POSITIVE_INFINITY)
            return null;
        return SimpleOverlapRegion.byEnds(distance, aLeftEnd, aRightEnd, bLeftEnd, bRightEnd);
    }

    private double calculateDistance(double left, double right, int aLeftEnd, int aRightEnd, int bLeftEnd, int bRightEnd) {
        final int aLength = aRightEnd - aLeftEnd;
        final int bLength = bRightEnd - bLeftEnd;
        if (aLength <= 0 || bLength <= 0)
            return Double.POSITIVE_INFINITY;

        return (left + right) / 128 * Math.min(aLength, bLength) + Math.abs(aLength - bLength);
    }

}
