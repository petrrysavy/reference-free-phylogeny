package rysavpe1.reads.distance.simple;

import rysavpe1.reads.distance.DistanceCalculator;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.utils.MathUtils;

/**
 * Decorator of sequence distance that considers both orientations of strings
 * and also possibilities that the sequences are from the complementary strands.
 * Assumption is that both distances are symmetric. If we have two reads, we
 * cannot be sure that they are oriented in the same way or thay are from the
 * same strand. Therefore this class calculates both directions and uses the
 * smaller one.
 *
 * @author Petr Ryšavý
 */
public class UnorientedComplementDistance extends DecoratedDistance<Sequence> {

    public UnorientedComplementDistance(DistanceCalculator<Sequence, ? extends Number> innerDistance) {
        super(innerDistance);

        if (!innerDistance.isSymmetric())
            throw new IllegalArgumentException("Only symmetric distances are supported.");
    }

    @Override
    public Double getDistance(Sequence a, Sequence b) {
        return MathUtils.min(super.getDistance(a, b),
                super.getDistance(a, b.reverse()),
                super.getDistance(a, b.complement()),
                super.getDistance(a, b.reverseComplement())
        );
    }
}
