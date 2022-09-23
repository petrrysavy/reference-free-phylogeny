/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rysavpe1.reads.combined.embedded;

import java.util.Arrays;
import java.util.Collection;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import rysavpe1.reads.embedded.EmbeddedSequence;
import rysavpe1.reads.embedded.EmbeddingFunction;
import rysavpe1.reads.embedded.TripletsEmbedding;
import rysavpe1.reads.embedded.TripletsIndexVectorEmbedding;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.overlap.OverlapRegion;

@RunWith(Parameterized.class)
public class EndReadsOverlapFinderTest {
    
    private static final EmbeddingFunction<Sequence, int[]> READ_EMBEDDING = new TripletsEmbedding();
    private static final EmbeddingFunction<Sequence, int[]> CONTIG_EMBEDDING = new TripletsIndexVectorEmbedding();
    
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {     
                 { "AAAAAAAAAAAAAAAAAAGTTTGAAGCTCG", "GTTTGAAAAAGCTCGAAAAA", 18, 30, 0, 15, 3 },
                 { "GTTTGAAAAAGCTGCAAAAA", "AAAAAAAAAAAAAAAAAAGTTTGAAGCTGC", 0, 15, 18, 30, 3 },
                 { "GCTGCAAAAAAAAAAGTTTG", "AAAAAGCTGCAAAAAAAAAAAAAGTTTGAA", 0, 20, 5, 28, 3 },
                 { "AAAAAGCTGCAAAAAAAAAAAAAGTTTGAA", "GCTGCAAAAAAAAAAGTTTG", 5, 28, 0, 20, 3 },
           });
    }
    
    private final double distance;
    private final int aLeft, aRight, bLeft, bRight;
    private final EmbeddedSequence<int[]> aSeq, bSeq;

    public EndReadsOverlapFinderTest(String aSeq, String bSeq, int aLeft, int aRight, int bLeft, int bRight, double distance) {
        this.distance = distance;
        this.aLeft = aLeft;
        this.aRight = aRight;
        this.bLeft = bLeft;
        this.bRight = bRight;
        this.aSeq = new EmbeddedSequence<>(Sequence.fromString(aSeq), CONTIG_EMBEDDING);
        this.bSeq = new EmbeddedSequence<>(Sequence.fromString(bSeq), CONTIG_EMBEDDING);
    }
    
    

    @Test
    public void testSomeMethod() {
        final OverlapRegion region = new EndReadsOverlapFinder(5, READ_EMBEDDING).getOverlap(aSeq, bSeq);
        System.err.println("for sequences "+aSeq.getObject() + " and "+bSeq.getObject());
        System.err.println("the overlap with distance "+region.getDistance()+" is");
        System.err.println(aSeq.getObject().subSequence(region.getStartA(), region.getEndA()));
        System.err.println(bSeq.getObject().subSequence(region.getStartB(), region.getEndB()));
        assertThat(region.getDistance(), is(equalTo(distance)));
        assertThat(region.getStartA(), is(equalTo(aLeft)));
        assertThat(region.getEndA(), is(equalTo(aRight)));
        assertThat(region.getStartB(), is(equalTo(bLeft)));
        assertThat(region.getEndB(), is(equalTo(bRight)));
    }
    
}
