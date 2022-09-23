package rysavpe1.reads.combined.embedded;

import rysavpe1.reads.embedded.EmbeddedSequence;
import rysavpe1.reads.embedded.EmbeddingFunction;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.overlap.EditDistanceOverlapFinder;
import rysavpe1.reads.overlap.OverlapRegion;

/**
 * Use this in test to validate results.
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class CheatingOverlapFinder extends EditDistanceOverlapFinder implements EmbeddedOverlapFinder<int[]> {

    public CheatingOverlapFinder(int readLength, EmbeddingFunction<Sequence, int[]> readsEmbedding) {
        super(20);
    }


    @Override
    public OverlapRegion getOverlap(EmbeddedSequence<int[]> a, EmbeddedSequence<int[]> b) {

        return super.getOverlap(a.getObject(), b.getObject());

    }

}
