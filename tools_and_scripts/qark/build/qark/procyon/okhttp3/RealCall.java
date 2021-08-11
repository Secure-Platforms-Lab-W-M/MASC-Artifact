// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3;

import okhttp3.internal.NamedRunnable;
import okhttp3.internal.connection.RealConnection;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.connection.StreamAllocation;
import java.util.List;
import okhttp3.internal.http.RealInterceptorChain;
import okhttp3.internal.http.CallServerInterceptor;
import okhttp3.internal.connection.ConnectInterceptor;
import okhttp3.internal.cache.CacheInterceptor;
import okhttp3.internal.http.BridgeInterceptor;
import java.util.Collection;
import java.util.ArrayList;
import java.io.IOException;
import okhttp3.internal.platform.Platform;
import okhttp3.internal.http.RetryAndFollowUpInterceptor;

final class RealCall implements Call
{
    final OkHttpClient client;
    private EventListener eventListener;
    private boolean executed;
    final boolean forWebSocket;
    final Request originalRequest;
    final RetryAndFollowUpInterceptor retryAndFollowUpInterceptor;
    
    private RealCall(final OkHttpClient client, final Request originalRequest, final boolean forWebSocket) {
        this.client = client;
        this.originalRequest = originalRequest;
        this.forWebSocket = forWebSocket;
        this.retryAndFollowUpInterceptor = new RetryAndFollowUpInterceptor(client, forWebSocket);
    }
    
    private void captureCallStackTrace() {
        this.retryAndFollowUpInterceptor.setCallStackTrace(Platform.get().getStackTraceForCloseable("response.body().close()"));
    }
    
    static RealCall newRealCall(final OkHttpClient okHttpClient, final Request request, final boolean b) {
        final RealCall realCall = new RealCall(okHttpClient, request, b);
        realCall.eventListener = okHttpClient.eventListenerFactory().create(realCall);
        return realCall;
    }
    
    @Override
    public void cancel() {
        this.retryAndFollowUpInterceptor.cancel();
    }
    
    @Override
    public RealCall clone() {
        return newRealCall(this.client, this.originalRequest, this.forWebSocket);
    }
    
    @Override
    public void enqueue(final Callback callback) {
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("Already Executed");
            }
        }
        this.executed = true;
        // monitorexit(this)
        this.captureCallStackTrace();
        this.eventListener.callStart(this);
        final Callback callback2;
        this.client.dispatcher().enqueue(new AsyncCall(callback2));
    }
    
    @Override
    public Response execute() throws IOException {
        synchronized (this) {
            if (this.executed) {
                throw new IllegalStateException("Already Executed");
            }
        }
        this.executed = true;
        // monitorexit(this)
        this.captureCallStackTrace();
        this.eventListener.callStart(this);
        try {
            this.client.dispatcher().executed(this);
            if (this.getResponseWithInterceptorChain() == null) {
                throw new IOException("Canceled");
            }
        }
        catch (IOException ex) {
            try {
                this.eventListener.callFailed(this, ex);
                throw ex;
            }
            finally {
                this.client.dispatcher().finished(this);
            }
        }
        this.client.dispatcher().finished(this);
        return;
    }
    
    Response getResponseWithInterceptorChain() throws IOException {
        final ArrayList<Object> list = new ArrayList<Object>();
        list.addAll(this.client.interceptors());
        list.add(this.retryAndFollowUpInterceptor);
        list.add(new BridgeInterceptor(this.client.cookieJar()));
        list.add(new CacheInterceptor(this.client.internalCache()));
        list.add(new ConnectInterceptor(this.client));
        if (!this.forWebSocket) {
            list.addAll(this.client.networkInterceptors());
        }
        list.add(new CallServerInterceptor(this.forWebSocket));
        return ((Interceptor.Chain)new RealInterceptorChain((List<Interceptor>)list, null, null, null, 0, this.originalRequest, this, this.eventListener, this.client.connectTimeoutMillis(), this.client.readTimeoutMillis(), this.client.writeTimeoutMillis())).proceed(this.originalRequest);
    }
    
    @Override
    public boolean isCanceled() {
        return this.retryAndFollowUpInterceptor.isCanceled();
    }
    
    @Override
    public boolean isExecuted() {
        synchronized (this) {
            return this.executed;
        }
    }
    
    String redactedUrl() {
        return this.originalRequest.url().redact();
    }
    
    @Override
    public Request request() {
        return this.originalRequest;
    }
    
    StreamAllocation streamAllocation() {
        return this.retryAndFollowUpInterceptor.streamAllocation();
    }
    
    String toLoggableString() {
        final StringBuilder sb = new StringBuilder();
        String s;
        if (this.isCanceled()) {
            s = "canceled ";
        }
        else {
            s = "";
        }
        final StringBuilder append = sb.append(s);
        String s2;
        if (this.forWebSocket) {
            s2 = "web socket";
        }
        else {
            s2 = "call";
        }
        return append.append(s2).append(" to ").append(this.redactedUrl()).toString();
    }
    
    final class AsyncCall extends NamedRunnable
    {
        private final Callback responseCallback;
        
        AsyncCall(final Callback responseCallback) {
            super("OkHttp %s", new Object[] { RealCall.this.redactedUrl() });
            this.responseCallback = responseCallback;
        }
        
        @Override
        protected void execute() {
            boolean b = false;
            try {
                final Response responseWithInterceptorChain = RealCall.this.getResponseWithInterceptorChain();
                b = b;
                if (RealCall.this.retryAndFollowUpInterceptor.isCanceled()) {
                    b = true;
                    this.responseCallback.onFailure(RealCall.this, new IOException("Canceled"));
                }
                else {
                    b = true;
                    this.responseCallback.onResponse(RealCall.this, responseWithInterceptorChain);
                }
            }
            catch (IOException ex) {
                if (b) {
                    Platform.get().log(4, "Callback failure for " + RealCall.this.toLoggableString(), ex);
                }
                else {
                    RealCall.this.eventListener.callFailed(RealCall.this, ex);
                    this.responseCallback.onFailure(RealCall.this, ex);
                }
            }
            finally {
                RealCall.this.client.dispatcher().finished(this);
            }
        }
        
        RealCall get() {
            return RealCall.this;
        }
        
        String host() {
            return RealCall.this.originalRequest.url().host();
        }
        
        Request request() {
            return RealCall.this.originalRequest;
        }
    }
}
