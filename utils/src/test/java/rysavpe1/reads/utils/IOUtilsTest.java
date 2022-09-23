package rysavpe1.reads.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class IOUtilsTest {
    
    public IOUtilsTest() {
    }

    @Test
    public void testTimeStampedFileName() {
        final Date now = new Date();
        final String time = new SimpleDateFormat("yyyyMMddHHmmss").format(now);
        assertThat(IOUtils.timeStampedFileName("ABC", "DEF"),
                is(equalTo("ABC-"+time+".DEF")));
    }
    
}
