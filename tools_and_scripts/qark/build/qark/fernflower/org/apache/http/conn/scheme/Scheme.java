package org.apache.http.conn.scheme;

import java.util.Locale;
import org.apache.http.util.Args;
import org.apache.http.util.LangUtils;

@Deprecated
public final class Scheme {
   private final int defaultPort;
   private final boolean layered;
   private final String name;
   private final SchemeSocketFactory socketFactory;
   private String stringRep;

   public Scheme(String var1, int var2, SchemeSocketFactory var3) {
      Args.notNull(var1, "Scheme name");
      boolean var4;
      if (var2 > 0 && var2 <= 65535) {
         var4 = true;
      } else {
         var4 = false;
      }

      Args.check(var4, "Port is invalid");
      Args.notNull(var3, "Socket factory");
      this.name = var1.toLowerCase(Locale.ENGLISH);
      this.defaultPort = var2;
      if (var3 instanceof SchemeLayeredSocketFactory) {
         this.layered = true;
         this.socketFactory = var3;
      } else if (var3 instanceof LayeredSchemeSocketFactory) {
         this.layered = true;
         this.socketFactory = new SchemeLayeredSocketFactoryAdaptor2((LayeredSchemeSocketFactory)var3);
      } else {
         this.layered = false;
         this.socketFactory = var3;
      }
   }

   @Deprecated
   public Scheme(String var1, SocketFactory var2, int var3) {
      Args.notNull(var1, "Scheme name");
      Args.notNull(var2, "Socket factory");
      boolean var4;
      if (var3 > 0 && var3 <= 65535) {
         var4 = true;
      } else {
         var4 = false;
      }

      Args.check(var4, "Port is invalid");
      this.name = var1.toLowerCase(Locale.ENGLISH);
      if (var2 instanceof LayeredSocketFactory) {
         this.socketFactory = new SchemeLayeredSocketFactoryAdaptor((LayeredSocketFactory)var2);
         this.layered = true;
      } else {
         this.socketFactory = new SchemeSocketFactoryAdaptor(var2);
         this.layered = false;
      }

      this.defaultPort = var3;
   }

   public final boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (var1 instanceof Scheme) {
         Scheme var2 = (Scheme)var1;
         return this.name.equals(var2.name) && this.defaultPort == var2.defaultPort && this.layered == var2.layered;
      } else {
         return false;
      }
   }

   public final int getDefaultPort() {
      return this.defaultPort;
   }

   public final String getName() {
      return this.name;
   }

   public final SchemeSocketFactory getSchemeSocketFactory() {
      return this.socketFactory;
   }

   @Deprecated
   public final SocketFactory getSocketFactory() {
      SchemeSocketFactory var1 = this.socketFactory;
      if (var1 instanceof SchemeSocketFactoryAdaptor) {
         return ((SchemeSocketFactoryAdaptor)var1).getFactory();
      } else {
         return (SocketFactory)(this.layered ? new LayeredSocketFactoryAdaptor((LayeredSchemeSocketFactory)var1) : new SocketFactoryAdaptor(var1));
      }
   }

   public int hashCode() {
      return LangUtils.hashCode(LangUtils.hashCode(LangUtils.hashCode(17, this.defaultPort), this.name), this.layered);
   }

   public final boolean isLayered() {
      return this.layered;
   }

   public final int resolvePort(int var1) {
      return var1 <= 0 ? this.defaultPort : var1;
   }

   public final String toString() {
      if (this.stringRep == null) {
         StringBuilder var1 = new StringBuilder();
         var1.append(this.name);
         var1.append(':');
         var1.append(Integer.toString(this.defaultPort));
         this.stringRep = var1.toString();
      }

      return this.stringRep;
   }
}
