// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.vladium.emma.rt;

import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;

@Deprecated
public final class RT
{
    private RT() {
    }
    
    public static void dumpCoverageData(final File file, final boolean b) throws IOException {
        synchronized (RT.class) {
            dumpCoverageData(file, true, b);
        }
    }
    
    public static void dumpCoverageData(File file, final boolean b, final boolean b2) throws IOException {
        file = (File)new FileOutputStream(file, b);
        try {
            ((OutputStream)file).write(org.jacoco.agent.rt.RT.getAgent().getExecutionData(false));
        }
        finally {
            ((OutputStream)file).close();
        }
    }
}
