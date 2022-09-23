package rysavpe1.reads.utils;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.arrayContaining;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

/**
 *
 * @author Petr Ryšavý
 */
public class StatsUtilsTest {

    @Test
    public void testGetPearsonsCorrelationCoefficient() {
        assertThat(StatsUtils.getPearsonsCorrelationCoefficient(
                ArrayUtils.asArray(1.0, 2, 3, 4, 5),
                ArrayUtils.asArray(1.0, 2, 3, 4, 5)),
                is(closeTo(1.0, 1e-10))
        );
    }

    @Test
    public void testGetPearsonsCorrelationCoefficient2() {
        assertThat(StatsUtils.getPearsonsCorrelationCoefficient(
                ArrayUtils.asArray(1.0, 2, 3, 4, 1),
                ArrayUtils.asArray(1.0, 2, 3, 4, 5)),
                is(closeTo(1.0 / Math.sqrt(17), 1e-10))
        );
    }

    @Test
    public void testGetPearsonsCorrelationCoefficient3() {
        assertThat(StatsUtils.getPearsonsCorrelationCoefficient(
                ArrayUtils.asArray(2.0, 4, 6, 8, 10),
                ArrayUtils.asArray(1.0, 2, 3, 4, 5)),
                is(closeTo(1.0, 1e-10))
        );
    }

    @Test
    public void testGetPearsonsCorrelationCoefficient4() {
        assertThat(StatsUtils.getPearsonsCorrelationCoefficient(
                ArrayUtils.asArray(-1.0, -2, -3, -4, -5),
                ArrayUtils.asArray(1.0, 2, 3, 4, 5)),
                is(closeTo(-1.0, 1e-10))
        );
    }

    @Test
    public void testGetPearsonsCorrelationCoefficient5() {
        assertThat(StatsUtils.getPearsonsCorrelationCoefficient(
                ArrayUtils.asArray(4.0, 56, 47, 14, 1),
                ArrayUtils.asArray(1.0, 2, 3, 4, 5)),
                is(closeTo(-8.0 / Math.sqrt(717), 1e-10))
        );
    }

    @Test
    public void testGetPearsonsCorrelationCoefficient6() {
        assertThat(StatsUtils.getPearsonsCorrelationCoefficient(
                ArrayUtils.asArray(4864.0, 4564, 456, 14, 45),
                ArrayUtils.asArray(1.0, 2, 3, 4, 5)),
                is(closeTo(-3547.0 / 2.0 / Math.sqrt(3894703), 1e-10))
        );
    }

    @Test
    public void testGetPearsonsCorrelationCoefficient7() {
        assertThat(StatsUtils.getPearsonsCorrelationCoefficient(
                ArrayUtils.asArray(1.0, 1, 2, 1, 1),
                ArrayUtils.asArray(1.0, 2, 3, 4, 5)),
                is(closeTo(0.0, 1e-10))
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPearsonsCorrelationCoefficient8() {
        assertThat(StatsUtils.getPearsonsCorrelationCoefficient(
                ArrayUtils.asArray(1.0, 2, 3, 4, 5, 6),
                ArrayUtils.asArray(1.0, 2, 3, 4, 5)),
                is(closeTo(0.0, 1e-10))
        );
    }

    @Test
    public void testGetPearsonsCorrelationCoefficientInt() {
        assertThat(StatsUtils.getPearsonsCorrelationCoefficient(
                ArrayUtils.asArray(1, 2, 3, 4, 5),
                ArrayUtils.asArray(1, 2, 3, 4, 5)),
                is(closeTo(1.0, 1e-10))
        );
    }

    @Test
    public void testGetPearsonsCorrelationCoefficientInt2() {
        assertThat(StatsUtils.getPearsonsCorrelationCoefficient(
                ArrayUtils.asArray(1, 2, 3, 4, 1),
                ArrayUtils.asArray(1, 2, 3, 4, 5)),
                is(closeTo(1.0 / Math.sqrt(17), 1e-10))
        );
    }

    @Test
    public void testGetPearsonsCorrelationCoefficientInt3() {
        assertThat(StatsUtils.getPearsonsCorrelationCoefficient(
                ArrayUtils.asArray(2, 4, 6, 8, 10),
                ArrayUtils.asArray(1, 2, 3, 4, 5)),
                is(closeTo(1.0, 1e-10))
        );
    }

    @Test
    public void testGetPearsonsCorrelationCoefficientInt4() {
        assertThat(StatsUtils.getPearsonsCorrelationCoefficient(
                ArrayUtils.asArray(-1, -2, -3, -4, -5),
                ArrayUtils.asArray(1, 2, 3, 4, 5)),
                is(closeTo(-1.0, 1e-10))
        );
    }

    @Test
    public void testGetPearsonsCorrelationCoefficientInt5() {
        assertThat(StatsUtils.getPearsonsCorrelationCoefficient(
                ArrayUtils.asArray(4, 56, 47, 14, 1),
                ArrayUtils.asArray(1, 2, 3, 4, 5)),
                is(closeTo(-8.0 / Math.sqrt(717), 1e-10))
        );
    }

    @Test
    public void testGetPearsonsCorrelationCoefficientInt6() {
        assertThat(StatsUtils.getPearsonsCorrelationCoefficient(
                ArrayUtils.asArray(4864, 4564, 456, 14, 45),
                ArrayUtils.asArray(1, 2, 3, 4, 5)),
                is(closeTo(-3547.0 / 2.0 / Math.sqrt(3894703), 1e-10))
        );
    }

    @Test
    public void testGetPearsonsCorrelationCoefficientInt7() {
        assertThat(StatsUtils.getPearsonsCorrelationCoefficient(
                ArrayUtils.asArray(1, 1, 2, 1, 1),
                ArrayUtils.asArray(1, 2, 3, 4, 5)),
                is(closeTo(0.0, 1e-10))
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetPearsonsCorrelationCoefficientInt8() {
        assertThat(StatsUtils.getPearsonsCorrelationCoefficient(
                ArrayUtils.asArray(1, 2, 3, 4, 5, 6),
                ArrayUtils.asArray(1, 2, 3, 4, 5)),
                is(closeTo(0.0, 1e-10))
        );
    }

    @Test
    public void testMean() {
        assertThat(StatsUtils.mean(ArrayUtils.asArray(10.0, 2, 38, 23, 38, 23, 21)),
                is(closeTo(22.142857142857, 1e-10)));
    }

    @Test
    public void testStdev() {
        assertThat(StatsUtils.stdev(ArrayUtils.asArray(10.0, 2, 38, 23, 38, 23, 21)),
                is(closeTo(12.298996142875, 1e-10)));
    }

    @Test
    public void testToRankArray() {
        assertThat(ArrayUtils.asBoxedArray(StatsUtils.toRankArray(ArrayUtils.asArray(106.0, 86, 100, 101, 99, 103, 97, 113, 112, 110))),
                is(arrayContaining(6, 0, 3, 4, 2, 5, 1, 9, 8, 7)));
    }

    @Test
    public void testToRankArray2() {
        assertThat(ArrayUtils.asBoxedArray(StatsUtils.toRankArray(ArrayUtils.asArray(0.0, 20, 28, 27, 50, 29, 7, 17, 6, 12))),
                is(arrayContaining(0, 5, 7, 6, 9, 8, 2, 4, 1, 3)));
    }

    @Test
    public void testSpearmansCorrelationCoefficient() {
        // example from wikipedia
        assertThat(
                StatsUtils.getSpearmansCorrelationCoefficient(ArrayUtils.asArray(106.0, 86, 100, 101, 99, 103, 97, 113, 112, 110),
                        ArrayUtils.asArray(7.0, 0, 27, 50, 28, 29, 20, 12, 6, 17)),
                is(closeTo(-29.0 / 165, 1e-10))
        );
    }

    @Test
    public void testSpearmansCorrelationCoefficientInt() {
        // example from wikipedia
        assertThat(
                StatsUtils.getSpearmansCorrelationCoefficient(ArrayUtils.asArray(106, 86, 100, 101, 99, 103, 97, 113, 112, 110),
                        ArrayUtils.asArray(7, 0, 27, 50, 28, 29, 20, 12, 6, 17)),
                is(closeTo(-29.0 / 165, 1e-10))
        );
    }
    
    @Test
    public void testKLDivergence() {
        // /* K-L divergence is defined for positive discrete densities */
//        /* Face: 1   2   3   4   5   6 */
//        f    = {20, 14, 19, 19, 12, 16} / 100;  /* empirical density; 100 rolls of die */
//        g    = { 1,  1,  1,  1,  1,  1} / 6;    /* uniform density */
//        KL_fg = -sum( f#log(g/f) );             /* K-L divergence using natural log */
//        print KL_fg;
//https://blogs.sas.com/content/iml/2020/05/26/kullback-leibler-divergence-discrete.html
        assertThat(
                StatsUtils.KLDivergence(
                        new double[] {0.2, 0.14, 0.19, 0.19, 0.12, 0.16},
                        new double[] {1.0/6, 1.0/6, 1.0/6, 1.0/6, 1.0/6, 1.0/6}
                ),
                is(closeTo(0.0158936, 1e-6))
        );
    }

}
