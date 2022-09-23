package rysavpe1.reads.embedded;

import java.util.Iterator;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import rysavpe1.reads.model.ReadBag;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.utils.ArrayUtils;

/**
 *
 * @author Petr Ryšavý
 */
public class EmbeddedReadBagTest {

    @Test
    public void testConstruct() {
        TripletsEmbedding function = new TripletsEmbedding();
        int[] expected = new int[64];//instance.project(key);
        for (int i : ArrayUtils.asArray(4, 16, 6, 24, 34, 10, 41, 39, 29, 53, 20, 18))
            expected[i] = 1;
        for (int i : ArrayUtils.asArray(1, 25, 38))
            expected[i] = 2;

        // now test construction
        ReadBag bag = ReadBag.fromString("AATAATCACCTCTCTGTTAC");
        EmbeddedReadBag<int[]> bagEmb = new EmbeddedReadBag<>(bag, function);
        assertThat(bagEmb.size(), is(equalTo(1)));
        assertThat(bagEmb.isEmpty(), is(false));
        assertThat(bagEmb.contains(Sequence.fromString("AATAATCACCTCTCTGTTAC")), is(true));
        assertThat(bagEmb.contains(Sequence.fromString("AATAATCACCTCTCTGTTACAAA")), is(false));
        assertThat(bagEmb.count(Sequence.fromString("AATAATCACCTCTCTGTTAC")), is(equalTo(1)));
        assertThat(bagEmb.count(Sequence.fromString("AATAATCACCTCTCTGTTACAAA")), is(equalTo(0)));
        final Iterator<EmbeddedSequence<int[]>> it = bagEmb.embeddedIterator();
        assertThat(it.hasNext(), is(true));
        EmbeddedSequence<int[]> seq = it.next();
        assertThat(seq.getObject(), is(equalTo(Sequence.fromString("AATAATCACCTCTCTGTTAC"))));
        assertThat(seq.getProjected(), is(equalTo(expected)));
        assertThat(it.hasNext(), is(false));
    }

}
