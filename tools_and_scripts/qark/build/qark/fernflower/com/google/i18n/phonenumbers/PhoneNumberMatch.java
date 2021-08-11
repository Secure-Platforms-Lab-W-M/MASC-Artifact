package com.google.i18n.phonenumbers;

import java.util.Arrays;

public final class PhoneNumberMatch {
   private final Phonenumber.PhoneNumber number;
   private final String rawString;
   private final int start;

   PhoneNumberMatch(int var1, String var2, Phonenumber.PhoneNumber var3) {
      if (var1 >= 0) {
         if (var2 != null && var3 != null) {
            this.start = var1;
            this.rawString = var2;
            this.number = var3;
         } else {
            throw null;
         }
      } else {
         throw new IllegalArgumentException("Start index must be >= 0.");
      }
   }

   public int end() {
      return this.start + this.rawString.length();
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof PhoneNumberMatch)) {
         return false;
      } else {
         PhoneNumberMatch var2 = (PhoneNumberMatch)var1;
         return this.rawString.equals(var2.rawString) && this.start == var2.start && this.number.equals(var2.number);
      }
   }

   public int hashCode() {
      return Arrays.hashCode(new Object[]{this.start, this.rawString, this.number});
   }

   public Phonenumber.PhoneNumber number() {
      return this.number;
   }

   public String rawString() {
      return this.rawString;
   }

   public int start() {
      return this.start;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("PhoneNumberMatch [");
      var1.append(this.start());
      var1.append(",");
      var1.append(this.end());
      var1.append(") ");
      var1.append(this.rawString);
      return var1.toString();
   }
}
