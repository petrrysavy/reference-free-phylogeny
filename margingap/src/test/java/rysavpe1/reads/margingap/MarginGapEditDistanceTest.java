package rysavpe1.reads.margingap;

import org.junit.Before;
import org.junit.Test;
import rysavpe1.reads.model.Sequence;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

/**
 *
 * @author Petr Ryšavý
 */
public class MarginGapEditDistanceTest {
    private MarginGapEditDistance editDistance;

    @Before
    public void init() {
        editDistance = new MarginGapEditDistance(0, 1, 1, new LinearMarginGapPenalty(1.0, 3, 2));
    }

    @Test
    public void testGetDistance1() {
        assertThat(editDistance.getDistance(Sequence.fromString("ABC"), Sequence.fromString("CDA")), is(equalTo(2.0)));
    }

    @Test
    public void testGetDistance2() {
        assertThat(editDistance.getDistance(Sequence.fromString("ABC"), Sequence.fromString("BCD")), is(equalTo(0.0)));
    }

    @Test
    public void testGetDistance3() {
        assertThat(editDistance.getDistance(Sequence.fromString("BCD"), Sequence.fromString("CDA")), is(equalTo(0.0)));
    }

    @Test
    public void testGetDistance4() {
        assertThat(editDistance.getDistance(Sequence.fromString("ABC"), Sequence.fromString("ABC")), is(equalTo(0.0)));
    }

    @Test
    public void testGetDistance5() {
        editDistance = new MarginGapEditDistance(0, 1, 1, new LinearMarginGapPenalty(6.0/5.0, 6, 2));
        assertThat(editDistance.getDistance(Sequence.fromString("ABCDEF"), Sequence.fromString("DEFGHI")), is(closeTo(4.0 / 3.0, 1e-10)));
    }

    @Test
    public void testGetDistance6() {
        editDistance = new MarginGapEditDistance(0, 1, 1, new LinearMarginGapPenalty(6.0/5.0, 6, 2));
        assertThat(editDistance.getDistance(Sequence.fromString("ABCDEF"), Sequence.fromString("EFGHIJ")), is(closeTo(4.0, 1e-10)));
    }

    @Test
    public void testFigure2() {
        editDistance = new MarginGapEditDistance(0, 1, 1, new LinearMarginGapPenalty(4.0/3.0, 4, 2));
        assertThat(editDistance.getDistance(Sequence.fromString("TCGC"), Sequence.fromString("CGCT")), is(equalTo(0.0)));
    }

    @Test
    public void testSymmetric() {
        assertThat(editDistance.isSymmetric(), is(true));
    }

    @Test
    public void testNormalizedFalse() {
        assertThat(editDistance.isZeroOneNormalized(), is(false));
    }
}
