package com.google.i18n.phonenumbers.internal;

import com.google.i18n.phonenumbers.Phonemetadata;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexBasedMatcher implements MatcherApi {
   private final RegexCache regexCache = new RegexCache(100);

   private RegexBasedMatcher() {
   }

   public static MatcherApi create() {
      return new RegexBasedMatcher();
   }

   private static boolean match(CharSequence var0, Pattern var1, boolean var2) {
      Matcher var3 = var1.matcher(var0);
      if (!var3.lookingAt()) {
         return false;
      } else {
         return var3.matches() ? true : var2;
      }
   }

   public boolean matchNationalNumber(CharSequence var1, Phonemetadata.PhoneNumberDesc var2, boolean var3) {
      String var4 = var2.getNationalNumberPattern();
      return var4.length() == 0 ? false : match(var1, this.regexCache.getPatternForRegex(var4), var3);
   }
}
