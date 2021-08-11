package org.apache.http.auth;

import java.util.Locale;
import org.apache.http.HttpHost;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

public class AuthScope {
   public static final AuthScope ANY;
   public static final String ANY_HOST = null;
   public static final int ANY_PORT = -1;
   public static final String ANY_REALM = null;
   public static final String ANY_SCHEME = null;
   private final String host;
   private final HttpHost origin;
   private final int port;
   private final String realm;
   private final String scheme;

   static {
      ANY = new AuthScope(ANY_HOST, -1, ANY_REALM, ANY_SCHEME);
   }

   public AuthScope(String var1, int var2) {
      this(var1, var2, ANY_REALM, ANY_SCHEME);
   }

   public AuthScope(String var1, int var2, String var3) {
      this(var1, var2, var3, ANY_SCHEME);
   }

   public AuthScope(String var1, int var2, String var3, String var4) {
      if (var1 == null) {
         var1 = ANY_HOST;
      } else {
         var1 = var1.toLowerCase(Locale.ROOT);
      }

      this.host = var1;
      if (var2 < 0) {
         var2 = -1;
      }

      this.port = var2;
      if (var3 == null) {
         var3 = ANY_REALM;
      }

      this.realm = var3;
      if (var4 == null) {
         var1 = ANY_SCHEME;
      } else {
         var1 = var4.toUpperCase(Locale.ROOT);
      }

      this.scheme = var1;
      this.origin = null;
   }

   public AuthScope(HttpHost var1) {
      this(var1, ANY_REALM, ANY_SCHEME);
   }

   public AuthScope(HttpHost var1, String var2, String var3) {
      Args.notNull(var1, "Host");
      this.host = var1.getHostName().toLowerCase(Locale.ROOT);
      int var4;
      if (var1.getPort() < 0) {
         var4 = -1;
      } else {
         var4 = var1.getPort();
      }

      this.port = var4;
      if (var2 == null) {
         var2 = ANY_REALM;
      }

      this.realm = var2;
      if (var3 == null) {
         var2 = ANY_SCHEME;
      } else {
         var2 = var3.toUpperCase(Locale.ROOT);
      }

      this.scheme = var2;
      this.origin = var1;
   }

   public AuthScope(AuthScope var1) {
      Args.notNull(var1, "Scope");
      this.host = var1.getHost();
      this.port = var1.getPort();
      this.realm = var1.getRealm();
      this.scheme = var1.getScheme();
      this.origin = var1.getOrigin();
   }

   public boolean equals(Object var1) {
      boolean var3 = false;
      if (var1 == null) {
         return false;
      } else if (var1 == this) {
         return true;
      } else if (!(var1 instanceof AuthScope)) {
         return super.equals(var1);
      } else {
         AuthScope var4 = (AuthScope)var1;
         boolean var2 = var3;
         if (LangUtils.equals(this.host, var4.host)) {
            var2 = var3;
            if (this.port == var4.port) {
               var2 = var3;
               if (LangUtils.equals(this.realm, var4.realm)) {
                  var2 = var3;
                  if (LangUtils.equals(this.scheme, var4.scheme)) {
                     var2 = true;
                  }
               }
            }
         }

         return var2;
      }
   }

   public String getHost() {
      return this.host;
   }

   public HttpHost getOrigin() {
      return this.origin;
   }

   public int getPort() {
      return this.port;
   }

   public String getRealm() {
      return this.realm;
   }

   public String getScheme() {
      return this.scheme;
   }

   public int hashCode() {
      return LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(17, this.host), this.port), this.realm), this.scheme);
   }

   public int match(AuthScope var1) {
      byte var2 = 0;
      int var3;
      String var6;
      String var7;
      if (LangUtils.equals(this.scheme, var1.scheme)) {
         var3 = 0 + 1;
      } else {
         var6 = this.scheme;
         var7 = ANY_SCHEME;
         var3 = var2;
         if (var6 != var7) {
            var3 = var2;
            if (var1.scheme != var7) {
               return -1;
            }
         }
      }

      int var8;
      if (LangUtils.equals(this.realm, var1.realm)) {
         var8 = var3 + 2;
      } else {
         var6 = this.realm;
         var7 = ANY_REALM;
         var8 = var3;
         if (var6 != var7) {
            var8 = var3;
            if (var1.realm != var7) {
               return -1;
            }
         }
      }

      int var4 = this.port;
      int var5 = var1.port;
      if (var4 == var5) {
         var3 = var8 + 4;
      } else {
         var3 = var8;
         if (var4 != -1) {
            var3 = var8;
            if (var5 != -1) {
               return -1;
            }
         }
      }

      if (LangUtils.equals(this.host, var1.host)) {
         return var3 + 8;
      } else {
         var6 = this.host;
         var7 = ANY_HOST;
         return var6 != var7 && var1.host != var7 ? -1 : var3;
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      String var2 = this.scheme;
      if (var2 != null) {
         var1.append(var2.toUpperCase(Locale.ROOT));
         var1.append(' ');
      }

      if (this.realm != null) {
         var1.append('\'');
         var1.append(this.realm);
         var1.append('\'');
      } else {
         var1.append("<any realm>");
      }

      if (this.host != null) {
         var1.append('@');
         var1.append(this.host);
         if (this.port >= 0) {
            var1.append(':');
            var1.append(this.port);
         }
      }

      return var1.toString();
   }
}
