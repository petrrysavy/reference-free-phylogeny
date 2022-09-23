package rysavpe1.reads.combined.multiprojected;

import rysavpe1.reads.utils.MathUtils;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class QGramMatcher {
    
    private final int embeddingDimension;

    public QGramMatcher(int q) {
        embeddingDimension = MathUtils.pow(4, q);
    }
    
    public int[] distancesVectorBoth(int[] firstIndexVector, int[] secondIndexVector) {
        assert (firstIndexVector.length >= secondIndexVector.length);

        final int[] distancesVector = new int[firstIndexVector.length + secondIndexVector.length + 1];
        final int[] firstWindow = new int[embeddingDimension];
        final int[] secondWindow = new int[embeddingDimension];
        int distance = 0;
        // suffix of second being matched with the prefix of the first one
        int i = 1;
        for (; i <= secondIndexVector.length; i++) {
            distance += addingDistance(firstWindow, secondWindow, firstIndexVector[i - 1]);
            distance += addingDistance(secondWindow, firstWindow, secondIndexVector[secondIndexVector.length - i]);
            distancesVector[i] = distance;
        }
        for (; i <= firstIndexVector.length; i++) {
            distance += addingDistance(firstWindow, secondWindow, firstIndexVector[i - 1]);
            distance += removingDistance(firstWindow, secondWindow, firstIndexVector[i - secondIndexVector.length - 1]);
            distancesVector[i] = distance;
        }
        for(; i < distancesVector.length - 1; i++) {
            distance += removingDistance(firstWindow, secondWindow, firstIndexVector[i - secondIndexVector.length - 1]);
            distance += removingDistance(secondWindow, firstWindow, secondIndexVector[distancesVector.length - i - 1]);
            distancesVector[i] = distance;
        }
        return distancesVector;
    }

    private int addingDistance(int[] window, int[] otherWindow, int index) {
        window[index]++;
        return window[index] <= otherWindow[index] ? -1 : 1;
    }

    private int removingDistance(int[] window, int[] otherWindow, int index) {
        window[index]--;
        return window[index] >= otherWindow[index] ? - 1 : 1;
    }
}
