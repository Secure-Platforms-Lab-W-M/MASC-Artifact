package org.apache.commons.text.translate;

import java.io.IOException;
import java.io.Writer;

abstract class SinglePassTranslator extends CharSequenceTranslator {
   private String getClassName() {
      Class var1 = this.getClass();
      return var1.isAnonymousClass() ? var1.getName() : var1.getSimpleName();
   }

   public int translate(CharSequence var1, int var2, Writer var3) throws IOException {
      if (var2 == 0) {
         this.translateWhole(var1, var3);
         return Character.codePointCount(var1, var2, var1.length());
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append(this.getClassName());
         var4.append(".translate(final CharSequence input, final int index, final Writer out) can not handle a non-zero index.");
         throw new IllegalArgumentException(var4.toString());
      }
   }

   abstract void translateWhole(CharSequence var1, Writer var2) throws IOException;
}
