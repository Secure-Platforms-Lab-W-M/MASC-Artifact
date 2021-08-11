// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.http;

import okhttp3.MediaType;
import okio.BufferedSource;
import javax.annotation.Nullable;
import okhttp3.ResponseBody;

public final class RealResponseBody extends ResponseBody
{
    private final long contentLength;
    @Nullable
    private final String contentTypeString;
    private final BufferedSource source;
    
    public RealResponseBody(@Nullable final String contentTypeString, final long contentLength, final BufferedSource source) {
        this.contentTypeString = contentTypeString;
        this.contentLength = contentLength;
        this.source = source;
    }
    
    @Override
    public long contentLength() {
        return this.contentLength;
    }
    
    @Override
    public MediaType contentType() {
        if (this.contentTypeString != null) {
            return MediaType.parse(this.contentTypeString);
        }
        return null;
    }
    
    @Override
    public BufferedSource source() {
        return this.source;
    }
}
