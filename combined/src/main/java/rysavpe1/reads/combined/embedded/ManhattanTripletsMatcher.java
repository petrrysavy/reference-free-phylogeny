package rysavpe1.reads.combined.embedded;

import rysavpe1.reads.embedded.TripletsIndexVectorEmbedding;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class ManhattanTripletsMatcher {

    public int[] distancesVectorSubstring(int[] projectedRead, int[] contigIndexVector, int readLength) {
        final int readIVLength = readLength - 2;
        final int[] distancesVector = new int[contigIndexVector.length - readIVLength + 1]; // 2 is for the embedding size - cIV is projected and by 2 shorter !
        final int[] window = TripletsIndexVectorEmbedding.window(contigIndexVector, 0, readIVLength);
        int distance = 0;
        for (int i = 0; i < 64; i++)
            distance += Math.abs(projectedRead[i] - window[i]);

        distancesVector[0] = distance;
        for (int i = 0; i < distancesVector.length - 1; i++) {
            distance += addingDistance(window, projectedRead, contigIndexVector[i + readIVLength]);
            distance += removingDistance(window, projectedRead, contigIndexVector[i]);

            distancesVector[i + 1] = distance;
        }
        return distancesVector;
    }

    public int[] distancesVectorOverlap(int[] firstIndexVector, int[] secondIndexVector) {
        final int[] distancesVector = new int[Math.min(firstIndexVector.length, secondIndexVector.length) + 1];
        final int[] firstWindow = new int[64];
        final int[] secondWindow = new int[64];
        int distance = 0;
        for (int i = 1; i < distancesVector.length; i++) {
            distance += addingDistance(firstWindow, secondWindow, firstIndexVector[firstIndexVector.length - i]);
            distance += addingDistance(secondWindow, firstWindow, secondIndexVector[i - 1]);
            distancesVector[i] = distance;
        }
        return distancesVector;
    }

    public int[] distancesVectorBoth(int[] firstIndexVector, int[] secondIndexVector) {
        assert (firstIndexVector.length >= secondIndexVector.length);

        final int[] distancesVector = new int[firstIndexVector.length + secondIndexVector.length + 1];
        final int[] firstWindow = new int[64];
        final int[] secondWindow = new int[64];
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
