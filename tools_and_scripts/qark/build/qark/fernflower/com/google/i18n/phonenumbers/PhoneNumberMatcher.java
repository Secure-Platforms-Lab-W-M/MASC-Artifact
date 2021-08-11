package com.google.i18n.phonenumbers;

import java.lang.Character.UnicodeBlock;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

final class PhoneNumberMatcher implements Iterator {
   private static final Pattern[] INNER_MATCHES = new Pattern[]{Pattern.compile("/+(.*)"), Pattern.compile("(\\([^(]*)"), Pattern.compile("(?:\\p{Z}-|-\\p{Z})\\p{Z}*(.+)"), Pattern.compile("[‒-―－]\\p{Z}*(.+)"), Pattern.compile("\\.+\\p{Z}*([^.]+)"), Pattern.compile("\\p{Z}+(\\P{Z}+)")};
   private static final Pattern LEAD_CLASS;
   private static final Pattern MATCHING_BRACKETS;
   private static final Pattern PATTERN;
   private static final Pattern PUB_PAGES = Pattern.compile("\\d{1,5}-+\\d{1,5}\\s{0,4}\\(\\d{1,4}");
   private static final Pattern SLASH_SEPARATED_DATES = Pattern.compile("(?:(?:[0-3]?\\d/[01]?\\d)|(?:[01]?\\d/[0-3]?\\d))/(?:[12]\\d)?\\d{2}");
   private static final Pattern TIME_STAMPS = Pattern.compile("[12]\\d{3}[-/]?[01]\\d[-/]?[0-3]\\d +[0-2]\\d$");
   private static final Pattern TIME_STAMPS_SUFFIX = Pattern.compile(":[0-5]\\d");
   private PhoneNumberMatch lastMatch;
   private final PhoneNumberUtil.Leniency leniency;
   private long maxTries;
   private final PhoneNumberUtil phoneUtil;
   private final String preferredRegion;
   private int searchIndex;
   private PhoneNumberMatcher.State state;
   private final CharSequence text;

   static {
      StringBuilder var0 = new StringBuilder();
      var0.append("[^");
      var0.append("(\\[（［");
      var0.append(")\\]）］");
      var0.append("]");
      String var6 = var0.toString();
      String var1 = limit(0, 3);
      StringBuilder var2 = new StringBuilder();
      var2.append("(?:[");
      var2.append("(\\[（［");
      var2.append("])?(?:");
      var2.append(var6);
      var2.append("+[");
      var2.append(")\\]）］");
      var2.append("])?");
      var2.append(var6);
      var2.append("+(?:[");
      var2.append("(\\[（［");
      var2.append("]");
      var2.append(var6);
      var2.append("+[");
      var2.append(")\\]）］");
      var2.append("])");
      var2.append(var1);
      var2.append(var6);
      var2.append("*");
      MATCHING_BRACKETS = Pattern.compile(var2.toString());
      var6 = limit(0, 2);
      String var7 = limit(0, 4);
      var1 = limit(0, 20);
      StringBuilder var3 = new StringBuilder();
      var3.append("[-x‐-―−ー－-／  \u00ad\u200b\u2060　()（）［］.\\[\\]/~⁓∼～]");
      var3.append(var7);
      var7 = var3.toString();
      var3 = new StringBuilder();
      var3.append("\\p{Nd}");
      var3.append(limit(1, 20));
      String var8 = var3.toString();
      StringBuilder var4 = new StringBuilder();
      var4.append("(\\[（［");
      var4.append("+＋");
      String var9 = var4.toString();
      StringBuilder var5 = new StringBuilder();
      var5.append("[");
      var5.append(var9);
      var5.append("]");
      var9 = var5.toString();
      LEAD_CLASS = Pattern.compile(var9);
      var5 = new StringBuilder();
      var5.append("(?:");
      var5.append(var9);
      var5.append(var7);
      var5.append(")");
      var5.append(var6);
      var5.append(var8);
      var5.append("(?:");
      var5.append(var7);
      var5.append(var8);
      var5.append(")");
      var5.append(var1);
      var5.append("(?:");
      var5.append(PhoneNumberUtil.EXTN_PATTERNS_FOR_MATCHING);
      var5.append(")?");
      PATTERN = Pattern.compile(var5.toString(), 66);
   }

   PhoneNumberMatcher(PhoneNumberUtil var1, CharSequence var2, String var3, PhoneNumberUtil.Leniency var4, long var5) {
      this.state = PhoneNumberMatcher.State.NOT_READY;
      this.lastMatch = null;
      this.searchIndex = 0;
      if (var1 != null && var4 != null) {
         if (var5 >= 0L) {
            this.phoneUtil = var1;
            if (var2 == null) {
               var2 = "";
            }

            this.text = (CharSequence)var2;
            this.preferredRegion = var3;
            this.leniency = var4;
            this.maxTries = var5;
         } else {
            throw new IllegalArgumentException();
         }
      } else {
         throw null;
      }
   }

   static boolean allNumberGroupsAreExactlyPresent(PhoneNumberUtil var0, Phonenumber.PhoneNumber var1, StringBuilder var2, String[] var3) {
      String[] var6 = PhoneNumberUtil.NON_DIGITS_PATTERN.split(var2.toString());
      int var4;
      if (var1.hasExtension()) {
         var4 = var6.length - 2;
      } else {
         var4 = var6.length - 1;
      }

      if (var6.length == 1) {
         return true;
      } else if (var6[var4].contains(var0.getNationalSignificantNumber(var1))) {
         return true;
      } else {
         for(int var5 = var3.length - 1; var5 > 0 && var4 >= 0; --var4) {
            if (!var6[var4].equals(var3[var5])) {
               return false;
            }

            --var5;
         }

         return var4 >= 0 && var6[var4].endsWith(var3[0]);
      }
   }

   static boolean allNumberGroupsRemainGrouped(PhoneNumberUtil var0, Phonenumber.PhoneNumber var1, StringBuilder var2, String[] var3) {
      int var4 = 0;
      if (var1.getCountryCodeSource() != Phonenumber.PhoneNumber.CountryCodeSource.FROM_DEFAULT_COUNTRY) {
         String var6 = Integer.toString(var1.getCountryCode());
         var4 = var2.indexOf(var6) + var6.length();
      }

      for(int var5 = 0; var5 < var3.length; ++var5) {
         var4 = var2.indexOf(var3[var5], var4);
         if (var4 < 0) {
            return false;
         }

         var4 += var3[var5].length();
         if (var5 == 0 && var4 < var2.length() && var0.getNddPrefixForRegion(var0.getRegionCodeForCountryCode(var1.getCountryCode()), true) != null && Character.isDigit(var2.charAt(var4))) {
            String var7 = var0.getNationalSignificantNumber(var1);
            return var2.substring(var4 - var3[var5].length()).startsWith(var7);
         }
      }

      return var2.substring(var4).contains(var1.getExtension());
   }

   static boolean checkNumberGroupingIsValid(Phonenumber.PhoneNumber var0, CharSequence var1, PhoneNumberUtil var2, PhoneNumberMatcher.NumberGroupingChecker var3) {
      StringBuilder var5 = PhoneNumberUtil.normalizeDigits(var1, true);
      if (var3.checkGroups(var2, var0, var5, getNationalNumberGroups(var2, var0, (Phonemetadata.NumberFormat)null))) {
         return true;
      } else {
         Phonemetadata.PhoneMetadata var4 = MetadataManager.getAlternateFormatsForCountry(var0.getCountryCode());
         if (var4 != null) {
            Iterator var6 = var4.numberFormats().iterator();

            while(var6.hasNext()) {
               if (var3.checkGroups(var2, var0, var5, getNationalNumberGroups(var2, var0, (Phonemetadata.NumberFormat)var6.next()))) {
                  return true;
               }
            }
         }

         return false;
      }
   }

   static boolean containsMoreThanOneSlashInNationalNumber(Phonenumber.PhoneNumber var0, String var1) {
      int var3 = var1.indexOf(47);
      if (var3 < 0) {
         return false;
      } else {
         int var4 = var1.indexOf(47, var3 + 1);
         if (var4 < 0) {
            return false;
         } else {
            boolean var2;
            if (var0.getCountryCodeSource() != Phonenumber.PhoneNumber.CountryCodeSource.FROM_NUMBER_WITH_PLUS_SIGN && var0.getCountryCodeSource() != Phonenumber.PhoneNumber.CountryCodeSource.FROM_NUMBER_WITHOUT_PLUS_SIGN) {
               var2 = false;
            } else {
               var2 = true;
            }

            return var2 && PhoneNumberUtil.normalizeDigitsOnly(var1.substring(0, var3)).equals(Integer.toString(var0.getCountryCode())) ? var1.substring(var4 + 1).contains("/") : true;
         }
      }
   }

   static boolean containsOnlyValidXChars(Phonenumber.PhoneNumber var0, String var1, PhoneNumberUtil var2) {
      int var4;
      for(int var3 = 0; var3 < var1.length() - 1; var3 = var4 + 1) {
         char var5 = var1.charAt(var3);
         if (var5 != 'x') {
            var4 = var3;
            if (var5 != 'X') {
               continue;
            }
         }

         char var6 = var1.charAt(var3 + 1);
         if (var6 != 'x' && var6 != 'X') {
            var4 = var3;
            if (!PhoneNumberUtil.normalizeDigitsOnly(var1.substring(var3)).equals(var0.getExtension())) {
               return false;
            }
         } else {
            ++var3;
            var4 = var3;
            if (var2.isNumberMatch((Phonenumber.PhoneNumber)var0, (CharSequence)var1.substring(var3)) != PhoneNumberUtil.MatchType.NSN_MATCH) {
               return false;
            }
         }
      }

      return true;
   }

   private PhoneNumberMatch extractInnerMatch(CharSequence var1, int var2) {
      Pattern[] var7 = INNER_MATCHES;
      int var6 = var7.length;

      for(int var3 = 0; var3 < var6; ++var3) {
         Matcher var8 = var7[var3].matcher(var1);

         boolean var5;
         for(boolean var4 = true; var8.find() && this.maxTries > 0L; var4 = var5) {
            var5 = var4;
            PhoneNumberMatch var9;
            if (var4) {
               var9 = this.parseAndVerify(trimAfterFirstMatch(PhoneNumberUtil.UNWANTED_END_CHAR_PATTERN, var1.subSequence(0, var8.start())), var2);
               if (var9 != null) {
                  return var9;
               }

               --this.maxTries;
               var5 = false;
            }

            var9 = this.parseAndVerify(trimAfterFirstMatch(PhoneNumberUtil.UNWANTED_END_CHAR_PATTERN, var8.group(1)), var8.start(1) + var2);
            if (var9 != null) {
               return var9;
            }

            --this.maxTries;
         }
      }

      return null;
   }

   private PhoneNumberMatch extractMatch(CharSequence var1, int var2) {
      if (SLASH_SEPARATED_DATES.matcher(var1).find()) {
         return null;
      } else {
         if (TIME_STAMPS.matcher(var1).find()) {
            String var3 = this.text.toString().substring(var1.length() + var2);
            if (TIME_STAMPS_SUFFIX.matcher(var3).lookingAt()) {
               return null;
            }
         }

         PhoneNumberMatch var4 = this.parseAndVerify(var1, var2);
         return var4 != null ? var4 : this.extractInnerMatch(var1, var2);
      }
   }

   private PhoneNumberMatch find(int var1) {
      for(Matcher var2 = PATTERN.matcher(this.text); this.maxTries > 0L && var2.find(var1); --this.maxTries) {
         var1 = var2.start();
         CharSequence var3 = this.text.subSequence(var1, var2.end());
         var3 = trimAfterFirstMatch(PhoneNumberUtil.SECOND_NUMBER_START_PATTERN, var3);
         PhoneNumberMatch var4 = this.extractMatch(var3, var1);
         if (var4 != null) {
            return var4;
         }

         var1 += var3.length();
      }

      return null;
   }

   private static String[] getNationalNumberGroups(PhoneNumberUtil var0, Phonenumber.PhoneNumber var1, Phonemetadata.NumberFormat var2) {
      if (var2 == null) {
         String var5 = var0.format(var1, PhoneNumberUtil.PhoneNumberFormat.RFC3966);
         int var4 = var5.indexOf(59);
         int var3 = var4;
         if (var4 < 0) {
            var3 = var5.length();
         }

         return var5.substring(var5.indexOf(45) + 1, var3).split("-");
      } else {
         return var0.formatNsnUsingPattern(var0.getNationalSignificantNumber(var1), var2, PhoneNumberUtil.PhoneNumberFormat.RFC3966).split("-");
      }
   }

   private static boolean isInvalidPunctuationSymbol(char var0) {
      return var0 == '%' || Character.getType(var0) == 26;
   }

   static boolean isLatinLetter(char var0) {
      boolean var2 = Character.isLetter(var0);
      boolean var1 = false;
      if (!var2 && Character.getType(var0) != 6) {
         return false;
      } else {
         UnicodeBlock var3 = UnicodeBlock.of(var0);
         if (var3.equals(UnicodeBlock.BASIC_LATIN) || var3.equals(UnicodeBlock.LATIN_1_SUPPLEMENT) || var3.equals(UnicodeBlock.LATIN_EXTENDED_A) || var3.equals(UnicodeBlock.LATIN_EXTENDED_ADDITIONAL) || var3.equals(UnicodeBlock.LATIN_EXTENDED_B) || var3.equals(UnicodeBlock.COMBINING_DIACRITICAL_MARKS)) {
            var1 = true;
         }

         return var1;
      }
   }

   static boolean isNationalPrefixPresentIfRequired(Phonenumber.PhoneNumber var0, PhoneNumberUtil var1) {
      if (var0.getCountryCodeSource() != Phonenumber.PhoneNumber.CountryCodeSource.FROM_DEFAULT_COUNTRY) {
         return true;
      } else {
         Phonemetadata.PhoneMetadata var2 = var1.getMetadataForRegion(var1.getRegionCodeForCountryCode(var0.getCountryCode()));
         if (var2 == null) {
            return true;
         } else {
            String var3 = var1.getNationalSignificantNumber(var0);
            Phonemetadata.NumberFormat var4 = var1.chooseFormattingPatternForNumber(var2.numberFormats(), var3);
            if (var4 != null && var4.getNationalPrefixFormattingRule().length() > 0) {
               if (var4.getNationalPrefixOptionalWhenFormatting()) {
                  return true;
               } else {
                  return PhoneNumberUtil.formattingRuleHasFirstGroupOnly(var4.getNationalPrefixFormattingRule()) ? true : var1.maybeStripNationalPrefixAndCarrierCode(new StringBuilder(PhoneNumberUtil.normalizeDigitsOnly(var0.getRawInput())), var2, (StringBuilder)null);
               }
            } else {
               return true;
            }
         }
      }
   }

   private static String limit(int var0, int var1) {
      if (var0 >= 0 && var1 > 0 && var1 >= var0) {
         StringBuilder var2 = new StringBuilder();
         var2.append("{");
         var2.append(var0);
         var2.append(",");
         var2.append(var1);
         var2.append("}");
         return var2.toString();
      } else {
         throw new IllegalArgumentException();
      }
   }

   private PhoneNumberMatch parseAndVerify(CharSequence var1, int var2) {
      boolean var10001;
      try {
         if (!MATCHING_BRACKETS.matcher(var1).matches()) {
            return null;
         }

         if (PUB_PAGES.matcher(var1).find()) {
            return null;
         }
      } catch (NumberParseException var10) {
         var10001 = false;
         return null;
      }

      label67: {
         try {
            if (this.leniency.compareTo(PhoneNumberUtil.Leniency.VALID) < 0) {
               break label67;
            }
         } catch (NumberParseException var9) {
            var10001 = false;
            return null;
         }

         char var3;
         if (var2 > 0) {
            try {
               if (!LEAD_CLASS.matcher(var1).lookingAt()) {
                  var3 = this.text.charAt(var2 - 1);
                  if (isInvalidPunctuationSymbol(var3)) {
                     return null;
                  }

                  if (isLatinLetter(var3)) {
                     return null;
                  }
               }
            } catch (NumberParseException var8) {
               var10001 = false;
               return null;
            }
         }

         try {
            int var4 = var1.length() + var2;
            if (var4 < this.text.length()) {
               var3 = this.text.charAt(var4);
               if (isInvalidPunctuationSymbol(var3)) {
                  return null;
               }

               if (isLatinLetter(var3)) {
                  return null;
               }
            }
         } catch (NumberParseException var7) {
            var10001 = false;
            return null;
         }
      }

      try {
         Phonenumber.PhoneNumber var5 = this.phoneUtil.parseAndKeepRawInput(var1, this.preferredRegion);
         if (this.leniency.verify(var5, var1, this.phoneUtil)) {
            var5.clearCountryCodeSource();
            var5.clearRawInput();
            var5.clearPreferredDomesticCarrierCode();
            PhoneNumberMatch var11 = new PhoneNumberMatch(var2, var1.toString(), var5);
            return var11;
         } else {
            return null;
         }
      } catch (NumberParseException var6) {
         var10001 = false;
         return null;
      }
   }

   private static CharSequence trimAfterFirstMatch(Pattern var0, CharSequence var1) {
      Matcher var2 = var0.matcher(var1);
      CharSequence var3 = var1;
      if (var2.find()) {
         var3 = var1.subSequence(0, var2.start());
      }

      return var3;
   }

   public boolean hasNext() {
      if (this.state == PhoneNumberMatcher.State.NOT_READY) {
         PhoneNumberMatch var1 = this.find(this.searchIndex);
         this.lastMatch = var1;
         if (var1 == null) {
            this.state = PhoneNumberMatcher.State.DONE;
         } else {
            this.searchIndex = var1.end();
            this.state = PhoneNumberMatcher.State.READY;
         }
      }

      return this.state == PhoneNumberMatcher.State.READY;
   }

   public PhoneNumberMatch next() {
      if (this.hasNext()) {
         PhoneNumberMatch var1 = this.lastMatch;
         this.lastMatch = null;
         this.state = PhoneNumberMatcher.State.NOT_READY;
         return var1;
      } else {
         throw new NoSuchElementException();
      }
   }

   public void remove() {
      throw new UnsupportedOperationException();
   }

   interface NumberGroupingChecker {
      boolean checkGroups(PhoneNumberUtil var1, Phonenumber.PhoneNumber var2, StringBuilder var3, String[] var4);
   }

   private static enum State {
      DONE,
      NOT_READY,
      READY;

      static {
         PhoneNumberMatcher.State var0 = new PhoneNumberMatcher.State("DONE", 2);
         DONE = var0;
      }
   }
}
