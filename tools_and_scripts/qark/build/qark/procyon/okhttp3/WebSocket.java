// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3;

import okio.ByteString;
import javax.annotation.Nullable;

public interface WebSocket
{
    void cancel();
    
    boolean close(final int p0, @Nullable final String p1);
    
    long queueSize();
    
    Request request();
    
    boolean send(final String p0);
    
    boolean send(final ByteString p0);
    
    public interface Factory
    {
        WebSocket newWebSocket(final Request p0, final WebSocketListener p1);
    }
}
