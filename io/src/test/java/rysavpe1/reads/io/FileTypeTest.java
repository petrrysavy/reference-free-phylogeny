package rysavpe1.reads.io;

import java.nio.file.Path;
import java.nio.file.Paths;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 *
 * @author petr
 */
public class FileTypeTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();
    /**
     * Test of getComressed method, of class FileType.
     */
    @Test
    public void testGetComressed() {
        assertThat(FileType.FASTA.getComressed(), is(FileType.FASTAGZ));
        assertThat(FileType.FASTQ.getComressed(), is(FileType.FASTQGZ));
        assertThat(FileType.FASTAGZ.getComressed(), is(FileType.FASTAGZ));
        assertThat(FileType.FASTQGZ.getComressed(), is(FileType.FASTQGZ));
    }

    /**
     * Test of byExtension method, of class FileType.
     */
    @Test
    public void testByExtension() {
        assertThat(FileType.byExtension("fa"), is(FileType.FASTA));
        assertThat(FileType.byExtension("FA"), is(FileType.FASTA));
        assertThat(FileType.byExtension("Fa"), is(FileType.FASTA));
        assertThat(FileType.byExtension("fasta"), is(FileType.FASTA));
        assertThat(FileType.byExtension("fq"), is(FileType.FASTQ));
        assertThat(FileType.byExtension("fastq"), is(FileType.FASTQ));
        assertThat(FileType.byExtension("fa.gz"), is(FileType.FASTAGZ));
        assertThat(FileType.byExtension("fasta.gz"), is(FileType.FASTAGZ));
        assertThat(FileType.byExtension("fq.gz"), is(FileType.FASTQGZ));
        assertThat(FileType.byExtension("fastq.gz"), is(FileType.FASTQGZ));
        
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Unknown file extension : abc");
        FileType.byExtension("abc");
    }

    /**
     * Test of byExtensionSafe method, of class FileType.
     */
    @Test
    public void testByExtensionSafe() {
        assertThat(FileType.byExtensionSafe("fa"), is(FileType.FASTA));
        assertThat(FileType.byExtensionSafe("FA"), is(FileType.FASTA));
        assertThat(FileType.byExtensionSafe("Fa"), is(FileType.FASTA));
        assertThat(FileType.byExtensionSafe("fasta"), is(FileType.FASTA));
        assertThat(FileType.byExtensionSafe("fq"), is(FileType.FASTQ));
        assertThat(FileType.byExtensionSafe("fastq"), is(FileType.FASTQ));
        assertThat(FileType.byExtensionSafe("fa.gz"), is(FileType.FASTAGZ));
        assertThat(FileType.byExtensionSafe("fasta.gz"), is(FileType.FASTAGZ));
        assertThat(FileType.byExtensionSafe("fq.gz"), is(FileType.FASTQGZ));
        assertThat(FileType.byExtensionSafe("fastq.gz"), is(FileType.FASTQGZ));
        assertThat(FileType.byExtensionSafe("abc"), is(nullValue()));
    }

    /**
     * Test of guessByFirstLine method, of class FileType.
     */
    @Test
    public void testGuessByFirstLine() {
        assertThat(FileType.guessByFirstLine(">desc"), is(FileType.FASTA));
        assertThat(FileType.guessByFirstLine("@desc"), is(FileType.FASTQ));
        assertThat(FileType.guessByFirstLine(""), is(nullValue()));
        assertThat(FileType.guessByFirstLine("an unknown"), is(nullValue()));
    }

    /**
     * Test of guessFileType method, of class FileType.
     */
    @Test
    public void testGuessFileType() throws Exception {
        assertThat(FileType.guessFileType(Paths.get("..", "testfiles", "AF389119")), is(FileType.FASTA));
        assertThat(FileType.guessFileType(Paths.get("..", "testfiles", "test.fq")), is(FileType.FASTQ));
        assertThat(FileType.guessFileType(Paths.get("..", "testfiles", "test.fq.gz")), is(FileType.FASTQGZ));
        assertThat(FileType.guessFileType(Paths.get("..", "testfiles", "test.fq.unknownextension")), is(FileType.FASTQ));
        assertThat(FileType.guessFileType(Paths.get("..", "testfiles", "test.fq.gz.unknownextension")), is(FileType.FASTQGZ));
    }

}
