package org.apache.commons.text.translate;

import java.io.IOException;
import java.io.Writer;

public class OctalUnescaper extends CharSequenceTranslator {
   private boolean isOctalDigit(char var1) {
      return var1 >= '0' && var1 <= '7';
   }

   private boolean isZeroToThree(char var1) {
      return var1 >= '0' && var1 <= '3';
   }

   public int translate(CharSequence var1, int var2, Writer var3) throws IOException {
      int var4 = var1.length() - var2 - 1;
      StringBuilder var7 = new StringBuilder();
      if (var1.charAt(var2) == '\\' && var4 > 0 && this.isOctalDigit(var1.charAt(var2 + 1))) {
         int var5 = var2 + 1;
         int var6 = var2 + 2;
         var2 += 3;
         var7.append(var1.charAt(var5));
         if (var4 > 1 && this.isOctalDigit(var1.charAt(var6))) {
            var7.append(var1.charAt(var6));
            if (var4 > 2 && this.isZeroToThree(var1.charAt(var5)) && this.isOctalDigit(var1.charAt(var2))) {
               var7.append(var1.charAt(var2));
            }
         }

         var3.write(Integer.parseInt(var7.toString(), 8));
         return var7.length() + 1;
      } else {
         return 0;
      }
   }
}
