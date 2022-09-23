package rysavpe1.reads.utils;

import java.util.BitSet;
import java.util.Random;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class RandomExt extends Random {

    public RandomExt() {
    }

    public RandomExt(long seed) {
        super(seed);
    }

    public int[] nDistinctIntegers(int n, int bound) {
        final BitSet map = nBitsSet(n, bound);
        final int[] arr = new int[n];
        arr[0] = map.nextSetBit(0);
        for (int i = 1; i < arr.length; i++)
            arr[i] = map.nextSetBit(arr[i - 1] + 1);
        return arr;
    }

    public BitSet nBitsSet(int n, int bound) {
        if (n < 0)
            throw new IllegalArgumentException("n cannot be negative : " + n);
        if (n == 0) return new BitSet();
        if (n > bound)
            throw new IllegalArgumentException("Cannot select n " + n + " bits out of " + bound + ".");
        if (n > bound / 2) {
            BitSet negation = nBitsSet(bound - n, bound);
            negation.flip(0, bound);
            return negation;
         }

        final BitSet map = new BitSet(n);
        for (int i = 0; i < n; i++) {
            int bit;
            do {
                bit = nextInt(bound);
            } while (map.get(bit));
            map.set(bit);
        }
        return map;
    }
}
