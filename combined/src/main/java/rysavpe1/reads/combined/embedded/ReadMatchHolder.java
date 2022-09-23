package rysavpe1.reads.combined.embedded;

import rysavpe1.reads.combined.embedded.ReadMatchHolder.Match;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.overlap.ReadInContig;
import rysavpe1.reads.utils.TopNKeys;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class ReadMatchHolder extends TopNKeys<Match> {

    public ReadMatchHolder(int count) {
        super(count, true);
    }

    public boolean put(int key, Sequence read, boolean reversed, boolean complemented) {
        return super.put(key, new ReadMatch(read, reversed, complemented));
    }
    
    public boolean put(int key, ReadInContig readInContig, Sequence contig, boolean contigReaversed, boolean contigComplemented) {
        return super.put(key, new ContigMatch(readInContig, contig, contigReaversed, contigComplemented));
    }
    
//    public boolean put(int key, Sequence read, ReadInContig readInContig, Sequence contig, boolean contigReaversed, boolean contigComplemented) {
//        return super.put(key, new ContigAndReadMatch(read, readInContig, contig, contigReaversed, contigComplemented));
//    }
    

    public interface Match {

        public Sequence getSequence();

        public boolean isMarginDistance();
        
//        public default Sequence getOtherSequence() {
//            throw new UnsupportedOperationException("Not available.");
//        }
    }
    
    private class ReadMatch implements Match {
        private final Sequence sequence;
        private final boolean reversed;
        private final boolean complemented;

        public ReadMatch(Sequence sequence, boolean reversed, boolean complemented) {
            this.sequence = sequence;
            this.reversed = reversed;
            this.complemented = complemented;
        }

        @Override
        public Sequence getSequence() {
            Sequence sequenceRC = reversed ? sequence.reverse() : sequence;
            return complemented ? sequenceRC.complement() : sequenceRC;
        }

        @Override
        public boolean isMarginDistance() {
            return true;
        }
    }
    
    private class ContigMatch implements Match {
        private final ReadInContig readInContig;
        private final Sequence contig;
        private final boolean contigReversed;
        private final boolean contigComplemented;

        public ContigMatch(ReadInContig readInContig, Sequence contig, boolean contigReversed, boolean contigComplemented) {
            this.readInContig = readInContig;
            this.contig = contig;
            this.contigReversed = contigReversed;
            this.contigComplemented = contigComplemented;
        }

        @Override
        public Sequence getSequence() {
            Sequence contigRC = contigReversed ? contig.reverse() : contig;
            if(contigComplemented) contigRC = contigRC.complement();
            return contigRC.subSequence(readInContig.getContigStart(), readInContig.getContigEnd());
        }

        @Override
        public boolean isMarginDistance() {
            return readInContig.getContigStart() == 0 || readInContig.getContigEnd() == contig.length();
        }
    }
    
//    private class ContigAndReadMatch extends ContigMatch {
//        private final Sequence read;
//
//        public ContigAndReadMatch(Sequence read, ReadInContig readInContig, Sequence contig,
//                boolean contigReversed, boolean contigComplemented) {
//            super(readInContig, contig, contigReversed, contigComplemented);
//            this.read = read;
//        }
//
//        @Override
//        public Sequence getOtherSequence() {
//            return read;
//        }
//    }
}
