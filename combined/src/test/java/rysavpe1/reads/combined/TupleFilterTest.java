/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rysavpe1.reads.combined;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import rysavpe1.reads.model.ReadBag;
import rysavpe1.reads.model.ReadBagTuple;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.overlap.OverlapAdapter;
import rysavpe1.reads.overlap.OverlapAdapterFirstSeq;
import rysavpe1.reads.overlap.RegionValue;
import rysavpe1.reads.overlap.SimpleOverlapRegion;
import rysavpe1.reads.utils.CollectionUtils;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class TupleFilterTest {

    @Test
    public void testFilterTuple() {
        final ReadBag contigs = ReadBag.fromString("ACTGCAATGCAATGCAT", "ATCG", "ATCGCCAATGCATAAC");
        final ReadBag reads = ReadBag.fromString("AATGC", "AATGC", "TTTTT", "TGCAT", "CCAAT", "ATCGG", "CTGCA", "AAACT", "AAACT");
        final ReadBagTuple res = new TupleFilter(5).filterTuple(new ReadBagTuple(contigs, reads));
        System.err.println("res contigs " + res.getContigs().elementsString());
        System.err.println("res reads   " + res.getReads().elementsString());
        assertThat(res.getContigs(), is(equalTo(ReadBag.fromString("ACTGCAATGCAATGCAT", "ATCGCCAATGCATAAC"))));
        assertThat(res.getReads(), is(equalTo(ReadBag.fromString("TTTTT", "ATCGG", "AAACT", "AAACT"))));
    }

    @Test
    public void testFilterMultipleOccurences() {
        final ReadBag contigs = ReadBag.fromString("ACTGCAA");
        final ReadBag reads = ReadBag.fromString("AATGC", "TGCAA", "TGCAA");
        final ReadBagTuple res = new TupleFilter(5).filterTuple(new ReadBagTuple(contigs, reads));
        System.err.println("res contigs " + res.getContigs().elementsString());
        System.err.println("res reads   " + res.getReads().elementsString());
        assertThat(res.getContigs(), is(equalTo(ReadBag.fromString("ACTGCAA"))));
        assertThat(res.getReads().size(), is(equalTo(1)));
        assertThat(res.getReads(), is(equalTo(ReadBag.fromString("AATGC"))));
    }

    @Test
    public void testFindUnmappedContigs() {
        final Sequence contig = Sequence.fromString("01234567890123456789012345678901234567890123456789");
        final ReadBag target = new ReadBag(32);
        final List<OverlapAdapter> list = new ArrayList<>();
        list.add(getAdapter(4, 10));
        list.add(getAdapter(7, 12));
        list.add(getAdapter(13, 17));
        list.add(getAdapter(19, 28));
        list.add(getAdapter(21, 25));
        list.add(getAdapter(31, 41));
        list.add(getAdapter(45, 50));
        Collections.shuffle(list, new Random(42));

        assertThat(
                new TupleFilter(2).findNonOverlap(target, list, contig),
                is(equalTo(14))
        );
        System.err.println(target.elementsString());
        assertThat(target, is(equalTo(ReadBag.fromString("0123", "78", "890", "1234"))));
    }

    ;
    
    @Test
    public void testFindUnmappedContigs2() {
        final Sequence contig = Sequence.fromString("01234567890123456789012345678901234567890123456789");
        final ReadBag target = new ReadBag(4);
        final List<OverlapAdapter> list = CollectionUtils.asList(getAdapter(0, 24));

        assertThat(
                new TupleFilter(2).findNonOverlap(target, list, contig),
                is(equalTo(26))
        );
        System.err.println(target.elementsString());
        assertThat(target, is(equalTo(ReadBag.fromString("45678901234567890123456789"))));
    }

    private static OverlapAdapter getAdapter(int start, int end) {
        return new OverlapAdapterFirstSeq(new SimpleOverlapRegion(0, end - start, end, 0, 0), RegionValue.LENGTH);
    }

}
