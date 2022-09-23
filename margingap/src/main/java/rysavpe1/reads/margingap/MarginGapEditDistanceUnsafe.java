package rysavpe1.reads.margingap;

import rysavpe1.reads.distance.DistanceCalculator;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.utils.ArrayUtils;
import rysavpe1.reads.utils.MathUtils;

/**
 * Thread unsafe implementation of margin gap edit distance.
 *
 * @author Petr Ryšavý
 */
public class MarginGapEditDistanceUnsafe implements DistanceCalculator<Sequence, Double>
{
    private final double matchPremium;
    private final double mismatchPenalty;
    private final double gapPenalty;
    private final MarginGapPenaulty marginGapPenalty;
    private double[] scoreMatrixCurrent;
    private double[] scoreMatrixLast;

    public MarginGapEditDistanceUnsafe(double matchPremium, double mismatchPenalty, double gapPenalty, MarginGapPenaulty marginGapPenalty)
    {
        this.matchPremium = matchPremium;
        this.mismatchPenalty = mismatchPenalty;
        this.gapPenalty = gapPenalty;
        this.marginGapPenalty = marginGapPenalty;
        this.scoreMatrixCurrent = new double[100];
        this.scoreMatrixLast = new double[100];
    }

    @Override
    public Double getDistance(Sequence a, Sequence b)
    {
        // a goes over rows of the matrix, b over columns
        final char[] aSeq = a.getSequence();
        final char[] bSeq = b.getSequence();
        // create emty table, first string in rows, second to the columns
        scoreMatrixCurrent = ArrayUtils.ensureLength(scoreMatrixCurrent, bSeq.length + 1);
        scoreMatrixLast = ArrayUtils.ensureLength(scoreMatrixLast, bSeq.length + 1);
        double[] swap;
        // and get the penalty scores for the read lengths
        final double[] aMarginPenalty = marginGapPenalty.build(aSeq.length);
        final double[] bMarginPenalty = marginGapPenalty.build(bSeq.length);

        // initialize first row
        scoreMatrixLast[0] = 0;
        for (int j = 0; j < bSeq.length; j++)
            scoreMatrixLast[j + 1] = scoreMatrixLast[j] + bMarginPenalty[j];
        // fill the rest of the table
        for (int i = 0; i < aSeq.length - 1; i++)
        { // i goes over rows, i.e. the first word
            scoreMatrixCurrent[0] = scoreMatrixLast[0] + aMarginPenalty[i];
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
                scoreMatrixLast[bSeq.length] + aMarginPenalty[aSeq.length - i - 1],
                scoreMatrixLast[bSeq.length - 1] + (aSeq[i] == bSeq[bSeq.length - 1] ? -matchPremium : mismatchPenalty));

            // swap the score matrix line
            swap = scoreMatrixCurrent;
            scoreMatrixCurrent = scoreMatrixLast;
            scoreMatrixLast = swap;
        }

        scoreMatrixCurrent[0] = scoreMatrixLast[0] + aMarginPenalty[aSeq.length -1];
        // and the last row
        for (int j = 0; j < bSeq.length - 1; j++)
            scoreMatrixCurrent[j + 1] = MathUtils.min(
                // look to the left
                scoreMatrixCurrent[j] + bMarginPenalty[bSeq.length - j -1],
                // look to the top
                scoreMatrixLast[j + 1] + gapPenalty,
                // try the diagonal
                scoreMatrixLast[j] + (aSeq[aSeq.length - 1] == bSeq[j] ? -matchPremium : mismatchPenalty));
        scoreMatrixCurrent[bSeq.length] = MathUtils.min(
            scoreMatrixCurrent[bSeq.length - 1] + bMarginPenalty[0],
            scoreMatrixLast[bSeq.length] + aMarginPenalty[0],
            scoreMatrixLast[bSeq.length - 1] + (aSeq[aSeq.length - 1] == bSeq[bSeq.length - 1] ? -matchPremium : mismatchPenalty));

        return scoreMatrixCurrent[bSeq.length];
    }
}
