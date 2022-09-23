package rysavpe1.reads.distance.simple;

import org.junit.Before;
import org.junit.Test;
import rysavpe1.reads.model.ReadBag;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static rysavpe1.reads.model.ReadBag.fromString;

/**
 *
 * @author Petr Ryšavý
 */
public class MaxSizeSimilarityTest {

    private MaxSizeSimilarity distance;

    @Before
    public void init() {
        distance = new MaxSizeSimilarity();
    }

    @Test
    public void testGetDistance() {
        ReadBag bag1 = fromString("A", "T", "C", "G", "T");
        ReadBag bag2 = fromString("A", "T", "C", "G", "T", "G", "C");
        assertThat(distance.getDistance(bag1, bag2), is(equalTo(7.0)));
        assertThat(distance.getDistance(bag2, bag1), is(equalTo(7.0)));
    }

    @Test
    public void testSymmetric() {
        assertThat(distance.isSymmetric(), is(true));
    }

    @Test
    public void testNormalizedFalse() {
        assertThat(distance.isZeroOneNormalized(), is(false));
    }

}
