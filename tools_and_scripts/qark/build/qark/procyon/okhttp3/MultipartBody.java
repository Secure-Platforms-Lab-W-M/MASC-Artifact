// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3;

import java.util.ArrayList;
import java.util.UUID;
import java.io.IOException;
import okio.Buffer;
import javax.annotation.Nullable;
import okio.BufferedSink;
import okhttp3.internal.Util;
import java.util.List;
import okio.ByteString;

public final class MultipartBody extends RequestBody
{
    public static final MediaType ALTERNATIVE;
    private static final byte[] COLONSPACE;
    private static final byte[] CRLF;
    private static final byte[] DASHDASH;
    public static final MediaType DIGEST;
    public static final MediaType FORM;
    public static final MediaType MIXED;
    public static final MediaType PARALLEL;
    private final ByteString boundary;
    private long contentLength;
    private final MediaType contentType;
    private final MediaType originalType;
    private final List<Part> parts;
    
    static {
        MIXED = MediaType.parse("multipart/mixed");
        ALTERNATIVE = MediaType.parse("multipart/alternative");
        DIGEST = MediaType.parse("multipart/digest");
        PARALLEL = MediaType.parse("multipart/parallel");
        FORM = MediaType.parse("multipart/form-data");
        COLONSPACE = new byte[] { 58, 32 };
        CRLF = new byte[] { 13, 10 };
        DASHDASH = new byte[] { 45, 45 };
    }
    
    MultipartBody(final ByteString boundary, final MediaType originalType, final List<Part> list) {
        this.contentLength = -1L;
        this.boundary = boundary;
        this.originalType = originalType;
        this.contentType = MediaType.parse(originalType + "; boundary=" + boundary.utf8());
        this.parts = Util.immutableList(list);
    }
    
    static StringBuilder appendQuotedString(final StringBuilder sb, final String s) {
        sb.append('\"');
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            switch (char1) {
                default: {
                    sb.append(char1);
                    break;
                }
                case 10: {
                    sb.append("%0A");
                    break;
                }
                case 13: {
                    sb.append("%0D");
                    break;
                }
                case 34: {
                    sb.append("%22");
                    break;
                }
            }
        }
        sb.append('\"');
        return sb;
    }
    
    private long writeOrCountBytes(@Nullable BufferedSink bufferedSink, final boolean b) throws IOException {
        long n = 0L;
        BufferedSink bufferedSink2 = null;
        if (b) {
            bufferedSink2 = (bufferedSink = new Buffer());
        }
        for (int i = 0; i < this.parts.size(); ++i) {
            final Part part = this.parts.get(i);
            final Headers headers = part.headers;
            final RequestBody body = part.body;
            bufferedSink.write(MultipartBody.DASHDASH);
            bufferedSink.write(this.boundary);
            bufferedSink.write(MultipartBody.CRLF);
            if (headers != null) {
                for (int j = 0; j < headers.size(); ++j) {
                    bufferedSink.writeUtf8(headers.name(j)).write(MultipartBody.COLONSPACE).writeUtf8(headers.value(j)).write(MultipartBody.CRLF);
                }
            }
            final MediaType contentType = body.contentType();
            if (contentType != null) {
                bufferedSink.writeUtf8("Content-Type: ").writeUtf8(contentType.toString()).write(MultipartBody.CRLF);
            }
            final long contentLength = body.contentLength();
            if (contentLength != -1L) {
                bufferedSink.writeUtf8("Content-Length: ").writeDecimalLong(contentLength).write(MultipartBody.CRLF);
            }
            else if (b) {
                ((Buffer)bufferedSink2).clear();
                return -1L;
            }
            bufferedSink.write(MultipartBody.CRLF);
            if (b) {
                n += contentLength;
            }
            else {
                body.writeTo(bufferedSink);
            }
            bufferedSink.write(MultipartBody.CRLF);
        }
        bufferedSink.write(MultipartBody.DASHDASH);
        bufferedSink.write(this.boundary);
        bufferedSink.write(MultipartBody.DASHDASH);
        bufferedSink.write(MultipartBody.CRLF);
        long n2 = n;
        if (b) {
            n2 = n + ((Buffer)bufferedSink2).size();
            ((Buffer)bufferedSink2).clear();
        }
        return n2;
    }
    
    public String boundary() {
        return this.boundary.utf8();
    }
    
    @Override
    public long contentLength() throws IOException {
        final long contentLength = this.contentLength;
        if (contentLength != -1L) {
            return contentLength;
        }
        return this.contentLength = this.writeOrCountBytes(null, true);
    }
    
    @Override
    public MediaType contentType() {
        return this.contentType;
    }
    
    public Part part(final int n) {
        return this.parts.get(n);
    }
    
    public List<Part> parts() {
        return this.parts;
    }
    
    public int size() {
        return this.parts.size();
    }
    
    public MediaType type() {
        return this.originalType;
    }
    
    @Override
    public void writeTo(final BufferedSink bufferedSink) throws IOException {
        this.writeOrCountBytes(bufferedSink, false);
    }
    
    public static final class Builder
    {
        private final ByteString boundary;
        private final List<Part> parts;
        private MediaType type;
        
        public Builder() {
            this(UUID.randomUUID().toString());
        }
        
        public Builder(final String s) {
            this.type = MultipartBody.MIXED;
            this.parts = new ArrayList<Part>();
            this.boundary = ByteString.encodeUtf8(s);
        }
        
        public Builder addFormDataPart(final String s, final String s2) {
            return this.addPart(Part.createFormData(s, s2));
        }
        
        public Builder addFormDataPart(final String s, @Nullable final String s2, final RequestBody requestBody) {
            return this.addPart(Part.createFormData(s, s2, requestBody));
        }
        
        public Builder addPart(@Nullable final Headers headers, final RequestBody requestBody) {
            return this.addPart(Part.create(headers, requestBody));
        }
        
        public Builder addPart(final Part part) {
            if (part == null) {
                throw new NullPointerException("part == null");
            }
            this.parts.add(part);
            return this;
        }
        
        public Builder addPart(final RequestBody requestBody) {
            return this.addPart(Part.create(requestBody));
        }
        
        public MultipartBody build() {
            if (this.parts.isEmpty()) {
                throw new IllegalStateException("Multipart body must have at least one part.");
            }
            return new MultipartBody(this.boundary, this.type, this.parts);
        }
        
        public Builder setType(final MediaType type) {
            if (type == null) {
                throw new NullPointerException("type == null");
            }
            if (!type.type().equals("multipart")) {
                throw new IllegalArgumentException("multipart != " + type);
            }
            this.type = type;
            return this;
        }
    }
    
    public static final class Part
    {
        final RequestBody body;
        @Nullable
        final Headers headers;
        
        private Part(@Nullable final Headers headers, final RequestBody body) {
            this.headers = headers;
            this.body = body;
        }
        
        public static Part create(@Nullable final Headers headers, final RequestBody requestBody) {
            if (requestBody == null) {
                throw new NullPointerException("body == null");
            }
            if (headers != null && headers.get("Content-Type") != null) {
                throw new IllegalArgumentException("Unexpected header: Content-Type");
            }
            if (headers != null && headers.get("Content-Length") != null) {
                throw new IllegalArgumentException("Unexpected header: Content-Length");
            }
            return new Part(headers, requestBody);
        }
        
        public static Part create(final RequestBody requestBody) {
            return create(null, requestBody);
        }
        
        public static Part createFormData(final String s, final String s2) {
            return createFormData(s, null, RequestBody.create(null, s2));
        }
        
        public static Part createFormData(final String s, @Nullable final String s2, final RequestBody requestBody) {
            if (s == null) {
                throw new NullPointerException("name == null");
            }
            final StringBuilder sb = new StringBuilder("form-data; name=");
            MultipartBody.appendQuotedString(sb, s);
            if (s2 != null) {
                sb.append("; filename=");
                MultipartBody.appendQuotedString(sb, s2);
            }
            return create(Headers.of("Content-Disposition", sb.toString()), requestBody);
        }
        
        public RequestBody body() {
            return this.body;
        }
        
        @Nullable
        public Headers headers() {
            return this.headers;
        }
    }
}
