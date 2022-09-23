package rysavpe1.reads.distance.simple;

import org.junit.Before;
import org.junit.Test;
import rysavpe1.reads.model.Sequence;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 *
 * @author Petr Ryšavý
 */
public class EditDistanceTest {
    private EditDistance ed;

    @Before
    public void bootstrap() {
        ed = new EditDistance(0, 1, 1);
    }

    @Test
    public void testDist1() {
        assertThat(ed.getDistance(new Sequence("kitten".toCharArray(), ""), new Sequence("sitting".toCharArray(), "")), is(equalTo(3)));
    }

    @Test
    public void testDist2() {
        assertThat(ed.getDistance(new Sequence("Hello".toCharArray(), ""), new Sequence("Jello".toCharArray(), "")), is(equalTo(1)));
    }

    @Test
    public void testDist3() {
        assertThat(ed.getDistance(new Sequence("good".toCharArray(), ""), new Sequence("goodbye".toCharArray(), "")), is(equalTo(3)));
    }

    @Test
    public void testDist4() {
        assertThat(ed.getDistance(new Sequence("goodbye".toCharArray(), ""), new Sequence("goodbye".toCharArray(), "")), is(equalTo(0)));
    }

    @Test
    public void testDist5() {
        assertThat(ed.getDistance(Sequence.fromString("ATCGCTGCAA"), Sequence.fromString("CTCCTCCA")), is(equalTo(4)));
    }

    @Test
    public void testDist6() {
        assertThat(ed.getDistance(Sequence.fromString("TCG"), Sequence.fromString("TCC")), is(equalTo(1)));
    }

    @Test
    public void testDist7() {
        assertThat(ed.getDistance(Sequence.fromString("GCA"), Sequence.fromString("CCA")), is(equalTo(1)));
    }

    @Test
    public void testDist8() {
        assertThat(ed.getDistance(Sequence.fromString("TCG"), Sequence.fromString("CCA")), is(equalTo(2)));
    }

    @Test
    public void testDist9() {
        assertThat(ed.getDistance(Sequence.fromString("GCA"), Sequence.fromString("TCC")), is(equalTo(2)));
    }


    @Test
    public void testSymmetric() {
        assertThat(ed.isSymmetric(), is(true));
    }

    @Test
    public void testNormalizedFalse() {
        assertThat(ed.isZeroOneNormalized(), is(false));
    }
}
