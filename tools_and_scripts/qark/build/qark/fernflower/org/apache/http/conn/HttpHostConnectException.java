package org.apache.http.conn;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetAddress;
import java.util.Arrays;
import org.apache.http.HttpHost;

public class HttpHostConnectException extends ConnectException {
   private static final long serialVersionUID = -3194482710275220224L;
   private final HttpHost host;

   public HttpHostConnectException(IOException var1, HttpHost var2, InetAddress... var3) {
      StringBuilder var5 = new StringBuilder();
      var5.append("Connect to ");
      String var4;
      if (var2 != null) {
         var4 = var2.toHostString();
      } else {
         var4 = "remote host";
      }

      var5.append(var4);
      String var7;
      if (var3 != null && var3.length > 0) {
         StringBuilder var6 = new StringBuilder();
         var6.append(" ");
         var6.append(Arrays.asList(var3));
         var7 = var6.toString();
      } else {
         var7 = "";
      }

      var5.append(var7);
      if (var1 != null && var1.getMessage() != null) {
         StringBuilder var8 = new StringBuilder();
         var8.append(" failed: ");
         var8.append(var1.getMessage());
         var7 = var8.toString();
      } else {
         var7 = " refused";
      }

      var5.append(var7);
      super(var5.toString());
      this.host = var2;
      this.initCause(var1);
   }

   @Deprecated
   public HttpHostConnectException(HttpHost var1, ConnectException var2) {
      this(var2, var1, (InetAddress[])null);
   }

   public HttpHost getHost() {
      return this.host;
   }
}
