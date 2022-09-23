package rysavpe1.reads.embedded;

import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.model.UnorientedObject;

/**
 * Embedding of a sequence object.
 *
 * @author Petr Ryšavý
 * @param <T> The target embedding space.
 */
public class EmbeddedSequence<T> extends EmbeddedObject<Sequence, T> implements UnorientedObject<EmbeddedSequence<T>> {

    /** Reversed sequence. */
    private EmbeddedSequence<T> reverse;
    /** Complementary sequence. */
    private EmbeddedSequence<T> complement;

    /**
     * Creates new embedded sequence object.
     * @param s The original sequence.
     * @param projection It's projection.
     */
    private EmbeddedSequence(Sequence s, T projection) {
        super(s, projection);
    }

    /**
     * Creates new embedded sequence. Automatically calculates reverse and
     * complementary sequences.
     * @param s The original sequence.
     * @param function The function that defines the embedding.
     */
    @SuppressWarnings("LeakingThisInConstructor")
    public EmbeddedSequence(Sequence s, EmbeddingFunction<Sequence, T> function) {
        this(s, function.project(s));
        reverse = new EmbeddedSequence<>(s.reverse(), function.project(s.reverse()));
        complement = new EmbeddedSequence<>(s.complement(), function.project(s.complement()));
        EmbeddedSequence<T> reverseComplement = new EmbeddedSequence<>(s.reverseComplement(), function.project(s.reverseComplement()));

        // set the links properly - this is already set
        this.reverse.reverse = this;
        this.reverse.complement = reverseComplement;
        this.complement.reverse = reverseComplement;
        this.complement.complement = this;
        reverseComplement.reverse = this.complement;
        reverseComplement.complement = this.reverse;
    }

    @Override
    public EmbeddedSequence<T> reverse() {
        return reverse;
    }

    @Override
    public EmbeddedSequence<T> complement() {
        return complement;
    }
}
