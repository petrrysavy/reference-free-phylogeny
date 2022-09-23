package rysavpe1.reads.embedded;

import rysavpe1.reads.utils.Pair;

/**
 *
 * @author petr
 */
public final class EmbeddedReadBagTuple<T, U> extends Pair<EmbeddedReadBag<T>, EmbeddedReadBag<U>> {
    
    public EmbeddedReadBagTuple(EmbeddedReadBag<T> contigs, EmbeddedReadBag<U> reads) {
        super(contigs, reads);
    }
    
    public EmbeddedReadBag<T> getContigs() {
        return value1;
    }
    
    public EmbeddedReadBag<U> getReads() {
        return value2;
    }
}
