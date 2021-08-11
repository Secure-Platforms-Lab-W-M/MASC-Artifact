// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.http;

import okhttp3.EventListener;
import okhttp3.Call;
import java.net.HttpRetryException;
import java.io.Closeable;
import okhttp3.internal.Util;
import okhttp3.internal.http2.ConnectionShutdownException;
import okhttp3.internal.connection.RouteException;
import okhttp3.ResponseBody;
import okhttp3.internal.connection.RealConnection;
import javax.net.ssl.SSLPeerUnverifiedException;
import java.security.cert.CertificateException;
import javax.net.ssl.SSLHandshakeException;
import java.net.SocketTimeoutException;
import java.io.InterruptedIOException;
import java.io.IOException;
import okhttp3.RequestBody;
import java.net.ProtocolException;
import java.net.Proxy;
import okhttp3.Request;
import okhttp3.Route;
import okhttp3.Response;
import okhttp3.CertificatePinner;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.Address;
import okhttp3.HttpUrl;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.OkHttpClient;
import okhttp3.Interceptor;

public final class RetryAndFollowUpInterceptor implements Interceptor
{
    private static final int MAX_FOLLOW_UPS = 20;
    private Object callStackTrace;
    private volatile boolean canceled;
    private final OkHttpClient client;
    private final boolean forWebSocket;
    private volatile StreamAllocation streamAllocation;
    
    public RetryAndFollowUpInterceptor(final OkHttpClient client, final boolean forWebSocket) {
        this.client = client;
        this.forWebSocket = forWebSocket;
    }
    
    private Address createAddress(final HttpUrl httpUrl) {
        SSLSocketFactory sslSocketFactory = null;
        HostnameVerifier hostnameVerifier = null;
        CertificatePinner certificatePinner = null;
        if (httpUrl.isHttps()) {
            sslSocketFactory = this.client.sslSocketFactory();
            hostnameVerifier = this.client.hostnameVerifier();
            certificatePinner = this.client.certificatePinner();
        }
        return new Address(httpUrl.host(), httpUrl.port(), this.client.dns(), this.client.socketFactory(), sslSocketFactory, hostnameVerifier, certificatePinner, this.client.proxyAuthenticator(), this.client.proxy(), this.client.protocols(), this.client.connectionSpecs(), this.client.proxySelector());
    }
    
    private Request followUpRequest(final Response response, final Route route) throws IOException {
        final RequestBody requestBody = null;
        if (response == null) {
            throw new IllegalStateException();
        }
        final int code = response.code();
        final String method = response.request().method();
        Label_0215: {
            switch (code) {
                case 407: {
                    Proxy proxy;
                    if (route != null) {
                        proxy = route.proxy();
                    }
                    else {
                        proxy = this.client.proxy();
                    }
                    if (proxy.type() != Proxy.Type.HTTP) {
                        throw new ProtocolException("Received HTTP_PROXY_AUTH (407) code while not using proxy");
                    }
                    return this.client.proxyAuthenticator().authenticate(route, response);
                }
                case 401: {
                    return this.client.authenticator().authenticate(route, response);
                }
                case 307:
                case 308: {
                    if (method.equals("GET") || method.equals("HEAD")) {
                        break Label_0215;
                    }
                    break;
                }
                case 300:
                case 301:
                case 302:
                case 303: {
                    if (!this.client.followRedirects()) {
                        break;
                    }
                    final String header = response.header("Location");
                    if (header == null) {
                        break;
                    }
                    final HttpUrl resolve = response.request().url().resolve(header);
                    if (resolve != null && (resolve.scheme().equals(response.request().url().scheme()) || this.client.followSslRedirects())) {
                        final Request.Builder builder = response.request().newBuilder();
                        if (HttpMethod.permitsRequestBody(method)) {
                            final boolean redirectsWithBody = HttpMethod.redirectsWithBody(method);
                            if (HttpMethod.redirectsToGet(method)) {
                                builder.method("GET", null);
                            }
                            else {
                                RequestBody body = requestBody;
                                if (redirectsWithBody) {
                                    body = response.request().body();
                                }
                                builder.method(method, body);
                            }
                            if (!redirectsWithBody) {
                                builder.removeHeader("Transfer-Encoding");
                                builder.removeHeader("Content-Length");
                                builder.removeHeader("Content-Type");
                            }
                        }
                        if (!this.sameConnection(response, resolve)) {
                            builder.removeHeader("Authorization");
                        }
                        return builder.url(resolve).build();
                    }
                    break;
                }
                case 408: {
                    if (this.client.retryOnConnectionFailure() && !(response.request().body() instanceof UnrepeatableRequestBody) && (response.priorResponse() == null || response.priorResponse().code() != 408) && this.retryAfter(response, 0) <= 0) {
                        return response.request();
                    }
                    break;
                }
                case 503: {
                    if ((response.priorResponse() == null || response.priorResponse().code() != 503) && this.retryAfter(response, Integer.MAX_VALUE) == 0) {
                        return response.request();
                    }
                    break;
                }
            }
        }
        return null;
    }
    
    private boolean isRecoverable(final IOException ex, final boolean b) {
        final boolean b2 = true;
        if (!(ex instanceof ProtocolException)) {
            if (ex instanceof InterruptedIOException) {
                return ex instanceof SocketTimeoutException && !b && b2;
            }
            if ((!(ex instanceof SSLHandshakeException) || !(ex.getCause() instanceof CertificateException)) && !(ex instanceof SSLPeerUnverifiedException)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean recover(final IOException ex, final StreamAllocation streamAllocation, final boolean b, final Request request) {
        streamAllocation.streamFailed(ex);
        return this.client.retryOnConnectionFailure() && (!b || !(request.body() instanceof UnrepeatableRequestBody)) && this.isRecoverable(ex, b) && streamAllocation.hasMoreRoutes();
    }
    
    private int retryAfter(final Response response, final int n) {
        final String header = response.header("Retry-After");
        if (header == null) {
            return n;
        }
        if (header.matches("\\d+")) {
            return Integer.valueOf(header);
        }
        return Integer.MAX_VALUE;
    }
    
    private boolean sameConnection(final Response response, final HttpUrl httpUrl) {
        final HttpUrl url = response.request().url();
        return url.host().equals(httpUrl.host()) && url.port() == httpUrl.port() && url.scheme().equals(httpUrl.scheme());
    }
    
    public void cancel() {
        this.canceled = true;
        final StreamAllocation streamAllocation = this.streamAllocation;
        if (streamAllocation != null) {
            streamAllocation.cancel();
        }
    }
    
    @Override
    public Response intercept(Chain build) throws IOException {
        final Request request = ((Chain)build).request();
        final RealInterceptorChain realInterceptorChain = (RealInterceptorChain)build;
        final Call call = realInterceptorChain.call();
        final EventListener eventListener = realInterceptorChain.eventListener();
        StreamAllocation streamAllocation = new StreamAllocation(this.client.connectionPool(), this.createAddress(request.url()), call, eventListener, this.callStackTrace);
        this.streamAllocation = streamAllocation;
        int n = 0;
        Object o = null;
        build = request;
        Object o2 = o;
        while (!this.canceled) {
            try {
                o = realInterceptorChain.proceed((Request)build, streamAllocation, null, null);
                if (false) {
                    streamAllocation.streamFailed(null);
                    streamAllocation.release();
                }
                build = o;
                if (o2 != null) {
                    build = ((Response)o).newBuilder().priorResponse(((Response)o2).newBuilder().body(null).build()).build();
                }
                o = this.followUpRequest((Response)build, streamAllocation.route());
                if (o == null) {
                    if (!this.forWebSocket) {
                        streamAllocation.release();
                    }
                    return (Response)build;
                }
            }
            catch (RouteException o) {
                if (!this.recover(((RouteException)o).getLastConnectException(), streamAllocation, false, (Request)build)) {
                    throw ((RouteException)o).getLastConnectException();
                }
                goto Label_0234;
            }
            catch (IOException o) {
                if (!this.recover((IOException)o, streamAllocation, !(o instanceof ConnectionShutdownException), (Request)build)) {
                    throw o;
                }
                if (false) {
                    streamAllocation.streamFailed(null);
                    streamAllocation.release();
                    continue;
                }
                continue;
            }
            finally {
                if (true) {
                    streamAllocation.streamFailed(null);
                    streamAllocation.release();
                }
            }
            Util.closeQuietly(((Response)build).body());
            ++n;
            if (n > 20) {
                streamAllocation.release();
                throw new ProtocolException("Too many follow-up requests: " + n);
            }
            if (((Request)o).body() instanceof UnrepeatableRequestBody) {
                streamAllocation.release();
                throw new HttpRetryException("Cannot retry streamed HTTP body", ((Response)build).code());
            }
            StreamAllocation streamAllocation2;
            if (!this.sameConnection((Response)build, ((Request)o).url())) {
                streamAllocation.release();
                streamAllocation2 = new StreamAllocation(this.client.connectionPool(), this.createAddress(((Request)o).url()), call, eventListener, this.callStackTrace);
                this.streamAllocation = streamAllocation2;
            }
            else {
                streamAllocation2 = streamAllocation;
                if (streamAllocation.codec() != null) {
                    throw new IllegalStateException("Closing the body of " + build + " didn't close its backing stream. Bad interceptor?");
                }
            }
            streamAllocation = streamAllocation2;
            o2 = build;
            build = o;
        }
        streamAllocation.release();
        throw new IOException("Canceled");
    }
    
    public boolean isCanceled() {
        return this.canceled;
    }
    
    public void setCallStackTrace(final Object callStackTrace) {
        this.callStackTrace = callStackTrace;
    }
    
    public StreamAllocation streamAllocation() {
        return this.streamAllocation;
    }
}
