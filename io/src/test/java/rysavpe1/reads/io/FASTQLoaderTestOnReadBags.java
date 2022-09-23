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
import static org.hamcrest.CoreMatchers.is;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import rysavpe1.reads.model.ReadBag;
import rysavpe1.reads.utils.CollectionUtils;

/**
 *
 * @author Petr Ryšavý
 */
public class FASTQLoaderTestOnReadBags {

    private static final ReadBag EXPECTED_BAG = ReadBag.fromString(
            "AAAAAAAAGCAGGCTAGGCTAAGCTATGATGTTCCTTAGATTAGGTGTATTAAATCCATTTTCAACTTACAATATTTTCAACTTACGATGAGTTTATCAG",
            "TCGGCTCACTGCAACCTCTGCCTCCTGGGTTCAAGTGATTCTCCTGCCTCAGCCTCCCGAATAGCTAGGACTACAAGCGCCTGCTACCACGCCCAGCTAA",
            "AGCAGTCCTGCCTCAGCCTCCGGAGTAGCTGGGACCACAGGTTCATGCCACCATGGCCAGCCAACTTTTGCATGTTTTGTAGAGATGGGGTCTCACAGTG",
            "ACTGGCGTTCACCCCTCAGACACACAGGTGGCAGCAAAGTTTTATTGTAAAATAAGAGATCGATATAAAAATGGGATATAAAAAGGGAGAAGGAGGGGAA",
            "GATTACGAGACTAATACACACTAATACTCTGAGGTGCTCAGTAAACATATTTGCATGGGGTGTGGCCACCATCTTGATTTGAATTCCCGTTGTCCCAGCC"
    );
    private static final String CONTENTS = "@seq1-2160\n"
            + "AAAAAAAAGCAGGCTAGGCTAAGCTATGATGTTCCTTAGATTAGGTGTATTAAATCCATTTTCAACTTACAATATTTTCAACTTACGATGAGTTTATCAG\n"
            + "+\n"
            + "CBCCCGFGGG1>GGGGGG1GGGGGGGEDGGGGGGGGGGEGGEGGGGGGGGGFFGGGGGGGGG:>GGGGGGGGGGGGGEGGGGGGGGG/0GGGGGG0C00B\n"
            + "@seq1-2159\n"
            + "TCGGCTCACTGCAACCTCTGCCTCCTGGGTTCAAGTGATTCTCCTGCCTCAGCCTCCCGAATAGCTAGGACTACAAGCGCCTGCTACCACGCCCAGCTAA\n"
            + "+\n"
            + "ABCCCGC/1GGGGG/GEGEBGGFGGGGGEG@GGGGGGGGGGFGGGGGGCGGGGGGGGGGGG1GGGG0>GGGGGAGGGGGG@/1GGGGEGGGGGGGGCGGG\n"
            + "@seq1-2158\n"
            + "AGCAGTCCTGCCTCAGCCTCCGGAGTAGCTGGGACCACAGGTTCATGCCACCATGGCCAGCCAACTTTTGCATGTTTTGTAGAGATGGGGTCTCACAGTG\n"
            + "+\n"
            + "CCCCCGG@GG>=GGGGG1GGDGG>GG1GEGG1GGGGGGGGGGGGGGGGGFGG=G1GG<G/GG1::GG1GGGGBGGGGGGGGGGGGGGEGDEGGE0GGGGG\n"
            + "@seq1-2157\n"
            + "ACTGGCGTTCACCCCTCAGACACACAGGTGGCAGCAAAGTTTTATTGTAAAATAAGAGATCGATATAAAAATGGGATATAAAAAGGGAGAAGGAGGGGAA\n"
            + "+\n"
            + "CBBCBEGGGGGGGGEGGGGGGGGGGGGGGGGGGGFGGGGGFGGGEGGGG1GGGGFCDGG1=FGGGGGGFGGGGGGCGGGGGGFGFGBGGGG.DGGGGGFG\n"
            + "@seq1-2156\n"
            + "GATTACGAGACTAATACACACTAATACTCTGAGGTGCTCAGTAAACATATTTGCATGGGGTGTGGCCACCATCTTGATTTGAATTCCCGTTGTCCCAGCC\n"
            + "+\n"
            + "CCBBCGGGGGGGGGGGGGGFGGGGGGGGGGGGEGGGGFGGGGGGGGCGGGGGEGF1GGGG@GGFGGGGGGGGGGG1GGGGGGGGGCGGGEGGGCDGGGGG\n";

    private static final Path PATH = Paths.get("..", "testfiles", "test.fq");
    private static final Path OUTPUT_PATH = Paths.get("..", "testfiles", "output", "test.fq");

    private Loader loader;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void construct() {
        loader = new FASTQLoader(null);
    }

    @Test
    public void testLoadReadBagBufferedReader() throws IOException {
        assertThat(loader.loadReadBag(new BufferedReader(new StringReader((CONTENTS)))),
                is(equalTo(EXPECTED_BAG)));
    }

    @Test
    public void testLoadReadBagPath() throws IOException {
        assertThat(loader.loadReadBag(PATH),
                is(equalTo(EXPECTED_BAG)));
    }

    @Test
    public void testLoadReadBagPaths() throws IOException {
        ReadBag[] bags = loader.loadReadBags(CollectionUtils.asList(PATH, PATH, PATH));
        assertThat(bags.length, is(3));
        for (ReadBag rb : bags)
            assertThat(rb, is(equalTo(EXPECTED_BAG)));
    }

    @Test
    public void testLoadReadBagByDirectory() throws IOException {
        ReadBag[] bags = loader.loadReadBags(PATH.getParent(), CollectionUtils.asList(PATH.getFileName().toString()));
        assertThat(bags.length, is(1));
        assertThat(bags[0], is(equalTo(EXPECTED_BAG)));
    }

    @Test
    public void testWriteReadBag() throws IOException {
        StringWriter wr = new StringWriter();
        BufferedWriter bw = new BufferedWriter(wr);
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Not supported yet.");
        loader.writeReadBag(EXPECTED_BAG, bw);
    }

    @Test
    public void testWriteReadBagToFile() throws IOException {
        Files.deleteIfExists(OUTPUT_PATH);
        thrown.expect(UnsupportedOperationException.class);
        thrown.expectMessage("Not supported yet.");
        loader.writeReadBag(EXPECTED_BAG, OUTPUT_PATH);
    }
}
