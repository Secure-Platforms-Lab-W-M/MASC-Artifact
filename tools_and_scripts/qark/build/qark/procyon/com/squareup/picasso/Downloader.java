// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package com.squareup.picasso;

import java.io.InputStream;
import android.graphics.Bitmap;
import java.io.IOException;
import android.net.Uri;

public interface Downloader
{
    Response load(final Uri p0, final int p1) throws IOException;
    
    void shutdown();
    
    public static class Response
    {
        final Bitmap bitmap;
        final boolean cached;
        final long contentLength;
        final InputStream stream;
        
        @Deprecated
        public Response(final Bitmap bitmap, final boolean cached) {
            if (bitmap == null) {
                throw new IllegalArgumentException("Bitmap may not be null.");
            }
            this.stream = null;
            this.bitmap = bitmap;
            this.cached = cached;
            this.contentLength = -1L;
        }
        
        @Deprecated
        public Response(final Bitmap bitmap, final boolean b, final long n) {
            this(bitmap, b);
        }
        
        @Deprecated
        public Response(final InputStream inputStream, final boolean b) {
            this(inputStream, b, -1L);
        }
        
        public Response(final InputStream stream, final boolean cached, final long contentLength) {
            if (stream == null) {
                throw new IllegalArgumentException("Stream may not be null.");
            }
            this.stream = stream;
            this.bitmap = null;
            this.cached = cached;
            this.contentLength = contentLength;
        }
        
        @Deprecated
        public Bitmap getBitmap() {
            return this.bitmap;
        }
        
        public long getContentLength() {
            return this.contentLength;
        }
        
        public InputStream getInputStream() {
            return this.stream;
        }
    }
    
    public static class ResponseException extends IOException
    {
        final boolean localCacheOnly;
        final int responseCode;
        
        public ResponseException(final String s, final int n, final int responseCode) {
            super(s);
            this.localCacheOnly = NetworkPolicy.isOfflineOnly(n);
            this.responseCode = responseCode;
        }
    }
}
