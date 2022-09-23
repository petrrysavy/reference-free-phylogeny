package rysavpe1.reads.embedded;

/**
 * The function that defines an embedding. Embedding is a projection from one
 * space (for example from the space of sequences) to another space (for example
 * space of numerical vectors). Embeddings are often defined in order to provide
 * approximate results faster based on calculations in the embedded space.
 *
 * @author Petr Ryšavý
 * @param <K> The type of the original space.
 * @param <V> The type of the embedded values.
 */
public interface EmbeddingFunction<K, V> {

    /**
     * Calculates the projection of a key.
     * @param key The original value.
     * @return It's projection to the embedded space.
     */
    public V project(K key);
}
