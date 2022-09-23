package rysavpe1.reads.overlap;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import rysavpe1.reads.model.Sequence;

/**
 * A virtual overlap finder finds an overlap. This tests it.
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class OverlapAdapterAndSequenceTest {
    
    private static final Sequence ASEQ = Sequence.fromString("ACTGACAC");
    private static final Sequence BSEQ = Sequence.fromString("ATATGCTAGCTA");
    private static final double DISTANCE = 3.141592654;

    @Test
    public void testForwardForward() {
        OverlapRegion region = new SimpleOverlapRegion(DISTANCE, 4, 8, 4, 4);
        
        OverlapAdapterAndSequence s1 = OverlapAdapterAndSequence.overlapAdapterFirstSeq(ASEQ, BSEQ, region, RegionValue.LENGTH_OVER_DISTANCE, false, false);
        assertThat(s1.getStart(), is(equalTo(4)));
        assertThat(s1.getEnd(), is(equalTo(8)));
        assertThat(s1.projectA().getSequenceString(), is(equalTo("ACAC")));
        assertThat(s1.projectB().getSequenceString(), is(equalTo("ATAT")));
        
        OverlapAdapterAndSequence s2 = OverlapAdapterAndSequence.overlapAdapterSecondSeq(ASEQ, BSEQ, region, RegionValue.LENGTH_OVER_DISTANCE, false, false);
        assertThat(s2.getStart(), is(equalTo(0)));
        assertThat(s2.getEnd(), is(equalTo(4)));
        assertThat(s2.projectA().getSequenceString(), is(equalTo("ACAC")));
        assertThat(s2.projectB().getSequenceString(), is(equalTo("ATAT")));
    }

    @Test
    public void testForwardReverse() {
        OverlapRegion region = new SimpleOverlapRegion(DISTANCE, 4, 8, 4, 12);
        region = region.reverseB(12);
        
        OverlapAdapterAndSequence s1 = OverlapAdapterAndSequence.overlapAdapterFirstSeq(ASEQ, BSEQ, region, RegionValue.LENGTH_OVER_DISTANCE, true, false);
        assertThat(s1.getStart(), is(equalTo(4)));
        assertThat(s1.getEnd(), is(equalTo(8)));
        assertThat(s1.projectA().getSequenceString(), is(equalTo("ACAC")));
        assertThat(s1.projectB().getSequenceString(), is(equalTo("TATA")));
        
        OverlapAdapterAndSequence s2 = OverlapAdapterAndSequence.overlapAdapterSecondSeq(ASEQ, BSEQ, region, RegionValue.LENGTH_OVER_DISTANCE, true, false);
        assertThat(s2.getStart(), is(equalTo(0)));
        assertThat(s2.getEnd(), is(equalTo(4)));
        assertThat(s2.projectA().getSequenceString(), is(equalTo("ACAC")));
        assertThat(s2.projectB().getSequenceString(), is(equalTo("TATA")));
    }
    
}
