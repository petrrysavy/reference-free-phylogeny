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
public class LinearMarginGapPenaltyTest {
    private LinearMarginGapPenalty penaulty;

    @Test
    public void testGetMarginGapPenaulty() {
        penaulty = new LinearMarginGapPenalty(7.0/5.0, 7, 4);
        final double[] pen = penaulty.build(7);
        assertThat(pen.length, is(7));
        assertThat(pen[0], is(closeTo(0.0, 1e-10)));
        assertThat(pen[1], is(closeTo(0.0, 1e-10)));
        assertThat(pen[2], is(closeTo(1.0, 1e-10)));
        assertThat(pen[3], is(closeTo(2.0, 1e-10)));
        assertThat(pen[4], is(closeTo(3.0, 1e-10)));
        assertThat(pen[5], is(closeTo(4.0, 1e-10)));
        assertThat(pen[6], is(closeTo(4.0, 1e-10)));
    }

    @Test
    public void testGetMarginGapPenaulty2() {
        penaulty = new LinearMarginGapPenalty(1.0, 3, 2);
        final double[] pen = penaulty.build(3);
        assertThat(pen.length, is(3));
        assertThat(pen[0], is(closeTo(0.0, 1e-10)));
        assertThat(pen[1], is(closeTo(1.0, 1e-10)));
        assertThat(pen[2], is(closeTo(2.0, 1e-10)));
    }

    @Test
    public void testGetMarginGapPenalty2() {
        penaulty = new LinearMarginGapPenalty(3.0, 30, 2);
        final double[] pen = penaulty.build(30);
        assertThat(pen.length, is(30));
        for (int g = 0; g < 4; g++)
            assertThat(pen[g], is(equalTo(0.0)));
        assertThat(pen[4], is(closeTo(1.0 / 22.0, 1e-10)));
        assertThat(pen[13], is(closeTo(19.0 / 22.0, 1e-10)));
        for (int g = 26; g < 30; g++)
            assertThat(pen[g], is(equalTo(2.0)));
    }
    
    @Test
    public void extremeRLandCoverageFail() {
        // this one was failing on hyperion - readLength 3, and coverage 0.1
        penaulty = new LinearMarginGapPenalty(0.1, 3, 2);
        final double[] pen = penaulty.build(3);
    }

}
