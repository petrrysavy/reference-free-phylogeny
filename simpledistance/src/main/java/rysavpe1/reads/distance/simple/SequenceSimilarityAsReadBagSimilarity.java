package rysavpe1.reads.distance.simple;

import rysavpe1.reads.distance.AbstractMeasure;
import rysavpe1.reads.model.ReadBag;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.multiset.Multiset;

/**
 * This is just an adapter class that is used if we need to use a measure
 * between multisets where distance of reads bags would be expected.
 *
 * @author Petr Ryšavý
 */
public class SequenceSimilarityAsReadBagSimilarity extends AbstractMeasure<ReadBag>
{
    private final AbstractMeasure<Multiset<Sequence>> me;

    public SequenceSimilarityAsReadBagSimilarity(AbstractMeasure<Multiset<Sequence>> me)
    {
        this.me = me;
    }

    @Override
    public Double getSimilarity(ReadBag a, ReadBag b)
    {
        throw new RuntimeException();
    }

    @Override
    public Double getDistance(ReadBag a, ReadBag b)
    {
        return me.getDistance(a, b);
    }
}
