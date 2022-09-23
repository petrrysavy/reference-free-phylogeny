package rysavpe1.reads.combined.multiprojected.model;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import rysavpe1.reads.embedded.EmbeddedSequence;
import rysavpe1.reads.model.ReadBag;
import rysavpe1.reads.model.Sequence;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class QGramContigSet extends AbstractCollection<Sequence> {

    /** The original reads bag with contigs as they are. */
    private final ReadBag bag;
    /** List of the embedded contigs. */
    private final List<QGramContig> list;

    private QGramContigSet(ReadBag bag, List<QGramContig> list) {
        this.bag = bag;
        this.list = list;
    }

    /**
     * Creates new embedded readsbag using the Q-grams embedding.
     * @param bag Bag of sequences.
     */
    public QGramContigSet(ReadBag bag) {
        this.bag = bag;
        // build the unmodifiable view of the reads bag that contains the embedding
        List<QGramContig> tmp = new ArrayList<>(bag.uniqueSize());
        for (Sequence s : bag.toSet())
            tmp.add(new QGramContig(s));
        list = Collections.unmodifiableList(tmp);
    }

    @Override
    public Iterator<Sequence> iterator() {
        return bag.iterator();
    }

    @Override
    public int size() {
        return bag.size();
    }

    public Iterator<QGramContig> embeddedIterator() {
        return list.iterator();
    }

    public List<QGramContig> toList() {
        return list;
    }
}
