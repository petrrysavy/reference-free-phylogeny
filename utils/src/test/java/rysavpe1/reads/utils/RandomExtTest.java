/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rysavpe1.reads.utils;

import java.util.Arrays;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class RandomExtTest {
    
    public RandomExtTest() {
    }

    @Test
    public void nDistinctIntegersNonMinusOne() {
        final int[] arr = new RandomExt().nDistinctIntegers(100, 200);
        System.err.println(Arrays.toString(arr));
        assertThat(arr.length, is(equalTo(100)));
        assertThat(CollectionUtils.asBoxedList(arr), everyItem(is(lessThan(200))));
        assertThat(CollectionUtils.asBoxedList(arr), everyItem(is(greaterThan(-1))));
    }
    
    @Test
    public void testNBitsSet() {
        assertThat(new RandomExt().nBitsSet(0, 200).cardinality(), is(equalTo(0)));
        assertThat(new RandomExt().nBitsSet(50, 200).cardinality(), is(equalTo(50)));
        assertThat(new RandomExt().nBitsSet(100, 200).cardinality(), is(equalTo(100)));
        assertThat(new RandomExt().nBitsSet(150, 200).cardinality(), is(equalTo(150)));
        assertThat(new RandomExt().nBitsSet(200, 200).cardinality(), is(equalTo(200)));
    }
    
}
