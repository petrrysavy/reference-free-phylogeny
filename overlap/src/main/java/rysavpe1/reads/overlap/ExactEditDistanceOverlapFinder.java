package rysavpe1.reads.overlap;

import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.utils.ArrayUtils;
import rysavpe1.reads.utils.MathUtils;

/**
 * This is an exact version of the overlap finder that tries to find the overlap
 * of two sequences such that the resulting post-normalized edit distance is
 * minimized. This class uses the exact version running in {@code O(n**3)},
 * where {@code n} is the length of the sequences. First we notice that if one
 * sequence starts in the middle, the second must start at the beginning.
 * Therefore we have {@code 2n} tables to fill, each with {@code n**2} entries.
 * We iterate over all possible initial positions of the alignment and then we
 * calculate the edit distance table and find the exact minimum.
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class ExactEditDistanceOverlapFinder implements OverlapFinder {

    private final int minLength;

    public ExactEditDistanceOverlapFinder(int minLength) {
        this.minLength = minLength;
    }

    @Override
    public OverlapRegion getOverlap(Sequence a, Sequence b) {
        final char[] aSeq = a.getSequence();
        final char[] bSeq = b.getSequence();

        OverlapRegion best = DummyOverlapRegion.DUMMY;
        for(int i = 0; i <= aSeq.length - minLength; i++) {
            OverlapRegion found = getOverlap(aSeq, bSeq, i);
            if(found.distanceToLengthRatio() < best.distanceToLengthRatio())
                best = found;
        }
        for(int i = 0; i <= bSeq.length - minLength; i++) {
            OverlapRegion found = getOverlap(bSeq, aSeq, i);
            if(found.distanceToLengthRatio() < best.distanceToLengthRatio())
                best = new SimpleOverlapRegion(found.getDistance(), found.getLengthB(),
                        found.getEndB(), found.getLengthA(), found.getEndA());
        }
        return best == DummyOverlapRegion.DUMMY ? null : best;
    }

    private OverlapRegion getOverlap(char[] aSeq, char[] bSeq, int aSeqOffset) {
        if(aSeq.length - aSeqOffset < minLength || bSeq.length < minLength)
            return DummyOverlapRegion.DUMMY;
        
        int[] last = ArrayUtils.identity(bSeq.length + 1);
        int[] current = new int[last.length];
        int[] swap;
        OverlapRegion region = DummyOverlapRegion.DUMMY;

        for (int i = aSeqOffset; i < aSeq.length; i++) {
            current[0] = last[0] + 1;
            for (int j = 0; j < bSeq.length; j++)
                current[j + 1] = MathUtils.min(
                        current[j] + 1,
                        last[j + 1] + 1,
                        last[j] + (aSeq[i] == bSeq[j] ? 0 : 1)
                );

            final int lenMax = Math.max(bSeq.length, i - aSeqOffset + 1);
            if (((double) current[bSeq.length]) / lenMax < region.distanceToLengthRatio()
                    /*explicit:&& bSeq.length >= minLength*/ && i-aSeqOffset + 1 >= minLength)
                region = new SimpleOverlapRegion(current[bSeq.length], i - aSeqOffset + 1, i + 1, bSeq.length, bSeq.length);
            swap = current;
            current = last;
            last = swap;
        }

        for (int j = minLength; j < bSeq.length; j++)
            if (((double) last[j]) / Math.max(aSeq.length - aSeqOffset, j) < region.distanceToLengthRatio()
                    /*explicit: && aSeq.length - aSeqOffset >= minLength && j >= minLength*/)
                region = new SimpleOverlapRegion(last[j], aSeq.length - aSeqOffset, aSeq.length, j, j);

        return region;
    }

}
