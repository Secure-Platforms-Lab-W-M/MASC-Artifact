package org.apache.commons.text.translate;

import java.io.IOException;
import java.io.Writer;

public class UnicodeUnescaper extends CharSequenceTranslator {
   public int translate(CharSequence var1, int var2, Writer var3) throws IOException {
      if (var1.charAt(var2) == '\\' && var2 + 1 < var1.length() && var1.charAt(var2 + 1) == 'u') {
         int var4;
         for(var4 = 2; var2 + var4 < var1.length() && var1.charAt(var2 + var4) == 'u'; ++var4) {
         }

         int var5 = var4;
         if (var2 + var4 < var1.length()) {
            var5 = var4;
            if (var1.charAt(var2 + var4) == '+') {
               var5 = var4 + 1;
            }
         }

         if (var2 + var5 + 4 <= var1.length()) {
            var1 = var1.subSequence(var2 + var5, var2 + var5 + 4);

            try {
               var3.write((char)Integer.parseInt(var1.toString(), 16));
            } catch (NumberFormatException var7) {
               StringBuilder var6 = new StringBuilder();
               var6.append("Unable to parse unicode value: ");
               var6.append(var1);
               throw new IllegalArgumentException(var6.toString(), var7);
            }

            return var5 + 4;
         } else {
            StringBuilder var8 = new StringBuilder();
            var8.append("Less than 4 hex digits in unicode value: '");
            var8.append(var1.subSequence(var2, var1.length()));
            var8.append("' due to end of CharSequence");
            throw new IllegalArgumentException(var8.toString());
         }
      } else {
         return 0;
      }
   }
}
