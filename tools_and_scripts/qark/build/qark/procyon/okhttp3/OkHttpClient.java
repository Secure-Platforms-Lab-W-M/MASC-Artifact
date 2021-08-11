// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3;

import java.util.Collections;
import java.util.concurrent.TimeUnit;
import java.util.Collection;
import okhttp3.internal.tls.OkHostnameVerifier;
import java.util.ArrayList;
import okhttp3.internal.ws.RealWebSocket;
import java.util.Random;
import java.util.Arrays;
import java.security.KeyStore;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.SSLContext;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import javax.net.ssl.TrustManager;
import okhttp3.internal.platform.Platform;
import javax.net.ssl.X509TrustManager;
import java.util.Iterator;
import okhttp3.internal.connection.RouteDatabase;
import java.net.UnknownHostException;
import java.net.MalformedURLException;
import java.net.Socket;
import okhttp3.internal.connection.StreamAllocation;
import okhttp3.internal.connection.RealConnection;
import javax.net.ssl.SSLSocket;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import javax.net.ssl.SSLSocketFactory;
import javax.net.SocketFactory;
import java.net.ProxySelector;
import java.net.Proxy;
import okhttp3.internal.cache.InternalCache;
import javax.net.ssl.HostnameVerifier;
import okhttp3.internal.tls.CertificateChainCleaner;
import javax.annotation.Nullable;
import java.util.List;

public class OkHttpClient implements Cloneable, Call.Factory, WebSocket.Factory
{
    static final List<ConnectionSpec> DEFAULT_CONNECTION_SPECS;
    static final List<Protocol> DEFAULT_PROTOCOLS;
    final Authenticator authenticator;
    @Nullable
    final Cache cache;
    @Nullable
    final CertificateChainCleaner certificateChainCleaner;
    final CertificatePinner certificatePinner;
    final int connectTimeout;
    final ConnectionPool connectionPool;
    final List<ConnectionSpec> connectionSpecs;
    final CookieJar cookieJar;
    final Dispatcher dispatcher;
    final Dns dns;
    final EventListener.Factory eventListenerFactory;
    final boolean followRedirects;
    final boolean followSslRedirects;
    final HostnameVerifier hostnameVerifier;
    final List<Interceptor> interceptors;
    @Nullable
    final InternalCache internalCache;
    final List<Interceptor> networkInterceptors;
    final int pingInterval;
    final List<Protocol> protocols;
    @Nullable
    final Proxy proxy;
    final Authenticator proxyAuthenticator;
    final ProxySelector proxySelector;
    final int readTimeout;
    final boolean retryOnConnectionFailure;
    final SocketFactory socketFactory;
    @Nullable
    final SSLSocketFactory sslSocketFactory;
    final int writeTimeout;
    
    static {
        DEFAULT_PROTOCOLS = Util.immutableList(Protocol.HTTP_2, Protocol.HTTP_1_1);
        DEFAULT_CONNECTION_SPECS = Util.immutableList(ConnectionSpec.MODERN_TLS, ConnectionSpec.CLEARTEXT);
        Internal.instance = new Internal() {
            @Override
            public void addLenient(final Headers.Builder builder, final String s) {
                builder.addLenient(s);
            }
            
            @Override
            public void addLenient(final Headers.Builder builder, final String s, final String s2) {
                builder.addLenient(s, s2);
            }
            
            @Override
            public void apply(final ConnectionSpec connectionSpec, final SSLSocket sslSocket, final boolean b) {
                connectionSpec.apply(sslSocket, b);
            }
            
            @Override
            public int code(final Response.Builder builder) {
                return builder.code;
            }
            
            @Override
            public boolean connectionBecameIdle(final ConnectionPool connectionPool, final RealConnection realConnection) {
                return connectionPool.connectionBecameIdle(realConnection);
            }
            
            @Override
            public Socket deduplicate(final ConnectionPool connectionPool, final Address address, final StreamAllocation streamAllocation) {
                return connectionPool.deduplicate(address, streamAllocation);
            }
            
            @Override
            public boolean equalsNonHost(final Address address, final Address address2) {
                return address.equalsNonHost(address2);
            }
            
            @Override
            public RealConnection get(final ConnectionPool connectionPool, final Address address, final StreamAllocation streamAllocation, final Route route) {
                return connectionPool.get(address, streamAllocation, route);
            }
            
            @Override
            public HttpUrl getHttpUrlChecked(final String s) throws MalformedURLException, UnknownHostException {
                return HttpUrl.getChecked(s);
            }
            
            @Override
            public Call newWebSocketCall(final OkHttpClient okHttpClient, final Request request) {
                return RealCall.newRealCall(okHttpClient, request, true);
            }
            
            @Override
            public void put(final ConnectionPool connectionPool, final RealConnection realConnection) {
                connectionPool.put(realConnection);
            }
            
            @Override
            public RouteDatabase routeDatabase(final ConnectionPool connectionPool) {
                return connectionPool.routeDatabase;
            }
            
            @Override
            public void setCache(final Builder builder, final InternalCache internalCache) {
                builder.setInternalCache(internalCache);
            }
            
            @Override
            public StreamAllocation streamAllocation(final Call call) {
                return ((RealCall)call).streamAllocation();
            }
        };
    }
    
    public OkHttpClient() {
        this(new Builder());
    }
    
    OkHttpClient(final Builder builder) {
        this.dispatcher = builder.dispatcher;
        this.proxy = builder.proxy;
        this.protocols = builder.protocols;
        this.connectionSpecs = builder.connectionSpecs;
        this.interceptors = Util.immutableList(builder.interceptors);
        this.networkInterceptors = Util.immutableList(builder.networkInterceptors);
        this.eventListenerFactory = builder.eventListenerFactory;
        this.proxySelector = builder.proxySelector;
        this.cookieJar = builder.cookieJar;
        this.cache = builder.cache;
        this.internalCache = builder.internalCache;
        this.socketFactory = builder.socketFactory;
        int n = 0;
        for (final ConnectionSpec connectionSpec : this.connectionSpecs) {
            if (n != 0 || connectionSpec.isTls()) {
                n = 1;
            }
            else {
                n = 0;
            }
        }
        if (builder.sslSocketFactory != null || n == 0) {
            this.sslSocketFactory = builder.sslSocketFactory;
            this.certificateChainCleaner = builder.certificateChainCleaner;
        }
        else {
            final X509TrustManager systemDefaultTrustManager = this.systemDefaultTrustManager();
            this.sslSocketFactory = this.systemDefaultSslSocketFactory(systemDefaultTrustManager);
            this.certificateChainCleaner = CertificateChainCleaner.get(systemDefaultTrustManager);
        }
        this.hostnameVerifier = builder.hostnameVerifier;
        this.certificatePinner = builder.certificatePinner.withCertificateChainCleaner(this.certificateChainCleaner);
        this.proxyAuthenticator = builder.proxyAuthenticator;
        this.authenticator = builder.authenticator;
        this.connectionPool = builder.connectionPool;
        this.dns = builder.dns;
        this.followSslRedirects = builder.followSslRedirects;
        this.followRedirects = builder.followRedirects;
        this.retryOnConnectionFailure = builder.retryOnConnectionFailure;
        this.connectTimeout = builder.connectTimeout;
        this.readTimeout = builder.readTimeout;
        this.writeTimeout = builder.writeTimeout;
        this.pingInterval = builder.pingInterval;
        if (this.interceptors.contains(null)) {
            throw new IllegalStateException("Null interceptor: " + this.interceptors);
        }
        if (this.networkInterceptors.contains(null)) {
            throw new IllegalStateException("Null network interceptor: " + this.networkInterceptors);
        }
    }
    
    private SSLSocketFactory systemDefaultSslSocketFactory(final X509TrustManager x509TrustManager) {
        try {
            final SSLContext sslContext = Platform.get().getSSLContext();
            sslContext.init(null, new TrustManager[] { x509TrustManager }, null);
            return sslContext.getSocketFactory();
        }
        catch (GeneralSecurityException ex) {
            throw Util.assertionError("No System TLS", ex);
        }
    }
    
    private X509TrustManager systemDefaultTrustManager() {
        TrustManager[] trustManagers;
        try {
            final TrustManagerFactory instance = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            instance.init((KeyStore)null);
            trustManagers = instance.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
        }
        catch (GeneralSecurityException ex) {
            throw Util.assertionError("No System TLS", ex);
        }
        return (X509TrustManager)trustManagers[0];
    }
    
    public Authenticator authenticator() {
        return this.authenticator;
    }
    
    public Cache cache() {
        return this.cache;
    }
    
    public CertificatePinner certificatePinner() {
        return this.certificatePinner;
    }
    
    public int connectTimeoutMillis() {
        return this.connectTimeout;
    }
    
    public ConnectionPool connectionPool() {
        return this.connectionPool;
    }
    
    public List<ConnectionSpec> connectionSpecs() {
        return this.connectionSpecs;
    }
    
    public CookieJar cookieJar() {
        return this.cookieJar;
    }
    
    public Dispatcher dispatcher() {
        return this.dispatcher;
    }
    
    public Dns dns() {
        return this.dns;
    }
    
    public EventListener.Factory eventListenerFactory() {
        return this.eventListenerFactory;
    }
    
    public boolean followRedirects() {
        return this.followRedirects;
    }
    
    public boolean followSslRedirects() {
        return this.followSslRedirects;
    }
    
    public HostnameVerifier hostnameVerifier() {
        return this.hostnameVerifier;
    }
    
    public List<Interceptor> interceptors() {
        return this.interceptors;
    }
    
    InternalCache internalCache() {
        if (this.cache != null) {
            return this.cache.internalCache;
        }
        return this.internalCache;
    }
    
    public List<Interceptor> networkInterceptors() {
        return this.networkInterceptors;
    }
    
    public Builder newBuilder() {
        return new Builder(this);
    }
    
    @Override
    public Call newCall(final Request request) {
        return RealCall.newRealCall(this, request, false);
    }
    
    @Override
    public WebSocket newWebSocket(final Request request, final WebSocketListener webSocketListener) {
        final RealWebSocket realWebSocket = new RealWebSocket(request, webSocketListener, new Random(), this.pingInterval);
        realWebSocket.connect(this);
        return realWebSocket;
    }
    
    public int pingIntervalMillis() {
        return this.pingInterval;
    }
    
    public List<Protocol> protocols() {
        return this.protocols;
    }
    
    public Proxy proxy() {
        return this.proxy;
    }
    
    public Authenticator proxyAuthenticator() {
        return this.proxyAuthenticator;
    }
    
    public ProxySelector proxySelector() {
        return this.proxySelector;
    }
    
    public int readTimeoutMillis() {
        return this.readTimeout;
    }
    
    public boolean retryOnConnectionFailure() {
        return this.retryOnConnectionFailure;
    }
    
    public SocketFactory socketFactory() {
        return this.socketFactory;
    }
    
    public SSLSocketFactory sslSocketFactory() {
        return this.sslSocketFactory;
    }
    
    public int writeTimeoutMillis() {
        return this.writeTimeout;
    }
    
    public static final class Builder
    {
        Authenticator authenticator;
        @Nullable
        Cache cache;
        @Nullable
        CertificateChainCleaner certificateChainCleaner;
        CertificatePinner certificatePinner;
        int connectTimeout;
        ConnectionPool connectionPool;
        List<ConnectionSpec> connectionSpecs;
        CookieJar cookieJar;
        Dispatcher dispatcher;
        Dns dns;
        EventListener.Factory eventListenerFactory;
        boolean followRedirects;
        boolean followSslRedirects;
        HostnameVerifier hostnameVerifier;
        final List<Interceptor> interceptors;
        @Nullable
        InternalCache internalCache;
        final List<Interceptor> networkInterceptors;
        int pingInterval;
        List<Protocol> protocols;
        @Nullable
        Proxy proxy;
        Authenticator proxyAuthenticator;
        ProxySelector proxySelector;
        int readTimeout;
        boolean retryOnConnectionFailure;
        SocketFactory socketFactory;
        @Nullable
        SSLSocketFactory sslSocketFactory;
        int writeTimeout;
        
        public Builder() {
            this.interceptors = new ArrayList<Interceptor>();
            this.networkInterceptors = new ArrayList<Interceptor>();
            this.dispatcher = new Dispatcher();
            this.protocols = OkHttpClient.DEFAULT_PROTOCOLS;
            this.connectionSpecs = OkHttpClient.DEFAULT_CONNECTION_SPECS;
            this.eventListenerFactory = EventListener.factory(EventListener.NONE);
            this.proxySelector = ProxySelector.getDefault();
            this.cookieJar = CookieJar.NO_COOKIES;
            this.socketFactory = SocketFactory.getDefault();
            this.hostnameVerifier = OkHostnameVerifier.INSTANCE;
            this.certificatePinner = CertificatePinner.DEFAULT;
            this.proxyAuthenticator = Authenticator.NONE;
            this.authenticator = Authenticator.NONE;
            this.connectionPool = new ConnectionPool();
            this.dns = Dns.SYSTEM;
            this.followSslRedirects = true;
            this.followRedirects = true;
            this.retryOnConnectionFailure = true;
            this.connectTimeout = 10000;
            this.readTimeout = 10000;
            this.writeTimeout = 10000;
            this.pingInterval = 0;
        }
        
        Builder(final OkHttpClient okHttpClient) {
            this.interceptors = new ArrayList<Interceptor>();
            this.networkInterceptors = new ArrayList<Interceptor>();
            this.dispatcher = okHttpClient.dispatcher;
            this.proxy = okHttpClient.proxy;
            this.protocols = okHttpClient.protocols;
            this.connectionSpecs = okHttpClient.connectionSpecs;
            this.interceptors.addAll(okHttpClient.interceptors);
            this.networkInterceptors.addAll(okHttpClient.networkInterceptors);
            this.eventListenerFactory = okHttpClient.eventListenerFactory;
            this.proxySelector = okHttpClient.proxySelector;
            this.cookieJar = okHttpClient.cookieJar;
            this.internalCache = okHttpClient.internalCache;
            this.cache = okHttpClient.cache;
            this.socketFactory = okHttpClient.socketFactory;
            this.sslSocketFactory = okHttpClient.sslSocketFactory;
            this.certificateChainCleaner = okHttpClient.certificateChainCleaner;
            this.hostnameVerifier = okHttpClient.hostnameVerifier;
            this.certificatePinner = okHttpClient.certificatePinner;
            this.proxyAuthenticator = okHttpClient.proxyAuthenticator;
            this.authenticator = okHttpClient.authenticator;
            this.connectionPool = okHttpClient.connectionPool;
            this.dns = okHttpClient.dns;
            this.followSslRedirects = okHttpClient.followSslRedirects;
            this.followRedirects = okHttpClient.followRedirects;
            this.retryOnConnectionFailure = okHttpClient.retryOnConnectionFailure;
            this.connectTimeout = okHttpClient.connectTimeout;
            this.readTimeout = okHttpClient.readTimeout;
            this.writeTimeout = okHttpClient.writeTimeout;
            this.pingInterval = okHttpClient.pingInterval;
        }
        
        public Builder addInterceptor(final Interceptor interceptor) {
            if (interceptor == null) {
                throw new IllegalArgumentException("interceptor == null");
            }
            this.interceptors.add(interceptor);
            return this;
        }
        
        public Builder addNetworkInterceptor(final Interceptor interceptor) {
            if (interceptor == null) {
                throw new IllegalArgumentException("interceptor == null");
            }
            this.networkInterceptors.add(interceptor);
            return this;
        }
        
        public Builder authenticator(final Authenticator authenticator) {
            if (authenticator == null) {
                throw new NullPointerException("authenticator == null");
            }
            this.authenticator = authenticator;
            return this;
        }
        
        public OkHttpClient build() {
            return new OkHttpClient(this);
        }
        
        public Builder cache(@Nullable final Cache cache) {
            this.cache = cache;
            this.internalCache = null;
            return this;
        }
        
        public Builder certificatePinner(final CertificatePinner certificatePinner) {
            if (certificatePinner == null) {
                throw new NullPointerException("certificatePinner == null");
            }
            this.certificatePinner = certificatePinner;
            return this;
        }
        
        public Builder connectTimeout(final long n, final TimeUnit timeUnit) {
            this.connectTimeout = Util.checkDuration("timeout", n, timeUnit);
            return this;
        }
        
        public Builder connectionPool(final ConnectionPool connectionPool) {
            if (connectionPool == null) {
                throw new NullPointerException("connectionPool == null");
            }
            this.connectionPool = connectionPool;
            return this;
        }
        
        public Builder connectionSpecs(final List<ConnectionSpec> list) {
            this.connectionSpecs = Util.immutableList(list);
            return this;
        }
        
        public Builder cookieJar(final CookieJar cookieJar) {
            if (cookieJar == null) {
                throw new NullPointerException("cookieJar == null");
            }
            this.cookieJar = cookieJar;
            return this;
        }
        
        public Builder dispatcher(final Dispatcher dispatcher) {
            if (dispatcher == null) {
                throw new IllegalArgumentException("dispatcher == null");
            }
            this.dispatcher = dispatcher;
            return this;
        }
        
        public Builder dns(final Dns dns) {
            if (dns == null) {
                throw new NullPointerException("dns == null");
            }
            this.dns = dns;
            return this;
        }
        
        public Builder eventListener(final EventListener eventListener) {
            if (eventListener == null) {
                throw new NullPointerException("eventListener == null");
            }
            this.eventListenerFactory = EventListener.factory(eventListener);
            return this;
        }
        
        public Builder eventListenerFactory(final EventListener.Factory eventListenerFactory) {
            if (eventListenerFactory == null) {
                throw new NullPointerException("eventListenerFactory == null");
            }
            this.eventListenerFactory = eventListenerFactory;
            return this;
        }
        
        public Builder followRedirects(final boolean followRedirects) {
            this.followRedirects = followRedirects;
            return this;
        }
        
        public Builder followSslRedirects(final boolean followSslRedirects) {
            this.followSslRedirects = followSslRedirects;
            return this;
        }
        
        public Builder hostnameVerifier(final HostnameVerifier hostnameVerifier) {
            if (hostnameVerifier == null) {
                throw new NullPointerException("hostnameVerifier == null");
            }
            this.hostnameVerifier = hostnameVerifier;
            return this;
        }
        
        public List<Interceptor> interceptors() {
            return this.interceptors;
        }
        
        public List<Interceptor> networkInterceptors() {
            return this.networkInterceptors;
        }
        
        public Builder pingInterval(final long n, final TimeUnit timeUnit) {
            this.pingInterval = Util.checkDuration("interval", n, timeUnit);
            return this;
        }
        
        public Builder protocols(final List<Protocol> list) {
            final ArrayList list2 = new ArrayList<Protocol>(list);
            if (!list2.contains(Protocol.HTTP_1_1)) {
                throw new IllegalArgumentException("protocols doesn't contain http/1.1: " + list2);
            }
            if (list2.contains(Protocol.HTTP_1_0)) {
                throw new IllegalArgumentException("protocols must not contain http/1.0: " + list2);
            }
            if (list2.contains(null)) {
                throw new IllegalArgumentException("protocols must not contain null");
            }
            list2.remove(Protocol.SPDY_3);
            this.protocols = Collections.unmodifiableList((List<? extends Protocol>)list2);
            return this;
        }
        
        public Builder proxy(@Nullable final Proxy proxy) {
            this.proxy = proxy;
            return this;
        }
        
        public Builder proxyAuthenticator(final Authenticator proxyAuthenticator) {
            if (proxyAuthenticator == null) {
                throw new NullPointerException("proxyAuthenticator == null");
            }
            this.proxyAuthenticator = proxyAuthenticator;
            return this;
        }
        
        public Builder proxySelector(final ProxySelector proxySelector) {
            this.proxySelector = proxySelector;
            return this;
        }
        
        public Builder readTimeout(final long n, final TimeUnit timeUnit) {
            this.readTimeout = Util.checkDuration("timeout", n, timeUnit);
            return this;
        }
        
        public Builder retryOnConnectionFailure(final boolean retryOnConnectionFailure) {
            this.retryOnConnectionFailure = retryOnConnectionFailure;
            return this;
        }
        
        void setInternalCache(@Nullable final InternalCache internalCache) {
            this.internalCache = internalCache;
            this.cache = null;
        }
        
        public Builder socketFactory(final SocketFactory socketFactory) {
            if (socketFactory == null) {
                throw new NullPointerException("socketFactory == null");
            }
            this.socketFactory = socketFactory;
            return this;
        }
        
        public Builder sslSocketFactory(final SSLSocketFactory sslSocketFactory) {
            if (sslSocketFactory == null) {
                throw new NullPointerException("sslSocketFactory == null");
            }
            this.sslSocketFactory = sslSocketFactory;
            this.certificateChainCleaner = Platform.get().buildCertificateChainCleaner(sslSocketFactory);
            return this;
        }
        
        public Builder sslSocketFactory(final SSLSocketFactory sslSocketFactory, final X509TrustManager x509TrustManager) {
            if (sslSocketFactory == null) {
                throw new NullPointerException("sslSocketFactory == null");
            }
            if (x509TrustManager == null) {
                throw new NullPointerException("trustManager == null");
            }
            this.sslSocketFactory = sslSocketFactory;
            this.certificateChainCleaner = CertificateChainCleaner.get(x509TrustManager);
            return this;
        }
        
        public Builder writeTimeout(final long n, final TimeUnit timeUnit) {
            this.writeTimeout = Util.checkDuration("timeout", n, timeUnit);
            return this;
        }
    }
}
