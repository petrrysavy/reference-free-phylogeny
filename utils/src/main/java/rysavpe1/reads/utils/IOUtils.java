package rysavpe1.reads.utils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Utility class for manipulating input and output.
 *
 * @author Petr Ryšavý
 */
public class IOUtils {

    /** Date format used for file timestamps. */
    public static final DateFormat DATE_STAMP_FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    public static final OpenOption[] FILE_WRITE_OPTIONS = new OpenOption[]{
        StandardOpenOption.WRITE, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING
    };

    /** Do not let anybody to instantiate the class. */
    private IOUtils() {
    }

    /**
     * Prints list of lines to a writer.
     * @param list List of strings to print.
     * @param out Target writer.
     * @throws IOException When writing to writer fails.
     */
    public static void printTo(Iterable<String> list, Writer out) throws IOException {
        for (String st : list) {
            out.write(st);
            out.write('\n');
        }
        out.flush();
    }

    /**
     * Checks whether the file can be read. File must not be null, must exist,
     * it must be a regular file and must be readable.
     * @param file File to check.
     */
    public static void checkCanReadFile(Path file) {
        if (file == null)
            throw new IllegalArgumentException("File cannot be null.");
        if (!Files.exists(file))
            throw new IllegalArgumentException("File must exist : " + file);
        if (!Files.isRegularFile(file))
            throw new IllegalArgumentException("Input file must be a regular file.");
        if (!Files.isReadable(file))
            throw new IllegalArgumentException("File is not readable");
    }

    /**
     * Decides whether the file can be read. File must not be null, must exist,
     * it must be a regular file and must be readable.
     * @param file File to check.
     * @return {@code true} if the file can be read from.
     */
    public static boolean canReadFile(Path file) {
        return file != null
                && Files.exists(file)
                && Files.isRegularFile(file)
                && Files.isReadable(file);
    }

    /**
     * Decides whether a directory exists. File must not be null, must exist, it
     * must be a directory and must be readable.
     * @param file File to check.
     * @return {@code true} if the file can be read from.
     */
    public static boolean canReadDirectory(Path file) {
        return file != null
                && Files.exists(file)
                && Files.isDirectory(file)
                && Files.isReadable(file);
    }

    /**
     * Checks whether the file can be written to. File must not be null, must
     * exist, it must be a regular file and must be writable.
     * @param file File to check.
     */
    public static void checkCanWriteFile(Path file) {
        if (file == null)
            throw new IllegalArgumentException("File cannot be null.");
        if (!Files.exists(file))
            throw new IllegalArgumentException("File must exist : " + file);
        if (!Files.isRegularFile(file))
            throw new IllegalArgumentException("Input file must be a regular file.");
        if (!Files.isReadable(file))
            throw new IllegalArgumentException("File is not readable");
    }

    /**
     * Decides whether the file can be written to. File must not be null, must
     * exist, it must be a regular file and must be writable.
     * @param file File to check.
     * @return {@code true} if the file can be write to.
     */
    public static boolean canWriteFile(Path file) {
        if (file == null)
            return false;
        if (!Files.exists(file))
            return false;
        if (!Files.isRegularFile(file))
            return false;
        return Files.isWritable(file);
    }

    /**
     * Writes set of strings to a file. Assumes that the file should be
     * truncated if existing and created if it does not exist.
     * @param file Target file.
     * @param contents The lines that will be stored in the file.
     * @throws IOException When IO fails.
     */
    public static void write(Path file, List<String> contents) throws IOException {
        Files.write(
                file,
                contents,
                FILE_WRITE_OPTIONS
        );
    }

    /**
     * Writes set of strings to a file.Assumes that the file should be truncated
     * if existing and created if it does not exist.
     * @param <T> Type of the objects.
     * @param file Target file.
     * @param stream Stream of objects that should be printed to the file. One
     * at a line.
     * @throws IOException When IO fails.
     */
    public static <T> void write(Path file, Stream<T> stream) throws IOException {
        Files.write(
                file,
                (Iterable<String>) stream.map(Object::toString)::iterator,
                FILE_WRITE_OPTIONS
        );
    }

    /**
     * Writes set of strings to a file. Assumes that the file should be
     * truncated if existing and created if it does not exist.
     * @param file Target file.
     * @param stream The list of numbers to store to a file.
     * @throws IOException When IO fails.
     */
    public static void write(Path file, IntStream stream) throws IOException {
        write(file, stream.mapToObj(i -> Integer.toString(i)));
    }

    /**
     * Writes an array to a file, one element per line. Assumes that the file
     * should be truncated if existing and created if it does not exist.
     * @param <T> Type of the objects.
     * @param file Target file.
     * @param values Array of values to write.
     * @throws IOException When IO fails.
     */
    public static <T> void write(Path file, T[] values) throws IOException {
        write(file, Arrays.stream(values));
    }

    /**
     * Creates a new buffered writer to the given file with standard file write
     * options.
     * @param file Target file.
     * @return The writer to the file.
     * @throws IOException When IO fails.
     */
    public static BufferedWriter newBufferedWriter(Path file) throws IOException {
        return Files.newBufferedWriter(file, FILE_WRITE_OPTIONS);
    }

    /**
     * Creates a new buffered writer to the given file with standard file write
     * options.
     * @param file Target file.
     * @return The writer to the file.
     * @throws IOException When IO fails.
     */
    public static BufferedWriter newBufferedWriterShadowInSout(Path file) throws IOException {
        return new BufferedWriter(new OutputStreamWriter(
                new ProxyOutputStream(Files.newOutputStream(file, FILE_WRITE_OPTIONS))));
    }

    /**
     * This method obtains extension from file name. Extension is anything after
     * the first dot in the file name.
     * @param fileName Name of a file. For example {@code seq.fa}.
     * @return Extension, for example {@code fa}.
     */
    public static String getExtension(String fileName) {
        fileName = fileName.toLowerCase();
        final int dot = fileName.indexOf('.');
        return dot == -1 ? fileName : fileName.substring(dot + 1);
    }

    /**
     * Gets a name for a file that should contain a timestamp.
     * @param file Filename.
     * @param extension Extension.
     * @return A name for the file.
     */
    public static String timeStampedFileName(String file, String extension) {
        StringBuilder sb = new StringBuilder(file.length() + extension.length() + 16);
        sb.append(file);
        sb.append('-');
        sb.append(DATE_STAMP_FORMAT.format(new Date()));
        sb.append('.');
        sb.append(extension);
        return sb.toString();
    }

    /**
     * This method resolves file names with respect to a directory.
     * @param directory The directory.
     * @param files Names of files.
     * @return Paths to files within the directory.
     */
    public static List<Path> resolve(Path directory, Iterable<String> files) {
        List<Path> resolved = new ArrayList<>();
        for (String file : files)
            resolved.add(directory.resolve(file));
        return resolved;
    }

    public static void saveToFileIfFileDoesNotExist(Path file, Callable<List<String>> callable) throws IOException, Exception {
        if (!canReadFile(file)) {
            List<String> outputs = callable.call();
            write(file, outputs);
        }
    }
}
