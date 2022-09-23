package rysavpe1.reads.margingap;

import rysavpe1.reads.distance.DistanceCalculator;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.utils.MathUtils;

/**
 *
 * @author Petr Ryšavý
 */
public class WeakBorderEditDistance implements DistanceCalculator<Sequence, Double> {

    private final double matchPremium;
    private final double mismatchPenalty;
    private final double gapPenalty;
    private final WeakBorderGapPenaulty borderGapPeanulty;

    public WeakBorderEditDistance(double matchPremium, double mismatchPenalty, double gapPenalty, WeakBorderGapPenaulty borderGapPenaulty) {
        this.matchPremium = matchPremium;
        this.mismatchPenalty = mismatchPenalty;
        this.gapPenalty = gapPenalty;
        this.borderGapPeanulty = borderGapPenaulty;
    }

    @Override
    public Double getDistance(Sequence a, Sequence b) {
        // a goes over rows of the matrix, b over columns
        final char[] aSeq = a.getSequence();
        final char[] bSeq = b.getSequence();
        // create emty table, first string in rows, second to the columns
        double[] scoreMatrixCurrent = new double[bSeq.length + 1];
        double[] scoreMatrixLast = new double[bSeq.length + 1];
        double[] swap;

        // initialize first row
        for (int j = 0; j < bSeq.length; j++)
            scoreMatrixLast[j + 1] = scoreMatrixLast[j] + borderGapPeanulty.getBorderGapPenaulty(j, bSeq.length);
        // fill the rest of the table
        for (int i = 0; i < aSeq.length - 1; i++) { // i goes over rows, i.e. the first word
            scoreMatrixCurrent[0] = scoreMatrixLast[0] + borderGapPeanulty.getBorderGapPenaulty(i, aSeq.length);
            for (int j = 0; j < bSeq.length - 1; j++)
                scoreMatrixCurrent[j + 1] = MathUtils.min(
                        // look to the left
                        scoreMatrixCurrent[j] + gapPenalty,
                        // look to the top
                        scoreMatrixLast[j + 1] + gapPenalty,
                        // try the diagonal
                        scoreMatrixLast[j] + (aSeq[i] == bSeq[j] ? -matchPremium : mismatchPenalty));
            scoreMatrixCurrent[bSeq.length] = MathUtils.min(
                    scoreMatrixCurrent[bSeq.length - 1] + gapPenalty,
                    scoreMatrixLast[bSeq.length] + borderGapPeanulty.getBorderGapPenaulty(aSeq.length - i - 1, aSeq.length),
                    scoreMatrixLast[bSeq.length - 1] + (aSeq[i] == bSeq[bSeq.length - 1] ? -matchPremium : mismatchPenalty));

            // swap the score matrix line
            swap = scoreMatrixCurrent;
            scoreMatrixCurrent = scoreMatrixLast;
            scoreMatrixLast = swap;
        }

        scoreMatrixCurrent[0] = scoreMatrixLast[0] + borderGapPeanulty.getBorderGapPenaulty(aSeq.length - 1, aSeq.length);
        // and the last row
        for (int j = 0; j < bSeq.length - 1; j++)
            scoreMatrixCurrent[j + 1] = MathUtils.min(
                    // look to the left
                    scoreMatrixCurrent[j] + borderGapPeanulty.getBorderGapPenaulty(bSeq.length - j - 1, bSeq.length),
                    // look to the top
                    scoreMatrixLast[j + 1] + gapPenalty,
                    // try the diagonal
                    scoreMatrixLast[j] + (aSeq[aSeq.length - 1] == bSeq[j] ? -matchPremium : mismatchPenalty));
        scoreMatrixCurrent[bSeq.length] = MathUtils.min(
                scoreMatrixCurrent[bSeq.length - 1] + borderGapPeanulty.getBorderGapPenaulty(0, bSeq.length),
                scoreMatrixLast[bSeq.length] + borderGapPeanulty.getBorderGapPenaulty(0, aSeq.length),
                scoreMatrixLast[bSeq.length - 1] + (aSeq[aSeq.length - 1] == bSeq[bSeq.length - 1] ? -matchPremium : mismatchPenalty));

        return scoreMatrixCurrent[bSeq.length];
    }
}
