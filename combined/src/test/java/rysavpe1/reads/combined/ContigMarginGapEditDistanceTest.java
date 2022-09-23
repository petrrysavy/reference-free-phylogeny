/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rysavpe1.reads.combined;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import rysavpe1.reads.margingap.LinearMarginGapPenalty;
import rysavpe1.reads.model.Sequence;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class ContigMarginGapEditDistanceTest {
    private ContigMarginGapEditDistance editDistance;
    
    @Before
    public void init() {
        editDistance = new ContigMarginGapEditDistance(0, 1, 1, new LinearMarginGapPenalty(2.0, 5, 2));
    }

    @Test
    public void testGetDistance1() {
        assertThat(editDistance.getDistance(Sequence.fromString("ABCD12"), Sequence.fromString("BCD12")), is(equalTo(0.0)));
    }

    @Test
    public void testGetDistance2() {
        assertThat(editDistance.getDistance(Sequence.fromString("ABCD12"), Sequence.fromString("AXCD1")), is(equalTo(1.0)));
    }

    
}
