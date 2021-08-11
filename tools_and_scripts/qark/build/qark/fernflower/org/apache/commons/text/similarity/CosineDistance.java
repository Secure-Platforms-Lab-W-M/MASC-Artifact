package org.apache.commons.text.similarity;

import java.util.Map;

public class CosineDistance implements EditDistance {
   private final CosineSimilarity cosineSimilarity = new CosineSimilarity();
   private final Tokenizer tokenizer = new RegexTokenizer();

   public Double apply(CharSequence var1, CharSequence var2) {
      CharSequence[] var3 = (CharSequence[])this.tokenizer.tokenize(var1);
      CharSequence[] var5 = (CharSequence[])this.tokenizer.tokenize(var2);
      Map var4 = Counter.method_4(var3);
      Map var6 = Counter.method_4(var5);
      return 1.0D - this.cosineSimilarity.cosineSimilarity(var4, var6);
   }
}
