package okhttp3.internal.connection;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.Proxy.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import okhttp3.Address;
import okhttp3.HttpUrl;
import okhttp3.Route;
import okhttp3.internal.Util;

public final class RouteSelector {
   private final Address address;
   private List inetSocketAddresses = Collections.emptyList();
   private InetSocketAddress lastInetSocketAddress;
   private Proxy lastProxy;
   private int nextInetSocketAddressIndex;
   private int nextProxyIndex;
   private final List postponedRoutes = new ArrayList();
   private List proxies = Collections.emptyList();
   private final RouteDatabase routeDatabase;

   public RouteSelector(Address var1, RouteDatabase var2) {
      this.address = var1;
      this.routeDatabase = var2;
      this.resetNextProxy(var1.url(), var1.proxy());
   }

   static String getHostString(InetSocketAddress var0) {
      InetAddress var1 = var0.getAddress();
      return var1 == null ? var0.getHostName() : var1.getHostAddress();
   }

   private boolean hasNextInetSocketAddress() {
      return this.nextInetSocketAddressIndex < this.inetSocketAddresses.size();
   }

   private boolean hasNextPostponed() {
      return this.postponedRoutes.isEmpty() ^ true;
   }

   private boolean hasNextProxy() {
      return this.nextProxyIndex < this.proxies.size();
   }

   private InetSocketAddress nextInetSocketAddress() throws IOException {
      if (this.hasNextInetSocketAddress()) {
         List var3 = this.inetSocketAddresses;
         int var1 = this.nextInetSocketAddressIndex++;
         return (InetSocketAddress)var3.get(var1);
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("No route to ");
         var2.append(this.address.url().host());
         var2.append("; exhausted inet socket addresses: ");
         var2.append(this.inetSocketAddresses);
         throw new SocketException(var2.toString());
      }
   }

   private Route nextPostponed() {
      return (Route)this.postponedRoutes.remove(0);
   }

   private Proxy nextProxy() throws IOException {
      if (this.hasNextProxy()) {
         List var3 = this.proxies;
         int var1 = this.nextProxyIndex++;
         Proxy var4 = (Proxy)var3.get(var1);
         this.resetNextInetSocketAddress(var4);
         return var4;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("No route to ");
         var2.append(this.address.url().host());
         var2.append("; exhausted proxy configurations: ");
         var2.append(this.proxies);
         throw new SocketException(var2.toString());
      }
   }

   private void resetNextInetSocketAddress(Proxy var1) throws IOException {
      this.inetSocketAddresses = new ArrayList();
      int var2;
      String var5;
      StringBuilder var7;
      if (var1.type() != Type.DIRECT && var1.type() != Type.SOCKS) {
         SocketAddress var9 = var1.address();
         if (!(var9 instanceof InetSocketAddress)) {
            var7 = new StringBuilder();
            var7.append("Proxy.address() is not an InetSocketAddress: ");
            var7.append(var9.getClass());
            throw new IllegalArgumentException(var7.toString());
         }

         InetSocketAddress var6 = (InetSocketAddress)var9;
         var5 = getHostString(var6);
         var2 = var6.getPort();
      } else {
         var5 = this.address.url().host();
         var2 = this.address.url().port();
      }

      if (var2 >= 1 && var2 <= 65535) {
         if (var1.type() == Type.SOCKS) {
            this.inetSocketAddresses.add(InetSocketAddress.createUnresolved(var5, var2));
         } else {
            List var8 = this.address.dns().lookup(var5);
            if (var8.isEmpty()) {
               var7 = new StringBuilder();
               var7.append(this.address.dns());
               var7.append(" returned no addresses for ");
               var7.append(var5);
               throw new UnknownHostException(var7.toString());
            }

            int var3 = 0;

            for(int var4 = var8.size(); var3 < var4; ++var3) {
               InetAddress var10 = (InetAddress)var8.get(var3);
               this.inetSocketAddresses.add(new InetSocketAddress(var10, var2));
            }
         }

         this.nextInetSocketAddressIndex = 0;
      } else {
         var7 = new StringBuilder();
         var7.append("No route to ");
         var7.append(var5);
         var7.append(":");
         var7.append(var2);
         var7.append("; port is out of range");
         throw new SocketException(var7.toString());
      }
   }

   private void resetNextProxy(HttpUrl var1, Proxy var2) {
      if (var2 != null) {
         this.proxies = Collections.singletonList(var2);
      } else {
         List var3 = this.address.proxySelector().select(var1.uri());
         if (var3 != null && !var3.isEmpty()) {
            var3 = Util.immutableList(var3);
         } else {
            var3 = Util.immutableList((Object[])(Proxy.NO_PROXY));
         }

         this.proxies = var3;
      }

      this.nextProxyIndex = 0;
   }

   public void connectFailed(Route var1, IOException var2) {
      if (var1.proxy().type() != Type.DIRECT && this.address.proxySelector() != null) {
         this.address.proxySelector().connectFailed(this.address.url().uri(), var1.proxy().address(), var2);
      }

      this.routeDatabase.failed(var1);
   }

   public boolean hasNext() {
      return this.hasNextInetSocketAddress() || this.hasNextProxy() || this.hasNextPostponed();
   }

   public Route next() throws IOException {
      if (!this.hasNextInetSocketAddress()) {
         if (!this.hasNextProxy()) {
            if (this.hasNextPostponed()) {
               return this.nextPostponed();
            }

            throw new NoSuchElementException();
         }

         this.lastProxy = this.nextProxy();
      }

      InetSocketAddress var1 = this.nextInetSocketAddress();
      this.lastInetSocketAddress = var1;
      Route var2 = new Route(this.address, this.lastProxy, var1);
      if (this.routeDatabase.shouldPostpone(var2)) {
         this.postponedRoutes.add(var2);
         return this.next();
      } else {
         return var2;
      }
   }
}
