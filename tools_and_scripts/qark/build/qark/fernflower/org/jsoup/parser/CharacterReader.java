package org.jsoup.parser;

import java.util.Arrays;
import java.util.Locale;
import org.jsoup.helper.Validate;

final class CharacterReader {
   static final char EOF = '\uffff';
   private static final int maxCacheLen = 12;
   private final char[] input;
   private final int length;
   private int mark = 0;
   private int pos = 0;
   private final String[] stringCache = new String[512];

   CharacterReader(String var1) {
      Validate.notNull(var1);
      this.input = var1.toCharArray();
      this.length = this.input.length;
   }

   private String cacheString(int var1, int var2) {
      char[] var9 = this.input;
      String[] var8 = this.stringCache;
      String var6;
      if (var2 > 12) {
         var6 = new String(var9, var1, var2);
      } else {
         int var5 = 0;
         int var4 = 0;

         int var3;
         for(var3 = var1; var4 < var2; ++var3) {
            var5 = var5 * 31 + var9[var3];
            ++var4;
         }

         var3 = var5 & var8.length - 1;
         String var7 = var8[var3];
         if (var7 == null) {
            var6 = new String(var9, var1, var2);
            var8[var3] = var6;
            return var6;
         }

         var6 = var7;
         if (!this.rangeEquals(var1, var2, var7)) {
            var6 = new String(var9, var1, var2);
            var8[var3] = var6;
            return var6;
         }
      }

      return var6;
   }

   void advance() {
      ++this.pos;
   }

   char consume() {
      char var1;
      if (this.pos >= this.length) {
         var1 = '\uffff';
      } else {
         var1 = this.input[this.pos];
      }

      ++this.pos;
      return var1;
   }

   String consumeAsString() {
      char[] var2 = this.input;
      int var1 = this.pos++;
      return new String(var2, var1, 1);
   }

   String consumeData() {
      int var1 = this.pos;
      int var2 = this.length;

      for(char[] var4 = this.input; this.pos < var2; ++this.pos) {
         char var3 = var4[this.pos];
         if (var3 == '&' || var3 == '<' || var3 == 0) {
            break;
         }
      }

      return this.pos > var1 ? this.cacheString(var1, this.pos - var1) : "";
   }

   String consumeDigitSequence() {
      int var1;
      for(var1 = this.pos; this.pos < this.length; ++this.pos) {
         char var2 = this.input[this.pos];
         if (var2 < '0' || var2 > '9') {
            break;
         }
      }

      return this.cacheString(var1, this.pos - var1);
   }

   String consumeHexSequence() {
      int var1;
      for(var1 = this.pos; this.pos < this.length; ++this.pos) {
         char var2 = this.input[this.pos];
         if ((var2 < '0' || var2 > '9') && (var2 < 'A' || var2 > 'F') && (var2 < 'a' || var2 > 'f')) {
            break;
         }
      }

      return this.cacheString(var1, this.pos - var1);
   }

   String consumeLetterSequence() {
      int var1;
      for(var1 = this.pos; this.pos < this.length; ++this.pos) {
         char var2 = this.input[this.pos];
         if ((var2 < 'A' || var2 > 'Z') && (var2 < 'a' || var2 > 'z')) {
            break;
         }
      }

      return this.cacheString(var1, this.pos - var1);
   }

   String consumeLetterThenDigitSequence() {
      int var1;
      char var2;
      for(var1 = this.pos; this.pos < this.length; ++this.pos) {
         var2 = this.input[this.pos];
         if ((var2 < 'A' || var2 > 'Z') && (var2 < 'a' || var2 > 'z')) {
            break;
         }
      }

      while(!this.isEmpty()) {
         var2 = this.input[this.pos];
         if (var2 < '0' || var2 > '9') {
            break;
         }

         ++this.pos;
      }

      return this.cacheString(var1, this.pos - var1);
   }

   String consumeTagName() {
      int var1 = this.pos;
      int var2 = this.length;

      for(char[] var4 = this.input; this.pos < var2; ++this.pos) {
         char var3 = var4[this.pos];
         if (var3 == '\t' || var3 == '\n' || var3 == '\r' || var3 == '\f' || var3 == ' ' || var3 == '/' || var3 == '>' || var3 == 0) {
            break;
         }
      }

      return this.pos > var1 ? this.cacheString(var1, this.pos - var1) : "";
   }

   String consumeTo(char var1) {
      int var2 = this.nextIndexOf(var1);
      if (var2 != -1) {
         String var3 = this.cacheString(this.pos, var2);
         this.pos += var2;
         return var3;
      } else {
         return this.consumeToEnd();
      }
   }

   String consumeTo(String var1) {
      int var2 = this.nextIndexOf(var1);
      if (var2 != -1) {
         var1 = this.cacheString(this.pos, var2);
         this.pos += var2;
         return var1;
      } else {
         return this.consumeToEnd();
      }
   }

   String consumeToAny(char... var1) {
      int var3 = this.pos;
      int var4 = this.length;

      for(char[] var7 = this.input; this.pos < var4; ++this.pos) {
         int var5 = var1.length;

         for(int var2 = 0; var2 < var5; ++var2) {
            char var6 = var1[var2];
            if (var7[this.pos] == var6) {
               return this.pos > var3 ? this.cacheString(var3, this.pos - var3) : "";
            }
         }
      }

      return this.pos > var3 ? this.cacheString(var3, this.pos - var3) : "";
   }

   String consumeToAnySorted(char... var1) {
      int var2 = this.pos;
      int var3 = this.length;

      for(char[] var4 = this.input; this.pos < var3 && Arrays.binarySearch(var1, var4[this.pos]) < 0; ++this.pos) {
      }

      return this.pos > var2 ? this.cacheString(var2, this.pos - var2) : "";
   }

   String consumeToEnd() {
      String var1 = this.cacheString(this.pos, this.length - this.pos);
      this.pos = this.length;
      return var1;
   }

   boolean containsIgnoreCase(String var1) {
      String var2 = var1.toLowerCase(Locale.ENGLISH);
      var1 = var1.toUpperCase(Locale.ENGLISH);
      return this.nextIndexOf(var2) > -1 || this.nextIndexOf(var1) > -1;
   }

   char current() {
      return this.pos >= this.length ? '\uffff' : this.input[this.pos];
   }

   boolean isEmpty() {
      return this.pos >= this.length;
   }

   void mark() {
      this.mark = this.pos;
   }

   boolean matchConsume(String var1) {
      if (this.matches(var1)) {
         this.pos += var1.length();
         return true;
      } else {
         return false;
      }
   }

   boolean matchConsumeIgnoreCase(String var1) {
      if (this.matchesIgnoreCase(var1)) {
         this.pos += var1.length();
         return true;
      } else {
         return false;
      }
   }

   boolean matches(char var1) {
      return !this.isEmpty() && this.input[this.pos] == var1;
   }

   boolean matches(String var1) {
      int var3 = var1.length();
      if (var3 <= this.length - this.pos) {
         int var2 = 0;

         while(true) {
            if (var2 >= var3) {
               return true;
            }

            if (var1.charAt(var2) != this.input[this.pos + var2]) {
               break;
            }

            ++var2;
         }
      }

      return false;
   }

   boolean matchesAny(char... var1) {
      if (!this.isEmpty()) {
         char var3 = this.input[this.pos];
         int var4 = var1.length;

         for(int var2 = 0; var2 < var4; ++var2) {
            if (var1[var2] == var3) {
               return true;
            }
         }
      }

      return false;
   }

   boolean matchesAnySorted(char[] var1) {
      return !this.isEmpty() && Arrays.binarySearch(var1, this.input[this.pos]) >= 0;
   }

   boolean matchesDigit() {
      if (!this.isEmpty()) {
         char var1 = this.input[this.pos];
         if (var1 >= '0' && var1 <= '9') {
            return true;
         }
      }

      return false;
   }

   boolean matchesIgnoreCase(String var1) {
      int var3 = var1.length();
      if (var3 <= this.length - this.pos) {
         int var2 = 0;

         while(true) {
            if (var2 >= var3) {
               return true;
            }

            if (Character.toUpperCase(var1.charAt(var2)) != Character.toUpperCase(this.input[this.pos + var2])) {
               break;
            }

            ++var2;
         }
      }

      return false;
   }

   boolean matchesLetter() {
      if (!this.isEmpty()) {
         char var1 = this.input[this.pos];
         if (var1 >= 'A' && var1 <= 'Z' || var1 >= 'a' && var1 <= 'z' || Character.isLetter(var1)) {
            return true;
         }
      }

      return false;
   }

   int nextIndexOf(char var1) {
      for(int var2 = this.pos; var2 < this.length; ++var2) {
         if (var1 == this.input[var2]) {
            return var2 - this.pos;
         }
      }

      return -1;
   }

   int nextIndexOf(CharSequence var1) {
      char var5 = var1.charAt(0);

      int var3;
      for(int var2 = this.pos; var2 < this.length; var2 = var3 + 1) {
         var3 = var2;
         if (var5 != this.input[var2]) {
            var3 = var2;

            while(true) {
               var2 = var3 + 1;
               var3 = var2;
               if (var2 >= this.length) {
                  break;
               }

               var3 = var2;
               if (var5 == this.input[var2]) {
                  var3 = var2;
                  break;
               }
            }
         }

         int var4 = var3 + 1;
         int var6 = var1.length() + var4 - 1;
         if (var3 < this.length && var6 <= this.length) {
            for(var2 = 1; var4 < var6 && var1.charAt(var2) == this.input[var4]; ++var2) {
               ++var4;
            }

            if (var4 == var6) {
               return var3 - this.pos;
            }
         }
      }

      return -1;
   }

   int pos() {
      return this.pos;
   }

   boolean rangeEquals(int var1, int var2, String var3) {
      if (var2 == var3.length()) {
         char[] var5 = this.input;
         int var4 = 0;

         while(true) {
            if (var2 == 0) {
               return true;
            }

            if (var5[var1] != var3.charAt(var4)) {
               break;
            }

            ++var4;
            ++var1;
            --var2;
         }
      }

      return false;
   }

   void rewindToMark() {
      this.pos = this.mark;
   }

   public String toString() {
      return new String(this.input, this.pos, this.length - this.pos);
   }

   void unconsume() {
      --this.pos;
   }
}
