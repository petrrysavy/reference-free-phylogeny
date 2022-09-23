package rysavpe1.reads.distance.simple;

import rysavpe1.reads.distance.AbstractMeasure;
import rysavpe1.reads.model.ReadBag;
import rysavpe1.reads.model.ReadBagTuple;
import rysavpe1.reads.utils.Tuple;

/**
 *
 * @author Petr Ryšavý
 */
public class ReadBagSimilarityAsTupleSimilarity extends AbstractMeasure<ReadBagTuple> {

    private final AbstractMeasure<Tuple<ReadBag>> me;

    public ReadBagSimilarityAsTupleSimilarity(AbstractMeasure<Tuple<ReadBag>> me) {
        this.me = me;
    }

    @Override
    public Double getSimilarity(ReadBagTuple a, ReadBagTuple b) {
        throw new RuntimeException();
    }

    @Override
    public Double getDistance(ReadBagTuple a, ReadBagTuple b) {
        return me.getDistance(a, b);
    }
}
