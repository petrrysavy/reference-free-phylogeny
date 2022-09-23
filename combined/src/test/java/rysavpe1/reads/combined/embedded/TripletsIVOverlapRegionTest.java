package rysavpe1.reads.combined.embedded;

import java.util.Arrays;
import java.util.Collection;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import rysavpe1.reads.overlap.OverlapRegion;

/**
 * Test representing matching of two sequences. Imagine ACTGCTGCAACT and ACTG.
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
@RunWith(Parameterized.class)
public class TripletsIVOverlapRegionTest {

    private static final int LEN_A_IV = 10;
    private static final int LEN_B_IV = 2;
    
    // i, startA, endA, startB, endB
    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {0, 0, 0, 4, 4},
            {1, 0, 3, 1, 4},
            {2, 0, 4, 0, 4},
            {3, 1, 5, 0, 4},
            {4, 2, 6, 0, 4},
            {5, 3, 7, 0, 4},
            {6, 4, 8, 0, 4},
            {7, 5, 9, 0, 4},
            {8, 6, 10, 0, 4},
            {9, 7, 11, 0, 4},
            {10, 8, 12, 0, 4},
            {11, 9, 12, 0, 3},
            {12, 12, 12, 0, 0},
        });
    }

    private final int i;
    private final int startA;
    private final int endA;
    private final int startB;
    private final int endB;

    public TripletsIVOverlapRegionTest(int i, int startA, int endA, int startB, int endB) {
        this.i = i;
        this.startA = startA;
        this.endA = endA;
        this.startB = startB;
        this.endB = endB;
    }
    
    @Test
    public void testValues() {
        final TripletsIVOverlapRegion region = new TripletsIVOverlapRegion(LEN_A_IV, LEN_B_IV, i, 0);
        assertThat(region.getStartA(), is(equalTo(getIVStart(startA, LEN_A_IV))));
        assertThat(region.getEndA(), is(equalTo(getIVEnd(endA))));
        assertThat(region.getStartB(), is(equalTo(getIVStart(startB, LEN_B_IV))));
        assertThat(region.getEndB(), is(equalTo(getIVEnd(endB))));
        final OverlapRegion sequenceRegion = region.asSequenceOverlapRegion();
        assertThat(sequenceRegion.getStartA(), is(equalTo(startA)));
        assertThat(sequenceRegion.getEndA(), is(equalTo(endA)));
        assertThat(sequenceRegion.getLengthA(), is(equalTo(endA - startA)));
        assertThat(sequenceRegion.getStartB(), is(equalTo(startB)));
        assertThat(sequenceRegion.getEndB(), is(equalTo(endB)));
        assertThat(sequenceRegion.getLengthB(), is(equalTo(endB - startB)));
    }
    
    private int getIVStart(int start, int len) {
        if(start == (len + 2)) return len;
        return start;
    }
    
    private int getIVEnd(int end) {
        if(end == 0) return 0;
        return end - 2;
    }
    
}
