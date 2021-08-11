package org.apache.commons.text.matcher;

import java.util.Arrays;

abstract class AbstractStringMatcher implements org.apache.commons.text.matcher.StringMatcher {
   protected AbstractStringMatcher() {
   }

   public int isMatch(char[] var1, int var2) {
      return this.isMatch(var1, var2, 0, var1.length);
   }

   static final class CharMatcher extends AbstractStringMatcher {
      // $FF: renamed from: ch char
      private final char field_96;

      CharMatcher(char var1) {
         this.field_96 = var1;
      }

      public int isMatch(char[] var1, int var2, int var3, int var4) {
         return this.field_96 == var1[var2] ? 1 : 0;
      }
   }

   static final class CharSetMatcher extends AbstractStringMatcher {
      private final char[] chars;

      CharSetMatcher(char[] var1) {
         var1 = (char[])var1.clone();
         this.chars = var1;
         Arrays.sort(var1);
      }

      public int isMatch(char[] var1, int var2, int var3, int var4) {
         return Arrays.binarySearch(this.chars, var1[var2]) >= 0 ? 1 : 0;
      }
   }

   static final class NoMatcher extends AbstractStringMatcher {
      public int isMatch(char[] var1, int var2, int var3, int var4) {
         return 0;
      }
   }

   static final class StringMatcher extends AbstractStringMatcher {
      private final char[] chars;

      StringMatcher(String var1) {
         this.chars = var1.toCharArray();
      }

      public int isMatch(char[] var1, int var2, int var3, int var4) {
         int var5 = this.chars.length;
         if (var2 + var5 > var4) {
            return 0;
         } else {
            var3 = 0;

            while(true) {
               char[] var6 = this.chars;
               if (var3 >= var6.length) {
                  return var5;
               }

               if (var6[var3] != var1[var2]) {
                  return 0;
               }

               ++var3;
               ++var2;
            }
         }
      }

      public String toString() {
         StringBuilder var1 = new StringBuilder();
         var1.append(super.toString());
         var1.append(' ');
         var1.append(Arrays.toString(this.chars));
         return var1.toString();
      }
   }

   static final class TrimMatcher extends AbstractStringMatcher {
      private static final int SPACE_INT = 32;

      public int isMatch(char[] var1, int var2, int var3, int var4) {
         return var1[var2] <= ' ' ? 1 : 0;
      }
   }
}
