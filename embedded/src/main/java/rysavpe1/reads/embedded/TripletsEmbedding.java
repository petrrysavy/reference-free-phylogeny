package rysavpe1.reads.embedded;

import rysavpe1.reads.bio.Nucleotides;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.utils.MathUtils;

/**
 * Embedding of sequences to metric space R**64. For each k-mer of length 3 in
 * the sequence we calculate number of its occurences and then we store those
 * numbers in a vector.
 *
 * @author Petr Ryšavý
 */
public class TripletsEmbedding implements EmbeddingFunction<Sequence, int[]> {

    /** Length of the k-mers. By default 3. */
    private static final int WORD_LEN = 3;
    /** Dimension of the target space. By default 64. */
    private static final int EMBEDDING_DIM = MathUtils.pow(Nucleotides.NUCLEOTIDES_COUNT, WORD_LEN);

    /**
     * Calculates occurences of all 3-mers in the sequence. {@inheritDoc }
     */
    @Override
    public int[] project(Sequence key) {
        final int[] embedding = new int[EMBEDDING_DIM];
        final char[] seq = key.getSequence();
        for (int i = 0; i <= seq.length - WORD_LEN; i++)
            ++embedding[calculateIndex(seq, i)];
        return embedding;
    }

    /** Gives index to a coordinate in embedded space. Each k-mer is given its
     * own coordinate.
     * @param arr The sequence.
     * @param offset Location of the first nucleotide of the k-mer.
     * @return first nucleotide * 16 + second * 4 + third.
     */
    protected static int calculateIndex(char[] arr, int offset) {
        return Nucleotides.toInteger(arr[offset]) * Nucleotides.NUCLEOTIDES_COUNT_SQ
                + Nucleotides.toInteger(arr[offset + 1]) * Nucleotides.NUCLEOTIDES_COUNT
                + Nucleotides.toInteger(arr[offset + 2]);
    }
}
