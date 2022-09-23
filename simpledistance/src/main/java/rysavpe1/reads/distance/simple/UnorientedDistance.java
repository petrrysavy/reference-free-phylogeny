package rysavpe1.reads.distance.simple;

import rysavpe1.reads.distance.DistanceCalculator;
import rysavpe1.reads.model.Sequence;

/**
 * Decorator of sequence distance that considers both orientations of strings.
 * Assumption is that both distances are symmetric. If we have two reads, we cannot
 * be sure that they are oriented in the same way. Therefore this class calculates
 * both directions and uses the smaller one.
 *
 * @author Petr Ryšavý
 */
public class UnorientedDistance extends DecoratedDistance<Sequence> {

    public UnorientedDistance(DistanceCalculator<Sequence, ? extends Number> innerDistance) {
        super(innerDistance);
        
        if (!innerDistance.isSymmetric())
            throw new IllegalArgumentException("Only symmetric distances are supported.");
    }

    @Override
    public Double getDistance(Sequence a, Sequence b) {
        final Sequence bReversed = b.reverse();
        return Math.min(super.getDistance(a, b),
            super.getDistance(a, bReversed));
    }
}
