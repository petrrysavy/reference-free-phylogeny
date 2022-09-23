package rysavpe1.reads.distance.simple;

import java.util.Set;
import rysavpe1.reads.distance.DistanceCalculator;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.multiset.Multiset;

/**
 * A simple class that compares two multisets by taking distance of two most
 * similar objects in the sets.
 *
 * @author Petr Ryšavý
 * @param <T> The type of objects in the multisets.
 */
public class BestMatchNormDistance extends AbstractMultisetDistance<Sequence> {

    public BestMatchNormDistance(DistanceCalculator<Sequence, ? extends Number> innerDistance) {
        super(innerDistance);
    }

    @Override
    public boolean isSymmetric() {
        return innerDistance.isSymmetric();
    }

    @Override
    public boolean isZeroOneNormalized() {
        return true;
    }

    @Override
    public Double getDistance(Multiset<Sequence> a, Multiset<Sequence> b) {
        final Set<Sequence> bSet = b.toSet();
        double bestMatch = Double.POSITIVE_INFINITY;

        for (Sequence aElem : a.toSet()) {
            for (Sequence bElem : bSet)
                bestMatch = Math.min(innerDistance.getDistance(aElem, bElem).doubleValue() / Math.max(aElem.length(), bElem.length()), bestMatch);
        }
        return bestMatch;
    }

}
