package org.apache.commons.text.similarity;

public class JaccardDistance implements EditDistance {
   private final JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();

   public Double apply(CharSequence var1, CharSequence var2) {
      if (var1 != null && var2 != null) {
         return 1.0D - this.jaccardSimilarity.apply(var1, var2);
      } else {
         throw new IllegalArgumentException("Input cannot be null");
      }
   }
}
