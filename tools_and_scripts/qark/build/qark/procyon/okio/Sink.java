// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okio;

import java.io.IOException;
import java.io.Flushable;
import java.io.Closeable;

public interface Sink extends Closeable, Flushable
{
    void close() throws IOException;
    
    void flush() throws IOException;
    
    Timeout timeout();
    
    void write(final Buffer p0, final long p1) throws IOException;
}
