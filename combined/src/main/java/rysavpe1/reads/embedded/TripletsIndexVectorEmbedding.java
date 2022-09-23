package rysavpe1.reads.embedded;

import static rysavpe1.reads.embedded.TripletsEmbedding.calculateIndex;
import rysavpe1.reads.model.Sequence;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class TripletsIndexVectorEmbedding implements EmbeddingFunction<Sequence, int[]> {

    @Override
    public int[] project(Sequence key) {
        final char[] arr = key.getSequence();
        final int[] indices = new int[arr.length - 2];
        for (int i = 0; i < indices.length; i++)
            indices[i] = calculateIndex(arr, i);
        return indices;
    }
    
    public static int[] window(int[] indexVector, int from, int to) {
        final int[] window = new int[64];
        for (int i = from; i < to; i++)
            window[indexVector[i]]++;
        return window;
    }

}
