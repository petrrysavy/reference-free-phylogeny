package rysavpe1.reads.model;

import rysavpe1.reads.utils.Tuple;

/**
 *
 * @author petr
 */
public final class ReadBagTuple extends Tuple<ReadBag> {
    
    public ReadBagTuple(ReadBag contigs, ReadBag reads) {
        super(contigs, reads);
    }
    
    public ReadBag getContigs() {
        return value1;
    }
    
    public ReadBag getReads() {
        return value2;
    }
}
