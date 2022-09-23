/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rysavpe1.reads.overlap;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public interface ReadInContig extends OverlapRegion {
    @Override
    public default int getLengthA() {
        return getReadLength();
    }
    
    @Override
    public default int getStartA() {
        return 0;
    }
    
    public default int getReadStart() {
        return 0;
    }
    
    @Override
    public default int getEndA() {
        return getReadLength();
    }
    
    public default int getReadEnd() {
        return getReadLength();
    }
    
    @Override
    public default int getStartB() {
        return getContigStart();
    }
    
    @Override
    public default int getEndB() {
        return getContigEnd();
    }
    
    @Override
    public default int getLengthB() {
        return getContigLength();
    }
    
    public default int getContigLength() {
        return getContigEnd() - getContigStart();
    }

    @Override
    public default OverlapRegion swapSequences() {
        throw new UnsupportedOperationException();
    }
    
    public int getReadLength();
    
    public int getContigStart();
    
    public int getContigEnd();
}
