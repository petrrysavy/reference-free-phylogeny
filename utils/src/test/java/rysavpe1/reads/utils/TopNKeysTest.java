/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rysavpe1.reads.utils;

import org.hamcrest.Matchers;
import static org.junit.Assert.assertThat;
import org.junit.Test;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class TopNKeysTest {

    @Test
    public void testInterval() {
        final TopNKeys<Integer> topNKeys = new TopNKeys<>(3, true);
        topNKeys.put(12, 1);
        topNKeys.put(11, 2);
        topNKeys.put(10, 3);
        topNKeys.put(12, 4);
        topNKeys.put(10, 5);
        topNKeys.put(10, 6);
        topNKeys.put(9, 7);
        topNKeys.put(12, 8);
        topNKeys.put(11, 9);
        topNKeys.put(9, 10);
        topNKeys.put(10, 11);
        topNKeys.put(8, 12);
        topNKeys.put(42, 13);
        assertThat(topNKeys, Matchers.containsInAnyOrder(3, 5, 6, 7, 10, 11, 12));
        assertThat(topNKeys, Matchers.contains(12, 7, 10, 3, 5, 6, 11));
    }

    @Test
    public void testGap() {
        final TopNKeys<Integer> topNKeys = new TopNKeys<>(3, true);
        topNKeys.put(12, 1);
        topNKeys.put(11, 2);
        topNKeys.put(10, 3);
        topNKeys.put(12, 4);
        topNKeys.put(10, 5);
        topNKeys.put(10, 6);
        topNKeys.put(12, 8);
        topNKeys.put(11, 9);
        topNKeys.put(10, 11);
        topNKeys.put(8, 12);
        topNKeys.put(42, 13);
        assertThat(topNKeys, Matchers.containsInAnyOrder(3, 5, 6, 11, 12));
        assertThat(topNKeys, Matchers.contains(12, 3, 5, 6, 11));
    }

    @Test
    public void testIntervalDown() {
        final TopNKeys<Integer> topNKeys = new TopNKeys<>(3, false);
        topNKeys.put(-12, -1);
        topNKeys.put(-11, -2);
        topNKeys.put(-10, -3);
        topNKeys.put(-12, -4);
        topNKeys.put(-10, -5);
        topNKeys.put(-10, -6);
        topNKeys.put(-9, -7);
        topNKeys.put(-12, -8);
        topNKeys.put(-11, -9);
        topNKeys.put(-9, -10);
        topNKeys.put(-10, -11);
        topNKeys.put(-8, -12);
        topNKeys.put(-42, -13);
        assertThat(topNKeys, Matchers.containsInAnyOrder(-3, -5, -6, -7, -10, -11, -12));
        assertThat(topNKeys, Matchers.contains(-12, -7, -10, -3, -5, -6, -11));
    }

    @Test
    public void testGapDown() {
        final TopNKeys<Integer> topNKeys = new TopNKeys<>(3, false);
        topNKeys.put(-12, -1);
        topNKeys.put(-11, -2);
        topNKeys.put(-10, -3);
        topNKeys.put(-12, -4);
        topNKeys.put(-10, -5);
        topNKeys.put(-10, -6);
        topNKeys.put(-12, -8);
        topNKeys.put(-11, -9);
        topNKeys.put(-10, -11);
        topNKeys.put(-8, -12);
        topNKeys.put(-42, -13);
        assertThat(topNKeys, Matchers.containsInAnyOrder(-3, -5, -6, -11, -12));
        assertThat(topNKeys, Matchers.contains(-12, -3, -5, -6, -11));
    }

}
