package rysavpe1.reads.io.utils;

/**
 * Generic utility class.
 *
 * @author Petr Ryšavý
 */
public final class Utils {

    /** Do not let anybody to instantiate the class. */
    private Utils() {
    }

    /**
     * Removes extension that is known.
     * @param filename Name of file, possible with extension.
     * @return {@code filename}, however without extension.
     */
    public static String stripKnownExtension(String filename) {
        if (filename.endsWith(".fa") || filename.endsWith(".fq"))
            return filename.substring(0, filename.length() - 3);
        if (filename.endsWith(".fasta") || filename.endsWith(".fastq"))
            return filename.substring(0, filename.length() - 6);
        return filename;
    }
}
