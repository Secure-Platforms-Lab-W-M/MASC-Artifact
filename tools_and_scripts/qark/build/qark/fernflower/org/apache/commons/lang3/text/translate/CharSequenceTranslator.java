package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

@Deprecated
public abstract class CharSequenceTranslator {
   static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

   public static String hex(int var0) {
      return Integer.toHexString(var0).toUpperCase(Locale.ENGLISH);
   }

   public abstract int translate(CharSequence var1, int var2, Writer var3) throws IOException;

   public final String translate(CharSequence var1) {
      if (var1 == null) {
         return null;
      } else {
         try {
            StringWriter var2 = new StringWriter(var1.length() * 2);
            this.translate(var1, var2);
            String var4 = var2.toString();
            return var4;
         } catch (IOException var3) {
            throw new RuntimeException(var3);
         }
      }
   }

   public final void translate(CharSequence var1, Writer var2) throws IOException {
      if (var2 == null) {
         throw new IllegalArgumentException("The Writer must not be null");
      } else if (var1 != null) {
         int var4 = 0;
         int var6 = var1.length();

         while(true) {
            while(var4 < var6) {
               int var7 = this.translate(var1, var4, var2);
               int var5;
               if (var7 == 0) {
                  char var3 = var1.charAt(var4);
                  var2.write(var3);
                  var5 = var4 + 1;
                  var4 = var5;
                  if (Character.isHighSurrogate(var3)) {
                     var4 = var5;
                     if (var5 < var6) {
                        var3 = var1.charAt(var5);
                        var4 = var5;
                        if (Character.isLowSurrogate(var3)) {
                           var2.write(var3);
                           var4 = var5 + 1;
                        }
                     }
                  }
               } else {
                  for(var5 = 0; var5 < var7; ++var5) {
                     var4 += Character.charCount(Character.codePointAt(var1, var4));
                  }
               }
            }

            return;
         }
      }
   }

   public final CharSequenceTranslator with(CharSequenceTranslator... var1) {
      CharSequenceTranslator[] var2 = new CharSequenceTranslator[var1.length + 1];
      var2[0] = this;
      System.arraycopy(var1, 0, var2, 1, var1.length);
      return new AggregateTranslator(var2);
   }
}
