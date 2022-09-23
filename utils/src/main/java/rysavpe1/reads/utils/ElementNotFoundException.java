package rysavpe1.reads.utils;

/**
 * Exception marking that some element was not found.
 *
 * @author Petr Ryšavý
 */
public class ElementNotFoundException extends RuntimeException {

    /**
     * Creates a new instance of <code>ElementNotFoundException</code> without a
     * detailed message.
     */
    public ElementNotFoundException() {
    }

    /**
     * Creates a new instance of <code>ElementNotFoundException</code> with a
     * message.
     *
     * @param msg Message.
     */
    public ElementNotFoundException(String msg) {
        super(msg);
    }
}
