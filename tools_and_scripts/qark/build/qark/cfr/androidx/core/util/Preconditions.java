/*
 * Decompiled with CFR 0_124.
 */
package androidx.core.util;

import java.util.Locale;

public final class Preconditions {
    private Preconditions() {
    }

    public static void checkArgument(boolean bl) {
        if (bl) {
            return;
        }
        throw new IllegalArgumentException();
    }

    public static void checkArgument(boolean bl, Object object) {
        if (bl) {
            return;
        }
        throw new IllegalArgumentException(String.valueOf(object));
    }

    public static int checkArgumentInRange(int n, int n2, int n3, String string2) {
        if (n >= n2) {
            if (n <= n3) {
                return n;
            }
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too high)", string2, n2, n3));
        }
        throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too low)", string2, n2, n3));
    }

    public static int checkArgumentNonnegative(int n) {
        if (n >= 0) {
            return n;
        }
        throw new IllegalArgumentException();
    }

    public static int checkArgumentNonnegative(int n, String string2) {
        if (n >= 0) {
            return n;
        }
        throw new IllegalArgumentException(string2);
    }

    public static <T> T checkNotNull(T t) {
        if (t != null) {
            return t;
        }
        throw null;
    }

    public static <T> T checkNotNull(T t, Object object) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException(String.valueOf(object));
    }

    public static void checkState(boolean bl) {
        Preconditions.checkState(bl, null);
    }

    public static void checkState(boolean bl, String string2) {
        if (bl) {
            return;
        }
        throw new IllegalStateException(string2);
    }
}

