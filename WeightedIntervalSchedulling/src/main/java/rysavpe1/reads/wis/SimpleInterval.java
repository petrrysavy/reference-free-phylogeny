package rysavpe1.reads.wis;

/**
 * Simple non-mutable implementation of interval.
 *
 * @author Petr Ryšavý
 */
public class SimpleInterval implements Interval {

    public final int start;
    public final int end;
    public final double value;

    public SimpleInterval(int start, int end, double value) {
        this.start = start;
        this.end = end;
        this.value = value;
    }

    @Override
    public int getEnd() {
        return end;
    }

    @Override
    public int getStart() {
        return start;
    }

    @Override
    public double getValue() {
        return value;
    }

    @Override
    public int hashCode() {
        int hash = 55 + this.start;
        hash = 11 * hash + this.end;
        hash = 11 * hash + (int) (Double.doubleToLongBits(this.value) ^ (Double.doubleToLongBits(this.value) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final SimpleInterval other = (SimpleInterval) obj;
        if (this.start != other.start)
            return false;
        if (this.end != other.end)
            return false;
        return Double.doubleToLongBits(this.value) == Double.doubleToLongBits(other.value);
    }

    @Override
    public String toString() {
        return "[" + start + ", " + end + ") : " + value;
    }

}
