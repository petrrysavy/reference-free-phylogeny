package rysavpe1.reads.distance;

import java.util.Arrays;
import java.util.List;

/**
 * @author Petr Ryšavý
 * @param <T>
 * @param <U>
 */
public interface SimilarityCalculator<T, U extends Number> extends Measure {

    public U getSimilarity(T a, T b);

    public default double[][] getSimilarityMatrix(T[] a, T[] b) {
        return getSimilarityMatrix(Arrays.asList(a), Arrays.asList(b));
    }

    public default double[][] getSimilarityMatrix(T[] values) {
        final boolean isSymmetric = isSymmetric();

        final double[][] similarity = new double[values.length][values.length];
        for (int i = 0; i < similarity.length; i++)
            if (isSymmetric)
                for (int j = i + 1; j < values.length; j++)
                    similarity[j][i] = similarity[i][j] = getSimilarity(values[i], values[j]).doubleValue();
            else
                for (int j = 0; j < values.length; j++)
                    similarity[i][j] = getSimilarity(values[i], values[j]).doubleValue();
        return similarity;
    }

    public default double[][] getSimilarityMatrix(List<T> aList, List<T> bList) {
        final boolean isSymmetric = isSymmetric() && aList.equals(bList);

        final double[][] similarity = new double[aList.size()][bList.size()];
        for (int i = 0; i < similarity.length; i++)
            if (isSymmetric)
                for (int j = i + 1; j < bList.size(); j++)
                    similarity[j][i] = similarity[i][j] = getSimilarity(aList.get(i), bList.get(j)).doubleValue();
            else
                for (int j = 0; j < bList.size(); j++)
                    similarity[i][j] = getSimilarity(aList.get(i), bList.get(j)).doubleValue();
        return similarity;
    }
}
