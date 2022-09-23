package rysavpe1.reads.cli;

import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.ahocorasick.trie.handler.EmitHandler;
import rysavpe1.reads.combined.multiprojected.model.CombinedInput;
import rysavpe1.reads.combined.multiprojected.model.QGramContigSet;
import rysavpe1.reads.embedded.EmbeddedReadBag;
import rysavpe1.reads.embedded.TripletsEmbedding;
import rysavpe1.reads.model.ReadBag;
import rysavpe1.reads.model.Sequence;

/**
 * This class is used to filter out reads that are redundant in the meaning that
 * they are already contained in a contig. Those reads are filtered out. Also,
 * contigs shorter than read length are not used further.
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class FilterRedundantReadsAndEmbedd {

    private final int readLength;

    private final TripletsEmbedding embedding;

    public FilterRedundantReadsAndEmbedd(int readLength) {
        this.readLength = readLength;
        this.embedding = new TripletsEmbedding();
    }

    public CombinedInput preprocessData(ReadBag reads, ReadBag contigs, int lengthEstimate) {
        // filter too short contigs
        ReadBag newContigs = new ReadBag(contigs.size(), contigs.getDescription() + "/filtered", contigs.getFile());
        for (Sequence s : contigs)
            if (s.length() > readLength)
                newContigs.add(s);

        // use an Aho-Corasick implementation to remove reads that are parts of contigs
        ReadBag newReads = new ReadBag(reads.size(), reads.getDescription() + "/filtered", reads.getFile());
        newReads.addAll(reads);
        if (!newContigs.isEmpty()) {
            Trie.TrieBuilder trieBuilder = Trie.builder();
            for (Sequence s : reads.toSet())
                trieBuilder.addKeyword(s.getSequenceString());
            Trie trie = trieBuilder.build();
            EmitHandler emitHandler = (Emit emit) -> {
                newReads.removeAllOccurences(Sequence.fromString(emit.getKeyword()));
                return true;
            };
            for (Sequence contig : newContigs)
                trie.parseText(contig.getSequenceString(), emitHandler);
        }
        return new CombinedInput(lengthEstimate,
                new EmbeddedReadBag<>(newReads, embedding),
                new QGramContigSet(newContigs));
    }

}
