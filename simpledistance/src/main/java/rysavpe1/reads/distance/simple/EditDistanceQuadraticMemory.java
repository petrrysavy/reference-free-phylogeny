package rysavpe1.reads.distance.simple;

import rysavpe1.reads.distance.DistanceCalculator;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.utils.MathUtils;

/**
 *
 * @author Petr Ryšavý
 */
public class EditDistanceQuadraticMemory implements DistanceCalculator<Sequence, Integer>
{
    private final int matchPremium;
    private final int mismatchPenalty;
    private final int gapPenalty;

    public EditDistanceQuadraticMemory(int matchPremium, int mismatchPenalty, int gapPenalty)
    {
        this.matchPremium = matchPremium;
        this.mismatchPenalty = mismatchPenalty;
        this.gapPenalty = gapPenalty;
    }

    @Override
    public Integer getDistance(Sequence a, Sequence b)
    {
        final char[] aSeq = a.getSequence();
        final char[] bSeq = b.getSequence();
        // create emty table, first string in rows, second to the columns
        int[][] scoreMatrix = new int[aSeq.length + 1][bSeq.length + 1];

        // initialize first row
        for (int i = 0; i < bSeq.length; i++)
            scoreMatrix[0][i + 1] = scoreMatrix[0][i] + gapPenalty;
        // initialize first column
        for (int i = 0; i < aSeq.length; i++)
            scoreMatrix[i + 1][0] = scoreMatrix[i][0] + gapPenalty;
        // fill the rest of the table
        for (int i = 0; i < aSeq.length; i++) // i goes over rows, i.e. the first word
            for (int j = 0; j < bSeq.length; j++)
                scoreMatrix[i + 1][j + 1] = MathUtils.min(
                    // look to the left
                    scoreMatrix[i + 1][j] + gapPenalty,
                    // look to the top
                    scoreMatrix[i][j + 1] + gapPenalty,
                    // try the diagonal
                    scoreMatrix[i][j] + (aSeq[i] == bSeq[j] ? -matchPremium : mismatchPenalty));

        return scoreMatrix[aSeq.length][bSeq.length];
    }
}
