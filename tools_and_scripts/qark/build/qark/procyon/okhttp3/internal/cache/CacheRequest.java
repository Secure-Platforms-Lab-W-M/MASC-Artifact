// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.cache;

import java.io.IOException;
import okio.Sink;

public interface CacheRequest
{
    void abort();
    
    Sink body() throws IOException;
}
