package rysavpe1.reads.io.utils;

import java.io.IOException;

/**
 * This exception is used to signalize that the file does not match expectations
 * and is probably formatted in a wrong way.
 *
 * @author Petr Ryšavý
 */
public class MismatchedFileException extends IOException {

    public MismatchedFileException() {
    }

    public MismatchedFileException(String message) {
        super(message);
    }
}
