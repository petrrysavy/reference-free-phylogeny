package rysavpe1.reads.combined.multiprojected.model;

import rysavpe1.reads.combined.multiprojected.QGramEmbedding;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.model.UnorientedObject;

/**
 * This class provides basic embedding for contigs.
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class QGramContig implements UnorientedObject<QGramContig> {

    /** The embedding used for this type of contigs. */
    private static final QGramEmbedding EMBEDDING = new QGramEmbedding();

    /** The contig sequence. */
    private Sequence s;
    /** Reversed sequence. */
    private QGramContig reverse;
    /** Complementary sequence. */
    private QGramContig complement;

    /** The inner map with q-gram index vector embeddings. */
    int[][] indexVectors = new int[QGramEmbedding.MAX_Q + 1][];
    /** The inner map with q-gram counts. */
    int[][] embeddings = new int[QGramEmbedding.MAX_Q + 1][];

    /**
     * Creates new embedded sequence object.
     * @param s The original sequence.
     * @param projection It's projection.
     */
    private QGramContig() {
    }

    /**
     * Creates new embedded sequence. Automatically calculates reverse and
     * complementary sequences.
     * @param s The original sequence.
     * @param function The function that defines the embedding.
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public QGramContig(Sequence s) {
        this.s = s;
        reverse = constructWithOnlySequence(s.reverse());
        complement = constructWithOnlySequence(s.complement());
        QGramContig reverseComplement = constructWithOnlySequence(s.reverseComplement());

        // set the links properly - this is already set
        this.reverse.reverse = this;
        this.reverse.complement = reverseComplement;
        this.complement.reverse = reverseComplement;
        this.complement.complement = this;
        reverseComplement.reverse = this.complement;
        reverseComplement.complement = this.reverse;
    }

    private static QGramContig constructWithOnlySequence(Sequence s) {
        QGramContig contig = new QGramContig();
        contig.s = s;
        return contig;
    }

    public int[] indexVector(int q) {
        assert (q > 0 && q <= QGramEmbedding.MAX_Q);

        if (indexVectors[q] == null)
            indexVectors[q] = EMBEDDING.projectIV(s, q);

        return indexVectors[q];
    }

    public int[] embedding(int q) {
        assert (q > 0 && q <= QGramEmbedding.MAX_Q);

        if (embeddings[q] == null)
            embeddings[q] = EMBEDDING.project(s, q);

        return embeddings[q];
    }

    @Override
    public QGramContig reverse() {
        return reverse;
    }

    @Override
    public QGramContig complement() {
        return complement;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(! (obj instanceof QGramContig)) return false;
        return ((QGramContig) obj).s.equals(this.s);
    }

    @Override
    public int hashCode() {
        return s.hashCode();
    }
    
    public Sequence getObject() {
        return s;
    }
    
}
