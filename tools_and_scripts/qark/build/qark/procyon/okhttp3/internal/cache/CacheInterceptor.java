// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.cache;

import okhttp3.Request;
import okhttp3.internal.http.HttpMethod;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.Protocol;
import java.io.Closeable;
import okhttp3.internal.Internal;
import okhttp3.Headers;
import okio.Sink;
import okhttp3.ResponseBody;
import okhttp3.internal.http.RealResponseBody;
import okio.Timeout;
import okio.Buffer;
import java.io.IOException;
import okhttp3.internal.Util;
import java.util.concurrent.TimeUnit;
import okio.BufferedSource;
import okio.BufferedSink;
import okio.Source;
import okio.Okio;
import okhttp3.Response;
import okhttp3.Interceptor;

public final class CacheInterceptor implements Interceptor
{
    final InternalCache cache;
    
    public CacheInterceptor(final InternalCache cache) {
        this.cache = cache;
    }
    
    private Response cacheWritingResponse(final CacheRequest cacheRequest, final Response response) throws IOException {
        if (cacheRequest != null) {
            final Sink body = cacheRequest.body();
            if (body != null) {
                return response.newBuilder().body(new RealResponseBody(response.header("Content-Type"), response.body().contentLength(), Okio.buffer(new Source() {
                    boolean cacheRequestClosed;
                    final /* synthetic */ BufferedSink val$cacheBody = Okio.buffer(body);
                    final /* synthetic */ BufferedSource val$source = response.body().source();
                    
                    @Override
                    public void close() throws IOException {
                        if (!this.cacheRequestClosed && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                            this.cacheRequestClosed = true;
                            cacheRequest.abort();
                        }
                        this.val$source.close();
                    }
                    
                    @Override
                    public long read(final Buffer buffer, long read) throws IOException {
                        try {
                            read = this.val$source.read(buffer, read);
                            if (read == -1L) {
                                if (!this.cacheRequestClosed) {
                                    this.cacheRequestClosed = true;
                                    this.val$cacheBody.close();
                                }
                                return -1L;
                            }
                        }
                        catch (IOException ex) {
                            if (!this.cacheRequestClosed) {
                                this.cacheRequestClosed = true;
                                cacheRequest.abort();
                            }
                            throw ex;
                        }
                        buffer.copyTo(this.val$cacheBody.buffer(), buffer.size() - read, read);
                        this.val$cacheBody.emitCompleteSegments();
                        return read;
                    }
                    
                    @Override
                    public Timeout timeout() {
                        return this.val$source.timeout();
                    }
                }))).build();
            }
        }
        return response;
    }
    
    private static Headers combine(final Headers headers, final Headers headers2) {
        final Headers.Builder builder = new Headers.Builder();
        for (int i = 0; i < headers.size(); ++i) {
            final String name = headers.name(i);
            final String value = headers.value(i);
            if ((!"Warning".equalsIgnoreCase(name) || !value.startsWith("1")) && (isContentSpecificHeader(name) || !isEndToEnd(name) || headers2.get(name) == null)) {
                Internal.instance.addLenient(builder, name, value);
            }
        }
        for (int j = 0; j < headers2.size(); ++j) {
            final String name2 = headers2.name(j);
            if (!isContentSpecificHeader(name2) && isEndToEnd(name2)) {
                Internal.instance.addLenient(builder, name2, headers2.value(j));
            }
        }
        return builder.build();
    }
    
    static boolean isContentSpecificHeader(final String s) {
        return "Content-Length".equalsIgnoreCase(s) || "Content-Encoding".equalsIgnoreCase(s) || "Content-Type".equalsIgnoreCase(s);
    }
    
    static boolean isEndToEnd(final String s) {
        return !"Connection".equalsIgnoreCase(s) && !"Keep-Alive".equalsIgnoreCase(s) && !"Proxy-Authenticate".equalsIgnoreCase(s) && !"Proxy-Authorization".equalsIgnoreCase(s) && !"TE".equalsIgnoreCase(s) && !"Trailers".equalsIgnoreCase(s) && !"Transfer-Encoding".equalsIgnoreCase(s) && !"Upgrade".equalsIgnoreCase(s);
    }
    
    private static Response stripBody(final Response response) {
        Response build = response;
        if (response != null) {
            build = response;
            if (response.body() != null) {
                build = response.newBuilder().body(null).build();
            }
        }
        return build;
    }
    
    @Override
    public Response intercept(final Chain chain) throws IOException {
        Response response;
        if (this.cache != null) {
            response = this.cache.get(chain.request());
        }
        else {
            response = null;
        }
        final CacheStrategy value = new CacheStrategy.Factory(System.currentTimeMillis(), chain.request(), response).get();
        final Request networkRequest = value.networkRequest;
        final Response cacheResponse = value.cacheResponse;
        if (this.cache != null) {
            this.cache.trackResponse(value);
        }
        if (response != null && cacheResponse == null) {
            Util.closeQuietly(response.body());
        }
        Response response2;
        if (networkRequest == null && cacheResponse == null) {
            response2 = new Response.Builder().request(chain.request()).protocol(Protocol.HTTP_1_1).code(504).message("Unsatisfiable Request (only-if-cached)").body(Util.EMPTY_RESPONSE).sentRequestAtMillis(-1L).receivedResponseAtMillis(System.currentTimeMillis()).build();
        }
        else {
            if (networkRequest == null) {
                return cacheResponse.newBuilder().cacheResponse(stripBody(cacheResponse)).build();
            }
            Label_0331: {
                try {
                    final Response proceed = chain.proceed(networkRequest);
                    if (proceed == null && response != null) {
                        Util.closeQuietly(response.body());
                    }
                    if (cacheResponse == null) {
                        break Label_0331;
                    }
                    if (proceed.code() == 304) {
                        response = cacheResponse.newBuilder().headers(combine(cacheResponse.headers(), proceed.headers())).sentRequestAtMillis(proceed.sentRequestAtMillis()).receivedResponseAtMillis(proceed.receivedResponseAtMillis()).cacheResponse(stripBody(cacheResponse)).networkResponse(stripBody(proceed)).build();
                        proceed.body().close();
                        this.cache.trackConditionalCacheHit();
                        this.cache.update(cacheResponse, response);
                        return response;
                    }
                }
                finally {
                    if (!false && response != null) {
                        Util.closeQuietly(response.body());
                    }
                }
                Util.closeQuietly(cacheResponse.body());
            }
            final Response response4;
            final Response response3 = response2 = response4.newBuilder().cacheResponse(stripBody(cacheResponse)).networkResponse(stripBody(response4)).build();
            if (this.cache != null) {
                if (HttpHeaders.hasBody(response3) && CacheStrategy.isCacheable(response3, networkRequest)) {
                    return this.cacheWritingResponse(this.cache.put(response3), response3);
                }
                response2 = response3;
                if (HttpMethod.invalidatesCache(networkRequest.method())) {
                    try {
                        this.cache.remove(networkRequest);
                        return response3;
                    }
                    catch (IOException ex) {
                        return response3;
                    }
                }
            }
        }
        return response2;
    }
}
