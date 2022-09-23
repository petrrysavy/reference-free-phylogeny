package rysavpe1.reads.embedded;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;
import rysavpe1.reads.model.ReadBag;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.utils.IteratorWrapper;

/**
 * A reads bag which is embedded in some other space. Reads are represented as
 * embedded objects instead of strings.
 *
 * @author Petr Ryšavý
 * @param <T> The target space of the embedding.
 */
public class EmbeddedReadBag<T> extends AbstractCollection<Sequence> implements EmbeddedMultiset<EmbeddedSequence<T>, Sequence, T> {

    /** The original reads bag. */
    private final ReadBag bag;
    /** List of embedded objects. */
    private final List<EmbeddedSequence<T>> list;

    private EmbeddedReadBag(ReadBag bag, List<EmbeddedSequence<T>> list) {
        this.bag = bag;
        this.list = list;
    }

    /**
     * Creates new embedded readsbag based on function that calculates the
     * embedding.
     * @param bag Bag of sequences.
     * @param function The function that calculatees embedding.
     */
    public EmbeddedReadBag(ReadBag bag, EmbeddingFunction<Sequence, T> function) {
        this.bag = bag;
        // build the unmodifiable view of the reads bag that contains the embedding
        List<EmbeddedSequence<T>> tmp = new ArrayList<>(bag.uniqueSize());
        for (Sequence s : bag.toSet())
            tmp.add(new EmbeddedSequence<>(s, function));
        list = Collections.unmodifiableList(tmp);
    }

    @Override
    public Iterator<Sequence> iterator() {
        return bag.iterator();
    }

    @Override
    public Iterator<EmbeddedSequence<T>> embeddedIterator() {
        return list.iterator();
    }

    @Override
    public int size() {
        return bag.size();
    }

    @Override
    public int uniqueSize() {
        return bag.uniqueSize();
    }

    @Override
    public int count(Object o) {
        return bag.count(o);
    }

    /**
     * Gets the original reads bag.
     * @return The original reads bag.
     */
    public ReadBag getBag() {
        return bag;
    }

    public List<EmbeddedSequence<T>> toList() {
        return list;
    }

    public EmbeddedReadBag<T> copy() {
        ReadBag newReads = new ReadBag(bag.size(), bag.getDescription(), bag.getFile());
        newReads.addAll(bag);

        return new EmbeddedReadBag<>(newReads, list);
    }

    public EmbeddedReadBag<T> filter(Predicate<EmbeddedSequence<T>> predicate) {
        final ReadBag newBag = new ReadBag(bag.size() / 2, bag.getDescription() + "/" + predicate, bag.getFile());
        final List<EmbeddedSequence<T>> tmp = new ArrayList<>(bag.uniqueSize() / 2);

        for (EmbeddedSequence<T> seq : new IteratorWrapper<>(embeddedIterator()))
            if (predicate.test(seq)) {
                final Sequence seqS = seq.getObject();
                newBag.add(seqS, bag.count(seqS));
                tmp.add(seq);
            }

        return new EmbeddedReadBag<>(newBag, Collections.unmodifiableList(tmp));
    }

}
