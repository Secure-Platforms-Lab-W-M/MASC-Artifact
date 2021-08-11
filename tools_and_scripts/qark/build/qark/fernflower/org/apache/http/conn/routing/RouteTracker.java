package org.apache.http.conn.routing;

import java.net.InetAddress;
import org.apache.http.HttpHost;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.apache.http.util.LangUtils;

public final class RouteTracker implements RouteInfo, Cloneable {
   private boolean connected;
   private RouteInfo.LayerType layered;
   private final InetAddress localAddress;
   private HttpHost[] proxyChain;
   private boolean secure;
   private final HttpHost targetHost;
   private RouteInfo.TunnelType tunnelled;

   public RouteTracker(HttpHost var1, InetAddress var2) {
      Args.notNull(var1, "Target host");
      this.targetHost = var1;
      this.localAddress = var2;
      this.tunnelled = RouteInfo.TunnelType.PLAIN;
      this.layered = RouteInfo.LayerType.PLAIN;
   }

   public RouteTracker(HttpRoute var1) {
      this(var1.getTargetHost(), var1.getLocalAddress());
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   public final void connectProxy(HttpHost var1, boolean var2) {
      Args.notNull(var1, "Proxy host");
      Asserts.check(this.connected ^ true, "Already connected");
      this.connected = true;
      this.proxyChain = new HttpHost[]{var1};
      this.secure = var2;
   }

   public final void connectTarget(boolean var1) {
      Asserts.check(this.connected ^ true, "Already connected");
      this.connected = true;
      this.secure = var1;
   }

   public final boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof RouteTracker)) {
         return false;
      } else {
         RouteTracker var2 = (RouteTracker)var1;
         return this.connected == var2.connected && this.secure == var2.secure && this.tunnelled == var2.tunnelled && this.layered == var2.layered && LangUtils.equals(this.targetHost, var2.targetHost) && LangUtils.equals(this.localAddress, var2.localAddress) && LangUtils.equals(this.proxyChain, var2.proxyChain);
      }
   }

   public final int getHopCount() {
      int var1 = 0;
      if (this.connected) {
         HttpHost[] var2 = this.proxyChain;
         if (var2 == null) {
            return 1;
         }

         var1 = var2.length + 1;
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
      return var1 < var2 - 1 ? this.proxyChain[var1] : this.targetHost;
   }

   public final RouteInfo.LayerType getLayerType() {
      return this.layered;
   }

   public final InetAddress getLocalAddress() {
      return this.localAddress;
   }

   public final HttpHost getProxyHost() {
      HttpHost[] var1 = this.proxyChain;
      return var1 == null ? null : var1[0];
   }

   public final HttpHost getTargetHost() {
      return this.targetHost;
   }

   public final RouteInfo.TunnelType getTunnelType() {
      return this.tunnelled;
   }

   public final int hashCode() {
      int var1 = LangUtils.hashCode(LangUtils.hashCode(17, this.targetHost), this.localAddress);
      int var3 = var1;
      if (this.proxyChain != null) {
         HttpHost[] var5 = this.proxyChain;
         int var4 = var5.length;
         int var2 = 0;

         while(true) {
            var3 = var1;
            if (var2 >= var4) {
               break;
            }

            var1 = LangUtils.hashCode(var1, var5[var2]);
            ++var2;
         }
      }

      return LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(var3, this.connected), this.secure), this.tunnelled), this.layered);
   }

   public final boolean isConnected() {
      return this.connected;
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

   public final void layerProtocol(boolean var1) {
      Asserts.check(this.connected, "No layered protocol unless connected");
      this.layered = RouteInfo.LayerType.LAYERED;
      this.secure = var1;
   }

   public void reset() {
      this.connected = false;
      this.proxyChain = null;
      this.tunnelled = RouteInfo.TunnelType.PLAIN;
      this.layered = RouteInfo.LayerType.PLAIN;
      this.secure = false;
   }

   public final HttpRoute toRoute() {
      return !this.connected ? null : new HttpRoute(this.targetHost, this.localAddress, this.proxyChain, this.secure, this.tunnelled, this.layered);
   }

   public final String toString() {
      StringBuilder var3 = new StringBuilder(this.getHopCount() * 30 + 50);
      var3.append("RouteTracker[");
      InetAddress var4 = this.localAddress;
      if (var4 != null) {
         var3.append(var4);
         var3.append("->");
      }

      var3.append('{');
      if (this.connected) {
         var3.append('c');
      }

      if (this.tunnelled == RouteInfo.TunnelType.TUNNELLED) {
         var3.append('t');
      }

      if (this.layered == RouteInfo.LayerType.LAYERED) {
         var3.append('l');
      }

      if (this.secure) {
         var3.append('s');
      }

      var3.append("}->");
      if (this.proxyChain != null) {
         HttpHost[] var5 = this.proxyChain;
         int var2 = var5.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            var3.append(var5[var1]);
            var3.append("->");
         }
      }

      var3.append(this.targetHost);
      var3.append(']');
      return var3.toString();
   }

   public final void tunnelProxy(HttpHost var1, boolean var2) {
      Args.notNull(var1, "Proxy host");
      Asserts.check(this.connected, "No tunnel unless connected");
      Asserts.notNull(this.proxyChain, "No tunnel without proxy");
      HttpHost[] var3 = this.proxyChain;
      HttpHost[] var4 = new HttpHost[var3.length + 1];
      System.arraycopy(var3, 0, var4, 0, var3.length);
      var4[var4.length - 1] = var1;
      this.proxyChain = var4;
      this.secure = var2;
   }

   public final void tunnelTarget(boolean var1) {
      Asserts.check(this.connected, "No tunnel unless connected");
      Asserts.notNull(this.proxyChain, "No tunnel without proxy");
      this.tunnelled = RouteInfo.TunnelType.TUNNELLED;
      this.secure = var1;
   }
}
