package rysavpe1.reads.model;

import java.nio.file.Path;
import rysavpe1.reads.settings.Settings;
import rysavpe1.reads.multiset.HashMultiset;

/**
 * A representation of bag of reads. This is efficiently a multiset of sequences
 * and a description.
 *
 * @author Petr Ryšavý
 */
public class ReadBag extends HashMultiset<Sequence> {

    /** Description of the bag of reads. */
    private String description;
    /** File that stored the reads. */
    private Path file;

    /**
     * Constructs a new bag of reads.
     *
     * @param initialCapacity Initial capacity of the multiset.
     * @param description Description, a comment or {@code null}.
     */
    public ReadBag(int initialCapacity, String description) {
        this(initialCapacity, description, null);
    }

    /**
     * Constructs a new bag of reads.
     *
     * @param initialCapacity Initial capacity of the multiset.
     * @param description Description, a comment or {@code null}.
     * @param file Location of the reads.
     */
    public ReadBag(int initialCapacity, String description, Path file) {
        super(initialCapacity);
        this.description = description;
        this.file = file;
    }

    /**
     * Constructs a new bag of reads.
     *
     * @param initialCapacity Initial capacity of the multiset.
     */
    public ReadBag(int initialCapacity) {
        this(initialCapacity, null);
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Returns the description.
     *
     * @return Description.
     */
    public String getDescription() {
        return description;
    }

    public void setFile(Path file) {
        this.file = file;
    }

    /**
     * Gets the location of the bag of reads.
     * @return File that stores the data.
     */
    public Path getFile() {
        return file;
    }

    /**
     * Finds the longest sequence.
     *
     * @return The longest sequence or {@code null} if the bag is empty.
     */
    public Sequence longestSequence() {
        int maxLenght = -1;
        Sequence maximal = null;
        for (Sequence s : toSet())
            if (s.length() > maxLenght) {
                maximal = s;
                maxLenght = s.length();
            }
        return maximal;
    }

    /**
     * Constructs a bag of reads from string representation of sequences.
     *
     * @param sequences The sequences to be stored in the new reads bag.
     * @return A read bags that contains all the sequences.
     */
    public static ReadBag fromString(String... sequences) {
        final ReadBag bag = new ReadBag(sequences.length);
        for (String sequence : sequences)
            bag.add(Sequence.fromString(sequence));
        return bag;
    }

    public static ReadBag fromSequences(Sequence... sequences) {
        final ReadBag bag = new ReadBag(sequences.length);
        for (Sequence sequence : sequences)
            bag.add(sequence);
        return bag;
    }

    public static ReadBag fromSequence(Sequence sequence) {
        final ReadBag bag = new ReadBag(1);
        bag.add(sequence);
        bag.setDescription(sequence.getDescription());
        bag.setFile(sequence.getFile());
        return bag;
    }

    /**
     * Samples the bag of reads with the given ratio.
     * @param ratio The relative count of reads to include.
     * @return Bag of reads with the sample.
     */
    public ReadBag sample(double ratio) {
        final ReadBag sampled = new ReadBag((int) (this.size() * ratio) + 10, description + " sampled", file);
        for (Sequence s : this)
            if (Settings.RANDOM.nextDouble() < ratio)
                sampled.add(s);
        return sampled;
    }

    /**
     * {@inheritDoc}
     *
     * @return Description of the reads bag.
     */
    @Override
    public String toString() {
        return description;
    }

    /**
     * Returns a string that prints all elements of the set, not just a
     * description.
     * @return Textual representation of read bag contents.
     */
    public String elementsString() {
        return super.toString();
    }

    /**
     * Gets total length of all reads in this read set.
     * @return The overall length of data in this multiset.
     */
    public int allReadsLength() {
        int length = 0;
        for (Sequence s : this) length += s.length();
        return length;
    }
}
