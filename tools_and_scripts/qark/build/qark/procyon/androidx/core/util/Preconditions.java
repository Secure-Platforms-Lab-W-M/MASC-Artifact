// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.util;

import java.util.Locale;

public final class Preconditions
{
    private Preconditions() {
    }
    
    public static void checkArgument(final boolean b) {
        if (b) {
            return;
        }
        throw new IllegalArgumentException();
    }
    
    public static void checkArgument(final boolean b, final Object o) {
        if (b) {
            return;
        }
        throw new IllegalArgumentException(String.valueOf(o));
    }
    
    public static int checkArgumentInRange(final int n, final int n2, final int n3, final String s) {
        if (n < n2) {
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too low)", s, n2, n3));
        }
        if (n <= n3) {
            return n;
        }
        throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too high)", s, n2, n3));
    }
    
    public static int checkArgumentNonnegative(final int n) {
        if (n >= 0) {
            return n;
        }
        throw new IllegalArgumentException();
    }
    
    public static int checkArgumentNonnegative(final int n, final String s) {
        if (n >= 0) {
            return n;
        }
        throw new IllegalArgumentException(s);
    }
    
    public static <T> T checkNotNull(final T t) {
        if (t != null) {
            return t;
        }
        throw null;
    }
    
    public static <T> T checkNotNull(final T t, final Object o) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(String.valueOf(o));
    }
    
    public static void checkState(final boolean b) {
        checkState(b, null);
    }
    
    public static void checkState(final boolean b, final String s) {
        if (b) {
            return;
        }
        throw new IllegalStateException(s);
    }
}
