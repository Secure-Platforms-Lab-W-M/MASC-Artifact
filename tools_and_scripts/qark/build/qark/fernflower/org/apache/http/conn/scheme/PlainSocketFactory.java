package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;

@Deprecated
public class PlainSocketFactory implements SocketFactory, SchemeSocketFactory {
   private final HostNameResolver nameResolver;

   public PlainSocketFactory() {
      this.nameResolver = null;
   }

   @Deprecated
   public PlainSocketFactory(HostNameResolver var1) {
      this.nameResolver = var1;
   }

   public static PlainSocketFactory getSocketFactory() {
      return new PlainSocketFactory();
   }

   @Deprecated
   public Socket connectSocket(Socket var1, String var2, int var3, InetAddress var4, int var5, HttpParams var6) throws IOException, UnknownHostException, ConnectTimeoutException {
      InetSocketAddress var7 = null;
      if (var4 != null || var5 > 0) {
         if (var5 <= 0) {
            var5 = 0;
         }

         var7 = new InetSocketAddress(var4, var5);
      }

      HostNameResolver var9 = this.nameResolver;
      InetAddress var8;
      if (var9 != null) {
         var8 = var9.resolve(var2);
      } else {
         var8 = InetAddress.getByName(var2);
      }

      return this.connectSocket(var1, new InetSocketAddress(var8, var3), var7, var6);
   }

   public Socket connectSocket(Socket var1, InetSocketAddress var2, InetSocketAddress var3, HttpParams var4) throws IOException, ConnectTimeoutException {
      Args.notNull(var2, "Remote address");
      Args.notNull(var4, "HTTP parameters");
      Socket var7 = var1;
      if (var1 == null) {
         var7 = this.createSocket();
      }

      if (var3 != null) {
         var7.setReuseAddress(HttpConnectionParams.getSoReuseaddr(var4));
         var7.bind(var3);
      }

      int var5 = HttpConnectionParams.getConnectionTimeout(var4);
      int var6 = HttpConnectionParams.getSoTimeout(var4);

      try {
         var7.setSoTimeout(var6);
         var7.connect(var2, var5);
         return var7;
      } catch (SocketTimeoutException var8) {
         StringBuilder var9 = new StringBuilder();
         var9.append("Connect to ");
         var9.append(var2);
         var9.append(" timed out");
         throw new ConnectTimeoutException(var9.toString());
      }
   }

   public Socket createSocket() {
      return new Socket();
   }

   public Socket createSocket(HttpParams var1) {
      return new Socket();
   }

   public final boolean isSecure(Socket var1) {
      return false;
   }
}
