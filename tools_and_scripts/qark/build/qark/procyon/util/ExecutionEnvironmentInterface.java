// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package util;

import java.io.IOException;
import java.io.InputStream;

public interface ExecutionEnvironmentInterface
{
    boolean debug();
    
    InputStream getAsset(final String p0) throws IOException;
    
    String getWorkDir();
    
    boolean hasNetwork();
    
    void onReload() throws IOException;
    
    void releaseAllWakeLocks();
    
    void releaseWakeLock();
    
    void wakeLock();
}
