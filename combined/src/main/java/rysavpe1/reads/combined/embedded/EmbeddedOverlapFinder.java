package rysavpe1.reads.combined.embedded;

import rysavpe1.reads.embedded.EmbeddedSequence;
import rysavpe1.reads.overlap.OverlapRegion;

/**
 *
 * @author petr
 * @param <T>
 */
public interface EmbeddedOverlapFinder<T> {

    public OverlapRegion getOverlap(EmbeddedSequence<T> a, EmbeddedSequence<T> b);
}
