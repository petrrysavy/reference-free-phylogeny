package rysavpe1.reads.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Before;
import org.junit.Test;
import rysavpe1.reads.model.Sequence;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import rysavpe1.reads.utils.CollectionUtils;

/**
 *
 * @author Petr Ryšavý
 */
public class FASTQLoaderTestOnSequences {

    private static final String EXPECTED_DESCRIPTION = "seq1-2160";
    private static final String EXPECTED_SEQUENCE = "AAAAAAAAGCAGGCTAGGCTAAGCTATGATGTTCCTTAGATTAGGTGTATTAAATCCATTTTCAACTTACAATATTTTCAACTTACGATGAGTTTATCAG";
    private static final String CONTENTS = "@seq1-2160\n"
            + "AAAAAAAAGCAGGCTAGGCTAAGCTATGATGTTCCTTAGATTAGGTGTATTAAATCCATTTTCAACTTACAATATTTTCAACTTACGATGAGTTTATCAG\n"
            + "+\n"
            + "CBCCCGFGGG1>GGGGGG1GGGGGGGEDGGGGGGGGGGEGGEGGGGGGGGGFFGGGGGGGGG:>GGGGGGGGGGGGGEGGGGGGGGG/0GGGGGG0C00B";
    private static final Path PATH = Paths.get("..", "testfiles", "test.fq");
    private static final Path OUTPUT_PATH = Paths.get("..", "testfiles", "output", "test.fq");
    private static final Sequence SEQUENCE = new Sequence(EXPECTED_SEQUENCE.toCharArray(), EXPECTED_DESCRIPTION);

    private Loader loader;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void construct() {
        loader = new FASTQLoader(null);
    }

    private void checkSequence(Sequence s) {
        assertThat(s.getDescription(), is(equalTo(EXPECTED_DESCRIPTION)));
        assertThat(new String(s.getSequence()), is(equalTo(EXPECTED_SEQUENCE)));
    }

    @Test
    public void testLoadSequenceBufferedReader() throws IOException {
        checkSequence(loader.loadSequence(new BufferedReader(new StringReader(CONTENTS))));
    }

    @Test
    public void testLoadSequencePath() throws IOException {
        checkSequence(loader.loadSequence(PATH));
    }

    @Test
    public void testLoadSequencePaths() throws IOException {
        Sequence[] sequences = loader.loadSequences(CollectionUtils.asList(PATH, PATH, PATH));
        assertThat(sequences.length, is(3));
        for (Sequence s : sequences)
            checkSequence(s);
    }

    @Test
    public void testLoadSequenceByDirectory() throws IOException {
        Sequence[] sequences = loader.loadSequences(PATH.getParent(), CollectionUtils.asList(PATH.getFileName().toString()));
        assertThat(sequences.length, is(1));
        checkSequence(sequences[0]);
    }

    @Test
    public void testWriteSequence() throws IOException {
        StringWriter wr = new StringWriter();
        BufferedWriter bw = new BufferedWriter(wr);
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Not supported yet.");
        loader.writeSequence(SEQUENCE, bw);
    }

    @Test
    public void testWriteSequenceToFile() throws IOException {
        Files.deleteIfExists(OUTPUT_PATH);
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Not supported yet.");
        loader.writeSequence(SEQUENCE, OUTPUT_PATH);
    }

}
