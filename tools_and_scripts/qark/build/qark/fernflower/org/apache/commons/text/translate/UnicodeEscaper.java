package org.apache.commons.text.translate;

import java.io.IOException;
import java.io.Writer;

public class UnicodeEscaper extends CodePointTranslator {
   private final int above;
   private final int below;
   private final boolean between;

   public UnicodeEscaper() {
      this(0, Integer.MAX_VALUE, true);
   }

   protected UnicodeEscaper(int var1, int var2, boolean var3) {
      this.below = var1;
      this.above = var2;
      this.between = var3;
   }

   public static UnicodeEscaper above(int var0) {
      return outsideOf(0, var0);
   }

   public static UnicodeEscaper below(int var0) {
      return outsideOf(var0, Integer.MAX_VALUE);
   }

   public static UnicodeEscaper between(int var0, int var1) {
      return new UnicodeEscaper(var0, var1, true);
   }

   public static UnicodeEscaper outsideOf(int var0, int var1) {
      return new UnicodeEscaper(var0, var1, false);
   }

   protected String toUtf16Escape(int var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("\\u");
      var2.append(hex(var1));
      return var2.toString();
   }

   public boolean translate(int var1, Writer var2) throws IOException {
      if (this.between) {
         if (var1 < this.below || var1 > this.above) {
            return false;
         }
      } else if (var1 >= this.below && var1 <= this.above) {
         return false;
      }

      if (var1 > 65535) {
         var2.write(this.toUtf16Escape(var1));
      } else {
         var2.write("\\u");
         var2.write(HEX_DIGITS[var1 >> 12 & 15]);
         var2.write(HEX_DIGITS[var1 >> 8 & 15]);
         var2.write(HEX_DIGITS[var1 >> 4 & 15]);
         var2.write(HEX_DIGITS[var1 & 15]);
      }

      return true;
   }
}
