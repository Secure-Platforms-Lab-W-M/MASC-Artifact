package gnu.java.zrtp.utils;

public class EmojiBase32 {
   static int[] emojis = new int[]{128513, 128570, 128101, 127794, 128095, 9995, 128077, 128053, 128052, 128013, 128031, 127800, 127760, 127968, 127774, 128664, 128309, 128674, 128314, 128042, 128293, 127880, 128038, 128269, 128215, 128161, 128310, 128296, 128347, 127775, 10062, 128681};

   public static String binary2ascii(byte[] var0, int var1) {
      int var4 = (var1 + 7) / 8;
      int[] var9 = new int[Base32.divceil(var4 * 8, 5)];

      int var2;
      for(var2 = 0; var2 < var9.length; ++var2) {
         var9[var2] = 32;
      }

      var2 = var9.length;
      int var3 = 0;
      int var5 = var4 % 5;

      do {
         label37: {
            int var6;
            int var7;
            int var8;
            int[] var10;
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
                        var9[var6] = emojis[var3 % 32];
                        var7 = var3 / 32;
                     }

                     --var8;
                     var2 = var7 | (var0[var8] & 255) << 3;
                     var3 = var6 - 1;
                     var10 = emojis;
                     var9[var3] = var10[var2 % 32];
                     var2 /= 32;
                     var6 = var3 - 1;
                     var9[var6] = var10[var2 % 32];
                     var7 = var2 / 32;
                  }

                  --var8;
                  var2 = var7 | (var0[var8] & 255) << 1;
                  --var6;
                  var9[var6] = emojis[var2 % 32];
                  var7 = var2 / 32;
               }

               --var8;
               var2 = var7 | (var0[var8] & 255) << 4;
               var3 = var6 - 1;
               var10 = emojis;
               var9[var3] = var10[var2 % 32];
               var2 /= 32;
               var6 = var3 - 1;
               var9[var6] = var10[var2 % 32];
               var7 = var2 / 32;
            }

            var4 = var8 - 1;
            var2 = var7 | (var0[var4] & 255) << 2;
            var5 = var6 - 1;
            var10 = emojis;
            var9[var5] = var10[var2 % 32];
            var3 = var2 / 32;
            var2 = var5 - 1;
            var9[var2] = var10[var3];
         }

         var5 = 0;
      } while(var4 > 0);

      return new String(var9, 0, Base32.divceil(var1, 5));
   }
}
