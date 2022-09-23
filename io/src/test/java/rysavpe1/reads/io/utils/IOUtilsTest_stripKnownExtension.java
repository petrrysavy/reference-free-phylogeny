package rysavpe1.reads.io.utils;

import java.util.Arrays;
import java.util.Collection;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 *
 * @author petr
 */
@RunWith(Parameterized.class)
public class IOUtilsTest_stripKnownExtension {

    private final String filename;
    private final String expected;

    public IOUtilsTest_stripKnownExtension(String filename, String expected) {
        this.filename = filename;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {"abcd.fa", "abcd"},
            {"abcd.fasta", "abcd"},
            {"abcd.fq", "abcd"},
            {"abcd.fastq", "abcd"},
            {"abcd.abc", "abcd.abc"},
        });
    }

    @Test
    public void testStripKnownExtension() {
        assertThat(Utils.stripKnownExtension(filename), is(equalTo(expected)));
    }

}
