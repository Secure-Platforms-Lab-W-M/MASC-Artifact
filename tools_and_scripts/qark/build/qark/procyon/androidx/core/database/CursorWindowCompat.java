// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package androidx.core.database;

import android.os.Build$VERSION;
import android.database.CursorWindow;

public final class CursorWindowCompat
{
    private CursorWindowCompat() {
    }
    
    public static CursorWindow create(final String s, final long n) {
        if (Build$VERSION.SDK_INT >= 28) {
            return new CursorWindow(s, n);
        }
        if (Build$VERSION.SDK_INT >= 15) {
            return new CursorWindow(s);
        }
        return new CursorWindow(false);
    }
}
