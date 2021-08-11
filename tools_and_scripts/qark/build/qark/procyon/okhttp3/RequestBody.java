// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3;

import okio.ByteString;
import java.nio.charset.Charset;
import java.io.IOException;
import java.io.Closeable;
import okhttp3.internal.Util;
import okio.Source;
import okio.Okio;
import okio.BufferedSink;
import java.io.File;
import javax.annotation.Nullable;

public abstract class RequestBody
{
    public static RequestBody create(@Nullable final MediaType mediaType, final File file) {
        if (file == null) {
            throw new NullPointerException("content == null");
        }
        return new RequestBody() {
            @Override
            public long contentLength() {
                return file.length();
            }
            
            @Nullable
            @Override
            public MediaType contentType() {
                return mediaType;
            }
            
            @Override
            public void writeTo(final BufferedSink bufferedSink) throws IOException {
                Closeable source = null;
                try {
                    bufferedSink.writeAll((Source)(source = Okio.source(file)));
                }
                finally {
                    Util.closeQuietly(source);
                }
            }
        };
    }
    
    public static RequestBody create(@Nullable final MediaType mediaType, final String s) {
        Charset charset = Util.UTF_8;
        MediaType parse = mediaType;
        if (mediaType != null) {
            charset = mediaType.charset();
            parse = mediaType;
            if (charset == null) {
                charset = Util.UTF_8;
                parse = MediaType.parse(mediaType + "; charset=utf-8");
            }
        }
        return create(parse, s.getBytes(charset));
    }
    
    public static RequestBody create(@Nullable final MediaType mediaType, final ByteString byteString) {
        return new RequestBody() {
            @Override
            public long contentLength() throws IOException {
                return byteString.size();
            }
            
            @Nullable
            @Override
            public MediaType contentType() {
                return mediaType;
            }
            
            @Override
            public void writeTo(final BufferedSink bufferedSink) throws IOException {
                bufferedSink.write(byteString);
            }
        };
    }
    
    public static RequestBody create(@Nullable final MediaType mediaType, final byte[] array) {
        return create(mediaType, array, 0, array.length);
    }
    
    public static RequestBody create(@Nullable final MediaType mediaType, final byte[] array, final int n, final int n2) {
        if (array == null) {
            throw new NullPointerException("content == null");
        }
        Util.checkOffsetAndCount(array.length, n, n2);
        return new RequestBody() {
            @Override
            public long contentLength() {
                return n2;
            }
            
            @Nullable
            @Override
            public MediaType contentType() {
                return mediaType;
            }
            
            @Override
            public void writeTo(final BufferedSink bufferedSink) throws IOException {
                bufferedSink.write(array, n, n2);
            }
        };
    }
    
    public long contentLength() throws IOException {
        return -1L;
    }
    
    @Nullable
    public abstract MediaType contentType();
    
    public abstract void writeTo(final BufferedSink p0) throws IOException;
}
