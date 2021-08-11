// 
// Decompiled by Procyon v1.0-SNAPSHOT
// 

package okhttp3.internal.connection;

import javax.net.ssl.SSLSocketFactory;
import okhttp3.internal.http2.ErrorCode;
import okhttp3.internal.http2.Http2Stream;
import okhttp3.internal.ws.RealWebSocket;
import java.net.SocketException;
import okhttp3.internal.http2.Http2Codec;
import okhttp3.internal.http.HttpCodec;
import okhttp3.Interceptor;
import java.net.SocketTimeoutException;
import java.net.Proxy;
import okhttp3.internal.Internal;
import javax.annotation.Nullable;
import java.net.ProtocolException;
import java.net.UnknownServiceException;
import okhttp3.internal.Version;
import okio.Source;
import okhttp3.Response;
import okhttp3.internal.http.HttpHeaders;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.internal.http1.Http1Codec;
import okhttp3.HttpUrl;
import okhttp3.Request;
import javax.net.ssl.SSLSession;
import okhttp3.ConnectionSpec;
import okhttp3.Address;
import okio.Okio;
import javax.net.ssl.SSLPeerUnverifiedException;
import okhttp3.internal.tls.OkHostnameVerifier;
import java.security.cert.Certificate;
import okhttp3.CertificatePinner;
import java.security.cert.X509Certificate;
import okhttp3.internal.Util;
import okhttp3.internal.platform.Platform;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import okhttp3.EventListener;
import okhttp3.Call;
import java.util.ArrayList;
import okio.BufferedSource;
import okio.BufferedSink;
import okhttp3.Route;
import java.net.Socket;
import okhttp3.Protocol;
import okhttp3.Handshake;
import okhttp3.ConnectionPool;
import java.lang.ref.Reference;
import java.util.List;
import okhttp3.Connection;
import okhttp3.internal.http2.Http2Connection;

public final class RealConnection extends Listener implements Connection
{
    private static final int MAX_TUNNEL_ATTEMPTS = 21;
    private static final String NPE_THROW_WITH_NULL = "throw with null exception";
    public int allocationLimit;
    public final List<Reference<StreamAllocation>> allocations;
    private final ConnectionPool connectionPool;
    private Handshake handshake;
    private Http2Connection http2Connection;
    public long idleAtNanos;
    public boolean noNewStreams;
    private Protocol protocol;
    private Socket rawSocket;
    private final Route route;
    private BufferedSink sink;
    private Socket socket;
    private BufferedSource source;
    public int successCount;
    
    public RealConnection(final ConnectionPool connectionPool, final Route route) {
        this.allocationLimit = 1;
        this.allocations = new ArrayList<Reference<StreamAllocation>>();
        this.idleAtNanos = Long.MAX_VALUE;
        this.connectionPool = connectionPool;
        this.route = route;
    }
    
    private void connectSocket(final int p0, final int p1, final Call p2, final EventListener p3) throws IOException {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        okhttp3/internal/connection/RealConnection.route:Lokhttp3/Route;
        //     4: invokevirtual   okhttp3/Route.proxy:()Ljava/net/Proxy;
        //     7: astore          6
        //     9: aload_0        
        //    10: getfield        okhttp3/internal/connection/RealConnection.route:Lokhttp3/Route;
        //    13: invokevirtual   okhttp3/Route.address:()Lokhttp3/Address;
        //    16: astore          5
        //    18: aload           6
        //    20: invokevirtual   java/net/Proxy.type:()Ljava/net/Proxy$Type;
        //    23: getstatic       java/net/Proxy$Type.DIRECT:Ljava/net/Proxy$Type;
        //    26: if_acmpeq       40
        //    29: aload           6
        //    31: invokevirtual   java/net/Proxy.type:()Ljava/net/Proxy$Type;
        //    34: getstatic       java/net/Proxy$Type.HTTP:Ljava/net/Proxy$Type;
        //    37: if_acmpne       126
        //    40: aload           5
        //    42: invokevirtual   okhttp3/Address.socketFactory:()Ljavax/net/SocketFactory;
        //    45: invokevirtual   javax/net/SocketFactory.createSocket:()Ljava/net/Socket;
        //    48: astore          5
        //    50: aload_0        
        //    51: aload           5
        //    53: putfield        okhttp3/internal/connection/RealConnection.rawSocket:Ljava/net/Socket;
        //    56: aload           4
        //    58: aload_3        
        //    59: aload_0        
        //    60: getfield        okhttp3/internal/connection/RealConnection.route:Lokhttp3/Route;
        //    63: invokevirtual   okhttp3/Route.socketAddress:()Ljava/net/InetSocketAddress;
        //    66: aload           6
        //    68: invokevirtual   okhttp3/EventListener.connectStart:(Lokhttp3/Call;Ljava/net/InetSocketAddress;Ljava/net/Proxy;)V
        //    71: aload_0        
        //    72: getfield        okhttp3/internal/connection/RealConnection.rawSocket:Ljava/net/Socket;
        //    75: iload_2        
        //    76: invokevirtual   java/net/Socket.setSoTimeout:(I)V
        //    79: invokestatic    okhttp3/internal/platform/Platform.get:()Lokhttp3/internal/platform/Platform;
        //    82: aload_0        
        //    83: getfield        okhttp3/internal/connection/RealConnection.rawSocket:Ljava/net/Socket;
        //    86: aload_0        
        //    87: getfield        okhttp3/internal/connection/RealConnection.route:Lokhttp3/Route;
        //    90: invokevirtual   okhttp3/Route.socketAddress:()Ljava/net/InetSocketAddress;
        //    93: iload_1        
        //    94: invokevirtual   okhttp3/internal/platform/Platform.connectSocket:(Ljava/net/Socket;Ljava/net/InetSocketAddress;I)V
        //    97: aload_0        
        //    98: aload_0        
        //    99: getfield        okhttp3/internal/connection/RealConnection.rawSocket:Ljava/net/Socket;
        //   102: invokestatic    okio/Okio.source:(Ljava/net/Socket;)Lokio/Source;
        //   105: invokestatic    okio/Okio.buffer:(Lokio/Source;)Lokio/BufferedSource;
        //   108: putfield        okhttp3/internal/connection/RealConnection.source:Lokio/BufferedSource;
        //   111: aload_0        
        //   112: aload_0        
        //   113: getfield        okhttp3/internal/connection/RealConnection.rawSocket:Ljava/net/Socket;
        //   116: invokestatic    okio/Okio.sink:(Ljava/net/Socket;)Lokio/Sink;
        //   119: invokestatic    okio/Okio.buffer:(Lokio/Sink;)Lokio/BufferedSink;
        //   122: putfield        okhttp3/internal/connection/RealConnection.sink:Lokio/BufferedSink;
        //   125: return         
        //   126: new             Ljava/net/Socket;
        //   129: dup            
        //   130: aload           6
        //   132: invokespecial   java/net/Socket.<init>:(Ljava/net/Proxy;)V
        //   135: astore          5
        //   137: goto            50
        //   140: astore_3       
        //   141: new             Ljava/net/ConnectException;
        //   144: dup            
        //   145: new             Ljava/lang/StringBuilder;
        //   148: dup            
        //   149: invokespecial   java/lang/StringBuilder.<init>:()V
        //   152: ldc             "Failed to connect to "
        //   154: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   157: aload_0        
        //   158: getfield        okhttp3/internal/connection/RealConnection.route:Lokhttp3/Route;
        //   161: invokevirtual   okhttp3/Route.socketAddress:()Ljava/net/InetSocketAddress;
        //   164: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   167: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   170: invokespecial   java/net/ConnectException.<init>:(Ljava/lang/String;)V
        //   173: astore          4
        //   175: aload           4
        //   177: aload_3        
        //   178: invokevirtual   java/net/ConnectException.initCause:(Ljava/lang/Throwable;)Ljava/lang/Throwable;
        //   181: pop            
        //   182: aload           4
        //   184: athrow         
        //   185: astore_3       
        //   186: ldc             "throw with null exception"
        //   188: aload_3        
        //   189: invokevirtual   java/lang/NullPointerException.getMessage:()Ljava/lang/String;
        //   192: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   195: ifeq            125
        //   198: new             Ljava/io/IOException;
        //   201: dup            
        //   202: aload_3        
        //   203: invokespecial   java/io/IOException.<init>:(Ljava/lang/Throwable;)V
        //   206: athrow         
        //    Exceptions:
        //  throws java.io.IOException
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  79     97     140    185    Ljava/net/ConnectException;
        //  97     125    185    207    Ljava/lang/NullPointerException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0125:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at com.strobel.decompiler.DecompilerDriver.decompileType(DecompilerDriver.java:330)
        //     at com.strobel.decompiler.DecompilerDriver.decompileJar(DecompilerDriver.java:251)
        //     at com.strobel.decompiler.DecompilerDriver.main(DecompilerDriver.java:141)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private void connectTls(final ConnectionSpecSelector connectionSpecSelector) throws IOException {
        final Address address = this.route.address();
        Object sslSocketFactory = address.sslSocketFactory();
        SSLSocket sslSocket = null;
        SSLSocket sslSocket2 = null;
        SSLSession session;
        try {
            sslSocketFactory = (sslSocket = (sslSocket2 = (SSLSocket)((SSLSocketFactory)sslSocketFactory).createSocket(this.rawSocket, address.url().host(), address.url().port(), true)));
            final ConnectionSpec configureSecureSocket = connectionSpecSelector.configureSecureSocket((SSLSocket)sslSocketFactory);
            sslSocket2 = (SSLSocket)sslSocketFactory;
            sslSocket = (SSLSocket)sslSocketFactory;
            if (configureSecureSocket.supportsTlsExtensions()) {
                sslSocket2 = (SSLSocket)sslSocketFactory;
                sslSocket = (SSLSocket)sslSocketFactory;
                Platform.get().configureTlsExtensions((SSLSocket)sslSocketFactory, address.url().host(), address.protocols());
            }
            sslSocket2 = (SSLSocket)sslSocketFactory;
            sslSocket = (SSLSocket)sslSocketFactory;
            ((SSLSocket)sslSocketFactory).startHandshake();
            sslSocket2 = (SSLSocket)sslSocketFactory;
            sslSocket = (SSLSocket)sslSocketFactory;
            session = ((SSLSocket)sslSocketFactory).getSession();
            sslSocket2 = (SSLSocket)sslSocketFactory;
            sslSocket = (SSLSocket)sslSocketFactory;
            if (!this.isValid(session)) {
                sslSocket2 = (SSLSocket)sslSocketFactory;
                sslSocket = (SSLSocket)sslSocketFactory;
                throw new IOException("a valid ssl session was not established");
            }
        }
        catch (AssertionError assertionError) {
            sslSocket = sslSocket2;
            if (Util.isAndroidGetsocknameError(assertionError)) {
                sslSocket = sslSocket2;
                throw new IOException(assertionError);
            }
            return;
        }
        finally {
            if (sslSocket != null) {
                Platform.get().afterHandshake(sslSocket);
            }
            if (!false) {
                Util.closeQuietly(sslSocket);
            }
        }
        final Handshake value = Handshake.get(session);
        if (!address.hostnameVerifier().verify(address.url().host(), session)) {
            final X509Certificate x509Certificate = value.peerCertificates().get(0);
            throw new SSLPeerUnverifiedException("Hostname " + address.url().host() + " not verified:\n    certificate: " + CertificatePinner.pin(x509Certificate) + "\n    DN: " + x509Certificate.getSubjectDN().getName() + "\n    subjectAltNames: " + OkHostnameVerifier.allSubjectAltNames(x509Certificate));
        }
        address.certificatePinner().check(address.url().host(), value.peerCertificates());
        final ConnectionSpec connectionSpec;
        String selectedProtocol;
        if (connectionSpec.supportsTlsExtensions()) {
            selectedProtocol = Platform.get().getSelectedProtocol((SSLSocket)sslSocketFactory);
        }
        else {
            selectedProtocol = null;
        }
        this.socket = (Socket)sslSocketFactory;
        this.source = Okio.buffer(Okio.source(this.socket));
        this.sink = Okio.buffer(Okio.sink(this.socket));
        this.handshake = value;
        Protocol protocol;
        if (selectedProtocol != null) {
            protocol = Protocol.get(selectedProtocol);
        }
        else {
            protocol = Protocol.HTTP_1_1;
        }
        this.protocol = protocol;
        if (sslSocketFactory != null) {
            Platform.get().afterHandshake((SSLSocket)sslSocketFactory);
        }
        if (!true) {
            Util.closeQuietly((Socket)sslSocketFactory);
        }
    }
    
    private void connectTunnel(final int n, final int n2, final int n3, final Call call, final EventListener eventListener) throws IOException {
        Request request = this.createTunnelRequest();
        final HttpUrl url = request.url();
        for (int i = 0; i < 21; ++i) {
            this.connectSocket(n, n2, call, eventListener);
            request = this.createTunnel(n2, n3, request, url);
            if (request == null) {
                break;
            }
            Util.closeQuietly(this.rawSocket);
            this.rawSocket = null;
            this.sink = null;
            this.source = null;
            eventListener.connectEnd(call, this.route.socketAddress(), this.route.proxy(), null);
        }
    }
    
    private Request createTunnel(final int n, final int n2, Request request, final HttpUrl httpUrl) throws IOException {
        final Request request2 = null;
        final String string = "CONNECT " + Util.hostHeader(httpUrl, true) + " HTTP/1.1";
        Response build;
        Request authenticate = null;
    Label_0336:
        do {
            final Http1Codec http1Codec = new Http1Codec(null, null, this.source, this.sink);
            this.source.timeout().timeout(n, TimeUnit.MILLISECONDS);
            this.sink.timeout().timeout(n2, TimeUnit.MILLISECONDS);
            http1Codec.writeRequest(request.headers(), string);
            http1Codec.finishRequest();
            build = http1Codec.readResponseHeaders(false).request(request).build();
            long contentLength;
            if ((contentLength = HttpHeaders.contentLength(build)) == -1L) {
                contentLength = 0L;
            }
            final Source fixedLengthSource = http1Codec.newFixedLengthSource(contentLength);
            Util.skipAll(fixedLengthSource, Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
            fixedLengthSource.close();
            switch (build.code()) {
                default: {
                    throw new IOException("Unexpected response code for CONNECT: " + build.code());
                }
                case 200: {
                    if (this.source.buffer().exhausted()) {
                        authenticate = request2;
                        if (this.sink.buffer().exhausted()) {
                            break Label_0336;
                        }
                    }
                    throw new IOException("TLS tunnel buffered too many bytes!");
                }
                case 407: {
                    authenticate = this.route.address().proxyAuthenticator().authenticate(this.route, build);
                    if (authenticate == null) {
                        throw new IOException("Failed to authenticate with proxy");
                    }
                    request = authenticate;
                    continue;
                }
            }
        } while (!"close".equalsIgnoreCase(build.header("Connection")));
        return authenticate;
    }
    
    private Request createTunnelRequest() {
        return new Request.Builder().url(this.route.address().url()).header("Host", Util.hostHeader(this.route.address().url(), true)).header("Proxy-Connection", "Keep-Alive").header("User-Agent", Version.userAgent()).build();
    }
    
    private void establishProtocol(final ConnectionSpecSelector connectionSpecSelector, final int n, final Call call, final EventListener eventListener) throws IOException {
        if (this.route.address().sslSocketFactory() == null) {
            this.protocol = Protocol.HTTP_1_1;
            this.socket = this.rawSocket;
        }
        else {
            eventListener.secureConnectStart(call);
            this.connectTls(connectionSpecSelector);
            eventListener.secureConnectEnd(call, this.handshake);
            if (this.protocol == Protocol.HTTP_2) {
                this.socket.setSoTimeout(0);
                (this.http2Connection = new Http2Connection.Builder(true).socket(this.socket, this.route.address().url().host(), this.source, this.sink).listener(this).pingIntervalMillis(n).build()).start();
            }
        }
    }
    
    private boolean isValid(final SSLSession sslSession) {
        return !"NONE".equals(sslSession.getProtocol()) && !"SSL_NULL_WITH_NULL_NULL".equals(sslSession.getCipherSuite());
    }
    
    public static RealConnection testConnection(final ConnectionPool connectionPool, final Route route, final Socket socket, final long idleAtNanos) {
        final RealConnection realConnection = new RealConnection(connectionPool, route);
        realConnection.socket = socket;
        realConnection.idleAtNanos = idleAtNanos;
        return realConnection;
    }
    
    public void cancel() {
        Util.closeQuietly(this.rawSocket);
    }
    
    public void connect(final int n, final int n2, final int n3, final int n4, final boolean b, final Call call, final EventListener eventListener) {
        if (this.protocol != null) {
            throw new IllegalStateException("already connected");
        }
        final RouteException ex = null;
        final List<ConnectionSpec> connectionSpecs = this.route.address().connectionSpecs();
        final ConnectionSpecSelector connectionSpecSelector = new ConnectionSpecSelector(connectionSpecs);
        RouteException ex2 = ex;
        if (this.route.address().sslSocketFactory() == null) {
            if (!connectionSpecs.contains(ConnectionSpec.CLEARTEXT)) {
                throw new RouteException(new UnknownServiceException("CLEARTEXT communication not enabled for client"));
            }
            final String host = this.route.address().url().host();
            ex2 = ex;
            if (!Platform.get().isCleartextTrafficPermitted(host)) {
                throw new RouteException(new UnknownServiceException("CLEARTEXT communication to " + host + " not permitted by network security policy"));
            }
        }
    Label_0408:
        while (true) {
            try {
                while (true) {
                    Label_0241: {
                        if (!this.route.requiresTunnel()) {
                            this.connectSocket(n, n2, call, eventListener);
                            break Label_0241;
                        }
                        this.connectTunnel(n, n2, n3, call, eventListener);
                        if (this.rawSocket != null) {
                            break Label_0241;
                        }
                        if (this.route.requiresTunnel() && this.rawSocket == null) {
                            throw new RouteException(new ProtocolException("Too many tunnel connections attempted: 21"));
                        }
                        break Label_0408;
                    }
                    this.establishProtocol(connectionSpecSelector, n4, call, eventListener);
                    eventListener.connectEnd(call, this.route.socketAddress(), this.route.proxy(), this.protocol);
                    continue;
                }
            }
            catch (IOException ex3) {
                Util.closeQuietly(this.socket);
                Util.closeQuietly(this.rawSocket);
                this.socket = null;
                this.rawSocket = null;
                this.source = null;
                this.sink = null;
                this.handshake = null;
                this.protocol = null;
                this.http2Connection = null;
                eventListener.connectFailed(call, this.route.socketAddress(), this.route.proxy(), null, ex3);
                RouteException ex4;
                if (ex2 == null) {
                    ex4 = new RouteException(ex3);
                }
                else {
                    ex2.addConnectException(ex3);
                    ex4 = ex2;
                }
                if (b) {
                    ex2 = ex4;
                    if (connectionSpecSelector.connectionFailed(ex3)) {
                        continue;
                    }
                }
                throw ex4;
            }
            break;
        }
        if (this.http2Connection != null) {
            synchronized (this.connectionPool) {
                this.allocationLimit = this.http2Connection.maxConcurrentStreams();
            }
        }
    }
    
    @Override
    public Handshake handshake() {
        return this.handshake;
    }
    
    public boolean isEligible(final Address address, @Nullable final Route route) {
        if (this.allocations.size() < this.allocationLimit && !this.noNewStreams && Internal.instance.equalsNonHost(this.route.address(), address)) {
            if (address.url().host().equals(this.route().address().url().host())) {
                return true;
            }
            if (this.http2Connection != null && route != null && route.proxy().type() == Proxy.Type.DIRECT && this.route.proxy().type() == Proxy.Type.DIRECT && this.route.socketAddress().equals(route.socketAddress()) && route.address().hostnameVerifier() == OkHostnameVerifier.INSTANCE && this.supportsUrl(address.url())) {
                try {
                    address.certificatePinner().check(address.url().host(), this.handshake().peerCertificates());
                    return true;
                }
                catch (SSLPeerUnverifiedException ex) {
                    return false;
                }
            }
        }
        return false;
    }
    
    public boolean isHealthy(final boolean b) {
        boolean b2 = true;
        if (this.socket.isClosed() || this.socket.isInputShutdown() || this.socket.isOutputShutdown()) {
            b2 = false;
        }
        else if (this.http2Connection != null) {
            if (this.http2Connection.isShutdown()) {
                return false;
            }
        }
        else if (b) {
            try {
                final int soTimeout = this.socket.getSoTimeout();
                try {
                    this.socket.setSoTimeout(1);
                    return !this.source.exhausted();
                }
                finally {
                    this.socket.setSoTimeout(soTimeout);
                }
            }
            catch (IOException ex) {
                return false;
            }
            catch (SocketTimeoutException ex2) {
                return true;
            }
        }
        return b2;
    }
    
    public boolean isMultiplexed() {
        return this.http2Connection != null;
    }
    
    public HttpCodec newCodec(final OkHttpClient okHttpClient, final Interceptor.Chain chain, final StreamAllocation streamAllocation) throws SocketException {
        if (this.http2Connection != null) {
            return new Http2Codec(okHttpClient, chain, streamAllocation, this.http2Connection);
        }
        this.socket.setSoTimeout(chain.readTimeoutMillis());
        this.source.timeout().timeout(chain.readTimeoutMillis(), TimeUnit.MILLISECONDS);
        this.sink.timeout().timeout(chain.writeTimeoutMillis(), TimeUnit.MILLISECONDS);
        return new Http1Codec(okHttpClient, streamAllocation, this.source, this.sink);
    }
    
    public RealWebSocket.Streams newWebSocketStreams(final StreamAllocation streamAllocation) {
        return new RealWebSocket.Streams(true, this.source, this.sink) {
            @Override
            public void close() throws IOException {
                streamAllocation.streamFinished(true, streamAllocation.codec(), -1L, null);
            }
        };
    }
    
    @Override
    public void onSettings(final Http2Connection http2Connection) {
        synchronized (this.connectionPool) {
            this.allocationLimit = http2Connection.maxConcurrentStreams();
        }
    }
    
    @Override
    public void onStream(final Http2Stream http2Stream) throws IOException {
        http2Stream.close(ErrorCode.REFUSED_STREAM);
    }
    
    @Override
    public Protocol protocol() {
        return this.protocol;
    }
    
    @Override
    public Route route() {
        return this.route;
    }
    
    @Override
    public Socket socket() {
        return this.socket;
    }
    
    public boolean supportsUrl(final HttpUrl httpUrl) {
        return httpUrl.port() == this.route.address().url().port() && (httpUrl.host().equals(this.route.address().url().host()) || (this.handshake != null && OkHostnameVerifier.INSTANCE.verify(httpUrl.host(), this.handshake.peerCertificates().get(0))));
    }
    
    @Override
    public String toString() {
        final StringBuilder append = new StringBuilder().append("Connection{").append(this.route.address().url().host()).append(":").append(this.route.address().url().port()).append(", proxy=").append(this.route.proxy()).append(" hostAddress=").append(this.route.socketAddress()).append(" cipherSuite=");
        Object cipherSuite;
        if (this.handshake != null) {
            cipherSuite = this.handshake.cipherSuite();
        }
        else {
            cipherSuite = "none";
        }
        return append.append(cipherSuite).append(" protocol=").append(this.protocol).append('}').toString();
    }
}
