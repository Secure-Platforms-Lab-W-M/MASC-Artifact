package org.apache.commons.codec.digest;

import java.security.SecureRandom;
import java.util.Random;

class B64 {
   static final char[] B64T_ARRAY = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
   static final String B64T_STRING = "./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

   static void b64from24bit(byte var0, byte var1, byte var2, int var3, StringBuilder var4) {
      int var6 = var0 << 16 & 16777215 | var1 << 8 & '\uffff' | var2 & 255;

      for(int var5 = var3; var5 > 0; --var5) {
         var4.append(B64T_ARRAY[var6 & 63]);
         var6 >>= 6;
      }

   }

   static String getRandomSalt(int var0) {
      return getRandomSalt(var0, new SecureRandom());
   }

   static String getRandomSalt(int var0, Random var1) {
      StringBuilder var3 = new StringBuilder(var0);

      for(int var2 = 1; var2 <= var0; ++var2) {
         var3.append("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(var1.nextInt("./0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length())));
      }

      return var3.toString();
   }
}
