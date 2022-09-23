package rysavpe1.reads.utils;

import java.util.Collection;

/**
 *
 * @author Petr Ryšavý
 */
public final class MathUtils {

    private MathUtils() {

    }

    public static int max(int... arr) {
        int max = arr[0];
        for (int i = 1; i < arr.length; i++)
            if (arr[i] > max)
                max = arr[i];
        return max;
    }
    
    public static int max(int a, int b, int c) {
        if (a >= b && a >= c)
            return a;
        return b >= c ? b : c;
    }

    public static int min(int... arr) {
        int min = arr[0];
        for (int i = 1; i < arr.length; i++)
            if (arr[i] < min)
                min = arr[i];
        return min;
    }

    public static int min(int a, int b, int c) {
        if (a <= b && a <= c)
            return a;
        return b <= c ? b : c;
    }

    public static double min(double... arr) {
        double min = arr[0];
        for (int i = 1; i < arr.length; i++)
            if (arr[i] < min)
                min = arr[i];
        return min;
    }

    public static int min(int[] arr, int firstIndex, int toIndex) {
        assert (firstIndex < toIndex);

        int min = arr[firstIndex];
        for (int i = firstIndex + 1; i < toIndex; i++)
            if (arr[i] < min)
                min = arr[i];
        return min;
    }

    public static int colMin(int[][] arr, int columnIndex, int firstIndex, int lastIndex) {
        assert (firstIndex < lastIndex);

        int min = arr[firstIndex][columnIndex];
        for (int i = firstIndex + 1; i < lastIndex; i++)
            if (arr[i][columnIndex] < min)
                min = arr[i][columnIndex];
        return min;
    }

    public static int maxIndex(int... arr) {
        if (arr.length == 0)
            return -1;

        int maxIndex = 0;
        for (int i = 1; i < arr.length; i++)
            if (arr[i] > arr[maxIndex])
                maxIndex = i;
        return maxIndex;
    }

    public static int minIndex(int... arr) {
        if (arr.length == 0)
            return -1;

        int minIndex = 0;
        for (int i = 1; i < arr.length; i++)
            if (arr[i] < arr[minIndex])
                minIndex = i;
        return minIndex;
    }

    public static int minIndex(double... arr) {
        if (arr.length == 0)
            return -1;

        int minIndex = 0;
        for (int i = 1; i < arr.length; i++)
            if (arr[i] < arr[minIndex])
                minIndex = i;
        return minIndex;
    }

    public static double min(double a, double b, double c) {
        if (a <= b && a <= c)
            return a;
        return b <= c ? b : c;
    }

    public static double min(double a, double b, double c, double d) {
        return Math.min(Math.min(a, b), Math.min(c, d));
    }

    public static int sum(int[] vec) {
        int sum = 0;
        for (int i : vec)
            sum += i;
        return sum;
    }

    public static int sum(int[] vec, int fromIndex, int toIndex) {
        int sum = 0;
        toIndex = Math.min(vec.length, toIndex);
        for (int i = fromIndex; i < toIndex; i++)
            sum += vec[i];
        return sum;
    }

    public static double sum(double[] vec, int fromIndex, int toIndex) {
        double sum = 0;
        toIndex = Math.min(vec.length, toIndex);
        for (int i = fromIndex; i < toIndex; i++)
            sum += vec[i];
        return sum;
    }

    public static double sum(double[] vec) {
        double sum = 0;
        for (double i : vec)
            sum += i;
        return sum;
    }

    public static long sum(long[] vec) {
        long sum = 0;
        for (long i : vec)
            sum += i;
        return sum;
    }

    public static double sum(Collection<? extends Double> col) {
        double sum = 0.0;
        for (Double d : col) sum += d.doubleValue();
        return sum;
    }

    public static int[] addTo(int[] target, int[] toAdd) {
        for (int i = 0; i < target.length; i++)
            target[i] += toAdd[i];
        return target;
    }

    public static double[] addTo(double[] target, double[] toAdd) {
        for (int i = 0; i < target.length; i++)
            target[i] += toAdd[i];
        return target;
    }

    public static int sumSquares(int[] vec) {
        int sum = 0;
        for (int i : vec)
            sum += i * i;
        return sum;
    }

    public static int sumSquares(int[][] matrix) {
        int sum = 0;
        for (int[] vec : matrix)
            sum += sumSquares(vec);
        return sum;
    }

    public static int[] rowMinIndex(double[][] matrix) {
        final int[] min = new int[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i].length == 0)
                min[i] = -1;
            else {
                int minIndex = 0;
                for (int j = 1; j < matrix[i].length; j++)
                    if (matrix[i][j] < matrix[i][minIndex])
                        minIndex = j;
                min[i] = minIndex;
            }
        }
        return min;
    }

    public static int[] rowMaxIndex(double[][] matrix) {
        final int[] max = new int[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i].length == 0)
                max[i] = -1;
            else {
                int maxIndex = 0;
                for (int j = 1; j < matrix[i].length; j++)
                    if (matrix[i][j] > matrix[i][maxIndex])
                        maxIndex = j;
                max[i] = maxIndex;
            }
        }
        return max;
    }

    public static int[] colMinIndex(double[][] matrix) {
        if (matrix.length == 0)
            return new int[0];

        final int[] min = new int[matrix[0].length];
        for (int j = 0; j < matrix[0].length; j++) {
            int minIndex = 0;
            for (int i = 1; i < matrix.length; i++)
                if (matrix[i][j] < matrix[minIndex][j])
                    minIndex = i;
            min[j] = minIndex;
        }
        return min;
    }

    public static int[] colMaxIndex(double[][] matrix) {
        if (matrix.length == 0)
            return new int[0];

        final int[] max = new int[matrix[0].length];
        for (int j = 0; j < matrix[0].length; j++) {
            int maxIndex = 0;
            for (int i = 1; i < matrix.length; i++)
                if (matrix[i][j] > matrix[maxIndex][j])
                    maxIndex = i;
            max[j] = maxIndex;
        }
        return max;
    }

    public static int[] rowSum(int[][] matrix) {
        final int[] sum = new int[matrix.length];
        for (int i = 0; i < matrix.length; i++)
            sum[i] = sum(matrix[i]);
        return sum;
    }

    public static int[] colSum(int[][] matrix) {
        final int[] sum = new int[matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            addTo(sum, matrix[i]);
        return sum;
    }

    public static double[] rowSum(double[][] matrix) {
        final double[] sum = new double[matrix.length];
        for (int i = 0; i < matrix.length; i++)
            sum[i] = sum(matrix[i]);
        return sum;
    }

    public static double[] colSum(double[][] matrix) {
        final double[] sum = new double[matrix[0].length];
        for (int i = 0; i < matrix.length; i++)
            addTo(sum, matrix[i]);
        return sum;
    }

    public static double average(double a, double b) {
        return (a + b) / 2.0;
    }

    public static double average(double... array) {
        return sum(array) / array.length;
    }

    public static double average(long... array) {
        return ((double) sum(array)) / array.length;
    }

    public static double average(int... array) {
        return ((double) sum(array)) / array.length;
    }

    public static double weightedAverage(double weight, double a, double b) {
        assert (weight <= 1.0 && weight >= 0.0);
        return weight * a + (1.0 - weight) * b;
    }

    public static double weightedAverageSafeNaN(double weight, double a, double b) {
        assert (weight <= 1.0 && weight >= 0.0);
        if(weight < 1e-12) return b;
        if(weight >= (1.0 - 1e-12)) return a;
        return weight * a + (1.0 - weight) * b;
    }

    public static int pow(int a, int b) {
        assert (b >= 0);

        int result = 1;
        for (int i = 0; i < b; i++)
            result *= a;
        return result;
    }

    public static int lastZero(int[] arr) {
        int i = arr.length - 1;
        while (i >= 0 && arr[i] != 0)
            i--;
        return i;
    }

    public static double[][] averageDim3(long[][][] arr) {
        final double[][] averages = new double[arr.length][arr[0].length];
        for (int i = 0; i < arr.length; i++)
            for (int j = 0; j < arr[0].length; j++)
                averages[i][j] = average(arr[i][j]);
        return averages;
    }

    public static double[] averageRows(double[][] arr) {
        return divide(rowSum(arr), arr[0].length);
    }

    public static double[] averageCols(double[][] arr) {
        return divide(colSum(arr), arr.length);
    }

    public static double[] divide(double[] arr, double value) {
        for (int i = 0; i < arr.length; i++)
            arr[i] /= value;
        return arr;
    }

    public static double[][][] add(double[][][] arr1, double[][][] arr2) {
        assert arr1.length == arr2.length;
        final double[][][] result = new double[arr1.length][][];

        for (int i = 0; i < arr1.length; i++) {
            assert arr1[i].length == arr2[i].length;
            result[i] = new double[arr1[i].length][];

            for (int j = 0; j < arr1[i].length; j++) {
                assert arr1[i][j].length == arr2[i][j].length;
                result[i][j] = new double[arr1[i][j].length];

                for (int k = 0; k < arr1[i][j].length; k++) {
                    result[i][j][k] = arr1[i][j][k] + arr2[i][j][k];
                }
            }

        }

        return result;
    }

    public static long[][][] add(long[][][] arr1, long[][][] arr2) {
        assert arr1.length == arr2.length;
        final long[][][] result = new long[arr1.length][][];

        for (int i = 0; i < arr1.length; i++) {
            assert arr1[i].length == arr2[i].length;
            result[i] = new long[arr1[i].length][];

            for (int j = 0; j < arr1[i].length; j++) {
                assert arr1[i][j].length == arr2[i][j].length;
                result[i][j] = new long[arr1[i][j].length];

                for (int k = 0; k < arr1[i][j].length; k++) {
                    result[i][j][k] = arr1[i][j][k] + arr2[i][j][k];
                }
            }

        }

        return result;
    }

    public static int countGeqThan(int[] arr, int threshold) {
        int count = 0;
        for(int val  : arr)
            if(val >= threshold)
                count++;
        return count;
    }
    
    public static double[] lowerTriangleVector(double[][] matrix) {
        final double[] vec = new double[matrix.length * (matrix.length - 1) / 2];
        int k = 0;
        
        for(int i = 1; i < matrix.length; i++)
            for(int j = 0; j < i; j++)
                vec[k++] = matrix[i][j];
        return vec;
    }
    
    public static int roundUp(double value) {
        return (int) Math.ceil(value);
    }
    
    public static double logBase(double value, double base) {
        return Math.log(value) / Math.log(base);
    }
    
    public static double log2(double value) {
        return logBase(value, 2.0);
    }

    public static double[] normalize(int[] distro) {
        int sum = MathUtils.sum(distro);
        double[] result = new double[distro.length];
        for (int i = 0; i < distro.length; i++) result[i] = ((double)distro[i]) / sum;
        return result;
    }

    public static double[] normalize(long[] distro) {
        long sum = MathUtils.sum(distro);
        double[] result = new double[distro.length];
        for (int i = 0; i < distro.length; i++) result[i] = ((double)distro[i]) / sum;
        return result;
    }
    
}
