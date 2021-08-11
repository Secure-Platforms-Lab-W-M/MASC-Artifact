package org.apache.http.impl.conn;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.Proxy.Type;
import java.util.List;
import org.apache.http.HttpException;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.conn.SchemePortResolver;
import org.apache.http.protocol.HttpContext;

public class SystemDefaultRoutePlanner extends DefaultRoutePlanner {
   private final ProxySelector proxySelector;

   public SystemDefaultRoutePlanner(ProxySelector var1) {
      this((SchemePortResolver)null, var1);
   }

   public SystemDefaultRoutePlanner(SchemePortResolver var1, ProxySelector var2) {
      super(var1);
      this.proxySelector = var2;
   }

   private Proxy chooseProxy(List var1) {
      Proxy var4 = null;

      for(int var2 = 0; var4 == null && var2 < var1.size(); ++var2) {
         Proxy var5 = (Proxy)var1.get(var2);
         int var3 = null.$SwitchMap$java$net$Proxy$Type[var5.type().ordinal()];
         if (var3 == 1 || var3 == 2) {
            var4 = var5;
         }
      }

      Proxy var6 = var4;
      if (var4 == null) {
         var6 = Proxy.NO_PROXY;
      }

      return var6;
   }

   private String getHost(InetSocketAddress var1) {
      return var1.isUnresolved() ? var1.getHostName() : var1.getAddress().getHostAddress();
   }

   protected HttpHost determineProxy(HttpHost var1, HttpRequest var2, HttpContext var3) throws HttpException {
      URI var11;
      try {
         var11 = new URI(var1.toURI());
      } catch (URISyntaxException var4) {
         StringBuilder var10 = new StringBuilder();
         var10.append("Cannot convert host to URI: ");
         var10.append(var1);
         throw new HttpException(var10.toString(), var4);
      }

      ProxySelector var6 = this.proxySelector;
      ProxySelector var5 = var6;
      if (var6 == null) {
         var5 = ProxySelector.getDefault();
      }

      if (var5 == null) {
         return null;
      } else {
         Proxy var7 = this.chooseProxy(var5.select(var11));
         if (var7.type() == Type.HTTP) {
            if (var7.address() instanceof InetSocketAddress) {
               InetSocketAddress var8 = (InetSocketAddress)var7.address();
               return new HttpHost(this.getHost(var8), var8.getPort());
            } else {
               StringBuilder var9 = new StringBuilder();
               var9.append("Unable to handle non-Inet proxy address: ");
               var9.append(var7.address());
               throw new HttpException(var9.toString());
            }
         } else {
            return null;
         }
      }
   }
}
