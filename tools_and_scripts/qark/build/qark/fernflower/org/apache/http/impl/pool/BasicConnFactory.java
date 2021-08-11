package org.apache.http.impl.pool;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import javax.net.SocketFactory;
import javax.net.ssl.SSLSocketFactory;
import org.apache.http.HttpClientConnection;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.HttpHost;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.SocketConfig;
import org.apache.http.impl.DefaultBHttpClientConnection;
import org.apache.http.impl.DefaultBHttpClientConnectionFactory;
import org.apache.http.params.HttpParamConfig;
import org.apache.http.params.HttpParams;
import org.apache.http.pool.ConnFactory;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

public class BasicConnFactory implements ConnFactory {
   private final HttpConnectionFactory connFactory;
   private final int connectTimeout;
   private final SocketFactory plainfactory;
   private final SocketConfig sconfig;
   private final SSLSocketFactory sslfactory;

   public BasicConnFactory() {
      this((SocketFactory)null, (SSLSocketFactory)null, 0, SocketConfig.DEFAULT, ConnectionConfig.DEFAULT);
   }

   public BasicConnFactory(int var1, SocketConfig var2, ConnectionConfig var3) {
      this((SocketFactory)null, (SSLSocketFactory)null, var1, var2, var3);
   }

   public BasicConnFactory(SocketFactory var1, SSLSocketFactory var2, int var3, SocketConfig var4, ConnectionConfig var5) {
      this.plainfactory = var1;
      this.sslfactory = var2;
      this.connectTimeout = var3;
      if (var4 == null) {
         var4 = SocketConfig.DEFAULT;
      }

      this.sconfig = var4;
      if (var5 == null) {
         var5 = ConnectionConfig.DEFAULT;
      }

      this.connFactory = new DefaultBHttpClientConnectionFactory(var5);
   }

   @Deprecated
   public BasicConnFactory(SSLSocketFactory var1, HttpParams var2) {
      Args.notNull(var2, "HTTP params");
      this.plainfactory = null;
      this.sslfactory = var1;
      this.connectTimeout = var2.getIntParameter("http.connection.timeout", 0);
      this.sconfig = HttpParamConfig.getSocketConfig(var2);
      this.connFactory = new DefaultBHttpClientConnectionFactory(HttpParamConfig.getConnectionConfig(var2));
   }

   public BasicConnFactory(SocketConfig var1, ConnectionConfig var2) {
      this((SocketFactory)null, (SSLSocketFactory)null, 0, var1, var2);
   }

   @Deprecated
   public BasicConnFactory(HttpParams var1) {
      this((SSLSocketFactory)null, (HttpParams)var1);
   }

   @Deprecated
   protected HttpClientConnection create(Socket var1, HttpParams var2) throws IOException {
      DefaultBHttpClientConnection var3 = new DefaultBHttpClientConnection(var2.getIntParameter("http.socket.buffer-size", 8192));
      var3.bind(var1);
      return var3;
   }

   public HttpClientConnection create(HttpHost var1) throws IOException {
      String var5 = var1.getSchemeName();
      final Socket var11;
      if ("http".equalsIgnoreCase(var5)) {
         SocketFactory var10 = this.plainfactory;
         if (var10 != null) {
            var11 = var10.createSocket();
         } else {
            var11 = new Socket();
         }
      } else {
         if (!"https".equalsIgnoreCase(var5)) {
            StringBuilder var9 = new StringBuilder();
            var9.append(var5);
            var9.append(" scheme is not supported");
            throw new IOException(var9.toString());
         }

         Object var12 = this.sslfactory;
         if (var12 == null) {
            var12 = SSLSocketFactory.getDefault();
         }

         var11 = ((SocketFactory)var12).createSocket();
      }

      String var6 = var1.getHostName();
      int var3 = var1.getPort();
      int var2 = var3;
      if (var3 == -1) {
         if (var1.getSchemeName().equalsIgnoreCase("http")) {
            var2 = 80;
         } else {
            var2 = var3;
            if (var1.getSchemeName().equalsIgnoreCase("https")) {
               var2 = 443;
            }
         }
      }

      var11.setSoTimeout(this.sconfig.getSoTimeout());
      if (this.sconfig.getSndBufSize() > 0) {
         var11.setSendBufferSize(this.sconfig.getSndBufSize());
      }

      if (this.sconfig.getRcvBufSize() > 0) {
         var11.setReceiveBufferSize(this.sconfig.getRcvBufSize());
      }

      var11.setTcpNoDelay(this.sconfig.isTcpNoDelay());
      var3 = this.sconfig.getSoLinger();
      if (var3 >= 0) {
         var11.setSoLinger(true, var3);
      }

      var11.setKeepAlive(this.sconfig.isSoKeepAlive());
      final InetSocketAddress var8 = new InetSocketAddress(var6, var2);

      try {
         AccessController.doPrivileged(new PrivilegedExceptionAction() {
            public Object run() throws IOException {
               var11.connect(var8, BasicConnFactory.this.connectTimeout);
               return null;
            }
         });
      } catch (PrivilegedActionException var7) {
         boolean var4 = var7.getCause() instanceof IOException;
         StringBuilder var13 = new StringBuilder();
         var13.append("method contract violation only checked exceptions are wrapped: ");
         var13.append(var7.getCause());
         Asserts.check(var4, var13.toString());
         throw (IOException)var7.getCause();
      }

      return (HttpClientConnection)this.connFactory.createConnection(var11);
   }
}
