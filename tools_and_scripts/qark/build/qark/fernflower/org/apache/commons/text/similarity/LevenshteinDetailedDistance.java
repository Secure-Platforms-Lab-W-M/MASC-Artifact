package org.apache.commons.text.similarity;

import java.lang.reflect.Array;
import java.util.Arrays;

public class LevenshteinDetailedDistance implements EditDistance {
   private static final LevenshteinDetailedDistance DEFAULT_INSTANCE = new LevenshteinDetailedDistance();
   private final Integer threshold;

   public LevenshteinDetailedDistance() {
      this((Integer)null);
   }

   public LevenshteinDetailedDistance(Integer var1) {
      if (var1 != null && var1 < 0) {
         throw new IllegalArgumentException("Threshold must not be negative");
      } else {
         this.threshold = var1;
      }
   }

   private static LevenshteinResults findDetailedResults(CharSequence var0, CharSequence var1, int[][] var2, boolean var3) {
      int var12 = 0;
      int var11 = 0;
      int var10 = 0;
      int var4 = var1.length();
      int var5 = var0.length();

      while(var4 >= 0 && var5 >= 0) {
         int var14;
         if (var5 == 0) {
            var14 = -1;
         } else {
            var14 = var2[var4][var5 - 1];
         }

         int var6;
         if (var4 == 0) {
            var6 = -1;
         } else {
            var6 = var2[var4 - 1][var5];
         }

         int var16;
         if (var4 > 0 && var5 > 0) {
            var16 = var2[var4 - 1][var5 - 1];
         } else {
            var16 = -1;
         }

         if (var14 == -1 && var6 == -1 && var16 == -1) {
            break;
         }

         int var7 = var2[var4][var5];
         if (var5 > 0 && var4 > 0 && var0.charAt(var5 - 1) == var1.charAt(var4 - 1)) {
            --var5;
            --var4;
         } else {
            boolean var17 = false;
            boolean var18 = false;
            boolean var8;
            boolean var9;
            int var13;
            int var15;
            if ((var7 - 1 != var14 || var7 > var16 || var7 > var6) && (var16 != -1 || var6 != -1)) {
               label104: {
                  if (var7 - 1 != var6 || var7 > var16 || var7 > var14) {
                     var6 = var12;
                     var7 = var11;
                     var13 = var4;
                     var15 = var5;
                     var8 = var17;
                     var9 = var18;
                     if (var16 != -1) {
                        break label104;
                     }

                     var6 = var12;
                     var7 = var11;
                     var13 = var4;
                     var15 = var5;
                     var8 = var17;
                     var9 = var18;
                     if (var14 != -1) {
                        break label104;
                     }
                  }

                  var13 = var4 - 1;
                  if (var3) {
                     var6 = var12 + 1;
                     var8 = true;
                     var7 = var11;
                     var15 = var5;
                     var9 = var18;
                  } else {
                     var7 = var11 + 1;
                     var9 = true;
                     var8 = var17;
                     var15 = var5;
                     var6 = var12;
                  }
               }
            } else {
               var15 = var5 - 1;
               if (var3) {
                  var7 = var11 + 1;
                  var9 = true;
                  var6 = var12;
                  var13 = var4;
                  var8 = var17;
               } else {
                  var6 = var12 + 1;
                  var8 = true;
                  var7 = var11;
                  var13 = var4;
                  var9 = var18;
               }
            }

            var12 = var6;
            var11 = var7;
            var4 = var13;
            var5 = var15;
            if (!var9) {
               var12 = var6;
               var11 = var7;
               var4 = var13;
               var5 = var15;
               if (!var8) {
                  ++var10;
                  var5 = var15 - 1;
                  var4 = var13 - 1;
                  var12 = var6;
                  var11 = var7;
               }
            }
         }
      }

      return new LevenshteinResults(var11 + var12 + var10, var11, var12, var10);
   }

   public static LevenshteinDetailedDistance getDefaultInstance() {
      return DEFAULT_INSTANCE;
   }

   private static LevenshteinResults limitedCompare(CharSequence var0, CharSequence var1, int var2) {
      if (var0 != null && var1 != null) {
         if (var2 < 0) {
            throw new IllegalArgumentException("Threshold must not be negative");
         } else {
            int var3 = var0.length();
            int var4 = var1.length();
            if (var3 == 0) {
               return var4 <= var2 ? new LevenshteinResults(var4, var4, 0, 0) : new LevenshteinResults(-1, 0, 0, 0);
            } else if (var4 == 0) {
               return var3 <= var2 ? new LevenshteinResults(var3, 0, var3, 0) : new LevenshteinResults(-1, 0, 0, 0);
            } else {
               boolean var10 = false;
               CharSequence var11;
               CharSequence var12;
               if (var3 > var4) {
                  var3 = var4;
                  var4 = var0.length();
                  var10 = true;
                  var11 = var1;
                  var12 = var0;
               } else {
                  var12 = var1;
                  var11 = var0;
               }

               int[] var16 = new int[var3 + 1];
               int[] var15 = new int[var3 + 1];
               int[][] var14 = (int[][])Array.newInstance(Integer.TYPE, new int[]{var4 + 1, var3 + 1});

               int var5;
               for(var5 = 0; var5 <= var3; var14[0][var5] = var5++) {
               }

               for(var5 = 0; var5 <= var4; var14[var5][0] = var5++) {
               }

               int var7 = Math.min(var3, var2) + 1;

               for(var5 = 0; var5 < var7; var16[var5] = var5++) {
               }

               Arrays.fill(var16, var7, var16.length, Integer.MAX_VALUE);
               Arrays.fill(var15, Integer.MAX_VALUE);

               int[] var13;
               for(int var6 = 1; var6 <= var4; var15 = var13) {
                  char var9 = var12.charAt(var6 - 1);
                  var15[0] = var6;
                  int var8 = Math.max(1, var6 - var2);
                  if (var6 > Integer.MAX_VALUE - var2) {
                     var7 = var3;
                  } else {
                     var7 = Math.min(var3, var6 + var2);
                  }

                  if (var8 > var7) {
                     return new LevenshteinResults(-1, 0, 0, 0);
                  }

                  if (var8 > 1) {
                     var15[var8 - 1] = Integer.MAX_VALUE;
                  }

                  while(var8 <= var7) {
                     if (var11.charAt(var8 - 1) == var9) {
                        var15[var8] = var16[var8 - 1];
                     } else {
                        var15[var8] = Math.min(Math.min(var15[var8 - 1], var16[var8]), var16[var8 - 1]) + 1;
                     }

                     var14[var6][var8] = var15[var8];
                     ++var8;
                  }

                  var13 = var16;
                  ++var6;
                  var16 = var15;
               }

               if (var16[var3] <= var2) {
                  return findDetailedResults(var11, var12, var14, var10);
               } else {
                  return new LevenshteinResults(-1, 0, 0, 0);
               }
            }
         }
      } else {
         throw new IllegalArgumentException("CharSequences must not be null");
      }
   }

   private static LevenshteinResults unlimitedCompare(CharSequence var0, CharSequence var1) {
      if (var0 != null && var1 != null) {
         int var5 = var0.length();
         int var4 = var1.length();
         if (var5 == 0) {
            return new LevenshteinResults(var4, var4, 0, 0);
         } else if (var4 == 0) {
            return new LevenshteinResults(var5, 0, var5, 0);
         } else {
            boolean var8 = false;
            int var3 = var5;
            int var2 = var4;
            CharSequence var10 = var0;
            CharSequence var9 = var1;
            if (var5 > var4) {
               var3 = var4;
               var2 = var0.length();
               var8 = true;
               var9 = var0;
               var10 = var1;
            }

            int[] var14 = new int[var3 + 1];
            int[] var13 = new int[var3 + 1];
            int[][] var12 = (int[][])Array.newInstance(Integer.TYPE, new int[]{var2 + 1, var3 + 1});

            for(var4 = 0; var4 <= var3; var12[0][var4] = var4++) {
            }

            for(var4 = 0; var4 <= var2; var12[var4][0] = var4++) {
            }

            for(var4 = 0; var4 <= var3; var14[var4] = var4++) {
            }

            int[] var11;
            for(var4 = 1; var4 <= var2; var13 = var11) {
               char var7 = var9.charAt(var4 - 1);
               var13[0] = var4;

               for(var5 = 1; var5 <= var3; ++var5) {
                  byte var6;
                  if (var10.charAt(var5 - 1) == var7) {
                     var6 = 0;
                  } else {
                     var6 = 1;
                  }

                  var13[var5] = Math.min(Math.min(var13[var5 - 1] + 1, var14[var5] + 1), var14[var5 - 1] + var6);
                  var12[var4][var5] = var13[var5];
               }

               var11 = var14;
               ++var4;
               var14 = var13;
            }

            return findDetailedResults(var10, var9, var12, var8);
         }
      } else {
         throw new IllegalArgumentException("CharSequences must not be null");
      }
   }

   public LevenshteinResults apply(CharSequence var1, CharSequence var2) {
      Integer var3 = this.threshold;
      return var3 != null ? limitedCompare(var1, var2, var3) : unlimitedCompare(var1, var2);
   }

   public Integer getThreshold() {
      return this.threshold;
   }
}
