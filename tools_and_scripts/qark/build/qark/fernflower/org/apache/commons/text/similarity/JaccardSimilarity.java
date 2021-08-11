package org.apache.commons.text.similarity;

import java.util.HashSet;

public class JaccardSimilarity implements SimilarityScore {
   private Double calculateJaccardSimilarity(CharSequence var1, CharSequence var2) {
      int var5 = var1.length();
      int var4 = var2.length();
      if (var5 != 0 && var4 != 0) {
         HashSet var6 = new HashSet();

         int var3;
         for(var3 = 0; var3 < var5; ++var3) {
            var6.add(var1.charAt(var3));
         }

         HashSet var7 = new HashSet();

         for(var3 = 0; var3 < var4; ++var3) {
            var7.add(var2.charAt(var3));
         }

         HashSet var8 = new HashSet(var6);
         var8.addAll(var7);
         return (double)(var6.size() + var7.size() - var8.size()) * 1.0D / (double)var8.size();
      } else {
         return 0.0D;
      }
   }

   public Double apply(CharSequence var1, CharSequence var2) {
      if (var1 != null && var2 != null) {
         return this.calculateJaccardSimilarity(var1, var2);
      } else {
         throw new IllegalArgumentException("Input cannot be null");
      }
   }
}
