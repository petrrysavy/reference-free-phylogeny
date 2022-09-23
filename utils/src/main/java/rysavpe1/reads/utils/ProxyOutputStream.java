package rysavpe1.reads.utils;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A stream object that duplicates the output to two selected streams.
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class ProxyOutputStream extends OutputStream {
    
    private final OutputStream first;
    private final OutputStream second;
    
    public ProxyOutputStream(OutputStream first) {
        this(first, System.out);
    }
    
    public ProxyOutputStream(OutputStream first, OutputStream second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public void write(int b) throws IOException {
        first.write(b);
        second.write(b);
    }

    @Override
    public void close() throws IOException {
        first.close();
        second.close();
        super.close();
    }

    @Override
    public void flush() throws IOException {
        first.flush();
        second.flush();
        super.flush();
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        first.write(b, off, len);
        second.write(b, off, len);
    }

    @Override
    public void write(byte[] b) throws IOException {
        first.write(b);
        second.write(b);
    }

}
