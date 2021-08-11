/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.database.CursorWindow
 *  android.os.Build
 *  android.os.Build$VERSION
 */
package androidx.core.database;

import android.database.CursorWindow;
import android.os.Build;

public final class CursorWindowCompat {
    private CursorWindowCompat() {
    }

    public static CursorWindow create(String string2, long l) {
        if (Build.VERSION.SDK_INT >= 28) {
            return new CursorWindow(string2, l);
        }
        if (Build.VERSION.SDK_INT >= 15) {
            return new CursorWindow(string2);
        }
        return new CursorWindow(false);
    }
}

