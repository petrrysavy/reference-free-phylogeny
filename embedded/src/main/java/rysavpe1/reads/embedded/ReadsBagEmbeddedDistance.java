package rysavpe1.reads.embedded;

import java.util.logging.Level;
import java.util.logging.Logger;
import rysavpe1.reads.distance.AbstractMeasure;
import rysavpe1.reads.distance.DistanceCalculator;
import rysavpe1.reads.model.ReadBag;
import rysavpe1.reads.model.Sequence;

/**
 * For testing purposes only! We do not want to repeatedely assembly the reads
 * bags.
 *
 * @author Petr Ryšavý
 * @param <T>
 */
public class ReadsBagEmbeddedDistance<T> extends AbstractMeasure<ReadBag> {

    private final MongeElkanEmbeddedDistance<EmbeddedSequence<T>, Sequence, T> innerDistance;
    private final EmbeddingFunction<Sequence, T> embedding;

    public ReadsBagEmbeddedDistance(
            DistanceCalculator<T, ? extends Number> embeddedDistance,
            DistanceCalculator<Sequence, ? extends Number> innerDistance,
            EmbeddingFunction<Sequence, T> embedding,
            int maxEmbeddedDistance
    ) {
        this.innerDistance = new MongeElkanEmbeddedDistance<>(embeddedDistance, innerDistance, maxEmbeddedDistance);
        this.embedding = embedding;
    }

    @Override
    public Double getDistance(ReadBag a, ReadBag b) {
        Logger.getLogger(ReadsBagEmbeddedDistance.class.getName()).log(Level.WARNING, "Do not use for production.");
        return innerDistance.getDistance(new EmbeddedReadBag<>(a, embedding), new EmbeddedReadBag<>(b, embedding));
    }
}
