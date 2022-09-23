package rysavpe1.reads.margingap;

/**
 * This method implements linear border gap penaulty. The gap penaulty is zero for
 * any x from interval [0, a], then linearly increases on interval [a, b] and is
 * maximal on interval [b, 1]. The x value represents distance of x from the
 * beginning of the word.
 *
 * @author Petr Ryšavý
 */
public class LinearBorderGapPenaulty implements WeakBorderGapPenaulty
{
    private final double a;
    private final double b;
    private final double maximalGapPenaulty;

    public LinearBorderGapPenaulty(double a, double b, double maximalGapPenaulty)
    {
        this.a = a;
        this.b = b;
        this.maximalGapPenaulty = maximalGapPenaulty;
    }

    @Override
    public double getBorderGapPenaulty(int gapIndex, int wordLength)
    {
        if(gapIndex < 0 || gapIndex >= wordLength)
            throw new IllegalArgumentException();
        
        final double x = (gapIndex + 0.5) / (wordLength);
        if (x <= a)
            return 0;
        else if (x >= b)
            return maximalGapPenaulty;
        else
            return maximalGapPenaulty * (x - a) / (b - a);
    }
}
