package org.apache.http.client.utils;

import java.util.StringTokenizer;

@Deprecated
public class Rfc3492Idn implements Idn {
   private static final String ACE_PREFIX = "xn--";
   private static final int base = 36;
   private static final int damp = 700;
   private static final char delimiter = '-';
   private static final int initial_bias = 72;
   private static final int initial_n = 128;
   private static final int skew = 38;
   private static final int tmax = 26;
   private static final int tmin = 1;

   private int adapt(int var1, int var2, boolean var3) {
      if (var3) {
         var1 /= 700;
      } else {
         var1 /= 2;
      }

      var2 = var1 + var1 / var2;

      for(var1 = 0; var2 > 455; var1 += 36) {
         var2 /= 35;
      }

      return var2 * 36 / (var2 + 38) + var1;
   }

   private int digit(char var1) {
      if (var1 >= 'A' && var1 <= 'Z') {
         return var1 - 65;
      } else if (var1 >= 'a' && var1 <= 'z') {
         return var1 - 97;
      } else if (var1 >= '0' && var1 <= '9') {
         return var1 - 48 + 26;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("illegal digit: ");
         var2.append(var1);
         throw new IllegalArgumentException(var2.toString());
      }
   }

   protected String decode(String var1) {
      String var12 = var1;
      short var4 = 128;
      byte var7 = 0;
      byte var8 = 72;
      StringBuilder var13 = new StringBuilder(var1.length());
      int var9 = var1.lastIndexOf(45);
      var1 = var1;
      int var5 = var4;
      int var3 = var7;
      int var6 = var8;
      if (var9 != -1) {
         var13.append(var12.subSequence(0, var9));
         var1 = var12.substring(var9 + 1);
         var6 = var8;
         var3 = var7;
         var5 = var4;
      }

      while(!var1.isEmpty()) {
         int var16 = 1;
         int var15 = 36;

         int var14;
         for(var14 = var3; !var1.isEmpty(); var14 = var9) {
            char var2 = var1.charAt(0);
            var1 = var1.substring(1);
            int var10 = this.digit(var2);
            var9 = var14 + var10 * var16;
            if (var15 <= var6 + 1) {
               var14 = 1;
            } else if (var15 >= var6 + 26) {
               var14 = 26;
            } else {
               var14 = var15 - var6;
            }

            if (var10 < var14) {
               var14 = var9;
               break;
            }

            var16 *= 36 - var14;
            var15 += 36;
         }

         var6 = var13.length();
         boolean var11;
         if (var3 == 0) {
            var11 = true;
         } else {
            var11 = false;
         }

         var6 = this.adapt(var14 - var3, var6 + 1, var11);
         var5 += var14 / (var13.length() + 1);
         var3 = var14 % (var13.length() + 1);
         var13.insert(var3, (char)var5);
         ++var3;
      }

      return var13.toString();
   }

   public String toUnicode(String var1) {
      StringBuilder var3 = new StringBuilder(var1.length());

      for(StringTokenizer var4 = new StringTokenizer(var1, "."); var4.hasMoreTokens(); var3.append(var1)) {
         String var2 = var4.nextToken();
         if (var3.length() > 0) {
            var3.append('.');
         }

         var1 = var2;
         if (var2.startsWith("xn--")) {
            var1 = this.decode(var2.substring(4));
         }
      }

      return var3.toString();
   }
}
