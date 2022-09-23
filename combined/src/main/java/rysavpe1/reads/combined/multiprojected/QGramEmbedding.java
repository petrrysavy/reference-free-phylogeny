package rysavpe1.reads.combined.multiprojected;

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
public class QGramEmbedding {
    
    /** The maximum allowed q-gram size - the sequence is not likely to be bigger. */
    public static final int MAX_Q = 20;

    /**
     * Calculates occurences of all q-mers in the sequence. {@inheritDoc }
     */
    public int[] projectIV(Sequence key, int q) {
        final char[] arr = key.getSequence();
        final int[] indices = new int[arr.length - (q - 1)];
        for (int i = 0; i < indices.length; i++)
            indices[i] = calculateIndex(arr, i, q);
        return indices;
    }

    public int[] project(Sequence key, int q) {
        final int embedding_dimension = MathUtils.pow(Nucleotides.NUCLEOTIDES_COUNT, q);
        final int[] embedding = new int[embedding_dimension];
        final char[] seq = key.getSequence();
        for (int i = 0; i <= seq.length - q; i++)
            ++embedding[calculateIndex(seq, i, q)];
        return embedding;
    }

    /** Gives index to a coordinate in embedded space. Each k-mer is given its
     * own coordinate.
     * @param arr The sequence.
     * @param offset Location of the first nucleotide of the k-mer.
     * @return first nucleotide * 16 + second * 4 + third.
     */
    protected static int calculateIndex(char[] arr, int offset, int q) {
        switch (q) {
            case 1:
                return Nucleotides.toInteger(arr[offset]);
            case 2:
                return Nucleotides.toInteger(arr[offset]) * Nucleotides.NUCLEOTIDES_COUNT
                        + Nucleotides.toInteger(arr[offset + 1]);
            case 3:
                return Nucleotides.toInteger(arr[offset]) * Nucleotides.NUCLEOTIDES_COUNT_SQ
                        + Nucleotides.toInteger(arr[offset + 1]) * Nucleotides.NUCLEOTIDES_COUNT
                        + Nucleotides.toInteger(arr[offset + 2]);
            case 4:
                return Nucleotides.toInteger(arr[offset]) * Nucleotides.NUCLEOTIDES_COUNT_CUBE
                        + Nucleotides.toInteger(arr[offset + 1]) * Nucleotides.NUCLEOTIDES_COUNT_SQ
                        + Nucleotides.toInteger(arr[offset + 2]) * Nucleotides.NUCLEOTIDES_COUNT
                        + Nucleotides.toInteger(arr[offset + 3]);
            case 5:
                return Nucleotides.toInteger(arr[offset]) * Nucleotides.NUCLEOTIDES_COUNT_POW4
                        + Nucleotides.toInteger(arr[offset + 1]) * Nucleotides.NUCLEOTIDES_COUNT_CUBE
                        + Nucleotides.toInteger(arr[offset + 2]) * Nucleotides.NUCLEOTIDES_COUNT_SQ
                        + Nucleotides.toInteger(arr[offset + 3]) * Nucleotides.NUCLEOTIDES_COUNT
                        + Nucleotides.toInteger(arr[offset + 4]);
            default:
                int value = 0;
                for (int i = 0; i < q; i++)
                    value = Nucleotides.NUCLEOTIDES_COUNT * value + Nucleotides.toInteger(arr[offset + i]);
                return value;
        }

    }
}
