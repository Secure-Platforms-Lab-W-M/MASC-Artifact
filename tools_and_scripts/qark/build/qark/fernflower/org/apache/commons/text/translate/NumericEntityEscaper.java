package org.apache.commons.text.translate;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.Range;

public class NumericEntityEscaper extends CodePointTranslator {
   private final boolean between;
   private final Range range;

   public NumericEntityEscaper() {
      this(0, Integer.MAX_VALUE, true);
   }

   private NumericEntityEscaper(int var1, int var2, boolean var3) {
      this.range = Range.between(var1, var2);
      this.between = var3;
   }

   public static NumericEntityEscaper above(int var0) {
      return outsideOf(0, var0);
   }

   public static NumericEntityEscaper below(int var0) {
      return outsideOf(var0, Integer.MAX_VALUE);
   }

   public static NumericEntityEscaper between(int var0, int var1) {
      return new NumericEntityEscaper(var0, var1, true);
   }

   public static NumericEntityEscaper outsideOf(int var0, int var1) {
      return new NumericEntityEscaper(var0, var1, false);
   }

   public boolean translate(int var1, Writer var2) throws IOException {
      if (this.between != this.range.contains(var1)) {
         return false;
      } else {
         var2.write("&#");
         var2.write(Integer.toString(var1, 10));
         var2.write(59);
         return true;
      }
   }
}
