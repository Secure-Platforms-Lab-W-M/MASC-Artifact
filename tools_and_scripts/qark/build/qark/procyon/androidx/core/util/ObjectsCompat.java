// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.util;

import java.util.Arrays;
import java.util.Objects;
import android.os.Build$VERSION;

public class ObjectsCompat
{
    private ObjectsCompat() {
    }
    
    public static boolean equals(final Object o, final Object o2) {
        if (Build$VERSION.SDK_INT >= 19) {
            return Objects.equals(o, o2);
        }
        return o == o2 || (o != null && o.equals(o2));
    }
    
    public static int hash(final Object... array) {
        if (Build$VERSION.SDK_INT >= 19) {
            return Objects.hash(array);
        }
        return Arrays.hashCode(array);
    }
    
    public static int hashCode(final Object o) {
        if (o != null) {
            return o.hashCode();
        }
        return 0;
    }
}
