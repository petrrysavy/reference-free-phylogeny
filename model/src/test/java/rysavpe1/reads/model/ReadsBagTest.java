package rysavpe1.reads.model;

import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 *
 * @author Petr Ryšavý
 */
public class ReadsBagTest
{
    private ReadBag readsBag;

    @Before
    public void bootstrap() {
        this.readsBag = new ReadBag(10, "A test reads bag.");
        readsBag.add(new Sequence("ATCGCCTTAG".toCharArray(), "A test sequence 1"));
        readsBag.add(new Sequence("ATCGCCTTAG".toCharArray(), "A test sequence 2"));
        readsBag.add(new Sequence("AAA".toCharArray(), "A test sequence 3"));
        readsBag.add(new Sequence("ATCG".toCharArray(), "A test sequence 4"));
        readsBag.add(new Sequence("ATCG".toCharArray(), "A test sequence 5"));
        readsBag.add(new Sequence("GCTATTATATCGCGTA".toCharArray(), "A test sequence 5"));
    }

    @Test
    public void testGetDescription() {
        assertThat(readsBag.getDescription(), is(equalTo("A test reads bag.")));
    }

    @Test
    public void testLongestSequence() {
        assertThat(readsBag.longestSequence(), is(equalTo(Sequence.fromString("GCTATTATATCGCGTA"))));
    }

    @Test
    public void testFromString() {
        assertThat(ReadBag.fromString("AAA", "ATCG", "ATCGCCTTAG", "GCTATTATATCGCGTA", "ATCGCCTTAG", "ATCG"), is(equalTo(readsBag)));
    }

    @Test
    public void testToString() {
        assertThat(readsBag.toString(), is(equalTo("A test reads bag.")));
    }

}
