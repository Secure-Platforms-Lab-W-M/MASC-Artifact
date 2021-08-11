package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.Writer;
import org.apache.commons.lang3.ArrayUtils;

@Deprecated
public class AggregateTranslator extends CharSequenceTranslator {
   private final CharSequenceTranslator[] translators;

   public AggregateTranslator(CharSequenceTranslator... var1) {
      this.translators = (CharSequenceTranslator[])ArrayUtils.clone((Object[])var1);
   }

   public int translate(CharSequence var1, int var2, Writer var3) throws IOException {
      CharSequenceTranslator[] var7 = this.translators;
      int var5 = var7.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         int var6 = var7[var4].translate(var1, var2, var3);
         if (var6 != 0) {
            return var6;
         }
      }

      return 0;
   }
}
