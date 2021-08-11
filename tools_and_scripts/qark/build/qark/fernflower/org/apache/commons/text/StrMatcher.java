package org.apache.commons.text;

import java.util.Arrays;

@Deprecated
public abstract class StrMatcher {
   private static final StrMatcher COMMA_MATCHER = new StrMatcher.CharMatcher(',');
   private static final StrMatcher DOUBLE_QUOTE_MATCHER = new StrMatcher.CharMatcher('"');
   private static final StrMatcher NONE_MATCHER = new StrMatcher.NoMatcher();
   private static final StrMatcher QUOTE_MATCHER = new StrMatcher.CharSetMatcher("'\"".toCharArray());
   private static final StrMatcher SINGLE_QUOTE_MATCHER = new StrMatcher.CharMatcher('\'');
   private static final StrMatcher SPACE_MATCHER = new StrMatcher.CharMatcher(' ');
   private static final StrMatcher SPLIT_MATCHER = new StrMatcher.CharSetMatcher(" \t\n\r\f".toCharArray());
   private static final StrMatcher TAB_MATCHER = new StrMatcher.CharMatcher('\t');
   private static final StrMatcher TRIM_MATCHER = new StrMatcher.TrimMatcher();

   protected StrMatcher() {
   }

   public static StrMatcher charMatcher(char var0) {
      return new StrMatcher.CharMatcher(var0);
   }

   public static StrMatcher charSetMatcher(String var0) {
      if (var0 != null && var0.length() != 0) {
         return (StrMatcher)(var0.length() == 1 ? new StrMatcher.CharMatcher(var0.charAt(0)) : new StrMatcher.CharSetMatcher(var0.toCharArray()));
      } else {
         return NONE_MATCHER;
      }
   }

   public static StrMatcher charSetMatcher(char... var0) {
      if (var0 != null && var0.length != 0) {
         return (StrMatcher)(var0.length == 1 ? new StrMatcher.CharMatcher(var0[0]) : new StrMatcher.CharSetMatcher(var0));
      } else {
         return NONE_MATCHER;
      }
   }

   public static StrMatcher commaMatcher() {
      return COMMA_MATCHER;
   }

   public static StrMatcher doubleQuoteMatcher() {
      return DOUBLE_QUOTE_MATCHER;
   }

   public static StrMatcher noneMatcher() {
      return NONE_MATCHER;
   }

   public static StrMatcher quoteMatcher() {
      return QUOTE_MATCHER;
   }

   public static StrMatcher singleQuoteMatcher() {
      return SINGLE_QUOTE_MATCHER;
   }

   public static StrMatcher spaceMatcher() {
      return SPACE_MATCHER;
   }

   public static StrMatcher splitMatcher() {
      return SPLIT_MATCHER;
   }

   public static StrMatcher stringMatcher(String var0) {
      return (StrMatcher)(var0 != null && var0.length() != 0 ? new StrMatcher.StringMatcher(var0) : NONE_MATCHER);
   }

   public static StrMatcher tabMatcher() {
      return TAB_MATCHER;
   }

   public static StrMatcher trimMatcher() {
      return TRIM_MATCHER;
   }

   public int isMatch(char[] var1, int var2) {
      return this.isMatch(var1, var2, 0, var1.length);
   }

   public abstract int isMatch(char[] var1, int var2, int var3, int var4);

   static final class CharMatcher extends StrMatcher {
      // $FF: renamed from: ch char
      private final char field_38;

      CharMatcher(char var1) {
         this.field_38 = var1;
      }

      public int isMatch(char[] var1, int var2, int var3, int var4) {
         return this.field_38 == var1[var2] ? 1 : 0;
      }
   }

   static final class CharSetMatcher extends StrMatcher {
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

   static final class NoMatcher extends StrMatcher {
      public int isMatch(char[] var1, int var2, int var3, int var4) {
         return 0;
      }
   }

   static final class StringMatcher extends StrMatcher {
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

   static final class TrimMatcher extends StrMatcher {
      public int isMatch(char[] var1, int var2, int var3, int var4) {
         return var1[var2] <= ' ' ? 1 : 0;
      }
   }
}
