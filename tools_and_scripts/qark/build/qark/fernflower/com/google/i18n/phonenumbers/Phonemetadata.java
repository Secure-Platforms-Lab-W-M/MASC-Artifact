package com.google.i18n.phonenumbers;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.List;

public final class Phonemetadata {
   private Phonemetadata() {
   }

   public static class NumberFormat implements Externalizable {
      private static final long serialVersionUID = 1L;
      private String domesticCarrierCodeFormattingRule_ = "";
      private String format_ = "";
      private boolean hasDomesticCarrierCodeFormattingRule;
      private boolean hasFormat;
      private boolean hasNationalPrefixFormattingRule;
      private boolean hasNationalPrefixOptionalWhenFormatting;
      private boolean hasPattern;
      private List leadingDigitsPattern_ = new ArrayList();
      private String nationalPrefixFormattingRule_ = "";
      private boolean nationalPrefixOptionalWhenFormatting_ = false;
      private String pattern_ = "";

      public static Phonemetadata.NumberFormat.Builder newBuilder() {
         return new Phonemetadata.NumberFormat.Builder();
      }

      public Phonemetadata.NumberFormat addLeadingDigitsPattern(String var1) {
         if (var1 != null) {
            this.leadingDigitsPattern_.add(var1);
            return this;
         } else {
            throw null;
         }
      }

      public Phonemetadata.NumberFormat clearNationalPrefixFormattingRule() {
         this.hasNationalPrefixFormattingRule = false;
         this.nationalPrefixFormattingRule_ = "";
         return this;
      }

      public String getDomesticCarrierCodeFormattingRule() {
         return this.domesticCarrierCodeFormattingRule_;
      }

      public String getFormat() {
         return this.format_;
      }

      public String getLeadingDigitsPattern(int var1) {
         return (String)this.leadingDigitsPattern_.get(var1);
      }

      public String getNationalPrefixFormattingRule() {
         return this.nationalPrefixFormattingRule_;
      }

      public boolean getNationalPrefixOptionalWhenFormatting() {
         return this.nationalPrefixOptionalWhenFormatting_;
      }

      public String getPattern() {
         return this.pattern_;
      }

      public boolean hasDomesticCarrierCodeFormattingRule() {
         return this.hasDomesticCarrierCodeFormattingRule;
      }

      public boolean hasFormat() {
         return this.hasFormat;
      }

      public boolean hasNationalPrefixFormattingRule() {
         return this.hasNationalPrefixFormattingRule;
      }

      public boolean hasNationalPrefixOptionalWhenFormatting() {
         return this.hasNationalPrefixOptionalWhenFormatting;
      }

      public boolean hasPattern() {
         return this.hasPattern;
      }

      public List leadingDigitPatterns() {
         return this.leadingDigitsPattern_;
      }

      public int leadingDigitsPatternSize() {
         return this.leadingDigitsPattern_.size();
      }

      public void readExternal(ObjectInput var1) throws IOException {
         this.setPattern(var1.readUTF());
         this.setFormat(var1.readUTF());
         int var3 = var1.readInt();

         for(int var2 = 0; var2 < var3; ++var2) {
            this.leadingDigitsPattern_.add(var1.readUTF());
         }

         if (var1.readBoolean()) {
            this.setNationalPrefixFormattingRule(var1.readUTF());
         }

         if (var1.readBoolean()) {
            this.setDomesticCarrierCodeFormattingRule(var1.readUTF());
         }

         this.setNationalPrefixOptionalWhenFormatting(var1.readBoolean());
      }

      public Phonemetadata.NumberFormat setDomesticCarrierCodeFormattingRule(String var1) {
         this.hasDomesticCarrierCodeFormattingRule = true;
         this.domesticCarrierCodeFormattingRule_ = var1;
         return this;
      }

      public Phonemetadata.NumberFormat setFormat(String var1) {
         this.hasFormat = true;
         this.format_ = var1;
         return this;
      }

      public Phonemetadata.NumberFormat setNationalPrefixFormattingRule(String var1) {
         this.hasNationalPrefixFormattingRule = true;
         this.nationalPrefixFormattingRule_ = var1;
         return this;
      }

      public Phonemetadata.NumberFormat setNationalPrefixOptionalWhenFormatting(boolean var1) {
         this.hasNationalPrefixOptionalWhenFormatting = true;
         this.nationalPrefixOptionalWhenFormatting_ = var1;
         return this;
      }

      public Phonemetadata.NumberFormat setPattern(String var1) {
         this.hasPattern = true;
         this.pattern_ = var1;
         return this;
      }

      public void writeExternal(ObjectOutput var1) throws IOException {
         var1.writeUTF(this.pattern_);
         var1.writeUTF(this.format_);
         int var3 = this.leadingDigitsPatternSize();
         var1.writeInt(var3);

         for(int var2 = 0; var2 < var3; ++var2) {
            var1.writeUTF((String)this.leadingDigitsPattern_.get(var2));
         }

         var1.writeBoolean(this.hasNationalPrefixFormattingRule);
         if (this.hasNationalPrefixFormattingRule) {
            var1.writeUTF(this.nationalPrefixFormattingRule_);
         }

         var1.writeBoolean(this.hasDomesticCarrierCodeFormattingRule);
         if (this.hasDomesticCarrierCodeFormattingRule) {
            var1.writeUTF(this.domesticCarrierCodeFormattingRule_);
         }

         var1.writeBoolean(this.nationalPrefixOptionalWhenFormatting_);
      }

      public static final class Builder extends Phonemetadata.NumberFormat {
         public Phonemetadata.NumberFormat build() {
            return this;
         }

         public Phonemetadata.NumberFormat.Builder mergeFrom(Phonemetadata.NumberFormat var1) {
            if (var1.hasPattern()) {
               this.setPattern(var1.getPattern());
            }

            if (var1.hasFormat()) {
               this.setFormat(var1.getFormat());
            }

            for(int var2 = 0; var2 < var1.leadingDigitsPatternSize(); ++var2) {
               this.addLeadingDigitsPattern(var1.getLeadingDigitsPattern(var2));
            }

            if (var1.hasNationalPrefixFormattingRule()) {
               this.setNationalPrefixFormattingRule(var1.getNationalPrefixFormattingRule());
            }

            if (var1.hasDomesticCarrierCodeFormattingRule()) {
               this.setDomesticCarrierCodeFormattingRule(var1.getDomesticCarrierCodeFormattingRule());
            }

            if (var1.hasNationalPrefixOptionalWhenFormatting()) {
               this.setNationalPrefixOptionalWhenFormatting(var1.getNationalPrefixOptionalWhenFormatting());
            }

            return this;
         }
      }
   }

   public static class PhoneMetadata implements Externalizable {
      private static final long serialVersionUID = 1L;
      private Phonemetadata.PhoneNumberDesc carrierSpecific_ = null;
      private int countryCode_ = 0;
      private Phonemetadata.PhoneNumberDesc emergency_ = null;
      private Phonemetadata.PhoneNumberDesc fixedLine_ = null;
      private Phonemetadata.PhoneNumberDesc generalDesc_ = null;
      private boolean hasCarrierSpecific;
      private boolean hasCountryCode;
      private boolean hasEmergency;
      private boolean hasFixedLine;
      private boolean hasGeneralDesc;
      private boolean hasId;
      private boolean hasInternationalPrefix;
      private boolean hasLeadingDigits;
      private boolean hasLeadingZeroPossible;
      private boolean hasMainCountryForCode;
      private boolean hasMobile;
      private boolean hasMobileNumberPortableRegion;
      private boolean hasNationalPrefix;
      private boolean hasNationalPrefixForParsing;
      private boolean hasNationalPrefixTransformRule;
      private boolean hasNoInternationalDialling;
      private boolean hasPager;
      private boolean hasPersonalNumber;
      private boolean hasPreferredExtnPrefix;
      private boolean hasPreferredInternationalPrefix;
      private boolean hasPremiumRate;
      private boolean hasSameMobileAndFixedLinePattern;
      private boolean hasSharedCost;
      private boolean hasShortCode;
      private boolean hasSmsServices;
      private boolean hasStandardRate;
      private boolean hasTollFree;
      private boolean hasUan;
      private boolean hasVoicemail;
      private boolean hasVoip;
      private String id_ = "";
      private String internationalPrefix_ = "";
      private List intlNumberFormat_ = new ArrayList();
      private String leadingDigits_ = "";
      private boolean leadingZeroPossible_ = false;
      private boolean mainCountryForCode_ = false;
      private boolean mobileNumberPortableRegion_ = false;
      private Phonemetadata.PhoneNumberDesc mobile_ = null;
      private String nationalPrefixForParsing_ = "";
      private String nationalPrefixTransformRule_ = "";
      private String nationalPrefix_ = "";
      private Phonemetadata.PhoneNumberDesc noInternationalDialling_ = null;
      private List numberFormat_ = new ArrayList();
      private Phonemetadata.PhoneNumberDesc pager_ = null;
      private Phonemetadata.PhoneNumberDesc personalNumber_ = null;
      private String preferredExtnPrefix_ = "";
      private String preferredInternationalPrefix_ = "";
      private Phonemetadata.PhoneNumberDesc premiumRate_ = null;
      private boolean sameMobileAndFixedLinePattern_ = false;
      private Phonemetadata.PhoneNumberDesc sharedCost_ = null;
      private Phonemetadata.PhoneNumberDesc shortCode_ = null;
      private Phonemetadata.PhoneNumberDesc smsServices_ = null;
      private Phonemetadata.PhoneNumberDesc standardRate_ = null;
      private Phonemetadata.PhoneNumberDesc tollFree_ = null;
      private Phonemetadata.PhoneNumberDesc uan_ = null;
      private Phonemetadata.PhoneNumberDesc voicemail_ = null;
      private Phonemetadata.PhoneNumberDesc voip_ = null;

      public static Phonemetadata.PhoneMetadata.Builder newBuilder() {
         return new Phonemetadata.PhoneMetadata.Builder();
      }

      public Phonemetadata.PhoneMetadata addIntlNumberFormat(Phonemetadata.NumberFormat var1) {
         if (var1 != null) {
            this.intlNumberFormat_.add(var1);
            return this;
         } else {
            throw null;
         }
      }

      public Phonemetadata.PhoneMetadata addNumberFormat(Phonemetadata.NumberFormat var1) {
         if (var1 != null) {
            this.numberFormat_.add(var1);
            return this;
         } else {
            throw null;
         }
      }

      public Phonemetadata.PhoneMetadata clearIntlNumberFormat() {
         this.intlNumberFormat_.clear();
         return this;
      }

      public Phonemetadata.PhoneMetadata clearLeadingZeroPossible() {
         this.hasLeadingZeroPossible = false;
         this.leadingZeroPossible_ = false;
         return this;
      }

      public Phonemetadata.PhoneMetadata clearMainCountryForCode() {
         this.hasMainCountryForCode = false;
         this.mainCountryForCode_ = false;
         return this;
      }

      public Phonemetadata.PhoneMetadata clearMobileNumberPortableRegion() {
         this.hasMobileNumberPortableRegion = false;
         this.mobileNumberPortableRegion_ = false;
         return this;
      }

      public Phonemetadata.PhoneMetadata clearNationalPrefix() {
         this.hasNationalPrefix = false;
         this.nationalPrefix_ = "";
         return this;
      }

      public Phonemetadata.PhoneMetadata clearNationalPrefixTransformRule() {
         this.hasNationalPrefixTransformRule = false;
         this.nationalPrefixTransformRule_ = "";
         return this;
      }

      public Phonemetadata.PhoneMetadata clearPreferredExtnPrefix() {
         this.hasPreferredExtnPrefix = false;
         this.preferredExtnPrefix_ = "";
         return this;
      }

      public Phonemetadata.PhoneMetadata clearPreferredInternationalPrefix() {
         this.hasPreferredInternationalPrefix = false;
         this.preferredInternationalPrefix_ = "";
         return this;
      }

      public Phonemetadata.PhoneMetadata clearSameMobileAndFixedLinePattern() {
         this.hasSameMobileAndFixedLinePattern = false;
         this.sameMobileAndFixedLinePattern_ = false;
         return this;
      }

      public Phonemetadata.PhoneNumberDesc getCarrierSpecific() {
         return this.carrierSpecific_;
      }

      public int getCountryCode() {
         return this.countryCode_;
      }

      public Phonemetadata.PhoneNumberDesc getEmergency() {
         return this.emergency_;
      }

      public Phonemetadata.PhoneNumberDesc getFixedLine() {
         return this.fixedLine_;
      }

      public Phonemetadata.PhoneNumberDesc getGeneralDesc() {
         return this.generalDesc_;
      }

      public String getId() {
         return this.id_;
      }

      public String getInternationalPrefix() {
         return this.internationalPrefix_;
      }

      public Phonemetadata.NumberFormat getIntlNumberFormat(int var1) {
         return (Phonemetadata.NumberFormat)this.intlNumberFormat_.get(var1);
      }

      public String getLeadingDigits() {
         return this.leadingDigits_;
      }

      public boolean getMainCountryForCode() {
         return this.mainCountryForCode_;
      }

      public Phonemetadata.PhoneNumberDesc getMobile() {
         return this.mobile_;
      }

      public String getNationalPrefix() {
         return this.nationalPrefix_;
      }

      public String getNationalPrefixForParsing() {
         return this.nationalPrefixForParsing_;
      }

      public String getNationalPrefixTransformRule() {
         return this.nationalPrefixTransformRule_;
      }

      public Phonemetadata.PhoneNumberDesc getNoInternationalDialling() {
         return this.noInternationalDialling_;
      }

      public Phonemetadata.NumberFormat getNumberFormat(int var1) {
         return (Phonemetadata.NumberFormat)this.numberFormat_.get(var1);
      }

      public Phonemetadata.PhoneNumberDesc getPager() {
         return this.pager_;
      }

      public Phonemetadata.PhoneNumberDesc getPersonalNumber() {
         return this.personalNumber_;
      }

      public String getPreferredExtnPrefix() {
         return this.preferredExtnPrefix_;
      }

      public String getPreferredInternationalPrefix() {
         return this.preferredInternationalPrefix_;
      }

      public Phonemetadata.PhoneNumberDesc getPremiumRate() {
         return this.premiumRate_;
      }

      public boolean getSameMobileAndFixedLinePattern() {
         return this.sameMobileAndFixedLinePattern_;
      }

      public Phonemetadata.PhoneNumberDesc getSharedCost() {
         return this.sharedCost_;
      }

      public Phonemetadata.PhoneNumberDesc getShortCode() {
         return this.shortCode_;
      }

      public Phonemetadata.PhoneNumberDesc getSmsServices() {
         return this.smsServices_;
      }

      public Phonemetadata.PhoneNumberDesc getStandardRate() {
         return this.standardRate_;
      }

      public Phonemetadata.PhoneNumberDesc getTollFree() {
         return this.tollFree_;
      }

      public Phonemetadata.PhoneNumberDesc getUan() {
         return this.uan_;
      }

      public Phonemetadata.PhoneNumberDesc getVoicemail() {
         return this.voicemail_;
      }

      public Phonemetadata.PhoneNumberDesc getVoip() {
         return this.voip_;
      }

      public boolean hasCarrierSpecific() {
         return this.hasCarrierSpecific;
      }

      public boolean hasCountryCode() {
         return this.hasCountryCode;
      }

      public boolean hasEmergency() {
         return this.hasEmergency;
      }

      public boolean hasFixedLine() {
         return this.hasFixedLine;
      }

      public boolean hasGeneralDesc() {
         return this.hasGeneralDesc;
      }

      public boolean hasId() {
         return this.hasId;
      }

      public boolean hasInternationalPrefix() {
         return this.hasInternationalPrefix;
      }

      public boolean hasLeadingDigits() {
         return this.hasLeadingDigits;
      }

      public boolean hasLeadingZeroPossible() {
         return this.hasLeadingZeroPossible;
      }

      public boolean hasMainCountryForCode() {
         return this.hasMainCountryForCode;
      }

      public boolean hasMobile() {
         return this.hasMobile;
      }

      public boolean hasMobileNumberPortableRegion() {
         return this.hasMobileNumberPortableRegion;
      }

      public boolean hasNationalPrefix() {
         return this.hasNationalPrefix;
      }

      public boolean hasNationalPrefixForParsing() {
         return this.hasNationalPrefixForParsing;
      }

      public boolean hasNationalPrefixTransformRule() {
         return this.hasNationalPrefixTransformRule;
      }

      public boolean hasNoInternationalDialling() {
         return this.hasNoInternationalDialling;
      }

      public boolean hasPager() {
         return this.hasPager;
      }

      public boolean hasPersonalNumber() {
         return this.hasPersonalNumber;
      }

      public boolean hasPreferredExtnPrefix() {
         return this.hasPreferredExtnPrefix;
      }

      public boolean hasPreferredInternationalPrefix() {
         return this.hasPreferredInternationalPrefix;
      }

      public boolean hasPremiumRate() {
         return this.hasPremiumRate;
      }

      public boolean hasSameMobileAndFixedLinePattern() {
         return this.hasSameMobileAndFixedLinePattern;
      }

      public boolean hasSharedCost() {
         return this.hasSharedCost;
      }

      public boolean hasShortCode() {
         return this.hasShortCode;
      }

      public boolean hasSmsServices() {
         return this.hasSmsServices;
      }

      public boolean hasStandardRate() {
         return this.hasStandardRate;
      }

      public boolean hasTollFree() {
         return this.hasTollFree;
      }

      public boolean hasUan() {
         return this.hasUan;
      }

      public boolean hasVoicemail() {
         return this.hasVoicemail;
      }

      public boolean hasVoip() {
         return this.hasVoip;
      }

      public int intlNumberFormatSize() {
         return this.intlNumberFormat_.size();
      }

      public List intlNumberFormats() {
         return this.intlNumberFormat_;
      }

      public boolean isLeadingZeroPossible() {
         return this.leadingZeroPossible_;
      }

      public boolean isMainCountryForCode() {
         return this.mainCountryForCode_;
      }

      public boolean isMobileNumberPortableRegion() {
         return this.mobileNumberPortableRegion_;
      }

      public int numberFormatSize() {
         return this.numberFormat_.size();
      }

      public List numberFormats() {
         return this.numberFormat_;
      }

      public void readExternal(ObjectInput var1) throws IOException {
         Phonemetadata.PhoneNumberDesc var4;
         if (var1.readBoolean()) {
            var4 = new Phonemetadata.PhoneNumberDesc();
            var4.readExternal(var1);
            this.setGeneralDesc(var4);
         }

         if (var1.readBoolean()) {
            var4 = new Phonemetadata.PhoneNumberDesc();
            var4.readExternal(var1);
            this.setFixedLine(var4);
         }

         if (var1.readBoolean()) {
            var4 = new Phonemetadata.PhoneNumberDesc();
            var4.readExternal(var1);
            this.setMobile(var4);
         }

         if (var1.readBoolean()) {
            var4 = new Phonemetadata.PhoneNumberDesc();
            var4.readExternal(var1);
            this.setTollFree(var4);
         }

         if (var1.readBoolean()) {
            var4 = new Phonemetadata.PhoneNumberDesc();
            var4.readExternal(var1);
            this.setPremiumRate(var4);
         }

         if (var1.readBoolean()) {
            var4 = new Phonemetadata.PhoneNumberDesc();
            var4.readExternal(var1);
            this.setSharedCost(var4);
         }

         if (var1.readBoolean()) {
            var4 = new Phonemetadata.PhoneNumberDesc();
            var4.readExternal(var1);
            this.setPersonalNumber(var4);
         }

         if (var1.readBoolean()) {
            var4 = new Phonemetadata.PhoneNumberDesc();
            var4.readExternal(var1);
            this.setVoip(var4);
         }

         if (var1.readBoolean()) {
            var4 = new Phonemetadata.PhoneNumberDesc();
            var4.readExternal(var1);
            this.setPager(var4);
         }

         if (var1.readBoolean()) {
            var4 = new Phonemetadata.PhoneNumberDesc();
            var4.readExternal(var1);
            this.setUan(var4);
         }

         if (var1.readBoolean()) {
            var4 = new Phonemetadata.PhoneNumberDesc();
            var4.readExternal(var1);
            this.setEmergency(var4);
         }

         if (var1.readBoolean()) {
            var4 = new Phonemetadata.PhoneNumberDesc();
            var4.readExternal(var1);
            this.setVoicemail(var4);
         }

         if (var1.readBoolean()) {
            var4 = new Phonemetadata.PhoneNumberDesc();
            var4.readExternal(var1);
            this.setShortCode(var4);
         }

         if (var1.readBoolean()) {
            var4 = new Phonemetadata.PhoneNumberDesc();
            var4.readExternal(var1);
            this.setStandardRate(var4);
         }

         if (var1.readBoolean()) {
            var4 = new Phonemetadata.PhoneNumberDesc();
            var4.readExternal(var1);
            this.setCarrierSpecific(var4);
         }

         if (var1.readBoolean()) {
            var4 = new Phonemetadata.PhoneNumberDesc();
            var4.readExternal(var1);
            this.setSmsServices(var4);
         }

         if (var1.readBoolean()) {
            var4 = new Phonemetadata.PhoneNumberDesc();
            var4.readExternal(var1);
            this.setNoInternationalDialling(var4);
         }

         this.setId(var1.readUTF());
         this.setCountryCode(var1.readInt());
         this.setInternationalPrefix(var1.readUTF());
         if (var1.readBoolean()) {
            this.setPreferredInternationalPrefix(var1.readUTF());
         }

         if (var1.readBoolean()) {
            this.setNationalPrefix(var1.readUTF());
         }

         if (var1.readBoolean()) {
            this.setPreferredExtnPrefix(var1.readUTF());
         }

         if (var1.readBoolean()) {
            this.setNationalPrefixForParsing(var1.readUTF());
         }

         if (var1.readBoolean()) {
            this.setNationalPrefixTransformRule(var1.readUTF());
         }

         this.setSameMobileAndFixedLinePattern(var1.readBoolean());
         int var3 = var1.readInt();

         int var2;
         Phonemetadata.NumberFormat var5;
         for(var2 = 0; var2 < var3; ++var2) {
            var5 = new Phonemetadata.NumberFormat();
            var5.readExternal(var1);
            this.numberFormat_.add(var5);
         }

         var3 = var1.readInt();

         for(var2 = 0; var2 < var3; ++var2) {
            var5 = new Phonemetadata.NumberFormat();
            var5.readExternal(var1);
            this.intlNumberFormat_.add(var5);
         }

         this.setMainCountryForCode(var1.readBoolean());
         if (var1.readBoolean()) {
            this.setLeadingDigits(var1.readUTF());
         }

         this.setLeadingZeroPossible(var1.readBoolean());
         this.setMobileNumberPortableRegion(var1.readBoolean());
      }

      public Phonemetadata.PhoneMetadata setCarrierSpecific(Phonemetadata.PhoneNumberDesc var1) {
         if (var1 != null) {
            this.hasCarrierSpecific = true;
            this.carrierSpecific_ = var1;
            return this;
         } else {
            throw null;
         }
      }

      public Phonemetadata.PhoneMetadata setCountryCode(int var1) {
         this.hasCountryCode = true;
         this.countryCode_ = var1;
         return this;
      }

      public Phonemetadata.PhoneMetadata setEmergency(Phonemetadata.PhoneNumberDesc var1) {
         if (var1 != null) {
            this.hasEmergency = true;
            this.emergency_ = var1;
            return this;
         } else {
            throw null;
         }
      }

      public Phonemetadata.PhoneMetadata setFixedLine(Phonemetadata.PhoneNumberDesc var1) {
         if (var1 != null) {
            this.hasFixedLine = true;
            this.fixedLine_ = var1;
            return this;
         } else {
            throw null;
         }
      }

      public Phonemetadata.PhoneMetadata setGeneralDesc(Phonemetadata.PhoneNumberDesc var1) {
         if (var1 != null) {
            this.hasGeneralDesc = true;
            this.generalDesc_ = var1;
            return this;
         } else {
            throw null;
         }
      }

      public Phonemetadata.PhoneMetadata setId(String var1) {
         this.hasId = true;
         this.id_ = var1;
         return this;
      }

      public Phonemetadata.PhoneMetadata setInternationalPrefix(String var1) {
         this.hasInternationalPrefix = true;
         this.internationalPrefix_ = var1;
         return this;
      }

      public Phonemetadata.PhoneMetadata setLeadingDigits(String var1) {
         this.hasLeadingDigits = true;
         this.leadingDigits_ = var1;
         return this;
      }

      public Phonemetadata.PhoneMetadata setLeadingZeroPossible(boolean var1) {
         this.hasLeadingZeroPossible = true;
         this.leadingZeroPossible_ = var1;
         return this;
      }

      public Phonemetadata.PhoneMetadata setMainCountryForCode(boolean var1) {
         this.hasMainCountryForCode = true;
         this.mainCountryForCode_ = var1;
         return this;
      }

      public Phonemetadata.PhoneMetadata setMobile(Phonemetadata.PhoneNumberDesc var1) {
         if (var1 != null) {
            this.hasMobile = true;
            this.mobile_ = var1;
            return this;
         } else {
            throw null;
         }
      }

      public Phonemetadata.PhoneMetadata setMobileNumberPortableRegion(boolean var1) {
         this.hasMobileNumberPortableRegion = true;
         this.mobileNumberPortableRegion_ = var1;
         return this;
      }

      public Phonemetadata.PhoneMetadata setNationalPrefix(String var1) {
         this.hasNationalPrefix = true;
         this.nationalPrefix_ = var1;
         return this;
      }

      public Phonemetadata.PhoneMetadata setNationalPrefixForParsing(String var1) {
         this.hasNationalPrefixForParsing = true;
         this.nationalPrefixForParsing_ = var1;
         return this;
      }

      public Phonemetadata.PhoneMetadata setNationalPrefixTransformRule(String var1) {
         this.hasNationalPrefixTransformRule = true;
         this.nationalPrefixTransformRule_ = var1;
         return this;
      }

      public Phonemetadata.PhoneMetadata setNoInternationalDialling(Phonemetadata.PhoneNumberDesc var1) {
         if (var1 != null) {
            this.hasNoInternationalDialling = true;
            this.noInternationalDialling_ = var1;
            return this;
         } else {
            throw null;
         }
      }

      public Phonemetadata.PhoneMetadata setPager(Phonemetadata.PhoneNumberDesc var1) {
         if (var1 != null) {
            this.hasPager = true;
            this.pager_ = var1;
            return this;
         } else {
            throw null;
         }
      }

      public Phonemetadata.PhoneMetadata setPersonalNumber(Phonemetadata.PhoneNumberDesc var1) {
         if (var1 != null) {
            this.hasPersonalNumber = true;
            this.personalNumber_ = var1;
            return this;
         } else {
            throw null;
         }
      }

      public Phonemetadata.PhoneMetadata setPreferredExtnPrefix(String var1) {
         this.hasPreferredExtnPrefix = true;
         this.preferredExtnPrefix_ = var1;
         return this;
      }

      public Phonemetadata.PhoneMetadata setPreferredInternationalPrefix(String var1) {
         this.hasPreferredInternationalPrefix = true;
         this.preferredInternationalPrefix_ = var1;
         return this;
      }

      public Phonemetadata.PhoneMetadata setPremiumRate(Phonemetadata.PhoneNumberDesc var1) {
         if (var1 != null) {
            this.hasPremiumRate = true;
            this.premiumRate_ = var1;
            return this;
         } else {
            throw null;
         }
      }

      public Phonemetadata.PhoneMetadata setSameMobileAndFixedLinePattern(boolean var1) {
         this.hasSameMobileAndFixedLinePattern = true;
         this.sameMobileAndFixedLinePattern_ = var1;
         return this;
      }

      public Phonemetadata.PhoneMetadata setSharedCost(Phonemetadata.PhoneNumberDesc var1) {
         if (var1 != null) {
            this.hasSharedCost = true;
            this.sharedCost_ = var1;
            return this;
         } else {
            throw null;
         }
      }

      public Phonemetadata.PhoneMetadata setShortCode(Phonemetadata.PhoneNumberDesc var1) {
         if (var1 != null) {
            this.hasShortCode = true;
            this.shortCode_ = var1;
            return this;
         } else {
            throw null;
         }
      }

      public Phonemetadata.PhoneMetadata setSmsServices(Phonemetadata.PhoneNumberDesc var1) {
         if (var1 != null) {
            this.hasSmsServices = true;
            this.smsServices_ = var1;
            return this;
         } else {
            throw null;
         }
      }

      public Phonemetadata.PhoneMetadata setStandardRate(Phonemetadata.PhoneNumberDesc var1) {
         if (var1 != null) {
            this.hasStandardRate = true;
            this.standardRate_ = var1;
            return this;
         } else {
            throw null;
         }
      }

      public Phonemetadata.PhoneMetadata setTollFree(Phonemetadata.PhoneNumberDesc var1) {
         if (var1 != null) {
            this.hasTollFree = true;
            this.tollFree_ = var1;
            return this;
         } else {
            throw null;
         }
      }

      public Phonemetadata.PhoneMetadata setUan(Phonemetadata.PhoneNumberDesc var1) {
         if (var1 != null) {
            this.hasUan = true;
            this.uan_ = var1;
            return this;
         } else {
            throw null;
         }
      }

      public Phonemetadata.PhoneMetadata setVoicemail(Phonemetadata.PhoneNumberDesc var1) {
         if (var1 != null) {
            this.hasVoicemail = true;
            this.voicemail_ = var1;
            return this;
         } else {
            throw null;
         }
      }

      public Phonemetadata.PhoneMetadata setVoip(Phonemetadata.PhoneNumberDesc var1) {
         if (var1 != null) {
            this.hasVoip = true;
            this.voip_ = var1;
            return this;
         } else {
            throw null;
         }
      }

      public void writeExternal(ObjectOutput var1) throws IOException {
         var1.writeBoolean(this.hasGeneralDesc);
         if (this.hasGeneralDesc) {
            this.generalDesc_.writeExternal(var1);
         }

         var1.writeBoolean(this.hasFixedLine);
         if (this.hasFixedLine) {
            this.fixedLine_.writeExternal(var1);
         }

         var1.writeBoolean(this.hasMobile);
         if (this.hasMobile) {
            this.mobile_.writeExternal(var1);
         }

         var1.writeBoolean(this.hasTollFree);
         if (this.hasTollFree) {
            this.tollFree_.writeExternal(var1);
         }

         var1.writeBoolean(this.hasPremiumRate);
         if (this.hasPremiumRate) {
            this.premiumRate_.writeExternal(var1);
         }

         var1.writeBoolean(this.hasSharedCost);
         if (this.hasSharedCost) {
            this.sharedCost_.writeExternal(var1);
         }

         var1.writeBoolean(this.hasPersonalNumber);
         if (this.hasPersonalNumber) {
            this.personalNumber_.writeExternal(var1);
         }

         var1.writeBoolean(this.hasVoip);
         if (this.hasVoip) {
            this.voip_.writeExternal(var1);
         }

         var1.writeBoolean(this.hasPager);
         if (this.hasPager) {
            this.pager_.writeExternal(var1);
         }

         var1.writeBoolean(this.hasUan);
         if (this.hasUan) {
            this.uan_.writeExternal(var1);
         }

         var1.writeBoolean(this.hasEmergency);
         if (this.hasEmergency) {
            this.emergency_.writeExternal(var1);
         }

         var1.writeBoolean(this.hasVoicemail);
         if (this.hasVoicemail) {
            this.voicemail_.writeExternal(var1);
         }

         var1.writeBoolean(this.hasShortCode);
         if (this.hasShortCode) {
            this.shortCode_.writeExternal(var1);
         }

         var1.writeBoolean(this.hasStandardRate);
         if (this.hasStandardRate) {
            this.standardRate_.writeExternal(var1);
         }

         var1.writeBoolean(this.hasCarrierSpecific);
         if (this.hasCarrierSpecific) {
            this.carrierSpecific_.writeExternal(var1);
         }

         var1.writeBoolean(this.hasSmsServices);
         if (this.hasSmsServices) {
            this.smsServices_.writeExternal(var1);
         }

         var1.writeBoolean(this.hasNoInternationalDialling);
         if (this.hasNoInternationalDialling) {
            this.noInternationalDialling_.writeExternal(var1);
         }

         var1.writeUTF(this.id_);
         var1.writeInt(this.countryCode_);
         var1.writeUTF(this.internationalPrefix_);
         var1.writeBoolean(this.hasPreferredInternationalPrefix);
         if (this.hasPreferredInternationalPrefix) {
            var1.writeUTF(this.preferredInternationalPrefix_);
         }

         var1.writeBoolean(this.hasNationalPrefix);
         if (this.hasNationalPrefix) {
            var1.writeUTF(this.nationalPrefix_);
         }

         var1.writeBoolean(this.hasPreferredExtnPrefix);
         if (this.hasPreferredExtnPrefix) {
            var1.writeUTF(this.preferredExtnPrefix_);
         }

         var1.writeBoolean(this.hasNationalPrefixForParsing);
         if (this.hasNationalPrefixForParsing) {
            var1.writeUTF(this.nationalPrefixForParsing_);
         }

         var1.writeBoolean(this.hasNationalPrefixTransformRule);
         if (this.hasNationalPrefixTransformRule) {
            var1.writeUTF(this.nationalPrefixTransformRule_);
         }

         var1.writeBoolean(this.sameMobileAndFixedLinePattern_);
         int var3 = this.numberFormatSize();
         var1.writeInt(var3);

         int var2;
         for(var2 = 0; var2 < var3; ++var2) {
            ((Phonemetadata.NumberFormat)this.numberFormat_.get(var2)).writeExternal(var1);
         }

         var3 = this.intlNumberFormatSize();
         var1.writeInt(var3);

         for(var2 = 0; var2 < var3; ++var2) {
            ((Phonemetadata.NumberFormat)this.intlNumberFormat_.get(var2)).writeExternal(var1);
         }

         var1.writeBoolean(this.mainCountryForCode_);
         var1.writeBoolean(this.hasLeadingDigits);
         if (this.hasLeadingDigits) {
            var1.writeUTF(this.leadingDigits_);
         }

         var1.writeBoolean(this.leadingZeroPossible_);
         var1.writeBoolean(this.mobileNumberPortableRegion_);
      }

      public static final class Builder extends Phonemetadata.PhoneMetadata {
         public Phonemetadata.PhoneMetadata build() {
            return this;
         }
      }
   }

   public static class PhoneMetadataCollection implements Externalizable {
      private static final long serialVersionUID = 1L;
      private List metadata_ = new ArrayList();

      public static Phonemetadata.PhoneMetadataCollection.Builder newBuilder() {
         return new Phonemetadata.PhoneMetadataCollection.Builder();
      }

      public Phonemetadata.PhoneMetadataCollection addMetadata(Phonemetadata.PhoneMetadata var1) {
         if (var1 != null) {
            this.metadata_.add(var1);
            return this;
         } else {
            throw null;
         }
      }

      public Phonemetadata.PhoneMetadataCollection clear() {
         this.metadata_.clear();
         return this;
      }

      public int getMetadataCount() {
         return this.metadata_.size();
      }

      public List getMetadataList() {
         return this.metadata_;
      }

      public void readExternal(ObjectInput var1) throws IOException {
         int var3 = var1.readInt();

         for(int var2 = 0; var2 < var3; ++var2) {
            Phonemetadata.PhoneMetadata var4 = new Phonemetadata.PhoneMetadata();
            var4.readExternal(var1);
            this.metadata_.add(var4);
         }

      }

      public void writeExternal(ObjectOutput var1) throws IOException {
         int var3 = this.getMetadataCount();
         var1.writeInt(var3);

         for(int var2 = 0; var2 < var3; ++var2) {
            ((Phonemetadata.PhoneMetadata)this.metadata_.get(var2)).writeExternal(var1);
         }

      }

      public static final class Builder extends Phonemetadata.PhoneMetadataCollection {
         public Phonemetadata.PhoneMetadataCollection build() {
            return this;
         }
      }
   }

   public static class PhoneNumberDesc implements Externalizable {
      private static final long serialVersionUID = 1L;
      private String exampleNumber_ = "";
      private boolean hasExampleNumber;
      private boolean hasNationalNumberPattern;
      private String nationalNumberPattern_ = "";
      private List possibleLengthLocalOnly_ = new ArrayList();
      private List possibleLength_ = new ArrayList();

      public static Phonemetadata.PhoneNumberDesc.Builder newBuilder() {
         return new Phonemetadata.PhoneNumberDesc.Builder();
      }

      public Phonemetadata.PhoneNumberDesc addPossibleLength(int var1) {
         this.possibleLength_.add(var1);
         return this;
      }

      public Phonemetadata.PhoneNumberDesc addPossibleLengthLocalOnly(int var1) {
         this.possibleLengthLocalOnly_.add(var1);
         return this;
      }

      public Phonemetadata.PhoneNumberDesc clearExampleNumber() {
         this.hasExampleNumber = false;
         this.exampleNumber_ = "";
         return this;
      }

      public Phonemetadata.PhoneNumberDesc clearNationalNumberPattern() {
         this.hasNationalNumberPattern = false;
         this.nationalNumberPattern_ = "";
         return this;
      }

      public Phonemetadata.PhoneNumberDesc clearPossibleLength() {
         this.possibleLength_.clear();
         return this;
      }

      public Phonemetadata.PhoneNumberDesc clearPossibleLengthLocalOnly() {
         this.possibleLengthLocalOnly_.clear();
         return this;
      }

      public boolean exactlySameAs(Phonemetadata.PhoneNumberDesc var1) {
         return this.nationalNumberPattern_.equals(var1.nationalNumberPattern_) && this.possibleLength_.equals(var1.possibleLength_) && this.possibleLengthLocalOnly_.equals(var1.possibleLengthLocalOnly_) && this.exampleNumber_.equals(var1.exampleNumber_);
      }

      public String getExampleNumber() {
         return this.exampleNumber_;
      }

      public String getNationalNumberPattern() {
         return this.nationalNumberPattern_;
      }

      public int getPossibleLength(int var1) {
         return (Integer)this.possibleLength_.get(var1);
      }

      public int getPossibleLengthCount() {
         return this.possibleLength_.size();
      }

      public List getPossibleLengthList() {
         return this.possibleLength_;
      }

      public int getPossibleLengthLocalOnly(int var1) {
         return (Integer)this.possibleLengthLocalOnly_.get(var1);
      }

      public int getPossibleLengthLocalOnlyCount() {
         return this.possibleLengthLocalOnly_.size();
      }

      public List getPossibleLengthLocalOnlyList() {
         return this.possibleLengthLocalOnly_;
      }

      public boolean hasExampleNumber() {
         return this.hasExampleNumber;
      }

      public boolean hasNationalNumberPattern() {
         return this.hasNationalNumberPattern;
      }

      public void readExternal(ObjectInput var1) throws IOException {
         if (var1.readBoolean()) {
            this.setNationalNumberPattern(var1.readUTF());
         }

         int var3 = var1.readInt();

         int var2;
         for(var2 = 0; var2 < var3; ++var2) {
            this.possibleLength_.add(var1.readInt());
         }

         var3 = var1.readInt();

         for(var2 = 0; var2 < var3; ++var2) {
            this.possibleLengthLocalOnly_.add(var1.readInt());
         }

         if (var1.readBoolean()) {
            this.setExampleNumber(var1.readUTF());
         }

      }

      public Phonemetadata.PhoneNumberDesc setExampleNumber(String var1) {
         this.hasExampleNumber = true;
         this.exampleNumber_ = var1;
         return this;
      }

      public Phonemetadata.PhoneNumberDesc setNationalNumberPattern(String var1) {
         this.hasNationalNumberPattern = true;
         this.nationalNumberPattern_ = var1;
         return this;
      }

      public void writeExternal(ObjectOutput var1) throws IOException {
         var1.writeBoolean(this.hasNationalNumberPattern);
         if (this.hasNationalNumberPattern) {
            var1.writeUTF(this.nationalNumberPattern_);
         }

         int var3 = this.getPossibleLengthCount();
         var1.writeInt(var3);

         int var2;
         for(var2 = 0; var2 < var3; ++var2) {
            var1.writeInt((Integer)this.possibleLength_.get(var2));
         }

         var3 = this.getPossibleLengthLocalOnlyCount();
         var1.writeInt(var3);

         for(var2 = 0; var2 < var3; ++var2) {
            var1.writeInt((Integer)this.possibleLengthLocalOnly_.get(var2));
         }

         var1.writeBoolean(this.hasExampleNumber);
         if (this.hasExampleNumber) {
            var1.writeUTF(this.exampleNumber_);
         }

      }

      public static final class Builder extends Phonemetadata.PhoneNumberDesc {
         public Phonemetadata.PhoneNumberDesc build() {
            return this;
         }

         public Phonemetadata.PhoneNumberDesc.Builder mergeFrom(Phonemetadata.PhoneNumberDesc var1) {
            if (var1.hasNationalNumberPattern()) {
               this.setNationalNumberPattern(var1.getNationalNumberPattern());
            }

            int var2;
            for(var2 = 0; var2 < var1.getPossibleLengthCount(); ++var2) {
               this.addPossibleLength(var1.getPossibleLength(var2));
            }

            for(var2 = 0; var2 < var1.getPossibleLengthLocalOnlyCount(); ++var2) {
               this.addPossibleLengthLocalOnly(var1.getPossibleLengthLocalOnly(var2));
            }

            if (var1.hasExampleNumber()) {
               this.setExampleNumber(var1.getExampleNumber());
            }

            return this;
         }
      }
   }
}
