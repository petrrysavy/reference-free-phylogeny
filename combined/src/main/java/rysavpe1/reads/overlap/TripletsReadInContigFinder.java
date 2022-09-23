package rysavpe1.reads.overlap;

import java.util.ArrayList;
import java.util.List;
import rysavpe1.reads.combined.embedded.ManhattanTripletsMatcher;
import rysavpe1.reads.combined.multiprojected.model.QGramContig;
import rysavpe1.reads.embedded.EmbeddedSequence;
import rysavpe1.reads.utils.MathUtils;
import rysavpe1.reads.utils.TopNKeys;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class TripletsReadInContigFinder implements ReadFinder<int[], int[]> {

    private final ManhattanTripletsMatcher matcher = new ManhattanTripletsMatcher();
    
    @Override
    public ReadInContig findReadInContig(EmbeddedSequence<int[]> read, EmbeddedSequence<int[]> contig) {
        return findReadInContig(read.getProjected(), contig.getProjected(), read.getObject().length());
    }
    
    @Override
    public List<ReadInContig> findMultipleMatches(EmbeddedSequence<int[]> read, EmbeddedSequence<int[]> contig, int sensitivity) {
        return findMultipleMatches(read.getProjected(), contig.getProjected(), read.getObject().length(), sensitivity);
    }
    
    @Override
    public ReadInContig findReadInContig(EmbeddedSequence<int[]> read, QGramContig contig) {
        return findReadInContig(read.getProjected(), contig.indexVector(3), read.getObject().length());
    }

    @Override
    public List<ReadInContig> findMultipleMatches(EmbeddedSequence<int[]> read, QGramContig contig, int sensitivity) {
        return findMultipleMatches(read.getProjected(), contig.indexVector(3), read.getObject().length(), sensitivity);
    }
    
    public ReadInContig findReadInContig(int[] read, int[] contigIndexVector, int readLength) {
        final int[] distances = matcher.distancesVectorSubstring(read, contigIndexVector, readLength);
        final int minIndex = MathUtils.minIndex(distances);
        return new SimpleReadInContig(readLength, minIndex, minIndex + readLength, distances[minIndex]);
    }
    
    public List<ReadInContig> findMultipleMatches(int[] read, int[] contigIndexVector, int readLength, int sensitivity) {
        final int[] distances = matcher.distancesVectorSubstring(read, contigIndexVector, readLength);
        final TopNKeys<Integer> keys = new TopNKeys<>(sensitivity, true);
        for(int i = 0; i < distances.length; i++)
            keys.put(distances[i], i);
        final ArrayList<ReadInContig> retval = new ArrayList<>(keys.size());
        for(int i : keys)
            retval.add(new SimpleReadInContig(readLength, i, i+readLength, distances[i]));
        return retval;
    }
    
}
