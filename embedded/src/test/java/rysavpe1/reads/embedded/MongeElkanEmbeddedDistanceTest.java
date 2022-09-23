package rysavpe1.reads.embedded;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.number.IsCloseTo.closeTo;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Test;
import rysavpe1.reads.distance.simple.EditDistance;
import rysavpe1.reads.distance.simple.ManhattanDistance;
import rysavpe1.reads.model.ReadBag;
import rysavpe1.reads.model.Sequence;

/**
 *
 * @author Petr Ryšavý
 */
public class MongeElkanEmbeddedDistanceTest {

    private MongeElkanEmbeddedDistance<EmbeddedSequence<int[]>, Sequence, int[]> distance;
    private EmbeddingFunction<Sequence, int[]> embedding;

    @Before
    public void bootstrap() {
        distance = new MongeElkanEmbeddedDistance<>(new ManhattanDistance(), new EditDistance(), 4);
        embedding = new TripletsEmbedding();
    }

    @Test
    public void testGetDistance() {
        final ReadBag bag1 = ReadBag.fromString("ATCA", "AAAA", "CATA");
        final ReadBag bag2 = ReadBag.fromString("TTTT", "ATAT");
        final EmbeddedReadBag<int[]> bag1emb = new EmbeddedReadBag<>(bag1, embedding);
        final EmbeddedReadBag<int[]> bag2emb = new EmbeddedReadBag<>(bag2, embedding);
        assertThat(distance.getDistance(bag1emb, bag2emb), is(closeTo(1.0, 1e-10)));
    }

    @Test
    public void testGetDistance2() {
        final ReadBag bag1 = ReadBag.fromString("ATAT");
        final ReadBag bag2 = ReadBag.fromString("TATA", "ATCT");
        final EmbeddedReadBag<int[]> bag1emb = new EmbeddedReadBag<>(bag1, embedding);
        final EmbeddedReadBag<int[]> bag2emb = new EmbeddedReadBag<>(bag2, embedding);
        assertThat(distance.getDistance(bag1emb, bag2emb), is(closeTo(0.0, 1e-10)));
    }

    @Test
    public void testGetDistance3() {
        final ReadBag bag1 = ReadBag.fromString("ACAC");
        final ReadBag bag2 = ReadBag.fromString("CACA", "ACGC");
        final EmbeddedReadBag<int[]> bag1emb = new EmbeddedReadBag<>(bag1, embedding);
        final EmbeddedReadBag<int[]> bag2emb = new EmbeddedReadBag<>(bag2, embedding);
        assertThat(distance.getDistance(bag1emb, bag2emb), is(closeTo(0.0, 1e-10)));
    }

}
