package rysavpe1.reads.distance.simple;

import rysavpe1.reads.distance.AbstractMeasure;
import rysavpe1.reads.multiset.Multiset;

/**
 *
 * @author Petr Ryšavý
 */
public class CosineSimmilarityDistance extends AbstractMeasure<Multiset<?>>
{
    @Override
    public boolean isZeroOneNormalized() {
        return true;
    }

    @Override
    public Double getSimilarity(Multiset a, Multiset b)
    {
        if (a.isEmpty() && b.isEmpty())
            return 1.0;
        if (a.isEmpty() || b.isEmpty())
            return 0.0;

        double product = 0.0;
        double aSize2 = 0.0;
        double bSize2 = 0.0;

        for (Object s : a.union(b).toSet())
        {
            final int aCount = a.count(s);
            final int bCount = b.count(s);

            product += aCount * bCount;
            aSize2 += aCount * aCount;
            bSize2 += bCount * bCount;
        }

        return product / Math.sqrt(aSize2 * bSize2);
    }
}
