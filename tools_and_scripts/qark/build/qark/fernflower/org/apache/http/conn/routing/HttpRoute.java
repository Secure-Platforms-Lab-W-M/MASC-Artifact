package org.apache.http.conn.routing;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.http.HttpHost;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

public final class HttpRoute implements RouteInfo, Cloneable {
   private final RouteInfo.LayerType layered;
   private final InetAddress localAddress;
   private final List proxyChain;
   private final boolean secure;
   private final HttpHost targetHost;
   private final RouteInfo.TunnelType tunnelled;

   public HttpRoute(HttpHost var1) {
      this(var1, (InetAddress)null, (List)Collections.emptyList(), false, RouteInfo.TunnelType.PLAIN, RouteInfo.LayerType.PLAIN);
   }

   private HttpRoute(HttpHost var1, InetAddress var2, List var3, boolean var4, RouteInfo.TunnelType var5, RouteInfo.LayerType var6) {
      Args.notNull(var1, "Target host");
      this.targetHost = normalize(var1);
      this.localAddress = var2;
      if (var3 != null && !var3.isEmpty()) {
         this.proxyChain = new ArrayList(var3);
      } else {
         this.proxyChain = null;
      }

      if (var5 == RouteInfo.TunnelType.TUNNELLED) {
         boolean var7;
         if (this.proxyChain != null) {
            var7 = true;
         } else {
            var7 = false;
         }

         Args.check(var7, "Proxy required if tunnelled");
      }

      this.secure = var4;
      if (var5 == null) {
         var5 = RouteInfo.TunnelType.PLAIN;
      }

      this.tunnelled = var5;
      if (var6 == null) {
         var6 = RouteInfo.LayerType.PLAIN;
      }

      this.layered = var6;
   }

   public HttpRoute(HttpHost var1, InetAddress var2, HttpHost var3, boolean var4) {
      List var6 = Collections.singletonList(Args.notNull(var3, "Proxy host"));
      RouteInfo.TunnelType var7;
      if (var4) {
         var7 = RouteInfo.TunnelType.TUNNELLED;
      } else {
         var7 = RouteInfo.TunnelType.PLAIN;
      }

      RouteInfo.LayerType var5;
      if (var4) {
         var5 = RouteInfo.LayerType.LAYERED;
      } else {
         var5 = RouteInfo.LayerType.PLAIN;
      }

      this(var1, var2, var6, var4, var7, var5);
   }

   public HttpRoute(HttpHost var1, InetAddress var2, HttpHost var3, boolean var4, RouteInfo.TunnelType var5, RouteInfo.LayerType var6) {
      List var7;
      if (var3 != null) {
         var7 = Collections.singletonList(var3);
      } else {
         var7 = null;
      }

      this(var1, var2, var7, var4, var5, var6);
   }

   public HttpRoute(HttpHost var1, InetAddress var2, boolean var3) {
      this(var1, var2, Collections.emptyList(), var3, RouteInfo.TunnelType.PLAIN, RouteInfo.LayerType.PLAIN);
   }

   public HttpRoute(HttpHost var1, InetAddress var2, HttpHost[] var3, boolean var4, RouteInfo.TunnelType var5, RouteInfo.LayerType var6) {
      List var7;
      if (var3 != null) {
         var7 = Arrays.asList(var3);
      } else {
         var7 = null;
      }

      this(var1, var2, var7, var4, var5, var6);
   }

   public HttpRoute(HttpHost var1, HttpHost var2) {
      this(var1, (InetAddress)null, var2, false);
   }

   private static int getDefaultPort(String var0) {
      if ("http".equalsIgnoreCase(var0)) {
         return 80;
      } else {
         return "https".equalsIgnoreCase(var0) ? 443 : -1;
      }
   }

   private static HttpHost normalize(HttpHost var0) {
      if (var0.getPort() >= 0) {
         return var0;
      } else {
         InetAddress var1 = var0.getAddress();
         String var2 = var0.getSchemeName();
         if (var1 != null) {
            var0 = new HttpHost(var1, getDefaultPort(var2), var2);
            return var0;
         } else {
            var0 = new HttpHost(var0.getHostName(), getDefaultPort(var2), var2);
            return var0;
         }
      }
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   public final boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 instanceof HttpRoute) {
         HttpRoute var2 = (HttpRoute)var1;
         return this.secure == var2.secure && this.tunnelled == var2.tunnelled && this.layered == var2.layered && LangUtils.equals(this.targetHost, var2.targetHost) && LangUtils.equals(this.localAddress, var2.localAddress) && LangUtils.equals(this.proxyChain, var2.proxyChain);
      } else {
         return false;
      }
   }

   public final int getHopCount() {
      List var2 = this.proxyChain;
      int var1 = 1;
      if (var2 != null) {
         var1 = 1 + var2.size();
      }

      return var1;
   }

   public final HttpHost getHopTarget(int var1) {
      Args.notNegative(var1, "Hop index");
      int var2 = this.getHopCount();
      boolean var3;
      if (var1 < var2) {
         var3 = true;
      } else {
         var3 = false;
      }

      Args.check(var3, "Hop index exceeds tracked route length");
      return var1 < var2 - 1 ? (HttpHost)this.proxyChain.get(var1) : this.targetHost;
   }

   public final RouteInfo.LayerType getLayerType() {
      return this.layered;
   }

   public final InetAddress getLocalAddress() {
      return this.localAddress;
   }

   public final InetSocketAddress getLocalSocketAddress() {
      return this.localAddress != null ? new InetSocketAddress(this.localAddress, 0) : null;
   }

   public final HttpHost getProxyHost() {
      List var1 = this.proxyChain;
      return var1 != null && !var1.isEmpty() ? (HttpHost)this.proxyChain.get(0) : null;
   }

   public final HttpHost getTargetHost() {
      return this.targetHost;
   }

   public final RouteInfo.TunnelType getTunnelType() {
      return this.tunnelled;
   }

   public final int hashCode() {
      int var1 = LangUtils.hashCode(LangUtils.hashCode(17, this.targetHost), this.localAddress);
      List var3 = this.proxyChain;
      int var2 = var1;
      if (var3 != null) {
         Iterator var4 = var3.iterator();

         while(true) {
            var2 = var1;
            if (!var4.hasNext()) {
               break;
            }

            var1 = LangUtils.hashCode(var1, (HttpHost)var4.next());
         }
      }

      return LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(var2, this.secure), this.tunnelled), this.layered);
   }

   public final boolean isLayered() {
      return this.layered == RouteInfo.LayerType.LAYERED;
   }

   public final boolean isSecure() {
      return this.secure;
   }

   public final boolean isTunnelled() {
      return this.tunnelled == RouteInfo.TunnelType.TUNNELLED;
   }

   public final String toString() {
      StringBuilder var1 = new StringBuilder(this.getHopCount() * 30 + 50);
      InetAddress var2 = this.localAddress;
      if (var2 != null) {
         var1.append(var2);
         var1.append("->");
      }

      var1.append('{');
      if (this.tunnelled == RouteInfo.TunnelType.TUNNELLED) {
         var1.append('t');
      }

      if (this.layered == RouteInfo.LayerType.LAYERED) {
         var1.append('l');
      }

      if (this.secure) {
         var1.append('s');
      }

      var1.append("}->");
      List var3 = this.proxyChain;
      if (var3 != null) {
         Iterator var4 = var3.iterator();

         while(var4.hasNext()) {
            var1.append((HttpHost)var4.next());
            var1.append("->");
         }
      }

      var1.append(this.targetHost);
      return var1.toString();
   }
}
