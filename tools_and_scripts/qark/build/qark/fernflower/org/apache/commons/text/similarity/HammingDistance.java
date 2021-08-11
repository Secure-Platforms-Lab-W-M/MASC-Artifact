package org.apache.commons.text.similarity;

public class HammingDistance implements EditDistance {
   public Integer apply(CharSequence var1, CharSequence var2) {
      if (var1 != null && var2 != null) {
         if (var1.length() == var2.length()) {
            int var4 = 0;

            int var5;
            for(int var3 = 0; var3 < var1.length(); var4 = var5) {
               var5 = var4;
               if (var1.charAt(var3) != var2.charAt(var3)) {
                  var5 = var4 + 1;
               }

               ++var3;
            }

            return var4;
         } else {
            throw new IllegalArgumentException("CharSequences must have the same length");
         }
      } else {
         throw new IllegalArgumentException("CharSequences must not be null");
      }
   }
}
