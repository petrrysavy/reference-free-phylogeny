package rysavpe1.reads.distance;

import java.util.Arrays;
import java.util.List;

/**
 * A class that is capable of distance calculation.
 *
 * @author Petr Ryšavý
 * @param <T> The type of compared objects.
 * @param <U> The numerical value of the distance.
 */
public interface DistanceCalculator<T, U extends Number> extends Measure {

    /**
     * Calculates the distance.
     * @param a The first object.
     * @param b The second object.
     * @return Distance of {@code a} and {@code b} (in this order).
     */
    public U getDistance(T a, T b);

    /**
     * Gets matrix of distances between each pair of objects.
     * @param a List of first arguments.
     * @param b List of second arguments.
     * @return Matrix that contains distance of i-th element from the first list
     * and j-th from the second.
     */
    public default double[][] getDistanceMatrix(T[] a, T[] b) {
        return getDistanceMatrix(Arrays.asList(a), Arrays.asList(b));
    }

    /**
     * Gets square matrix of distances.
     * @param values Values to compare.
     * @return Distance of i-th and j-th element of the list.
     */
    public default double[][] getDistanceMatrix(T[] values) {
        final boolean isSymmetric = isSymmetric();

        final double[][] distance = new double[values.length][values.length];
        for (int i = 0; i < distance.length; i++)
            if (isSymmetric)
                for (int j = i + 1; j < values.length; j++)
                    distance[j][i] = distance[i][j] = getDistance(values[i], values[j]).doubleValue();
            else
                for (int j = 0; j < values.length; j++)
                    distance[i][j] = getDistance(values[i], values[j]).doubleValue();
        return distance;
    }

    /**
     * Gets matrix of distances between each pair of objects.
     * @param aList List of first arguments.
     * @param bList List of second arguments.
     * @return Matrix that contains distance of i-th element from the first list
     * and j-th from the second.
     */
    public default double[][] getDistanceMatrix(List<T> aList, List<T> bList) {
        final boolean isSymmetric = isSymmetric() && aList.equals(bList);

        final double[][] distance = new double[aList.size()][bList.size()];
        for (int i = 0; i < distance.length; i++)
            if (isSymmetric)
                for (int j = i + 1; j < bList.size(); j++)
                    distance[j][i] = distance[i][j] = getDistance(aList.get(i), bList.get(j)).doubleValue();
            else
                for (int j = 0; j < bList.size(); j++)
                    distance[i][j] = getDistance(aList.get(i), bList.get(j)).doubleValue();
        return distance;
    }
}
