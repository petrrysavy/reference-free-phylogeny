package rysavpe1.reads.overlap;

import java.util.Arrays;
import java.util.Collection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import rysavpe1.reads.model.Sequence;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import rysavpe1.reads.distance.simple.EditDistance;

/**
 *
 * @author petr
 */
@RunWith(Parameterized.class)
public class EditDistanceOverlapFinderTest {

    private static final String s1 = "TTTGGTCGCTGTCTGGCTGTCAGTAAGTATGCTAGAGTCCCGTTTCCGTTTCATTACCAACACCACGTCTCCTTGCCCAATTAGCACATTAGCCTTCTCTCCTTTCGCAAGGTTGCTCAGTTCATTGATGCTTAGTGCTGGCCCATATCTCTTGTCTTCTTTGCCCAGAATGAGGAATCCCCTCAGAACAGCGGACTCCACTCCAGCTGTGCCTTCATCTGGGTCTTCAGTTAAAGTGCCAGCATCCTTTCCGAGAACTGTGAGTCTCTTCGTGGCCTTGTTATAGTTGAATACAGGAGAATTGCCCCTTACAAGTATTCTCATTCCTGATCCCCTCACATTCACAGTAAATGAGGAGAACTGCATTCTACTTTGCTTTGGTGGAGCGGCTGCGAAGGGAAGAAGTTTTATTATCTGTGCGGTATCAAATGTCCCAAGCACATCCCTCATTTGTTGGAACAGAGTTCTTACAAACCCACTGTATTGGCCTCTAATGGCCTTAGGTACTAAAGACTGAAATGGTTCAAATTCCATTTTATTGTATAGCATTGTAGGGTTCTGGGACCACTGAATTTTAACAGTTTCCCAGTTTCTGATGATCCATTGATAGGTATTGACCAACACTGATTCAGGACCATTAATCTCCCACATCATTGACGATGAGTAAGTTATTGTCAGTTTCTCTGTTCCCTGTGTTTCACTGACCTCCTCGGGAGACAGTAGTACATTTCCTCGTTGGTCCCGGATTCTCAAAAAACGGTCAATGCTCACCACTACCCTCTCCGTGCTGGAGTACTCATCTACACCCATTTTGCTGATTCTCACTCCTCTCATTGACATCTCGATGCTTGGAGTCATGTCTGGCAATATCCCAATCATTCCCATCACATTGTCGATAGGTTCAACTCCCCAATTTTGAAAAAGCACTTTCGCATCCTTCTGAAAATGTCTTAAAAGTTGATGCATAGGATTCAATCGCTGATTCGCCCTATTGACGAAATTCAGATCACCTCTGACTGCTTTTATCATACAATCCTCTTGTGAAAATACCATGGCCACAATTATTGCTTCGGCAATCGACTGTTCGTCTCTCCCACTCACTATCAGCTGAATCAATCTCCTGGTTGCTTTTCTGAGTATGGCTGTTGCTCTTCTCCCAACCATTGTGAACTCTTCATATCCCTCATGCACTCTTATCTTCAATGTTTGAAGATTGCCCGTAAGCACCTCTTCCTCTCTCTTGACTGATGATCCGCTTGTTCTCTTAAATGTGAATCCACCAAAACTGAAGGATGAGCTAATTCTCAGTCCCATTGCAGCCTTGCATATATCCACGGCTTGCTCTTCTGTTGGGTTCTGCCTAAGGATGTCTACCATCCTAATTCCACCAATCTGTGTGCTGTGGCACATCTCCAATAAAGATGCTAGTGGATCTGCTGATACTGCAGCTCTTCTCACTATGTTCCTAGCAGCAATAATCAAGCTTTGATCAACATCATCATTCCTCACTTCCCCTCCTGGAGTATACATCTGTTCCCAGCATGTTCCTTGAGTCAAATGCAACACTTCAATGTACACACTGCTTGTTCCACCAGCCACTGGGAGGAATCTCGTTTTGCGGACCAGTTCTCTCTCCAACATGTATGCAACCATCAAAGGAGAAATTTTGCAATCCTGGAGTTCTTCTTTCTTCTCTTTGGTTATCGTTAGTTGCGATTCCGATGTTAGTATCCTGGCTCCCACTTCGTTAGGGAAAACAACTTCCATGATTACATCCTGTGCCTCCTTGGCACTGAGATCTGCATGACCAGGATTTATGTCAACTCTCCGACGTATTTTGACTTGGTTTCTAAAATGGACAGGGCCAAAGGTTCCATGCTTTAGCCTTTCGACTCTTTCAAAATAAGTTTTGTAGATTTTTGGATAATGAACTGTATTTGTTATTGGTCCATTCCTATTCCACCATGTCACAGCCAGAGGTGATACCATCACTCGGTCTGATCCTGCATCATTCATTTTACTCCATAAAGTTTGTCCTTGCTCATTTCTCTCAGGAATCATTTCCGTTATCCTCTTGTCTGCTGTAATTGGATATTTCATTGCCATCATCCATTTCATCCTAAGTGCTGGGTTCTTCTCCTGTCTTCCTGATGTGTACTTCTTGATTATGGC";
    private static final String s2 = "GACGTAACAGAGGCTTCTTTATTCTAGGAAGTAATGAGTACAGTTTCCTTCCGTGCTAGCCCGAGCGACGGAAAAGCAGGCTCTCGAGCTTCTGAGGGGCGGGGACCTTTCTGTGTAGAAGACCAGAACGTGAAAGGTAGTAGGAATACTAAAGCCAGGAGTACAGTCTACAAGAAGGGAGACATAAGGGTAACTTACGACGGTATTACCAACAAGACAGTTTTCCCTCTAAAGAGACATGACTCTTGCATCCAACATACGACTAAACCGGGCGTCTACGGGAGACAACTAACCACAAAGGAGGTGAAGACCAGGAATACCGGGTCATGGACGAAGAGTCAAGTTCACATGAACTAAGGTATCAGAGGTATAAAAGTAACCTTCGTTAAACTTGAGGAGATCACCTTTCGAAGGGAGAACCCTCGTGGAAGCAGGGAAACTACTTCGAATTATGAGAATCTAGAAGTTTACGCCGTCTTACCGTACGGTAGGTGTGGTCAACTGAGAACACACGACCTAAGAGTAAACCAGACTAATCCGACATGTGAACCGACAAAACTTCGTCAGACTTTCCCAGATAAGGCTGATCTCTCATAGGGAGAGAAAGTTTCAGCATGGGTGACCGATGCCGTCCAGGTATGTGTGTCCGTCCGTCCGTCCTGAACACTCGTTGGCTGGGAGAGTTATACTCACGTCTGGCACGATCTTTTCACTCTAGAAGCTTGAGTCGTAAGGGACCCAAGGCCGAGAGAGAGTGAACTAGGTAGTAACGAAAAACACGTCGTCAAACTTTAAAGGGAAACTCTTACAACGTGTAAGAAAGTATTCGTTAAGAACAAAAAGCAGGTAAGAGTGGGGAGGTCTTCAAGGCTAGTAACTAGGGTGCAAACTAGTAGGACTGGTTAAGGTAGTGGTAACAAGGTTGAGGAAACTGACGTCGTGGACGCCGAGGTCTGGAGGATCCCTCTCAACTTGGAACGTAGTCTCTCGTGTAGGACCCTAGGTAAGGCCACGCTTGTTCTCGAGAACAGGAGACTATTCAACGTAGTAAGTTTAACCTTACGGTCTAGTAGTACACTCAGTCTGGTCGGCAACGTAGCAGTGGTAATAATCGAACCGCGGTCTAAGCGGAATAAAGAAGAAACAGTATTTCCTACTCAAGAGAGTAGGTGAAAGGCAAATGAGAGGACATATATCCAGGAGGTCAAAAGAATCCTAGAAAGGGGCGTGACCCTACAAGAAGGTCCATAAATAAAGAGGAAAGCAGTTTTCGTCTCTCGTGGTAAGAGAGATAACAATTCGACAAAACCTAGTTGGCAGGGAGTATTAGTGACTCAAACTCAAGCCACGTGTAAACCTACATCTTAGCAGGTTAAGGTGGTTAGTAAAAAGGCTGCCTACGAGACTAAAGTCACCGTAAGACCGCAAGAGGTAGTCAGAGGTAGACAAGCAT";

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
            {"CATG", "AAGC", 1, 4, 0, 3, 1, 2},
            {"ABCDEF", "DEFGHI", 3, 6, 0, 3, 0, 2},
            {"ABCDEFGH", "DEFGHI", 3, 8, 0, 5, 0, 2},
            {"ABCDEF", "XYZABC", 0, 3, 3, 6, 0, 2},
            {"ABCDEFGH", "XYZABCDEF", 0, 6, 3, 9, 0, 2},
            {"ABCDEF", "DXFGHI", 3, 6, 0, 3, 1, 2},
            {"ABCDEFGH", "DEXGHI", 3, 8, 0, 5, 1, 2},
            {"AMCDEF", "XYZABC", 0, 3, 3, 6, 1, 2},
            {"ABMDEFGH", "XYZABCDEF", 0, 6, 3, 9, 1, 2},
            {"ABCDEF", "DXFGHI", 3, 6, 0, 3, 1, 2},
            {"ABCDEYXH", "DEFGHI", 3, 8, 0, 5, 2, 2},
            {"ABCDEFGHIJKL", "DEFGH", 3, 8, 0, 5, 0, 2},
            //            {"ABCYEFGXIJKL", "DEFGH", 3, 7, 0, 5, 2, 2},
            {"DEFGH", "ABCDEFGHIJKL", 0, 5, 3, 8, 0, 2},
            {"ACTGCTGCAACT", "GCATCTGAAAT", 6, 12, 0, 6, 1, 3},
            //            {"DEFGH", "ABCYEFGXIJKL", 0, 5, 4, 8, 5, 2},
            {"GACGTAACAGAGGCTTCTTTATTCTAGGAXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", "YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYGATGTGTACTTCTTGATTATGGC", 0, 29, 32, 32 + 23, 12, 10},
            {s1, s2, 2141, 2172, 0, 27, 13, 10}
        });
    }

    private final Sequence pattern, query;
    private final int startA, endA, startB, endB;
    private final double distance;
    private final int threshold;

    public EditDistanceOverlapFinderTest(String pattern, String query, int startA, int endA, int startB, int endB, int distance, int threshold) {
        this.pattern = Sequence.fromString(pattern);
        this.query = Sequence.fromString(query);
        this.startA = startA;
        this.endA = endA;
        this.startB = startB;
        this.endB = endB;
        this.distance = distance;
        this.threshold = threshold;
    }

    @Test
    public void testGetOverlap() {
        final OverlapRegion result = new EditDistanceOverlapFinder(threshold).getOverlap(pattern, query);
        System.err.println("pattern : " + pattern + " query " + query);
        System.err.println("result : " + result + ", " + result.distanceToLengthRatio());
        assertThat(result.getStartA(), is(equalTo(startA)));
        assertThat(result.getEndA(), is(equalTo(endA)));
        assertThat(result.getStartB(), is(equalTo(startB)));
        assertThat(result.getEndB(), is(equalTo(endB)));
        assertThat(result.getDistance(), is(equalTo(distance)));
        assertThat((int) result.getDistance(), is(equalTo(
                new EditDistance().getDistance(pattern.subSequence(startA, endA), query.subSequence(startB, endB)))));
        System.err.println("ok");
    }

    @Test
    public void testGetOverlapSym() {
        final OverlapRegion result = new EditDistanceOverlapFinder(threshold).getOverlap(query, pattern);
        System.err.println("query : " + query + "\npattern " + pattern);
        System.err.println("result : " + result + ", " + result.distanceToLengthRatio());
        assertThat(result.getStartA(), is(equalTo(startB)));
        assertThat(result.getEndA(), is(equalTo(endB)));
        assertThat(result.getStartB(), is(equalTo(startA)));
        assertThat(result.getEndB(), is(equalTo(endA)));
        assertThat(result.getDistance(), is(equalTo(distance)));
        assertThat((int) result.getDistance(), is(equalTo(
                new EditDistance().getDistance(pattern.subSequence(startA, endA), query.subSequence(startB, endB)))));
        System.err.println("ok");
    }

}
