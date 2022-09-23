package rysavpe1.reads.combined.embedded;

import rysavpe1.reads.distance.simple.UkkonenDistance;
import rysavpe1.reads.embedded.EmbeddedSequence;
import rysavpe1.reads.embedded.EmbeddingFunction;
import rysavpe1.reads.embedded.TripletsIndexVectorEmbedding;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.overlap.OverlapRegion;
import rysavpe1.reads.overlap.ReadInContig;
import rysavpe1.reads.overlap.SimpleOverlapRegion;
import rysavpe1.reads.overlap.TripletsReadInContigFinder;
import rysavpe1.reads.utils.MathUtils;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class EndReadsOverlapFinder1 implements EmbeddedOverlapFinder<int[]> {

    private final int readLength;
    private final EmbeddingFunction<Sequence, int[]> readsEmbedding;

    public EndReadsOverlapFinder1(int readLength, EmbeddingFunction<Sequence, int[]> readsEmbedding) {
        this.readLength = readLength;
        this.readsEmbedding = readsEmbedding;
    }

    private final TripletsReadInContigFinder finder = new TripletsReadInContigFinder();

    @Override
    public OverlapRegion getOverlap(EmbeddedSequence<int[]> a, EmbeddedSequence<int[]> b) {

        if (a.getObject().length() <= readLength || b.getObject().length() <= readLength)
            return null;

        // TODO slow, do it better ... precalculate ... for now good
        final int readIVLength = readLength - 2;
        final int[] aLeft = TripletsIndexVectorEmbedding.window(a.getProjected(), 0, readIVLength);
        final int aProjectedLength = a.getProjected().length;
        final int[] aRight = TripletsIndexVectorEmbedding.window(a.getProjected(), aProjectedLength - readIVLength, aProjectedLength);
        final int[] bLeft = TripletsIndexVectorEmbedding.window(b.getProjected(), 0, readIVLength);
        final int bProjectedLength = b.getProjected().length;
        final int[] bRight = TripletsIndexVectorEmbedding.window(b.getProjected(), bProjectedLength - readIVLength, bProjectedLength);

        // case when
        // aaaaaaaaaaaaaaaaaaaaa
        //             bbbbbbbbbbbbbbbbbbbbbbbb
        final double[] left = new double[4], right = new double[4];
        final int[] aLeftEnd = new int[4], aRightEnd = new int[4],
                bLeftEnd = new int[4], bRightEnd = new int[4];

        final ReadInContig case1A = finder.findReadInContig(bLeft, a.getProjected(), readLength);
        final ReadInContig case1B = finder.findReadInContig(aRight, b.getProjected(), readLength);
        left[0] = case1A.getDistance();
        right[0] = case1B.getDistance();
        aLeftEnd[0] = case1A.getContigStart();
        aRightEnd[0] = a.getObject().length();
        bLeftEnd[0] = 0;
        bRightEnd[0] = case1B.getContigEnd();

        // bbbbbbbbbbbbbbbbbbbbb
        //              aaaaaaaaaaaaaaaaaaaaaaaa
        final ReadInContig case2A = finder.findReadInContig(aLeft, b.getProjected(), readLength);
        final ReadInContig case2B = finder.findReadInContig(bRight, a.getProjected(), readLength);
        left[1] = case2A.getDistance();
        right[1] = case2B.getDistance();
        aLeftEnd[1] = 0;
        aRightEnd[1] = case2B.getContigEnd();
        bLeftEnd[1] = case2A.getContigStart();
        bRightEnd[1] = b.getObject().length();

        // aaaaaaaaaaaaaaaaaaaaaaaa
        //        bbbbbbbb
        final ReadInContig case3AL = finder.findReadInContig(bLeft, a.getProjected(), readLength);
        final ReadInContig case3AR = finder.findReadInContig(bRight, a.getProjected(), readLength);
        left[2] = case3AL.getDistance();
        right[2] = case3AR.getDistance();
        aLeftEnd[2] = case3AL.getContigStart();
        aRightEnd[2] = case3AR.getContigEnd();
        bLeftEnd[2] = 0;
        bRightEnd[2] = b.getObject().length();

        //         aaaaaaaa
        // bbbbbbbbbbbbbbbbbbbbbbbb
        final ReadInContig case4BL = finder.findReadInContig(aLeft, b.getProjected(), readLength);
        final ReadInContig case4BR = finder.findReadInContig(aRight, b.getProjected(), readLength);
        left[3] = case4BL.getDistance();
        right[3] = case4BR.getDistance();
        aLeftEnd[3] = 0;
        aRightEnd[3] = a.getObject().length();
        bLeftEnd[3] = case4BL.getContigStart();
        bRightEnd[3] = case4BR.getContigEnd();

        final double[] value = new double[4];
        for (int i = 0; i < 4; i++)
            value[i] = calculateValue(left[i], right[i], aLeftEnd[i], aRightEnd[i], bLeftEnd[i], bRightEnd[i]);

        final int minIndex = MathUtils.minIndex(value);

        if (value[minIndex] == Double.POSITIVE_INFINITY)
            return null;

        final OverlapRegion retval = SimpleOverlapRegion.byEnds(value[minIndex], aLeftEnd[minIndex],
                aRightEnd[minIndex], bLeftEnd[minIndex], bRightEnd[minIndex]);
        return new SimpleOverlapRegion(new UkkonenDistance().getDistance(a.getObject().subSequence(retval.getStartA(), retval.getEndA()),
                b.getObject().subSequence(retval.getStartB(), retval.getEndB())), retval.getLengthA(), retval.getEndA(), retval.getLengthB(), retval.getEndB());

    }

    private double calculateValue(double left, double right, int aLeftEnd, int aRightEnd, int bLeftEnd, int bRightEnd) {
        final int aLength = aRightEnd - aLeftEnd;
        final int bLength = bRightEnd - bLeftEnd;
        if (aLength <= 0 || bLength <= 0)
            return Double.POSITIVE_INFINITY;

        return ((left + right) / 128 * Math.min(aLength, bLength) + Math.abs(aLength - bLength)) / Math.max(aLength, bLength);
    }

    private double calculateDistance(double left, double right, int aLeftEnd, int aRightEnd, int bLeftEnd, int bRightEnd) {
        final int aLength = aRightEnd - aLeftEnd;
        final int bLength = bRightEnd - bLeftEnd;
        if (aLength <= 0 || bLength <= 0)
            return Double.POSITIVE_INFINITY;

        return (left + right) / 128 * Math.min(aLength, bLength) + Math.abs(aLength - bLength);
    }

}
