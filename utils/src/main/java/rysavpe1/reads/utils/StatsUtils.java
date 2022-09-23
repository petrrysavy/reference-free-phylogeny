package rysavpe1.reads.utils;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Utility class that is capable of calculating various statistics.
 *
 * @author Petr Ryšavý
 */
public final class StatsUtils {

    /** Do not let anybody to instantiate the class. */
    private StatsUtils() {
    }

    /**
     * Calculates the Pearson's correlation coefficient.
     * @param x One random variable.
     * @param y Second random variable.
     * @return Correlation between {@code x} and {@code y}.
     * @see https://en.wikipedia.org/wiki/Pearson_correlation_coefficient
     */
    public static double getPearsonsCorrelationCoefficient(double[] x, double[] y) {
        if (x.length != y.length)
            throw new IllegalArgumentException("Pearson's correlation coefficient can be calculated only on random variables that are on the same domain.");

        final double xAverage = MathUtils.average(x);
        final double yAverage = MathUtils.average(y);

        double nominator = 0.0;
        double stdevX2 = 0.0;
        double stdevY2 = 0.0;

        for (int i = 0; i < x.length; i++) {
            final double xDiff = x[i] - xAverage;
            final double yDiff = y[i] - yAverage;

            nominator += xDiff * yDiff;
            stdevX2 += xDiff * xDiff;
            stdevY2 += yDiff * yDiff;
        }

        return nominator / Math.sqrt(stdevX2 * stdevY2);
    }

    public static double getPearsonsCorrelationCoefficient(int[] x, int[] y) {
        if (x.length != y.length)
            throw new IllegalArgumentException("Pearson's correlation coefficient can be calculated only on random variables that are on the same domain.");

        final double xAverage = MathUtils.average(x);
        final double yAverage = MathUtils.average(y);

        double nominator = 0.0;
        double stdevX2 = 0.0;
        double stdevY2 = 0.0;

        for (int i = 0; i < x.length; i++) {
            final double xDiff = x[i] - xAverage;
            final double yDiff = y[i] - yAverage;

            nominator += xDiff * yDiff;
            stdevX2 += xDiff * xDiff;
            stdevY2 += yDiff * yDiff;
        }

        return nominator / Math.sqrt(stdevX2 * stdevY2);
    }

    public static double getSpearmansCorrelationCoefficient(double[] x, double[] y) {
        if (x.length != y.length)
            throw new IllegalArgumentException("Spearmans's correlation coefficient can be calculated only on random variables that are on the same domain.");

        return getPearsonsCorrelationCoefficient(toRankArray(x), toRankArray(y));
    }

    public static double getSpearmansCorrelationCoefficient(int[] x, int[] y) {
        if (x.length != y.length)
            throw new IllegalArgumentException("Spearmans's correlation coefficient can be calculated only on random variables that are on the same domain.");

        return getPearsonsCorrelationCoefficient(toRankArray(x), toRankArray(y));
    }

    public static int[] toRankArray(double[] x) {
        final IntDoublePair[] xRankIndex = IntDoublePair.rankIndex(x);
        Arrays.sort(xRankIndex, Comparator.comparing(IntDoublePair::getValue2));
        final int[] rank = new int[x.length];
        for (int i = 0; i < rank.length; i++) // is is the final rank - xRankIndex is now sorted by value2
            rank[xRankIndex[i].value1] = i;
        return rank;
    }

    public static int[] toRankArray(int[] x) {
        final IntIntPair[] xRankIndex = IntIntPair.rankIndex(x);
        Arrays.sort(xRankIndex, Comparator.comparing(IntIntPair::getValue2));
        final int[] rank = new int[x.length];
        for (int i = 0; i < rank.length; i++) // is is the final rank - xRankIndex is now sorted by value2
            rank[xRankIndex[i].value1] = i;
        return rank;
    }

    public static double mean(double[] x) {
        double sum = 0.0;
        for (double val : x)
            sum += val;
        return sum / x.length;
    }

    public static double stdev(double[] x) {
        double sum = 0.0;
        double sumsq = 0.0;
        for (double val : x) {
            sum += val;
            sumsq += val * val;
        }
        return Math.sqrt(((double) x.length) * sumsq - sum * sum) / x.length;
    }

    /**
     * The Kullback-Leibner divergence, sometimes called relative entropy.
     * @param groudTruthP The data, what we expect.
     * @param approximationQ A model approximation.
     * @return The relative entropy.
     * @see https://en.wikipedia.org/wiki/Kullback%E2%80%93Leibler_divergence
     */
    public static double KLDivergence(double[] groudTruthP, double[] approximationQ) {
        return KLDivergence(groudTruthP, approximationQ, Math.E);
    }
    /**
     * The Kullback-Leibner divergence, sometimes called relative entropy.
     * @param groudTruthP The data, what we expect.
     * @param approximationQ A model approximation.
     * @return The relative entropy.
     * @see https://en.wikipedia.org/wiki/Kullback%E2%80%93Leibler_divergence
     */
    public static double KLDivergence(double[] groudTruthP, double[] approximationQ, double logBase) {
        if (groudTruthP.length != approximationQ.length)
            throw new IllegalArgumentException("Distributions need to have the same universe, found : " + groudTruthP.length + ", " + approximationQ.length);

        double divergence = 0.0;
        for (int i = 0; i < groudTruthP.length; i++)
            divergence += groudTruthP[i] * MathUtils.logBase(groudTruthP[i] / approximationQ[i], logBase);
        return divergence;
    }

    private static final class IntDoublePair extends Pair<Integer, Double> {

        public IntDoublePair(Integer value1, Double value2) {
            super(value1, value2);
        }

        private static IntDoublePair[] rankIndex(double[] x) {
            final IntDoublePair[] xRank = new IntDoublePair[x.length];
            for (int i = 0; i < x.length; i++)
                xRank[i] = new IntDoublePair(i, x[i]);
            return xRank;
        }
    }

    private static final class IntIntPair extends Pair<Integer, Integer> {

        public IntIntPair(Integer value1, Integer value2) {
            super(value1, value2);
        }

        private static IntIntPair[] rankIndex(int[] x) {
            final IntIntPair[] xRank = new IntIntPair[x.length];
            for (int i = 0; i < x.length; i++)
                xRank[i] = new IntIntPair(i, x[i]);
            return xRank;
        }
    }
}
