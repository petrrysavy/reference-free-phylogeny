package rysavpe1.reads.combined;

import java.util.BitSet;
import java.util.Collection;
import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.ahocorasick.trie.Trie.TrieBuilder;
import org.ahocorasick.trie.handler.EmitHandler;
import rysavpe1.reads.model.ReadBag;
import rysavpe1.reads.model.ReadBagTuple;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.overlap.OverlapAdapter;

/**
 * This class can be used for filtering reads and contigs. First, contigs
 * shorter than read length are removed. Second, reads that are found in a
 * contig are removed.
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class TupleFilter {

    public final int readLength;

    public TupleFilter(int readLength) {
        this.readLength = readLength;
    }

    public ReadBagTuple filterTuple(ReadBagTuple tuple) {
        // filter too short contigs
        ReadBag contigs = tuple.getContigs();
        ReadBag newContigs = new ReadBag(contigs.size(), contigs.getDescription() + "/filtered", contigs.getFile());
        for (Sequence s : contigs)
            if (s.length() > readLength)
                newContigs.add(s);

        // use an Aho-Corasick implementation to remove reads that are parts of contigs
        ReadBag reads = tuple.getReads();
        ReadBag newReads = new ReadBag(reads.size(), reads.getDescription() + "/filtered", reads.getFile());
        newReads.addAll(reads);
        if (!newContigs.isEmpty()) {
            TrieBuilder trieBuilder = Trie.builder();
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
        return new ReadBagTuple(newContigs, newReads);
    }

    public int findNonOverlap(Collection<Sequence> target, Collection<? extends OverlapAdapter> regions, Sequence contig) {
        if (regions.isEmpty()) {
            if(contig.length() >= readLength)
                target.add(contig);
            return contig.length();
        }

        final BitSet mapped = new BitSet(contig.length());
        for (OverlapAdapter region : regions)
            mapped.set(region.getStart(), region.getEnd());

//        System.err.println("contig " + contig.length() + " in overlap " + mapped.cardinality());
//        System.err.println("contig "+contig.getSequenceString());
//        System.err.println("regions : "+regions);
//        System.err.println("mapped  : "+mapped);
        int nonOverlap = 0;
        int end = 0;
        while (end != contig.length()) {
            final int start = mapped.nextClearBit(end);
            if (start == -1) break;
            end = mapped.nextSetBit(start);
            if (end == -1) end = contig.length();

            nonOverlap += end - start;
            if ((end - start) < readLength) continue;
            target.add(contig.subSequence(start, end));
        }
        return nonOverlap;
    }
}
