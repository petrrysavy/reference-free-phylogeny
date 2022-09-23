package rysavpe1.reads.settings;



import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Static objects of the project with settings.
 *
 * @author Petr Ryšavý
 */
public final class Settings {

    /** The charset used for loading input files. */
    public static final Charset CHARSET = Charset.forName("UTF-8");

    /** Logger. */
    public static final Logger LOGGER = Logger.getGlobal();

    /** Random number generator. */
    public static final Random RANDOM = new Random(42);
    
    /** Date format used. */
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    
    /** Do not let anybody to instantiate the class. */
    private Settings() {
    }
}
