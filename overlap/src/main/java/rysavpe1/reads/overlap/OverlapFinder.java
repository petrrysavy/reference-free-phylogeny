package rysavpe1.reads.overlap;

import rysavpe1.reads.model.Sequence;

/**
 *
 * @author petr
 */
public interface OverlapFinder {

    public OverlapRegion getOverlap(Sequence a, Sequence b);
}
