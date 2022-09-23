package rysavpe1.reads.model;

import java.util.Random;
import rysavpe1.reads.bio.Nucleotides;

/**
 *
 * @author Petr Ryšavý
 */
public class RandomGenerators {

    private RandomGenerators() {
    }

    public static ReadBag generateReadBag(int bagSize, int readLength, long seed) {
        final ReadBag bag = new ReadBag(bagSize);
        final Random rand = new Random(seed);
        for (int i = 0; i < bagSize; i++) {
            // generate new string
            final char[] read = new char[readLength];
            for (int j = 0; j < readLength; j++)
                read[j] = Nucleotides.NUCLEOTIDES[rand.nextInt(Nucleotides.NUCLEOTIDES.length)];
            bag.add(new Sequence(read, "Random read " + i));
        }
        return bag;
    }

    public static Sequence generateRandomSequence(int length, Random random) {
        final char[] read = new char[length];
        for (int j = 0; j < length; j++)
            read[j] = Nucleotides.NUCLEOTIDES[random.nextInt(Nucleotides.NUCLEOTIDES.length)];
        return new Sequence(read, "Random read");
    }

}
