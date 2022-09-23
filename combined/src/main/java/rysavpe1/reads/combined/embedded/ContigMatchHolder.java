package rysavpe1.reads.combined.embedded;

import java.util.Collections;
import java.util.Iterator;
import rysavpe1.reads.combined.embedded.ContigMatchHolder.Match;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.overlap.ReadInContig;
import rysavpe1.reads.utils.TopNList;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 * @param <T>
 */
public class ContigMatchHolder extends TopNList<Match> {

    public ContigMatchHolder(int capacity) {
        super(capacity);
    }

    public boolean add(ReadInContig readInContig, Sequence read, boolean readReversed, boolean readComplemented, Sequence contig) {
//        System.err.println(super.toString());
        return super.add(new Match(readInContig, read, readReversed, readComplemented, contig));
    }
    
//    public boolean put(int key, Sequence read, ReadInContig readInContig, Sequence contig, boolean contigReaversed, boolean contigComplemented) {
//        return super.put(key, new ContigAndReadMatch(read, readInContig, contig, contigReaversed, contigComplemented));
//    }
    public final class Match implements Comparable<Match> {

        private final ReadInContig readInContig;
        private final Sequence read;
        private final boolean readReversed;
        private final boolean readComplemented;
        private final boolean marginDistance;

        public Match(ReadInContig readInContig, Sequence read, boolean readReversed, boolean readComplemented, Sequence contig) {
            this.readInContig = readInContig;
            this.read = read;
            this.readReversed = readReversed;
            this.readComplemented = readComplemented;
            this.marginDistance = readInContig.getContigStart() == 0 || readInContig.getContigEnd() == contig.length();
        }

        public Sequence getSequence() {
            Sequence readRC = readReversed ? read.reverse() : read;
            return readComplemented ? readRC.complement() : readRC;
        }
        
        public Sequence getContigPart(Sequence contig) {
            return contig.subSequence(readInContig.getContigStart(), readInContig.getContigEnd());
        }

        public boolean isMarginDistance() {
            return marginDistance;
        }

        @Override
        public int compareTo(Match o) {
            return Double.compare(o.readInContig.getDistance(), this.readInContig.getDistance());
        }
        
        @Override
        public String toString() {
            return "match "+readInContig.getDistance();
        }
    }

    public static final class Empty extends ContigMatchHolder {
        
        public static ContigMatchHolder.Empty INSTANCE = new Empty();

        private Empty() {
            super(1);
        }

        @Override
        public Iterator<Match> iterator() {
            return Collections.emptyIterator();
        }

        @Override
        public boolean add(Match m) {
            return false;
        }

        @Override
        public boolean add(ReadInContig readInContig, Sequence read, boolean readReversed, boolean readComplemented, Sequence contig) {
            return false;
        }

    }
}
