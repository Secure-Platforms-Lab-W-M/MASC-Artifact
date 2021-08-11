package org.apache.http.conn;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import org.apache.http.HttpHost;
import org.apache.http.util.Args;

@Deprecated
public class HttpInetSocketAddress extends InetSocketAddress {
   private static final long serialVersionUID = -6650701828361907957L;
   private final HttpHost httphost;

   public HttpInetSocketAddress(HttpHost var1, InetAddress var2, int var3) {
      super(var2, var3);
      Args.notNull(var1, "HTTP host");
      this.httphost = var1;
   }

   public HttpHost getHttpHost() {
      return this.httphost;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.httphost.getHostName());
      var1.append(":");
      var1.append(this.getPort());
      return var1.toString();
   }
}
