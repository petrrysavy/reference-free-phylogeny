package rysavpe1.reads.combined.embedded;

import java.util.HashSet;
import java.util.Set;
import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.ahocorasick.trie.Trie.TrieBuilder;
import org.ahocorasick.trie.handler.EmitHandler;
import rysavpe1.reads.combined.TupleFilter;
import rysavpe1.reads.embedded.EmbeddedReadBag;
import rysavpe1.reads.embedded.EmbeddedReadBagTuple;
import rysavpe1.reads.model.Sequence;

/**
 * This class can be used for filtering reads and contigs.First, contigs shorter
 * than read length are removed.Second, reads that are found in a contig are
 * removed.
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 * @param <T> contigs embedding space
 * @param <U> reads embedding space
 */
public class EmbeddedTupleFilter<T, U> extends TupleFilter {

//    private final EmbeddingFunction<Sequence, U> readsEmbedding;
//    private final EmbeddingFunction<Sequence, T> contigsEmbedding;

    public EmbeddedTupleFilter(int readLength/*, EmbeddingFunction<Sequence, U> readsEmbedding,
            EmbeddingFunction<Sequence, T> contigsEmbedding*/) {
        super(readLength);
//        this.readsEmbedding = readsEmbedding;
//        this.contigsEmbedding = contigsEmbedding;
    }

    public EmbeddedReadBagTuple<T, U> filterTuple(EmbeddedReadBagTuple<T, U> tuple) {
        // filter too short contigs
        EmbeddedReadBag<T> newContigs = tuple.getContigs().filter(s -> s.getObject().length() > readLength);

        // use an Aho-Corasick implementation to remove reads that are parts of contigs
        EmbeddedReadBag<U> reads = tuple.getReads();
        if (newContigs.isEmpty())
            return new EmbeddedReadBagTuple<>(newContigs, reads.copy());

        TrieBuilder trieBuilder = Trie.builder();
        for (Sequence s : reads.getBag().toSet())
            trieBuilder.addKeyword(s.getSequenceString());
        Trie trie = trieBuilder.build();
        Set<String> mappedReads = new HashSet<>(reads.size());
        EmitHandler emitHandler = (Emit emit) -> {
                mappedReads.add(emit.getKeyword());
                return true;
            };
        for (Sequence contig : newContigs)
                trie.parseText(contig.getSequenceString(), emitHandler);
        reads = reads.filter(s -> !mappedReads.contains(s.getObject().getSequenceString()));

        return new EmbeddedReadBagTuple<>(newContigs, reads);
    }
}
