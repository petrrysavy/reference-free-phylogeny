package rysavpe1.reads.overlap;

import java.util.List;
import rysavpe1.reads.combined.multiprojected.model.QGramContig;
import rysavpe1.reads.embedded.EmbeddedSequence;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public interface ReadFinder<T, U> {
    public ReadInContig findReadInContig(EmbeddedSequence<T> read, EmbeddedSequence<U> contig);
    
    public List<ReadInContig> findMultipleMatches(EmbeddedSequence<int[]> read, EmbeddedSequence<int[]> contig, int sensitivity);
    
    public ReadInContig findReadInContig(EmbeddedSequence<T> read, QGramContig contig);
    
    public List<ReadInContig> findMultipleMatches(EmbeddedSequence<int[]> read, QGramContig contig, int sensitivity);
}
