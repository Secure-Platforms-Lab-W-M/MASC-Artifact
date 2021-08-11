package org.apache.commons.text.similarity;

import java.util.Arrays;

public class LevenshteinDistance implements EditDistance {
   private static final LevenshteinDistance DEFAULT_INSTANCE = new LevenshteinDistance();
   private final Integer threshold;

   public LevenshteinDistance() {
      this((Integer)null);
   }

   public LevenshteinDistance(Integer var1) {
      if (var1 != null && var1 < 0) {
         throw new IllegalArgumentException("Threshold must not be negative");
      } else {
         this.threshold = var1;
      }
   }

   public static LevenshteinDistance getDefaultInstance() {
      return DEFAULT_INSTANCE;
   }

   private static int limitedCompare(CharSequence var0, CharSequence var1, int var2) {
      if (var0 != null && var1 != null) {
         if (var2 < 0) {
            throw new IllegalArgumentException("Threshold must not be negative");
         } else {
            int var5 = var0.length();
            int var3 = var1.length();
            int var4 = -1;
            if (var5 == 0) {
               if (var3 <= var2) {
                  var4 = var3;
               }

               return var4;
            } else if (var3 == 0) {
               if (var5 <= var2) {
                  var4 = var5;
               }

               return var4;
            } else {
               CharSequence var9;
               CharSequence var10;
               if (var5 > var3) {
                  var4 = var0.length();
                  var9 = var1;
                  var10 = var0;
               } else {
                  var10 = var1;
                  var9 = var0;
                  var4 = var3;
                  var3 = var5;
               }

               if (var4 - var3 > var2) {
                  return -1;
               } else {
                  int[] var13 = new int[var3 + 1];
                  int[] var12 = new int[var3 + 1];
                  int var6 = Math.min(var3, var2) + 1;

                  for(var5 = 0; var5 < var6; var13[var5] = var5++) {
                  }

                  Arrays.fill(var13, var6, var13.length, Integer.MAX_VALUE);
                  Arrays.fill(var12, Integer.MAX_VALUE);

                  int[] var11;
                  for(var5 = 1; var5 <= var4; var12 = var11) {
                     char var8 = var10.charAt(var5 - 1);
                     var12[0] = var5;
                     int var7 = Math.max(1, var5 - var2);
                     if (var5 > Integer.MAX_VALUE - var2) {
                        var6 = var3;
                     } else {
                        var6 = Math.min(var3, var5 + var2);
                     }

                     if (var7 > 1) {
                        var12[var7 - 1] = Integer.MAX_VALUE;
                     }

                     for(; var7 <= var6; ++var7) {
                        if (var9.charAt(var7 - 1) == var8) {
                           var12[var7] = var13[var7 - 1];
                        } else {
                           var12[var7] = Math.min(Math.min(var12[var7 - 1], var13[var7]), var13[var7 - 1]) + 1;
                        }
                     }

                     var11 = var13;
                     ++var5;
                     var13 = var12;
                  }

                  if (var13[var3] <= var2) {
                     return var13[var3];
                  } else {
                     return -1;
                  }
               }
            }
         }
      } else {
         throw new IllegalArgumentException("CharSequences must not be null");
      }
   }

   private static int unlimitedCompare(CharSequence var0, CharSequence var1) {
      if (var0 != null && var1 != null) {
         int var5 = var0.length();
         int var4 = var1.length();
         if (var5 == 0) {
            return var4;
         } else if (var4 == 0) {
            return var5;
         } else {
            int var3 = var5;
            int var2 = var4;
            CharSequence var11 = var0;
            CharSequence var10 = var1;
            if (var5 > var4) {
               var3 = var4;
               var2 = var0.length();
               var10 = var0;
               var11 = var1;
            }

            int[] var12 = new int[var3 + 1];

            for(var4 = 0; var4 <= var3; var12[var4] = var4++) {
            }

            for(var4 = 1; var4 <= var2; ++var4) {
               int var6 = var12[0];
               char var9 = var10.charAt(var4 - 1);
               var12[0] = var4;

               for(var5 = 1; var5 <= var3; ++var5) {
                  int var8 = var12[var5];
                  byte var7;
                  if (var11.charAt(var5 - 1) == var9) {
                     var7 = 0;
                  } else {
                     var7 = 1;
                  }

                  var12[var5] = Math.min(Math.min(var12[var5 - 1] + 1, var12[var5] + 1), var6 + var7);
                  var6 = var8;
               }
            }

            return var12[var3];
         }
      } else {
         throw new IllegalArgumentException("CharSequences must not be null");
      }
   }

   public Integer apply(CharSequence var1, CharSequence var2) {
      Integer var3 = this.threshold;
      return var3 != null ? limitedCompare(var1, var2, var3) : unlimitedCompare(var1, var2);
   }

   public Integer getThreshold() {
      return this.threshold;
   }
}
