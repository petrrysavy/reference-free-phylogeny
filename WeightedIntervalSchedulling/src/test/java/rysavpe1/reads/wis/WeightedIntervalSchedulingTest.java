package rysavpe1.reads.wis;

import java.util.Arrays;
import java.util.Collection;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import rysavpe1.reads.utils.ArrayUtils;

/**
 *
 * @author petr
 */
@RunWith(Parameterized.class)
public class WeightedIntervalSchedulingTest {

    private static final SimpleInterval A = new SimpleInterval(-1, 11, 5);
    private static final SimpleInterval B = new SimpleInterval(3, 5, 7);
    private static final SimpleInterval C = new SimpleInterval(0, 2, 2);
    private static final SimpleInterval D = new SimpleInterval(6, 10, 3);
    private static final SimpleInterval E = new SimpleInterval(1, 7, 9);
    private static final SimpleInterval F = new SimpleInterval(0, 1, 2);
    private static final SimpleInterval G = new SimpleInterval(8, 9, 1);
    private static final SimpleInterval H = new SimpleInterval(6, 7, 5);

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {ArrayUtils.asArray2(A), 5.0, ArrayUtils.asArray2(A)},
            {ArrayUtils.asArray2(A, B), 7.0, ArrayUtils.asArray2(B)},
            {ArrayUtils.asArray2(A, F, G), 5.0, ArrayUtils.asArray2(A)},
            {ArrayUtils.asArray2(A, B, F, G, H), 15.0, ArrayUtils.asArray2(F, B, G, H)},
            {ArrayUtils.asArray2(A, B, G, H), 13.0, ArrayUtils.asArray2(B, G, H)},
            {ArrayUtils.asArray2(A, B, D, E, F, G, H), 15.0, ArrayUtils.asArray2(B, F, G, H)},
            {ArrayUtils.asArray2(A, B, C, D, E, G, H), 15.0, ArrayUtils.asArray2(B, C, G, H)},
            {ArrayUtils.asArray2(E, F, H), 11.0, ArrayUtils.asArray2(E, F)},
            {ArrayUtils.asArray2(E, F, G, H), 12.0, ArrayUtils.asArray2(E, F, G)},
            {ArrayUtils.asArray2(B, D, E), 10.0, ArrayUtils.asArray2(B, D)},});
    }

    private final SimpleInterval[] arr;
    private final SimpleInterval[] result;
    private final double optimum;

    public WeightedIntervalSchedulingTest(SimpleInterval[] arr, double optimum, SimpleInterval[] result) {
        this.arr = arr;
        this.optimum = optimum;
        this.result = result;
    }

    @Test
    public void testGetOptimalValueSorted() {
        assertThat(WeightedIntervalScheduling.getOptimalValueSorted(arr), is(equalTo(optimum)));
    }

    @Test
    public void testSolveSorted() {
        assertThat(WeightedIntervalScheduling.solve(arr), containsInAnyOrder(result));
    }
}
