/*
 * Decompiled with CFR 0_124.
 */
package com.vladium.emma.rt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Deprecated
public final class RT {
    private RT() {
    }

    public static void dumpCoverageData(File file, boolean bl) throws IOException {
        synchronized (RT.class) {
            RT.dumpCoverageData(file, true, bl);
            return;
        }
    }

    public static void dumpCoverageData(File object, boolean bl, boolean bl2) throws IOException {
        object = new FileOutputStream((File)object, bl);
        try {
            object.write(org.jacoco.agent.rt.RT.getAgent().getExecutionData(false));
            return;
        }
        finally {
            object.close();
        }
    }
}

