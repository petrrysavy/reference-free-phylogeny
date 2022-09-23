package rysavpe1.reads.distance.simple;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;
import rysavpe1.reads.distance.AbstractMeasure;

/**
 *
 * @author Petr Ryšavý
 */
public class ProductSimilarityTest {

    @Test
    public void testGetDistance() {
        assertThat(new ProductSimilarity<>(new ConstDistance(0.0), new ConstDistance(0.0)).getDistance(0, 0), is(equalTo(0.0)));
        assertThat(new ProductSimilarity<>(new ConstDistance(1.0), new ConstDistance(0.0)).getDistance(0, 0), is(equalTo(0.0)));
        assertThat(new ProductSimilarity<>(new ConstDistance(0.0), new ConstDistance(1.0)).getDistance(0, 0), is(equalTo(0.0)));
        assertThat(new ProductSimilarity<>(new ConstDistance(0.5), new ConstDistance(0.2)).getDistance(0, 0), is(closeTo(0.1, 1e-10)));
        assertThat(new ProductSimilarity<>(new ConstDistance(1.0), new ConstDistance(0.3)).getDistance(0, 0), is(closeTo(0.3, 1e-10)));
        assertThat(new ProductSimilarity<>(new ConstDistance(0.1), new ConstDistance(0.1)).getDistance(0, 0), is(closeTo(0.01, 1e-10)));
        assertThat(new ProductSimilarity<>(new ConstDistance(0.3), new ConstDistance(0.2)).getDistance(0, 0), is(closeTo(0.06, 1e-10)));
        assertThat(new ProductSimilarity<>(new ConstDistance(0.5520387506), new ConstDistance(0.7687072138)).getDistance(0, 0), is(closeTo(0.42435616988335907828, 1e-10)));
    }

    @Test
    public void testIsZeroOneNormalized() {
        assertThat(new ProductSimilarity<>(new ConstDistance(0.0), new ConstDistance(0.0)).isZeroOneNormalized(), is(false));
    }

    @Test
    public void testIsSymmetric() {
        assertThat(new ProductSimilarity<>(new ConstDistance(0.0), new ConstDistance(0.0)).isSymmetric(), is(true));
    }

    private final class ConstDistance extends AbstractMeasure<Integer> {
        private final double dist;

        ConstDistance(double dist) {
            this.dist = dist;
        }

        @Override
        public Double getDistance(Integer a, Integer b) {
            return dist;
        }
    }
}
