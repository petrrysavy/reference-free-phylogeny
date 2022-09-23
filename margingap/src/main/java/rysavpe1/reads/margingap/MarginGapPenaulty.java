
package rysavpe1.reads.margingap;

/**
 * Class that defines distance for gaps that span over margins. This penalty is
 * used in Levenshtein distance.
 * @author Petr Ryšavý
 */
public interface MarginGapPenaulty {

    /**
     * Precalculates penalty for the continuing margin gap.
     * @param readLength Length of the string that is compared.
     * @return Penalty for i-th gap.
     */
    public double[] build(int readLength);
}

