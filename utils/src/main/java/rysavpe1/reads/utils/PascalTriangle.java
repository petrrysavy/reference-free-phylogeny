package rysavpe1.reads.utils;

import java.math.BigInteger;

/**
 * Implementation of the Pascal triangle.
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class PascalTriangle {

    /** The value with the binomial coefficients. */
    private final BigInteger[][] binomial;

    /**
     * Constructs and pre-computes the binomial coefficients.
     * @param maxN The maximum {@code n} that can be passed to the class.
     */
    public PascalTriangle(int maxN) {
        binomial = new BigInteger[maxN + 1][maxN + 1];

        for (int i = 0; i <= maxN; i++)
            binomial[i][0] = binomial[i][i] = BigInteger.ONE;
        for (int i = 2; i <= maxN; i++)
            for (int j = 1; j < i; j++)
                binomial[i][j] = binomial[i - 1][j - 1].add(binomial[i - 1][j]);
    }

    /**
     * Bets the binomial coeeficient {@code n over k}.
     * @param n The number of elements to choose from.
     * @param k The number of elements to choose.
     * @return The binomial coefficient.
     * @throws IllegalArgumentException If {@code k>n} or one of the arguments
     * is less than zero.
     * @throws IndexOutOfBoundsException If {@code n} is greater than
     * {@code maxN} from the constructor.
     */
    public BigInteger get(int n, int k) {
        if (n < 0 || k > n || k < 0)
            throw new IllegalArgumentException("Illegal combination number : " + n + " over " + k);

        return binomial[n][k];
    }
}
