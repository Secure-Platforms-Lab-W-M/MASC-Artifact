package org.apache.http.conn.socket;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import org.apache.http.HttpHost;
import org.apache.http.protocol.HttpContext;

public class PlainConnectionSocketFactory implements ConnectionSocketFactory {
   public static final PlainConnectionSocketFactory INSTANCE = new PlainConnectionSocketFactory();

   public static PlainConnectionSocketFactory getSocketFactory() {
      return INSTANCE;
   }

   public Socket connectSocket(int var1, Socket var2, HttpHost var3, InetSocketAddress var4, InetSocketAddress var5, HttpContext var6) throws IOException {
      if (var2 == null) {
         var2 = this.createSocket(var6);
      }

      if (var5 != null) {
         var2.bind(var5);
      }

      try {
         var2.connect(var4, var1);
         return var2;
      } catch (IOException var8) {
         try {
            var2.close();
         } catch (IOException var7) {
         }

         throw var8;
      }
   }

   public Socket createSocket(HttpContext var1) throws IOException {
      return new Socket();
   }
}
