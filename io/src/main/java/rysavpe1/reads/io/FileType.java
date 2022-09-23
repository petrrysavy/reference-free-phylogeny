package rysavpe1.reads.io;

import rysavpe1.reads.io.utils.UnknownFileType;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Set;
import java.util.zip.GZIPInputStream;
import rysavpe1.reads.io.utils.GZIPFileOpener;
import rysavpe1.reads.io.utils.SequenceChecker;
import rysavpe1.reads.settings.Settings;
import rysavpe1.reads.utils.CollectionUtils;
import rysavpe1.reads.utils.IOUtils;

/**
 *
 * @author petr
 */
public enum FileType {
    FASTA(CollectionUtils.asSet("fa", "fasta"), new FASTALoader(null)),
    FASTQ(CollectionUtils.asSet("fq", "fastq"), new FASTQLoader(null)),
    FASTAGZ(CollectionUtils.asSet("fa.gz", "fasta.gz"), new FASTALoader(GZIPFileOpener.INSTANCE)),
    FASTQGZ(CollectionUtils.asSet("fq.gz", "fastq.gz"), new FASTQLoader(GZIPFileOpener.INSTANCE));

    private final Set<String> extensions;
    public final Loader loader;

    private FileType(Set<String> extensions, Loader loader) {
        this.extensions = extensions;
        this.loader = loader;
    }

    public FileType getComressed() {
        switch (this) {
            case FASTA:
                return FASTAGZ;
            case FASTQ:
                return FASTQGZ;
            default:
                return this;
        }
    }

    public static void setSequenceChecker(SequenceChecker checker) {
        for (FileType type : FileType.values())
            type.loader.setSequenceChecker(checker);
    }

    public static FileType byExtension(String extension) {
        FileType t = byExtensionSafe(extension);
        if (t == null)
            throw new IllegalArgumentException("Unknown file extension : " + extension);
        return t;
    }

    public static FileType byExtensionSafe(String extension) {
        extension = extension.trim().toLowerCase();
        for (FileType type : FileType.values())
            if (type.extensions.contains(extension))
                return type;
        return null;
    }

    public static FileType guessByFirstLine(String line) {
        if (line == null || line.isEmpty())
            return null;
        if (line.startsWith(">"))
            return FASTA;
        if (line.startsWith("@"))
            return FASTQ;
        return null;
    }

    public static FileType guessFileType(Path file) throws IOException {
        FileType f = byExtensionSafe(IOUtils.getExtension(file.getFileName().toString()));

        if (f != null) return f;

        if (!Files.exists(file))
            throw new FileNotFoundException(file.toString());

        try ( // try to guess by the first line
                BufferedReader br = Files.newBufferedReader(file, Settings.CHARSET)) {
            f = guessByFirstLine(br.readLine());
        } catch (IOException mie) {
            // for example MalformedInputException may happen for gzipped files, ignore the exception
        }

        if (f != null) return f;

        // try tu unzip the stream, maybe it is gzipped
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new GZIPInputStream(
                Files.newInputStream(file, StandardOpenOption.READ))))) {
            f = guessByFirstLine(br.readLine());
        } catch (IOException ioe) { // f is null in this case
        }

        if (f != null) return f.getComressed();
        throw new UnknownFileType("Unknown file : " + file);
    }
}
