// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okio;

import java.io.IOException;
import java.io.Closeable;

public interface Source extends Closeable
{
    void close() throws IOException;
    
    long read(final Buffer p0, final long p1) throws IOException;
    
    Timeout timeout();
}
