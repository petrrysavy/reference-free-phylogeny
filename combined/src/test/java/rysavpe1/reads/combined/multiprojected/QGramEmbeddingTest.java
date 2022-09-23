package rysavpe1.reads.combined.multiprojected;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import rysavpe1.reads.model.Sequence;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class QGramEmbeddingTest {
    
    private final QGramEmbedding embedding = new QGramEmbedding();
    
    @Test
    public void testProject() {
        assertThat(embedding.projectIV(Sequence.fromString("AACTG"), 1), is(equalTo(new int[]{0, 0, 2, 1, 3})));
        assertThat(embedding.projectIV(Sequence.fromString("AAAC"), 2), is(equalTo(new int[]{0, 0, 2})));
        assertThat(embedding.projectIV(Sequence.fromString("AAAAC"), 3), is(equalTo(new int[]{0, 0, 2})));
        assertThat(embedding.projectIV(Sequence.fromString("AAAAAC"), 4), is(equalTo(new int[]{0, 0, 2})));
        assertThat(embedding.projectIV(Sequence.fromString("AAAAAAC"), 5), is(equalTo(new int[]{0, 0, 2})));
        assertThat(embedding.projectIV(Sequence.fromString("AAAAAAAC"), 6), is(equalTo(new int[]{0, 0, 2})));
        assertThat(embedding.projectIV(Sequence.fromString("AAAAAAAAC"), 7), is(equalTo(new int[]{0, 0, 2})));
    }
    
}
