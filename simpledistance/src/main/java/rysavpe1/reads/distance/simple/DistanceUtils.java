package rysavpe1.reads.distance.simple;

import rysavpe1.reads.distance.DistanceCalculator;
import rysavpe1.reads.model.Sequence;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class DistanceUtils {

    private DistanceUtils() {
    }

    public static DistanceCalculator<Sequence, ? extends Number> getOrientedDistance(
            boolean shouldReverse, boolean shouldComplement,
            DistanceCalculator<Sequence, ? extends Number> innerDistance) {
        if (shouldReverse && shouldComplement)
            return new UnorientedComplementDistance(innerDistance);
        else if (shouldReverse) return new UnorientedDistance(innerDistance);
        else if (shouldComplement) return new ComplementDistance(innerDistance);
        else // normal edit distance return innerDistance;
            return innerDistance;
    }

}
