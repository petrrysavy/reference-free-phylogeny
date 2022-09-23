package rysavpe1.reads.distance.simple;

import rysavpe1.reads.distance.AbstractMeasure;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.multiset.Multiset;

/**
 *
 * @author Petr Ryšavý
 * @param <T>
 */
public class CountCharactersDistance<T extends Multiset<Sequence>> extends AbstractMeasure<T> {

    @Override
    public Double getSimilarity(T a, T b) {
        throw new UnsupportedOperationException("not normalized");
    }

    @Override
    public Double getDistance(T a, T b) {
        final int aCount = countCharacters(a);
        final int bCount = countCharacters(b);
        return new Double(Math.max(aCount, bCount));
    }

    private int countCharacters(T a) {
        int count = 0;
        for(Sequence s : a)
            count += s.length();
        return count;
    }
    
}
