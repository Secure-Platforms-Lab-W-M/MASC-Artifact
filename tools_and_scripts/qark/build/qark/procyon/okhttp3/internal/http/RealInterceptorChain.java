// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.http;

import okhttp3.internal.Util;
import java.util.concurrent.TimeUnit;
import java.io.IOException;
import okhttp3.Response;
import okhttp3.Connection;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.Request;
import java.util.List;
import okhttp3.EventListener;
import okhttp3.internal.connection.RealConnection;
import okhttp3.Call;
import okhttp3.Interceptor;

public final class RealInterceptorChain implements Chain
{
    private final Call call;
    private int calls;
    private final int connectTimeout;
    private final RealConnection connection;
    private final EventListener eventListener;
    private final HttpCodec httpCodec;
    private final int index;
    private final List<Interceptor> interceptors;
    private final int readTimeout;
    private final Request request;
    private final StreamAllocation streamAllocation;
    private final int writeTimeout;
    
    public RealInterceptorChain(final List<Interceptor> interceptors, final StreamAllocation streamAllocation, final HttpCodec httpCodec, final RealConnection connection, final int index, final Request request, final Call call, final EventListener eventListener, final int connectTimeout, final int readTimeout, final int writeTimeout) {
        this.interceptors = interceptors;
        this.connection = connection;
        this.streamAllocation = streamAllocation;
        this.httpCodec = httpCodec;
        this.index = index;
        this.request = request;
        this.call = call;
        this.eventListener = eventListener;
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
        this.writeTimeout = writeTimeout;
    }
    
    @Override
    public Call call() {
        return this.call;
    }
    
    @Override
    public int connectTimeoutMillis() {
        return this.connectTimeout;
    }
    
    @Override
    public Connection connection() {
        return this.connection;
    }
    
    public EventListener eventListener() {
        return this.eventListener;
    }
    
    public HttpCodec httpStream() {
        return this.httpCodec;
    }
    
    @Override
    public Response proceed(final Request request) throws IOException {
        return this.proceed(request, this.streamAllocation, this.httpCodec, this.connection);
    }
    
    public Response proceed(final Request request, final StreamAllocation streamAllocation, final HttpCodec httpCodec, final RealConnection realConnection) throws IOException {
        if (this.index >= this.interceptors.size()) {
            throw new AssertionError();
        }
        ++this.calls;
        if (this.httpCodec != null && !this.connection.supportsUrl(request.url())) {
            throw new IllegalStateException("network interceptor " + this.interceptors.get(this.index - 1) + " must retain the same host and port");
        }
        if (this.httpCodec != null && this.calls > 1) {
            throw new IllegalStateException("network interceptor " + this.interceptors.get(this.index - 1) + " must call proceed() exactly once");
        }
        final RealInterceptorChain realInterceptorChain = new RealInterceptorChain(this.interceptors, streamAllocation, httpCodec, realConnection, this.index + 1, request, this.call, this.eventListener, this.connectTimeout, this.readTimeout, this.writeTimeout);
        final Interceptor interceptor = this.interceptors.get(this.index);
        final Response intercept = interceptor.intercept((Interceptor.Chain)realInterceptorChain);
        if (httpCodec != null && this.index + 1 < this.interceptors.size() && realInterceptorChain.calls != 1) {
            throw new IllegalStateException("network interceptor " + interceptor + " must call proceed() exactly once");
        }
        if (intercept == null) {
            throw new NullPointerException("interceptor " + interceptor + " returned null");
        }
        if (intercept.body() == null) {
            throw new IllegalStateException("interceptor " + interceptor + " returned a response with no body");
        }
        return intercept;
    }
    
    @Override
    public int readTimeoutMillis() {
        return this.readTimeout;
    }
    
    @Override
    public Request request() {
        return this.request;
    }
    
    public StreamAllocation streamAllocation() {
        return this.streamAllocation;
    }
    
    @Override
    public Chain withConnectTimeout(int checkDuration, final TimeUnit timeUnit) {
        checkDuration = Util.checkDuration("timeout", checkDuration, timeUnit);
        return new RealInterceptorChain(this.interceptors, this.streamAllocation, this.httpCodec, this.connection, this.index, this.request, this.call, this.eventListener, checkDuration, this.readTimeout, this.writeTimeout);
    }
    
    @Override
    public Chain withReadTimeout(int checkDuration, final TimeUnit timeUnit) {
        checkDuration = Util.checkDuration("timeout", checkDuration, timeUnit);
        return new RealInterceptorChain(this.interceptors, this.streamAllocation, this.httpCodec, this.connection, this.index, this.request, this.call, this.eventListener, this.connectTimeout, checkDuration, this.writeTimeout);
    }
    
    @Override
    public Chain withWriteTimeout(int checkDuration, final TimeUnit timeUnit) {
        checkDuration = Util.checkDuration("timeout", checkDuration, timeUnit);
        return new RealInterceptorChain(this.interceptors, this.streamAllocation, this.httpCodec, this.connection, this.index, this.request, this.call, this.eventListener, this.connectTimeout, this.readTimeout, checkDuration);
    }
    
    @Override
    public int writeTimeoutMillis() {
        return this.writeTimeout;
    }
}
