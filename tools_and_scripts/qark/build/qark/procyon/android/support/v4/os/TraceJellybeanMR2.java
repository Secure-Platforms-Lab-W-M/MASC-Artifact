// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package android.support.v4.os;

import android.os.Trace;

class TraceJellybeanMR2
{
    public static void beginSection(final String s) {
        Trace.beginSection(s);
    }
    
    public static void endSection() {
        Trace.endSection();
    }
}
