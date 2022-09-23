package rysavpe1.reads.utils;

import java.util.List;
import java.util.StringTokenizer;

/**
 * Utility class for parsing input.
 *
 * @author Petr Ryšavý
 */
public class ParseUtils {

    /** Do not let anybody to instantiate the class. */
    private ParseUtils() {
    }

    /**
     * Captures a list of integers from a string tokenizer, assuming that all
     * tokens are integers.
     * @param st Tokenizer with the input.
     * @param list List, where the result should be added.
     */
    public static void parseAllToIntList(StringTokenizer st, List<Integer> list) {
        while (st.hasMoreTokens()) {
            list.add(Integer.parseInt(st.nextToken()));
        }
    }

    /**
     * Captures a list of doubles from a string tokenizer, assuming that all
     * tokens are doubles.
     * @param st Tokenizer with the input.
     * @param list List, where the result should be added.
     */
    public static void parseAllToDoubleList(StringTokenizer st, List<Double> list) {
        while (st.hasMoreTokens()) {
            list.add(Double.parseDouble(st.nextToken()));
        }
    }

    /**
     * Captures strings from a string tokenizer.
     * @param st Tokenizer with the input.
     * @param list List, where the result should be added.
     */
    public static void parseAllToStringList(StringTokenizer st, List<String> list) {
        while (st.hasMoreTokens()) {
            list.add(st.nextToken());
        }
    }

    /**
     * Parses a boolean value.
     * @param string Boolean value, {@code true} and {@code 1} and {@code yes}
     * are interpreted as true value, opposite as false.
     * @return Parsed boolean value.
     * @throws NumberFormatException When {@code string} is not in form
     * {@code true/false}, {@code 1/0} or {@code yes/no}.
     */
    public static boolean parseBoolean(String string) {
        if (string.equalsIgnoreCase("true") || string.equalsIgnoreCase("1") || string.equalsIgnoreCase("yes"))
            return true;
        if (string.equalsIgnoreCase("false") || string.equalsIgnoreCase("0") || string.equalsIgnoreCase("no"))
            return false;
        throw new NumberFormatException("Unknown boolean value : " + string);
    }

    public static double[][] parseDoubleMatrix(List<String> lines) {
        final double[][] matrix = new double[lines.size()][lines.size()];
        for (int i = 0; i < matrix.length; i++) {
            final StringTokenizer st = new StringTokenizer(lines.get(i));
            for (int j = 0; j < matrix.length; j++)
                matrix[i][j] = Double.parseDouble(st.nextToken());
        }
        return matrix;
    }
}
