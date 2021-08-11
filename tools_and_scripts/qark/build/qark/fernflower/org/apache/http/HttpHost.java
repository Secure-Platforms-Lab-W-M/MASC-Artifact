package org.apache.http;

import java.io.Serializable;
import java.net.InetAddress;
import java.util.Locale;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

public final class HttpHost implements Cloneable, Serializable {
   public static final String DEFAULT_SCHEME_NAME = "http";
   private static final long serialVersionUID = -7529410654042457626L;
   protected final InetAddress address;
   protected final String hostname;
   protected final String lcHostname;
   protected final int port;
   protected final String schemeName;

   public HttpHost(String var1) {
      this((String)var1, -1, (String)null);
   }

   public HttpHost(String var1, int var2) {
      this((String)var1, var2, (String)null);
   }

   public HttpHost(String var1, int var2, String var3) {
      this.hostname = (String)Args.containsNoBlanks(var1, "Host name");
      this.lcHostname = var1.toLowerCase(Locale.ROOT);
      if (var3 != null) {
         this.schemeName = var3.toLowerCase(Locale.ROOT);
      } else {
         this.schemeName = "http";
      }

      this.port = var2;
      this.address = null;
   }

   public HttpHost(InetAddress var1) {
      this((InetAddress)var1, -1, (String)null);
   }

   public HttpHost(InetAddress var1, int var2) {
      this((InetAddress)var1, var2, (String)null);
   }

   public HttpHost(InetAddress var1, int var2, String var3) {
      this((InetAddress)Args.notNull(var1, "Inet address"), var1.getHostName(), var2, var3);
   }

   public HttpHost(InetAddress var1, String var2, int var3, String var4) {
      this.address = (InetAddress)Args.notNull(var1, "Inet address");
      String var5 = (String)Args.notNull(var2, "Hostname");
      this.hostname = var5;
      this.lcHostname = var5.toLowerCase(Locale.ROOT);
      if (var4 != null) {
         this.schemeName = var4.toLowerCase(Locale.ROOT);
      } else {
         this.schemeName = "http";
      }

      this.port = var3;
   }

   public HttpHost(HttpHost var1) {
      Args.notNull(var1, "HTTP host");
      this.hostname = var1.hostname;
      this.lcHostname = var1.lcHostname;
      this.schemeName = var1.schemeName;
      this.port = var1.port;
      this.address = var1.address;
   }

   public static HttpHost create(String var0) {
      Args.containsNoBlanks(var0, "HTTP Host");
      String var4 = var0;
      String var3 = null;
      int var1 = var0.indexOf("://");
      var0 = var0;
      if (var1 > 0) {
         var3 = var4.substring(0, var1);
         var0 = var4.substring(var1 + 3);
      }

      var1 = -1;
      int var2 = var0.lastIndexOf(":");
      var4 = var0;
      if (var2 > 0) {
         try {
            var1 = Integer.parseInt(var0.substring(var2 + 1));
         } catch (NumberFormatException var5) {
            StringBuilder var6 = new StringBuilder();
            var6.append("Invalid HTTP host: ");
            var6.append(var0);
            throw new IllegalArgumentException(var6.toString());
         }

         var4 = var0.substring(0, var2);
      }

      return new HttpHost(var4, var1, var3);
   }

   public Object clone() throws CloneNotSupportedException {
      return super.clone();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof HttpHost)) {
         return false;
      } else {
         HttpHost var3 = (HttpHost)var1;
         if (this.lcHostname.equals(var3.lcHostname) && this.port == var3.port && this.schemeName.equals(var3.schemeName)) {
            InetAddress var2 = this.address;
            if (var2 == null) {
               if (var3.address == null) {
                  return true;
               }
            } else if (var2.equals(var3.address)) {
               return true;
            }
         }

         return false;
      }
   }

   public InetAddress getAddress() {
      return this.address;
   }

   public String getHostName() {
      return this.hostname;
   }

   public int getPort() {
      return this.port;
   }

   public String getSchemeName() {
      return this.schemeName;
   }

   public int hashCode() {
      int var2 = LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(17, this.lcHostname), this.port), this.schemeName);
      InetAddress var3 = this.address;
      int var1 = var2;
      if (var3 != null) {
         var1 = LangUtils.hashCode(var2, var3);
      }

      return var1;
   }

   public String toHostString() {
      if (this.port != -1) {
         StringBuilder var1 = new StringBuilder(this.hostname.length() + 6);
         var1.append(this.hostname);
         var1.append(":");
         var1.append(Integer.toString(this.port));
         return var1.toString();
      } else {
         return this.hostname;
      }
   }

   public String toString() {
      return this.toURI();
   }

   public String toURI() {
      StringBuilder var1 = new StringBuilder();
      var1.append(this.schemeName);
      var1.append("://");
      var1.append(this.hostname);
      if (this.port != -1) {
         var1.append(':');
         var1.append(Integer.toString(this.port));
      }

      return var1.toString();
   }
}
