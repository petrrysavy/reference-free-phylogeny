package rysavpe1.reads.distance.simple;

import rysavpe1.reads.distance.DistanceCalculator;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.utils.MathUtils;

/**
 * This penalizes gaps only in the shorter sequence. Used fror the case when
 * sequence lengths are different.
 *
 * @author Petr Ryšavý
 */
public class ShorterInLongerEditDistance implements DistanceCalculator<Sequence, Integer> {

    private final int matchPremium;
    private final int mismatchPenalty;
    private final int gapPenalty;

    public ShorterInLongerEditDistance(int matchPremium, int mismatchPenalty, int gapPenalty) {
        this.matchPremium = matchPremium;
        this.mismatchPenalty = mismatchPenalty;
        this.gapPenalty = gapPenalty;
    }

    public ShorterInLongerEditDistance() {
        this(0, 1, 1);
    }

    @Override
    public Integer getDistance(Sequence a, Sequence b) {
        // make sure that a (row sequence) is the shorter one
        if (a.length() > b.length()) {
            return getDistance(b, a);
        }

        final char[] aSeq = a.getSequence();
        final char[] bSeq = b.getSequence();
        // create emty table, first string in rows, second to the columns
        int[] scoreMatrixCurrent = new int[bSeq.length + 1];
        int[] scoreMatrixLast = new int[bSeq.length + 1];
        int[] swap;

        // do not initialize first row - it should be filled with 0s
        for (int j = 0; j < bSeq.length; j++) {
            scoreMatrixLast[j + 1] = scoreMatrixLast[j] + gapPenalty;
        }
        // fill the rest of the table
        for (int i = 0; i < aSeq.length; i++) { // i goes over rows, i.e. the first word
            scoreMatrixCurrent[0] = scoreMatrixLast[0] + gapPenalty;
            for (int j = 0; j < bSeq.length; j++) {
                scoreMatrixCurrent[j + 1] = MathUtils.min(
                        // look to the left
                        scoreMatrixCurrent[j] + gapPenalty,
                        // look to the top
                        scoreMatrixLast[j + 1] + gapPenalty,
                        // try the diagonal
                        scoreMatrixLast[j] + (aSeq[i] == bSeq[j] ? -matchPremium : mismatchPenalty));
            }

            // swap the score matrix line
            swap = scoreMatrixCurrent;
            scoreMatrixCurrent = scoreMatrixLast;
            scoreMatrixLast = swap;
        }

        // now find the minimum in the last row
        return scoreMatrixLast[bSeq.length];
    }
}
