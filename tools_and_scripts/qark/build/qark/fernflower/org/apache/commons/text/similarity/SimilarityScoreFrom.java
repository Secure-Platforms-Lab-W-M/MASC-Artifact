package org.apache.commons.text.similarity;

import org.apache.commons.lang3.Validate;

public class SimilarityScoreFrom {
   private final CharSequence left;
   private final SimilarityScore similarityScore;

   public SimilarityScoreFrom(SimilarityScore var1, CharSequence var2) {
      boolean var3;
      if (var1 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      Validate.isTrue(var3, "The edit distance may not be null.");
      this.similarityScore = var1;
      this.left = var2;
   }

   public Object apply(CharSequence var1) {
      return this.similarityScore.apply(this.left, var1);
   }

   public CharSequence getLeft() {
      return this.left;
   }

   public SimilarityScore getSimilarityScore() {
      return this.similarityScore;
   }
}
