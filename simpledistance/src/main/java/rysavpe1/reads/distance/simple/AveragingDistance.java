package rysavpe1.reads.distance.simple;

import java.util.ArrayList;
import java.util.List;
import rysavpe1.reads.distance.DistanceCalculator;
import rysavpe1.reads.multiset.Multiset;

/**
 *
 * @author Petr Ryšavý
 * @param <T>
 */
public class AveragingDistance<T> extends AbstractMultisetDistance<T> {

    public AveragingDistance(DistanceCalculator<T, ? extends Number> innerDistance) {
        super(innerDistance);
    }

    @Override
    public Double getDistance(Multiset<T> a, Multiset<T> b) {
        final List<T> aList = new ArrayList(a.toSet());
        final List<T> bList = new ArrayList(b.toSet());
        final double[][] distances = innerDistance.getDistanceMatrix(aList, bList);

        double distance = 0.0;
        for (int i = 0; i < aList.size(); i++)
            for (int j = 0; j < bList.size(); j++)
                distance += distances[i][j] * a.count(aList.get(i)) * b.count(bList.get(j));

        return distance / a.size() / b.size();
    }

    @Override
    public boolean isSymmetric() {
        return innerDistance.isSymmetric();
    }
}
