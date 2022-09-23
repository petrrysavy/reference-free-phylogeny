package rysavpe1.reads.combined.multiprojected;

import java.util.Random;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Ignore;
import org.junit.Test;
import rysavpe1.reads.bio.Nucleotides;
import rysavpe1.reads.distance.simple.ManhattanDistance;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.utils.MathUtils;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class QGramOverlapFinderTest {
    private final int MIN_OVERLAP = 20;
    private final QGramEmbedding EMBEDDING = new QGramEmbedding();
    private final ManhattanDistance DISTANCE = new ManhattanDistance();

    @Test
    @Ignore // This test runs cca 10sec, currently passes - I do not want to spend too much time building the project in development
    public void testOnRandomSequences() {
        Random r = new Random(42);
        final Sequence s1 = randomSequence(r, 10000);
        final Sequence s2 = randomSequence(r, 6000);
        double minValue = Double.POSITIVE_INFINITY;
        
        for(int q = 3; q <= 7; q++) {
            final int qPlusOnePow = MathUtils.pow(Nucleotides.NUCLEOTIDES_COUNT, q+1);
            final int qMinusOnePow = MathUtils.pow(Nucleotides.NUCLEOTIDES_COUNT, q-1);
            
            for(int len = MIN_OVERLAP; len <= 6000; len++) {
                if(len > qPlusOnePow)
                    break;
                
                if(len > qMinusOnePow) {
                    minValue = Math.min(minValue, getOverlapValue(s1.subSequence(0, len), s2.subSequence(s2.length() - len, s2.length()), q) / len);
                    minValue = Math.min(minValue, getOverlapValue(s2.subSequence(0, len), s1.subSequence(s1.length() - len, s1.length()), q) / len);
                }
            }
            if(6000 < qPlusOnePow)
                for(int i = 0; i <= 4000; i++)
                    minValue = Math.min(minValue, getOverlapValue(s1.subSequence(i, i+6000), s2, q) / 6000);
                    
        }
        
        assertThat(new QGramOverlapFinder(MIN_OVERLAP).getOverlap(s1, s2).distanceToLengthRatio(), is(equalTo(minValue)));
    }
    
    private Sequence randomSequence(Random r, int length) {
        return new Sequence(Nucleotides.fromInteger(r.ints(length, 0, Nucleotides.NUCLEOTIDES_COUNT).toArray()), "A random sequence of length "+length);
    }
    
    private double getOverlapValue(Sequence s1, Sequence s2, int q) {
        return DISTANCE.getDistance(EMBEDDING.project(s1, q), EMBEDDING.project(s2, q)) / q;
    }

}
