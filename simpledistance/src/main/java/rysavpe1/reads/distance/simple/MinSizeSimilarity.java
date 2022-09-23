package rysavpe1.reads.distance.simple;

import rysavpe1.reads.distance.AbstractMeasure;
import rysavpe1.reads.model.ReadBag;

/**
 *
 * @author Petr Ryšavý
 */
public class MinSizeSimilarity extends AbstractMeasure<ReadBag> {

    @Override
    public Double getSimilarity(ReadBag a, ReadBag b) {
        throw new UnsupportedOperationException("not normalized");
    }

    @Override
    public Double getDistance(ReadBag a, ReadBag b) {
        return new Double(Math.min(a.size(), b.size()));
    }
    
}
