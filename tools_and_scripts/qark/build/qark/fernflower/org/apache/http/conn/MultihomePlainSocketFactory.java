package org.apache.http.conn;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import org.apache.http.conn.scheme.SocketFactory;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
public final class MultihomePlainSocketFactory implements SocketFactory {
   private static final MultihomePlainSocketFactory DEFAULT_FACTORY = new MultihomePlainSocketFactory();

   private MultihomePlainSocketFactory() {
   }

   public static MultihomePlainSocketFactory getSocketFactory() {
      return DEFAULT_FACTORY;
   }

   public Socket connectSocket(Socket var1, String var2, int var3, InetAddress var4, int var5, HttpParams var6) throws IOException {
      Args.notNull(var2, "Target host");
      Args.notNull(var6, "HTTP parameters");
      Socket var7 = var1;
      var1 = var1;
      if (var7 == null) {
         var1 = this.createSocket();
      }

      if (var4 != null || var5 > 0) {
         if (var5 <= 0) {
            var5 = 0;
         }

         var1.bind(new InetSocketAddress(var4, var5));
      }

      var5 = HttpConnectionParams.getConnectionTimeout(var6);
      InetAddress[] var10 = InetAddress.getAllByName(var2);
      ArrayList var13 = new ArrayList(var10.length);
      var13.addAll(Arrays.asList(var10));
      Collections.shuffle(var13);
      Iterator var15 = var13.iterator();
      IOException var11 = null;

      while(var15.hasNext()) {
         var4 = (InetAddress)var15.next();

         try {
            var1.connect(new InetSocketAddress(var4, var3), var5);
            break;
         } catch (SocketTimeoutException var8) {
            StringBuilder var12 = new StringBuilder();
            var12.append("Connect to ");
            var12.append(var4);
            var12.append(" timed out");
            throw new ConnectTimeoutException(var12.toString());
         } catch (IOException var9) {
            Socket var14 = new Socket();
            var11 = var9;
            var1 = var14;
         }
      }

      if (var11 == null) {
         return var1;
      } else {
         throw var11;
      }
   }

   public Socket createSocket() {
      return new Socket();
   }

   public final boolean isSecure(Socket var1) throws IllegalArgumentException {
      Args.notNull(var1, "Socket");
      Asserts.check(var1.isClosed() ^ true, "Socket is closed");
      return false;
   }
}
