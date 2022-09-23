package rysavpe1.reads.distance.simple;

import org.junit.Test;
import rysavpe1.reads.model.Sequence;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 *
 * @author Petr Ryšavý
 */
public class EditDistanceQuadraticMemoryTest
{
    @Test
    public void testDist1()
    {
        EditDistanceQuadraticMemory ed = new EditDistanceQuadraticMemory(0, 1, 1);
        assertThat(3, is(equalTo(ed.getDistance(new Sequence("kitten".toCharArray(), ""), new Sequence("sitting".toCharArray(), "")))));
    }
    @Test
    public void testDist2()
    {
        EditDistanceQuadraticMemory ed = new EditDistanceQuadraticMemory(0, 1, 1);
        assertThat(1, is(equalTo(ed.getDistance(new Sequence("Hello".toCharArray(), ""), new Sequence("Jello".toCharArray(), "")))));
    }

    @Test
    public void testDist3()
    {
        EditDistanceQuadraticMemory ed = new EditDistanceQuadraticMemory(0, 1, 1);
        assertThat(3, is(equalTo(ed.getDistance(new Sequence("good".toCharArray(), ""), new Sequence("goodbye".toCharArray(), "")))));
    }

    @Test
    public void testDist4()
    {
        EditDistanceQuadraticMemory ed = new EditDistanceQuadraticMemory(0, 1, 1);
        assertThat(0, is(equalTo(ed.getDistance(new Sequence("goodbye".toCharArray(), ""), new Sequence("goodbye".toCharArray(), "")))));
    }
}
