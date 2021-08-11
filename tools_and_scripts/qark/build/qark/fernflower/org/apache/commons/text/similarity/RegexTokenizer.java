package org.apache.commons.text.similarity;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

class RegexTokenizer implements Tokenizer {
   private static final Pattern PATTERN = Pattern.compile("(\\w)+");

   public CharSequence[] tokenize(CharSequence var1) {
      Validate.isTrue(StringUtils.isNotBlank(var1), "Invalid text");
      Matcher var3 = PATTERN.matcher(var1);
      ArrayList var2 = new ArrayList();

      while(var3.find()) {
         var2.add(var3.group(0));
      }

      return (CharSequence[])var2.toArray(new String[0]);
   }
}
