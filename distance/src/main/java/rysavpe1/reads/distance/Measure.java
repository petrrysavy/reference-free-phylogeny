package rysavpe1.reads.distance;

/**
 * Measure can calculate similarity and/or distance. It has some properties.
 *
 * @author Petr Ryšavý
 */
public interface Measure {

    /**
     * Tests whether this measure is symmetric. The measure is symmetric if for
     * all a, b hodls that dist(a,b)=dist(b,a).
     * @return Is this measure symmetric.
     */
    public default boolean isSymmetric() {
        return true;
    }

    /**
     * Tests whether this measure is normalized to values between zero and one.
     * That means that for all a : dist(a) is between 0 and 1.
     * @return Is this measure normalized to unit interval.
     */
    public default boolean isZeroOneNormalized() {
        return false;
    }
}
