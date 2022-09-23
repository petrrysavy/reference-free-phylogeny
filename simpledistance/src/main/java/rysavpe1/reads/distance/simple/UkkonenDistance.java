package rysavpe1.reads.distance.simple;

import java.util.Arrays;
import rysavpe1.reads.distance.DistanceCalculator;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.utils.MathUtils;

/**
 * Standard edit distance calculation, but faster implementing the Ukkonen's
 * cutoff algorithm. I took a great deal of inspiration from
 * {@link https://github.com/sunesimonsen/ukkonen/blob/master/index.js}.
 *
 * @author Petr Ryšavý
 */
public class UkkonenDistance implements DistanceCalculator<Sequence, Integer> {

    @Override
    public Integer getDistance(Sequence a, Sequence b) {
        return this.getDistance(a, b, Integer.MAX_VALUE);
    }

    @SuppressWarnings("empty-statement")
    public Integer getDistance(Sequence a, Sequence b, int threshold) {
        if (a.length() > b.length())
            return getDistance(b, a, threshold);

        char[] aSeq = a.getSequence(), bSeq = b.getSequence();
        int aLen = aSeq.length, bLen = bSeq.length;

        // suffix trimming
        while (aLen > 0 && aSeq[--aLen] == bSeq[--bLen]);
        aLen++; // revert the last decrement
        bLen++;

        // a is suffix of b
        if (aLen == 0) return bLen;

        // prefix trimming
        int tStart = 0;
        while (tStart < aLen && aSeq[tStart] == bSeq[tStart])
            tStart++;
        aLen -= tStart;
        bLen -= tStart;

        if (aLen == 0) return bLen;

        if(bLen < threshold) threshold = bLen;
        
        final int dLen = bLen - aLen;
        if(threshold < dLen) return threshold;
        
        final int ZERO_K = ((aLen < threshold ? aLen : threshold) >> 1) + 2; // floor(min(threshold, aLen) / 2) + 2
        final int arrLength = dLen + ZERO_K * 2 + 2;
        int[] currentRow = new int[arrLength];
        int[] nextRow = new int[arrLength];
        for(int i = 0; i < arrLength; i++) currentRow[i] = nextRow[i] = -1;
        
        if(aLen != a.length()) {
            aSeq = Arrays.copyOfRange(aSeq, tStart, tStart + aLen);
            bSeq = Arrays.copyOfRange(bSeq, tStart, tStart + bLen);
        }
        
        int i = 1;
        final int conditionRow = dLen + ZERO_K;
        final int endMax = conditionRow << 1;
        
        do {
            int start, previousCell, currentCell = -1, nextCell;
            
            if(i <= ZERO_K) {
                start =  -i + 1;
                nextCell = i - 2;
            } else {
                start = i - (ZERO_K << 1) + 1;
                nextCell = currentRow[ZERO_K + start];
            }
            
            int end;
            if(i <= conditionRow) {
                end = i;
                nextRow[ZERO_K + i] = -1;
            } else end = endMax - i;
            
            for(int k = start, rowIndex = start + ZERO_K; k < end; k++, rowIndex++) {
                previousCell = currentCell;
                currentCell = nextCell;
                nextCell = currentRow[rowIndex + 1];
                
                int t = MathUtils.max(currentCell + 1, previousCell, nextCell + 1);

                while(t < aLen && t + k < bLen && aSeq[t] == bSeq[t + k]) t++;
                
                nextRow[rowIndex] = t;
            }
            
            
            if(nextRow[conditionRow] >= aLen || i > threshold)
                return i -1;
            
            final int[] tmp = currentRow;
            currentRow = nextRow;
            nextRow = tmp;
            i++;
        } while(true);
    }
}
