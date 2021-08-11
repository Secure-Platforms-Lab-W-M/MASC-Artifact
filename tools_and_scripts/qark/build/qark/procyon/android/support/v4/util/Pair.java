// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.util;

public class Pair<F, S>
{
    public final F first;
    public final S second;
    
    public Pair(final F first, final S second) {
        this.first = first;
        this.second = second;
    }
    
    public static <A, B> Pair<A, B> create(final A a, final B b) {
        return new Pair<A, B>(a, b);
    }
    
    private static boolean objectsEqual(final Object o, final Object o2) {
        return o == o2 || (o != null && o.equals(o2));
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = o instanceof Pair;
        final boolean b2 = false;
        if (!b) {
            return false;
        }
        final Pair pair = (Pair)o;
        boolean b3 = b2;
        if (objectsEqual(pair.first, this.first)) {
            b3 = b2;
            if (objectsEqual(pair.second, this.second)) {
                b3 = true;
            }
        }
        return b3;
    }
    
    @Override
    public int hashCode() {
        final F first = this.first;
        int hashCode = 0;
        int hashCode2;
        if (first == null) {
            hashCode2 = 0;
        }
        else {
            hashCode2 = first.hashCode();
        }
        final S second = this.second;
        if (second != null) {
            hashCode = second.hashCode();
        }
        return hashCode2 ^ hashCode;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Pair{");
        sb.append(String.valueOf(this.first));
        sb.append(" ");
        sb.append(String.valueOf(this.second));
        sb.append("}");
        return sb.toString();
    }
}
