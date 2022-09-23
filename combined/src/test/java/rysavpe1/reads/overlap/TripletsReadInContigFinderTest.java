package rysavpe1.reads.overlap;

import java.util.List;
import java.util.stream.Collectors;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import org.junit.Test;
import static org.junit.Assert.*;
import rysavpe1.reads.embedded.EmbeddedSequence;
import rysavpe1.reads.embedded.TripletsEmbedding;
import rysavpe1.reads.embedded.TripletsIndexVectorEmbedding;
import rysavpe1.reads.model.Sequence;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class TripletsReadInContigFinderTest {

    @Test
    public void testSomeMethod() {
        TripletsIndexVectorEmbedding emb = new TripletsIndexVectorEmbedding();
        TripletsEmbedding embRead = new TripletsEmbedding();
        final ReadInContig pos = new TripletsReadInContigFinder().findReadInContig(
                new EmbeddedSequence<>(Sequence.fromString("TGCTG"), embRead),
                new EmbeddedSequence<>(Sequence.fromString("AATGCTGCAACT"), emb));
        assertThat(pos.getContigStart(), is(equalTo(2)));
        assertThat(pos.getContigEnd(), is(equalTo(7)));
        assertThat(pos.getContigLength(), is(equalTo(5)));
        assertThat(pos.getReadLength(), is(equalTo(5)));
        assertThat(pos.getReadStart(), is(equalTo(0)));
        assertThat(pos.getReadEnd(), is(equalTo(5)));
        assertThat(pos.getDistance(), is(equalTo(0.0)));
    }

    @Test
    public void testMultiple() {
        TripletsIndexVectorEmbedding emb = new TripletsIndexVectorEmbedding();
        TripletsEmbedding embRead = new TripletsEmbedding();
        final List<ReadInContig> poss = new TripletsReadInContigFinder().findMultipleMatches(
                new EmbeddedSequence<>(Sequence.fromString("TGCTG"), embRead),
                new EmbeddedSequence<>(Sequence.fromString("AATGCTGCAATGCTGAT"), emb),
                1);
        assertThat(poss.stream().map((value) -> {return value.getContigStart();}).collect(Collectors.toList()),
                containsInAnyOrder(2, 3, 10));
    }

}
