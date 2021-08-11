package org.apache.commons.text.similarity;

public class LongestCommonSubsequenceDistance implements EditDistance {
   private final LongestCommonSubsequence longestCommonSubsequence = new LongestCommonSubsequence();

   public Integer apply(CharSequence var1, CharSequence var2) {
      if (var1 != null && var2 != null) {
         return var1.length() + var2.length() - this.longestCommonSubsequence.apply(var1, var2) * 2;
      } else {
         throw new IllegalArgumentException("Inputs must not be null");
      }
   }
}
