package androidx.core.util;

import java.io.PrintWriter;

public final class TimeUtils {
   public static final int HUNDRED_DAY_FIELD_LEN = 19;
   private static final int SECONDS_PER_DAY = 86400;
   private static final int SECONDS_PER_HOUR = 3600;
   private static final int SECONDS_PER_MINUTE = 60;
   private static char[] sFormatStr = new char[24];
   private static final Object sFormatSync = new Object();

   private TimeUtils() {
   }

   private static int accumField(int var0, int var1, boolean var2, int var3) {
      if (var0 > 99 || var2 && var3 >= 3) {
         return var1 + 3;
      } else if (var0 > 9 || var2 && var3 >= 2) {
         return var1 + 2;
      } else {
         return !var2 && var0 <= 0 ? 0 : var1 + 1;
      }
   }

   public static void formatDuration(long var0, long var2, PrintWriter var4) {
      if (var0 == 0L) {
         var4.print("--");
      } else {
         formatDuration(var0 - var2, var4, 0);
      }
   }

   public static void formatDuration(long var0, PrintWriter var2) {
      formatDuration(var0, var2, 0);
   }

   public static void formatDuration(long param0, PrintWriter param2, int param3) {
      // $FF: Couldn't be decompiled
   }

   public static void formatDuration(long param0, StringBuilder param2) {
      // $FF: Couldn't be decompiled
   }

   private static int formatDurationLocked(long var0, int var2) {
      if (sFormatStr.length < var2) {
         sFormatStr = new char[var2];
      }

      char[] var16 = sFormatStr;
      if (var0 == 0L) {
         while(var2 - 1 < 0) {
            var16[0] = ' ';
         }

         var16[0] = '0';
         return 0 + 1;
      } else {
         byte var3;
         if (var0 > 0L) {
            var3 = 43;
         } else {
            var0 = -var0;
            var3 = 45;
         }

         int var13 = (int)(var0 % 1000L);
         int var4 = (int)Math.floor((double)(var0 / 1000L));
         int var5;
         if (var4 > 86400) {
            var5 = var4 / 86400;
            var4 -= 86400 * var5;
         } else {
            var5 = 0;
         }

         int var7;
         if (var4 > 3600) {
            var7 = var4 / 3600;
            var4 -= var7 * 3600;
         } else {
            var7 = 0;
         }

         int var6;
         int var8;
         if (var4 > 60) {
            var8 = var4 / 60;
            var6 = var4 - var8 * 60;
         } else {
            var8 = 0;
            var6 = var4;
         }

         int var10 = 0;
         byte var12 = 0;
         byte var11 = 3;
         boolean var14 = false;
         byte var19;
         if (var2 != 0) {
            var4 = accumField(var5, 1, false, 0);
            if (var4 > 0) {
               var14 = true;
            }

            var4 += accumField(var7, 1, var14, 2);
            if (var4 > 0) {
               var14 = true;
            } else {
               var14 = false;
            }

            var4 += accumField(var8, 1, var14, 2);
            if (var4 > 0) {
               var14 = true;
            } else {
               var14 = false;
            }

            int var9 = var4 + accumField(var6, 1, var14, 2);
            if (var9 > 0) {
               var19 = 3;
            } else {
               var19 = 0;
            }

            var9 += accumField(var13, 2, true, var19) + 1;
            var4 = var12;

            while(true) {
               var10 = var4;
               if (var9 >= var2) {
                  break;
               }

               var16[var4] = ' ';
               ++var4;
               ++var9;
            }
         }

         var16[var10] = (char)var3;
         ++var10;
         boolean var17;
         if (var2 != 0) {
            var17 = true;
         } else {
            var17 = false;
         }

         boolean var15 = true;
         byte var20 = 2;
         var5 = printField(var16, var5, 'd', var10, false, 0);
         if (var5 != var10) {
            var14 = true;
         } else {
            var14 = false;
         }

         if (var17) {
            var19 = 2;
         } else {
            var19 = 0;
         }

         var5 = printField(var16, var7, 'h', var5, var14, var19);
         if (var5 != var10) {
            var14 = true;
         } else {
            var14 = false;
         }

         if (var17) {
            var19 = 2;
         } else {
            var19 = 0;
         }

         var5 = printField(var16, var8, 'm', var5, var14, var19);
         if (var5 != var10) {
            var14 = var15;
         } else {
            var14 = false;
         }

         if (var17) {
            var19 = var20;
         } else {
            var19 = 0;
         }

         var4 = printField(var16, var6, 's', var5, var14, var19);
         byte var18;
         if (var17 && var4 != var10) {
            var18 = var11;
         } else {
            var18 = 0;
         }

         var2 = printField(var16, var13, 'm', var4, true, var18);
         var16[var2] = 's';
         return var2 + 1;
      }
   }

   private static int printField(char[] var0, int var1, char var2, int var3, boolean var4, int var5) {
      int var6;
      if (!var4) {
         var6 = var3;
         if (var1 <= 0) {
            return var6;
         }
      }

      int var7;
      label40: {
         if (!var4 || var5 < 3) {
            var6 = var1;
            var7 = var3;
            if (var1 <= 99) {
               break label40;
            }
         }

         var6 = var1 / 100;
         var0[var3] = (char)(var6 + 48);
         var7 = var3 + 1;
         var6 = var1 - var6 * 100;
      }

      label41: {
         if ((!var4 || var5 < 2) && var6 <= 9) {
            var5 = var6;
            var1 = var7;
            if (var3 == var7) {
               break label41;
            }
         }

         var3 = var6 / 10;
         var0[var7] = (char)(var3 + 48);
         var1 = var7 + 1;
         var5 = var6 - var3 * 10;
      }

      var0[var1] = (char)(var5 + 48);
      ++var1;
      var0[var1] = var2;
      var6 = var1 + 1;
      return var6;
   }
}
