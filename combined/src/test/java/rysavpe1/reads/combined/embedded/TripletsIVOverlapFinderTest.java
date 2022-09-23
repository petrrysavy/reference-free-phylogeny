package rysavpe1.reads.combined.embedded;

import java.util.Arrays;
import java.util.Collection;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import rysavpe1.reads.distance.simple.ManhattanDistance;
import rysavpe1.reads.embedded.EmbeddedSequence;
import rysavpe1.reads.embedded.EmbeddingFunction;
import rysavpe1.reads.embedded.TripletsEmbedding;
import rysavpe1.reads.embedded.TripletsIndexVectorEmbedding;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.overlap.OverlapRegion;

@RunWith(Parameterized.class)
public class TripletsIVOverlapFinderTest {
    private static final EmbeddingFunction<Sequence, int[]> EMBEDDING = new TripletsIndexVectorEmbedding();
    private static final EmbeddingFunction<Sequence, int[]> EMBEDDING_TRIPLETS = new TripletsEmbedding();

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {"GCATGA", "ATGTCGGG", 2, 6, 0, 4, 2, 2},
            {"ACTGAC", "GACTTT", 3, 6, 0, 3, 0, 2},
            {"ACTGACTG", "GACTGG", 3, 8, 0, 5, 0, 2},
            {"ACTGGG", "TTTACT", 0, 3, 3, 6, 0, 2},
            {"AAAAAACC", "TTTAAAAAA", 0, 6, 3, 9, 0, 2},
            {"AAACCCCCCC", "CTCCCCCG", 3, 10, 0, 7, 4, 2},
            {"AAAAAAGG", "CCCAAAATA", 0, 6, 3, 9, 4, 4},
            {"AAAAAATT", "CCCAAAATA", 0, 7, 2, 9, 4, 2},
            {"ACTGCTGCAACT", "GCTG", 3, 7, 0, 4, 0, 2},
//            {"ABCAEFGTIJKL", "DEFGH", 3, 7, 0, 5, 2, 2},
            {"ACACT", "TTTACACTGGGG", 0, 5, 3, 8, 0, 2},
//            {"DEFGH", "ABCAEFGTIJKL", 0, 5, 4, 8, 5, 2},
            {"GACGTAACAGAGGCTTCTTTATTCTAGGATTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTTT", "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAGATGTGTACTTCTTGATTATGGC", 0, 24, 31, 32 + 23, getDistance(Sequence.fromString("GACGTAACAGAGGCTTCTTTATTC"), Sequence.fromString("AGATGTGTACTTCTTGATTATGGC")), 10},
        });
    }

    private final EmbeddedSequence<int[]> pattern, query;
    private final int startA, endA, startB, endB;
    private final double distance;
    private final int threshold;

    public TripletsIVOverlapFinderTest(String pattern, String query, int startA, int endA, int startB, int endB, int distance, int threshold) {
        this.pattern = new EmbeddedSequence<>(Sequence.fromString(pattern), EMBEDDING);
        this.query = new EmbeddedSequence<>(Sequence.fromString(query), EMBEDDING);
        this.startA = startA;
        this.endA = endA;
        this.startB = startB;
        this.endB = endB;
        System.err.println("distance "+distance);
        this.distance = distance;
        this.threshold = threshold;
    }

    @Test
    public void testGetOverlap() {
        final OverlapRegion result = new TripletsIVOverlapFinder(threshold).getOverlap(pattern, query);
        System.err.println("pattern : " + pattern.getObject() + " query " + query.getObject());
        System.err.println("result : " + result + ", " + result.distanceToLengthRatio());
        assertThat(result.getStartA(), is(equalTo(startA)));
        assertThat(result.getEndA(), is(equalTo(endA)));
        assertThat(result.getStartB(), is(equalTo(startB)));
        assertThat(result.getEndB(), is(equalTo(endB)));
        assertThat(result.getDistance(), is(equalTo(distance)));
        assertThat((int) result.getDistance(), is(equalTo(
                getDistance(pattern.getObject().subSequence(startA, endA), query.getObject().subSequence(startB, endB)))));
        System.err.println("ok");
    }

    @Test
    public void testGetOverlapSym() {
        final OverlapRegion result = new TripletsIVOverlapFinder(threshold).getOverlap(query, pattern);
        System.err.println("query : " + query.getObject() + "\npattern " + pattern.getObject());
        System.err.println("result : " + result + ", " + result.distanceToLengthRatio());
        assertThat(result.getStartA(), is(equalTo(startB)));
        assertThat(result.getEndA(), is(equalTo(endB)));
        assertThat(result.getStartB(), is(equalTo(startA)));
        assertThat(result.getEndB(), is(equalTo(endA)));
        assertThat(result.getDistance(), is(equalTo(distance)));
        assertThat((int) result.getDistance(), is(equalTo(
                getDistance(pattern.getObject().subSequence(startA, endA), query.getObject().subSequence(startB, endB)))));
        System.err.println("ok");
    }
    
    private static int getDistance(Sequence a, Sequence b) {
        return new ManhattanDistance().getDistance(EMBEDDING_TRIPLETS.project(a), EMBEDDING_TRIPLETS.project(b)).intValue();
    }

    
}
