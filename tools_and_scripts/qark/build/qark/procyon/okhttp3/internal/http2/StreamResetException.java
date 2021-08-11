// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.http2;

import java.io.IOException;

public final class StreamResetException extends IOException
{
    public final ErrorCode errorCode;
    
    public StreamResetException(final ErrorCode errorCode) {
        super("stream was reset: " + errorCode);
        this.errorCode = errorCode;
    }
}
