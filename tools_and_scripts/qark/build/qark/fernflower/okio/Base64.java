package okio;

import java.io.UnsupportedEncodingException;

final class Base64 {
   private static final byte[] MAP = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
   private static final byte[] URL_MAP = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};

   private Base64() {
   }

   public static byte[] decode(String var0) {
      int var3;
      for(var3 = var0.length(); var3 > 0; --var3) {
         char var1 = var0.charAt(var3 - 1);
         if (var1 != '=' && var1 != '\n' && var1 != '\r' && var1 != ' ' && var1 != '\t') {
            break;
         }
      }

      byte[] var10 = new byte[(int)((long)var3 * 6L / 8L)];
      int var2 = 0;
      int var6 = 0;
      int var5 = 0;

      int var4;
      int var7;
      int var12;
      for(var4 = 0; var4 < var3; var5 = var7) {
         int var8;
         label109: {
            char var9 = var0.charAt(var4);
            if (var9 >= 'A' && var9 <= 'Z') {
               var12 = var9 - 65;
            } else if (var9 >= 'a' && var9 <= 'z') {
               var12 = var9 - 71;
            } else if (var9 >= '0' && var9 <= '9') {
               var12 = var9 + 4;
            } else if (var9 != '+' && var9 != '-') {
               if (var9 != '/' && var9 != '_') {
                  var8 = var2;
                  var12 = var6;
                  var7 = var5;
                  if (var9 != '\n') {
                     var8 = var2;
                     var12 = var6;
                     var7 = var5;
                     if (var9 != '\r') {
                        var8 = var2;
                        var12 = var6;
                        var7 = var5;
                        if (var9 != ' ') {
                           if (var9 != '\t') {
                              return null;
                           }

                           var8 = var2;
                           var12 = var6;
                           var7 = var5;
                        }
                     }
                  }
                  break label109;
               }

               var12 = 63;
            } else {
               var12 = 62;
            }

            var5 = var5 << 6 | (byte)var12;
            ++var6;
            var8 = var2;
            var12 = var6;
            var7 = var5;
            if (var6 % 4 == 0) {
               var12 = var2 + 1;
               var10[var2] = (byte)(var5 >> 16);
               var2 = var12 + 1;
               var10[var12] = (byte)(var5 >> 8);
               var10[var2] = (byte)var5;
               var8 = var2 + 1;
               var7 = var5;
               var12 = var6;
            }
         }

         ++var4;
         var2 = var8;
         var6 = var12;
      }

      var3 = var6 % 4;
      if (var3 == 1) {
         return null;
      } else {
         if (var3 == 2) {
            var10[var2] = (byte)(var5 << 12 >> 16);
            var12 = var2 + 1;
         } else {
            var12 = var2;
            if (var3 == 3) {
               var3 = var5 << 6;
               var4 = var2 + 1;
               var10[var2] = (byte)(var3 >> 16);
               var12 = var4 + 1;
               var10[var4] = (byte)(var3 >> 8);
            }
         }

         if (var12 == var10.length) {
            return var10;
         } else {
            byte[] var11 = new byte[var12];
            System.arraycopy(var10, 0, var11, 0, var12);
            return var11;
         }
      }
   }

   public static String encode(byte[] var0) {
      return encode(var0, MAP);
   }

   private static String encode(byte[] var0, byte[] var1) {
      byte[] var6 = new byte[(var0.length + 2) / 3 * 4];
      int var2 = 0;
      int var4 = var0.length - var0.length % 3;

      int var3;
      for(var3 = 0; var3 < var4; var3 += 3) {
         int var5 = var2 + 1;
         var6[var2] = var1[(var0[var3] & 255) >> 2];
         var2 = var5 + 1;
         var6[var5] = var1[(var0[var3] & 3) << 4 | (var0[var3 + 1] & 255) >> 4];
         var5 = var2 + 1;
         var6[var2] = var1[(var0[var3 + 1] & 15) << 2 | (var0[var3 + 2] & 255) >> 6];
         var2 = var5 + 1;
         var6[var5] = var1[var0[var3 + 2] & 63];
      }

      var3 = var0.length % 3;
      if (var3 != 1) {
         if (var3 == 2) {
            var3 = var2 + 1;
            var6[var2] = var1[(var0[var4] & 255) >> 2];
            var2 = var3 + 1;
            var6[var3] = var1[(var0[var4] & 3) << 4 | (var0[var4 + 1] & 255) >> 4];
            var3 = var2 + 1;
            var6[var2] = var1[(var0[var4 + 1] & 15) << 2];
            var6[var3] = 61;
         }
      } else {
         var3 = var2 + 1;
         var6[var2] = var1[(var0[var4] & 255) >> 2];
         var2 = var3 + 1;
         var6[var3] = var1[(var0[var4] & 3) << 4];
         var3 = var2 + 1;
         var6[var2] = 61;
         var6[var3] = 61;
      }

      try {
         String var8 = new String(var6, "US-ASCII");
         return var8;
      } catch (UnsupportedEncodingException var7) {
         throw new AssertionError(var7);
      }
   }

   public static String encodeUrl(byte[] var0) {
      return encode(var0, URL_MAP);
   }
}
