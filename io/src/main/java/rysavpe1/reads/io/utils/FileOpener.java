package rysavpe1.reads.io.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;

/**
 *
 * @author petr
 */
public interface FileOpener {

    public BufferedReader openForReading(Path file) throws IOException;

    public BufferedWriter openForWriting(Path file) throws IOException;
}
