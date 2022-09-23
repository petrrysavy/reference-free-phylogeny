package rysavpe1.reads.distance.simple;

import org.junit.Before;
import org.junit.Test;
import rysavpe1.reads.multiset.HashMultiset;
import rysavpe1.reads.multiset.Multiset;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;

/**
 *
 * @author Petr Ryšavý
 */
public class CosineSimmilarityDistanceTest
{

    private Multiset<Integer> a;
    private Multiset<Integer> b;

    @Before
    public void init()
    {
        a = new HashMultiset<>();
        b = new HashMultiset<>();

        a.add(5);
        a.add(6, 2);
        a.add(7, 3);
        a.add(8);
        a.add(9);
        b.add(1);
        b.add(2);
        b.add(3, 3);
        b.add(4, 2);
        b.add(6);
        b.add(7, 3);
    }

    @Test
    public void testGetDistance()
    {
        double distance = new CosineSimmilarityDistance().getDistance(a, b);
        assertThat(distance, is(closeTo(9.0 / 4.0 / 5.0, 1e-12)));
    }

    @Test
    public void testGetSimilarity() {
        double similarity = new CosineSimmilarityDistance().getSimilarity(a, b);
        assertThat(similarity, is(closeTo(11.0 / 4.0 / 5.0, 1e-12)));
    }

}
