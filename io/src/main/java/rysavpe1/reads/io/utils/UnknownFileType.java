package rysavpe1.reads.io.utils;

import java.io.IOException;

/**
 * This exception is used to signalize that the program does not know the file
 * type.
 *
 * @author Petr Ryšavý
 */
public class UnknownFileType extends IOException {

    public UnknownFileType() {
    }

    public UnknownFileType(String message) {
        super(message);
    }
}
