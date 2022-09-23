package rysavpe1.reads.overlap;

import rysavpe1.reads.model.Sequence;

/**
 *
 * @author petr
 */
public class SimilarityOverlapFinder implements OverlapFinder {

    private final int minLength;
    private final int matchPremium = 5;
    private final int gapCost = -4;
    private final int mismatchPenalty = -4;

    public SimilarityOverlapFinder(int minLength) {
        this.minLength = minLength;
    }

    @Override
    public OverlapRegion getOverlap(Sequence a, Sequence b) {
        final char[] aSeq = a.getSequence();
        final char[] bSeq = b.getSequence();
        // create empty table, first string in rows, second to the columsn
        int[] distanceCurrent = new int[bSeq.length + 1];
        int[] distanceLast = new int[bSeq.length + 1];
        int[] lengthACurrent = new int[bSeq.length + 1];
        int[] lengthALast = new int[bSeq.length + 1];
        int[] lengthBCurrent = new int[bSeq.length + 1];
        int[] lengthBLast = new int[bSeq.length + 1];
        int[] similarityCurrent = new int[bSeq.length + 1];
        int[] similarityLast = new int[bSeq.length + 1];
        int[] swap;
        // first row does not need to be initialized - it is all zeroes (thanks Java)
        OverlapRegion region = DummyOverlapRegion.DUMMY;
        int maxSimilarity = Integer.MIN_VALUE;

        for (int i = 0; i < aSeq.length; i++) {
            for (int j = 0; j < bSeq.length; j++) {
                final int matchCost = aSeq[i] == bSeq[j] ? matchPremium : mismatchPenalty;
                final int gapInA = similarityCurrent[j] + gapCost;
                final int gapInB = similarityLast[j + 1] + gapCost;
                final int diagonal = similarityLast[j] + matchCost;

                if (diagonal > gapInA && diagonal > gapInB) { // diagonal wins
                    similarityCurrent[j + 1] = similarityLast[j] + matchCost;
                    distanceCurrent[j + 1] = distanceLast[j] + aSeq[i] == bSeq[j] ? 0 : 1;
                    lengthACurrent[j + 1] = lengthALast[j] + 1;
                    lengthBCurrent[j + 1] = lengthBLast[j] + 1;
                } else if (gapInA > gapInB) { // gap in A is the smallest
                    similarityCurrent[j + 1] = similarityCurrent[j] + gapCost;
                    distanceCurrent[j + 1] = distanceCurrent[j] + 1;
                    lengthACurrent[j + 1] = lengthACurrent[j];
                    lengthBCurrent[j + 1] = lengthBLast[j] + 1;
                } else { // gap in B is the right choice
                    similarityCurrent[j + 1] = similarityLast[j + 1] + gapCost;
                    distanceCurrent[j + 1] = distanceLast[j + 1] + 1;
                    lengthACurrent[j + 1] = lengthALast[j + 1] + 1;
                    lengthBCurrent[j + 1] = lengthBLast[j + 1];
                }
            }

            // we need to check whether we did find an optimum
            if (similarityCurrent[bSeq.length] > maxSimilarity
                    && lengthACurrent[bSeq.length] >= minLength && lengthBCurrent[bSeq.length] >= minLength) {
                region = new SimpleOverlapRegion(distanceCurrent[bSeq.length], lengthACurrent[bSeq.length], i + 1, lengthBCurrent[bSeq.length], bSeq.length);
                maxSimilarity = similarityCurrent[bSeq.length];
            }

            // swap the last
            swap = distanceCurrent;
            distanceCurrent = distanceLast;
            distanceLast = swap;
            swap = lengthACurrent;
            lengthACurrent = lengthALast;
            lengthALast = swap;
            swap = lengthBCurrent;
            lengthBCurrent = lengthBLast;
            lengthBLast = swap;
            swap = similarityCurrent;
            similarityCurrent = similarityLast;
            similarityLast = swap;
        }

        // and also we need to check whether we did find an optimum in the last row
        for (int j = 1; j < bSeq.length; j++) // <, not <=, bSeq.lenght was already done
            if (similarityLast[j] >  maxSimilarity
                    && lengthALast[j] >= minLength && lengthBLast[j] >= minLength) {
                region = new SimpleOverlapRegion(distanceLast[j], lengthALast[j], aSeq.length, lengthBLast[j], j);
                maxSimilarity = similarityLast[j];
            }

        return region == DummyOverlapRegion.DUMMY ? null : region;
    }

}
