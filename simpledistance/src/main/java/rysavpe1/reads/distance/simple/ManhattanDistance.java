package rysavpe1.reads.distance.simple;

import rysavpe1.reads.distance.AbstractMeasure;

/**
 * Manhattan distance on vectors of natural numbers.
 *
 * @author Petr Ryšavý
 */
public class ManhattanDistance extends AbstractMeasure<int[]> {

    @Override
    public Double getDistance(int[] a, int[] b) {
        assert (a.length == b.length);

        int distance = 0;
        for (int i = 0; i < a.length; i++)
            distance += Math.abs(a[i] - b[i]);
        return Double.valueOf(distance);
    }
}
