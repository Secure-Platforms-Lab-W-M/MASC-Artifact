package okhttp3.internal.connection;

import java.io.IOException;
import java.net.ConnectException;
import java.net.ProtocolException;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.Proxy.Type;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.Nullable;
import javax.net.ssl.SSLPeerUnverifiedException;
import okhttp3.Address;
import okhttp3.Connection;
import okhttp3.ConnectionPool;
import okhttp3.Handshake;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.internal.Internal;
import okhttp3.internal.Util;
import okhttp3.internal.Version;
import okhttp3.internal.http.HttpCodec;
import okhttp3.internal.http.HttpHeaders;
import okhttp3.internal.http1.Http1Codec;
import okhttp3.internal.http2.ErrorCode;
import okhttp3.internal.http2.Http2Codec;
import okhttp3.internal.http2.Http2Connection;
import okhttp3.internal.http2.Http2Stream;
import okhttp3.internal.platform.Platform;
import okhttp3.internal.tls.OkHostnameVerifier;
import okhttp3.internal.ws.RealWebSocket;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import okio.Source;

public final class RealConnection extends Http2Connection.Listener implements Connection {
   private static final String NPE_THROW_WITH_NULL = "throw with null exception";
   public int allocationLimit = 1;
   public final List allocations = new ArrayList();
   private final ConnectionPool connectionPool;
   private Handshake handshake;
   private Http2Connection http2Connection;
   public long idleAtNanos = Long.MAX_VALUE;
   public boolean noNewStreams;
   private Protocol protocol;
   private Socket rawSocket;
   private final Route route;
   private BufferedSink sink;
   private Socket socket;
   private BufferedSource source;
   public int successCount;

   public RealConnection(ConnectionPool var1, Route var2) {
      this.connectionPool = var1;
      this.route = var2;
   }

   private void connectSocket(int var1, int var2) throws IOException {
      Proxy var3 = this.route.proxy();
      Address var4 = this.route.address();
      Socket var7;
      if (var3.type() != Type.DIRECT && var3.type() != Type.HTTP) {
         var7 = new Socket(var3);
      } else {
         var7 = var4.socketFactory().createSocket();
      }

      this.rawSocket = var7;
      var7.setSoTimeout(var2);

      try {
         Platform.get().connectSocket(this.rawSocket, this.route.socketAddress(), var1);
      } catch (ConnectException var5) {
         StringBuilder var8 = new StringBuilder();
         var8.append("Failed to connect to ");
         var8.append(this.route.socketAddress());
         ConnectException var9 = new ConnectException(var8.toString());
         var9.initCause(var5);
         throw var9;
      }

      try {
         this.source = Okio.buffer(Okio.source(this.rawSocket));
         this.sink = Okio.buffer(Okio.sink(this.rawSocket));
      } catch (NullPointerException var6) {
         if ("throw with null exception".equals(var6.getMessage())) {
            throw new IOException(var6);
         }
      }
   }

   private void connectTls(ConnectionSpecSelector param1) throws IOException {
      // $FF: Couldn't be decompiled
   }

   private void connectTunnel(int var1, int var2, int var3) throws IOException {
      Request var5 = this.createTunnelRequest();
      HttpUrl var6 = var5.url();
      int var4 = 0;

      while(true) {
         ++var4;
         if (var4 > 21) {
            StringBuilder var7 = new StringBuilder();
            var7.append("Too many tunnel connections attempted: ");
            var7.append(21);
            throw new ProtocolException(var7.toString());
         }

         this.connectSocket(var1, var2);
         var5 = this.createTunnel(var2, var3, var5, var6);
         if (var5 == null) {
            return;
         }

         Util.closeQuietly(this.rawSocket);
         this.rawSocket = null;
         this.sink = null;
         this.source = null;
      }
   }

   private Request createTunnel(int var1, int var2, Request var3, HttpUrl var4) throws IOException {
      StringBuilder var10 = new StringBuilder();
      var10.append("CONNECT ");
      var10.append(Util.hostHeader(var4, true));
      var10.append(" HTTP/1.1");
      String var15 = var10.toString();

      Response var12;
      do {
         Http1Codec var11 = new Http1Codec((OkHttpClient)null, (StreamAllocation)null, this.source, this.sink);
         this.source.timeout().timeout((long)var1, TimeUnit.MILLISECONDS);
         this.sink.timeout().timeout((long)var2, TimeUnit.MILLISECONDS);
         var11.writeRequest(var3.headers(), var15);
         var11.finishRequest();
         var12 = var11.readResponseHeaders(false).request(var3).build();
         long var8 = HttpHeaders.contentLength(var12);
         long var6 = var8;
         if (var8 == -1L) {
            var6 = 0L;
         }

         Source var13 = var11.newFixedLengthSource(var6);
         Util.skipAll(var13, Integer.MAX_VALUE, TimeUnit.MILLISECONDS);
         var13.close();
         int var5 = var12.code();
         if (var5 == 200) {
            if (this.source.buffer().exhausted() && this.sink.buffer().exhausted()) {
               return null;
            }

            throw new IOException("TLS tunnel buffered too many bytes!");
         }

         if (var5 != 407) {
            StringBuilder var14 = new StringBuilder();
            var14.append("Unexpected response code for CONNECT: ");
            var14.append(var12.code());
            throw new IOException(var14.toString());
         }

         var3 = this.route.address().proxyAuthenticator().authenticate(this.route, var12);
         if (var3 == null) {
            throw new IOException("Failed to authenticate with proxy");
         }
      } while(!"close".equalsIgnoreCase(var12.header("Connection")));

      return var3;
   }

   private Request createTunnelRequest() {
      return (new Request.Builder()).url(this.route.address().url()).header("Host", Util.hostHeader(this.route.address().url(), true)).header("Proxy-Connection", "Keep-Alive").header("User-Agent", Version.userAgent()).build();
   }

   private void establishProtocol(ConnectionSpecSelector var1) throws IOException {
      if (this.route.address().sslSocketFactory() == null) {
         this.protocol = Protocol.HTTP_1_1;
         this.socket = this.rawSocket;
      } else {
         this.connectTls(var1);
         if (this.protocol == Protocol.HTTP_2) {
            this.socket.setSoTimeout(0);
            Http2Connection var2 = (new Http2Connection.Builder(true)).socket(this.socket, this.route.address().url().host(), this.source, this.sink).listener(this).build();
            this.http2Connection = var2;
            var2.start();
         }

      }
   }

   public static RealConnection testConnection(ConnectionPool var0, Route var1, Socket var2, long var3) {
      RealConnection var5 = new RealConnection(var0, var1);
      var5.socket = var2;
      var5.idleAtNanos = var3;
      return var5;
   }

   public void cancel() {
      Util.closeQuietly(this.rawSocket);
   }

   public void connect(int param1, int param2, int param3, boolean param4) {
      // $FF: Couldn't be decompiled
   }

   public Handshake handshake() {
      return this.handshake;
   }

   public boolean isEligible(Address var1, @Nullable Route var2) {
      if (this.allocations.size() < this.allocationLimit) {
         if (this.noNewStreams) {
            return false;
         } else if (!Internal.instance.equalsNonHost(this.route.address(), var1)) {
            return false;
         } else if (var1.url().host().equals(this.route().address().url().host())) {
            return true;
         } else if (this.http2Connection == null) {
            return false;
         } else if (var2 == null) {
            return false;
         } else if (var2.proxy().type() != Type.DIRECT) {
            return false;
         } else if (this.route.proxy().type() != Type.DIRECT) {
            return false;
         } else if (!this.route.socketAddress().equals(var2.socketAddress())) {
            return false;
         } else if (var2.address().hostnameVerifier() != OkHostnameVerifier.INSTANCE) {
            return false;
         } else if (!this.supportsUrl(var1.url())) {
            return false;
         } else {
            try {
               var1.certificatePinner().check(var1.url().host(), this.handshake().peerCertificates());
               return true;
            } catch (SSLPeerUnverifiedException var3) {
               return false;
            }
         }
      } else {
         return false;
      }
   }

   public boolean isHealthy(boolean var1) {
      if (!this.socket.isClosed() && !this.socket.isInputShutdown()) {
         if (this.socket.isOutputShutdown()) {
            return false;
         } else {
            Http2Connection var3 = this.http2Connection;
            if (var3 != null) {
               return var3.isShutdown() ^ true;
            } else if (var1) {
               boolean var10001;
               int var2;
               try {
                  var2 = this.socket.getSoTimeout();
               } catch (SocketTimeoutException var21) {
                  var10001 = false;
                  return true;
               } catch (IOException var22) {
                  var10001 = false;
                  return false;
               }

               boolean var13 = false;

               try {
                  var13 = true;
                  this.socket.setSoTimeout(1);
                  var1 = this.source.exhausted();
                  var13 = false;
               } finally {
                  if (var13) {
                     try {
                        this.socket.setSoTimeout(var2);
                     } catch (SocketTimeoutException var14) {
                        var10001 = false;
                        return true;
                     } catch (IOException var15) {
                        var10001 = false;
                        return false;
                     }
                  }
               }

               if (var1) {
                  try {
                     this.socket.setSoTimeout(var2);
                     return false;
                  } catch (SocketTimeoutException var16) {
                     var10001 = false;
                     return true;
                  } catch (IOException var17) {
                     var10001 = false;
                  }
               } else {
                  try {
                     this.socket.setSoTimeout(var2);
                     return true;
                  } catch (SocketTimeoutException var18) {
                     var10001 = false;
                     return true;
                  } catch (IOException var19) {
                     var10001 = false;
                  }
               }

               return false;
            } else {
               return true;
            }
         }
      } else {
         return false;
      }
   }

   public boolean isMultiplexed() {
      return this.http2Connection != null;
   }

   public HttpCodec newCodec(OkHttpClient var1, StreamAllocation var2) throws SocketException {
      Http2Connection var3 = this.http2Connection;
      if (var3 != null) {
         return new Http2Codec(var1, var2, var3);
      } else {
         this.socket.setSoTimeout(var1.readTimeoutMillis());
         this.source.timeout().timeout((long)var1.readTimeoutMillis(), TimeUnit.MILLISECONDS);
         this.sink.timeout().timeout((long)var1.writeTimeoutMillis(), TimeUnit.MILLISECONDS);
         return new Http1Codec(var1, var2, this.source, this.sink);
      }
   }

   public RealWebSocket.Streams newWebSocketStreams(final StreamAllocation var1) {
      return new RealWebSocket.Streams(true, this.source, this.sink) {
         public void close() throws IOException {
            StreamAllocation var1x = var1;
            var1x.streamFinished(true, var1x.codec());
         }
      };
   }

   public void onSettings(Http2Connection param1) {
      // $FF: Couldn't be decompiled
   }

   public void onStream(Http2Stream var1) throws IOException {
      var1.close(ErrorCode.REFUSED_STREAM);
   }

   public Protocol protocol() {
      return this.protocol;
   }

   public Route route() {
      return this.route;
   }

   public Socket socket() {
      return this.socket;
   }

   public boolean supportsUrl(HttpUrl var1) {
      int var2 = var1.port();
      int var3 = this.route.address().url().port();
      boolean var5 = false;
      if (var2 != var3) {
         return false;
      } else if (!var1.host().equals(this.route.address().url().host())) {
         boolean var4 = var5;
         if (this.handshake != null) {
            var4 = var5;
            if (OkHostnameVerifier.INSTANCE.verify(var1.host(), (X509Certificate)this.handshake.peerCertificates().get(0))) {
               var4 = true;
            }
         }

         return var4;
      } else {
         return true;
      }
   }

   public String toString() {
      StringBuilder var2 = new StringBuilder();
      var2.append("Connection{");
      var2.append(this.route.address().url().host());
      var2.append(":");
      var2.append(this.route.address().url().port());
      var2.append(", proxy=");
      var2.append(this.route.proxy());
      var2.append(" hostAddress=");
      var2.append(this.route.socketAddress());
      var2.append(" cipherSuite=");
      Handshake var1 = this.handshake;
      Object var3;
      if (var1 != null) {
         var3 = var1.cipherSuite();
      } else {
         var3 = "none";
      }

      var2.append(var3);
      var2.append(" protocol=");
      var2.append(this.protocol);
      var2.append('}');
      return var2.toString();
   }
}
