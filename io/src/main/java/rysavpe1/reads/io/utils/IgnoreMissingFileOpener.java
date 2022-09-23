package rysavpe1.reads.io.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Path;
import rysavpe1.reads.utils.IOUtils;

/**
 *
 * @author petr
 */
public class IgnoreMissingFileOpener extends DefaultFileOpener {

    public static final IgnoreMissingFileOpener INSTANCE = new IgnoreMissingFileOpener();

    private IgnoreMissingFileOpener() {
    }

    @Override
    public BufferedReader openForReading(Path file) throws IOException {
        if(! IOUtils.canReadFile(file))
            return new BufferedReader(new StringReader(""));
        return super.openForReading(file);
    }

    @Override
    public BufferedWriter openForWriting(Path file) throws IOException {
        return super.openForWriting(file);
    }

}
