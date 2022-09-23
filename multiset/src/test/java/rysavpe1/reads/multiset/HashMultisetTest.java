package rysavpe1.reads.multiset;

import java.util.Iterator;
import static org.hamcrest.CoreMatchers.equalTo;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import rysavpe1.reads.utils.CollectionUtils;
import rysavpe1.reads.utils.IteratorWrapper;

/**
 *
 * @author Petr Ryšavý
 */
public class HashMultisetTest {

    private HashMultiset<Integer> multiset;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void init() {
        multiset = new HashMultiset<>();
        multiset.add(0);
        multiset.add(1);
        multiset.add(0);
        multiset.add(2);
        multiset.add(4);
        multiset.add(1);
        multiset.add(0);
        multiset.add(4);
    }

    @Test
    public void constructEmptySet() {
        multiset = new HashMultiset<>();
        assertThat(multiset.size(), is(equalTo(0)));
        assertThat(multiset.contains(0), is(false));
        assertThat(multiset.count(0), is(equalTo(0)));
        assertThat(multiset.iterator().hasNext(), is(false));
    }

    @Test
    public void constructViaCollection() {
        HashMultiset<Integer> multiset2 = new HashMultiset<>(CollectionUtils.asList(0, 1, 0, 2, 4, 1, 0, 4));
        assertThat(multiset2, is(equalTo(multiset)));
    }

    @Test
    public void constructViaVarargs() {
        HashMultiset<Integer> multiset2 = new HashMultiset<>(0, 1, 0, 2, 4, 1, 0, 4);
        assertThat(multiset2, is(equalTo(multiset)));
    }

    @Test
    public void testIterator() {
        assertThat(new IteratorWrapper<>(multiset.iterator()), containsInAnyOrder(0, 0, 0, 1, 1, 2, 4, 4));
    }

    @Test
    public void testCount() {
        assertThat(multiset.count(0), is(equalTo(3)));
        assertThat(multiset.count(1), is(equalTo(2)));
        assertThat(multiset.count(2), is(equalTo(1)));
        assertThat(multiset.count(3), is(equalTo(0)));
        assertThat(multiset.count(4), is(equalTo(2)));
    }

    @Test
    public void testContains() {
        assertThat(multiset.contains(0), is(true));
        assertThat(multiset.contains(1), is(true));
        assertThat(multiset.contains(2), is(true));
        assertThat(multiset.contains(3), is(false));
        assertThat(multiset.contains(4), is(true));
    }

    @Test
    public void testSize() {
        assertThat(multiset.size(), is(equalTo(8)));
    }

    @Test
    public void testUniqueSize() {
        assertThat(multiset.uniqueSize(), is(equalTo(4)));
    }

    @Test
    public void testClear() {
        multiset.clear();
        assertThat(multiset.size(), is(equalTo(0)));
        assertThat(multiset.iterator().hasNext(), is(false));
    }

    @Test
    public void testAdd() {
        multiset.add(5);
        assertThat(multiset, containsInAnyOrder(0, 0, 0, 1, 1, 2, 4, 4, 5));
    }

    @Test
    public void testAddAll() {
        HashMultiset<Integer> other = new HashMultiset<>(1, 2, 4, 4, 5, 5, 0);
        multiset.addAll(other);
        assertThat(multiset.size(), is(equalTo(15)));
        assertThat(multiset, containsInAnyOrder(0, 0, 0, 0, 1, 1, 1, 2, 2, 4, 4, 4, 4, 5, 5));
    }

    @Test
    public void testAddCount() {
        multiset.add(5, 10);
        assertThat(multiset.count(5), is(equalTo(10)));
    }

    @Test
    public void testToSet() {
        assertThat(multiset.toSet(), is(equalTo(CollectionUtils.asSet(0, 1, 2, 4))));
    }

    @Test
    public void testRemove() {
        multiset.remove(2);
        multiset.remove(0);
        multiset.remove(5);
        assertThat(multiset.size(), is(equalTo(6)));
        assertThat(multiset.count(0), is(equalTo(2)));
        assertThat(multiset.count(2), is(equalTo(0)));
        assertThat(multiset, containsInAnyOrder(0, 0, 1, 1, 4, 4));
    }

    @Test
    public void testUnion() {
        multiset.clear();
        multiset.add(1, 3);
        multiset.add(2, 5);
        multiset.add(0);
        Multiset<Integer> multiset2 = new HashMultiset<>();
        multiset2.add(3);
        multiset2.add(2, 5);
        assertThat(multiset.size(), is(equalTo(9)));
        assertThat(multiset2.size(), is(equalTo(6)));
        Multiset<Integer> union = multiset.union(multiset2);
        assertThat(union.size(), is(equalTo(15)));
        assertThat(union.count(-1), is(equalTo(0)));
        assertThat(union.count(0), is(equalTo(1)));
        assertThat(union.count(1), is(equalTo(3)));
        assertThat(union.count(2), is(equalTo(10)));
        assertThat(union.count(3), is(equalTo(1)));
        assertThat(union.count(4), is(equalTo(0)));
    }

    @Test
    public void testClone() {
        Multiset<Integer> clone = multiset.clone();
        assertThat(clone, is(equalTo(multiset)));
    }

    @Test
    public void testIteratorRemove() {
        Iterator<Integer> it = multiset.iterator();
        while (it.hasNext()) {
            it.next();
            it.remove();
        }
        assertThat(multiset.isEmpty(), is(true));
    }

    @Test
    public void testIteratorRemove2() {
        Iterator<Integer> it = multiset.iterator();
        it.next();
        it.next();
        it.remove();
        it.next();
        it.next();
        assertThat(multiset.size(), is(equalTo(7)));
    }

    @Test
    public void testIteratorDidNotCallNext1() {
        Iterator<Integer> it = multiset.iterator();
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Next needs to be called before each remove operation.");
        it.remove();
    }

    @Test
    public void testIteratorDidNotCallNext2() {
        Iterator<Integer> it = multiset.iterator();
        it.next();
        it.remove();
        thrown.expect(IllegalStateException.class);
        thrown.expectMessage("Next needs to be called before each remove operation.");
        it.remove();
    }
}
