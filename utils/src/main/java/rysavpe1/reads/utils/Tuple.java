package rysavpe1.reads.utils;

import java.util.Objects;

/**
 *
 * @author petr
 * @param <T>
 */
public class Tuple<T> extends Pair<T, T> {

    public Tuple(T value1, T value2) {
        super(value1, value2);
    }
    
    /**
     * Tests vhether the two values are equal.
     * @return {@code true} if {@code value1} equals to {@code value2}.
     */
    public boolean valuesEqual() {
        return Objects.equals(value1, value2);
    }

}
