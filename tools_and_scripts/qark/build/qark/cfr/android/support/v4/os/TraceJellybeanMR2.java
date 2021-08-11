/*
 * Decompiled with CFR 0_124.
 * 
 * Could not load the following classes:
 *  android.os.Trace
 */
package android.support.v4.os;

import android.os.Trace;

class TraceJellybeanMR2 {
    TraceJellybeanMR2() {
    }

    public static void beginSection(String string) {
        Trace.beginSection((String)string);
    }

    public static void endSection() {
        Trace.endSection();
    }
}

