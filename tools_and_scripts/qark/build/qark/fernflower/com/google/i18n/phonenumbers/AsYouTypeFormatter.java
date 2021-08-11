package com.google.i18n.phonenumbers;

import com.google.i18n.phonenumbers.internal.RegexCache;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AsYouTypeFormatter {
   private static final Pattern DIGIT_PATTERN = Pattern.compile(" ");
   private static final String DIGIT_PLACEHOLDER = " ";
   private static final Pattern ELIGIBLE_FORMAT_PATTERN = Pattern.compile("[-x‐-―−ー－-／  \u00ad\u200b\u2060　()（）［］.\\[\\]/~⁓∼～]*(\\$\\d[-x‐-―−ー－-／  \u00ad\u200b\u2060　()（）［］.\\[\\]/~⁓∼～]*)+");
   private static final Phonemetadata.PhoneMetadata EMPTY_METADATA = (new Phonemetadata.PhoneMetadata()).setInternationalPrefix("NA");
   private static final int MIN_LEADING_DIGITS_LENGTH = 3;
   private static final Pattern NATIONAL_PREFIX_SEPARATORS_PATTERN = Pattern.compile("[- ]");
   private static final char SEPARATOR_BEFORE_NATIONAL_NUMBER = ' ';
   private boolean ableToFormat = true;
   private StringBuilder accruedInput = new StringBuilder();
   private StringBuilder accruedInputWithoutFormatting = new StringBuilder();
   private String currentFormattingPattern = "";
   private Phonemetadata.PhoneMetadata currentMetadata;
   private String currentOutput = "";
   private String defaultCountry;
   private Phonemetadata.PhoneMetadata defaultMetadata;
   private String extractedNationalPrefix = "";
   private StringBuilder formattingTemplate = new StringBuilder();
   private boolean inputHasFormatting = false;
   private boolean isCompleteNumber = false;
   private boolean isExpectingCountryCallingCode = false;
   private int lastMatchPosition = 0;
   private StringBuilder nationalNumber = new StringBuilder();
   private int originalPosition = 0;
   private final PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
   private int positionToRemember = 0;
   private List possibleFormats = new ArrayList();
   private StringBuilder prefixBeforeNationalNumber = new StringBuilder();
   private RegexCache regexCache = new RegexCache(64);
   private boolean shouldAddSpaceAfterNationalPrefix = false;

   AsYouTypeFormatter(String var1) {
      this.defaultCountry = var1;
      Phonemetadata.PhoneMetadata var2 = this.getMetadataForRegion(var1);
      this.currentMetadata = var2;
      this.defaultMetadata = var2;
   }

   private boolean ableToExtractLongerNdd() {
      if (this.extractedNationalPrefix.length() > 0) {
         this.nationalNumber.insert(0, this.extractedNationalPrefix);
         int var1 = this.prefixBeforeNationalNumber.lastIndexOf(this.extractedNationalPrefix);
         this.prefixBeforeNationalNumber.setLength(var1);
      }

      return this.extractedNationalPrefix.equals(this.removeNationalPrefixFromNationalNumber()) ^ true;
   }

   private String appendNationalNumber(String var1) {
      int var2 = this.prefixBeforeNationalNumber.length();
      StringBuilder var3;
      if (this.shouldAddSpaceAfterNationalPrefix && var2 > 0 && this.prefixBeforeNationalNumber.charAt(var2 - 1) != ' ') {
         var3 = new StringBuilder();
         var3.append(new String(this.prefixBeforeNationalNumber));
         var3.append(' ');
         var3.append(var1);
         return var3.toString();
      } else {
         var3 = new StringBuilder();
         var3.append(this.prefixBeforeNationalNumber);
         var3.append(var1);
         return var3.toString();
      }
   }

   private String attemptToChooseFormattingPattern() {
      if (this.nationalNumber.length() >= 3) {
         this.getAvailableFormats(this.nationalNumber.toString());
         String var1 = this.attemptToFormatAccruedDigits();
         if (var1.length() > 0) {
            return var1;
         } else {
            return this.maybeCreateNewTemplate() ? this.inputAccruedNationalNumber() : this.accruedInput.toString();
         }
      } else {
         return this.appendNationalNumber(this.nationalNumber.toString());
      }
   }

   private String attemptToChoosePatternWithPrefixExtracted() {
      this.ableToFormat = true;
      this.isExpectingCountryCallingCode = false;
      this.possibleFormats.clear();
      this.lastMatchPosition = 0;
      this.formattingTemplate.setLength(0);
      this.currentFormattingPattern = "";
      return this.attemptToChooseFormattingPattern();
   }

   private boolean attemptToExtractCountryCallingCode() {
      if (this.nationalNumber.length() == 0) {
         return false;
      } else {
         StringBuilder var2 = new StringBuilder();
         int var1 = this.phoneUtil.extractCountryCode(this.nationalNumber, var2);
         if (var1 == 0) {
            return false;
         } else {
            this.nationalNumber.setLength(0);
            this.nationalNumber.append(var2);
            String var4 = this.phoneUtil.getRegionCodeForCountryCode(var1);
            if ("001".equals(var4)) {
               this.currentMetadata = this.phoneUtil.getMetadataForNonGeographicalRegion(var1);
            } else if (!var4.equals(this.defaultCountry)) {
               this.currentMetadata = this.getMetadataForRegion(var4);
            }

            var4 = Integer.toString(var1);
            StringBuilder var3 = this.prefixBeforeNationalNumber;
            var3.append(var4);
            var3.append(' ');
            this.extractedNationalPrefix = "";
            return true;
         }
      }
   }

   private boolean attemptToExtractIdd() {
      RegexCache var2 = this.regexCache;
      StringBuilder var3 = new StringBuilder();
      var3.append("\\+|");
      var3.append(this.currentMetadata.getInternationalPrefix());
      Matcher var4 = var2.getPatternForRegex(var3.toString()).matcher(this.accruedInputWithoutFormatting);
      if (var4.lookingAt()) {
         this.isCompleteNumber = true;
         int var1 = var4.end();
         this.nationalNumber.setLength(0);
         this.nationalNumber.append(this.accruedInputWithoutFormatting.substring(var1));
         this.prefixBeforeNationalNumber.setLength(0);
         this.prefixBeforeNationalNumber.append(this.accruedInputWithoutFormatting.substring(0, var1));
         if (this.accruedInputWithoutFormatting.charAt(0) != '+') {
            this.prefixBeforeNationalNumber.append(' ');
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean createFormattingTemplate(Phonemetadata.NumberFormat var1) {
      String var2 = var1.getPattern();
      this.formattingTemplate.setLength(0);
      String var3 = this.getFormattingTemplate(var2, var1.getFormat());
      if (var3.length() > 0) {
         this.formattingTemplate.append(var3);
         return true;
      } else {
         return false;
      }
   }

   private void getAvailableFormats(String var1) {
      boolean var2;
      if (this.isCompleteNumber && this.extractedNationalPrefix.length() == 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      List var3;
      if (var2 && this.currentMetadata.intlNumberFormatSize() > 0) {
         var3 = this.currentMetadata.intlNumberFormats();
      } else {
         var3 = this.currentMetadata.numberFormats();
      }

      Iterator var5 = var3.iterator();

      while(true) {
         Phonemetadata.NumberFormat var4;
         do {
            do {
               if (!var5.hasNext()) {
                  this.narrowDownPossibleFormats(var1);
                  return;
               }

               var4 = (Phonemetadata.NumberFormat)var5.next();
            } while(this.extractedNationalPrefix.length() > 0 && PhoneNumberUtil.formattingRuleHasFirstGroupOnly(var4.getNationalPrefixFormattingRule()) && !var4.getNationalPrefixOptionalWhenFormatting() && !var4.hasDomesticCarrierCodeFormattingRule());
         } while(this.extractedNationalPrefix.length() == 0 && !this.isCompleteNumber && !PhoneNumberUtil.formattingRuleHasFirstGroupOnly(var4.getNationalPrefixFormattingRule()) && !var4.getNationalPrefixOptionalWhenFormatting());

         if (ELIGIBLE_FORMAT_PATTERN.matcher(var4.getFormat()).matches()) {
            this.possibleFormats.add(var4);
         }
      }
   }

   private String getFormattingTemplate(String var1, String var2) {
      Matcher var3 = this.regexCache.getPatternForRegex(var1).matcher("999999999999999");
      var3.find();
      String var4 = var3.group();
      return var4.length() < this.nationalNumber.length() ? "" : var4.replaceAll(var1, var2).replaceAll("9", " ");
   }

   private Phonemetadata.PhoneMetadata getMetadataForRegion(String var1) {
      int var2 = this.phoneUtil.getCountryCodeForRegion(var1);
      var1 = this.phoneUtil.getRegionCodeForCountryCode(var2);
      Phonemetadata.PhoneMetadata var3 = this.phoneUtil.getMetadataForRegion(var1);
      return var3 != null ? var3 : EMPTY_METADATA;
   }

   private String inputAccruedNationalNumber() {
      int var2 = this.nationalNumber.length();
      if (var2 <= 0) {
         return this.prefixBeforeNationalNumber.toString();
      } else {
         String var3 = "";

         for(int var1 = 0; var1 < var2; ++var1) {
            var3 = this.inputDigitHelper(this.nationalNumber.charAt(var1));
         }

         return this.ableToFormat ? this.appendNationalNumber(var3) : this.accruedInput.toString();
      }
   }

   private String inputDigitHelper(char var1) {
      Matcher var3 = DIGIT_PATTERN.matcher(this.formattingTemplate);
      if (var3.find(this.lastMatchPosition)) {
         String var4 = var3.replaceFirst(Character.toString(var1));
         this.formattingTemplate.replace(0, var4.length(), var4);
         int var2 = var3.start();
         this.lastMatchPosition = var2;
         return this.formattingTemplate.substring(0, var2 + 1);
      } else {
         if (this.possibleFormats.size() == 1) {
            this.ableToFormat = false;
         }

         this.currentFormattingPattern = "";
         return this.accruedInput.toString();
      }
   }

   private String inputDigitWithOptionToRememberPosition(char var1, boolean var2) {
      this.accruedInput.append(var1);
      if (var2) {
         this.originalPosition = this.accruedInput.length();
      }

      if (!this.isDigitOrLeadingPlusSign(var1)) {
         this.ableToFormat = false;
         this.inputHasFormatting = true;
      } else {
         var1 = this.normalizeAndAccrueDigitsAndPlusSign(var1, var2);
      }

      if (!this.ableToFormat) {
         if (this.inputHasFormatting) {
            return this.accruedInput.toString();
         } else {
            if (this.attemptToExtractIdd()) {
               if (this.attemptToExtractCountryCallingCode()) {
                  return this.attemptToChoosePatternWithPrefixExtracted();
               }
            } else if (this.ableToExtractLongerNdd()) {
               this.prefixBeforeNationalNumber.append(' ');
               return this.attemptToChoosePatternWithPrefixExtracted();
            }

            return this.accruedInput.toString();
         }
      } else {
         int var3 = this.accruedInputWithoutFormatting.length();
         if (var3 != 0 && var3 != 1 && var3 != 2) {
            if (var3 == 3) {
               if (!this.attemptToExtractIdd()) {
                  this.extractedNationalPrefix = this.removeNationalPrefixFromNationalNumber();
                  return this.attemptToChooseFormattingPattern();
               }

               this.isExpectingCountryCallingCode = true;
            }

            if (this.isExpectingCountryCallingCode) {
               if (this.attemptToExtractCountryCallingCode()) {
                  this.isExpectingCountryCallingCode = false;
               }

               StringBuilder var6 = new StringBuilder();
               var6.append(this.prefixBeforeNationalNumber);
               var6.append(this.nationalNumber.toString());
               return var6.toString();
            } else if (this.possibleFormats.size() > 0) {
               String var4 = this.inputDigitHelper(var1);
               String var5 = this.attemptToFormatAccruedDigits();
               if (var5.length() > 0) {
                  return var5;
               } else {
                  this.narrowDownPossibleFormats(this.nationalNumber.toString());
                  if (this.maybeCreateNewTemplate()) {
                     return this.inputAccruedNationalNumber();
                  } else {
                     return this.ableToFormat ? this.appendNationalNumber(var4) : this.accruedInput.toString();
                  }
               }
            } else {
               return this.attemptToChooseFormattingPattern();
            }
         } else {
            return this.accruedInput.toString();
         }
      }
   }

   private boolean isDigitOrLeadingPlusSign(char var1) {
      return Character.isDigit(var1) || this.accruedInput.length() == 1 && PhoneNumberUtil.PLUS_CHARS_PATTERN.matcher(Character.toString(var1)).matches();
   }

   private boolean isNanpaNumberWithNationalPrefix() {
      return this.currentMetadata.getCountryCode() == 1 && this.nationalNumber.charAt(0) == '1' && this.nationalNumber.charAt(1) != '0' && this.nationalNumber.charAt(1) != '1';
   }

   private boolean maybeCreateNewTemplate() {
      Iterator var1 = this.possibleFormats.iterator();

      while(var1.hasNext()) {
         Phonemetadata.NumberFormat var2 = (Phonemetadata.NumberFormat)var1.next();
         String var3 = var2.getPattern();
         if (this.currentFormattingPattern.equals(var3)) {
            return false;
         }

         if (this.createFormattingTemplate(var2)) {
            this.currentFormattingPattern = var3;
            this.shouldAddSpaceAfterNationalPrefix = NATIONAL_PREFIX_SEPARATORS_PATTERN.matcher(var2.getNationalPrefixFormattingRule()).find();
            this.lastMatchPosition = 0;
            return true;
         }

         var1.remove();
      }

      this.ableToFormat = false;
      return false;
   }

   private void narrowDownPossibleFormats(String var1) {
      int var2 = var1.length();
      Iterator var4 = this.possibleFormats.iterator();

      while(var4.hasNext()) {
         Phonemetadata.NumberFormat var5 = (Phonemetadata.NumberFormat)var4.next();
         if (var5.leadingDigitsPatternSize() != 0) {
            int var3 = Math.min(var2 - 3, var5.leadingDigitsPatternSize() - 1);
            if (!this.regexCache.getPatternForRegex(var5.getLeadingDigitsPattern(var3)).matcher(var1).lookingAt()) {
               var4.remove();
            }
         }
      }

   }

   private char normalizeAndAccrueDigitsAndPlusSign(char var1, boolean var2) {
      if (var1 == '+') {
         this.accruedInputWithoutFormatting.append(var1);
         var1 = var1;
      } else {
         var1 = Character.forDigit(Character.digit(var1, 10), 10);
         this.accruedInputWithoutFormatting.append(var1);
         this.nationalNumber.append(var1);
      }

      if (var2) {
         this.positionToRemember = this.accruedInputWithoutFormatting.length();
      }

      return var1;
   }

   private String removeNationalPrefixFromNationalNumber() {
      byte var2 = 0;
      int var1;
      if (this.isNanpaNumberWithNationalPrefix()) {
         var1 = 1;
         StringBuilder var3 = this.prefixBeforeNationalNumber;
         var3.append('1');
         var3.append(' ');
         this.isCompleteNumber = true;
      } else {
         var1 = var2;
         if (this.currentMetadata.hasNationalPrefixForParsing()) {
            Matcher var4 = this.regexCache.getPatternForRegex(this.currentMetadata.getNationalPrefixForParsing()).matcher(this.nationalNumber);
            var1 = var2;
            if (var4.lookingAt()) {
               var1 = var2;
               if (var4.end() > 0) {
                  this.isCompleteNumber = true;
                  var1 = var4.end();
                  this.prefixBeforeNationalNumber.append(this.nationalNumber.substring(0, var1));
               }
            }
         }
      }

      String var5 = this.nationalNumber.substring(0, var1);
      this.nationalNumber.delete(0, var1);
      return var5;
   }

   String attemptToFormatAccruedDigits() {
      Iterator var1 = this.possibleFormats.iterator();

      Phonemetadata.NumberFormat var2;
      Matcher var3;
      do {
         if (!var1.hasNext()) {
            return "";
         }

         var2 = (Phonemetadata.NumberFormat)var1.next();
         var3 = this.regexCache.getPatternForRegex(var2.getPattern()).matcher(this.nationalNumber);
      } while(!var3.matches());

      this.shouldAddSpaceAfterNationalPrefix = NATIONAL_PREFIX_SEPARATORS_PATTERN.matcher(var2.getNationalPrefixFormattingRule()).find();
      return this.appendNationalNumber(var3.replaceAll(var2.getFormat()));
   }

   public void clear() {
      this.currentOutput = "";
      this.accruedInput.setLength(0);
      this.accruedInputWithoutFormatting.setLength(0);
      this.formattingTemplate.setLength(0);
      this.lastMatchPosition = 0;
      this.currentFormattingPattern = "";
      this.prefixBeforeNationalNumber.setLength(0);
      this.extractedNationalPrefix = "";
      this.nationalNumber.setLength(0);
      this.ableToFormat = true;
      this.inputHasFormatting = false;
      this.positionToRemember = 0;
      this.originalPosition = 0;
      this.isCompleteNumber = false;
      this.isExpectingCountryCallingCode = false;
      this.possibleFormats.clear();
      this.shouldAddSpaceAfterNationalPrefix = false;
      if (!this.currentMetadata.equals(this.defaultMetadata)) {
         this.currentMetadata = this.getMetadataForRegion(this.defaultCountry);
      }

   }

   String getExtractedNationalPrefix() {
      return this.extractedNationalPrefix;
   }

   public int getRememberedPosition() {
      if (!this.ableToFormat) {
         return this.originalPosition;
      } else {
         int var2 = 0;

         int var1;
         int var3;
         for(var1 = 0; var2 < this.positionToRemember && var1 < this.currentOutput.length(); var2 = var3) {
            var3 = var2;
            if (this.accruedInputWithoutFormatting.charAt(var2) == this.currentOutput.charAt(var1)) {
               var3 = var2 + 1;
            }

            ++var1;
         }

         return var1;
      }
   }

   public String inputDigit(char var1) {
      String var2 = this.inputDigitWithOptionToRememberPosition(var1, false);
      this.currentOutput = var2;
      return var2;
   }

   public String inputDigitAndRememberPosition(char var1) {
      String var2 = this.inputDigitWithOptionToRememberPosition(var1, true);
      this.currentOutput = var2;
      return var2;
   }
}
