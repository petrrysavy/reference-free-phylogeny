/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rysavpe1.reads.combined;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import rysavpe1.reads.model.ReadBag;
import rysavpe1.reads.model.ReadBagTuple;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class CombinedDistanceTest {

    private static final int READ_LENGTH = 5;
    private static final double COVERAGE = 2;
    private static final int OVERLAP_THRESHOLD = 3;

    private ReadBagTuple tupleA, tupleB;
    private CombinedDistance distance;

    @Before
    public void bootstrap() {
        distance = new CombinedDistance(READ_LENGTH, COVERAGE, OVERLAP_THRESHOLD, false, false, -1);

        tupleA = new ReadBagTuple(
                ReadBag.fromString("ABCD1234567890EF", "JKLMXO"),
                ReadBag.fromString("PQRST", "XYZXQ", "ABCD1", "CD123", "34567", "56789", "890EF", "JKLMX", "KLMXO")
        );
        tupleB = new ReadBagTuple(
                ReadBag.fromString("3456789XEFGHIJKLMNOPQ"),
                ReadBag.fromString("QRSTU", "AXCD1", "34567", "45678", "89XEF", "XEFGH", "HIJKL", "KLMNO", "MNOPQ", "XYZWQ", "BCD12")
        );
    }

    @Ignore
    @Test
    public void testCombinedDistance() {
        assertThat(
                distance.getDistance(tupleA, tupleB),
                is(closeTo(0.5 * (8.0 / 11 / 8 + 3.0 / 11 / 10 + 16.0 / 27 / 8 + 11.0 / 27 / 10) * 27, 1e-10))
                // 3,14545454545454545455
        );
    }

    @Ignore
    @Test
    public void testCombinedDistance2() {
        assertThat(
                distance.getDistance(tupleB, tupleA),
                is(closeTo(0.5 * (8.0 / 11 / 8 + 3.0 / 11 / 10 + 16.0 / 27 / 8 + 11.0 / 27 / 10) * 27, 1e-10))
        );
    }
    
    @Test
    public void testIsSymmetric() {
        assertThat(distance.isSymmetric(), is(true));
    }
    
    @Test
    public void testIsNotNormalized() {
        assertThat(distance.isZeroOneNormalized(), is(false));
    }

}
