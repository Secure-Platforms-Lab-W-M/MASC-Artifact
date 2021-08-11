/*
 * Decompiled with CFR 0_124.
 */
package util;

import java.io.IOException;
import java.io.InputStream;

public interface ExecutionEnvironmentInterface {
    public boolean debug();

    public InputStream getAsset(String var1) throws IOException;

    public String getWorkDir();

    public boolean hasNetwork();

    public void onReload() throws IOException;

    public void releaseAllWakeLocks();

    public void releaseWakeLock();

    public void wakeLock();
}

