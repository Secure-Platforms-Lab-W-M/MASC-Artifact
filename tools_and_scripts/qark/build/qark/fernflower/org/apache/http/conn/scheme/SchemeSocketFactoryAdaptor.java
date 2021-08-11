package org.apache.http.conn.scheme;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.HttpParams;

@Deprecated
class SchemeSocketFactoryAdaptor implements SchemeSocketFactory {
   private final SocketFactory factory;

   SchemeSocketFactoryAdaptor(SocketFactory var1) {
      this.factory = var1;
   }

   public Socket connectSocket(Socket var1, InetSocketAddress var2, InetSocketAddress var3, HttpParams var4) throws IOException, UnknownHostException, ConnectTimeoutException {
      String var7 = var2.getHostName();
      int var6 = var2.getPort();
      int var5;
      InetAddress var8;
      if (var3 != null) {
         var8 = var3.getAddress();
         var5 = var3.getPort();
      } else {
         var8 = null;
         var5 = 0;
      }

      return this.factory.connectSocket(var1, var7, var6, var8, var5, var4);
   }

   public Socket createSocket(HttpParams var1) throws IOException {
      return this.factory.createSocket();
   }

   public boolean equals(Object var1) {
      if (var1 == null) {
         return false;
      } else if (this == var1) {
         return true;
      } else {
         return var1 instanceof SchemeSocketFactoryAdaptor ? this.factory.equals(((SchemeSocketFactoryAdaptor)var1).factory) : this.factory.equals(var1);
      }
   }

   public SocketFactory getFactory() {
      return this.factory;
   }

   public int hashCode() {
      return this.factory.hashCode();
   }

   public boolean isSecure(Socket var1) throws IllegalArgumentException {
      return this.factory.isSecure(var1);
   }
}
