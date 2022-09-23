package rysavpe1.reads.embedded;

import java.util.Objects;

/**
 * Interface that defines embedding of a object.
 *
 * @author Petr Ryšavý
 * @param <K> The original key.
 * @param <V> The projected value.
 */
public class EmbeddedObject<K, V> {

    /** This is the original object. */
    private final K object;
    /** This is the value to that {@code object} is projected. */
    private final V projected;

    /**
     * Creates mew instance.
     * @param object Original object.
     * @param projected It's projection.
     */
    public EmbeddedObject(K object, V projected) {
        this.object = object;
        this.projected = projected;
    }

    /**
     * Gets the original object.
     * @return The original object.
     */
    public K getObject() {
        return object;
    }

    /**
     * The projected value.
     * @return The embedded value.
     */
    public V getProjected() {
        return projected;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(! (obj instanceof EmbeddedObject)) return false;
        EmbeddedObject other = (EmbeddedObject) obj;
        return Objects.equals(object, other.object);
    }

    @Override
    public int hashCode() {
        return object.hashCode();
    }
}
