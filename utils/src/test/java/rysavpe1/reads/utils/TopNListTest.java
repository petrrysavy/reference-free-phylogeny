/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rysavpe1.reads.utils;

import java.util.Comparator;
import static org.hamcrest.Matchers.containsInAnyOrder;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test of a list that holds several highest values encountered.
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class TopNListTest {

    @Test
    public void testNaturalOrder() {
        TopNList<Integer> list = new TopNList<>(3);
        list.add(1);
        list.add(3);
        list.add(5);
        list.add(3);
        list.add(1);
        list.add(5);
        System.err.println(list);
        assertThat(list, containsInAnyOrder(5, 5, 3));
    }

    @Test
    public void testNaturalOrder2() {
        TopNList<Integer> list = new TopNList<>(3);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        System.err.println(list);
        assertThat(list, containsInAnyOrder(4, 5, 6));
    }

    @Test
    public void testNaturalOrder3() {
        TopNList<Integer> list = new TopNList<>(3);
        list.add(6);
        list.add(5);
        list.add(4);
        list.add(3);
        list.add(2);
        list.add(1);
        System.err.println(list);
        assertThat(list, containsInAnyOrder(4, 5, 6));
    }

    @Test
    public void testNaturalOrderAddAll() {
        TopNList<Integer> list = new TopNList<>(3);
        list.addAll(CollectionUtils.asList(1, 2, 3, 4, 5, 6));
        System.err.println(list);
        assertThat(list, containsInAnyOrder(4, 5, 6));
    }

    @Test
    public void testNaturalOrderAddAll2() {
        TopNList<Integer> list = new TopNList<>(3);
        list.addAll(CollectionUtils.asList(6, 5, 4, 3, 2, 1));
        System.err.println(list);
        assertThat(list, containsInAnyOrder(4, 5, 6));
    }

    @Test
    public void testReverseOrder() {
        TopNList<Integer> list = new TopNList<>(3, Comparator.reverseOrder());
        list.add(1);
        list.add(3);
        list.add(5);
        list.add(3);
        list.add(1);
        list.add(5);
        System.err.println(list);
        assertThat(list, containsInAnyOrder(1, 1, 3));
    }

    @Test
    public void testReverseOrder2() {
        TopNList<Integer> list = new TopNList<>(3, Comparator.reverseOrder());
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        System.err.println(list);
        assertThat(list, containsInAnyOrder(1,2,3));
    }

    @Test
    public void testReverseOrder3() {
        TopNList<Integer> list = new TopNList<>(3, Comparator.reverseOrder());
        list.add(6);
        list.add(5);
        list.add(4);
        list.add(3);
        list.add(2);
        list.add(1);
        System.err.println(list);
        assertThat(list, containsInAnyOrder(1,2,3));
    }

    @Test
    public void testReverseOrderAddAll() {
        TopNList<Integer> list = new TopNList<>(3, Comparator.reverseOrder());
        list.addAll(CollectionUtils.asList(1, 2, 3, 4, 5, 6));
        System.err.println(list);
        assertThat(list, containsInAnyOrder(1,2,3));
    }

    @Test
    public void testReverseOrderAddAll2() {
        TopNList<Integer> list = new TopNList<>(3, Comparator.reverseOrder());
        list.addAll(CollectionUtils.asList(6, 5, 4, 3, 2, 1));
        System.err.println(list);
        assertThat(list, containsInAnyOrder(1,2,3));
    }
}
