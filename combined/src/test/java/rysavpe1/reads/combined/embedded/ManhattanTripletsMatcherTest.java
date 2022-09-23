/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rysavpe1.reads.combined.embedded;

import java.util.Arrays;
import java.util.Random;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import org.junit.Test;
import static org.junit.Assert.*;
import rysavpe1.reads.distance.simple.ManhattanDistance;
import rysavpe1.reads.embedded.TripletsEmbedding;
import rysavpe1.reads.embedded.TripletsIndexVectorEmbedding;
import rysavpe1.reads.model.Sequence;
import rysavpe1.reads.utils.ArrayUtils;

/**
 *
 * @author Petr Ryšavý <petr.rysavy@fel.cvut.cz>
 */
public class ManhattanTripletsMatcherTest {

    @Test
    public void testDistancesVectorSubstring() {
        final Random rand = new Random(42);
        final int[] contigIndexVector = rand.ints(10000, 0, 64).toArray();
        final int[] projectedRead = rand.ints(64, 0, 1000).toArray();
        final ManhattanDistance dist = new ManhattanDistance();
        final int[] distances = new int[contigIndexVector.length - 100 + 3];
        for (int i = 0; i < distances.length; i++)
            distances[i] = dist.getDistance(getEmbedding(Arrays.copyOfRange(contigIndexVector, i, i + 98)), projectedRead).intValue();
        assertThat(new ManhattanTripletsMatcher().distancesVectorSubstring(projectedRead, contigIndexVector, 100),
                is(equalTo(distances)));
    }
    
    @Test
    public void testDistancesVectorSubstringSimple() {
        TripletsIndexVectorEmbedding emb = new TripletsIndexVectorEmbedding();
        TripletsEmbedding embRead = new TripletsEmbedding();
        final int[] contigVectorA = emb.project(Sequence.fromString("ACTGCTGCAACT"));
        final int[] readProjected = embRead.project(Sequence.fromString("ACTG"));
        final int[] distances = ArrayUtils.asArray(0, 2, 4, 2, 2, 4, 4, 4, 2);
        assertThat(new ManhattanTripletsMatcher().distancesVectorSubstring(readProjected, contigVectorA, 4),
                is(equalTo(distances)));
    }

    @Test
    public void testDistancesVectorOverlap() {
        final Random rand = new Random(42);
        final int[] firstContigIndexVector = rand.ints(15000, 0, 64).toArray();
        final int[] firstContigIndexVectorRev = ArrayUtils.reversedCopy(firstContigIndexVector);
        final int[] secondContigIndexVector = rand.ints(10000, 0, 64).toArray();
        final ManhattanDistance dist = new ManhattanDistance();
        final int[] distances = new int[secondContigIndexVector.length + 1];
        for (int i = 0; i < distances.length; i++)
            distances[i] = dist.getDistance(
                    getEmbedding(Arrays.copyOfRange(firstContigIndexVectorRev, 0, i)),
                    getEmbedding(Arrays.copyOfRange(secondContigIndexVector, 0, i)))
                    .intValue();
        assertThat(new ManhattanTripletsMatcher().distancesVectorOverlap(firstContigIndexVector, secondContigIndexVector),
                is(equalTo(distances)));
    }

    @Test
    public void testDistancesVectorBothSimple() {
        TripletsIndexVectorEmbedding emb = new TripletsIndexVectorEmbedding();
        final int[] contigVectorA = emb.project(Sequence.fromString("ACTGCTGCAACT"));
        final int[] contigVectorB = emb.project(Sequence.fromString("ACTG"));
        final int[] distances = ArrayUtils.asArray(0, 2, 0, 2, 4, 2, 2, 4, 4, 4, 2, 0, 0);
        assertThat(new ManhattanTripletsMatcher().distancesVectorBoth(contigVectorA, contigVectorB),
                is(equalTo(distances)));
    }

    @Test
    public void testDistancesVectorBoth() {
        final Random rand = new Random(42);
        final int[] firstContigIndexVector = rand.ints(15000, 0, 64).toArray();
        final int[] secondContigIndexVector = rand.ints(10000, 0, 64).toArray();
        final int[] secondContigIndexVectorRev = ArrayUtils.reversedCopy(secondContigIndexVector);
        final ManhattanDistance dist = new ManhattanDistance();
        final int[] distances = new int[firstContigIndexVector.length + secondContigIndexVector.length + 1];
        for (int i = 0; i <= secondContigIndexVector.length; i++)
            distances[i] = dist.getDistance(
                    getEmbedding(Arrays.copyOfRange(firstContigIndexVector, 0, i)),
                    getEmbedding(Arrays.copyOfRange(secondContigIndexVectorRev, 0, i)))
                    .intValue();
        for (int i = secondContigIndexVector.length + 1; i <= firstContigIndexVector.length; i++)
            distances[i] = dist.getDistance(
                    getEmbedding(Arrays.copyOfRange(firstContigIndexVector, i - secondContigIndexVector.length, i)),
                    getEmbedding(secondContigIndexVector))
                    .intValue();
        for (int i = firstContigIndexVector.length + 1; i < distances.length; i++)
            distances[i] = dist.getDistance(
                    getEmbedding(Arrays.copyOfRange(firstContigIndexVector, i - secondContigIndexVector.length, firstContigIndexVector.length)),
                    getEmbedding(Arrays.copyOfRange(secondContigIndexVector, 0, distances.length - i - 1)))
                    .intValue();
        int[] res = new ManhattanTripletsMatcher().distancesVectorBoth(firstContigIndexVector, secondContigIndexVector);
        for (int i = 0; i < res.length; i++)
            if (res[i] != distances[i])
                System.err.println("bug " + i + " res " + res[i] + " true " + distances[i]);
//        assertThat(new ManhattanTripletsMatcher().distancesVectorBoth(firstContigIndexVector, secondContigIndexVector),
//                is(equalTo(distances)));
        assertThat(ArrayUtils.reversedCopy(new ManhattanTripletsMatcher().distancesVectorBoth(firstContigIndexVector, secondContigIndexVector)),
                is(equalTo(ArrayUtils.reversedCopy(distances))));
    }

    private int[] getEmbedding(int[] indexVector) {
        final int[] embedd = new int[64];
        for (int i : indexVector) embedd[i]++;
        return embedd;
    }

}
