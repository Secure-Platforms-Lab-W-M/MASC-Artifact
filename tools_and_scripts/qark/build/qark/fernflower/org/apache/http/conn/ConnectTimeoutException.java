package org.apache.http.conn;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetAddress;
import java.util.Arrays;
import org.apache.http.HttpHost;

public class ConnectTimeoutException extends InterruptedIOException {
   private static final long serialVersionUID = -4816682903149535989L;
   private final HttpHost host;

   public ConnectTimeoutException() {
      this.host = null;
   }

   public ConnectTimeoutException(IOException var1, HttpHost var2, InetAddress... var3) {
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
         var7 = " timed out";
      }

      var5.append(var7);
      super(var5.toString());
      this.host = var2;
      this.initCause(var1);
   }

   public ConnectTimeoutException(String var1) {
      super(var1);
      this.host = null;
   }

   public HttpHost getHost() {
      return this.host;
   }
}
