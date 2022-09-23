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
import rysavpe1.reads.utils.CollectionUtils;
import rysavpe1.reads.utils.StringUtils;

/**
 *
 * @author Petr Ryšavý
 */
public class FASTALoaderTestOnSequences {

    private static final String EXPECTED_DESCRIPTION = "ENA|LC002520|LC002520.1 Zika virus genomic RNA, complete genome, strain: MR766-NIID.";
    private static final String EXPECTED_SEQUENCE = "AGTTGTTGATCTGTGTGAGTCAGACTGCGACAGTTCGAGTCTGAAGCGAGAGCTAACAAC"
            + "AGTATCAACAGGTTTAATTTGGATTTGGAAACGAGAGTTTCTGGTCATGAAAAACCCAAA"
            + "GAAGAAATCCGGAGGATTCCGGATTGTCAATATGCTAAAACGCGGAGTAGCCCGTGTAAA"
            + "CCCCTTGGGAGGTTTGAAGAGGTTGCCAGCCGGACTTCTGCTGGGTCATGGACCCATCAG"
            + "AATGGTTTTGGCGATACTAGCCTTTTTGAGATTTACAGCAATCAAGCCATCACTGGGCCT"
            + "TATCAACAGATGGGGTTCCGTGGGGAAAAAAGAGGCTATGGAAATAATAAAGAAGTTCAA"
            + "GAAAGATCTTGCTGCCATGTTGAGAATAATCAATGCTAGGAAAGAGAGGAAGAGACGTGG"
            + "CGCAGACACCAGCATCGGAATCATTGGCCTCCTGCTGACTACAGCCATGGCAGCAGAGAT"
            + "CACTAGACGCGGGAGTGCATACTACATGTACTTGGATAGGAGCGATGCCGGGAAGGCCAT"
            + "TTCGTTTGCTACCACATTGGGAGTGAACAAGTGCCACGTACAGATCATGGACCTCGGGCA"
            + "CATGTGTGACGCCACCATGAGTTATGAGTGCCCTATGCTGGATGAGGGAGTGGAACCAGA"
            + "TGATGTCGATTGCTGGTGCAACACGACATCAACTTGGGTTGTGTACGGAACCTGTCATCA"
            + "CAAAAAAGGTGAGGCACGGCGATCTAGAAGAGCCGTGACGCTCCCTTCTCACTCTACAAG"
            + "GAAGTTGCAAACGCGGTCGCAGACCTGGTTAGAATCAAGAGAATACACGAAGCACTTGAT"
            + "CAAGGTTGAAAACTGGATATTCAGGAACCCCGGGTTTGCGCTAGTGGCCGTTGCCATTGC"
            + "CTGGCTTTTGGGAAGCTCGACGAGCCAAAAAGTCATATACTTGGTCATGATACTGCTGAT"
            + "TGCCCCGGCATACAGTATCAGGTGCATTGGAGTCAGCAATAGAGACTTCGTGGAGGGCAT"
            + "GTCAGGTGGGACCTGGGTTGATGTTGTCTTGGAACATGGAGGCTGCGTTACCGTGATGGC"
            + "ACAGGACAAGCCAACAGTTGACATAGAGTTGGTCACGACGACGGTTAGTAACATGGCCGA"
            + "GGTAAGATCCTATTGCTACGAGGCATCGATATCGGACATGGCTTCGGACAGTCGTTGCCC"
            + "AACACAAGGTGAAGCCTACCTTGACAAGCAATCAGACACTCAATATGTCTGCAAAAGAAC"
            + "ATTAGTGGACAGAGGTTGGGGAAACGGTTGTGGACTTTTTGGCAAAGGGAGCTTGGTGAC"
            + "ATGTGCCAAGTTTACGTGTTCTAAGAAGATGACCGGGAAGAGCATTCAACCGGAAAATCT"
            + "GGAGTATCGGATAATGCTATCAGTGCATGGCTCCCAGCATAGCGGGATGACTGTCAATGA"
            + "TATAGGATATGAAACTGACGAAAATAGAGCGAAAGTCGAGGTTACGCCTAATTCACCAAG"
            + "AGCGGAAGCAACCTTGGGAGGCTTTGGAAGCTTAGGACTTGACTGTGAACCAAGGACAGG"
            + "CCTTGACTTTTCAGATCTGTATTACCTGACCATGAACAATAAGCATTGGTTGGTGCACAA"
            + "AGAGTGGTTTCATGACATCCCATTGCCTTGGCATGCTGGGGCAGACACTGGAACTCCACA"
            + "CTGGAACAACAAAGAGGCATTGGTAGAATTCAAGGATGCCCACGCCAAGAGGCAAACCGT"
            + "CGTCGTTCTGGGGAGCCAGGAAGGAGCCGTTCACACGGCTCTCGCTGGAGCTCTAGAGGC"
            + "TGAGATGGATGGTGCAAAGGGAAAGCTGTTCTCTGGCCATTTGAAATGCCGCCTAAAAAT"
            + "GGACAAGCTTAGATTGAAGGGCGTGTCATATTCCTTGTGCACTGCGGCATTCACATTCAC"
            + "CAAGGTCCCAGCTGAAACACTGCATGGAACAGTCACAGTGGAGGTGCAGTATGCAGGGAC"
            + "AGATGGACCCTGCAAGATCCCAGTCCAGATGGCGGTGGACATGCAGACCCTGACCCCAGT"
            + "TGGAAGGCTGATAACCGCCAACCCCGTGATTACTGAAAGCACTGAGAACTCAAAGATGAT"
            + "GTTGGAGCTTGACCCACCATTTGGGGATTCTTACATTGTCATAGGAGTTGGGGACAAGAA"
            + "AATCACCCACCACTGGCATAGGAGTGGTAGCACCATCGGAAAGGCATTTGAGGCCACTGT"
            + "GAGAGGCGCCAAGAGAATGGCAGTCCTGGGGGATACAGCCTGGGACTTCGGATCAGTCGG"
            + "GGGTGTGTTCAACTCACTGGGTAAGGGCATTCACCAGATTTTTGGAGCAGCCTTCAAATC"
            + "ACTGTTTGGAGGAATGTCCTGGTTCTCACAGATCCTCATAGGCACGCTGCTAGTGTGGTT"
            + "AGGTTTGAACACAAAGAATGGATCTATCTCCCTCACATGCTTGGCCCTGGGGGGAGTGAT"
            + "GATCTTCCTCTCCACGGCTGTTTCTGCTGACGTGGGGTGCTCAGTGGACTTCTCAAAAAA"
            + "GGAAACGAGATGTGGCACGGGGGTATTCATCTATAATGATGTTGAAGCCTGGAGGGACCG"
            + "GTACAAGTACCATCCTGACTCCCCCCGCAGATTGGCAGCAGCAGTCAAGCAGGCCTGGGA"
            + "AGAGGGGATCTGTGGGATCTCATCCGTTTCAAGAATGGAAAACATCATGTGGAAATCAGT"
            + "AGAAGGGGAGCTCAATGCTATCCTAGAGGAGAATGGAGTTCAACTGACAGTTGTTGTGGG"
            + "ATCTGTAAAAAACCCCATGTGGAGAGGTCCACAAAGATTGCCAGTGCCTGTGAATGAGCT"
            + "GCCCCATGGCTGGAAAGCCTGGGGGAAATCGTATTTTGTTAGGGCGGCAAAGACCAACAA"
            + "CAGTTTTGTTGTCGACGGTGACACACTGAAGGAATGTCCGCTTGAGCACAGAGCATGGAA"
            + "TAGTTTTCTTGTGGAGGATCACGGGTTTGGAGTCTTCCACACCAGTGTCTGGCTTAAGGT"
            + "CAGAGAAGATTACTCATTAGAATGTGACCCAGCCGTCATAGGAACAGCTGTTAAGGGAAG"
            + "GGAGGCCGCGCACAGTGATCTGGGCTATTGGATTGAAAGTGAAAAGAATGACACATGGAG"
            + "GCTGAAGAGGGCCCACCTGATTGAGATGAAAACATGTGAATGGCCAAAGTCTCACACATT"
            + "GTGGACAGATGGAGTAGAAGAAAGTGATCTTATCATACCCAAGTCTTTAGCTGGTCCACT"
            + "CAGCCACCACAACACCAGAGAGGGTTACAGAACCCAAGTGAAAGGGCCATGGCACAGTGA"
            + "AGAGCTTGAAATCCGGTTTGAGGAATGTCCAGGCACCAAGGTTTACGTGGAGGAGACATG"
            + "CGGAACTAGAGGACCATCTCTGAGATCAACTACTGCAAGTGGAAGGGTCATTGAGGAATG"
            + "GTGCTGTAGGGAATGCACAATGCCCCCACTATCGTTTCGAGCAAAAGACGGCTGCTGGTA"
            + "TGGAATGGAGATAAGGCCCAGGAAAGAACCAGAGAGCAACTTAGTGAGGTCAATGGTGAC"
            + "AGCGGGGTCAACCGATCATATGGACCACTTCTCTCTTGGAGTGCTTGTGATTCTACTCAT"
            + "GGTGCAGGAGGGGTTGAAGAAGAGAATGACCACAAAGATCATCATGAGCACATCAATGGC"
            + "AGTGCTGGTAGTCATGATCTTGGGAGGATTTTCAATGAGTGACCTGGCCAAGCTTGTGAT"
            + "CCTGATGGGTGCTACTTTCGCAGAAATGAACACTGGAGGAGATGTAGCTCACTTGGCATT"
            + "GGTAGCGGCATTTAAAGTCAGACCAGCCTTGCTGGTCTCCTTCATTTTCAGAGCCAATTG"
            + "GACACCCCGTGAGAGCATGCTGCTAGCCCTGGCTTCGTGTCTTCTGCAAACTGCGATCTC"
            + "TGCTCTTGAAGGTGACTTGATGGTCCTCATTAATGGATTTGCTTTGGCCTGGTTGGCAAT"
            + "TCGAGCAATGGCCGTGCCACGCACTGACAACATCGCTCTACCAATCTTGGCTGCTCTAAC"
            + "ACCACTAGCTCGAGGCACACTGCTCGTGGCATGGAGAGCGGGCCTGGCTACTTGTGGAGG"
            + "GATCATGCTCCTCTCCCTGAAAGGGAAAGGTAGTGTGAAGAAGAACCTGCCATTTGTCAT"
            + "GGCCCTGGGATTGACAGCTGTGAGGGTAGTAGACCCTATTAATGTGGTAGGACTACTGTT"
            + "ACTCACAAGGAGTGGGAAGCGGAGCTGGCCCCCTAGTGAAGTTCTCACAGCCGTTGGCCT"
            + "GATATGTGCACTGGCCGGAGGGTTTGCCAAGGCAGACATTGAGATGGCTGGACCCATGGC"
            + "TGCAGTAGGCTTGCTAATTGTCAGCTATGTGGTCTCGGGAAAGAGTGTGGACATGTACAT"
            + "TGAAAGAGCAGGTGACATCACATGGGAAAAGGACGCGGAAGTCACTGGAAACAGTCCTCG"
            + "GCTTGACGTGGCACTGGATGAGAGTGGTGATTTCTCCTTGGTAGAGGAAGATGGTCCACC"
            + "CATGAGAGAGATCATACTTAAGGTGGTCCTGATGGCCATCTGTGGCATGAACCCAATAGC"
            + "TATACCTTTTGCTGCAGGAGCGTGGTATGTGTATGTGAAGACTGGGAAAAGGAGTGGCGC"
            + "CCTCTGGGACGTGCCTGCTCCCAAAGAAGTGAAGAAAGGAGAGACCACAGATGGAGTGTA"
            + "CAGAGTGATGACTCGCAGACTGCTAGGTTCAACACAGGTTGGAGTGGGAGTCATGCAAGA"
            + "GGGAGTCTTCCACACCATGTGGCACGTTACAAAAGGAGCCGCACTGAGGAGCGGTGAGGG"
            + "AAGACTTGATCCATACTGGGGGGATGTCAAGCAGGACTTGGTGTCATACTGTGGGCCTTG"
            + "GAAGTTGGATGCAGCTTGGGATGGACTCAGCGAGGTACAGCTTTTGGCCGTACCTCCCGG"
            + "AGAGAGGGCCAGAAACATTCAGACCCTGCCTGGAATATTCAAGACAAAGGACGGGGACAT"
            + "CGGAGCAGTTGCTCTGGACTACCCTGCAGGGACCTCAGGATCTCCGATCCTAGACAAATG"
            + "TGGAAGAGTGATAGGACTCTATGGCAATGGGGTTGTGATCAAGAATGGAAGCTATGTTAG"
            + "TGCTATAACCCAGGGAAAGAGGGAGGAGGAGACTCCGGTTGAATGTTTCGAACCCTCGAT"
            + "GCTGAAGAAGAAGCAGCTAACTGTCTTGGATCTGCATCCAGGAGCCGGAAAAACCAGGAG"
            + "AGTTCTTCCTGAAATAGTCCGTGAAGCCATAAAAAAGAGACTCCGGACAGTGATCTTGGC"
            + "ACCAACTAGGGTTGTCGCTGCTGAGATGGAGGAGGCCTTGAGAGGACTTCCGGTGCGTTA"
            + "CATGACAACAGCAGTCAACGTCACCCATTCTGGGACAGAAATCGTTGATTTGATGTGCCA"
            + "TGCCACTTTCACTTCACGCTTACTACAACCCATCAGAGTCCCTAATTACAATCTCTACAT"
            + "CATGGATGAAGCCCACTTCACAGACCCCTCAAGTATAGCTGCAAGAGGATATATATCAAC"
            + "AAGGGTTGAAATGGGCGAGGCGGCTGCCATTTTTATGACTGCCACACCACCAGGAACCCG"
            + "TGATGCGTTTCCTGACTCTAACTCACCAATCATGGACACAGAAGTGGAAGTCCCAGAGAG"
            + "AGCCTGGAGCTCAGGCTTTGATTGGGTGACAGACCATTCTGGGAAAACAGTTTGGTTCGT"
            + "TCCAAGCGTGAGAAACGGAAATGAAATCGCAGCCTGTCTGACAAAGGCTGGAAAGCGGGT"
            + "CATACAGCTCAGCAGGAAGACTTTTGAGACAGAATTTCAGAAAACAAAAAATCAAGAGTG"
            + "GGACTTTGTCATAACAACTGACATCTCAGAGATGGGCGCCAACTTCAAGGCTGACCGGGT"
            + "CATAGACTCTAGGAGATGCCTAAAACCAGTCATACTTGATGGTGAGAGAGTCATCTTGGC"
            + "TGGGCCCATGCCTGTCACGCATGCTAGTGCTGCTCAGAGGAGAGGACGTATAGGCAGGAA"
            + "CCCTAACAAACCTGGAGATGAGTACATGTATGGAGGTGGGTGTGCAGAGACTGATGAAGG"
            + "CCATGCACACTGGCTTGAAGCAAGAATGCTTCTTGACAACATCTACCTCCAGGATGGCCT"
            + "CATAGCCTCGCTCTATCGGCCTGAGGCCGATAAGGTAGCCGCCATTGAGGGAGAGTTTAA"
            + "GCTGAGGACAGAGCAAAGGAAGACCTTCGTGGAACTCATGAAGAGAGGAGACCTTCCCGT"
            + "CTGGCTAGCCTATCAGGTTGCATCTGCCGGAATAACTTACACAGACAGAAGATGGTGCTT"
            + "TGATGGCACAACCAACAACACCATAATGGAAGACAGCGTACCAGCAGAGGTGTGGACAAA"
            + "GTATGGAGAGAAGAGAGTGCTCAAACCGAGATGGATGGATGCTAGGGTCTGTTCAGACCA"
            + "TGCGGCCCTGAAGTCGTTCAAAGAATTCGCCGCTGGAAAAAGAGGAGCGGCTTTGGGAGT"
            + "AATGGAGGCCCTGGGAACACTGCCAGGACACATGACAGAGAGGTTTCAGGAAGCCATTGA"
            + "CAACCTCGCCGTGCTCATGCGAGCAGAGACTGGAAGCAGGCCTTATAAGGCAGCGGCAGC"
            + "CCAACTGCCGGAGACCCTAGAGACCATTATGCTCTTAGGTTTGCTGGGAACAGTTTCACT"
            + "GGGGATCTTCTTCGTCTTGATGCGGAATAAGGGCATCGGGAAGATGGGCTTTGGAATGGT"
            + "AACCCTTGGGGCCAGTGCATGGCTCATGTGGCTTTCGGAAATTGAACCAGCCAGAATTGC"
            + "ATGTGTCCTCATTGTTGTGTTTTTATTACTGGTGGTGCTCATACCCGAGCCAGAGAAGCA"
            + "AAGATCTCCCCAAGATAACCAGATGGCAATTATCATCATGGTGGCAGTGGGCCTTCTAGG"
            + "TTTGATAACTGCAAACGAACTTGGATGGCTGGAAAGAACAAAAAATGACATAGCTCATCT"
            + "AATGGGAAGGAGAGAAGAAGGAGCAACCATGGGATTCTCAATGGACATTGATCTGCGGCC"
            + "AGCCTCCGCCTGGGCTATCTATGCCGCATTGACAACTCTCATCACCCCAGCTGTCCAACA"
            + "TGCGGTAACCACTTCATACAACAACTACTCCTTAATGGCGATGGCCACACAAGCTGGAGT"
            + "GCTGTTTGGCATGGGCAAAGGGATGCCATTTTATGCATGGGACCTTGGAGTCCCGCTGCT"
            + "AATGATGGGTTGCTATTCACAATTAACACCCCTGACTCTGATAGTAGCTATCATTCTGCT"
            + "TGTGGCGCACTACATGTACTTGATCCCAGGCCTACAAGCGGCAGCAGCGCGTGCTGCCCA"
            + "GAAAAGGACAGCAGCTGGCATCATGAAGAATCCCGTTGTGGATGGAATAGTGGTAACTGA"
            + "CATTGACACAATGACAATAGACCCCCAGGTGGAGAAGAAGATGGGACAAGTGTTACTCAT"
            + "AGCAGTAGCCATCTCCAGTGCTGTGCTGCTGCGGACCGCCTGGGGATGGGGGGAGGCTGG"
            + "AGCTCTGATCACAGCAGCGACCTCCACCTTGTGGGAAGGCTCTCCAAACAAATACTGGAA"
            + "CTCCTCTACAGCCACCTCACTGTGCAACATCTTCAGAGGAAGCTATCTGGCAGGAGCTTC"
            + "CCTTATCTATACAGTGACGAGAAACGCTGGCCTGGTTAAGAGACGTGGAGGTGGGACGGG"
            + "AGAGACTCTGGGAGAGAAGTGGAAAGCTCGTCTGAATCAGATGTCGGCCCTGGAGTTCTA"
            + "CTCTTATAAAAAGTCAGGTATCACTGAAGTGTGTAGAGAGGAGGCTCGCCGTGCCCTCAA"
            + "GGATGGAGTGGCCACAGGAGGACATGCCGTATCCCGGGGAAGTGCAAAGCTCAGATGGTT"
            + "GGTGGAGAGAGGATATCTGCAGCCCTATGGGAAGGTTGTTGACCTCGGATGTGGCAGAGG"
            + "GGGCTGGAGCTATTATGCCGCCACCATCCGCAAAGTGCAGGAGGTGAGAGGATACACAAA"
            + "GGGAGGTCCCGGTCATGAAGAACCCATGCTGGTGCAAAGCTATGGGTGGAACATAGTTCG"
            + "TCTCAAGAGTGGAGTGGACGTCTTCCACATGGCGGCTGAGCCGTGTGACACTCTGCTGTG"
            + "TGACATAGGTGAGTCATCATCTAGTCCTGAAGTGGAAGAGACACGAACACTCAGAGTGCT"
            + "CTCTATGGTGGGGGACTGGCTTGAAAAAAGACCAGGGGCCTTCTGTATAAAGGTGCTGTG"
            + "CCCATACACCAGCACTATGATGGAAACCATGGAGCGACTGCAACGTAGGCATGGGGGAGG"
            + "ATTAGTCAGAGTGCCATTGTCTCGCAACTCCACACATGAGATGTACTGGGTCTCTGGGGC"
            + "AAAGAGCAACATCATAAAAAGTGTGTCCACCACAAGTCAGCTCCTCCTGGGACGCATGGA"
            + "TGGCCCCAGGAGGCCAGTGAAATATGAGGAGGATGTGAACCTCGGCTCGGGTACACGAGC"
            + "TGTGGCAAGCTGTGCTGAGGCTCCTAACATGAAAATCATCGGCAGGCGCATTGAGAGAAT"
            + "CCGCAATGAACATGCAGAAACATGGTTTCTTGATGAAAACCACCCATACAGGACATGGGC"
            + "CTACCATGGGAGCTACGAAGCCCCCACGCAAGGATCAGCGTCTTCCCTCGTGAACGGGGT"
            + "TGTTAGACTCCTGTCAAAGCCTTGGGACGTGGTGACTGGAGTTACAGGAATAGCCATGAC"
            + "TGACACCACACCATACGGCCAACAAAGAGTCTTCAAAGAAAAAGTGGACACCAGGGTGCC"
            + "AGATCCCCAAGAAGGCACTCGCCAGGTAATGAACATAGTCTCTTCCTGGCTGTGGAAGGA"
            + "GCTGGGGAAACGCAAGCGGCCACGCGTCTGCACCAAAGAAGAGTTTATCAACAAGGTGCG"
            + "CAGCAATGCAGCACTGGGAGCAATATTTGAAGAGGAAAAAGAATGGAAGACGGCTGTGGA"
            + "AGCTGTGAATGATCCAAGGTTTTGGGCCCTAGTGGATAGGGAGAGAGAACACCACCTGAG"
            + "AGGAGAGTGTCACAGCTGTGTGTACAACATGATGGGAAAAAGAGAAAAGAAGCAAGGAGA"
            + "GTTCGGGAAAGCAAAAGGTAGCCGCGCCATCTGGTACATGTGGTTGGGAGCCAGATTCTT"
            + "GGAGTTTGAAGCCCTTGGATTCTTGAACGAGGACCATTGGATGGGAAGAGAAAACTCAGG"
            + "AGGTGGAGTCGAAGGGTTAGGATTGCAAAGACTTGGATACATTCTAGAAGAAATGAATCG"
            + "GGCACCAGGAGGAAAGATGTACGCAGATGACACTGCTGGCTGGGACACCCGCATTAGTAA"
            + "GTTTGATCTGGAGAATGAAGCTCTGATTACCAACCAAATGGAGGAAGGGCACAGAACTCT"
            + "GGCGTTGGCCGTGATTAAATACACATACCAAAACAAAGTGGTGAAGGTTCTCAGACCAGC"
            + "TGAAGGAGGAAAAACAGTTATGGACATCATTTCAAGACAAGACCAGAGAGGGAGTGGACA"
            + "AGTTGTCACTTATGCTCTCAACACATTCACCAACTTGGTGGTGCAGCTTATCCGGAACAT"
            + "GGAAGCTGAGGAAGTGTTAGAGATGCAAGACTTATGGTTGTTGAGGAAGCCAGAGAAAGT"
            + "GACCAGATGGTTGCAGAGCAATGGATGGGATAGACTCAAACGAATGGCGGTCAGTGGAGA"
            + "TGACTGCGTTGTGAAGCCAATCGATGATAGGTTTGCACATGCCCTCAGGTTCTTGAATGA"
            + "CATGGGAAAAGTTAGGAAAGACACACAGGAGTGGAAACCCTCGACTGGATGGAGCAATTG"
            + "GGAAGAAGTCCCGTTCTGCTCCCACCACTTCAACAAGCTGTACCTCAAGGATGGGAGATC"
            + "CATTGTGGTCCCTTGCCGCCACCAAGATGAACTGATTGGCCGAGCTCGCGTCTCACCAGG"
            + "GGCAGGATGGAGCATCCGGGAGACTGCCTGTCTTGCAAAATCATATGCGCAGATGTGGCA"
            + "GCTCCTTTATTTCCACAGAAGAGACCTTCGACTGATGGCTAATGCCATTTGCTCGGCTGT"
            + "GCCAGTTGACTGGGTACCAACTGGGAGAACCACCTGGTCAATCCATGGAAAGGGAGAATG"
            + "GATGACCACTGAGGACATGCTCATGGTGTGGAATAGAGTGTGGATTGAGGAGAACGACCA"
            + "TATGGAGGACAAGACTCCTGTAACAAAATGGACAGACATTCCCTATCTAGGAAAAAGGGA"
            + "GGACTTATGGTGTGGATCCCTTATAGGGCACAGACCCCGCACCACTTGGGCTGAAAACAT"
            + "CAAAGACACAGTCAACATGGTGCGCAGGATCATAGGTGATGAAGAAAAGTACATGGACTA"
            + "TCTATCCACCCAAGTCCGCTACTTGGGTGAGGAAGGGTCCACACCCGGAGTGTTGTAAGC"
            + "ACCAATTTTAGTGTTGTCAGGCCTGCTAGTCAGCCACAGTTTGGGGAAAGCTGTGCAGCC"
            + "TGTAACCCCCCCAGGAGAAGCTGGGAAACCAAGCTCATAGTCAGGCCGAGAACGCCATGG"
            + "CACGGAAGAAGCCATGCTGCCTGTGAGCCCCTCAGAGGACACTGAGTCAAAAAACCCCAC"
            + "GCGCTTGGAAGCGCAGGATGGGAAAAGAAGGTGGCGACCTTCCCCACCCTTCAATCTGGG"
            + "GCCTGAACTGGAGACTAGCTGTGAATCTCCAGCAGAGGGACTAGTGGTTAGAGGAGACCC"
            + "CCCGGAAAACGCAAAACAGCATATTGACGCTGGGAAAGACCAGAGACTCCATGAGTTTCC"
            + "ACCACGCTGGCCGCCAGGCACAGATCGCCGAACAGCGGCGGCCGGTGTGGGGAAATCCAT"
            + "GGTTTCT";
    private static final String CONTENTS = '>' + EXPECTED_DESCRIPTION + '\n' + EXPECTED_SEQUENCE;
    private static final Path PATH = Paths.get("..", "testfiles", "LC002520");
    private static final Path OUTPUT_PATH = Paths.get("..", "testfiles", "output","test.fa");
    private static final Sequence SEQUENCE = new Sequence(EXPECTED_SEQUENCE.toCharArray(), EXPECTED_DESCRIPTION);

    private Loader loader;

    @Before
    public void construct() {
        loader = new FASTALoader(null);
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
        loader.writeSequence(SEQUENCE, bw);
        bw.flush();
        assertThat(wr.getBuffer().toString(), is(equalTo(CONTENTS)));
    }
    
    @Test
    public void testWriteSequenceToFile() throws IOException {
        Files.deleteIfExists(OUTPUT_PATH);
        loader.writeSequence(SEQUENCE, OUTPUT_PATH);
        assertThat(StringUtils.toCharArray(Files.readAllLines(OUTPUT_PATH), "\n"), is(equalTo(CONTENTS.toCharArray())));
        Files.delete(OUTPUT_PATH);
    }

}