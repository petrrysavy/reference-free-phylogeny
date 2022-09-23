package rysavpe1.reads.embedded;

import java.util.Arrays;
import java.util.List;
import rysavpe1.reads.distance.AbstractMeasure;
import rysavpe1.reads.distance.DistanceCalculator;
import rysavpe1.reads.model.UnorientedIterator;
import rysavpe1.reads.model.UnorientedObject;
import rysavpe1.reads.utils.CollectionUtils;
import rysavpe1.reads.utils.IteratorWrapper;
import rysavpe1.reads.utils.StatsUtils;

/**
 * Monge-Elkan distance defined in the embedded space. Calculates distance in
 * the embedded space first. Then selects candidates that differ from the query
 * sequence by at most {@code 3} and then uses those candidates to calculate the
 * Monge-Elkan distance.
 *
 * @author Petr Ryšavý
 * @param <T> The type of embedded objects.
 * @param <K> The original space.
 * @param <V> The embedded space.
 */
public class MaxGapEmbeddedDistance<T extends EmbeddedObject<K, V> & UnorientedObject<T>, K, V> extends AbstractMeasure<EmbeddedMultiset<T, K, V>> {

    /** Distance in embedded space. */
    private final DistanceCalculator<V, ? extends Number> embeddedDistance;
    /** Distance in the original space. */
    private final DistanceCalculator<K, ? extends Number> innerDistance;
    /** Value of the maximal possible distance that can be found between objects
     * in the embedded space. */
    private final int maxEmbeddedDistance;
    /** Maximum distance in the original space. Used as in the thresholding
     * approach. */
    private final double maxDistance;

    /**
     * Crates instance of the Monge-Elkan distance in the embedded space.
     * @param embeddedDistance Distance in embedded space.
     * @param innerDistance Distance in the original space.
     * @param maxEmbeddedDistance Value of the maximal possible distance that
     * can be found between objects in the embedded space. If this value is too
     * low, then there may be some out of bounds error.
     * @param maxDistance Maximum distance in the original space. Used as in the
     * thresholding approach.
     */
    public MaxGapEmbeddedDistance(DistanceCalculator<V, ? extends Number> embeddedDistance,
            DistanceCalculator<K, ? extends Number> innerDistance,
            int maxEmbeddedDistance, double maxDistance) {
        this.embeddedDistance = embeddedDistance;
        this.innerDistance = innerDistance;
        this.maxEmbeddedDistance = maxEmbeddedDistance;
        this.maxDistance = maxDistance;
    }

    /**
     * Calculates the Monge-Elkan distance in the embedded space. Identifies few
     * candidates in the second embedded multiset that are close enough to each
     * element of the first embedded multiset. Then uses only those candidates
     * to calculate the exact distance.
     *
     * {@inheritDoc }
     */
    @Override
    public Double getDistance(EmbeddedMultiset<T, K, V> a, EmbeddedMultiset<T, K, V> b) {
        double embeddedMin;

        // here we will store the sequences by the difference from the currently searched one
        List<List<T>> byDiffs = CollectionUtils.nLists(maxEmbeddedDistance + 1);

        final double[] bestMatches = new double[a.uniqueSize()];
        final int[] counts = new int[bestMatches.length];
        int j = 0;

        for (T aElem : new IteratorWrapper<>(a.embeddedIterator())) {
            // initialize to find the least distant read in the other read set
            embeddedMin = Double.POSITIVE_INFINITY;

            CollectionUtils.clear(byDiffs);

            // iterate over the second read set to find the closest one
            for (T bElem : new UnorientedIterator<>(b.embeddedIterator())) {
                final double embeddedDist = embeddedDistance.getDistance(aElem.getProjected(), bElem.getProjected()).doubleValue();

                embeddedMin = Math.min(embeddedDist, embeddedMin);

                // TODO fix-me tohle je jen pro manhattanskou vzdálenost - co kdybych používal v budoucnu reálnou
                byDiffs.get(new Double(embeddedDist).intValue()).add(bElem);
            }

            // and finaly find the minimum from the candidate list
            double bestMatch = Double.POSITIVE_INFINITY;
            final int start = new Double(embeddedMin).intValue();
            assert (!byDiffs.get(start).isEmpty());
            // TODO fix-me the value 3 should not be hardcoded, note that in Manhattan and tripletts the distance is always even
            for (int i = start; i < Math.min(start + 3, byDiffs.size()); i++)
                for (T bElem : byDiffs.get(i))
                    bestMatch = Math.min(innerDistance.getDistance(aElem.getObject(), bElem.getObject()).doubleValue(), bestMatch);

            // store the numbers
            bestMatches[j] = bestMatch;
            counts[j] = a.count(aElem.getObject());
            j++;
//            distance += bestMatch * a.count(aElem.getObject());
        }

        double distance = 0.0;
        final boolean[] outlier = detectOutlier(bestMatches);
        for (int i = 0; i < bestMatches.length; i++)
            distance += (outlier[i] ? maxDistance : bestMatches[i]) * counts[i];
        return distance / a.size();
    }

    private boolean[] detectOutlier(double[] data) {
        // max gap
        final double[] dataCpy = Arrays.copyOf(data, data.length);
        Arrays.sort(dataCpy);

        // find the max gap
        double maxDiff = 0.0;
        int index = 0;
        for (int i = 1; i < data.length; i++) {
            final double diff = dataCpy[i] - dataCpy[i - 1];
            if (diff > maxDiff) {
                index = i;
                maxDiff = diff;
            }
        }

        boolean[] outlier = new boolean[data.length];
        // gap separates outliers
        if (index >= ((double) data.length) * 0.75 && maxDiff >= StatsUtils.stdev(data)) {
            double firstOut = dataCpy[index];
            for (int i = 0; i < data.length; i++)
                outlier[i] = data[i] >= firstOut;
        }

        return outlier;
    }

}
