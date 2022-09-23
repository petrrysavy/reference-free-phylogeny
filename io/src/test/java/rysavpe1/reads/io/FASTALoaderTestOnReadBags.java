package rysavpe1.reads.io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.StringTokenizer;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Before;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import rysavpe1.reads.model.ReadBag;
import rysavpe1.reads.utils.CollectionUtils;

/**
 *
 * @author Petr Ryšavý
 */
public class FASTALoaderTestOnReadBags {

    private static final ReadBag EXPECTED_BAG = ReadBag.fromString(
            "CTTACGAACAGATGGAGACTGATGGAGAAC",
            "CTCCCTTTTGACAGAACAACCATTATGGCA",
            "TTCCTTTGACATGAGTAATGAAGGATCTTA",
            "CATCCGTCGGAAAAATGATTGGTGGAATTG",
            "TATGGACCTGCCGTAGCCAGTGGGTACGAC",
            "ACACAAGAGTCAACTGGTGTGGATGGCATG",
            "CAAGAATTGCTTATGAAAGAATGTGCAACA",
            "CCAAGTGTACAGCCTAATCAGACCAAATGA",
            "CCTGCCGTAGCCAGTGGGTACGACTTTGAA",
            "CTAGTCGGAATAGACCCTTTCAGACTGCTT",
            "AATCAGCATACAACCTACGTTCTCAGTACA",
            "TTCTAGCACGGTCTGCACTCATATTGAGAG",
            "GCATTCAATGGGAATACAGAGGGAAGAACA",
            "CTCCCTTTTGACAGAACAACCATTATGGCA",
            "CCAATGAAAATATGGAGACTATGGAATCAA",
            "AACCTACGTTCTCAGTACAGAGAAATCTCC",
            "ATAAGGACCAGAAGTGGAGGAAACACCAAT",
            "CATCAAAGGGACGAAGGTGCTCCCAAGAGG",
            "ACATCAAAATCATGGCGTCCCAAGGCACCA",
            "TATGACAAAGAAGAAATAAGGCGAATCTGG",
            "CAGCGAGCCCGATCGTGCCTTCCTTTGACA",
            "GCTTTCCACTAGAGGAGTTCAAATTGCTTC",
            "TGTGCTCTCTGATGCAAGGTTCAACTCTCC",
            "GAATCCAGCACACAAGAGTCAACTGGTGTG",
            "AAACAGCCAAGTGTACAGCCTAATCAGACC",
            "CTTACGAACAGATGGAGACTGATGGAGAAC",
            "GGTGCTGCAGTCAAAGGAGTTGGAACAATG",
            "ATAAGGATGATGGAAAGTGCAAGACCAGAA",
            "GAACATCCCAGTGCGGGGAAAGATCCTAAG",
            "AAACGTGGGATCAATGATCGGAACTTCTGG",
            "GAGTAAACGGAAAGTGGATGAGAGAACTCA",
            "TGGAGACTATGGAATCAAGTACACTTGAAC",
            "AGCAGGTACTGGGCCATAAGGACCAGAAGT",
            "GAGGGAAGAACATCTGACATGAGGACCGAA",
            "TCAGAGCATCCGTCGGAAAAATGATTGGTG",
            "TCCAGGGGCGGGGAGTCTTCGAGCTCTCGG",
            "AACCTACGTTCTCAGTACAGAGAAATCTCC",
            "ATGAGGACCGAAATCATAAGGATGATGGAA",
            "GGGCATCTGCGGGCCAAATCAGCATACAAC",
            "GTACGACTTTGAAAGAGAGGGATACTCTCT",
            "CCAAGAGGGAAGCTTTCCACTAGAGGAGTT",
            "TCAAATTGCTTCCAATGAAAATATGGAGAC",
            "ATCAAAGGGACGAAGGTGCTCCCAAGAGGG",
            "GAATGGACGAAAAACAAGAATTGCTTATGA",
            "GAAGATGTGTCTTTCCAGGGGCGGGGAGTC",
            "GCGGGGAAAGATCCTAAGAAAACTGGAGGA",
            "ACTCATATTGAGAGGGTCGGTTGCTCACAA",
            "CTTCGAGCTCTCGGACGAAAAGGCAGCGAG",
            "TTTCAAACTGCTGCACAAAAAGCAATGATG",
            "ACATCAAAATCATGGCGTCCCAAGGCACCA",
            "GATCGTGCCTTCCTTTGACATGAGTAATGA",
            "GATGGAGACTGATGGAGAACGCCAGAATGC",
            "AAATCATAAGGATGATGGAAAGTGCAAGAC",
            "TGAAAATATGGAGACTATGGAATCAAGTAC",
            "TTGACAGAACAACCATTATGGCAGCATTCA",
            "CACACAAGAGTCAACTGGTGTGGATGGCAT",
            "TAGACCCTTTCAGACTGCTTCAAAACAGCC",
            "CCTTTCAGACTGCTTCAAAACAGCCAAGTG",
            "GAGAAATCTCCCTTTTGACAGAACAACCAT",
            "TCTGACTCACATGATGATCTGGCATTCCAA",
            "TCAGACCAAATGAGAATCCAGCACACAAGA",
            "TACAACCTACGTTCTCAGTACAGAGAAATC",
            "TGACATCAAAATCATGGCGTCCCAAGGCAC",
            "ACGAAGGTGCTCCCAAGAGGGAAGCTTTCC",
            "ACTTCTGGAGGGGTGAGAATGGACGAAAAA",
            "CCCTTTCAGACTGCTTCAAAACAGCCAAGT",
            "GTCTTACGAACAGATGGAGACTGATGGAGA",
            "ATCGTGCCTTCCTTTGACATGAGTAATGAA",
            "ATGGTGACGATGCAACGGCTGGTCTGACTC",
            "ACATCTGACATGAGGACCGAAATCATAAGG",
            "ATGGAGACTGATGGAGAACGCCAGAATGCC",
            "TGATGCAAGGTTCAACTCTCCCTAGGAGGT",
            "CAATGGGAATACAGAGGGAAGAACATCTGA",
            "ATGGCATGCCATTCTGCCGCATTTGAAGAT",
            "TCCACTAGAGGAGTTCAAATTGCTTCCAAT",
            "AAATACCTGGAAGAACATCCCAGTGCGGGG",
            "GAAGTGGAGGAAACACCAATCAACAGAGGG",
            "ATCGTGCCTTCCTTTGACATGAGTAATGAA",
            "AGATGTGTCTTTCCAGGGGCGGGGAGTCTT",
            "GACAGAACAACCATTATGGCAGCATTCAAT",
            "TCATGGCGTCCCAAGGCACCAAACGGTCTT",
            "GGTGATGGAATTGGTCAGGATGATCAAACG",
            "TGTGCACCGAACTCAAACTCAGTGATTATG",
            "GGAACTTCTGGAGGGGTGAGAATGGACGAA",
            "TGATCCAAAACAGCTTAACAATAGAGAGAA",
            "TACAGGAGAGTAAACGGAAAGTGGATGAGA",
            "CGTGGGATCAATGATCGGAACTTCTGGAGG",
            "ATAAGGCGAATCTGGCGCCAAGCTAATAAT",
            "GATGGCATGCCATTCTGCCGCATTTGAAGA",
            "TACCTGGAAGAACATCCCAGTGCGGGGAAA",
            "ATCTCCCTTTTGACAGAACAACCATTATGG",
            "GCAGCGAGCCCGATCGTGCCTTCCTTTGAC",
            "GAAAGATCCTAAGAAAACTGGAGGACCTAT",
            "TCTTCGGAGACAATGCAGAGGAGTACGACA",
            "GGGACGAAGGTGCTCCCAAGAGGGAAGCTT",
            "GGCAGCATTCAATGGGAATACAGAGGGAAG",
            "GACAATGCAGAGGAGTACGACAATTAAAGA",
            "CAGCGAGCCCGATCGTGCCTTCCTTTGACA",
            "ACATGAGTAATGAAGGATCTTATTTCTTCG",
            "CCTTCCTTTGACATGAGTAATGAAGGATCT",
            "GTCTTACGAACAGATGGAGACTGATGGAGA",
            "AAACAGCCAAGTGTACAGCCTAATCAGACC",
            "AATAAATACCTGGAAGAACATCCCAGTGCG",
            "AACAATAGAGAGAATGGTGCTCTCTGCTTT",
            "TGAGAATCCAGCACACAAGAGTCAACTGGT",
            "GGTCTTACGAACAGATGGAGACTGATGGAG",
            "TGGAATCAAGTACACTTGAACTGAGAAGCA",
            "AAGCAGGGTAGATAATCACTCACTGAGTGA",
            "GAGAGGGATACTCTCTAGTCGGAATAGACC",
            "GAAAGGAGAAATAAATACCTGGAAGAACAT",
            "GTATTAAGCTTCATCAAAGGGACGAAGGTG",
            "AATCTGGCGCCAAGCTAATAATGGTGACGA",
            "CGGAATAGACCCTTTCAGACTGCTTCAAAA",
            "TCAGTGATTATGAGGGACGGTTGATCCAAA",
            "TTTTGACGAAAGGAGAAATAAATACCTGGA",
            "AATAGAGAGAATGGTGCTCTCTGCTTTTGA",
            "CTTTGAAAGAGAGGGATACTCTCTAGTCGG",
            "CTTCAAAACAGCCAAGTGTACAGCCTAATC",
            "GGGAAGCTTTCCACTAGAGGAGTTCAAATT",
            "ATGGACCTGCCGTAGCCAGTGGGTACGACT",
            "CAGTGCGGGGAAAGATCCTAAGAAAACTGG",
            "GGACGATTCTACATCCAAATGTGCACCGAA",
            "TCAAAGGAGTTGGAACAATGGTGATGGAAT",
            "CAATGAAAATATGGAGACTATGGAATCAAG",
            "CGTCCCAAGGCACCAAACGGTCTTACGAAC",
            "TGCTCTCTGCTTTTGACGAAAGGAGAAATA",
            "GTGGGTACGACTTTGAAAGAGAGGGATACT",
            "GAGAGAGCCGGAACCCAGGGAATGCTGAGT",
            "CAGAACAACCATTATGGCAGCATTCAATGG",
            "TATATACAGGAGAGTAAACGGAAAGTGGAT",
            "ATCCAAAACAGCTTAACAATAGAGAGAATG",
            "ATGCCACTGAAATCAGAGCATCCGTCGGAA",
            "TGTACAGCCTAATCAGACCAAATGAGAATC",
            "GATGCAACTTATCAGAGGACAAGAGCTCTT",
            "AAATCTCCCTTTTGACAGAACAACCATTAT",
            "TGGAACAATGGTGATGGAATTGGTCAGGAT",
            "TGTCTTTCCAGGGGCGGGGAGTCTTCGAGC",
            "GGTGAGAATGGACGAAAAACAAGAATTGCT",
            "GAAAATATGGAGACTATGGAATCAAGTACA",
            "ATGGACCTGCCGTAGCCAGTGGGTACGACT",
            "GTCCTGCCTGCCTGCCTGTGTGTATGGACC",
            "CCACTGAAATCAGAGCATCCGTCGGAAAAA",
            "AGAACAACCATTATGGCAGCATTCAATGGG",
            "CTGAAATCAGAGCATCCGTCGGAAAAATGA",
            "TTTGAATGATGCAACTTATCAGAGGACAAG",
            "AAGAGTCAACTGGTGTGGATGGCATGCCAT",
            "CAACTGGTGTGGATGGCATGCCATTCTGCC",
            "TTGGTCAGGATGATCAAACGTGGGATCAAT",
            "GGCAGCGAGCCCGATCGTGCCTTCCTTTGA",
            "CTGCACTCATATTGAGAGGGTCGGTTGCTC",
            "ATCACTCACTGAGTGACATCAAAATCATGG",
            "ATCTTATTTCTTCGGAGACAATGCAGAGGA",
            "GAACAATGGTGATGGAATTGGTCAGGATGA",
            "CTCATCCTTTATGACAAAGAAGAAATAAGG",
            "CAGGATGTGCTCTCTGATGCAAGGTTCAAC");
    private static final String CONTENTS = ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 70 of length 30]\n"
            + "CTTACGAACAGATGGAGACTGATGGAGAAC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1296 of length 30]\n"
            + "CTCCCTTTTGACAGAACAACCATTATGGCA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1475 of length 30]\n"
            + "TTCCTTTGACATGAGTAATGAAGGATCTTA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 124 of length 30]\n"
            + "CATCCGTCGGAAAAATGATTGGTGGAATTG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 885 of length 30]\n"
            + "TATGGACCTGCCGTAGCCAGTGGGTACGAC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1013 of length 30]\n"
            + "ACACAAGAGTCAACTGGTGTGGATGGCATG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 688 of length 30]\n"
            + "CAAGAATTGCTTATGAAAGAATGTGCAACA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 974 of length 30]\n"
            + "CCAAGTGTACAGCCTAATCAGACCAAATGA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 891 of length 30]\n"
            + "CCTGCCGTAGCCAGTGGGTACGACTTTGAA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 936 of length 30]\n"
            + "CTAGTCGGAATAGACCCTTTCAGACTGCTT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1259 of length 30]\n"
            + "AATCAGCATACAACCTACGTTCTCAGTACA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 817 of length 30]\n"
            + "TTCTAGCACGGTCTGCACTCATATTGAGAG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1326 of length 30]\n"
            + "GCATTCAATGGGAATACAGAGGGAAGAACA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1296 of length 30]\n"
            + "CTCCCTTTTGACAGAACAACCATTATGGCA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1144 of length 30]\n"
            + "CCAATGAAAATATGGAGACTATGGAATCAA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1270 of length 30]\n"
            + "AACCTACGTTCTCAGTACAGAGAAATCTCC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1206 of length 30]\n"
            + "ATAAGGACCAGAAGTGGAGGAAACACCAAT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1082 of length 30]\n"
            + "CATCAAAGGGACGAAGGTGCTCCCAAGAGG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 34 of length 30]\n"
            + "ACATCAAAATCATGGCGTCCCAAGGCACCA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 375 of length 30]\n"
            + "TATGACAAAGAAGAAATAAGGCGAATCTGG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1456 of length 30]\n"
            + "CAGCGAGCCCGATCGTGCCTTCCTTTGACA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1115 of length 30]\n"
            + "GCTTTCCACTAGAGGAGTTCAAATTGCTTC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 532 of length 30]\n"
            + "TGTGCTCTCTGATGCAAGGTTCAACTCTCC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1004 of length 30]\n"
            + "GAATCCAGCACACAAGAGTCAACTGGTGTG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 968 of length 30]\n"
            + "AAACAGCCAAGTGTACAGCCTAATCAGACC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 70 of length 30]\n"
            + "CTTACGAACAGATGGAGACTGATGGAGAAC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 582 of length 30]\n"
            + "GGTGCTGCAGTCAAAGGAGTTGGAACAATG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1377 of length 30]\n"
            + "ATAAGGATGATGGAAAGTGCAAGACCAGAA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 285 of length 30]\n"
            + "GAACATCCCAGTGCGGGGAAAGATCCTAAG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 636 of length 30]\n"
            + "AAACGTGGGATCAATGATCGGAACTTCTGG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 340 of length 30]\n"
            + "GAGTAAACGGAAAGTGGATGAGAGAACTCA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1156 of length 30]\n"
            + "TGGAGACTATGGAATCAAGTACACTTGAAC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1191 of length 30]\n"
            + "AGCAGGTACTGGGCCATAAGGACCAGAAGT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1344 of length 30]\n"
            + "GAGGGAAGAACATCTGACATGAGGACCGAA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 118 of length 30]\n"
            + "TCAGAGCATCCGTCGGAAAAATGATTGGTG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1417 of length 30]\n"
            + "TCCAGGGGCGGGGAGTCTTCGAGCTCTCGG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1270 of length 30]\n"
            + "AACCTACGTTCTCAGTACAGAGAAATCTCC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1362 of length 30]\n"
            + "ATGAGGACCGAAATCATAAGGATGATGGAA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1243 of length 30]\n"
            + "GGGCATCTGCGGGCCAAATCAGCATACAAC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 908 of length 30]\n"
            + "GTACGACTTTGAAAGAGAGGGATACTCTCT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1104 of length 30]\n"
            + "CCAAGAGGGAAGCTTTCCACTAGAGGAGTT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1133 of length 30]\n"
            + "TCAAATTGCTTCCAATGAAAATATGGAGAC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1083 of length 30]\n"
            + "ATCAAAGGGACGAAGGTGCTCCCAAGAGGG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 674 of length 30]\n"
            + "GAATGGACGAAAAACAAGAATTGCTTATGA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1404 of length 30]\n"
            + "GAAGATGTGTCTTTCCAGGGGCGGGGAGTC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 297 of length 30]\n"
            + "GCGGGGAAAGATCCTAAGAAAACTGGAGGA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 833 of length 30]\n"
            + "ACTCATATTGAGAGGGTCGGTTGCTCACAA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1433 of length 30]\n"
            + "CTTCGAGCTCTCGGACGAAAAGGCAGCGAG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 732 of length 30]\n"
            + "TTTCAAACTGCTGCACAAAAAGCAATGATG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 34 of length 30]\n"
            + "ACATCAAAATCATGGCGTCCCAAGGCACCA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1466 of length 30]\n"
            + "GATCGTGCCTTCCTTTGACATGAGTAATGA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 80 of length 30]\n"
            + "GATGGAGACTGATGGAGAACGCCAGAATGC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1372 of length 30]\n"
            + "AAATCATAAGGATGATGGAAAGTGCAAGAC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1148 of length 30]\n"
            + "TGAAAATATGGAGACTATGGAATCAAGTAC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1303 of length 30]\n"
            + "TTGACAGAACAACCATTATGGCAGCATTCA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1012 of length 30]\n"
            + "CACACAAGAGTCAACTGGTGTGGATGGCAT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 946 of length 30]\n"
            + "TAGACCCTTTCAGACTGCTTCAAAACAGCC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 951 of length 30]\n"
            + "CCTTTCAGACTGCTTCAAAACAGCCAAGTG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1289 of length 30]\n"
            + "GAGAAATCTCCCTTTTGACAGAACAACCAT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 440 of length 30]\n"
            + "TCTGACTCACATGATGATCTGGCATTCCAA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 991 of length 30]\n"
            + "TCAGACCAAATGAGAATCCAGCACACAAGA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1267 of length 30]\n"
            + "TACAACCTACGTTCTCAGTACAGAGAAATC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 32 of length 30]\n"
            + "TGACATCAAAATCATGGCGTCCCAAGGCAC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1092 of length 30]\n"
            + "ACGAAGGTGCTCCCAAGAGGGAAGCTTTCC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 658 of length 30]\n"
            + "ACTTCTGGAGGGGTGAGAATGGACGAAAAA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 950 of length 30]\n"
            + "CCCTTTCAGACTGCTTCAAAACAGCCAAGT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 68 of length 30]\n"
            + "GTCTTACGAACAGATGGAGACTGATGGAGA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1467 of length 30]\n"
            + "ATCGTGCCTTCCTTTGACATGAGTAATGAA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 418 of length 30]\n"
            + "ATGGTGACGATGCAACGGCTGGTCTGACTC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1353 of length 30]\n"
            + "ACATCTGACATGAGGACCGAAATCATAAGG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 81 of length 30]\n"
            + "ATGGAGACTGATGGAGAACGCCAGAATGCC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 541 of length 30]\n"
            + "TGATGCAAGGTTCAACTCTCCCTAGGAGGT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1331 of length 30]\n"
            + "CAATGGGAATACAGAGGGAAGAACATCTGA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1035 of length 30]\n"
            + "ATGGCATGCCATTCTGCCGCATTTGAAGAT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1119 of length 30]\n"
            + "TCCACTAGAGGAGTTCAAATTGCTTCCAAT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 273 of length 30]\n"
            + "AAATACCTGGAAGAACATCCCAGTGCGGGG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1216 of length 30]\n"
            + "GAAGTGGAGGAAACACCAATCAACAGAGGG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1467 of length 30]\n"
            + "ATCGTGCCTTCCTTTGACATGAGTAATGAA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1406 of length 30]\n"
            + "AGATGTGTCTTTCCAGGGGCGGGGAGTCTT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1305 of length 30]\n"
            + "GACAGAACAACCATTATGGCAGCATTCAAT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 43 of length 30]\n"
            + "TCATGGCGTCCCAAGGCACCAAACGGTCTT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 611 of length 30]\n"
            + "GGTGATGGAATTGGTCAGGATGATCAAACG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 172 of length 30]\n"
            + "TGTGCACCGAACTCAAACTCAGTGATTATG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 655 of length 30]\n"
            + "GGAACTTCTGGAGGGGTGAGAATGGACGAA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 211 of length 30]\n"
            + "TGATCCAAAACAGCTTAACAATAGAGAGAA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 333 of length 30]\n"
            + "TACAGGAGAGTAAACGGAAAGTGGATGAGA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 639 of length 30]\n"
            + "CGTGGGATCAATGATCGGAACTTCTGGAGG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 390 of length 30]\n"
            + "ATAAGGCGAATCTGGCGCCAAGCTAATAAT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1034 of length 30]\n"
            + "GATGGCATGCCATTCTGCCGCATTTGAAGA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 276 of length 30]\n"
            + "TACCTGGAAGAACATCCCAGTGCGGGGAAA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1294 of length 30]\n"
            + "ATCTCCCTTTTGACAGAACAACCATTATGG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1455 of length 30]\n"
            + "GCAGCGAGCCCGATCGTGCCTTCCTTTGAC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 302 of length 30]\n"
            + "GAAAGATCCTAAGAAAACTGGAGGACCTAT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1507 of length 30]\n"
            + "TCTTCGGAGACAATGCAGAGGAGTACGACA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1089 of length 30]\n"
            + "GGGACGAAGGTGCTCCCAAGAGGGAAGCTT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1322 of length 30]\n"
            + "GGCAGCATTCAATGGGAATACAGAGGGAAG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1515 of length 30]\n"
            + "GACAATGCAGAGGAGTACGACAATTAAAGA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1456 of length 30]\n"
            + "CAGCGAGCCCGATCGTGCCTTCCTTTGACA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1483 of length 30]\n"
            + "ACATGAGTAATGAAGGATCTTATTTCTTCG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1473 of length 30]\n"
            + "CCTTCCTTTGACATGAGTAATGAAGGATCT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 68 of length 30]\n"
            + "GTCTTACGAACAGATGGAGACTGATGGAGA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 968 of length 30]\n"
            + "AAACAGCCAAGTGTACAGCCTAATCAGACC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 270 of length 30]\n"
            + "AATAAATACCTGGAAGAACATCCCAGTGCG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 227 of length 30]\n"
            + "AACAATAGAGAGAATGGTGCTCTCTGCTTT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1001 of length 30]\n"
            + "TGAGAATCCAGCACACAAGAGTCAACTGGT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 67 of length 30]\n"
            + "GGTCTTACGAACAGATGGAGACTGATGGAG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1165 of length 30]\n"
            + "TGGAATCAAGTACACTTGAACTGAGAAGCA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 5 of length 30]\n"
            + "AAGCAGGGTAGATAATCACTCACTGAGTGA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 922 of length 30]\n"
            + "GAGAGGGATACTCTCTAGTCGGAATAGACC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 261 of length 30]\n"
            + "GAAAGGAGAAATAAATACCTGGAAGAACAT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1071 of length 30]\n"
            + "GTATTAAGCTTCATCAAAGGGACGAAGGTG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 398 of length 30]\n"
            + "AATCTGGCGCCAAGCTAATAATGGTGACGA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 941 of length 30]\n"
            + "CGGAATAGACCCTTTCAGACTGCTTCAAAA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 190 of length 30]\n"
            + "TCAGTGATTATGAGGGACGGTTGATCCAAA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 254 of length 30]\n"
            + "TTTTGACGAAAGGAGAAATAAATACCTGGA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 230 of length 30]\n"
            + "AATAGAGAGAATGGTGCTCTCTGCTTTTGA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 914 of length 30]\n"
            + "CTTTGAAAGAGAGGGATACTCTCTAGTCGG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 963 of length 30]\n"
            + "CTTCAAAACAGCCAAGTGTACAGCCTAATC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1110 of length 30]\n"
            + "GGGAAGCTTTCCACTAGAGGAGTTCAAATT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 886 of length 30]\n"
            + "ATGGACCTGCCGTAGCCAGTGGGTACGACT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 293 of length 30]\n"
            + "CAGTGCGGGGAAAGATCCTAAGAAAACTGG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 153 of length 30]\n"
            + "GGACGATTCTACATCCAAATGTGCACCGAA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 592 of length 30]\n"
            + "TCAAAGGAGTTGGAACAATGGTGATGGAAT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1145 of length 30]\n"
            + "CAATGAAAATATGGAGACTATGGAATCAAG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 49 of length 30]\n"
            + "CGTCCCAAGGCACCAAACGGTCTTACGAAC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 244 of length 30]\n"
            + "TGCTCTCTGCTTTTGACGAAAGGAGAAATA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 904 of length 30]\n"
            + "GTGGGTACGACTTTGAAAGAGAGGGATACT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 772 of length 30]\n"
            + "GAGAGAGCCGGAACCCAGGGAATGCTGAGT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1307 of length 30]\n"
            + "CAGAACAACCATTATGGCAGCATTCAATGG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 329 of length 30]\n"
            + "TATATACAGGAGAGTAAACGGAAAGTGGAT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 213 of length 30]\n"
            + "ATCCAAAACAGCTTAACAATAGAGAGAATG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 106 of length 30]\n"
            + "ATGCCACTGAAATCAGAGCATCCGTCGGAA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 979 of length 30]\n"
            + "TGTACAGCCTAATCAGACCAAATGAGAATC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 477 of length 30]\n"
            + "GATGCAACTTATCAGAGGACAAGAGCTCTT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1292 of length 30]\n"
            + "AAATCTCCCTTTTGACAGAACAACCATTAT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 602 of length 30]\n"
            + "TGGAACAATGGTGATGGAATTGGTCAGGAT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1411 of length 30]\n"
            + "TGTCTTTCCAGGGGCGGGGAGTCTTCGAGC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 669 of length 30]\n"
            + "GGTGAGAATGGACGAAAAACAAGAATTGCT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1149 of length 30]\n"
            + "GAAAATATGGAGACTATGGAATCAAGTACA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 886 of length 30]\n"
            + "ATGGACCTGCCGTAGCCAGTGGGTACGACT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 863 of length 30]\n"
            + "GTCCTGCCTGCCTGCCTGTGTGTATGGACC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 109 of length 30]\n"
            + "CCACTGAAATCAGAGCATCCGTCGGAAAAA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1308 of length 30]\n"
            + "AGAACAACCATTATGGCAGCATTCAATGGG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 112 of length 30]\n"
            + "CTGAAATCAGAGCATCCGTCGGAAAAATGA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 470 of length 30]\n"
            + "TTTGAATGATGCAACTTATCAGAGGACAAG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1017 of length 30]\n"
            + "AAGAGTCAACTGGTGTGGATGGCATGCCAT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1023 of length 30]\n"
            + "CAACTGGTGTGGATGGCATGCCATTCTGCC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 621 of length 30]\n"
            + "TTGGTCAGGATGATCAAACGTGGGATCAAT\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1454 of length 30]\n"
            + "GGCAGCGAGCCCGATCGTGCCTTCCTTTGA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 829 of length 30]\n"
            + "CTGCACTCATATTGAGAGGGTCGGTTGCTC\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 19 of length 30]\n"
            + "ATCACTCACTGAGTGACATCAAAATCATGG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 1499 of length 30]\n"
            + "ATCTTATTTCTTCGGAGACAATGCAGAGGA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 604 of length 30]\n"
            + "GAACAATGGTGATGGAATTGGTCAGGATGA\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 366 of length 30]\n"
            + "CTCATCCTTTATGACAAAGAAGAAATAAGG\n"
            + ">ENA|AF389119|AF389119.1 Influenza A virus (A/Puerto Rico/8/34/Mount Sinai(H1N1)) segment 5, complete sequence. [cyclic subsequence 527 of length 30]\n"
            + "CAGGATGTGCTCTCTGATGCAAGGTTCAAC";
    private static final Path PATH = Paths.get("..", "testfiles", "AF389119");
    private static final Path OUTPUT_PATH = Paths.get("..", "testfiles", "output", "test.fa");

    private Loader loader;

    @Before
    public void construct() {
        loader = new FASTALoader(null);
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
        loader.writeReadBag(EXPECTED_BAG, bw);
        bw.flush();
        String[] values = new String[EXPECTED_BAG.size()];
        StringTokenizer st = new StringTokenizer(wr.getBuffer().toString(), "\n");
        for (int i = 0; i < EXPECTED_BAG.size(); i++) {
            assertThat(st.nextToken().charAt(0), is('>'));
            values[i] = st.nextToken();
        }
        assertFalse(st.hasMoreTokens());
        assertThat(ReadBag.fromString(values), is(equalTo(EXPECTED_BAG)));
    }

    @Test
    public void testWriteReadBagToFile() throws IOException {
        Files.deleteIfExists(OUTPUT_PATH);
        loader.writeReadBag(EXPECTED_BAG, OUTPUT_PATH);
        List<String> lines = Files.readAllLines(OUTPUT_PATH);
        assertThat(lines.size(), is(equalTo(EXPECTED_BAG.size() * 2)));
        String[] values = new String[EXPECTED_BAG.size()];
        for (int i = 0; i < EXPECTED_BAG.size(); i++) {
            assertThat(lines.get(i * 2).charAt(0), is('>'));
            values[i] = lines.get(i * 2 + 1);
        }
        assertThat(ReadBag.fromString(values), is(equalTo(EXPECTED_BAG)));
        Files.delete(OUTPUT_PATH);
    }

}
