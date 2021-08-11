package okhttp3;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Proxy.Type;
import javax.annotation.Nullable;

public final class Route {
   final Address address;
   final InetSocketAddress inetSocketAddress;
   final Proxy proxy;

   public Route(Address var1, Proxy var2, InetSocketAddress var3) {
      if (var1 != null) {
         if (var2 != null) {
            if (var3 != null) {
               this.address = var1;
               this.proxy = var2;
               this.inetSocketAddress = var3;
            } else {
               throw new NullPointerException("inetSocketAddress == null");
            }
         } else {
            throw new NullPointerException("proxy == null");
         }
      } else {
         throw new NullPointerException("address == null");
      }
   }

   public Address address() {
      return this.address;
   }

   public boolean equals(@Nullable Object var1) {
      return var1 instanceof Route && ((Route)var1).address.equals(this.address) && ((Route)var1).proxy.equals(this.proxy) && ((Route)var1).inetSocketAddress.equals(this.inetSocketAddress);
   }

   public int hashCode() {
      return ((17 * 31 + this.address.hashCode()) * 31 + this.proxy.hashCode()) * 31 + this.inetSocketAddress.hashCode();
   }

   public Proxy proxy() {
      return this.proxy;
   }

   public boolean requiresTunnel() {
      return this.address.sslSocketFactory != null && this.proxy.type() == Type.HTTP;
   }

   public InetSocketAddress socketAddress() {
      return this.inetSocketAddress;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Route{");
      var1.append(this.inetSocketAddress);
      var1.append("}");
      return var1.toString();
   }
}
