package rysavpe1.reads.distance.simple;

import java.util.Random;
import org.junit.Before;
import org.junit.Test;
import rysavpe1.reads.model.Sequence;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import rysavpe1.reads.model.RandomGenerators;
import rysavpe1.reads.utils.TicTac;

/**
 *
 * @author Petr Ryšavý
 */
public class UkkonenDistanceTest {
    private UkkonenDistance ed;

    @Before
    public void bootstrap() {
        ed = new UkkonenDistance();
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
    public void testAgainstED() {
        Random rand = new Random(42);
        final Sequence seqA = RandomGenerators.generateRandomSequence(10000, rand);
        final Sequence seqB = RandomGenerators.generateRandomSequence(12000, rand);
        final TicTac ticTac = new TicTac();
        final int ukkDist = ed.getDistance(seqA, seqB);
        System.out.println("Ukkonen: "+ticTac.printTime());
        ticTac.tic();
        final int edDist = new EditDistance().getDistance(seqA, seqB);
        System.out.println("Edit distance: "+ticTac.printTime());
        assertThat(ukkDist, is(equalTo(edDist)));
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
