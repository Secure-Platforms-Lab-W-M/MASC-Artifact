// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.http;

import okio.Buffer;
import okio.ForwardingSink;
import java.io.IOException;
import okio.BufferedSink;
import okhttp3.Request;
import okhttp3.internal.connection.StreamAllocation;
import java.net.ProtocolException;
import okhttp3.internal.Util;
import okio.Sink;
import okio.Okio;
import okhttp3.internal.connection.RealConnection;
import okhttp3.Response;
import okhttp3.Interceptor;

public final class CallServerInterceptor implements Interceptor
{
    private final boolean forWebSocket;
    
    public CallServerInterceptor(final boolean forWebSocket) {
        this.forWebSocket = forWebSocket;
    }
    
    @Override
    public Response intercept(final Chain chain) throws IOException {
        final RealInterceptorChain realInterceptorChain = (RealInterceptorChain)chain;
        final HttpCodec httpStream = realInterceptorChain.httpStream();
        final StreamAllocation streamAllocation = realInterceptorChain.streamAllocation();
        final RealConnection realConnection = (RealConnection)realInterceptorChain.connection();
        final Request request = realInterceptorChain.request();
        final long currentTimeMillis = System.currentTimeMillis();
        realInterceptorChain.eventListener().requestHeadersStart(realInterceptorChain.call());
        httpStream.writeRequestHeaders(request);
        realInterceptorChain.eventListener().requestHeadersEnd(realInterceptorChain.call(), request);
        final Response.Builder builder = null;
        Response.Builder responseHeaders = null;
        Response.Builder builder2 = builder;
        if (HttpMethod.permitsRequestBody(request.method())) {
            builder2 = builder;
            if (request.body() != null) {
                if ("100-continue".equalsIgnoreCase(request.header("Expect"))) {
                    httpStream.flushRequest();
                    realInterceptorChain.eventListener().responseHeadersStart(realInterceptorChain.call());
                    responseHeaders = httpStream.readResponseHeaders(true);
                }
                if (responseHeaders == null) {
                    realInterceptorChain.eventListener().requestBodyStart(realInterceptorChain.call());
                    final CountingSink countingSink = new CountingSink(httpStream.createRequestBody(request, request.body().contentLength()));
                    final BufferedSink buffer = Okio.buffer(countingSink);
                    request.body().writeTo(buffer);
                    buffer.close();
                    realInterceptorChain.eventListener().requestBodyEnd(realInterceptorChain.call(), countingSink.successfulCount);
                    builder2 = responseHeaders;
                }
                else {
                    builder2 = responseHeaders;
                    if (!realConnection.isMultiplexed()) {
                        streamAllocation.noNewStreams();
                        builder2 = responseHeaders;
                    }
                }
            }
        }
        httpStream.finishRequest();
        Object responseHeaders2;
        if ((responseHeaders2 = builder2) == null) {
            realInterceptorChain.eventListener().responseHeadersStart(realInterceptorChain.call());
            responseHeaders2 = httpStream.readResponseHeaders(false);
        }
        Response response = ((Response.Builder)responseHeaders2).request(request).handshake(streamAllocation.connection().handshake()).sentRequestAtMillis(currentTimeMillis).receivedResponseAtMillis(System.currentTimeMillis()).build();
        int n;
        if ((n = response.code()) == 100) {
            response = httpStream.readResponseHeaders(false).request(request).handshake(streamAllocation.connection().handshake()).sentRequestAtMillis(currentTimeMillis).receivedResponseAtMillis(System.currentTimeMillis()).build();
            n = response.code();
        }
        realInterceptorChain.eventListener().responseHeadersEnd(realInterceptorChain.call(), response);
        Response response2;
        if (this.forWebSocket && n == 101) {
            response2 = response.newBuilder().body(Util.EMPTY_RESPONSE).build();
        }
        else {
            response2 = response.newBuilder().body(httpStream.openResponseBody(response)).build();
        }
        if ("close".equalsIgnoreCase(response2.request().header("Connection")) || "close".equalsIgnoreCase(response2.header("Connection"))) {
            streamAllocation.noNewStreams();
        }
        if ((n == 204 || n == 205) && response2.body().contentLength() > 0L) {
            throw new ProtocolException("HTTP " + n + " had non-zero Content-Length: " + response2.body().contentLength());
        }
        return response2;
    }
    
    static final class CountingSink extends ForwardingSink
    {
        long successfulCount;
        
        CountingSink(final Sink sink) {
            super(sink);
        }
        
        @Override
        public void write(final Buffer buffer, final long n) throws IOException {
            super.write(buffer, n);
            this.successfulCount += n;
        }
    }
}
