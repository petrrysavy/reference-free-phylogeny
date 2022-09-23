package rysavpe1.reads.utils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

/**
 * @author Petr Ryšavý
 */
public class MathUtilsTest {

    @Test
    public void testMaxArr_Int() {
        assertThat(MathUtils.max(1, 2, 3, 4, 5, 6), is(equalTo(6)));
        assertThat(MathUtils.max(6, 5, 4, 3, 2, 1), is(equalTo(6)));
        assertThat(MathUtils.max(5, 74, 7, 5, 1, 65, -4, 4, 5), is(equalTo(74)));
    }

    @Test
    public void testMinArr_Int() {
        assertThat(MathUtils.min(1, 2, 3, 4, 5, 6), is(equalTo(1)));
        assertThat(MathUtils.min(6, 5, 4, 3, 2, 1), is(equalTo(1)));
        assertThat(MathUtils.min(5, 74, 7, 5, 1, 65, -4, 4, 5), is(equalTo(-4)));
    }

    @Test
    public void testMin_Int() {
        assertThat(MathUtils.min(1, 2, 3), is(equalTo(1)));
        assertThat(MathUtils.min(1, 3, 2), is(equalTo(1)));
        assertThat(MathUtils.min(2, 1, 3), is(equalTo(1)));
        assertThat(MathUtils.min(2, 3, 1), is(equalTo(1)));
        assertThat(MathUtils.min(3, 2, 1), is(equalTo(1)));
        assertThat(MathUtils.min(3, 1, 2), is(equalTo(1)));
    }

    @Test
    public void testMinArr_Double() {
        assertThat(MathUtils.min(1.0, 2, 3, 4, 5, 6), is(equalTo(1.0)));
        assertThat(MathUtils.min(6.0, 5, 4, 3, 2, 1), is(equalTo(1.0)));
        assertThat(MathUtils.min(5.0, 74, 7, 5, 1, 65, -4, 4, 5), is(equalTo(-4.0)));
    }

    @Test
    public void testMinArr_Range_Int() {
        assertThat(MathUtils.min(ArrayUtils.asArray(1, 2, 3, 4, 5, 6), 3, 5), is(equalTo(4)));
        assertThat(MathUtils.min(ArrayUtils.asArray(6, 5, 4, 3, 2, 1), 3, 5), is(equalTo(2)));
        assertThat(MathUtils.min(ArrayUtils.asArray(5, 74, 7, 5, 1, 65, -4, 4, 5), 3, 6), is(equalTo(1)));
    }

    @Test
    public void testColMin() {
        final int[][] arr = new int[][]{
            {1, 2, 3, 4},
            {5, 6, 4, 1},
            {0, 4, 5, 2}
        };
        assertThat(MathUtils.colMin(arr, 0, 0, 3), is(equalTo(0)));
        assertThat(MathUtils.colMin(arr, 1, 0, 3), is(equalTo(2)));
        assertThat(MathUtils.colMin(arr, 2, 0, 3), is(equalTo(3)));
        assertThat(MathUtils.colMin(arr, 3, 0, 3), is(equalTo(1)));
        assertThat(MathUtils.colMin(arr, 0, 2, 3), is(equalTo(0)));
        assertThat(MathUtils.colMin(arr, 1, 2, 3), is(equalTo(4)));
        assertThat(MathUtils.colMin(arr, 2, 2, 3), is(equalTo(5)));
        assertThat(MathUtils.colMin(arr, 3, 2, 3), is(equalTo(2)));
        assertThat(MathUtils.colMin(arr, 0, 0, 2), is(equalTo(1)));
        assertThat(MathUtils.colMin(arr, 1, 0, 2), is(equalTo(2)));
        assertThat(MathUtils.colMin(arr, 2, 0, 2), is(equalTo(3)));
        assertThat(MathUtils.colMin(arr, 3, 0, 2), is(equalTo(1)));
    }

    @Test
    public void testMaxIndexArr_Int() {
        assertThat(MathUtils.maxIndex(1, 2, 3, 4, 5, 6), is(equalTo(5)));
        assertThat(MathUtils.maxIndex(6, 5, 4, 3, 2, 1), is(equalTo(0)));
        assertThat(MathUtils.maxIndex(5, 74, 7, 5, 1, 65, -4, 4, 5), is(equalTo(1)));
    }

    @Test
    public void testMin_Double() {
        assertThat(MathUtils.min(1.0, 2.0, 3.0), is(equalTo(1.0)));
        assertThat(MathUtils.min(1.0, 3.0, 2.0), is(equalTo(1.0)));
        assertThat(MathUtils.min(2.0, 1.0, 3.0), is(equalTo(1.0)));
        assertThat(MathUtils.min(2.0, 3.0, 1.0), is(equalTo(1.0)));
        assertThat(MathUtils.min(3.0, 2.0, 1.0), is(equalTo(1.0)));
        assertThat(MathUtils.min(3.0, 1.0, 2.0), is(equalTo(1.0)));
    }

    @Test
    public void testMin4_Double() {
        assertThat(MathUtils.min(1.0, 2.0, 3.0, 4.0), is(equalTo(1.0)));
        assertThat(MathUtils.min(1.0, 3.0, 4.0, 2.0), is(equalTo(1.0)));
        assertThat(MathUtils.min(2.0, 4.0, 1.0, 3.0), is(equalTo(1.0)));
        assertThat(MathUtils.min(4.0, 2.0, 3.0, 1.0), is(equalTo(1.0)));
        assertThat(MathUtils.min(3.0, 4.0, 2.0, 1.0), is(equalTo(1.0)));
        assertThat(MathUtils.min(3.0, 1.0, 4.0, 2.0), is(equalTo(1.0)));
    }

    @Test
    public void testSum_IntArr() {
        assertThat(MathUtils.sum(ArrayUtils.asArray(1, 2, 3, 4, 5, 6, 7, 8, 9)), is(equalTo(45)));
        assertThat(MathUtils.sum(ArrayUtils.asArray(7, 8, 5, 1, 6, 9, 2, 3, 4)), is(equalTo(45)));
        assertThat(MathUtils.sum(ArrayUtils.asArray(1, 2, 3)), is(equalTo(6)));
        assertThat(MathUtils.sum(new int[0]), is(equalTo(0)));
    }

    @Test
    public void testSum_IntArr_3args() {
        assertThat(MathUtils.sum(ArrayUtils.asArray(1, 2, 3, 4, 5, 6, 7, 8, 9), 3, 6), is(equalTo(15)));
        assertThat(MathUtils.sum(ArrayUtils.asArray(7, 8, 5, 1, 6, 9, 2, 3, 4), 3, 6), is(equalTo(16)));
        assertThat(MathUtils.sum(ArrayUtils.asArray(1, 2, 3), 2, 3), is(equalTo(3)));
        assertThat(MathUtils.sum(new int[0], 0, 0), is(equalTo(0)));
    }

    @Test
    public void testSum_DoubleArr() {
        assertThat(MathUtils.sum(ArrayUtils.asArray(1.0, 2, 3, 4, 5, 6, 7, 8, 9)), is(equalTo(45.0)));
        assertThat(MathUtils.sum(ArrayUtils.asArray(7.0, 8, 5, 1, 6, 9, 2, 3, 4)), is(equalTo(45.0)));
        assertThat(MathUtils.sum(ArrayUtils.asArray(1.0, 2, 3)), is(equalTo(6.0)));
        assertThat(MathUtils.sum(new double[0]), is(equalTo(0.0)));
    }

    @Test
    public void testSum_LongArr() {
        assertThat(MathUtils.sum(ArrayUtils.asArray(1L, 2, 3, 4, 5, 6, 7, 8, 9)), is(equalTo(45L)));
        assertThat(MathUtils.sum(ArrayUtils.asArray(7L, 8, 5, 1, 6, 9, 2, 3, 4)), is(equalTo(45L)));
        assertThat(MathUtils.sum(ArrayUtils.asArray(1L, 2, 3)), is(equalTo(6L)));
        assertThat(MathUtils.sum(new long[0]), is(equalTo(0L)));
    }

    @Test
    public void testAddTo_Int() {
        final int[] arr = ArrayUtils.asArray(1, 2, 3, 4, 5, 6);
        final int[] arr2 = ArrayUtils.asArray(4, 5, 6, 2, 3, 4);
        final int[] sum = ArrayUtils.asArray(5, 7, 9, 6, 8, 10);
        assertThat(MathUtils.addTo(arr, arr2), is(equalTo(sum)));
        assertThat(arr, is(equalTo(sum)));
    }

    @Test
    public void testAddTo_Double() {
        final double[] arr = ArrayUtils.asArray(1.0, 2, 3, 4, 5, 6);
        final double[] arr2 = ArrayUtils.asArray(4.0, 5, 6, 2, 3, 4);
        final double[] sum = ArrayUtils.asArray(5.0, 7, 9, 6, 8, 10);
        assertThat(MathUtils.addTo(arr, arr2), is(equalTo(sum)));
        assertThat(arr, is(equalTo(sum)));
    }

    @Test
    public void testSumSquares_Arr() {
        assertThat(MathUtils.sumSquares(ArrayUtils.asArray(1, 2, 3, 4, 5)), is(equalTo(55)));
    }

    @Test
    public void testSumSquares_Arr2D() {
        assertThat(MathUtils.sumSquares(new int[][]{{1, 2}, {3}, {5, 4}}), is(equalTo(55)));
    }

    @Test
    public void testRowMinIndex() {
        assertThat(MathUtils.rowMinIndex(new double[][]{
            {1, 2, 3, 4, 5, 6},
            {4, 5, 1, 5, 3, 2},
            {4, 5, 12, 3, 1, 0},
            {}
        }), is(equalTo(ArrayUtils.asArray(0, 2, 5, -1))));
    }

    @Test
    public void testRowMaxIndex() {
        assertThat(MathUtils.rowMaxIndex(new double[][]{
            {1, 2, 3, 4, 5, 6},
            {4, 5, 1, 5, 3, 2},
            {4, 5, 12, 3, 1, 0},
            {}
        }), is(equalTo(ArrayUtils.asArray(5, 1, 2, -1))));
    }

    @Test
    public void testColMinIndex() {
        assertThat(MathUtils.colMinIndex(new double[][]{
            {1, 2, 3, 4, 5, 6},
            {4, 5, 1, 5, 3, 2},
            {4, 5, 12, 3, 1, 0}
        }), is(equalTo(ArrayUtils.asArray(0, 0, 1, 2, 2, 2))));
    }

    @Test
    public void testColMaxIndex() {
        assertThat(MathUtils.colMaxIndex(new double[][]{
            {1, 2, 3, 4, 5, 6},
            {4, 5, 1, 5, 3, 2},
            {4, 5, 12, 3, 1, 0}
        }), is(equalTo(ArrayUtils.asArray(1, 1, 2, 1, 0, 0))));
    }

    @Test
    public void testRowSum_Int() {
        assertThat(MathUtils.rowSum(new int[][]{
            {1, 2, 3, 4, 5, 6},
            {4, 5, 1, 5, 3, 2},
            {4, 5, 12, 3, 1, 0}
        }), is(equalTo(ArrayUtils.asArray(21, 20, 25))));
    }

    @Test
    public void testColSum_Int() {
        assertThat(MathUtils.colSum(new int[][]{
            {1, 2, 3, 4, 5, 6},
            {4, 5, 1, 5, 3, 2},
            {4, 5, 12, 3, 1, 0}
        }), is(equalTo(ArrayUtils.asArray(9, 12, 16, 12, 9, 8))));
    }

    @Test
    public void testRowSum_Double() {
        assertThat(MathUtils.rowSum(new double[][]{
            {1, 2, 3, 4, 5, 6},
            {4, 5, 1, 5, 3, 2},
            {4, 5, 12, 3, 1, 0}
        }), is(equalTo(ArrayUtils.asArray(21.0, 20, 25))));
    }

    @Test
    public void testColSum_Double() {
        assertThat(MathUtils.colSum(new double[][]{
            {1, 2, 3, 4, 5, 6},
            {4, 5, 1, 5, 3, 2},
            {4, 5, 12, 3, 1, 0}
        }), is(equalTo(ArrayUtils.asArray(9.0, 12, 16, 12, 9, 8))));
    }

    @Test
    public void testAverage() {
        assertThat(MathUtils.average(0.0, 0.0), is(equalTo(0.0)));
        assertThat(MathUtils.average(1.0, 1.0), is(equalTo(1.0)));
        assertThat(MathUtils.average(1.0, 0.0), is(equalTo(0.5)));
        assertThat(MathUtils.average(0.0, 1.0), is(equalTo(0.5)));
        assertThat(MathUtils.average(0.5291677427, 0.1145972106), is(equalTo(0.32188247665)));
        assertThat(MathUtils.average(0.1145972106, 0.5291677427), is(equalTo(0.32188247665)));
    }

    @Test
    public void testAverage_DoubleArr() {
        assertThat(MathUtils.average(1.0, 3.0, 2.0, 1.5, 2.5), is(closeTo(2.0, 1e-10)));
    }

    @Test
    public void testAverage_LongArr() {
        assertThat(MathUtils.average(1L, 3, 2, -1, 5), is(closeTo(2.0, 1e-10)));
    }

    @Test
    public void testAverage_IntArr() {
        assertThat(MathUtils.average(1, 3, 2, -1, 5), is(closeTo(2.0, 1e-10)));
    }
    
    @Test
    public void testWeightedAverage() {
        assertThat(MathUtils.weightedAverage(0.5, 0, 1), is(closeTo(0.5, 1e-10)));
        assertThat(MathUtils.weightedAverage(0.3, 0, 1), is(closeTo(0.7, 1e-10)));
        assertThat(MathUtils.weightedAverage(0.3, 2, 12), is(closeTo(9.0, 1e-10)));
        assertThat(MathUtils.weightedAverage(0.71, 100, 200), is(closeTo(129.0, 1e-10)));
    }

    @Test
    public void testPow() {
        assertThat(MathUtils.pow(0, 1), is(equalTo(0)));
        assertThat(MathUtils.pow(1, 3), is(equalTo(1)));
        assertThat(MathUtils.pow(5, 0), is(equalTo(1)));
        assertThat(MathUtils.pow(7, 1), is(equalTo(7)));
        assertThat(MathUtils.pow(2, 2), is(equalTo(4)));
        assertThat(MathUtils.pow(2, 3), is(equalTo(8)));
        assertThat(MathUtils.pow(2, 4), is(equalTo(16)));
        assertThat(MathUtils.pow(2, 5), is(equalTo(32)));
        assertThat(MathUtils.pow(7, 2), is(equalTo(49)));
        assertThat(MathUtils.pow(5, 3), is(equalTo(125)));
        assertThat(MathUtils.pow(-5, 3), is(equalTo(-125)));
        assertThat(MathUtils.pow(-2, 11), is(equalTo(-2048)));
        assertThat(MathUtils.pow(-2, 12), is(equalTo(4096)));
    }

    @Test
    public void testLastZero() {
        assertThat(MathUtils.lastZero(ArrayUtils.asArray(0, 0, 0, 2, 3)), is(equalTo(2)));
        assertThat(MathUtils.lastZero(ArrayUtils.asArray(0, 0, 0, 0, 0)), is(equalTo(4)));
        assertThat(MathUtils.lastZero(ArrayUtils.asArray(1, 2, 3, 4, 5)), is(equalTo(-1)));
    }

    @Test
    public void testAverageDim3_Long() {
        assertThat(MathUtils.averageDim3(new long[][][]{
            {{1L, 2}, {3L, 4}},
            {{5, 6}, {7, 8}}
        }), is(equalTo(new double[][]{
            {1.5, 3.5},
            {5.5, 7.5}
        })));
    }

    @Test
    public void testAverageRows_DoubleD2() {
        assertThat(MathUtils.averageRows(new double[][]{
            {1, 2},
            {3, 4},}), is(equalTo(ArrayUtils.asArray(1.5, 3.5))));
    }

    @Test
    public void testAverageCols_DoubleD2() {
        assertThat(MathUtils.averageCols(new double[][]{
            {1, 2},
            {3, 4},}), is(equalTo(ArrayUtils.asArray(2.0, 3.0))));
    }

    @Test
    public void testDivide_Double() {
        final double[] arr = ArrayUtils.asArray(1.0, 2, 3, 4, 5, 6);
        final double val = 1 / 3.0;
        final double[] divided = ArrayUtils.asArray(3.0, 6.0, 9.0, 12.0, 15.0, 18.0);
        assertThat(MathUtils.divide(arr, val), is(equalTo(divided)));
        assertThat(arr, is(equalTo(divided)));
    }

    @Test
    public void testAdd_Double3D() {
        assertThat(MathUtils.add(new double[][][]{
            {{1, 2}, {3, 4}},
            {{5, 6}, {7, 8}}
        }, new double[][][]{
            {{1, 2}, {3, 4}},
            {{5, 6}, {7, 8}}
        }), is(equalTo(new double[][][]{
            {{2, 4}, {6, 8}},
            {{10, 12}, {14, 16}}
        })));
    }

    @Test
    public void testAdd_Long3D() {
        assertThat(MathUtils.add(new long[][][]{
            {{1, 2}, {3, 4}},
            {{5, 6}, {7, 8}}
        }, new long[][][]{
            {{1, 2}, {3, 4}},
            {{5, 6}, {7, 8}}
        }), is(equalTo(new long[][][]{
            {{2, 4}, {6, 8}},
            {{10, 12}, {14, 16}}
        })));
    }

    @Test
    public void testCountGeqThanInt() {
        assertThat(MathUtils.countGeqThan(ArrayUtils.asArray(1, 0, 3, 4, 52, 4, 3, 2, 1, 2, 3), 3), is(equalTo(6)));
    }
    
    @Test
    public void testLowerTriangleVector() {
        assertThat(
                MathUtils.lowerTriangleVector(new double[][] {
                    {1.5, 1.6, 1.7, 1.8},
                    {1.1, 2.3, 3.4, 5.6},
                    {2.2, 3.3, 4.5, 7.8},
                    {4.4, 5.5, 6.6, 7.7}
                }),
                is(equalTo(ArrayUtils.asArray(1.1, 2.2, 3.3, 4.4, 5.5, 6.6)))
        );
    }
    
    @Test
    public void testRoundUp() {
        assertThat(MathUtils.roundUp(0.0), is(equalTo(0)));
        assertThat(MathUtils.roundUp(0.1), is(equalTo(1)));
        assertThat(MathUtils.roundUp(0.99), is(equalTo(1)));
        assertThat(MathUtils.roundUp(1.0), is(equalTo(1)));
    }
    
    @Test
    public void testLogBase() {
        assertThat(MathUtils.logBase(5, 4), is(closeTo(1.16096404744368117394, 1e-15)));
        assertThat(MathUtils.logBase(10, 10), is(closeTo(1, 1e-15)));
    }

}
