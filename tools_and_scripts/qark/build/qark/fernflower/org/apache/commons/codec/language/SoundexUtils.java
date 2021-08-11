package org.apache.commons.codec.language;

import java.util.Locale;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;

final class SoundexUtils {
   static String clean(String var0) {
      if (var0 != null) {
         if (var0.length() == 0) {
            return var0;
         } else {
            int var4 = var0.length();
            char[] var5 = new char[var4];
            int var2 = 0;

            int var3;
            for(int var1 = 0; var1 < var4; var2 = var3) {
               var3 = var2;
               if (Character.isLetter(var0.charAt(var1))) {
                  var5[var2] = var0.charAt(var1);
                  var3 = var2 + 1;
               }

               ++var1;
            }

            if (var2 == var4) {
               return var0.toUpperCase(Locale.ENGLISH);
            } else {
               return (new String(var5, 0, var2)).toUpperCase(Locale.ENGLISH);
            }
         }
      } else {
         return var0;
      }
   }

   static int difference(StringEncoder var0, String var1, String var2) throws EncoderException {
      return differenceEncoded(var0.encode(var1), var0.encode(var2));
   }

   static int differenceEncoded(String var0, String var1) {
      if (var0 != null && var1 != null) {
         int var5 = Math.min(var0.length(), var1.length());
         int var3 = 0;

         int var4;
         for(int var2 = 0; var2 < var5; var3 = var4) {
            var4 = var3;
            if (var0.charAt(var2) == var1.charAt(var2)) {
               var4 = var3 + 1;
            }

            ++var2;
         }

         return var3;
      } else {
         return 0;
      }
   }
}
