package gnu.java.zrtp.utils;

public class Base32 {
   private static final char[] chars = "ybndrfg8ejkmcpqxot1uwisza345h769".toCharArray();
   private static int[] revchars = new int[]{255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 18, 255, 25, 26, 27, 30, 29, 7, 31, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 24, 1, 12, 3, 8, 5, 6, 28, 21, 9, 10, 255, 11, 2, 16, 13, 14, 4, 22, 17, 19, 255, 20, 15, 0, 23, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255, 255};

   public static byte[] ascii2binary(String var0, int var1) {
      int var3 = 0;
      int var2 = divceil(var1, 5);
      int var26 = divceil(var2 * 5, 8);
      byte[] var27 = new byte[var26];
      int var4 = var27.length;
      int var7 = var2 % 8;

      do {
         int var16 = var3;
         int var17 = var2;
         int var18 = var3;
         int var5 = var4;
         int var12 = var2;
         int var19 = var3;
         int var8 = var4;
         int var20 = var2;
         int var21 = var3;
         int var9 = var4;
         int var13 = var2;
         int var22 = var3;
         int var6 = var4;
         int var14 = var2;
         int var23 = var3;
         int var10 = var4;
         int var24 = var2;
         int var25 = var3;
         int var11 = var4;
         int var15 = var2;
         int[] var28;
         switch(var7) {
         case 0:
            var28 = revchars;
            var17 = var2 - 1;
            var16 = var28[var0.charAt(var17) & 255];
         case 7:
            var28 = revchars;
            var12 = var17 - 1;
            var2 = var16 | var28[var0.charAt(var12) & 255] << 5;
            var5 = var4 - 1;
            var27[var5] = (byte)(var2 % 256);
            var18 = var2 / 256;
         case 6:
            var28 = revchars;
            var20 = var12 - 1;
            var19 = var18 | var28[var0.charAt(var20) & 255] << 2;
            var8 = var5;
         case 5:
            var28 = revchars;
            var13 = var20 - 1;
            var2 = var19 | var28[var0.charAt(var13) & 255] << 7;
            var9 = var8 - 1;
            var27[var9] = (byte)(var2 % 256);
            var21 = var2 / 256;
         case 4:
            var28 = revchars;
            var14 = var13 - 1;
            var2 = var21 | var28[var0.charAt(var14) & 255] << 4;
            var6 = var9 - 1;
            var27[var6] = (byte)(var2 % 256);
            var22 = var2 / 256;
         case 3:
            var28 = revchars;
            var24 = var14 - 1;
            var23 = var22 | var28[var0.charAt(var24) & 255] << 1;
            var10 = var6;
         case 2:
            var28 = revchars;
            var15 = var24 - 1;
            var2 = var23 | var28[var0.charAt(var15) & 255] << 6;
            var11 = var10 - 1;
            var27[var11] = (byte)(var2 % 256);
            var25 = var2 / 256;
         case 1:
            var28 = revchars;
            var2 = var15 - 1;
            var3 = var25 | var28[var0.charAt(var2) & 255] << 3;
            var4 = var11 - 1;
            var27[var4] = (byte)(var3 % 256);
         }

         var7 = 0;
      } while(var2 > 0);

      var1 = divceil(var1, 8);
      if (var1 < var26) {
         byte[] var29 = new byte[var1];
         System.arraycopy(var27, 0, var29, 0, var29.length);
         return var29;
      } else {
         return var27;
      }
   }

   public static String binary2ascii(byte[] var0, int var1) {
      int var4 = (var1 + 7) / 8;
      char[] var9 = new char[divceil(var4 * 8, 5)];

      int var2;
      for(var2 = 0; var2 < var9.length; ++var2) {
         var9[var2] = ' ';
      }

      var2 = var9.length;
      int var3 = 0;
      int var5 = var4 % 5;

      do {
         label37: {
            int var6;
            int var7;
            int var8;
            char[] var10;
            label36: {
               label35: {
                  label34: {
                     if (var5 != 0) {
                        var8 = var4;
                        var6 = var2;
                        var7 = var3;
                        if (var5 == 1) {
                           break label36;
                        }

                        var8 = var4;
                        var6 = var2;
                        var7 = var3;
                        if (var5 == 2) {
                           break label35;
                        }

                        var8 = var4;
                        var6 = var2;
                        var7 = var3;
                        if (var5 == 3) {
                           break label34;
                        }

                        var8 = var4;
                        var6 = var2;
                        var7 = var3;
                        if (var5 != 4) {
                           break label37;
                        }
                     } else {
                        var8 = var4 - 1;
                        var3 = var0[var8] & 255;
                        var6 = var2 - 1;
                        var9[var6] = chars[var3 % 32];
                        var7 = var3 / 32;
                     }

                     --var8;
                     var2 = var7 | (var0[var8] & 255) << 3;
                     var3 = var6 - 1;
                     var10 = chars;
                     var9[var3] = var10[var2 % 32];
                     var2 /= 32;
                     var6 = var3 - 1;
                     var9[var6] = var10[var2 % 32];
                     var7 = var2 / 32;
                  }

                  --var8;
                  var2 = var7 | (var0[var8] & 255) << 1;
                  --var6;
                  var9[var6] = chars[var2 % 32];
                  var7 = var2 / 32;
               }

               --var8;
               var2 = var7 | (var0[var8] & 255) << 4;
               var3 = var6 - 1;
               var10 = chars;
               var9[var3] = var10[var2 % 32];
               var2 /= 32;
               var6 = var3 - 1;
               var9[var6] = var10[var2 % 32];
               var7 = var2 / 32;
            }

            var4 = var8 - 1;
            var2 = var7 | (var0[var4] & 255) << 2;
            var5 = var6 - 1;
            var10 = chars;
            var9[var5] = var10[var2 % 32];
            var3 = var2 / 32;
            var2 = var5 - 1;
            var9[var2] = var10[var3];
         }

         var5 = 0;
      } while(var4 > 0);

      return new String(var9, 0, divceil(var1, 5));
   }

   public static int divceil(int var0, int var1) {
      if (var0 > 0) {
         if (var1 > 0) {
            var0 = var0 + var1 - 1;
         }
      } else if (var1 <= 0) {
         var0 = var0 + var1 + 1;
      }

      return var0 / var1;
   }
}
