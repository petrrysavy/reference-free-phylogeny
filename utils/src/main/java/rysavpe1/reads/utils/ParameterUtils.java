package rysavpe1.reads.utils;

/**
 *
 * @author Petr Ryšavý
 */
public class ParameterUtils {

    private ParameterUtils() {
    }

    public static void checkInRange(double min, double max, double value) {
        if (value < min || value > max)
            throw new IllegalArgumentException("Value " + value + " is not between " + min + " and " + max);
    }
}
