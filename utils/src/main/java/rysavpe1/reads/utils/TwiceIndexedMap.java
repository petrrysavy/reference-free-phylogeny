package rysavpe1.reads.utils;

import java.util.HashMap;

/**
 * A map with two keys of the same type.
 *
 * @author Petr Ryšavý
 * @param <K> Key type.
 * @param <V> Value type.
 */
public class TwiceIndexedMap<K, V> extends HashMap<PairSet<K>, V> {

    /**
     * Puts a new value to the map.
     * @param key1 The first key.
     * @param key2 The second key.
     * @param value The value.
     * @return The original value.
     */
    public V put(K key1, K key2, V value) {
        return super.put(new PairSet<>(key1, key2), value);
    }

    /**
     * Gets the value based on two keys.
     * @param key1 The first key.
     * @param key2 The second key.
     * @return The value assigned to the key pair.
     */
    public V get(K key1, K key2) {
        return super.get(new PairSet<>(key1, key2));
    }

    /**
     * Removes the value based on two keys.
     * @param key1 The first key.
     * @param key2 The second key.
     * @return The value assigned to the key pair.
     */
    public V removeKey(K key1, K key2) {
        return super.remove(new PairSet<>(key1, key2));
    }
}
