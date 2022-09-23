package rysavpe1.reads.distance.simple;

import rysavpe1.reads.distance.DistanceCalculator;
import rysavpe1.reads.model.Sequence;

/**
 * Decorator of sequence distance that considers both complements of strings. If
 * we have two reads, we cannot be sure that they come from the same strand.
 * Therefore this class calculates both possibilities - that the strings come
 * from the same strand and that they come from different strands.
 *
 * @author Petr Ryšavý
 */
public class ComplementDistance extends DecoratedDistance<Sequence> {

    public ComplementDistance(DistanceCalculator<Sequence, ? extends Number> innerDistance) {
        super(innerDistance);
    }

    @Override
    public Double getDistance(Sequence a, Sequence b) {
        return Math.min(super.getDistance(a, b),
                super.getDistance(a, b.reverseComplement()));
    }
}
