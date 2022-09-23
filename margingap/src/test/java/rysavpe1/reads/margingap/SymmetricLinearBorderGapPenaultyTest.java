package rysavpe1.reads.margingap;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

/**
 *
 * @author Petr Ryšavý
 */
public class SymmetricLinearBorderGapPenaultyTest {
    private SymmetricLinearBorderGapPenaulty penaulty;

    @Test
    public void testGetBorderGapPenaulty() {
        penaulty = new SymmetricLinearBorderGapPenaulty(2, 7, 4);
        assertThat(penaulty.getBorderGapPenaulty(0, 7), is(closeTo(0.0, 1e-10)));
        assertThat(penaulty.getBorderGapPenaulty(1, 7), is(closeTo(0.0, 1e-10)));
        assertThat(penaulty.getBorderGapPenaulty(2, 7), is(closeTo(1.0, 1e-10)));
        assertThat(penaulty.getBorderGapPenaulty(3, 7), is(closeTo(2.0, 1e-10)));
        assertThat(penaulty.getBorderGapPenaulty(4, 7), is(closeTo(3.0, 1e-10)));
        assertThat(penaulty.getBorderGapPenaulty(5, 7), is(closeTo(4.0, 1e-10)));
        assertThat(penaulty.getBorderGapPenaulty(6, 7), is(closeTo(4.0, 1e-10)));
    }

    @Test
    public void testGetBorderGapPenaulty1() {
        penaulty = new SymmetricLinearBorderGapPenaulty(1.5 / 7.0, 4);
        assertThat(penaulty.getBorderGapPenaulty(0, 7), is(closeTo(0.0, 1e-10)));
        assertThat(penaulty.getBorderGapPenaulty(1, 7), is(closeTo(0.0, 1e-10)));
        assertThat(penaulty.getBorderGapPenaulty(2, 7), is(closeTo(1.0, 1e-10)));
        assertThat(penaulty.getBorderGapPenaulty(3, 7), is(closeTo(2.0, 1e-10)));
        assertThat(penaulty.getBorderGapPenaulty(4, 7), is(closeTo(3.0, 1e-10)));
        assertThat(penaulty.getBorderGapPenaulty(5, 7), is(closeTo(4.0, 1e-10)));
        assertThat(penaulty.getBorderGapPenaulty(6, 7), is(closeTo(4.0, 1e-10)));
    }

    @Test
    public void testGetBorderGapPenaulty2() {
        penaulty = new SymmetricLinearBorderGapPenaulty(1, 3, 2);
        assertThat(penaulty.getBorderGapPenaulty(0, 3), is(closeTo(0.0, 1e-10)));
        assertThat(penaulty.getBorderGapPenaulty(1, 3), is(closeTo(1.0, 1e-10)));
        assertThat(penaulty.getBorderGapPenaulty(2, 3), is(closeTo(2.0, 1e-10)));
    }

    @Test
    public void testGetBorderGapPenaulty3() {
        penaulty = new SymmetricLinearBorderGapPenaulty(1.0 / 3.0, 2);
        assertThat(penaulty.getBorderGapPenaulty(0, 3), is(closeTo(0.0, 1e-10)));
        assertThat(penaulty.getBorderGapPenaulty(1, 3), is(closeTo(1.0, 1e-10)));
        assertThat(penaulty.getBorderGapPenaulty(2, 3), is(closeTo(2.0, 1e-10)));
    }

    @Test
    public void testGetMarginGapPenalty2() {
        penaulty = new SymmetricLinearBorderGapPenaulty(4.5, 30, 2);
        for (int g = 0; g < 4; g++)
            assertThat(penaulty.getBorderGapPenaulty(g, 30), is(equalTo(0.0)));
        assertThat(penaulty.getBorderGapPenaulty(4, 30), is(closeTo(1.0 / 22.0, 1e-10)));
        assertThat(penaulty.getBorderGapPenaulty(13, 30), is(closeTo(19.0 / 22.0, 1e-10)));
        for (int g = 26; g < 30; g++)
            assertThat(penaulty.getBorderGapPenaulty(g, 30), is(equalTo(2.0)));
    }

}
