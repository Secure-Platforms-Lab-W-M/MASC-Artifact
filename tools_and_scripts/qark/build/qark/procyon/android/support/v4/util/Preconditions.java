// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.util;

import android.text.TextUtils;
import android.support.annotation.NonNull;
import java.util.Iterator;
import java.util.Collection;
import android.support.annotation.IntRange;
import java.util.Locale;
import android.support.annotation.RestrictTo;

@RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
public class Preconditions
{
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
    
    public static float checkArgumentFinite(final float n, final String s) {
        if (Float.isNaN(n)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(" must not be NaN");
            throw new IllegalArgumentException(sb.toString());
        }
        if (!Float.isInfinite(n)) {
            return n;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(s);
        sb2.append(" must not be infinite");
        throw new IllegalArgumentException(sb2.toString());
    }
    
    public static float checkArgumentInRange(final float n, final float n2, final float n3, final String s) {
        if (Float.isNaN(n)) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(" must not be NaN");
            throw new IllegalArgumentException(sb.toString());
        }
        if (n < n2) {
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%f, %f] (too low)", s, n2, n3));
        }
        if (n <= n3) {
            return n;
        }
        throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%f, %f] (too high)", s, n2, n3));
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
    
    public static long checkArgumentInRange(final long n, final long n2, final long n3, final String s) {
        if (n < n2) {
            throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too low)", s, n2, n3));
        }
        if (n <= n3) {
            return n;
        }
        throw new IllegalArgumentException(String.format(Locale.US, "%s is out of range of [%d, %d] (too high)", s, n2, n3));
    }
    
    @IntRange(from = 0L)
    public static int checkArgumentNonnegative(final int n) {
        if (n >= 0) {
            return n;
        }
        throw new IllegalArgumentException();
    }
    
    @IntRange(from = 0L)
    public static int checkArgumentNonnegative(final int n, final String s) {
        if (n >= 0) {
            return n;
        }
        throw new IllegalArgumentException(s);
    }
    
    public static long checkArgumentNonnegative(final long n) {
        if (n >= 0L) {
            return n;
        }
        throw new IllegalArgumentException();
    }
    
    public static long checkArgumentNonnegative(final long n, final String s) {
        if (n >= 0L) {
            return n;
        }
        throw new IllegalArgumentException(s);
    }
    
    public static int checkArgumentPositive(final int n, final String s) {
        if (n > 0) {
            return n;
        }
        throw new IllegalArgumentException(s);
    }
    
    public static float[] checkArrayElementsInRange(final float[] array, final float n, final float n2, final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(" must not be null");
        checkNotNull(array, sb.toString());
        for (int i = 0; i < array.length; ++i) {
            final float n3 = array[i];
            if (Float.isNaN(n3)) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(s);
                sb2.append("[");
                sb2.append(i);
                sb2.append("] must not be NaN");
                throw new IllegalArgumentException(sb2.toString());
            }
            if (n3 < n) {
                throw new IllegalArgumentException(String.format(Locale.US, "%s[%d] is out of range of [%f, %f] (too low)", s, i, n, n2));
            }
            if (n3 > n2) {
                throw new IllegalArgumentException(String.format(Locale.US, "%s[%d] is out of range of [%f, %f] (too high)", s, i, n, n2));
            }
        }
        return array;
    }
    
    public static <T> T[] checkArrayElementsNotNull(final T[] array, final String s) {
        if (array != null) {
            for (int i = 0; i < array.length; ++i) {
                if (array[i] == null) {
                    throw new NullPointerException(String.format(Locale.US, "%s[%d] must not be null", s, i));
                }
            }
            return array;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(" must not be null");
        throw new NullPointerException(sb.toString());
    }
    
    @NonNull
    public static <C extends Collection<T>, T> C checkCollectionElementsNotNull(final C c, final String s) {
        if (c != null) {
            long n = 0L;
            final Iterator<T> iterator = c.iterator();
            while (iterator.hasNext()) {
                if (iterator.next() == null) {
                    throw new NullPointerException(String.format(Locale.US, "%s[%d] must not be null", s, n));
                }
                ++n;
            }
            return c;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(" must not be null");
        throw new NullPointerException(sb.toString());
    }
    
    public static <T> Collection<T> checkCollectionNotEmpty(final Collection<T> collection, final String s) {
        if (collection == null) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(" must not be null");
            throw new NullPointerException(sb.toString());
        }
        if (!collection.isEmpty()) {
            return collection;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(s);
        sb2.append(" is empty");
        throw new IllegalArgumentException(sb2.toString());
    }
    
    public static int checkFlagsArgument(final int n, final int n2) {
        if ((n & n2) == n) {
            return n;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Requested flags 0x");
        sb.append(Integer.toHexString(n));
        sb.append(", but only 0x");
        sb.append(Integer.toHexString(n2));
        sb.append(" are allowed");
        throw new IllegalArgumentException(sb.toString());
    }
    
    @NonNull
    public static <T> T checkNotNull(final T t) {
        if (t != null) {
            return t;
        }
        throw new NullPointerException();
    }
    
    @NonNull
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
    
    @NonNull
    public static <T extends CharSequence> T checkStringNotEmpty(final T t) {
        if (!TextUtils.isEmpty((CharSequence)t)) {
            return t;
        }
        throw new IllegalArgumentException();
    }
    
    @NonNull
    public static <T extends CharSequence> T checkStringNotEmpty(final T t, final Object o) {
        if (!TextUtils.isEmpty((CharSequence)t)) {
            return t;
        }
        throw new IllegalArgumentException(String.valueOf(o));
    }
}
