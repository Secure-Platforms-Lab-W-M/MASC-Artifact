package com.google.i18n.phonenumbers;

import java.io.Serializable;

public final class Phonenumber {
   private Phonenumber() {
   }

   public static class PhoneNumber implements Serializable {
      private static final long serialVersionUID = 1L;
      private Phonenumber.PhoneNumber.CountryCodeSource countryCodeSource_;
      private int countryCode_ = 0;
      private String extension_ = "";
      private boolean hasCountryCode;
      private boolean hasCountryCodeSource;
      private boolean hasExtension;
      private boolean hasItalianLeadingZero;
      private boolean hasNationalNumber;
      private boolean hasNumberOfLeadingZeros;
      private boolean hasPreferredDomesticCarrierCode;
      private boolean hasRawInput;
      private boolean italianLeadingZero_ = false;
      private long nationalNumber_ = 0L;
      private int numberOfLeadingZeros_ = 1;
      private String preferredDomesticCarrierCode_ = "";
      private String rawInput_ = "";

      public PhoneNumber() {
         this.countryCodeSource_ = Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED;
      }

      public final Phonenumber.PhoneNumber clear() {
         this.clearCountryCode();
         this.clearNationalNumber();
         this.clearExtension();
         this.clearItalianLeadingZero();
         this.clearNumberOfLeadingZeros();
         this.clearRawInput();
         this.clearCountryCodeSource();
         this.clearPreferredDomesticCarrierCode();
         return this;
      }

      public Phonenumber.PhoneNumber clearCountryCode() {
         this.hasCountryCode = false;
         this.countryCode_ = 0;
         return this;
      }

      public Phonenumber.PhoneNumber clearCountryCodeSource() {
         this.hasCountryCodeSource = false;
         this.countryCodeSource_ = Phonenumber.PhoneNumber.CountryCodeSource.UNSPECIFIED;
         return this;
      }

      public Phonenumber.PhoneNumber clearExtension() {
         this.hasExtension = false;
         this.extension_ = "";
         return this;
      }

      public Phonenumber.PhoneNumber clearItalianLeadingZero() {
         this.hasItalianLeadingZero = false;
         this.italianLeadingZero_ = false;
         return this;
      }

      public Phonenumber.PhoneNumber clearNationalNumber() {
         this.hasNationalNumber = false;
         this.nationalNumber_ = 0L;
         return this;
      }

      public Phonenumber.PhoneNumber clearNumberOfLeadingZeros() {
         this.hasNumberOfLeadingZeros = false;
         this.numberOfLeadingZeros_ = 1;
         return this;
      }

      public Phonenumber.PhoneNumber clearPreferredDomesticCarrierCode() {
         this.hasPreferredDomesticCarrierCode = false;
         this.preferredDomesticCarrierCode_ = "";
         return this;
      }

      public Phonenumber.PhoneNumber clearRawInput() {
         this.hasRawInput = false;
         this.rawInput_ = "";
         return this;
      }

      public boolean equals(Object var1) {
         return var1 instanceof Phonenumber.PhoneNumber && this.exactlySameAs((Phonenumber.PhoneNumber)var1);
      }

      public boolean exactlySameAs(Phonenumber.PhoneNumber var1) {
         if (var1 == null) {
            return false;
         } else if (this == var1) {
            return true;
         } else {
            return this.countryCode_ == var1.countryCode_ && this.nationalNumber_ == var1.nationalNumber_ && this.extension_.equals(var1.extension_) && this.italianLeadingZero_ == var1.italianLeadingZero_ && this.numberOfLeadingZeros_ == var1.numberOfLeadingZeros_ && this.rawInput_.equals(var1.rawInput_) && this.countryCodeSource_ == var1.countryCodeSource_ && this.preferredDomesticCarrierCode_.equals(var1.preferredDomesticCarrierCode_) && this.hasPreferredDomesticCarrierCode() == var1.hasPreferredDomesticCarrierCode();
         }
      }

      public int getCountryCode() {
         return this.countryCode_;
      }

      public Phonenumber.PhoneNumber.CountryCodeSource getCountryCodeSource() {
         return this.countryCodeSource_;
      }

      public String getExtension() {
         return this.extension_;
      }

      public long getNationalNumber() {
         return this.nationalNumber_;
      }

      public int getNumberOfLeadingZeros() {
         return this.numberOfLeadingZeros_;
      }

      public String getPreferredDomesticCarrierCode() {
         return this.preferredDomesticCarrierCode_;
      }

      public String getRawInput() {
         return this.rawInput_;
      }

      public boolean hasCountryCode() {
         return this.hasCountryCode;
      }

      public boolean hasCountryCodeSource() {
         return this.hasCountryCodeSource;
      }

      public boolean hasExtension() {
         return this.hasExtension;
      }

      public boolean hasItalianLeadingZero() {
         return this.hasItalianLeadingZero;
      }

      public boolean hasNationalNumber() {
         return this.hasNationalNumber;
      }

      public boolean hasNumberOfLeadingZeros() {
         return this.hasNumberOfLeadingZeros;
      }

      public boolean hasPreferredDomesticCarrierCode() {
         return this.hasPreferredDomesticCarrierCode;
      }

      public boolean hasRawInput() {
         return this.hasRawInput;
      }

      public int hashCode() {
         int var3 = this.getCountryCode();
         int var4 = Long.valueOf(this.getNationalNumber()).hashCode();
         int var5 = this.getExtension().hashCode();
         boolean var10 = this.isItalianLeadingZero();
         short var2 = 1231;
         short var1;
         if (var10) {
            var1 = 1231;
         } else {
            var1 = 1237;
         }

         int var6 = this.getNumberOfLeadingZeros();
         int var7 = this.getRawInput().hashCode();
         int var8 = this.getCountryCodeSource().hashCode();
         int var9 = this.getPreferredDomesticCarrierCode().hashCode();
         if (!this.hasPreferredDomesticCarrierCode()) {
            var2 = 1237;
         }

         return ((((((((41 * 53 + var3) * 53 + var4) * 53 + var5) * 53 + var1) * 53 + var6) * 53 + var7) * 53 + var8) * 53 + var9) * 53 + var2;
      }

      public boolean isItalianLeadingZero() {
         return this.italianLeadingZero_;
      }

      public Phonenumber.PhoneNumber mergeFrom(Phonenumber.PhoneNumber var1) {
         if (var1.hasCountryCode()) {
            this.setCountryCode(var1.getCountryCode());
         }

         if (var1.hasNationalNumber()) {
            this.setNationalNumber(var1.getNationalNumber());
         }

         if (var1.hasExtension()) {
            this.setExtension(var1.getExtension());
         }

         if (var1.hasItalianLeadingZero()) {
            this.setItalianLeadingZero(var1.isItalianLeadingZero());
         }

         if (var1.hasNumberOfLeadingZeros()) {
            this.setNumberOfLeadingZeros(var1.getNumberOfLeadingZeros());
         }

         if (var1.hasRawInput()) {
            this.setRawInput(var1.getRawInput());
         }

         if (var1.hasCountryCodeSource()) {
            this.setCountryCodeSource(var1.getCountryCodeSource());
         }

         if (var1.hasPreferredDomesticCarrierCode()) {
            this.setPreferredDomesticCarrierCode(var1.getPreferredDomesticCarrierCode());
         }

         return this;
      }

      public Phonenumber.PhoneNumber setCountryCode(int var1) {
         this.hasCountryCode = true;
         this.countryCode_ = var1;
         return this;
      }

      public Phonenumber.PhoneNumber setCountryCodeSource(Phonenumber.PhoneNumber.CountryCodeSource var1) {
         if (var1 != null) {
            this.hasCountryCodeSource = true;
            this.countryCodeSource_ = var1;
            return this;
         } else {
            throw null;
         }
      }

      public Phonenumber.PhoneNumber setExtension(String var1) {
         if (var1 != null) {
            this.hasExtension = true;
            this.extension_ = var1;
            return this;
         } else {
            throw null;
         }
      }

      public Phonenumber.PhoneNumber setItalianLeadingZero(boolean var1) {
         this.hasItalianLeadingZero = true;
         this.italianLeadingZero_ = var1;
         return this;
      }

      public Phonenumber.PhoneNumber setNationalNumber(long var1) {
         this.hasNationalNumber = true;
         this.nationalNumber_ = var1;
         return this;
      }

      public Phonenumber.PhoneNumber setNumberOfLeadingZeros(int var1) {
         this.hasNumberOfLeadingZeros = true;
         this.numberOfLeadingZeros_ = var1;
         return this;
      }

      public Phonenumber.PhoneNumber setPreferredDomesticCarrierCode(String var1) {
         if (var1 != null) {
            this.hasPreferredDomesticCarrierCode = true;
            this.preferredDomesticCarrierCode_ = var1;
            return this;
         } else {
            throw null;
         }
      }

      public Phonenumber.PhoneNumber setRawInput(String var1) {
         if (var1 != null) {
            this.hasRawInput = true;
            this.rawInput_ = var1;
            return this;
         } else {
            throw null;
         }
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append("Country Code: ");
         var1.append(this.countryCode_);
         var1.append(" National Number: ");
         var1.append(this.nationalNumber_);
         if (this.hasItalianLeadingZero() && this.isItalianLeadingZero()) {
            var1.append(" Leading Zero(s): true");
         }

         if (this.hasNumberOfLeadingZeros()) {
            var1.append(" Number of leading zeros: ");
            var1.append(this.numberOfLeadingZeros_);
         }

         if (this.hasExtension()) {
            var1.append(" Extension: ");
            var1.append(this.extension_);
         }

         if (this.hasCountryCodeSource()) {
            var1.append(" Country Code Source: ");
            var1.append(this.countryCodeSource_);
         }

         if (this.hasPreferredDomesticCarrierCode()) {
            var1.append(" Preferred Domestic Carrier Code: ");
            var1.append(this.preferredDomesticCarrierCode_);
         }

         return var1.toString();
      }

      public static enum CountryCodeSource {
         FROM_DEFAULT_COUNTRY,
         FROM_NUMBER_WITHOUT_PLUS_SIGN,
         FROM_NUMBER_WITH_IDD,
         FROM_NUMBER_WITH_PLUS_SIGN,
         UNSPECIFIED;

         static {
            Phonenumber.PhoneNumber.CountryCodeSource var0 = new Phonenumber.PhoneNumber.CountryCodeSource("UNSPECIFIED", 4);
            UNSPECIFIED = var0;
         }
      }
   }
}
