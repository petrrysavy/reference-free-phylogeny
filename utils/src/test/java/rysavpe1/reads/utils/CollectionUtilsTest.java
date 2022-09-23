package rysavpe1.reads.utils;



import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.Matchers.contains;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author petr
 */
public class CollectionUtilsTest {
    
    public CollectionUtilsTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of intersectionSize method, of class CollectionUtils.
     */
    @Test
    public void testIntersectionSize() {
        assertThat(CollectionUtils.intersectionSize(CollectionUtils.asSet(1, 2, 3, 4), CollectionUtils.asSet(2, 3, 4, 5)), is(equalTo(3)));
        assertThat(CollectionUtils.intersectionSize(CollectionUtils.asSet(1, 2, 3, 4), CollectionUtils.asSet(5, 6, 7, 8)), is(equalTo(0)));
        assertThat(CollectionUtils.intersectionSize(CollectionUtils.asSet(1, 2, 3, 4), CollectionUtils.asSet(1, 2, 3, 4)), is(equalTo(4)));
        assertThat(CollectionUtils.intersectionSize(CollectionUtils.asSet(1, 2, 3, 4), CollectionUtils.asSet(1)), is(equalTo(1)));
        assertThat(CollectionUtils.intersectionSize(CollectionUtils.asSet(1, 2, 3, 4), (Set<Integer>) Collections.EMPTY_SET), is(equalTo(0)));
    }

    /**
     * Test of getFirstNotIJ method, of class CollectionUtils.
     */
    @Test
    public void testGetFirstNotIJ() {
        assertThat(CollectionUtils.getFirstNotIJ(CollectionUtils.asSet(1, 2, 3), 1, 2), is(equalTo(3)));
        assertThat(CollectionUtils.getFirstNotIJ(CollectionUtils.asSet(1, 2, 3), 2, 3), is(equalTo(1)));
    }
    
    @Test(expected = NoSuchElementException.class)
    public void testGetFirstNotIJ2() {
        CollectionUtils.getFirstNotIJ(CollectionUtils.asSet(1, 2), 1, 2);
    }

    /**
     * Test of asSet method, of class CollectionUtils.
     */
    @Test
    public void testAsSet() {
        HashSet<Integer> set = new HashSet<>();
        set.add(1);
        set.add(2);
        set.add(3);
        assertThat(CollectionUtils.asSet(1, 2, 3), is(equalTo(set)));
    }

    /**
     * Test of asList method, of class CollectionUtils.
     */
    @Test
    public void testAsList() {
        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        assertThat(CollectionUtils.asList(1, 2, 3), is(equalTo(list)));
    }

    /**
     * Test of nLists method, of class CollectionUtils.
     */
    @Test
    public void testNLists() {
        assertThat(CollectionUtils.nLists(3).size(), is(equalTo(3)));
        assertThat(CollectionUtils.nLists(3), not(contains((Object) null)));
        assertThat(CollectionUtils.nLists(3).stream().allMatch(list -> list.isEmpty()), is(true));
    }

    /**
     * Test of clear method, of class CollectionUtils.
     */
    @Test
    public void testClear() {
        List<List<Integer>> lists = CollectionUtils.nLists(5);
        lists.stream().forEach(list -> list.add(Integer.SIZE));
        CollectionUtils.clear(lists);
        assertThat(lists.size(), is(equalTo(5)));
        assertThat(lists, not(contains((Object) null)));
        assertThat(lists.stream().allMatch(list -> list.isEmpty()), is(true));
        
    }

    /**
     * Test of carthesianProductMap method, of class CollectionUtils.
     */
    @Test
    public void testPairWithAll() {
        Map<Integer, Character> result = CollectionUtils.pairWithAll(CollectionUtils.asList(1, 2, 3), 'a');
        assertThat(result.size(), is(equalTo(3)));
        assertThat(result.get(1), is(equalTo('a')));
        assertThat(result.get(2), is(equalTo('a')));
        assertThat(result.get(3), is(equalTo('a')));
        assertThat(result.get(4), is(nullValue()));
    }

    /**
     * Test of carthesianProduct method, of class CollectionUtils.
     */
    @Test
    public void testCarthesianProduct() {
        assertThat(
                CollectionUtils.carthesianProduct(CollectionUtils.asList(1, 2, 3), CollectionUtils.asList('a', 'b')),
                is(equalTo(CollectionUtils.asList(new Pair(1, 'a'), new Pair(1, 'b'), new Pair(2, 'a'), new Pair(2, 'b'), new Pair(3, 'a'), new Pair(3, 'b'))))
        );
    }

    /**
     * Test of growToSize method, of class CollectionUtils.
     */
    @Test
    public void testGrowToSize() {
        List<Integer> l = CollectionUtils.asList(1, 2, 3);
        CollectionUtils.growToSize(l, 5);
        assertThat(l, is(equalTo(CollectionUtils.asList(1, 2, 3, null, null))));
    }

    /**
     * Test of zip method, of class CollectionUtils.
     */
    @Test
    public void testZip() {
        assertThat(
                CollectionUtils.zip(CollectionUtils.asList(
                        CollectionUtils.asList(1, 2, 3, 4),
                        CollectionUtils.asList(11, 12, 13, 14),
                        CollectionUtils.asList(21, 22, 23, 24))),
                is(equalTo(CollectionUtils.asList(
                        CollectionUtils.asList(1, 11, 21),
                        CollectionUtils.asList(2, 12, 22),
                        CollectionUtils.asList(3, 13, 23),
                        CollectionUtils.asList(4, 14, 24))))
        );
    }

    /**
     * Test of flattern method, of class CollectionUtils.
     */
    @Test
    public void testFlattern() {
        assertThat(
                CollectionUtils.flattern(CollectionUtils.asList(
                        CollectionUtils.asList(1, 2, 3, 4),
                        CollectionUtils.asList(11, 12, 13, 14),
                        CollectionUtils.asList(21, 22, 23, 24))),
                is(equalTo(CollectionUtils.asList(
                        1, 2, 3, 4, 11, 12, 13, 14, 21, 22, 23, 24)))
        );
    }
    
    @Test
    public void testFilterOutExtremes() {
        assertThat(CollectionUtils.filterOutExtremes(CollectionUtils.asList(4, 5, 1, 6, 2, 3, 7, 8, 9, 11, 10), 3),
                is(equalTo(CollectionUtils.asList(4, 5, 6, 7, 8)))
        );
    }
}
