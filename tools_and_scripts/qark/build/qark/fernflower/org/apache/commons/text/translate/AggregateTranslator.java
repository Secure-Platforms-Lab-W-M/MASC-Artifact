package org.apache.commons.text.translate;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AggregateTranslator extends CharSequenceTranslator {
   private final List translators = new ArrayList();

   public AggregateTranslator(CharSequenceTranslator... var1) {
      if (var1 != null) {
         int var3 = var1.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            CharSequenceTranslator var4 = var1[var2];
            if (var4 != null) {
               this.translators.add(var4);
            }
         }
      }

   }

   public int translate(CharSequence var1, int var2, Writer var3) throws IOException {
      Iterator var5 = this.translators.iterator();

      int var4;
      do {
         if (!var5.hasNext()) {
            return 0;
         }

         var4 = ((CharSequenceTranslator)var5.next()).translate(var1, var2, var3);
      } while(var4 == 0);

      return var4;
   }
}
