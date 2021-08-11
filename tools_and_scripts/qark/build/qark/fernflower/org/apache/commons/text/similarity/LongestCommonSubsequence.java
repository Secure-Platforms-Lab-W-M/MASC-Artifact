package org.apache.commons.text.similarity;

import java.lang.reflect.Array;

public class LongestCommonSubsequence implements SimilarityScore {
   public Integer apply(CharSequence var1, CharSequence var2) {
      if (var1 != null && var2 != null) {
         return this.longestCommonSubsequence(var1, var2).length();
      } else {
         throw new IllegalArgumentException("Inputs must not be null");
      }
   }

   @Deprecated
   public CharSequence logestCommonSubsequence(CharSequence var1, CharSequence var2) {
      return this.longestCommonSubsequence(var1, var2);
   }

   public CharSequence longestCommonSubsequence(CharSequence var1, CharSequence var2) {
      if (var1 != null && var2 != null) {
         StringBuilder var6 = new StringBuilder(Math.max(var1.length(), var2.length()));
         int[][] var7 = this.longestCommonSubstringLengthArray(var1, var2);
         int var4 = var1.length() - 1;
         int var3 = var2.length() - 1;
         int var5 = var7[var1.length()][var2.length()] - 1;

         while(var5 >= 0) {
            if (var1.charAt(var4) == var2.charAt(var3)) {
               var6.append(var1.charAt(var4));
               --var4;
               --var3;
               --var5;
            } else if (var7[var4 + 1][var3] < var7[var4][var3 + 1]) {
               --var4;
            } else {
               --var3;
            }
         }

         return var6.reverse().toString();
      } else {
         throw new IllegalArgumentException("Inputs must not be null");
      }
   }

   public int[][] longestCommonSubstringLengthArray(CharSequence var1, CharSequence var2) {
      int[][] var5 = (int[][])Array.newInstance(Integer.TYPE, new int[]{var1.length() + 1, var2.length() + 1});

      for(int var3 = 0; var3 < var1.length(); ++var3) {
         for(int var4 = 0; var4 < var2.length(); ++var4) {
            if (var3 == 0) {
               var5[var3][var4] = 0;
            }

            if (var4 == 0) {
               var5[var3][var4] = 0;
            }

            if (var1.charAt(var3) == var2.charAt(var4)) {
               var5[var3 + 1][var4 + 1] = var5[var3][var4] + 1;
            } else {
               var5[var3 + 1][var4 + 1] = Math.max(var5[var3 + 1][var4], var5[var3][var4 + 1]);
            }
         }
      }

      return var5;
   }
}
