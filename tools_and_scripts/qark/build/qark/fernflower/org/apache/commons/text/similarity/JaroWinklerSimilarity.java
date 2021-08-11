package org.apache.commons.text.similarity;

import java.util.Arrays;
import org.apache.commons.lang3.StringUtils;

public class JaroWinklerSimilarity implements SimilarityScore {
   protected static int[] matches(CharSequence var0, CharSequence var1) {
      CharSequence var9;
      CharSequence var10;
      if (var0.length() > var1.length()) {
         var9 = var0;
         var10 = var1;
      } else {
         var9 = var1;
         var10 = var0;
      }

      int var6 = Math.max(var9.length() / 2 - 1, 0);
      int[] var11 = new int[var10.length()];
      Arrays.fill(var11, -1);
      boolean[] var12 = new boolean[var9.length()];
      int var2 = 0;

      int var3;
      int var4;
      int var5;
      for(var3 = 0; var3 < var10.length(); var2 = var4) {
         char var7 = var10.charAt(var3);
         var5 = Math.max(var3 - var6, 0);
         int var8 = Math.min(var3 + var6 + 1, var9.length());

         while(true) {
            var4 = var2;
            if (var5 >= var8) {
               break;
            }

            if (!var12[var5] && var7 == var9.charAt(var5)) {
               var11[var3] = var5;
               var12[var5] = true;
               var4 = var2 + 1;
               break;
            }

            ++var5;
         }

         ++var3;
      }

      char[] var13 = new char[var2];
      char[] var14 = new char[var2];
      var3 = 0;

      for(var4 = 0; var3 < var10.length(); var4 = var5) {
         var5 = var4;
         if (var11[var3] != -1) {
            var13[var4] = var10.charAt(var3);
            var5 = var4 + 1;
         }

         ++var3;
      }

      var3 = 0;

      for(var4 = 0; var3 < var9.length(); var4 = var5) {
         var5 = var4;
         if (var12[var3]) {
            var14[var4] = var9.charAt(var3);
            var5 = var4 + 1;
         }

         ++var3;
      }

      var3 = 0;

      for(var4 = 0; var4 < var13.length; var3 = var5) {
         var5 = var3;
         if (var13[var4] != var14[var4]) {
            var5 = var3 + 1;
         }

         ++var4;
      }

      var5 = 0;

      for(var4 = 0; var4 < Math.min(4, var10.length()) && var0.charAt(var4) == var1.charAt(var4); ++var4) {
         ++var5;
      }

      return new int[]{var2, var3, var5};
   }

   public Double apply(CharSequence var1, CharSequence var2) {
      if (var1 != null && var2 != null) {
         if (StringUtils.equals(var1, var2)) {
            return 1.0D;
         } else {
            int[] var5 = matches(var1, var2);
            double var3 = (double)var5[0];
            if (var3 == 0.0D) {
               return 0.0D;
            } else {
               var3 = (var3 / (double)var1.length() + var3 / (double)var2.length() + (var3 - (double)var5[1] / 2.0D) / var3) / 3.0D;
               if (var3 >= 0.7D) {
                  var3 += (double)var5[2] * 0.1D * (1.0D - var3);
               }

               return var3;
            }
         }
      } else {
         throw new IllegalArgumentException("CharSequences must not be null");
      }
   }
}
