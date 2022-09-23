package rysavpe1.reads.utils;

import java.util.List;

/**
 * Utility class for manipulating strings.
 *
 * @author Petr Ryšavý
 */
public final class StringUtils {

    /** Do not let anybody to instantiate the class. */
    private StringUtils() {
    }

    /**
     * Converts a list of trings to an array of characters.
     * @param stringList List of strings, for example {@code ["abc", "def"]}.
     * @return A char array, for example {@code [a, b, c, d, e, f]}.
     */
    public static char[] toCharArray(List<String> stringList) {
        char[] array = new char[countChars(stringList)];
        int destIndex = 0;
        for (String s : stringList) {
            System.arraycopy(s.toCharArray(), 0, array, destIndex, s.length());
            destIndex += s.length();
        }
        return array;
    }

    /**
     * Converts a list of trings to an array of characters and includes a
     * separator between each of the strings.
     * @param stringList List of strings, for example {@code ["abc", "def"]}.
     * @param separator This string will be placed between any two strings for
     * the list. This may be for example a linebreak.
     * @return A char array, for example {@code [a, b, c, d, e, f]}.
     */
    public static char[] toCharArray(List<String> stringList, String separator) {
        final char[] separatorCh = separator.toCharArray();
        char[] array = new char[countChars(stringList) + separatorCh.length * (stringList.size() - 1)];
        int destIndex = 0;
        for (String s : stringList) {
            System.arraycopy(s.toCharArray(), 0, array, destIndex, s.length());
            destIndex += s.length();
            if (destIndex != array.length) {
                System.arraycopy(separatorCh, 0, array, destIndex, separatorCh.length);
                destIndex += separatorCh.length;
            }
        }
        return array;
    }

    /**
     * Counts how many characters are there in a list of strings.
     * @param stringList List of strings, for example {@code ["abc", "def"]}.
     * @return Count of characters in the list, for example {@code 6}.
     */
    public static int countChars(List<String> stringList) {
        int length = 0;
        for (String s : stringList)
            length += s.length();
        return length;
    }

    /**
     * Crates a new string that is formed by {@code k} copies of the input
     * string.
     * @param st The pattern, for example {@code "ab"}.
     * @param k The count, for example {@code 3}.
     * @return {@code st} copies {@code k}-times. For example {@code "ababab"}.
     */
    public static String kCopies(String st, int k) {
        StringBuilder sb = new StringBuilder(st.length() * k);
        for (int i = 0; i < k; i++)
            sb.append(st);
        return sb.toString();
    }

    /**
     * Prints an array of strings to a single string, making sure that all
     * strings are aligned in {@code spaces} blocks.
     * @param arr Array of strings.
     * @param spaces Width of the column.
     * @return Array in single string, the output is similar to format
     * {@code %5s %5s %5s ...}.
     */
    public static String toStringSpaced(String[] arr, int spaces) {
        final String printf = "%" + spaces + "s";
        StringBuilder sb = new StringBuilder(arr.length * 12 + 2);
        for (String st : arr)
            sb.append(String.format(printf, st));
        return sb.toString();
    }

    public static String toString(String[] arr, String delimiter) {
        if(arr.length == 0) return "";
        StringBuilder sb = new StringBuilder(arr.length * 12 + 2);
        for (String st : arr)
            sb.append(st).append(delimiter);
        sb.delete(sb.length() - delimiter.length(), sb.length());
        return sb.toString();
    }

    public static String toStringFormatted(double[] arr, String format) {
        StringBuilder sb = new StringBuilder(arr.length * 12 + 2);
        for (double val : arr)
            sb.append(String.format(format, val));
        return sb.toString();
    }

    public static String toStringFormatted(String[] arr, String format) {
        StringBuilder sb = new StringBuilder(arr.length * 12 + 2);
        for (String val : arr)
            sb.append(String.format(format, val));
        return sb.toString();
    }

    public static String toStringFormatted(int[] arr, String format) {
        StringBuilder sb = new StringBuilder(arr.length * 12 + 2);
        for (int val : arr)
            sb.append(String.format(format, val));
        return sb.toString();
    }

    public static String toFixedWidth(String string, int length) {
        if (string.length() == length) return string;
        if (string.length() > length) return string.substring(0, length);

        StringBuilder sb = new StringBuilder(length);
        sb.append(string);
        for (int i = sb.length(); i < length; i++) sb.append(' ');
        return sb.toString();
    }
    
    public static String[] appendToAll(String[] arr, String appedent) {
        final String[] retval = new String[arr.length];
        for(int i = 0; i < arr.length; i++)
            retval[i] = arr[i] + appedent;
        return retval;
    }
}
