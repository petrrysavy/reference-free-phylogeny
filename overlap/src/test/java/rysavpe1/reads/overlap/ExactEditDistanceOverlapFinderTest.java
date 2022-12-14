/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rysavpe1.reads.overlap;

import java.util.Arrays;
import java.util.Collection;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import rysavpe1.reads.distance.simple.EditDistance;
import rysavpe1.reads.model.Sequence;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
@RunWith(Parameterized.class)
public class ExactEditDistanceOverlapFinderTest {

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
            {"ABCYEFGXIJKL", "DEFGH", 3, 7, 0, 5, 2, 2},
            {"DEFGH", "ABCDEFGHIJKL", 0, 5, 3, 8, 0, 2},
            //            {"DEFGH", "ABCYEFGXIJKL", 0, 5, 4, 8, 5, 2}, // not sure hether to ed after H or not - both sols are equal
            {"GACGTAACAGAGGCTTCTTTATTCTAGGAXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX", "YYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYGATGTGTACTTCTTGATTATGGC", 0, 29, 32, 32 + 23, 12, 10},
            //            {s1, s2, 2149, 2172, 0, 29, 12, 10} // different than the one found by approx - and better
            {"AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", "TCGGGCCCCTGGCTCACGCCGAGGTGTCTAGTTGACTACCCCTATAGACTGTGGCACTATCCGTGCACCGTCAATTATACCATCCACAAGGTGAGGTTATACTTAGGGGGGCTTGAACACAGGTTCCAAGCGGCGTGTAACTGGACCCGTGGGGACAGATGTGAGCTTGAAGACAGGGACAGGTTTGAAATGAGCCCTCTTCTGCATTCGACAACTGAGCTCGCAATACTCCCATGTACATTCACCACCATGCCAGCCTTATCAACCGGGCTGATACACTTGCATCAGAACATCGTGGATGTCCAGTACCTCTACGGAGTAAGCTCATCTGTTATCTCATGGGCTATTAAGTGGGAGTACATTGTCCTCGCCTTCCTTGTGCTAGCTGACGCGCGCATCTGTGCGTGTCTTTGGCTTATGTTGCTCATAGGCAAGGCGGAGGCCGCATTAGAGAATCTGATCACCCTCAATGCTGCTGCCGCAGCTAGCATGCAGGGGTGGTGGTGGTGTCTCGCTTTTGTGTGCTGCGCTTGGTACATCAGGGGTCGTTTCGTCCCAGCAGTCACATACGGGCTCTTGCAGATATGGCCGCTACTATTGCTTGTCTTGGCCCTCCCCCAGCGCGCTTTCGCTTACGATCACGAAAGCGCGGCCTCTGTCGGGGTGCTGGTTCTAATGGCCATCACGGTGTTTAC",
                0, 56, 72, 129, 39, 20},
            // 17 vs 37 clash
            {"ACAATCGAGACCACCACAGTCCCACAGGATGCTGTGGCAAGGAGTCAGAGGCGAGGCCGTACTGGTAGGGGGAAGCATGGGGTGTATAGGTACGTCTCGCAGGGGGAACGACCGTCTGGTATGTTCGATTCCATCGTCCTTTGCGAGGCGTACGATACTGGTTGTGCGTGGTACGAACTAACACCTTCAGAGACCACCGTCAGACTACGCGCATACCTTAACACCCCTGGACTGCCAGTGTGTCAGGATCATCTTGACTTCTGGGAGGGCGTCTTCACAGGCCTCACTCACATCGACGCCCACTTCCTTTCACAAACCAAACAGGGGGGGGAGAACTTCGCATACCTGGTGGCTTACCAGGCCACGGTGTGTGCGCGGGCTAAGGCCCCTCCACCCTCCTGGGACACTATGTGGAAGTGCCTCACACGGCTAAAGCCTATGCTCACAGGCCCAACGCCCTTGTTGTACAGGCTTGGGGCCGTGCAAAATGAGATCACCACCACACATCCAATCACAAAATACATCATGACCTGCATGGCCGCTGATCTAGAGGTGATTACCAGCACGTGGGTAATAGCTGGAGGGGTTCTCGCAGCTCTGGCGGCCTACTGCCTGTGCGTAGGCAGCGCAGTCATCTGCGGCCGAATCATAACTTCCGGGAAACCGGCGGTGATGCCGGACCGGGAGATCTTGTATCAGCAATACGACGAGATGGAGGAGTGCTCTCAGCACATACCCTACCTCGCCGAAGGCCAGCAGATAGCTGAACAGTTCAAGCAAAAGAT", "TACCAGTGCTGCCAACTAGATCCGGTAGCACGGAAAGCCATCACATCTCTTACTGAGCGGCTGTACTGCGGCGGCCCCATGTACAACTCCCGAGGCCAGTCATGTGGGTACCGCAGGTGCCGGGCTAGTGGTGTCTTCACCACAAGCCTGGGCAACACCATGACATGCTACCTGAAGGCTCAGGCGGCTTGTAGGGCAGCAAAGCTCAAAAACTTTGACATGTTGGTCTGCGGAGACGACTTGGTTGTCGTTGCTGAGAGCGGGGGAGTCTCTGAGGACGCCGGGGCTCTGCGAGTCTTCACGGAGGCTATGACCAGATACTCGGCCCCCCCAGGAGACGAACCACGCCCAGAATACGACCTGGAGCTGATAACATCATGCTCGTC",
                764, 785, 0, 21, 8, 21},
            // 20 vs 172
            {"GAGGGTGGTTGACGTAGACAGTGGGCTGGTTAAAGCGGCGGAGAATGACATGAGGGAGGCCACAGCAGGGTTACCCGGCAGGGTGGACAGCCCGGCTAGGTACTGTATCCCACTGATGAAGTTCCAGAGGTGTTTCTGCCAAAACTGCTCGAGCTTTGGCCATGCCGACTGAACCGCCGGCTTGAGCTCTTCAGCCTGCTTTGCAGTGTTCTGGAGCAAGCCTAG", "AAAAAAAAAAAGAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAGGAACAGTTAGCTATGGAGTGTACCTAGTGTGTGCCGCTCTACCGAGCGGGGAGTAGGAAGAGGCCTACCCCTACGAAAAGTAGGAGTAGGCCGAAGAGTAATGAGCGGGGTCGGGCGCGCGACACGCTGTGAAAAATGTCGCCCCCGCCGGCGCCGACGGTGAACCAACTGGATAAGTCCAGTAGGCGCGCCTCCGGCAATGGAGTGAGTTTGAGCTTGGTCTTCACCGCCCAATTGAAGAGATATCGGCCGCAAACGGCCGCTTTCCCTCCACGGGAGATGAGGGACGCCCTGACTGCGCGAGCCCGACTCTTCCACACCCTGAGGGGTGGCGCCCCAAGTTTTCTGAGGGCTGAAGCCACCCGCGTCAGTTCGTGGTGAGAGTATGTGTGCATAGAAAAGGCGTCAAGCCCGTGTAACCTCTCAATTATGGCTGGAAGGTCCAAAGGATTCACGGAGTATACTGATCCATACATCTCAAAGTTGAGGTTCTGGTCCAGGGTGTCTTGGACCATGAGAATGGAGAAGAAGTGTGTCATTAGGACCATGCGAACCCATATGGTTGGAGCATACTGGATGATGTTTCCCAGCCATGAATTGATAGGGGAGTGTCTAACTGTTTCCCAGGCAGCCCGGGCGAGTGGAGTGGTTGGGTCTCTGGTCAGGTAGTATCTGCGGCGGCCCCGCGGGCCCAACGCCACAGACACATTTGAGGAACAGGATGTTATTAGCTCCAGGTCATATTCCGGTCTGGGGGGATCACCAGGAGGGGCAGAGTACCTGGTCATGGCCTCCGTGAAGGCTCTCAGGTTCCGCTCGTCCTCCTCAGTCCCCTGGCTTTCTGAGATGACTACTAGGTCATCGCCGCATACCAGCATTGTGGGCGCAACTATCCCCGCAGCCTTGCAGGCCGCTAGGGCTTTCACATAGCATGTGATGGTGTTACCCATGCTAGTGGTTAGCACCCCGCTGGCGCGGCAACGTCTGTAACCGCAGGTTTGACCCTTGCTGTT",
                0, 23, 1048, 1069, 10, 21},
            // 70 vs 41
            {"GTGGCTTATCAAGCCACAGTTTGTGCGCGGGCGAAAGCCCCCCCCCCCTCATGGGACACTATGTGGAAATGTCTCACACGCCTAAAGCCTATGCTCACGGGGCCTACGCCCCTGCTGTACAGGCTTGGGTCCGTACAAAATGAGATCACGACCACGCATCCAATCACTAAATACATCATGACCTGCATGTCGGCTGATTTAGAAGT", "AGGGGTCAGATGGTCATACAGGTAGGTGCCGGTCCAAGCACCGATCTTGAGGAGGCAAGCTTGCATGTACTTACCATGGGCCAGTCTGCGGACAGCCAAACACACCCGTAATAGGGCATGAGCGCGCACGTAATACGGTACGCGCAACAGGCTCGCCTGCAGTAGGTAGAGAGGTCCTATAATGGCTAAGAGCACTTTGGAGATGTCAAATCCAAGGGCGGGGTGCAAGAGGCATGTGAGCAGAATCACTGCATCACGGCCCCCCCGTACCTGTAGACCTGGGACCCATTGATGTATTACGGCCTCCGCCCGGGCTATGAA",
                186, 206, 0, 23, 10, 20
            }
        });
    }

    private final Sequence pattern, query;
    private final int startA, endA, startB, endB;
    private final double distance;
    private final int threshold;

    public ExactEditDistanceOverlapFinderTest(String pattern, String query, int startA, int endA, int startB, int endB, int distance, int threshold) {
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
        final OverlapRegion result = new ExactEditDistanceOverlapFinder(threshold).getOverlap(pattern, query);
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
        final OverlapRegion result = new ExactEditDistanceOverlapFinder(threshold).getOverlap(query, pattern);
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
