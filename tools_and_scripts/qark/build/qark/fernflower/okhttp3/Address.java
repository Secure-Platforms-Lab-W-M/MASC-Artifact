package okhttp3;

import java.net.Proxy;
import java.net.ProxySelector;
import java.util.List;
import javax.annotation.Nullable;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSocketFactory;
import okhttp3.internal.Util;

public final class Address {
   @Nullable
   final CertificatePinner certificatePinner;
   final List connectionSpecs;
   final Dns dns;
   @Nullable
   final HostnameVerifier hostnameVerifier;
   final List protocols;
   @Nullable
   final Proxy proxy;
   final Authenticator proxyAuthenticator;
   final ProxySelector proxySelector;
   final SocketFactory socketFactory;
   @Nullable
   final SSLSocketFactory sslSocketFactory;
   final HttpUrl url;

   public Address(String var1, int var2, Dns var3, SocketFactory var4, @Nullable SSLSocketFactory var5, @Nullable HostnameVerifier var6, @Nullable CertificatePinner var7, Authenticator var8, @Nullable Proxy var9, List var10, List var11, ProxySelector var12) {
      HttpUrl.Builder var14 = new HttpUrl.Builder();
      String var13;
      if (var5 != null) {
         var13 = "https";
      } else {
         var13 = "http";
      }

      this.url = var14.scheme(var13).host(var1).port(var2).build();
      if (var3 != null) {
         this.dns = var3;
         if (var4 != null) {
            this.socketFactory = var4;
            if (var8 != null) {
               this.proxyAuthenticator = var8;
               if (var10 != null) {
                  this.protocols = Util.immutableList(var10);
                  if (var11 != null) {
                     this.connectionSpecs = Util.immutableList(var11);
                     if (var12 != null) {
                        this.proxySelector = var12;
                        this.proxy = var9;
                        this.sslSocketFactory = var5;
                        this.hostnameVerifier = var6;
                        this.certificatePinner = var7;
                     } else {
                        throw new NullPointerException("proxySelector == null");
                     }
                  } else {
                     throw new NullPointerException("connectionSpecs == null");
                  }
               } else {
                  throw new NullPointerException("protocols == null");
               }
            } else {
               throw new NullPointerException("proxyAuthenticator == null");
            }
         } else {
            throw new NullPointerException("socketFactory == null");
         }
      } else {
         throw new NullPointerException("dns == null");
      }
   }

   @Nullable
   public CertificatePinner certificatePinner() {
      return this.certificatePinner;
   }

   public List connectionSpecs() {
      return this.connectionSpecs;
   }

   public Dns dns() {
      return this.dns;
   }

   public boolean equals(@Nullable Object var1) {
      return var1 instanceof Address && this.url.equals(((Address)var1).url) && this.equalsNonHost((Address)var1);
   }

   boolean equalsNonHost(Address var1) {
      return this.dns.equals(var1.dns) && this.proxyAuthenticator.equals(var1.proxyAuthenticator) && this.protocols.equals(var1.protocols) && this.connectionSpecs.equals(var1.connectionSpecs) && this.proxySelector.equals(var1.proxySelector) && Util.equal(this.proxy, var1.proxy) && Util.equal(this.sslSocketFactory, var1.sslSocketFactory) && Util.equal(this.hostnameVerifier, var1.hostnameVerifier) && Util.equal(this.certificatePinner, var1.certificatePinner) && this.url().port() == var1.url().port();
   }

   public int hashCode() {
      int var5 = this.url.hashCode();
      int var6 = this.dns.hashCode();
      int var7 = this.proxyAuthenticator.hashCode();
      int var8 = this.protocols.hashCode();
      int var9 = this.connectionSpecs.hashCode();
      int var10 = this.proxySelector.hashCode();
      Proxy var11 = this.proxy;
      int var4 = 0;
      int var1;
      if (var11 != null) {
         var1 = var11.hashCode();
      } else {
         var1 = 0;
      }

      SSLSocketFactory var12 = this.sslSocketFactory;
      int var2;
      if (var12 != null) {
         var2 = var12.hashCode();
      } else {
         var2 = 0;
      }

      HostnameVerifier var13 = this.hostnameVerifier;
      int var3;
      if (var13 != null) {
         var3 = var13.hashCode();
      } else {
         var3 = 0;
      }

      CertificatePinner var14 = this.certificatePinner;
      if (var14 != null) {
         var4 = var14.hashCode();
      }

      return (((((((((17 * 31 + var5) * 31 + var6) * 31 + var7) * 31 + var8) * 31 + var9) * 31 + var10) * 31 + var1) * 31 + var2) * 31 + var3) * 31 + var4;
   }

   @Nullable
   public HostnameVerifier hostnameVerifier() {
      return this.hostnameVerifier;
   }

   public List protocols() {
      return this.protocols;
   }

   @Nullable
   public Proxy proxy() {
      return this.proxy;
   }

   public Authenticator proxyAuthenticator() {
      return this.proxyAuthenticator;
   }

   public ProxySelector proxySelector() {
      return this.proxySelector;
   }

   public SocketFactory socketFactory() {
      return this.socketFactory;
   }

   @Nullable
   public SSLSocketFactory sslSocketFactory() {
      return this.sslSocketFactory;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Address{");
      var1.append(this.url.host());
      var1.append(":");
      var1 = var1.append(this.url.port());
      if (this.proxy != null) {
         var1.append(", proxy=");
         var1.append(this.proxy);
      } else {
         var1.append(", proxySelector=");
         var1.append(this.proxySelector);
      }

      var1.append("}");
      return var1.toString();
   }

   public HttpUrl url() {
      return this.url;
   }
}
