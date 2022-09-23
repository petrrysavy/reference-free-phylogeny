package rysavpe1.reads.combined;

import rysavpe1.reads.distance.DistanceCalculator;
import rysavpe1.reads.margingap.MarginGapPenaulty;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.utils.MathUtils;

/**
 * Edit distance calculation with different dealing with margin gaps.
 *
 * @author Petr Ryšavý
 */
public class ContigMarginGapEditDistance implements DistanceCalculator<Sequence, Double> {

    /** This is how much we get if we align two same symbols. */
    private final double matchPremium;
    /** This is how much we pay for aligning different symbols. */
    private final double mismatchPenalty;
    /** This is what we pay for a gap. */
    private final double gapPenalty;
    /** This tells us how much we pay for gaps at margins. */
    private final MarginGapPenaulty marginGapPenalty;

    /**
     * Creates new edit distance.
     * @param matchPremium This is how much we get if we align two same symbols.
     * @param mismatchPenalty This is how much we pay for aligning different
     * symbols.
     * @param gapPenalty This is what we pay for a gap.
     * @param marginGapPenalty This tells us how much we pay for gaps at
     * margins.
     */
    public ContigMarginGapEditDistance(double matchPremium, double mismatchPenalty, double gapPenalty, MarginGapPenaulty marginGapPenalty) {
        this.matchPremium = matchPremium;
        this.mismatchPenalty = mismatchPenalty;
        this.gapPenalty = gapPenalty;
        this.marginGapPenalty = marginGapPenalty;
    }

    @Override
    public Double getDistance(Sequence a, Sequence b) {
        if (a.length() <= b.length()) return getDistanceOfReadAndContig(a, b);
        else return getDistanceOfReadAndContig(b, a);
    }

    private double getDistanceOfReadAndContig(Sequence read, Sequence contig) {
        // a goes over rows of the matrix, b over columns
        final char[] readSeq = read.getSequence();
        final char[] contigSeq = contig.getSequence();
//        System.err.println("read "+read + " contig "+contig);
        // create emty table, first string in rows, second to the columns
        double[] scoreMatrixCurrent = new double[contigSeq.length + 1];
        double[] scoreMatrixLast = new double[contigSeq.length + 1];
        double[] swap;
        // and get the penalty scores for the read lengths
        final double[] readMarginPenalty = marginGapPenalty.build(readSeq.length);
        final double[] contigMarginPenalty = buildContigMarginPenalty(readMarginPenalty, contigSeq.length);
//        System.err.println("readMarginPenalty "+Arrays.toString(readMarginPenalty));
//        System.err.println("contigMarginPenalty "+Arrays.toString(contigMarginPenalty));

        // initialize first row
        for (int j = 0; j < contigSeq.length; j++)
            scoreMatrixLast[j + 1] = scoreMatrixLast[j] + contigMarginPenalty[j];
        // fill the rest of the table
        for (int i = 0; i < readSeq.length - 1; i++) { // i goes over rows, i.e. the first word
            scoreMatrixCurrent[0] = scoreMatrixLast[0] + readMarginPenalty[i];
            for (int j = 0; j < contigSeq.length - 1; j++)
                scoreMatrixCurrent[j + 1] = MathUtils.min(
                        // look to the left
                        scoreMatrixCurrent[j] + gapPenalty,
                        // look to the top
                        scoreMatrixLast[j + 1] + gapPenalty,
                        // try the diagonal
                        scoreMatrixLast[j] + (readSeq[i] == contigSeq[j] ? -matchPremium : mismatchPenalty));
            scoreMatrixCurrent[contigSeq.length] = MathUtils.min(
                    scoreMatrixCurrent[contigSeq.length - 1] + gapPenalty,
                    scoreMatrixLast[contigSeq.length] + readMarginPenalty[readSeq.length - i - 1],
                    scoreMatrixLast[contigSeq.length - 1] + (readSeq[i] == contigSeq[contigSeq.length - 1] ? -matchPremium : mismatchPenalty));

            // swap the score matrix line
            swap = scoreMatrixCurrent;
            scoreMatrixCurrent = scoreMatrixLast;
            scoreMatrixLast = swap;
        }

        scoreMatrixCurrent[0] = scoreMatrixLast[0] + readMarginPenalty[readSeq.length - 1];
        // and the last row
        for (int j = 0; j < contigSeq.length - 1; j++)
            scoreMatrixCurrent[j + 1] = MathUtils.min(
                    // look to the left
                    scoreMatrixCurrent[j] + contigMarginPenalty[contigSeq.length - j - 1],
                    // look to the top
                    scoreMatrixLast[j + 1] + gapPenalty,
                    // try the diagonal
                    scoreMatrixLast[j] + (readSeq[readSeq.length - 1] == contigSeq[j] ? -matchPremium : mismatchPenalty));
        scoreMatrixCurrent[contigSeq.length] = MathUtils.min(
                scoreMatrixCurrent[contigSeq.length - 1] + contigMarginPenalty[0],
                scoreMatrixLast[contigSeq.length] + readMarginPenalty[0],
                scoreMatrixLast[contigSeq.length - 1] + (readSeq[readSeq.length - 1] == contigSeq[contigSeq.length - 1] ? -matchPremium : mismatchPenalty));

        return scoreMatrixCurrent[contigSeq.length];
    }

    private double[] buildContigMarginPenalty(double[] readMarginPenalty, int contigLength) {
        assert (readMarginPenalty.length <= contigLength);
        final double[] contigPenalty = new double[contigLength];
        System.arraycopy(readMarginPenalty, 0, contigPenalty, contigLength - readMarginPenalty.length, readMarginPenalty.length);
        return contigPenalty;
    }
}
