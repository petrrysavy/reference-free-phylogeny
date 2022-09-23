package rysavpe1.reads.bio;

import java.util.Arrays;

/**
 * Generic utility class.
 *
 * @author Petr Ryšavý
 */
public final class Nucleotides {

    /** Those symbols represent nucleotides. */
    public static final char[] NUCLEOTIDES = new char[]{'A', 'T', 'C', 'G'};
    public static final char[] NUCLEOTIDES_NOT_A = new char[]{'T', 'C', 'G'};
    public static final char[] NUCLEOTIDES_NOT_T = new char[]{'A', 'C', 'G'};
    public static final char[] NUCLEOTIDES_NOT_C = new char[]{'A', 'T', 'G'};
    public static final char[] NUCLEOTIDES_NOT_G = new char[]{'A', 'T', 'C'};
    public static final char[] NUCLEOTIDES_A_FIRST = new char[]{'A', 'T', 'C', 'G'};
    public static final char[] NUCLEOTIDES_T_FIRST = new char[]{'T', 'A', 'C', 'G'};
    public static final char[] NUCLEOTIDES_C_FIRST = new char[]{'C', 'A', 'T', 'G'};
    public static final char[] NUCLEOTIDES_G_FIRST = new char[]{'G', 'C', 'A', 'T'};
    /** Count of the nucleotides. */
    public static final int NUCLEOTIDES_COUNT = 4;
    /** Square of the nucleotide count. */
    public static final int NUCLEOTIDES_COUNT_SQ = 16;
    public static final int NUCLEOTIDES_COUNT_CUBE = 64;
    public static final int NUCLEOTIDES_COUNT_POW4 = 256;
    public static final int NUCLEOTIDES_COUNT_POW5 = 1024;

    /** Do not let anybody to instantiate the class. */
    private Nucleotides() {
    }

    /**
     * For a DNA sequence returns its complement.
     * @param sequence The sequence, for example {@code ATCCG}.
     * @return The complement, for example {@code TAGGC}.
     */
    public static char[] complementaryCopy(char[] sequence) {
        final char[] copy = Arrays.copyOf(sequence, sequence.length);
        makeComplementary(copy);
        return copy;
    }

    /**
     * For a DNA sequence returns its complement.
     * @param sequence The sequence, for example {@code ATCCG}.
     * @return The complement, for example {@code TAGGC}.
     */
    public static char[] complementaryCopySafe(char[] sequence) {
        final char[] copy = Arrays.copyOf(sequence, sequence.length);
        makeComplementarySafe(copy);
        return copy;
    }

    /**
     * Changes a DNA sequence into its complement.
     * @param sequence The sequence, for example.
     */
    public static void makeComplementary(char[] sequence) {
        for (int i = 0; i < sequence.length; i++)
            sequence[i] = complement(sequence[i]);
    }

    /**
     * Changes a DNA sequence into its complement.
     * @param sequence The sequence, for example.
     */
    public static void makeComplementarySafe(char[] sequence) {
        for (int i = 0; i < sequence.length; i++)
            sequence[i] = complementSafe(sequence[i]);
    }

    /**
     * Returns a complement of the nucleotide. The pairs are A/T and C/G.
     * @param ch A nucleotide.
     * @return Its complementary nucleotide.
     */
    public static char complement(char ch) {
        switch (ch) {
            case 'A':
                return 'T';
            case 'T':
                return 'A';
            case 'C':
                return 'G';
            case 'G':
                return 'C';
            default:
                throw new IllegalArgumentException("Unknown nucleotide : " + ch);
        }
    }

    /**
     * Returns a complement of the nucleotide. The pairs are A/T and C/G.
     * Otherwise the shortcut notation is used.
     * @param ch A nucleotide.
     * @return Its complementary nucleotide.
     */
    public static char complementSafe(char ch) {
        switch (ch) {
            case 'A':
                return 'T';
            case 'T':
                return 'A';
            case 'C':
                return 'G';
            case 'G':
                return 'C';
            case 'N':
                return 'N';
            case 'Y':
                return 'R';
            case 'R':
                return 'Y';
            case 'M':
                return 'K';
            case 'K':
                return 'M';
            case 'S':
                return 'S';
            case 'W':
                return 'W';
            case 'B':
                return 'V';
            case 'D':
                return 'H';
            case 'H':
                return 'D';
            case 'V':
                return 'B';
            default:
                throw new IllegalArgumentException("Unknown nucleotide : " + ch);
        }
    }

    /**
     * This method returns an integer index for a nucleotide.
     * @param ch The nucleotide.
     * @return Unique integer characterizing the nucleotide.
     */
    public static int toInteger(char ch) {
        switch (ch) {
            case 'A':
                return 0;
            case 'T':
                return 1;
            case 'C':
                return 2;
            case 'G':
                return 3;
            default:
                throw new IllegalArgumentException("Unknown nucleotide : " + ch);
        }
    }

    public static int[] toInteger(char[] chars) {
        final int[] integers = new int[chars.length];
        for (int i = 0; i < chars.length; i++)
            integers[i] = toInteger(chars[i]);
        return integers;
    }
    public static char fromInteger(int code) {
        switch (code) {
            case 0:
                return 'A';
            case 1:
                return 'T';
            case 2:
                return 'C';
            case 3:
                return 'G';
            default:
                throw new IllegalArgumentException("Unknown nucleotide : " + code);
        }
    }

    public static char[] fromInteger(int[] ints) {
        final char[] chars = new char[ints.length];
        for (int i = 0; i < ints.length; i++)
            chars[i] = fromInteger(ints[i]);
        return chars;
    }

    /**
     * Gets list of nucleotides.
     * @param ch The nucleotide that should be first in the list.
     * @return List of all nucleotides with {@code ch} first.
     */
    public static char[] listNucleotidesPreference(char ch) {
        switch (ch) {
            case 'A':
                return NUCLEOTIDES_A_FIRST;
            case 'T':
                return NUCLEOTIDES_T_FIRST;
            case 'C':
                return NUCLEOTIDES_C_FIRST;
            case 'G':
                return NUCLEOTIDES_G_FIRST;
            default:
                throw new IllegalArgumentException("Unknown nucleotide : " + ch);
        }

    }

}
