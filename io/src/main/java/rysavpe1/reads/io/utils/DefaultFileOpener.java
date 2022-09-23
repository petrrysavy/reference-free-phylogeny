package rysavpe1.reads.io.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import rysavpe1.reads.utils.IOUtils;

/**
 *
 * @author petr
 */
public class DefaultFileOpener implements FileOpener {

    public static final DefaultFileOpener INSTANCE = new DefaultFileOpener();

    protected DefaultFileOpener() {
    }

    @Override
    public BufferedReader openForReading(Path file) throws IOException {
        return Files.newBufferedReader(file);
    }

    @Override
    public BufferedWriter openForWriting(Path file) throws IOException {
        return Files.newBufferedWriter(file, IOUtils.FILE_WRITE_OPTIONS);
    }

}
