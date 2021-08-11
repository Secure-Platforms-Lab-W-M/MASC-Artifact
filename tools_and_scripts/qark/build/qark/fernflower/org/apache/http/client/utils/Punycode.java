package org.apache.http.client.utils;

@Deprecated
public class Punycode {
   private static final Idn impl;

   static {
      Object var0;
      try {
         var0 = new JdkIdn();
      } catch (Exception var1) {
         var0 = new Rfc3492Idn();
      }

      impl = (Idn)var0;
   }

   public static String toUnicode(String var0) {
      return impl.toUnicode(var0);
   }
}
