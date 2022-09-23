package rysavpe1.reads.model;

/**
 * Definition of a sequence that does not have orientation. In DNA sequence we
 * do not know strand or orientation from 5' end to 3' end.
 *
 * @author Petr Ryšavý
 * @param <T> Type of the class. Should be the subclass itself.
 */
public interface UnorientedObject<T extends UnorientedObject<T>> {

    /**
     * Gets the reverse. Reverse contains symbols from the end to the beginning.
     * @return The reverse.
     */
    public T reverse();

    /**
     * Gets the complement. Complement has A and T nucleotides switched.
     * Similarly Gs and Cs are switched.
     * @return The complementary sequence.
     */
    public T complement();

    /**
     * Combination of reverse and complement. This is how read looks like if it
     * is on the second strand.
     * @return Reversed complement.
     */
    public default T reverseComplement() {
        return reverse().complement();
    }

}
