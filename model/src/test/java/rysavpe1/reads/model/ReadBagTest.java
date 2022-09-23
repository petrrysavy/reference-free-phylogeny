/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rysavpe1.reads.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class ReadBagTest {

    @Test
    public void testEquals() {
        assertThat(ReadBag.fromString("AAAC", "CTTC", "AAAC"),
                is(equalTo(ReadBag.fromString("AAAC", "CTTC", "AAAC"))));
    }
    
    @Test
    public void testAllReadLength() {
        assertThat(ReadBag.fromString("12345", "123", "1234567").allReadsLength(),
                is(equalTo(15)));
    }

}
