package rysavpe1.reads.io.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import rysavpe1.reads.utils.IOUtils;

/**
 *
 * @author petr
 */
public class GZIPFileOpener implements FileOpener {

    public static final GZIPFileOpener INSTANCE = new GZIPFileOpener();

    private GZIPFileOpener() {
    }

    @Override
    public BufferedReader openForReading(Path file) throws IOException {
        return new BufferedReader(new InputStreamReader(new GZIPInputStream(Files.newInputStream(file))));
    }

    @Override
    public BufferedWriter openForWriting(Path file) throws IOException {
        return new BufferedWriter(new OutputStreamWriter(new GZIPOutputStream(Files.newOutputStream(file, IOUtils.FILE_WRITE_OPTIONS))));
    }

}
