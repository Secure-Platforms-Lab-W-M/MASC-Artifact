package com.google.i18n.phonenumbers;

public class NumberParseException extends Exception {
   private NumberParseException.ErrorType errorType;
   private String message;

   public NumberParseException(NumberParseException.ErrorType var1, String var2) {
      super(var2);
      this.message = var2;
      this.errorType = var1;
   }

   public NumberParseException.ErrorType getErrorType() {
      return this.errorType;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Error type: ");
      var1.append(this.errorType);
      var1.append(". ");
      var1.append(this.message);
      return var1.toString();
   }

   public static enum ErrorType {
      INVALID_COUNTRY_CODE,
      NOT_A_NUMBER,
      TOO_LONG,
      TOO_SHORT_AFTER_IDD,
      TOO_SHORT_NSN;

      static {
         NumberParseException.ErrorType var0 = new NumberParseException.ErrorType("TOO_LONG", 4);
         TOO_LONG = var0;
      }
   }
}
