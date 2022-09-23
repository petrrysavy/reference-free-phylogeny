package rysavpe1.reads.distance.simple;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.number.IsCloseTo.closeTo;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import rysavpe1.reads.utils.TwiceIndexedMap;
import rysavpe1.reads.multiset.HashMultiset;
import rysavpe1.reads.multiset.Multiset;

/**
 *
 * @author Petr Ryšavý
 */
public class AveragingDistanceTest {

    private AveragingDistance<String> distance;

    @Before
    public void bootstrap() {
        final TwiceIndexedMap<String, Double> map = new TwiceIndexedMap<>();
        map.put("1", "A", 1.0);
        map.put("1", "B", 2.0);
        map.put("2", "A", 5.0);
        map.put("2", "B", 7.1);
        map.put("3", "A", 0.3);
        map.put("3", "B", 0.1);

        distance = new AveragingDistance<>((String a, String b) -> {
            return map.get(a, b);
        });
    }

    @Test
    public void testGetDistance() {
        final Multiset<String> aSet = new HashMultiset<>("A", "B", "B", "B");
        final Multiset<String> bSet = new HashMultiset<>("1", "1", "2", "3");
        assertThat(distance.getDistance(aSet, bSet), is(closeTo(2.55625, 1e-10)));
        assertThat(distance.getDistance(bSet, aSet), is(closeTo(2.55625, 1e-10)));
    }

    @Test
    public void testIsSymmetric() {
        assertThat(distance.isSymmetric(), is(true));
    }

}
