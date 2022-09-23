package rysavpe1.reads.combined.multiprojected.model;

import rysavpe1.reads.embedded.EmbeddedReadBag;
import rysavpe1.reads.utils.Pair;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class CombinedInput extends Pair<EmbeddedReadBag<int[]>, QGramContigSet> {

    private final int sequenceLengthEstimate;

    public CombinedInput(int sequenceLengthEstimate, EmbeddedReadBag<int[]> reads, QGramContigSet contigs) {
        super(reads, contigs);
        this.sequenceLengthEstimate = sequenceLengthEstimate;
    }

    public EmbeddedReadBag<int[]> getReads() {
        return value1;
    }

    public QGramContigSet getContigs() {
        return value2;
    }

    public int getSequenceLengthEstimate() {
        return sequenceLengthEstimate;
    }

}
