package rysavpe1.reads.margingap;

/**
 * This method implements linear border gap penaulty. The gap penaulty is zero
 * for any x from interval [0, a], then linearly increases on interval [a, b]
 * and is maximal on interval [b, 1]. The x value represents distance of x from
 * the beginning of the word. In this case a = 1.0 - b.
 *
 * @author Petr Ryšavý
 */
public class SymmetricLinearBorderGapPenaulty implements WeakBorderGapPenaulty {

    private final LinearBorderGapPenaulty borderGapPenaulty;

    public SymmetricLinearBorderGapPenaulty(double a, double maximalGapPenaulty) {
        this.borderGapPenaulty = new LinearBorderGapPenaulty(a, 1.0 - a, maximalGapPenaulty);
    }

    public SymmetricLinearBorderGapPenaulty(double freeGaps, int wordLenght, double maximalGapPenalty) {
        this((freeGaps - 0.5) / wordLenght, maximalGapPenalty);
    }

    @Override
    public double getBorderGapPenaulty(int gapIndex, int wordLength) {
        return borderGapPenaulty.getBorderGapPenaulty(gapIndex, wordLength);
    }
}
