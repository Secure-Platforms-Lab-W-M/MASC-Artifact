/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Build
 *  android.os.Build$VERSION
 *  android.os.Trace
 */
package android.support.v4.os;

import android.os.Build;
import android.os.Trace;

public final class TraceCompat {
    private TraceCompat() {
    }

    public static void beginSection(String string2) {
        if (Build.VERSION.SDK_INT >= 18) {
            Trace.beginSection((String)string2);
            return;
        }
    }

    public static void endSection() {
        if (Build.VERSION.SDK_INT >= 18) {
            Trace.endSection();
            return;
        }
    }
}

