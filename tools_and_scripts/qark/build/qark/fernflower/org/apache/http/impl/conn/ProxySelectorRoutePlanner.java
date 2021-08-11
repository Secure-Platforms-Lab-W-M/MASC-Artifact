package org.apache.http.impl.conn;

import java.net.InetAddress;
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
import org.apache.http.conn.params.ConnRouteParams;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.routing.HttpRoutePlanner;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;

@Deprecated
public class ProxySelectorRoutePlanner implements HttpRoutePlanner {
   protected ProxySelector proxySelector;
   protected final SchemeRegistry schemeRegistry;

   public ProxySelectorRoutePlanner(SchemeRegistry var1, ProxySelector var2) {
      Args.notNull(var1, "SchemeRegistry");
      this.schemeRegistry = var1;
      this.proxySelector = var2;
   }

   protected Proxy chooseProxy(List var1, HttpHost var2, HttpRequest var3, HttpContext var4) {
      Args.notEmpty(var1, "List of proxies");
      Proxy var8 = null;

      for(int var5 = 0; var8 == null && var5 < var1.size(); ++var5) {
         Proxy var9 = (Proxy)var1.get(var5);
         int var6 = null.$SwitchMap$java$net$Proxy$Type[var9.type().ordinal()];
         if (var6 == 1 || var6 == 2) {
            var8 = var9;
         }
      }

      Proxy var7 = var8;
      if (var8 == null) {
         var7 = Proxy.NO_PROXY;
      }

      return var7;
   }

   protected HttpHost determineProxy(HttpHost var1, HttpRequest var2, HttpContext var3) throws HttpException {
      ProxySelector var5 = this.proxySelector;
      ProxySelector var4 = var5;
      if (var5 == null) {
         var4 = ProxySelector.getDefault();
      }

      if (var4 == null) {
         return null;
      } else {
         URI var11;
         try {
            var11 = new URI(var1.toURI());
         } catch (URISyntaxException var6) {
            StringBuilder var10 = new StringBuilder();
            var10.append("Cannot convert host to URI: ");
            var10.append(var1);
            throw new HttpException(var10.toString(), var6);
         }

         Proxy var7 = this.chooseProxy(var4.select(var11), var1, var2, var3);
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

   public HttpRoute determineRoute(HttpHost var1, HttpRequest var2, HttpContext var3) throws HttpException {
      Args.notNull(var2, "HTTP request");
      HttpRoute var5 = ConnRouteParams.getForcedRoute(var2.getParams());
      if (var5 != null) {
         return var5;
      } else {
         Asserts.notNull(var1, "Target host");
         InetAddress var6 = ConnRouteParams.getLocalAddress(var2.getParams());
         HttpHost var7 = this.determineProxy(var1, var2, var3);
         boolean var4 = this.schemeRegistry.getScheme(var1.getSchemeName()).isLayered();
         return var7 == null ? new HttpRoute(var1, var6, var4) : new HttpRoute(var1, var6, var7, var4);
      }
   }

   protected String getHost(InetSocketAddress var1) {
      return var1.isUnresolved() ? var1.getHostName() : var1.getAddress().getHostAddress();
   }

   public ProxySelector getProxySelector() {
      return this.proxySelector;
   }

   public void setProxySelector(ProxySelector var1) {
      this.proxySelector = var1;
   }
}
