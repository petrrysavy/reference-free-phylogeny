package rysavpe1.reads.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A thread that reads from a reader until there is nothing left. This may be
 * handy because Java sometimes freezes if a subprocess produces some output and
 * this output is not read from the system stream. Once buffer gets full, the
 * program is prevented from printing another output and may freeze.
 *
 * @author Petr Ryšavý
 */
public class FlushReaderThread extends Thread {

    /** We will read through a buffer. */
    private final BufferedReader in;

    /**
     * Crates a new instance of this thread.
     * @param in This reader will be read from until there is nothing left. At
     * that point the thread ends.
     */
    public FlushReaderThread(Reader in) {
        this.in = new BufferedReader(in);
    }

    @Override
    public void run() {
        try {
            String line = null;
            while ((line = in.readLine()) != null)
                System.err.println(line);
        } catch (IOException ex) {
            Logger.getLogger(FlushReaderThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void interrupt() {
        try {
            in.close();
        } catch (IOException ex) {
            Logger.getLogger(FlushReaderThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        super.interrupt();
    }

}
