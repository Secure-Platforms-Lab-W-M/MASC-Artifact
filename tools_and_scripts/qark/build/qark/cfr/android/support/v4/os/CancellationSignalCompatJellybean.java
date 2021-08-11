/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.CancellationSignal
 */
package android.support.v4.os;

import android.os.CancellationSignal;

class CancellationSignalCompatJellybean {
    CancellationSignalCompatJellybean() {
    }

    public static void cancel(Object object) {
        ((CancellationSignal)object).cancel();
    }

    public static Object create() {
        return new CancellationSignal();
    }
}

