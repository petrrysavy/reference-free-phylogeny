package rysavpe1.reads.io.utils;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import rysavpe1.reads.bio.Nucleotides;
import static rysavpe1.reads.bio.Nucleotides.NUCLEOTIDES;
import static rysavpe1.reads.bio.Nucleotides.NUCLEOTIDES_NOT_A;
import static rysavpe1.reads.bio.Nucleotides.NUCLEOTIDES_NOT_C;
import static rysavpe1.reads.bio.Nucleotides.NUCLEOTIDES_NOT_G;
import static rysavpe1.reads.bio.Nucleotides.NUCLEOTIDES_NOT_T;
import rysavpe1.reads.settings.Settings;

/**
 *
 * @author petr
 */
final class SequenceCheckers {

    /**
     * Checks that the sequence is in FASTA format and makes sure that only
     * A/T/C/G are present. If not, the unknown value is randomly generated.
     * @param sequence The original sequence.
     * @return The same sequence with only A/T/C/G and for example N replaced
     * with a random nucleotide.
     */
    public static char[] checkFASTASequence(char[] sequence) {
        for (int i = 0; i < sequence.length; i++) {
            switch (sequence[i]) {
                // do nothing on nucleotides
                case 'A':
                case 'T':
                case 'C':
                case 'G':
                    continue;
                default:
                    Logger.getLogger(Nucleotides.class.getName()).log(Level.INFO, "found unknown nucleotide {0}", sequence[i]);
                    return null;
            }
        }
        return sequence;
    }

    /**
     * Checks that the sequence is in FASTA format and makes sure that only
     * A/T/C/G are present. If not, the unknown value is randomly generated.
     * @param sequence The original sequence.
     * @return The same sequence with only A/T/C/G and for example N replaced
     * with a random nucleotide.
     */
    public static char[] derandomizeFASTASequence(char[] sequence) {
        for (int i = 0; i < sequence.length; i++) {
            switch (sequence[i]) {
                // do nothing on nucleotides
                case 'A':
                case 'T':
                case 'C':
                case 'G':
                    continue;
                default:
                    Logger.getLogger(Nucleotides.class.getName()).log(Level.INFO, "found unknown nucleotide {0}", sequence[i]);
                    sequence[i] = derandomizeNucleotide(sequence[i], Settings.RANDOM);
                    Logger.getLogger(Nucleotides.class.getName()).log(Level.INFO, "... replaced with {0}", sequence[i]);
            }
        }
        return sequence;
    }

    /**
     * For an unknown symbol generates a nucleotide based on random decision.
     * @param ch Char to replace with a nucleotide.
     * @param r The random number generator.
     * @return One of A/T/C/G.
     */
    public static char derandomizeNucleotide(char ch, Random r) {
        switch (ch) {
            case 'U':
                return 'T';
            case 'R':
                return r.nextBoolean() ? 'A' : 'G';
            case 'Y':
                return r.nextBoolean() ? 'C' : 'T';
            case 'K':
                return r.nextBoolean() ? 'G' : 'T';
            case 'M':
                return r.nextBoolean() ? 'A' : 'C';
            case 'S':
                return r.nextBoolean() ? 'G' : 'C';
            case 'W':
                return r.nextBoolean() ? 'A' : 'T';
            case 'B':
                return NUCLEOTIDES_NOT_A[r.nextInt(NUCLEOTIDES_NOT_A.length)];
            case 'D':
                return NUCLEOTIDES_NOT_C[r.nextInt(NUCLEOTIDES_NOT_C.length)];
            case 'H':
                return NUCLEOTIDES_NOT_G[r.nextInt(NUCLEOTIDES_NOT_G.length)];
            case 'V':
                return NUCLEOTIDES_NOT_T[r.nextInt(NUCLEOTIDES_NOT_T.length)];
            case 'N':
                return NUCLEOTIDES[r.nextInt(NUCLEOTIDES.length)];
            default:
                throw new IllegalArgumentException("Unknown nucleotide : " + ch);
        }
    }
}
