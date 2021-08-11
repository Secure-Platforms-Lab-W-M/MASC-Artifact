// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3;

import java.security.cert.CertificateEncodingException;
import okio.BufferedSink;
import java.security.cert.CertificateException;
import okio.Buffer;
import java.util.ArrayList;
import java.security.cert.CertificateFactory;
import java.util.Collections;
import java.security.cert.Certificate;
import java.util.List;
import okhttp3.internal.http.StatusLine;
import okhttp3.internal.platform.Platform;
import okio.Source;
import okio.ForwardingSource;
import okio.ForwardingSink;
import okio.Sink;
import java.util.NoSuchElementException;
import okio.Okio;
import java.util.Iterator;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.Util;
import okio.BufferedSource;
import okio.ByteString;
import javax.annotation.Nullable;
import okhttp3.internal.cache.CacheStrategy;
import okhttp3.internal.cache.CacheRequest;
import java.io.IOException;
import okhttp3.internal.io.FileSystem;
import java.io.File;
import okhttp3.internal.cache.InternalCache;
import okhttp3.internal.cache.DiskLruCache;
import java.io.Flushable;
import java.io.Closeable;

public final class Cache implements Closeable, Flushable
{
    private static final int ENTRY_BODY = 1;
    private static final int ENTRY_COUNT = 2;
    private static final int ENTRY_METADATA = 0;
    private static final int VERSION = 201105;
    final DiskLruCache cache;
    private int hitCount;
    final InternalCache internalCache;
    private int networkCount;
    private int requestCount;
    int writeAbortCount;
    int writeSuccessCount;
    
    public Cache(final File file, final long n) {
        this(file, n, FileSystem.SYSTEM);
    }
    
    Cache(final File file, final long n, final FileSystem fileSystem) {
        this.internalCache = new InternalCache() {
            @Override
            public Response get(final Request request) throws IOException {
                return Cache.this.get(request);
            }
            
            @Override
            public CacheRequest put(final Response response) throws IOException {
                return Cache.this.put(response);
            }
            
            @Override
            public void remove(final Request request) throws IOException {
                Cache.this.remove(request);
            }
            
            @Override
            public void trackConditionalCacheHit() {
                Cache.this.trackConditionalCacheHit();
            }
            
            @Override
            public void trackResponse(final CacheStrategy cacheStrategy) {
                Cache.this.trackResponse(cacheStrategy);
            }
            
            @Override
            public void update(final Response response, final Response response2) {
                Cache.this.update(response, response2);
            }
        };
        this.cache = DiskLruCache.create(fileSystem, file, 201105, 2, n);
    }
    
    private void abortQuietly(@Nullable final DiskLruCache.Editor editor) {
        if (editor == null) {
            return;
        }
        try {
            editor.abort();
        }
        catch (IOException ex) {}
    }
    
    public static String key(final HttpUrl httpUrl) {
        return ByteString.encodeUtf8(httpUrl.toString()).md5().hex();
    }
    
    static int readInt(final BufferedSource bufferedSource) throws IOException {
        long decimalLong;
        try {
            decimalLong = bufferedSource.readDecimalLong();
            final String utf8LineStrict = bufferedSource.readUtf8LineStrict();
            if (decimalLong < 0L || decimalLong > 2147483647L || !utf8LineStrict.isEmpty()) {
                throw new IOException("expected an int but was \"" + decimalLong + utf8LineStrict + "\"");
            }
        }
        catch (NumberFormatException ex) {
            throw new IOException(ex.getMessage());
        }
        return (int)decimalLong;
    }
    
    @Override
    public void close() throws IOException {
        this.cache.close();
    }
    
    public void delete() throws IOException {
        this.cache.delete();
    }
    
    public File directory() {
        return this.cache.getDirectory();
    }
    
    public void evictAll() throws IOException {
        this.cache.evictAll();
    }
    
    @Override
    public void flush() throws IOException {
        this.cache.flush();
    }
    
    @Nullable
    Response get(final Request request) {
        while (true) {
            final String key = key(request.url());
            Closeable closeable;
            try {
                closeable = this.cache.get(key);
                if (closeable == null) {
                    closeable = null;
                    return (Response)closeable;
                }
            }
            catch (IOException ex) {
                return null;
            }
            try {
                final Entry entry = new Entry(((DiskLruCache.Snapshot)closeable).getSource(0));
                final Response response = (Response)(closeable = entry.response((DiskLruCache.Snapshot)closeable));
                if (!entry.matches(request, response)) {
                    Util.closeQuietly(response.body());
                    return null;
                }
                return (Response)closeable;
            }
            catch (IOException ex2) {
                Util.closeQuietly(closeable);
                return null;
            }
        }
    }
    
    public int hitCount() {
        synchronized (this) {
            return this.hitCount;
        }
    }
    
    public void initialize() throws IOException {
        this.cache.initialize();
    }
    
    public boolean isClosed() {
        return this.cache.isClosed();
    }
    
    public long maxSize() {
        return this.cache.getMaxSize();
    }
    
    public int networkCount() {
        synchronized (this) {
            return this.networkCount;
        }
    }
    
    @Nullable
    CacheRequest put(final Response response) {
        final String method = response.request().method();
        Label_0031: {
            if (!HttpMethod.invalidatesCache(response.request().method())) {
                break Label_0031;
            }
            try {
                this.remove(response.request());
                Label_0029: {
                    return null;
                }
                while (true) {
                    final Entry entry = new Entry(response);
                    DiskLruCache.Editor editor = null;
                    try {
                        final DiskLruCache.Editor edit = this.cache.edit(key(response.request().url()));
                        if (edit != null) {
                            editor = edit;
                            entry.writeTo(edit);
                            editor = edit;
                            return new CacheRequestImpl(edit);
                        }
                        return null;
                    }
                    catch (IOException ex) {
                        this.abortQuietly(editor);
                        return null;
                    }
                    continue;
                }
            }
            // iftrue(Label_0029:, !method.equals((Object)"GET") || HttpHeaders.hasVaryAll(response))
            catch (IOException ex2) {
                return null;
            }
        }
    }
    
    void remove(final Request request) throws IOException {
        this.cache.remove(key(request.url()));
    }
    
    public int requestCount() {
        synchronized (this) {
            return this.requestCount;
        }
    }
    
    public long size() throws IOException {
        return this.cache.size();
    }
    
    void trackConditionalCacheHit() {
        synchronized (this) {
            ++this.hitCount;
        }
    }
    
    void trackResponse(final CacheStrategy cacheStrategy) {
        synchronized (this) {
            ++this.requestCount;
            if (cacheStrategy.networkRequest != null) {
                ++this.networkCount;
            }
            else if (cacheStrategy.cacheResponse != null) {
                ++this.hitCount;
            }
        }
    }
    
    void update(Response response, final Response response2) {
        final Entry entry = new Entry(response2);
        final DiskLruCache.Snapshot snapshot = ((CacheResponseBody)response.body()).snapshot;
        response = null;
        try {
            final Object edit = snapshot.edit();
            if (edit != null) {
                response = (Response)edit;
                entry.writeTo((DiskLruCache.Editor)edit);
                response = (Response)edit;
                ((DiskLruCache.Editor)edit).commit();
            }
        }
        catch (IOException ex) {
            this.abortQuietly((DiskLruCache.Editor)response);
        }
    }
    
    public Iterator<String> urls() throws IOException {
        return new Iterator<String>() {
            boolean canRemove;
            final Iterator<DiskLruCache.Snapshot> delegate = Cache.this.cache.snapshots();
            @Nullable
            String nextUrl;
            
            @Override
            public boolean hasNext() {
                if (this.nextUrl != null) {
                    return true;
                }
                this.canRemove = false;
                while (this.delegate.hasNext()) {
                    final DiskLruCache.Snapshot snapshot = this.delegate.next();
                    try {
                        this.nextUrl = Okio.buffer(snapshot.getSource(0)).readUtf8LineStrict();
                        return true;
                    }
                    catch (IOException ex) {
                        continue;
                    }
                    finally {
                        snapshot.close();
                    }
                    break;
                }
                return false;
            }
            
            @Override
            public String next() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final String nextUrl = this.nextUrl;
                this.nextUrl = null;
                this.canRemove = true;
                return nextUrl;
            }
            
            @Override
            public void remove() {
                if (!this.canRemove) {
                    throw new IllegalStateException("remove() before next()");
                }
                this.delegate.remove();
            }
        };
    }
    
    public int writeAbortCount() {
        synchronized (this) {
            return this.writeAbortCount;
        }
    }
    
    public int writeSuccessCount() {
        synchronized (this) {
            return this.writeSuccessCount;
        }
    }
    
    private final class CacheRequestImpl implements CacheRequest
    {
        private Sink body;
        private Sink cacheOut;
        boolean done;
        private final DiskLruCache.Editor editor;
        
        CacheRequestImpl(final DiskLruCache.Editor editor) {
            this.editor = editor;
            this.cacheOut = editor.newSink(1);
            this.body = new ForwardingSink(this.cacheOut) {
                @Override
                public void close() throws IOException {
                    synchronized (Cache.this) {
                        if (CacheRequestImpl.this.done) {
                            return;
                        }
                        CacheRequestImpl.this.done = true;
                        final Cache this$0 = Cache.this;
                        ++this$0.writeSuccessCount;
                        // monitorexit(this.this$1.this$0)
                        super.close();
                        editor.commit();
                    }
                }
            };
        }
        
        @Override
        public void abort() {
            synchronized (Cache.this) {
                if (this.done) {
                    return;
                }
                this.done = true;
                final Cache this$0 = Cache.this;
                ++this$0.writeAbortCount;
                // monitorexit(this.this$0)
                Util.closeQuietly(this.cacheOut);
                try {
                    this.editor.abort();
                }
                catch (IOException o) {}
            }
        }
        
        @Override
        public Sink body() {
            return this.body;
        }
    }
    
    private static class CacheResponseBody extends ResponseBody
    {
        private final BufferedSource bodySource;
        @Nullable
        private final String contentLength;
        @Nullable
        private final String contentType;
        final DiskLruCache.Snapshot snapshot;
        
        CacheResponseBody(final DiskLruCache.Snapshot snapshot, final String contentType, final String contentLength) {
            this.snapshot = snapshot;
            this.contentType = contentType;
            this.contentLength = contentLength;
            this.bodySource = Okio.buffer(new ForwardingSource(snapshot.getSource(1)) {
                @Override
                public void close() throws IOException {
                    snapshot.close();
                    super.close();
                }
            });
        }
        
        @Override
        public long contentLength() {
            long long1 = -1L;
            try {
                if (this.contentLength != null) {
                    long1 = Long.parseLong(this.contentLength);
                }
                return long1;
            }
            catch (NumberFormatException ex) {
                return -1L;
            }
        }
        
        @Override
        public MediaType contentType() {
            if (this.contentType != null) {
                return MediaType.parse(this.contentType);
            }
            return null;
        }
        
        @Override
        public BufferedSource source() {
            return this.bodySource;
        }
    }
    
    private static final class Entry
    {
        private static final String RECEIVED_MILLIS;
        private static final String SENT_MILLIS;
        private final int code;
        @Nullable
        private final Handshake handshake;
        private final String message;
        private final Protocol protocol;
        private final long receivedResponseMillis;
        private final String requestMethod;
        private final Headers responseHeaders;
        private final long sentRequestMillis;
        private final String url;
        private final Headers varyHeaders;
        
        static {
            SENT_MILLIS = Platform.get().getPrefix() + "-Sent-Millis";
            RECEIVED_MILLIS = Platform.get().getPrefix() + "-Received-Millis";
        }
        
        Entry(final Response response) {
            this.url = response.request().url().toString();
            this.varyHeaders = HttpHeaders.varyHeaders(response);
            this.requestMethod = response.request().method();
            this.protocol = response.protocol();
            this.code = response.code();
            this.message = response.message();
            this.responseHeaders = response.headers();
            this.handshake = response.handshake();
            this.sentRequestMillis = response.sentRequestAtMillis();
            this.receivedResponseMillis = response.receivedResponseAtMillis();
        }
        
        Entry(final Source source) throws IOException {
        Label_0232_Outer:
            while (true) {
                Label_0407: {
                    BufferedSource buffer = null;
                Label_0327:
                    while (true) {
                    Label_0321:
                        while (true) {
                            try {
                                buffer = Okio.buffer(source);
                                this.url = buffer.readUtf8LineStrict();
                                this.requestMethod = buffer.readUtf8LineStrict();
                                Object o = new Headers.Builder();
                                for (int int1 = Cache.readInt(buffer), i = 0; i < int1; ++i) {
                                    ((Headers.Builder)o).addLenient(buffer.readUtf8LineStrict());
                                }
                                this.varyHeaders = ((Headers.Builder)o).build();
                                o = StatusLine.parse(buffer.readUtf8LineStrict());
                                this.protocol = ((StatusLine)o).protocol;
                                this.code = ((StatusLine)o).code;
                                this.message = ((StatusLine)o).message;
                                o = new Headers.Builder();
                                for (int int2 = Cache.readInt(buffer), j = 0; j < int2; ++j) {
                                    ((Headers.Builder)o).addLenient(buffer.readUtf8LineStrict());
                                }
                                final String value = ((Headers.Builder)o).get(Entry.SENT_MILLIS);
                                final String value2 = ((Headers.Builder)o).get(Entry.RECEIVED_MILLIS);
                                ((Headers.Builder)o).removeAll(Entry.SENT_MILLIS);
                                ((Headers.Builder)o).removeAll(Entry.RECEIVED_MILLIS);
                                if (value != null) {
                                    final long long1 = Long.parseLong(value);
                                    this.sentRequestMillis = long1;
                                    if (value2 == null) {
                                        break Label_0321;
                                    }
                                    final long long2 = Long.parseLong(value2);
                                    this.receivedResponseMillis = long2;
                                    this.responseHeaders = ((Headers.Builder)o).build();
                                    if (!this.isHttps()) {
                                        break Label_0407;
                                    }
                                    o = buffer.readUtf8LineStrict();
                                    if (((String)o).length() > 0) {
                                        throw new IOException("expected \"\" but was \"" + (String)o + "\"");
                                    }
                                    break Label_0327;
                                }
                            }
                            finally {
                                source.close();
                            }
                            final long long1 = 0L;
                            continue Label_0232_Outer;
                        }
                        final long long2 = 0L;
                        continue;
                    }
                    final CipherSuite forJavaName = CipherSuite.forJavaName(buffer.readUtf8LineStrict());
                    final List<Certificate> certificateList = this.readCertificateList(buffer);
                    final List<Certificate> certificateList2 = this.readCertificateList(buffer);
                    TlsVersion tlsVersion;
                    if (!buffer.exhausted()) {
                        tlsVersion = TlsVersion.forJavaName(buffer.readUtf8LineStrict());
                    }
                    else {
                        tlsVersion = TlsVersion.SSL_3_0;
                    }
                    this.handshake = Handshake.get(tlsVersion, forJavaName, certificateList, certificateList2);
                    break;
                }
                this.handshake = null;
                break;
            }
            source.close();
        }
        
        private boolean isHttps() {
            return this.url.startsWith("https://");
        }
        
        private List<Certificate> readCertificateList(final BufferedSource bufferedSource) throws IOException {
            final int int1 = Cache.readInt(bufferedSource);
            List<Certificate> emptyList;
            if (int1 == -1) {
                emptyList = Collections.emptyList();
            }
            else {
                try {
                    final CertificateFactory instance = CertificateFactory.getInstance("X.509");
                    final ArrayList list = new ArrayList<Certificate>(int1);
                    int n = 0;
                    while (true) {
                        emptyList = (List<Certificate>)list;
                        if (n >= int1) {
                            break;
                        }
                        final String utf8LineStrict = bufferedSource.readUtf8LineStrict();
                        final Buffer buffer = new Buffer();
                        buffer.write(ByteString.decodeBase64(utf8LineStrict));
                        list.add(instance.generateCertificate(buffer.inputStream()));
                        ++n;
                    }
                }
                catch (CertificateException ex) {
                    throw new IOException(ex.getMessage());
                }
            }
            return emptyList;
        }
        
        private void writeCertList(final BufferedSink bufferedSink, final List<Certificate> list) throws IOException {
            try {
                bufferedSink.writeDecimalLong(list.size()).writeByte(10);
                for (int i = 0; i < list.size(); ++i) {
                    bufferedSink.writeUtf8(ByteString.of(list.get(i).getEncoded()).base64()).writeByte(10);
                }
            }
            catch (CertificateEncodingException ex) {
                throw new IOException(ex.getMessage());
            }
        }
        
        public boolean matches(final Request request, final Response response) {
            return this.url.equals(request.url().toString()) && this.requestMethod.equals(request.method()) && HttpHeaders.varyMatches(response, this.varyHeaders, request);
        }
        
        public Response response(final DiskLruCache.Snapshot snapshot) {
            return new Response.Builder().request(new Request.Builder().url(this.url).method(this.requestMethod, null).headers(this.varyHeaders).build()).protocol(this.protocol).code(this.code).message(this.message).headers(this.responseHeaders).body(new CacheResponseBody(snapshot, this.responseHeaders.get("Content-Type"), this.responseHeaders.get("Content-Length"))).handshake(this.handshake).sentRequestAtMillis(this.sentRequestMillis).receivedResponseAtMillis(this.receivedResponseMillis).build();
        }
        
        public void writeTo(final DiskLruCache.Editor editor) throws IOException {
            final BufferedSink buffer = Okio.buffer(editor.newSink(0));
            buffer.writeUtf8(this.url).writeByte(10);
            buffer.writeUtf8(this.requestMethod).writeByte(10);
            buffer.writeDecimalLong(this.varyHeaders.size()).writeByte(10);
            for (int i = 0; i < this.varyHeaders.size(); ++i) {
                buffer.writeUtf8(this.varyHeaders.name(i)).writeUtf8(": ").writeUtf8(this.varyHeaders.value(i)).writeByte(10);
            }
            buffer.writeUtf8(new StatusLine(this.protocol, this.code, this.message).toString()).writeByte(10);
            buffer.writeDecimalLong(this.responseHeaders.size() + 2).writeByte(10);
            for (int j = 0; j < this.responseHeaders.size(); ++j) {
                buffer.writeUtf8(this.responseHeaders.name(j)).writeUtf8(": ").writeUtf8(this.responseHeaders.value(j)).writeByte(10);
            }
            buffer.writeUtf8(Entry.SENT_MILLIS).writeUtf8(": ").writeDecimalLong(this.sentRequestMillis).writeByte(10);
            buffer.writeUtf8(Entry.RECEIVED_MILLIS).writeUtf8(": ").writeDecimalLong(this.receivedResponseMillis).writeByte(10);
            if (this.isHttps()) {
                buffer.writeByte(10);
                buffer.writeUtf8(this.handshake.cipherSuite().javaName()).writeByte(10);
                this.writeCertList(buffer, this.handshake.peerCertificates());
                this.writeCertList(buffer, this.handshake.localCertificates());
                buffer.writeUtf8(this.handshake.tlsVersion().javaName()).writeByte(10);
            }
            buffer.close();
        }
    }
}
