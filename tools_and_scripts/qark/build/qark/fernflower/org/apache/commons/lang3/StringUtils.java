package org.apache.commons.lang3;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.regex.Pattern;

public class StringUtils {
   // $FF: renamed from: CR java.lang.String
   public static final String field_156 = "\r";
   public static final String EMPTY = "";
   public static final int INDEX_NOT_FOUND = -1;
   // $FF: renamed from: LF java.lang.String
   public static final String field_157 = "\n";
   private static final int PAD_LIMIT = 8192;
   public static final String SPACE = " ";
   private static final int STRING_BUILDER_SIZE = 256;

   public static String abbreviate(String var0, int var1) {
      return abbreviate(var0, "...", 0, var1);
   }

   public static String abbreviate(String var0, int var1, int var2) {
      return abbreviate(var0, "...", var1, var2);
   }

   public static String abbreviate(String var0, String var1, int var2) {
      return abbreviate(var0, var1, 0, var2);
   }

   public static String abbreviate(String var0, String var1, int var2, int var3) {
      if (!isEmpty(var0)) {
         if (isEmpty(var1)) {
            return var0;
         } else {
            int var5 = var1.length();
            int var4 = var5 + 1;
            int var6 = var5 + var5 + 1;
            if (var3 >= var4) {
               if (var0.length() <= var3) {
                  return var0;
               } else {
                  var4 = var2;
                  if (var2 > var0.length()) {
                     var4 = var0.length();
                  }

                  var2 = var4;
                  if (var0.length() - var4 < var3 - var5) {
                     var2 = var0.length() - (var3 - var5);
                  }

                  StringBuilder var7;
                  if (var2 <= var5 + 1) {
                     var7 = new StringBuilder();
                     var7.append(var0.substring(0, var3 - var5));
                     var7.append(var1);
                     return var7.toString();
                  } else if (var3 >= var6) {
                     if (var2 + var3 - var5 < var0.length()) {
                        var7 = new StringBuilder();
                        var7.append(var1);
                        var7.append(abbreviate(var0.substring(var2), var1, var3 - var5));
                        return var7.toString();
                     } else {
                        var7 = new StringBuilder();
                        var7.append(var1);
                        var7.append(var0.substring(var0.length() - (var3 - var5)));
                        return var7.toString();
                     }
                  } else {
                     throw new IllegalArgumentException(String.format("Minimum abbreviation width with offset is %d", var6));
                  }
               }
            } else {
               throw new IllegalArgumentException(String.format("Minimum abbreviation width is %d", var4));
            }
         }
      } else {
         return var0;
      }
   }

   public static String abbreviateMiddle(String var0, String var1, int var2) {
      if (!isEmpty(var0)) {
         if (isEmpty(var1)) {
            return var0;
         } else if (var2 < var0.length()) {
            if (var2 < var1.length() + 2) {
               return var0;
            } else {
               var2 -= var1.length();
               int var3 = var2 / 2;
               int var4 = var0.length();
               int var5 = var2 / 2;
               StringBuilder var6 = new StringBuilder();
               var6.append(var0.substring(0, var3 + var2 % 2));
               var6.append(var1);
               var6.append(var0.substring(var4 - var5));
               return var6.toString();
            }
         } else {
            return var0;
         }
      } else {
         return var0;
      }
   }

   private static String appendIfMissing(String var0, CharSequence var1, boolean var2, CharSequence... var3) {
      if (var0 != null && !isEmpty(var1)) {
         if (endsWith(var0, var1, var2)) {
            return var0;
         } else {
            if (var3 != null && var3.length > 0) {
               int var5 = var3.length;

               for(int var4 = 0; var4 < var5; ++var4) {
                  if (endsWith(var0, var3[var4], var2)) {
                     return var0;
                  }
               }
            }

            StringBuilder var6 = new StringBuilder();
            var6.append(var0);
            var6.append(var1.toString());
            return var6.toString();
         }
      } else {
         return var0;
      }
   }

   public static String appendIfMissing(String var0, CharSequence var1, CharSequence... var2) {
      return appendIfMissing(var0, var1, false, var2);
   }

   public static String appendIfMissingIgnoreCase(String var0, CharSequence var1, CharSequence... var2) {
      return appendIfMissing(var0, var1, true, var2);
   }

   public static String capitalize(String var0) {
      if (var0 == null) {
         return var0;
      } else {
         int var3 = var0.length();
         if (var3 == 0) {
            return var0;
         } else {
            int var2 = var0.codePointAt(0);
            int var4 = Character.toTitleCase(var2);
            if (var2 == var4) {
               return var0;
            } else {
               int[] var5 = new int[var3];
               int var1 = 0 + 1;
               var5[0] = var4;

               for(var2 = Character.charCount(var2); var2 < var3; ++var1) {
                  var4 = var0.codePointAt(var2);
                  var5[var1] = var4;
                  var2 += Character.charCount(var4);
               }

               return new String(var5, 0, var1);
            }
         }
      }
   }

   public static String center(String var0, int var1) {
      return center(var0, var1, ' ');
   }

   public static String center(String var0, int var1, char var2) {
      if (var0 != null) {
         if (var1 <= 0) {
            return var0;
         } else {
            int var3 = var0.length();
            int var4 = var1 - var3;
            return var4 <= 0 ? var0 : rightPad(leftPad(var0, var4 / 2 + var3, var2), var1, var2);
         }
      } else {
         return var0;
      }
   }

   public static String center(String var0, int var1, String var2) {
      if (var0 != null) {
         if (var1 <= 0) {
            return var0;
         } else {
            String var5 = var2;
            if (isEmpty(var2)) {
               var5 = " ";
            }

            int var3 = var0.length();
            int var4 = var1 - var3;
            return var4 <= 0 ? var0 : rightPad(leftPad(var0, var4 / 2 + var3, var5), var1, var5);
         }
      } else {
         return var0;
      }
   }

   public static String chomp(String var0) {
      if (isEmpty(var0)) {
         return var0;
      } else if (var0.length() == 1) {
         char var4 = var0.charAt(0);
         return var4 != '\r' && var4 != '\n' ? var0 : "";
      } else {
         int var2 = var0.length() - 1;
         char var3 = var0.charAt(var2);
         int var1;
         if (var3 == '\n') {
            var1 = var2;
            if (var0.charAt(var2 - 1) == '\r') {
               var1 = var2 - 1;
            }
         } else {
            var1 = var2;
            if (var3 != '\r') {
               var1 = var2 + 1;
            }
         }

         return var0.substring(0, var1);
      }
   }

   @Deprecated
   public static String chomp(String var0, String var1) {
      return removeEnd(var0, var1);
   }

   public static String chop(String var0) {
      if (var0 == null) {
         return null;
      } else {
         int var1 = var0.length();
         if (var1 < 2) {
            return "";
         } else {
            --var1;
            String var2 = var0.substring(0, var1);
            return var0.charAt(var1) == '\n' && var2.charAt(var1 - 1) == '\r' ? var2.substring(0, var1 - 1) : var2;
         }
      }
   }

   public static int compare(String var0, String var1) {
      return compare(var0, var1, true);
   }

   public static int compare(String var0, String var1, boolean var2) {
      if (var0 == var1) {
         return 0;
      } else {
         byte var3 = -1;
         if (var0 == null) {
            return var2 ? -1 : 1;
         } else if (var1 == null) {
            if (var2) {
               var3 = 1;
            }

            return var3;
         } else {
            return var0.compareTo(var1);
         }
      }
   }

   public static int compareIgnoreCase(String var0, String var1) {
      return compareIgnoreCase(var0, var1, true);
   }

   public static int compareIgnoreCase(String var0, String var1, boolean var2) {
      if (var0 == var1) {
         return 0;
      } else {
         byte var3 = -1;
         if (var0 == null) {
            return var2 ? -1 : 1;
         } else if (var1 == null) {
            if (var2) {
               var3 = 1;
            }

            return var3;
         } else {
            return var0.compareToIgnoreCase(var1);
         }
      }
   }

   public static boolean contains(CharSequence var0, int var1) {
      boolean var3 = isEmpty(var0);
      boolean var2 = false;
      if (var3) {
         return false;
      } else {
         if (CharSequenceUtils.indexOf(var0, var1, 0) >= 0) {
            var2 = true;
         }

         return var2;
      }
   }

   public static boolean contains(CharSequence var0, CharSequence var1) {
      boolean var2 = false;
      if (var0 != null) {
         if (var1 == null) {
            return false;
         } else {
            if (CharSequenceUtils.indexOf(var0, var1, 0) >= 0) {
               var2 = true;
            }

            return var2;
         }
      } else {
         return false;
      }
   }

   public static boolean containsAny(CharSequence var0, CharSequence var1) {
      return var1 == null ? false : containsAny(var0, CharSequenceUtils.toCharArray(var1));
   }

   public static boolean containsAny(CharSequence var0, char... var1) {
      if (isEmpty(var0)) {
         return false;
      } else if (ArrayUtils.isEmpty(var1)) {
         return false;
      } else {
         int var5 = var0.length();
         int var6 = var1.length;

         for(int var3 = 0; var3 < var5; ++var3) {
            char var2 = var0.charAt(var3);

            for(int var4 = 0; var4 < var6; ++var4) {
               if (var1[var4] == var2) {
                  if (!Character.isHighSurrogate(var2)) {
                     return true;
                  }

                  if (var4 == var6 - 1) {
                     return true;
                  }

                  if (var3 < var5 - 1 && var1[var4 + 1] == var0.charAt(var3 + 1)) {
                     return true;
                  }
               }
            }
         }

         return false;
      }
   }

   public static boolean containsAny(CharSequence var0, CharSequence... var1) {
      if (!isEmpty(var0)) {
         if (ArrayUtils.isEmpty((Object[])var1)) {
            return false;
         } else {
            int var3 = var1.length;

            for(int var2 = 0; var2 < var3; ++var2) {
               if (contains(var0, var1[var2])) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean containsIgnoreCase(CharSequence var0, CharSequence var1) {
      if (var0 != null) {
         if (var1 == null) {
            return false;
         } else {
            int var3 = var1.length();
            int var4 = var0.length();

            for(int var2 = 0; var2 <= var4 - var3; ++var2) {
               if (CharSequenceUtils.regionMatches(var0, true, var2, var1, 0, var3)) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean containsNone(CharSequence var0, String var1) {
      return var0 != null && var1 != null ? containsNone(var0, var1.toCharArray()) : true;
   }

   public static boolean containsNone(CharSequence var0, char... var1) {
      if (var0 == null) {
         return true;
      } else if (var1 == null) {
         return true;
      } else {
         int var5 = var0.length();
         int var6 = var1.length;

         for(int var3 = 0; var3 < var5; ++var3) {
            char var2 = var0.charAt(var3);

            for(int var4 = 0; var4 < var6; ++var4) {
               if (var1[var4] == var2) {
                  if (!Character.isHighSurrogate(var2)) {
                     return false;
                  }

                  if (var4 == var6 - 1) {
                     return false;
                  }

                  if (var3 < var5 - 1 && var1[var4 + 1] == var0.charAt(var3 + 1)) {
                     return false;
                  }
               }
            }
         }

         return true;
      }
   }

   public static boolean containsOnly(CharSequence var0, String var1) {
      return var0 != null && var1 != null ? containsOnly(var0, var1.toCharArray()) : false;
   }

   public static boolean containsOnly(CharSequence var0, char... var1) {
      boolean var2 = false;
      if (var1 != null) {
         if (var0 == null) {
            return false;
         } else if (var0.length() == 0) {
            return true;
         } else if (var1.length == 0) {
            return false;
         } else {
            if (indexOfAnyBut(var0, var1) == -1) {
               var2 = true;
            }

            return var2;
         }
      } else {
         return false;
      }
   }

   public static boolean containsWhitespace(CharSequence var0) {
      if (isEmpty(var0)) {
         return false;
      } else {
         int var2 = var0.length();

         for(int var1 = 0; var1 < var2; ++var1) {
            if (Character.isWhitespace(var0.charAt(var1))) {
               return true;
            }
         }

         return false;
      }
   }

   private static void convertRemainingAccentCharacters(StringBuilder var0) {
      for(int var1 = 0; var1 < var0.length(); ++var1) {
         if (var0.charAt(var1) == 321) {
            var0.deleteCharAt(var1);
            var0.insert(var1, 'L');
         } else if (var0.charAt(var1) == 322) {
            var0.deleteCharAt(var1);
            var0.insert(var1, 'l');
         }
      }

   }

   public static int countMatches(CharSequence var0, char var1) {
      if (isEmpty(var0)) {
         return 0;
      } else {
         int var3 = 0;

         int var4;
         for(int var2 = 0; var2 < var0.length(); var3 = var4) {
            var4 = var3;
            if (var1 == var0.charAt(var2)) {
               var4 = var3 + 1;
            }

            ++var2;
         }

         return var3;
      }
   }

   public static int countMatches(CharSequence var0, CharSequence var1) {
      if (!isEmpty(var0) && !isEmpty(var1)) {
         int var2 = 0;
         int var3 = 0;

         while(true) {
            var3 = CharSequenceUtils.indexOf(var0, var1, var3);
            if (var3 == -1) {
               return var2;
            }

            ++var2;
            var3 += var1.length();
         }
      } else {
         return 0;
      }
   }

   public static CharSequence defaultIfBlank(CharSequence var0, CharSequence var1) {
      return isBlank(var0) ? var1 : var0;
   }

   public static CharSequence defaultIfEmpty(CharSequence var0, CharSequence var1) {
      return isEmpty(var0) ? var1 : var0;
   }

   public static String defaultString(String var0) {
      return defaultString(var0, "");
   }

   public static String defaultString(String var0, String var1) {
      return var0 == null ? var1 : var0;
   }

   public static String deleteWhitespace(String var0) {
      if (isEmpty(var0)) {
         return var0;
      } else {
         int var4 = var0.length();
         char[] var5 = new char[var4];
         int var2 = 0;

         int var3;
         for(int var1 = 0; var1 < var4; var2 = var3) {
            var3 = var2;
            if (!Character.isWhitespace(var0.charAt(var1))) {
               var5[var2] = var0.charAt(var1);
               var3 = var2 + 1;
            }

            ++var1;
         }

         if (var2 == var4) {
            return var0;
         } else {
            return new String(var5, 0, var2);
         }
      }
   }

   public static String difference(String var0, String var1) {
      if (var0 == null) {
         return var1;
      } else if (var1 == null) {
         return var0;
      } else {
         int var2 = indexOfDifference(var0, var1);
         return var2 == -1 ? "" : var1.substring(var2);
      }
   }

   public static boolean endsWith(CharSequence var0, CharSequence var1) {
      return endsWith(var0, var1, false);
   }

   private static boolean endsWith(CharSequence var0, CharSequence var1, boolean var2) {
      boolean var3 = false;
      if (var0 != null && var1 != null) {
         return var1.length() > var0.length() ? false : CharSequenceUtils.regionMatches(var0, var2, var0.length() - var1.length(), var1, 0, var1.length());
      } else {
         var2 = var3;
         if (var0 == var1) {
            var2 = true;
         }

         return var2;
      }
   }

   public static boolean endsWithAny(CharSequence var0, CharSequence... var1) {
      if (!isEmpty(var0)) {
         if (ArrayUtils.isEmpty((Object[])var1)) {
            return false;
         } else {
            int var3 = var1.length;

            for(int var2 = 0; var2 < var3; ++var2) {
               if (endsWith(var0, var1[var2])) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean endsWithIgnoreCase(CharSequence var0, CharSequence var1) {
      return endsWith(var0, var1, true);
   }

   public static boolean equals(CharSequence var0, CharSequence var1) {
      if (var0 == var1) {
         return true;
      } else if (var0 != null) {
         if (var1 == null) {
            return false;
         } else if (var0.length() != var1.length()) {
            return false;
         } else if (var0 instanceof String && var1 instanceof String) {
            return var0.equals(var1);
         } else {
            int var3 = var0.length();

            for(int var2 = 0; var2 < var3; ++var2) {
               if (var0.charAt(var2) != var1.charAt(var2)) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public static boolean equalsAny(CharSequence var0, CharSequence... var1) {
      if (ArrayUtils.isNotEmpty((Object[])var1)) {
         int var3 = var1.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            if (equals(var0, var1[var2])) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean equalsAnyIgnoreCase(CharSequence var0, CharSequence... var1) {
      if (ArrayUtils.isNotEmpty((Object[])var1)) {
         int var3 = var1.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            if (equalsIgnoreCase(var0, var1[var2])) {
               return true;
            }
         }
      }

      return false;
   }

   public static boolean equalsIgnoreCase(CharSequence var0, CharSequence var1) {
      if (var0 == var1) {
         return true;
      } else if (var0 != null) {
         if (var1 == null) {
            return false;
         } else {
            return var0.length() != var1.length() ? false : CharSequenceUtils.regionMatches(var0, true, 0, var1, 0, var0.length());
         }
      } else {
         return false;
      }
   }

   @SafeVarargs
   public static CharSequence firstNonBlank(CharSequence... var0) {
      if (var0 != null) {
         int var2 = var0.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            CharSequence var3 = var0[var1];
            if (isNotBlank(var3)) {
               return var3;
            }
         }
      }

      return null;
   }

   @SafeVarargs
   public static CharSequence firstNonEmpty(CharSequence... var0) {
      if (var0 != null) {
         int var2 = var0.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            CharSequence var3 = var0[var1];
            if (isNotEmpty(var3)) {
               return var3;
            }
         }
      }

      return null;
   }

   public static String getCommonPrefix(String... var0) {
      if (var0 != null) {
         if (var0.length == 0) {
            return "";
         } else {
            int var1 = indexOfDifference(var0);
            if (var1 == -1) {
               return var0[0] == null ? "" : var0[0];
            } else {
               return var1 == 0 ? "" : var0[0].substring(0, var1);
            }
         }
      } else {
         return "";
      }
   }

   public static String getDigits(String var0) {
      if (isEmpty(var0)) {
         return var0;
      } else {
         int var3 = var0.length();
         StringBuilder var4 = new StringBuilder(var3);

         for(int var2 = 0; var2 < var3; ++var2) {
            char var1 = var0.charAt(var2);
            if (Character.isDigit(var1)) {
               var4.append(var1);
            }
         }

         return var4.toString();
      }
   }

   @Deprecated
   public static int getFuzzyDistance(CharSequence var0, CharSequence var1, Locale var2) {
      if (var0 != null && var1 != null) {
         if (var2 == null) {
            throw new IllegalArgumentException("Locale must not be null");
         } else {
            String var11 = var0.toString().toLowerCase(var2);
            String var12 = var1.toString().toLowerCase(var2);
            int var4 = 0;
            int var3 = 0;
            int var6 = Integer.MIN_VALUE;

            for(int var5 = 0; var5 < var12.length(); ++var5) {
               char var10 = var12.charAt(var5);

               int var8;
               for(boolean var7 = false; var3 < var11.length() && !var7; var6 = var8) {
                  int var9 = var4;
                  var8 = var6;
                  if (var10 == var11.charAt(var3)) {
                     int var13 = var4 + 1;
                     var4 = var13;
                     if (var6 + 1 == var3) {
                        var4 = var13 + 2;
                     }

                     var8 = var3;
                     var7 = true;
                     var9 = var4;
                  }

                  ++var3;
                  var4 = var9;
               }
            }

            return var4;
         }
      } else {
         throw new IllegalArgumentException("Strings must not be null");
      }
   }

   @Deprecated
   public static double getJaroWinklerDistance(CharSequence var0, CharSequence var1) {
      if (var0 != null && var1 != null) {
         int[] var4 = matches(var0, var1);
         double var2 = (double)var4[0];
         if (var2 == 0.0D) {
            return 0.0D;
         } else {
            var2 = (var2 / (double)var0.length() + var2 / (double)var1.length() + (var2 - (double)var4[1]) / var2) / 3.0D;
            if (var2 >= 0.7D) {
               var2 += Math.min(0.1D, 1.0D / (double)var4[3]) * (double)var4[2] * (1.0D - var2);
            }

            return (double)Math.round(var2 * 100.0D) / 100.0D;
         }
      } else {
         throw new IllegalArgumentException("Strings must not be null");
      }
   }

   @Deprecated
   public static int getLevenshteinDistance(CharSequence var0, CharSequence var1) {
      if (var0 != null && var1 != null) {
         int var5 = var0.length();
         int var4 = var1.length();
         if (var5 == 0) {
            return var4;
         } else if (var4 == 0) {
            return var5;
         } else {
            int var3 = var5;
            int var2 = var4;
            CharSequence var11 = var0;
            CharSequence var10 = var1;
            if (var5 > var4) {
               var3 = var4;
               var2 = var0.length();
               var10 = var0;
               var11 = var1;
            }

            int[] var12 = new int[var3 + 1];

            for(var4 = 0; var4 <= var3; var12[var4] = var4++) {
            }

            for(var4 = 1; var4 <= var2; ++var4) {
               int var6 = var12[0];
               char var9 = var10.charAt(var4 - 1);
               var12[0] = var4;

               for(var5 = 1; var5 <= var3; ++var5) {
                  int var8 = var12[var5];
                  byte var7;
                  if (var11.charAt(var5 - 1) == var9) {
                     var7 = 0;
                  } else {
                     var7 = 1;
                  }

                  var12[var5] = Math.min(Math.min(var12[var5 - 1] + 1, var12[var5] + 1), var6 + var7);
                  var6 = var8;
               }
            }

            return var12[var3];
         }
      } else {
         throw new IllegalArgumentException("Strings must not be null");
      }
   }

   @Deprecated
   public static int getLevenshteinDistance(CharSequence var0, CharSequence var1, int var2) {
      if (var0 != null && var1 != null) {
         if (var2 < 0) {
            throw new IllegalArgumentException("Threshold must not be negative");
         } else {
            int var5 = var0.length();
            int var3 = var1.length();
            int var4 = -1;
            if (var5 == 0) {
               if (var3 <= var2) {
                  var4 = var3;
               }

               return var4;
            } else if (var3 == 0) {
               if (var5 <= var2) {
                  var4 = var5;
               }

               return var4;
            } else if (Math.abs(var5 - var3) > var2) {
               return -1;
            } else {
               CharSequence var9;
               CharSequence var10;
               if (var5 > var3) {
                  var4 = var0.length();
                  var9 = var1;
                  var10 = var0;
               } else {
                  var10 = var1;
                  var9 = var0;
                  var4 = var3;
                  var3 = var5;
               }

               int[] var13 = new int[var3 + 1];
               int[] var12 = new int[var3 + 1];
               int var6 = Math.min(var3, var2) + 1;

               for(var5 = 0; var5 < var6; var13[var5] = var5++) {
               }

               Arrays.fill(var13, var6, var13.length, Integer.MAX_VALUE);
               Arrays.fill(var12, Integer.MAX_VALUE);

               int[] var11;
               for(var5 = 1; var5 <= var4; var12 = var11) {
                  char var8 = var10.charAt(var5 - 1);
                  var12[0] = var5;
                  int var7 = Math.max(1, var5 - var2);
                  if (var5 > Integer.MAX_VALUE - var2) {
                     var6 = var3;
                  } else {
                     var6 = Math.min(var3, var5 + var2);
                  }

                  if (var7 > var6) {
                     return -1;
                  }

                  if (var7 > 1) {
                     var12[var7 - 1] = Integer.MAX_VALUE;
                  }

                  for(; var7 <= var6; ++var7) {
                     if (var9.charAt(var7 - 1) == var8) {
                        var12[var7] = var13[var7 - 1];
                     } else {
                        var12[var7] = Math.min(Math.min(var12[var7 - 1], var13[var7]), var13[var7 - 1]) + 1;
                     }
                  }

                  var11 = var13;
                  ++var5;
                  var13 = var12;
               }

               if (var13[var3] <= var2) {
                  return var13[var3];
               } else {
                  return -1;
               }
            }
         }
      } else {
         throw new IllegalArgumentException("Strings must not be null");
      }
   }

   public static int indexOf(CharSequence var0, int var1) {
      return isEmpty(var0) ? -1 : CharSequenceUtils.indexOf(var0, var1, 0);
   }

   public static int indexOf(CharSequence var0, int var1, int var2) {
      return isEmpty(var0) ? -1 : CharSequenceUtils.indexOf(var0, var1, var2);
   }

   public static int indexOf(CharSequence var0, CharSequence var1) {
      return var0 != null && var1 != null ? CharSequenceUtils.indexOf(var0, var1, 0) : -1;
   }

   public static int indexOf(CharSequence var0, CharSequence var1, int var2) {
      return var0 != null && var1 != null ? CharSequenceUtils.indexOf(var0, var1, var2) : -1;
   }

   public static int indexOfAny(CharSequence var0, String var1) {
      return !isEmpty(var0) && !isEmpty(var1) ? indexOfAny(var0, var1.toCharArray()) : -1;
   }

   public static int indexOfAny(CharSequence var0, char... var1) {
      if (isEmpty(var0)) {
         return -1;
      } else if (ArrayUtils.isEmpty(var1)) {
         return -1;
      } else {
         int var5 = var0.length();
         int var6 = var1.length;

         for(int var3 = 0; var3 < var5; ++var3) {
            char var2 = var0.charAt(var3);

            for(int var4 = 0; var4 < var6; ++var4) {
               if (var1[var4] == var2) {
                  if (var3 >= var5 - 1 || var4 >= var6 - 1 || !Character.isHighSurrogate(var2)) {
                     return var3;
                  }

                  if (var1[var4 + 1] == var0.charAt(var3 + 1)) {
                     return var3;
                  }
               }
            }
         }

         return -1;
      }
   }

   public static int indexOfAny(CharSequence var0, CharSequence... var1) {
      if (var0 != null) {
         if (var1 == null) {
            return -1;
         } else {
            int var3 = Integer.MAX_VALUE;
            int var6 = var1.length;

            int var4;
            for(int var2 = 0; var2 < var6; var3 = var4) {
               CharSequence var7 = var1[var2];
               if (var7 == null) {
                  var4 = var3;
               } else {
                  int var5 = CharSequenceUtils.indexOf(var0, var7, 0);
                  if (var5 == -1) {
                     var4 = var3;
                  } else {
                     var4 = var3;
                     if (var5 < var3) {
                        var4 = var5;
                     }
                  }
               }

               ++var2;
            }

            if (var3 == Integer.MAX_VALUE) {
               return -1;
            } else {
               return var3;
            }
         }
      } else {
         return -1;
      }
   }

   public static int indexOfAnyBut(CharSequence var0, CharSequence var1) {
      if (isEmpty(var0)) {
         return -1;
      } else if (isEmpty(var1)) {
         return -1;
      } else {
         int var5 = var0.length();

         for(int var3 = 0; var3 < var5; ++var3) {
            char var2 = var0.charAt(var3);
            boolean var4;
            if (CharSequenceUtils.indexOf(var1, var2, 0) >= 0) {
               var4 = true;
            } else {
               var4 = false;
            }

            if (var3 + 1 < var5 && Character.isHighSurrogate(var2)) {
               char var6 = var0.charAt(var3 + 1);
               if (var4 && CharSequenceUtils.indexOf(var1, var6, 0) < 0) {
                  return var3;
               }
            } else if (!var4) {
               return var3;
            }
         }

         return -1;
      }
   }

   public static int indexOfAnyBut(CharSequence var0, char... var1) {
      if (isEmpty(var0)) {
         return -1;
      } else if (ArrayUtils.isEmpty(var1)) {
         return -1;
      } else {
         int var5 = var0.length();
         int var6 = var1.length;

         label42:
         for(int var3 = 0; var3 < var5; ++var3) {
            char var2 = var0.charAt(var3);

            for(int var4 = 0; var4 < var6; ++var4) {
               if (var1[var4] == var2 && (var3 >= var5 - 1 || var4 >= var6 - 1 || !Character.isHighSurrogate(var2) || var1[var4 + 1] == var0.charAt(var3 + 1))) {
                  continue label42;
               }
            }

            return var3;
         }

         return -1;
      }
   }

   public static int indexOfDifference(CharSequence var0, CharSequence var1) {
      if (var0 == var1) {
         return -1;
      } else if (var0 != null && var1 != null) {
         int var2;
         for(var2 = 0; var2 < var0.length() && var2 < var1.length() && var0.charAt(var2) == var1.charAt(var2); ++var2) {
         }

         if (var2 >= var1.length()) {
            return var2 < var0.length() ? var2 : -1;
         } else {
            return var2;
         }
      } else {
         return 0;
      }
   }

   public static int indexOfDifference(CharSequence... var0) {
      if (var0 == null) {
         return -1;
      } else if (var0.length <= 1) {
         return -1;
      } else {
         boolean var3 = false;
         boolean var6 = true;
         int var7 = var0.length;
         int var5 = Integer.MAX_VALUE;
         int var4 = 0;
         int var8 = var0.length;

         int var1;
         int var2;
         for(var2 = 0; var2 < var8; var5 = var1) {
            CharSequence var9 = var0[var2];
            if (var9 == null) {
               var3 = true;
               var1 = 0;
            } else {
               var6 = false;
               var1 = Math.min(var9.length(), var5);
               var4 = Math.max(var9.length(), var4);
            }

            ++var2;
         }

         if (var6) {
            return -1;
         } else if (var4 == 0 && !var3) {
            return -1;
         } else if (var5 == 0) {
            return 0;
         } else {
            var2 = -1;
            var1 = 0;

            int var10;
            while(true) {
               var10 = var2;
               if (var1 >= var5) {
                  break;
               }

               char var12 = var0[0].charAt(var1);
               int var11 = 1;

               while(true) {
                  var10 = var2;
                  if (var11 >= var7) {
                     break;
                  }

                  if (var0[var11].charAt(var1) != var12) {
                     var10 = var1;
                     break;
                  }

                  ++var11;
               }

               if (var10 != -1) {
                  break;
               }

               ++var1;
               var2 = var10;
            }

            return var10 == -1 && var5 != var4 ? var5 : var10;
         }
      }
   }

   public static int indexOfIgnoreCase(CharSequence var0, CharSequence var1) {
      return indexOfIgnoreCase(var0, var1, 0);
   }

   public static int indexOfIgnoreCase(CharSequence var0, CharSequence var1, int var2) {
      if (var0 != null) {
         if (var1 == null) {
            return -1;
         } else {
            int var3 = var2;
            if (var2 < 0) {
               var3 = 0;
            }

            var2 = var0.length() - var1.length() + 1;
            if (var3 > var2) {
               return -1;
            } else if (var1.length() == 0) {
               return var3;
            } else {
               while(var3 < var2) {
                  if (CharSequenceUtils.regionMatches(var0, true, var3, var1, 0, var1.length())) {
                     return var3;
                  }

                  ++var3;
               }

               return -1;
            }
         }
      } else {
         return -1;
      }
   }

   public static boolean isAllBlank(CharSequence... var0) {
      if (ArrayUtils.isEmpty((Object[])var0)) {
         return true;
      } else {
         int var2 = var0.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            if (isNotBlank(var0[var1])) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isAllEmpty(CharSequence... var0) {
      if (ArrayUtils.isEmpty((Object[])var0)) {
         return true;
      } else {
         int var2 = var0.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            if (isNotEmpty(var0[var1])) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isAllLowerCase(CharSequence var0) {
      if (var0 != null) {
         if (isEmpty(var0)) {
            return false;
         } else {
            int var2 = var0.length();

            for(int var1 = 0; var1 < var2; ++var1) {
               if (!Character.isLowerCase(var0.charAt(var1))) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public static boolean isAllUpperCase(CharSequence var0) {
      if (var0 != null) {
         if (isEmpty(var0)) {
            return false;
         } else {
            int var2 = var0.length();

            for(int var1 = 0; var1 < var2; ++var1) {
               if (!Character.isUpperCase(var0.charAt(var1))) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public static boolean isAlpha(CharSequence var0) {
      if (isEmpty(var0)) {
         return false;
      } else {
         int var2 = var0.length();

         for(int var1 = 0; var1 < var2; ++var1) {
            if (!Character.isLetter(var0.charAt(var1))) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isAlphaSpace(CharSequence var0) {
      if (var0 == null) {
         return false;
      } else {
         int var2 = var0.length();

         for(int var1 = 0; var1 < var2; ++var1) {
            if (!Character.isLetter(var0.charAt(var1)) && var0.charAt(var1) != ' ') {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isAlphanumeric(CharSequence var0) {
      if (isEmpty(var0)) {
         return false;
      } else {
         int var2 = var0.length();

         for(int var1 = 0; var1 < var2; ++var1) {
            if (!Character.isLetterOrDigit(var0.charAt(var1))) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isAlphanumericSpace(CharSequence var0) {
      if (var0 == null) {
         return false;
      } else {
         int var2 = var0.length();

         for(int var1 = 0; var1 < var2; ++var1) {
            if (!Character.isLetterOrDigit(var0.charAt(var1)) && var0.charAt(var1) != ' ') {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isAnyBlank(CharSequence... var0) {
      if (ArrayUtils.isEmpty((Object[])var0)) {
         return false;
      } else {
         int var2 = var0.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            if (isBlank(var0[var1])) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean isAnyEmpty(CharSequence... var0) {
      if (ArrayUtils.isEmpty((Object[])var0)) {
         return false;
      } else {
         int var2 = var0.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            if (isEmpty(var0[var1])) {
               return true;
            }
         }

         return false;
      }
   }

   public static boolean isAsciiPrintable(CharSequence var0) {
      if (var0 == null) {
         return false;
      } else {
         int var2 = var0.length();

         for(int var1 = 0; var1 < var2; ++var1) {
            if (!CharUtils.isAsciiPrintable(var0.charAt(var1))) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isBlank(CharSequence var0) {
      if (var0 != null) {
         int var2 = var0.length();
         if (var2 == 0) {
            return true;
         } else {
            for(int var1 = 0; var1 < var2; ++var1) {
               if (!Character.isWhitespace(var0.charAt(var1))) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return true;
      }
   }

   public static boolean isEmpty(CharSequence var0) {
      return var0 == null || var0.length() == 0;
   }

   public static boolean isMixedCase(CharSequence var0) {
      boolean var6 = isEmpty(var0);
      boolean var7 = false;
      if (!var6) {
         if (var0.length() == 1) {
            return false;
         } else {
            boolean var2 = false;
            boolean var3 = false;
            int var5 = var0.length();

            boolean var4;
            for(int var1 = 0; var1 < var5; var2 = var4) {
               if (var2 && var3) {
                  return true;
               }

               if (Character.isUpperCase(var0.charAt(var1))) {
                  var4 = true;
               } else {
                  var4 = var2;
                  if (Character.isLowerCase(var0.charAt(var1))) {
                     var3 = true;
                     var4 = var2;
                  }
               }

               ++var1;
            }

            var6 = var7;
            if (var2) {
               var6 = var7;
               if (var3) {
                  var6 = true;
               }
            }

            return var6;
         }
      } else {
         return false;
      }
   }

   public static boolean isNoneBlank(CharSequence... var0) {
      return isAnyBlank(var0) ^ true;
   }

   public static boolean isNoneEmpty(CharSequence... var0) {
      return isAnyEmpty(var0) ^ true;
   }

   public static boolean isNotBlank(CharSequence var0) {
      return isBlank(var0) ^ true;
   }

   public static boolean isNotEmpty(CharSequence var0) {
      return isEmpty(var0) ^ true;
   }

   public static boolean isNumeric(CharSequence var0) {
      if (isEmpty(var0)) {
         return false;
      } else {
         int var2 = var0.length();

         for(int var1 = 0; var1 < var2; ++var1) {
            if (!Character.isDigit(var0.charAt(var1))) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isNumericSpace(CharSequence var0) {
      if (var0 == null) {
         return false;
      } else {
         int var2 = var0.length();

         for(int var1 = 0; var1 < var2; ++var1) {
            if (!Character.isDigit(var0.charAt(var1)) && var0.charAt(var1) != ' ') {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean isWhitespace(CharSequence var0) {
      if (var0 == null) {
         return false;
      } else {
         int var2 = var0.length();

         for(int var1 = 0; var1 < var2; ++var1) {
            if (!Character.isWhitespace(var0.charAt(var1))) {
               return false;
            }
         }

         return true;
      }
   }

   public static String join(Iterable var0, char var1) {
      return var0 == null ? null : join(var0.iterator(), var1);
   }

   public static String join(Iterable var0, String var1) {
      return var0 == null ? null : join(var0.iterator(), var1);
   }

   public static String join(Iterator var0, char var1) {
      if (var0 == null) {
         return null;
      } else if (!var0.hasNext()) {
         return "";
      } else {
         Object var3 = var0.next();
         if (!var0.hasNext()) {
            return Objects.toString(var3, "");
         } else {
            StringBuilder var2 = new StringBuilder(256);
            if (var3 != null) {
               var2.append(var3);
            }

            while(var0.hasNext()) {
               var2.append(var1);
               var3 = var0.next();
               if (var3 != null) {
                  var2.append(var3);
               }
            }

            return var2.toString();
         }
      }
   }

   public static String join(Iterator var0, String var1) {
      if (var0 == null) {
         return null;
      } else if (!var0.hasNext()) {
         return "";
      } else {
         Object var3 = var0.next();
         if (!var0.hasNext()) {
            return Objects.toString(var3, "");
         } else {
            StringBuilder var2 = new StringBuilder(256);
            if (var3 != null) {
               var2.append(var3);
            }

            while(var0.hasNext()) {
               if (var1 != null) {
                  var2.append(var1);
               }

               var3 = var0.next();
               if (var3 != null) {
                  var2.append(var3);
               }
            }

            return var2.toString();
         }
      }
   }

   public static String join(List var0, char var1, int var2, int var3) {
      if (var0 == null) {
         return null;
      } else {
         return var3 - var2 <= 0 ? "" : join(var0.subList(var2, var3).iterator(), var1);
      }
   }

   public static String join(List var0, String var1, int var2, int var3) {
      if (var0 == null) {
         return null;
      } else {
         return var3 - var2 <= 0 ? "" : join(var0.subList(var2, var3).iterator(), var1);
      }
   }

   public static String join(byte[] var0, char var1) {
      return var0 == null ? null : join((byte[])var0, var1, 0, var0.length);
   }

   public static String join(byte[] var0, char var1, int var2, int var3) {
      if (var0 == null) {
         return null;
      } else {
         int var4 = var3 - var2;
         if (var4 <= 0) {
            return "";
         } else {
            StringBuilder var5 = newStringBuilder(var4);

            for(var4 = var2; var4 < var3; ++var4) {
               if (var4 > var2) {
                  var5.append(var1);
               }

               var5.append(var0[var4]);
            }

            return var5.toString();
         }
      }
   }

   public static String join(char[] var0, char var1) {
      return var0 == null ? null : join((char[])var0, var1, 0, var0.length);
   }

   public static String join(char[] var0, char var1, int var2, int var3) {
      if (var0 == null) {
         return null;
      } else {
         int var4 = var3 - var2;
         if (var4 <= 0) {
            return "";
         } else {
            StringBuilder var5 = newStringBuilder(var4);

            for(var4 = var2; var4 < var3; ++var4) {
               if (var4 > var2) {
                  var5.append(var1);
               }

               var5.append(var0[var4]);
            }

            return var5.toString();
         }
      }
   }

   public static String join(double[] var0, char var1) {
      return var0 == null ? null : join((double[])var0, var1, 0, var0.length);
   }

   public static String join(double[] var0, char var1, int var2, int var3) {
      if (var0 == null) {
         return null;
      } else {
         int var4 = var3 - var2;
         if (var4 <= 0) {
            return "";
         } else {
            StringBuilder var5 = newStringBuilder(var4);

            for(var4 = var2; var4 < var3; ++var4) {
               if (var4 > var2) {
                  var5.append(var1);
               }

               var5.append(var0[var4]);
            }

            return var5.toString();
         }
      }
   }

   public static String join(float[] var0, char var1) {
      return var0 == null ? null : join((float[])var0, var1, 0, var0.length);
   }

   public static String join(float[] var0, char var1, int var2, int var3) {
      if (var0 == null) {
         return null;
      } else {
         int var4 = var3 - var2;
         if (var4 <= 0) {
            return "";
         } else {
            StringBuilder var5 = newStringBuilder(var4);

            for(var4 = var2; var4 < var3; ++var4) {
               if (var4 > var2) {
                  var5.append(var1);
               }

               var5.append(var0[var4]);
            }

            return var5.toString();
         }
      }
   }

   public static String join(int[] var0, char var1) {
      return var0 == null ? null : join((int[])var0, var1, 0, var0.length);
   }

   public static String join(int[] var0, char var1, int var2, int var3) {
      if (var0 == null) {
         return null;
      } else {
         int var4 = var3 - var2;
         if (var4 <= 0) {
            return "";
         } else {
            StringBuilder var5 = newStringBuilder(var4);

            for(var4 = var2; var4 < var3; ++var4) {
               if (var4 > var2) {
                  var5.append(var1);
               }

               var5.append(var0[var4]);
            }

            return var5.toString();
         }
      }
   }

   public static String join(long[] var0, char var1) {
      return var0 == null ? null : join((long[])var0, var1, 0, var0.length);
   }

   public static String join(long[] var0, char var1, int var2, int var3) {
      if (var0 == null) {
         return null;
      } else {
         int var4 = var3 - var2;
         if (var4 <= 0) {
            return "";
         } else {
            StringBuilder var5 = newStringBuilder(var4);

            for(var4 = var2; var4 < var3; ++var4) {
               if (var4 > var2) {
                  var5.append(var1);
               }

               var5.append(var0[var4]);
            }

            return var5.toString();
         }
      }
   }

   @SafeVarargs
   public static String join(Object... var0) {
      return join((Object[])var0, (String)null);
   }

   public static String join(Object[] var0, char var1) {
      return var0 == null ? null : join((Object[])var0, var1, 0, var0.length);
   }

   public static String join(Object[] var0, char var1, int var2, int var3) {
      if (var0 == null) {
         return null;
      } else {
         int var4 = var3 - var2;
         if (var4 <= 0) {
            return "";
         } else {
            StringBuilder var5 = newStringBuilder(var4);

            for(var4 = var2; var4 < var3; ++var4) {
               if (var4 > var2) {
                  var5.append(var1);
               }

               if (var0[var4] != null) {
                  var5.append(var0[var4]);
               }
            }

            return var5.toString();
         }
      }
   }

   public static String join(Object[] var0, String var1) {
      return var0 == null ? null : join((Object[])var0, var1, 0, var0.length);
   }

   public static String join(Object[] var0, String var1, int var2, int var3) {
      if (var0 == null) {
         return null;
      } else {
         String var5 = var1;
         if (var1 == null) {
            var5 = "";
         }

         int var4 = var3 - var2;
         if (var4 <= 0) {
            return "";
         } else {
            StringBuilder var6 = newStringBuilder(var4);

            for(var4 = var2; var4 < var3; ++var4) {
               if (var4 > var2) {
                  var6.append(var5);
               }

               if (var0[var4] != null) {
                  var6.append(var0[var4]);
               }
            }

            return var6.toString();
         }
      }
   }

   public static String join(short[] var0, char var1) {
      return var0 == null ? null : join((short[])var0, var1, 0, var0.length);
   }

   public static String join(short[] var0, char var1, int var2, int var3) {
      if (var0 == null) {
         return null;
      } else {
         int var4 = var3 - var2;
         if (var4 <= 0) {
            return "";
         } else {
            StringBuilder var5 = newStringBuilder(var4);

            for(var4 = var2; var4 < var3; ++var4) {
               if (var4 > var2) {
                  var5.append(var1);
               }

               var5.append(var0[var4]);
            }

            return var5.toString();
         }
      }
   }

   public static String joinWith(String var0, Object... var1) {
      if (var1 != null) {
         var0 = defaultString(var0);
         StringBuilder var2 = new StringBuilder();
         Iterator var3 = Arrays.asList(var1).iterator();

         while(var3.hasNext()) {
            var2.append(Objects.toString(var3.next(), ""));
            if (var3.hasNext()) {
               var2.append(var0);
            }
         }

         return var2.toString();
      } else {
         throw new IllegalArgumentException("Object varargs must not be null");
      }
   }

   public static int lastIndexOf(CharSequence var0, int var1) {
      return isEmpty(var0) ? -1 : CharSequenceUtils.lastIndexOf(var0, var1, var0.length());
   }

   public static int lastIndexOf(CharSequence var0, int var1, int var2) {
      return isEmpty(var0) ? -1 : CharSequenceUtils.lastIndexOf(var0, var1, var2);
   }

   public static int lastIndexOf(CharSequence var0, CharSequence var1) {
      return var0 != null && var1 != null ? CharSequenceUtils.lastIndexOf(var0, var1, var0.length()) : -1;
   }

   public static int lastIndexOf(CharSequence var0, CharSequence var1, int var2) {
      return var0 != null && var1 != null ? CharSequenceUtils.lastIndexOf(var0, var1, var2) : -1;
   }

   public static int lastIndexOfAny(CharSequence var0, CharSequence... var1) {
      if (var0 != null && var1 != null) {
         int var3 = -1;
         int var6 = var1.length;

         int var4;
         for(int var2 = 0; var2 < var6; var3 = var4) {
            CharSequence var7 = var1[var2];
            if (var7 == null) {
               var4 = var3;
            } else {
               int var5 = CharSequenceUtils.lastIndexOf(var0, var7, var0.length());
               var4 = var3;
               if (var5 > var3) {
                  var4 = var5;
               }
            }

            ++var2;
         }

         return var3;
      } else {
         return -1;
      }
   }

   public static int lastIndexOfIgnoreCase(CharSequence var0, CharSequence var1) {
      return var0 != null && var1 != null ? lastIndexOfIgnoreCase(var0, var1, var0.length()) : -1;
   }

   public static int lastIndexOfIgnoreCase(CharSequence var0, CharSequence var1, int var2) {
      if (var0 != null) {
         if (var1 == null) {
            return -1;
         } else {
            int var3 = var2;
            if (var2 > var0.length() - var1.length()) {
               var3 = var0.length() - var1.length();
            }

            if (var3 < 0) {
               return -1;
            } else if (var1.length() == 0) {
               return var3;
            } else {
               while(var3 >= 0) {
                  if (CharSequenceUtils.regionMatches(var0, true, var3, var1, 0, var1.length())) {
                     return var3;
                  }

                  --var3;
               }

               return -1;
            }
         }
      } else {
         return -1;
      }
   }

   public static int lastOrdinalIndexOf(CharSequence var0, CharSequence var1, int var2) {
      return ordinalIndexOf(var0, var1, var2, true);
   }

   public static String left(String var0, int var1) {
      if (var0 == null) {
         return null;
      } else if (var1 < 0) {
         return "";
      } else {
         return var0.length() <= var1 ? var0 : var0.substring(0, var1);
      }
   }

   public static String leftPad(String var0, int var1) {
      return leftPad(var0, var1, ' ');
   }

   public static String leftPad(String var0, int var1, char var2) {
      if (var0 == null) {
         return null;
      } else {
         int var3 = var1 - var0.length();
         if (var3 <= 0) {
            return var0;
         } else {
            return var3 > 8192 ? leftPad(var0, var1, String.valueOf(var2)) : repeat(var2, var3).concat(var0);
         }
      }
   }

   public static String leftPad(String var0, int var1, String var2) {
      if (var0 == null) {
         return null;
      } else {
         String var5 = var2;
         if (isEmpty(var2)) {
            var5 = " ";
         }

         int var3 = var5.length();
         int var4 = var1 - var0.length();
         if (var4 <= 0) {
            return var0;
         } else if (var3 == 1 && var4 <= 8192) {
            return leftPad(var0, var1, var5.charAt(0));
         } else if (var4 == var3) {
            return var5.concat(var0);
         } else if (var4 < var3) {
            return var5.substring(0, var4).concat(var0);
         } else {
            char[] var6 = new char[var4];
            char[] var7 = var5.toCharArray();

            for(var1 = 0; var1 < var4; ++var1) {
               var6[var1] = var7[var1 % var3];
            }

            return (new String(var6)).concat(var0);
         }
      }
   }

   public static int length(CharSequence var0) {
      return var0 == null ? 0 : var0.length();
   }

   public static String lowerCase(String var0) {
      return var0 == null ? null : var0.toLowerCase();
   }

   public static String lowerCase(String var0, Locale var1) {
      return var0 == null ? null : var0.toLowerCase(var1);
   }

   private static int[] matches(CharSequence var0, CharSequence var1) {
      CharSequence var9;
      CharSequence var10;
      if (var0.length() > var1.length()) {
         var9 = var0;
         var10 = var1;
      } else {
         var9 = var1;
         var10 = var0;
      }

      int var6 = Math.max(var9.length() / 2 - 1, 0);
      int[] var11 = new int[var10.length()];
      Arrays.fill(var11, -1);
      boolean[] var12 = new boolean[var9.length()];
      int var2 = 0;

      int var3;
      int var4;
      int var5;
      for(var3 = 0; var3 < var10.length(); var2 = var4) {
         char var7 = var10.charAt(var3);
         var5 = Math.max(var3 - var6, 0);
         int var8 = Math.min(var3 + var6 + 1, var9.length());

         while(true) {
            var4 = var2;
            if (var5 >= var8) {
               break;
            }

            if (!var12[var5] && var7 == var9.charAt(var5)) {
               var11[var3] = var5;
               var12[var5] = true;
               var4 = var2 + 1;
               break;
            }

            ++var5;
         }

         ++var3;
      }

      char[] var13 = new char[var2];
      char[] var14 = new char[var2];
      var3 = 0;

      for(var4 = 0; var3 < var10.length(); var4 = var5) {
         var5 = var4;
         if (var11[var3] != -1) {
            var13[var4] = var10.charAt(var3);
            var5 = var4 + 1;
         }

         ++var3;
      }

      var3 = 0;

      for(var4 = 0; var3 < var9.length(); var4 = var5) {
         var5 = var4;
         if (var12[var3]) {
            var14[var4] = var9.charAt(var3);
            var5 = var4 + 1;
         }

         ++var3;
      }

      var3 = 0;

      for(var4 = 0; var4 < var13.length; var3 = var5) {
         var5 = var3;
         if (var13[var4] != var14[var4]) {
            var5 = var3 + 1;
         }

         ++var4;
      }

      var5 = 0;

      for(var4 = 0; var4 < var10.length() && var0.charAt(var4) == var1.charAt(var4); ++var4) {
         ++var5;
      }

      return new int[]{var2, var3 / 2, var5, var9.length()};
   }

   public static String mid(String var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else if (var2 >= 0 && var1 <= var0.length()) {
         int var3 = var1;
         if (var1 < 0) {
            var3 = 0;
         }

         return var0.length() <= var3 + var2 ? var0.substring(var3) : var0.substring(var3, var3 + var2);
      } else {
         return "";
      }
   }

   private static StringBuilder newStringBuilder(int var0) {
      return new StringBuilder(var0 * 16);
   }

   public static String normalizeSpace(String var0) {
      if (isEmpty(var0)) {
         return var0;
      } else {
         int var7 = var0.length();
         char[] var8 = new char[var7];
         int var2 = 0;
         int var6 = 0;
         boolean var5 = true;

         int var3;
         for(int var4 = 0; var4 < var7; var6 = var3) {
            char var1 = var0.charAt(var4);
            if (Character.isWhitespace(var1)) {
               var3 = var2;
               if (var6 == 0) {
                  var3 = var2;
                  if (!var5) {
                     var8[var2] = " ".charAt(0);
                     var3 = var2 + 1;
                  }
               }

               ++var6;
               var2 = var3;
               var3 = var6;
            } else {
               var5 = false;
               if (var1 == 160) {
                  var1 = ' ';
               }

               var8[var2] = var1;
               var3 = 0;
               ++var2;
            }

            ++var4;
         }

         if (var5) {
            return "";
         } else {
            byte var9;
            if (var6 > 0) {
               var9 = 1;
            } else {
               var9 = 0;
            }

            return (new String(var8, 0, var2 - var9)).trim();
         }
      }
   }

   public static int ordinalIndexOf(CharSequence var0, CharSequence var1, int var2) {
      return ordinalIndexOf(var0, var1, var2, false);
   }

   private static int ordinalIndexOf(CharSequence var0, CharSequence var1, int var2, boolean var3) {
      int var4 = -1;
      if (var0 != null && var1 != null) {
         if (var2 <= 0) {
            return -1;
         } else if (var1.length() == 0) {
            return var3 ? var0.length() : 0;
         } else {
            byte var5 = 0;
            int var6 = var5;
            if (var3) {
               var4 = var0.length();
               var6 = var5;
            }

            int var7;
            int var8;
            do {
               if (var3) {
                  var8 = CharSequenceUtils.lastIndexOf(var0, var1, var4 - 1);
               } else {
                  var8 = CharSequenceUtils.indexOf(var0, var1, var4 + 1);
               }

               if (var8 < 0) {
                  return var8;
               }

               var7 = var6 + 1;
               var4 = var8;
               var6 = var7;
            } while(var7 < var2);

            return var8;
         }
      } else {
         return -1;
      }
   }

   public static String overlay(String var0, String var1, int var2, int var3) {
      if (var0 == null) {
         return null;
      } else {
         String var6 = var1;
         if (var1 == null) {
            var6 = "";
         }

         int var5 = var0.length();
         int var4 = var2;
         if (var2 < 0) {
            var4 = 0;
         }

         var2 = var4;
         if (var4 > var5) {
            var2 = var5;
         }

         var4 = var3;
         if (var3 < 0) {
            var4 = 0;
         }

         var3 = var4;
         if (var4 > var5) {
            var3 = var5;
         }

         var5 = var2;
         var4 = var3;
         if (var2 > var3) {
            var4 = var2;
            var5 = var3;
         }

         StringBuilder var7 = new StringBuilder();
         var7.append(var0.substring(0, var5));
         var7.append(var6);
         var7.append(var0.substring(var4));
         return var7.toString();
      }
   }

   private static String prependIfMissing(String var0, CharSequence var1, boolean var2, CharSequence... var3) {
      if (var0 != null && !isEmpty(var1)) {
         if (startsWith(var0, var1, var2)) {
            return var0;
         } else {
            if (var3 != null && var3.length > 0) {
               int var5 = var3.length;

               for(int var4 = 0; var4 < var5; ++var4) {
                  if (startsWith(var0, var3[var4], var2)) {
                     return var0;
                  }
               }
            }

            StringBuilder var6 = new StringBuilder();
            var6.append(var1.toString());
            var6.append(var0);
            return var6.toString();
         }
      } else {
         return var0;
      }
   }

   public static String prependIfMissing(String var0, CharSequence var1, CharSequence... var2) {
      return prependIfMissing(var0, var1, false, var2);
   }

   public static String prependIfMissingIgnoreCase(String var0, CharSequence var1, CharSequence... var2) {
      return prependIfMissing(var0, var1, true, var2);
   }

   public static String remove(String var0, char var1) {
      if (!isEmpty(var0)) {
         if (var0.indexOf(var1) == -1) {
            return var0;
         } else {
            char[] var5 = var0.toCharArray();
            int var3 = 0;

            int var4;
            for(int var2 = 0; var2 < var5.length; var3 = var4) {
               var4 = var3;
               if (var5[var2] != var1) {
                  var5[var3] = var5[var2];
                  var4 = var3 + 1;
               }

               ++var2;
            }

            return new String(var5, 0, var3);
         }
      } else {
         return var0;
      }
   }

   public static String remove(String var0, String var1) {
      if (!isEmpty(var0)) {
         return isEmpty(var1) ? var0 : replace(var0, var1, "", -1);
      } else {
         return var0;
      }
   }

   @Deprecated
   public static String removeAll(String var0, String var1) {
      return RegExUtils.removeAll(var0, var1);
   }

   public static String removeEnd(String var0, String var1) {
      if (!isEmpty(var0)) {
         if (isEmpty(var1)) {
            return var0;
         } else {
            return var0.endsWith(var1) ? var0.substring(0, var0.length() - var1.length()) : var0;
         }
      } else {
         return var0;
      }
   }

   public static String removeEndIgnoreCase(String var0, String var1) {
      if (!isEmpty(var0)) {
         if (isEmpty(var1)) {
            return var0;
         } else {
            return endsWithIgnoreCase(var0, var1) ? var0.substring(0, var0.length() - var1.length()) : var0;
         }
      } else {
         return var0;
      }
   }

   @Deprecated
   public static String removeFirst(String var0, String var1) {
      return replaceFirst(var0, var1, "");
   }

   public static String removeIgnoreCase(String var0, String var1) {
      if (!isEmpty(var0)) {
         return isEmpty(var1) ? var0 : replaceIgnoreCase(var0, var1, "", -1);
      } else {
         return var0;
      }
   }

   @Deprecated
   public static String removePattern(String var0, String var1) {
      return RegExUtils.removePattern(var0, var1);
   }

   public static String removeStart(String var0, String var1) {
      if (!isEmpty(var0)) {
         if (isEmpty(var1)) {
            return var0;
         } else {
            return var0.startsWith(var1) ? var0.substring(var1.length()) : var0;
         }
      } else {
         return var0;
      }
   }

   public static String removeStartIgnoreCase(String var0, String var1) {
      if (!isEmpty(var0)) {
         if (isEmpty(var1)) {
            return var0;
         } else {
            return startsWithIgnoreCase(var0, var1) ? var0.substring(var1.length()) : var0;
         }
      } else {
         return var0;
      }
   }

   public static String repeat(char var0, int var1) {
      if (var1 <= 0) {
         return "";
      } else {
         char[] var2 = new char[var1];
         --var1;

         while(var1 >= 0) {
            var2[var1] = var0;
            --var1;
         }

         return new String(var2);
      }
   }

   public static String repeat(String var0, int var1) {
      if (var0 == null) {
         return null;
      } else if (var1 <= 0) {
         return "";
      } else {
         int var4 = var0.length();
         if (var1 == 1) {
            return var0;
         } else if (var4 == 0) {
            return var0;
         } else if (var4 == 1 && var1 <= 8192) {
            return repeat(var0.charAt(0), var1);
         } else {
            int var5 = var4 * var1;
            if (var4 == 1) {
               return repeat(var0.charAt(0), var1);
            } else if (var4 != 2) {
               StringBuilder var6 = new StringBuilder(var5);

               for(var4 = 0; var4 < var1; ++var4) {
                  var6.append(var0);
               }

               return var6.toString();
            } else {
               char var2 = var0.charAt(0);
               char var3 = var0.charAt(1);
               char[] var7 = new char[var5];

               for(var1 = var1 * 2 - 2; var1 >= 0; var1 = var1 - 1 - 1) {
                  var7[var1] = var2;
                  var7[var1 + 1] = var3;
               }

               return new String(var7);
            }
         }
      }
   }

   public static String repeat(String var0, String var1, int var2) {
      if (var0 != null && var1 != null) {
         StringBuilder var3 = new StringBuilder();
         var3.append(var0);
         var3.append(var1);
         return removeEnd(repeat(var3.toString(), var2), var1);
      } else {
         return repeat(var0, var2);
      }
   }

   public static String replace(String var0, String var1, String var2) {
      return replace(var0, var1, var2, -1);
   }

   public static String replace(String var0, String var1, String var2, int var3) {
      return replace(var0, var1, var2, var3, false);
   }

   private static String replace(String var0, String var1, String var2, int var3, boolean var4) {
      if (!isEmpty(var0) && !isEmpty(var1) && var2 != null) {
         if (var3 == 0) {
            return var0;
         } else {
            String var11 = var0;
            String var10 = var1;
            if (var4) {
               var11 = var0.toLowerCase();
               var10 = var1.toLowerCase();
            }

            byte var7 = 0;
            int var8 = var11.indexOf(var10, 0);
            if (var8 == -1) {
               return var0;
            } else {
               int var9 = var10.length();
               int var6 = var2.length() - var9;
               if (var6 < 0) {
                  var6 = 0;
               }

               int var5 = 64;
               if (var3 < 0) {
                  var5 = 16;
               } else if (var3 <= 64) {
                  var5 = var3;
               }

               StringBuilder var12 = new StringBuilder(var0.length() + var6 * var5);
               var5 = var3;
               var6 = var8;
               var3 = var7;

               int var13;
               while(true) {
                  var13 = var3;
                  if (var6 == -1) {
                     break;
                  }

                  var12.append(var0, var3, var6);
                  var12.append(var2);
                  var3 = var6 + var9;
                  --var5;
                  if (var5 == 0) {
                     var13 = var3;
                     break;
                  }

                  var6 = var11.indexOf(var10, var3);
               }

               var12.append(var0, var13, var0.length());
               return var12.toString();
            }
         }
      } else {
         return var0;
      }
   }

   @Deprecated
   public static String replaceAll(String var0, String var1, String var2) {
      return RegExUtils.replaceAll(var0, var1, var2);
   }

   public static String replaceChars(String var0, char var1, char var2) {
      return var0 == null ? null : var0.replace(var1, var2);
   }

   public static String replaceChars(String var0, String var1, String var2) {
      if (!isEmpty(var0)) {
         if (isEmpty(var1)) {
            return var0;
         } else {
            String var10 = var2;
            if (var2 == null) {
               var10 = "";
            }

            boolean var5 = false;
            int var7 = var10.length();
            int var8 = var0.length();
            StringBuilder var11 = new StringBuilder(var8);

            for(int var4 = 0; var4 < var8; ++var4) {
               char var3 = var0.charAt(var4);
               int var9 = var1.indexOf(var3);
               if (var9 >= 0) {
                  boolean var6 = true;
                  var5 = var6;
                  if (var9 < var7) {
                     var11.append(var10.charAt(var9));
                     var5 = var6;
                  }
               } else {
                  var11.append(var3);
               }
            }

            if (var5) {
               return var11.toString();
            } else {
               return var0;
            }
         }
      } else {
         return var0;
      }
   }

   public static String replaceEach(String var0, String[] var1, String[] var2) {
      return replaceEach(var0, var1, var2, false, 0);
   }

   private static String replaceEach(String var0, String[] var1, String[] var2, boolean var3, int var4) {
      if (var0 != null && !var0.isEmpty() && var1 != null && var1.length != 0 && var2 != null) {
         if (var2.length == 0) {
            return var0;
         } else if (var4 < 0) {
            throw new IllegalStateException("Aborting to protect against StackOverflowError - output of one loop is the input of another");
         } else {
            int var11 = var1.length;
            int var5 = var2.length;
            if (var11 != var5) {
               StringBuilder var15 = new StringBuilder();
               var15.append("Search and Replace array lengths don't match: ");
               var15.append(var11);
               var15.append(" vs ");
               var15.append(var5);
               throw new IllegalArgumentException(var15.toString());
            } else {
               boolean[] var13 = new boolean[var11];
               int var6 = -1;
               int var7 = -1;

               int var8;
               int var9;
               int var10;
               for(var5 = 0; var5 < var11; var7 = var8) {
                  var9 = var6;
                  var8 = var7;
                  if (!var13[var5]) {
                     var9 = var6;
                     var8 = var7;
                     if (var1[var5] != null) {
                        var9 = var6;
                        var8 = var7;
                        if (!var1[var5].isEmpty()) {
                           if (var2[var5] == null) {
                              var9 = var6;
                              var8 = var7;
                           } else {
                              var10 = var0.indexOf(var1[var5]);
                              if (var10 == -1) {
                                 var13[var5] = true;
                                 var9 = var6;
                                 var8 = var7;
                              } else {
                                 label158: {
                                    if (var6 != -1) {
                                       var9 = var6;
                                       var8 = var7;
                                       if (var10 >= var6) {
                                          break label158;
                                       }
                                    }

                                    var9 = var10;
                                    var8 = var5;
                                 }
                              }
                           }
                        }
                     }
                  }

                  ++var5;
                  var6 = var9;
               }

               if (var6 == -1) {
                  return var0;
               } else {
                  byte var16 = 0;
                  var8 = 0;

                  for(var5 = 0; var5 < var1.length; var8 = var9) {
                     var9 = var8;
                     if (var1[var5] != null) {
                        if (var2[var5] == null) {
                           var9 = var8;
                        } else {
                           int var12 = var2[var5].length() - var1[var5].length();
                           var9 = var8;
                           if (var12 > 0) {
                              var9 = var8 + var12 * 3;
                           }
                        }
                     }

                     ++var5;
                  }

                  var5 = Math.min(var8, var0.length() / 5);
                  StringBuilder var14 = new StringBuilder(var0.length() + var5);
                  var5 = var16;
                  var8 = var7;

                  for(var7 = var6; var7 != -1; var5 = var9) {
                     while(var5 < var7) {
                        var14.append(var0.charAt(var5));
                        ++var5;
                     }

                     var14.append(var2[var8]);
                     var9 = var7 + var1[var8].length();
                     var7 = -1;
                     var6 = -1;

                     for(var5 = 0; var5 < var11; var7 = var8) {
                        if (!var13[var5] && var1[var5] != null) {
                           if (!var1[var5].isEmpty()) {
                              if (var2[var5] == null) {
                                 var8 = var7;
                              } else {
                                 var10 = var0.indexOf(var1[var5], var9);
                                 if (var10 == -1) {
                                    var13[var5] = true;
                                    var8 = var7;
                                 } else {
                                    label164: {
                                       if (var7 != -1) {
                                          var8 = var7;
                                          if (var10 >= var7) {
                                             break label164;
                                          }
                                       }

                                       var8 = var10;
                                       var6 = var5;
                                    }
                                 }
                              }
                           } else {
                              var8 = var7;
                           }
                        } else {
                           var8 = var7;
                        }

                        ++var5;
                     }

                     var8 = var6;
                  }

                  for(var6 = var0.length(); var5 < var6; ++var5) {
                     var14.append(var0.charAt(var5));
                  }

                  var0 = var14.toString();
                  if (!var3) {
                     return var0;
                  } else {
                     return replaceEach(var0, var1, var2, var3, var4 - 1);
                  }
               }
            }
         }
      } else {
         return var0;
      }
   }

   public static String replaceEachRepeatedly(String var0, String[] var1, String[] var2) {
      int var3;
      if (var1 == null) {
         var3 = 0;
      } else {
         var3 = var1.length;
      }

      return replaceEach(var0, var1, var2, true, var3);
   }

   @Deprecated
   public static String replaceFirst(String var0, String var1, String var2) {
      return RegExUtils.replaceFirst(var0, var1, var2);
   }

   public static String replaceIgnoreCase(String var0, String var1, String var2) {
      return replaceIgnoreCase(var0, var1, var2, -1);
   }

   public static String replaceIgnoreCase(String var0, String var1, String var2, int var3) {
      return replace(var0, var1, var2, var3, true);
   }

   public static String replaceOnce(String var0, String var1, String var2) {
      return replace(var0, var1, var2, 1);
   }

   public static String replaceOnceIgnoreCase(String var0, String var1, String var2) {
      return replaceIgnoreCase(var0, var1, var2, 1);
   }

   @Deprecated
   public static String replacePattern(String var0, String var1, String var2) {
      return RegExUtils.replacePattern(var0, var1, var2);
   }

   public static String reverse(String var0) {
      return var0 == null ? null : (new StringBuilder(var0)).reverse().toString();
   }

   public static String reverseDelimited(String var0, char var1) {
      if (var0 == null) {
         return null;
      } else {
         String[] var2 = split(var0, var1);
         ArrayUtils.reverse((Object[])var2);
         return join((Object[])var2, var1);
      }
   }

   public static String right(String var0, int var1) {
      if (var0 == null) {
         return null;
      } else if (var1 < 0) {
         return "";
      } else {
         return var0.length() <= var1 ? var0 : var0.substring(var0.length() - var1);
      }
   }

   public static String rightPad(String var0, int var1) {
      return rightPad(var0, var1, ' ');
   }

   public static String rightPad(String var0, int var1, char var2) {
      if (var0 == null) {
         return null;
      } else {
         int var3 = var1 - var0.length();
         if (var3 <= 0) {
            return var0;
         } else {
            return var3 > 8192 ? rightPad(var0, var1, String.valueOf(var2)) : var0.concat(repeat(var2, var3));
         }
      }
   }

   public static String rightPad(String var0, int var1, String var2) {
      if (var0 == null) {
         return null;
      } else {
         String var5 = var2;
         if (isEmpty(var2)) {
            var5 = " ";
         }

         int var3 = var5.length();
         int var4 = var1 - var0.length();
         if (var4 <= 0) {
            return var0;
         } else if (var3 == 1 && var4 <= 8192) {
            return rightPad(var0, var1, var5.charAt(0));
         } else if (var4 == var3) {
            return var0.concat(var5);
         } else if (var4 < var3) {
            return var0.concat(var5.substring(0, var4));
         } else {
            char[] var6 = new char[var4];
            char[] var7 = var5.toCharArray();

            for(var1 = 0; var1 < var4; ++var1) {
               var6[var1] = var7[var1 % var3];
            }

            return var0.concat(new String(var6));
         }
      }
   }

   public static String rotate(String var0, int var1) {
      if (var0 == null) {
         return null;
      } else {
         int var2 = var0.length();
         if (var1 != 0 && var2 != 0) {
            if (var1 % var2 == 0) {
               return var0;
            } else {
               StringBuilder var3 = new StringBuilder(var2);
               var1 = -(var1 % var2);
               var3.append(substring(var0, var1));
               var3.append(substring(var0, 0, var1));
               return var3.toString();
            }
         } else {
            return var0;
         }
      }
   }

   public static String[] split(String var0) {
      return split(var0, (String)null, -1);
   }

   public static String[] split(String var0, char var1) {
      return splitWorker(var0, var1, false);
   }

   public static String[] split(String var0, String var1) {
      return splitWorker(var0, var1, -1, false);
   }

   public static String[] split(String var0, String var1, int var2) {
      return splitWorker(var0, var1, var2, false);
   }

   public static String[] splitByCharacterType(String var0) {
      return splitByCharacterType(var0, false);
   }

   private static String[] splitByCharacterType(String var0, boolean var1) {
      if (var0 == null) {
         return null;
      } else if (var0.isEmpty()) {
         return ArrayUtils.EMPTY_STRING_ARRAY;
      } else {
         char[] var8 = var0.toCharArray();
         ArrayList var7 = new ArrayList();
         int var3 = 0;
         int var4 = Character.getType(var8[0]);

         int var5;
         for(int var2 = 0 + 1; var2 < var8.length; var4 = var5) {
            var5 = Character.getType(var8[var2]);
            if (var5 == var4) {
               var5 = var4;
            } else {
               if (var1 && var5 == 2 && var4 == 1) {
                  int var6 = var2 - 1;
                  var4 = var3;
                  if (var6 != var3) {
                     var7.add(new String(var8, var3, var6 - var3));
                     var4 = var6;
                  }
               } else {
                  var7.add(new String(var8, var3, var2 - var3));
                  var4 = var2;
               }

               var3 = var4;
            }

            ++var2;
         }

         var7.add(new String(var8, var3, var8.length - var3));
         return (String[])var7.toArray(new String[var7.size()]);
      }
   }

   public static String[] splitByCharacterTypeCamelCase(String var0) {
      return splitByCharacterType(var0, true);
   }

   public static String[] splitByWholeSeparator(String var0, String var1) {
      return splitByWholeSeparatorWorker(var0, var1, -1, false);
   }

   public static String[] splitByWholeSeparator(String var0, String var1, int var2) {
      return splitByWholeSeparatorWorker(var0, var1, var2, false);
   }

   public static String[] splitByWholeSeparatorPreserveAllTokens(String var0, String var1) {
      return splitByWholeSeparatorWorker(var0, var1, -1, true);
   }

   public static String[] splitByWholeSeparatorPreserveAllTokens(String var0, String var1, int var2) {
      return splitByWholeSeparatorWorker(var0, var1, var2, true);
   }

   private static String[] splitByWholeSeparatorWorker(String var0, String var1, int var2, boolean var3) {
      if (var0 == null) {
         return null;
      } else {
         int var5 = var0.length();
         if (var5 == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
         } else if (var1 != null && !"".equals(var1)) {
            int var10 = var1.length();
            ArrayList var11 = new ArrayList();
            int var7 = 0;
            int var6 = 0;
            int var4 = 0;

            while(var4 < var5) {
               int var8 = var0.indexOf(var1, var6);
               if (var8 > -1) {
                  if (var8 > var6) {
                     ++var7;
                     if (var7 == var2) {
                        var4 = var5;
                        var11.add(var0.substring(var6));
                     } else {
                        var11.add(var0.substring(var6, var8));
                        var6 = var8 + var10;
                        var4 = var8;
                     }
                  } else {
                     int var9 = var7;
                     var4 = var8;
                     if (var3) {
                        var9 = var7 + 1;
                        if (var9 == var2) {
                           var4 = var5;
                           var11.add(var0.substring(var6));
                        } else {
                           var11.add("");
                           var4 = var8;
                        }
                     }

                     var6 = var4 + var10;
                     var7 = var9;
                  }
               } else {
                  var11.add(var0.substring(var6));
                  var4 = var5;
               }
            }

            return (String[])var11.toArray(new String[var11.size()]);
         } else {
            return splitWorker(var0, (String)null, var2, var3);
         }
      }
   }

   public static String[] splitPreserveAllTokens(String var0) {
      return splitWorker(var0, (String)null, -1, true);
   }

   public static String[] splitPreserveAllTokens(String var0, char var1) {
      return splitWorker(var0, var1, true);
   }

   public static String[] splitPreserveAllTokens(String var0, String var1) {
      return splitWorker(var0, var1, -1, true);
   }

   public static String[] splitPreserveAllTokens(String var0, String var1, int var2) {
      return splitWorker(var0, var1, var2, true);
   }

   private static String[] splitWorker(String var0, char var1, boolean var2) {
      if (var0 == null) {
         return null;
      } else {
         int var7 = var0.length();
         if (var7 == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
         } else {
            ArrayList var8 = new ArrayList();
            int var3 = 0;
            int var6 = 0;
            boolean var4 = false;
            boolean var5 = false;

            while(true) {
               while(var3 < var7) {
                  if (var0.charAt(var3) == var1) {
                     if (var4 || var2) {
                        var8.add(var0.substring(var6, var3));
                        var4 = false;
                        var5 = true;
                     }

                     ++var3;
                     var6 = var3;
                  } else {
                     var5 = false;
                     var4 = true;
                     ++var3;
                  }
               }

               if (var4 || var2 && var5) {
                  var8.add(var0.substring(var6, var3));
               }

               return (String[])var8.toArray(new String[var8.size()]);
            }
         }
      }
   }

   private static String[] splitWorker(String var0, String var1, int var2, boolean var3) {
      if (var0 == null) {
         return null;
      } else {
         int var7 = var0.length();
         if (var7 == 0) {
            return ArrayUtils.EMPTY_STRING_ARRAY;
         } else {
            ArrayList var20 = new ArrayList();
            int var9 = 1;
            byte var19 = 1;
            int var13 = 1;
            byte var10 = 0;
            byte var17 = 0;
            int var4 = 0;
            byte var11 = 0;
            byte var15 = 0;
            int var8 = 0;
            boolean var12 = false;
            boolean var5 = false;
            boolean var16 = false;
            boolean var14 = false;
            boolean var6 = false;
            boolean var18 = false;
            boolean var21;
            int var22;
            boolean var23;
            if (var1 == null) {
               var6 = var18;
               var5 = var16;

               label104:
               while(true) {
                  while(true) {
                     var9 = var4;
                     var22 = var8;
                     var23 = var5;
                     var12 = var6;
                     if (var4 >= var7) {
                        break label104;
                     }

                     if (Character.isWhitespace(var0.charAt(var4))) {
                        label114: {
                           if (!var5) {
                              var9 = var13;
                              var22 = var4;
                              if (!var3) {
                                 break label114;
                              }
                           }

                           var5 = true;
                           if (var13 == var2) {
                              var5 = false;
                              var4 = var7;
                           }

                           var20.add(var0.substring(var8, var4));
                           var21 = false;
                           var9 = var13 + 1;
                           var6 = var5;
                           var5 = var21;
                           var22 = var4;
                        }

                        var4 = var22 + 1;
                        var8 = var4;
                        var13 = var9;
                     } else {
                        var6 = false;
                        var5 = true;
                        ++var4;
                     }
                  }
               }
            } else {
               var13 = var19;
               var4 = var17;
               var8 = var15;
               if (var1.length() == 1) {
                  char var25 = var1.charAt(0);
                  var5 = var14;
                  var6 = var12;
                  var8 = var11;
                  var4 = var10;

                  while(true) {
                     while(var4 < var7) {
                        if (var0.charAt(var4) == var25) {
                           int var24;
                           label118: {
                              if (!var6) {
                                 var22 = var9;
                                 var24 = var4;
                                 if (!var3) {
                                    break label118;
                                 }
                              }

                              var5 = true;
                              if (var9 == var2) {
                                 var5 = false;
                                 var4 = var7;
                              }

                              var20.add(var0.substring(var8, var4));
                              var6 = false;
                              var22 = var9 + 1;
                              var24 = var4;
                           }

                           var4 = var24 + 1;
                           var8 = var4;
                           var9 = var22;
                        } else {
                           var5 = false;
                           var6 = true;
                           ++var4;
                        }
                     }

                     var9 = var4;
                     var22 = var8;
                     var23 = var6;
                     var12 = var5;
                     break;
                  }
               } else {
                  label89:
                  while(true) {
                     while(true) {
                        var9 = var4;
                        var22 = var8;
                        var23 = var5;
                        var12 = var6;
                        if (var4 >= var7) {
                           break label89;
                        }

                        if (var1.indexOf(var0.charAt(var4)) >= 0) {
                           label121: {
                              if (!var5) {
                                 var9 = var13;
                                 var22 = var4;
                                 if (!var3) {
                                    break label121;
                                 }
                              }

                              var5 = true;
                              if (var13 == var2) {
                                 var5 = false;
                                 var4 = var7;
                              }

                              var20.add(var0.substring(var8, var4));
                              var21 = false;
                              var9 = var13 + 1;
                              var6 = var5;
                              var5 = var21;
                              var22 = var4;
                           }

                           var4 = var22 + 1;
                           var8 = var4;
                           var13 = var9;
                        } else {
                           var6 = false;
                           var5 = true;
                           ++var4;
                        }
                     }
                  }
               }
            }

            if (var23 || var3 && var12) {
               var20.add(var0.substring(var22, var9));
            }

            return (String[])var20.toArray(new String[var20.size()]);
         }
      }
   }

   public static boolean startsWith(CharSequence var0, CharSequence var1) {
      return startsWith(var0, var1, false);
   }

   private static boolean startsWith(CharSequence var0, CharSequence var1, boolean var2) {
      boolean var3 = false;
      if (var0 != null && var1 != null) {
         return var1.length() > var0.length() ? false : CharSequenceUtils.regionMatches(var0, var2, 0, var1, 0, var1.length());
      } else {
         var2 = var3;
         if (var0 == var1) {
            var2 = true;
         }

         return var2;
      }
   }

   public static boolean startsWithAny(CharSequence var0, CharSequence... var1) {
      if (!isEmpty(var0)) {
         if (ArrayUtils.isEmpty((Object[])var1)) {
            return false;
         } else {
            int var3 = var1.length;

            for(int var2 = 0; var2 < var3; ++var2) {
               if (startsWith(var0, var1[var2])) {
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   public static boolean startsWithIgnoreCase(CharSequence var0, CharSequence var1) {
      return startsWith(var0, var1, true);
   }

   public static String strip(String var0) {
      return strip(var0, (String)null);
   }

   public static String strip(String var0, String var1) {
      return isEmpty(var0) ? var0 : stripEnd(stripStart(var0, var1), var1);
   }

   public static String stripAccents(String var0) {
      if (var0 == null) {
         return null;
      } else {
         Pattern var1 = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
         StringBuilder var2 = new StringBuilder(Normalizer.normalize(var0, Form.NFD));
         convertRemainingAccentCharacters(var2);
         return var1.matcher(var2).replaceAll("");
      }
   }

   public static String[] stripAll(String... var0) {
      return stripAll(var0, (String)null);
   }

   public static String[] stripAll(String[] var0, String var1) {
      if (var0 == null) {
         return var0;
      } else {
         int var3 = var0.length;
         if (var3 == 0) {
            return var0;
         } else {
            String[] var4 = new String[var3];

            for(int var2 = 0; var2 < var3; ++var2) {
               var4[var2] = strip(var0[var2], var1);
            }

            return var4;
         }
      }
   }

   public static String stripEnd(String var0, String var1) {
      if (var0 != null) {
         int var3 = var0.length();
         int var2 = var3;
         if (var3 == 0) {
            return var0;
         } else {
            if (var1 == null) {
               while(true) {
                  var3 = var2;
                  if (var2 == 0) {
                     break;
                  }

                  var3 = var2;
                  if (!Character.isWhitespace(var0.charAt(var2 - 1))) {
                     break;
                  }

                  --var2;
               }
            } else {
               if (var1.isEmpty()) {
                  return var0;
               }

               while(true) {
                  var3 = var2;
                  if (var2 == 0) {
                     break;
                  }

                  var3 = var2;
                  if (var1.indexOf(var0.charAt(var2 - 1)) == -1) {
                     break;
                  }

                  --var2;
               }
            }

            return var0.substring(0, var3);
         }
      } else {
         return var0;
      }
   }

   public static String stripStart(String var0, String var1) {
      if (var0 != null) {
         int var4 = var0.length();
         if (var4 == 0) {
            return var0;
         } else {
            byte var3 = 0;
            int var2 = 0;
            int var5;
            if (var1 == null) {
               while(true) {
                  var5 = var2;
                  if (var2 == var4) {
                     break;
                  }

                  var5 = var2;
                  if (!Character.isWhitespace(var0.charAt(var2))) {
                     break;
                  }

                  ++var2;
               }
            } else {
               var2 = var3;
               if (var1.isEmpty()) {
                  return var0;
               }

               while(true) {
                  var5 = var2;
                  if (var2 == var4) {
                     break;
                  }

                  var5 = var2;
                  if (var1.indexOf(var0.charAt(var2)) == -1) {
                     break;
                  }

                  ++var2;
               }
            }

            return var0.substring(var5);
         }
      } else {
         return var0;
      }
   }

   public static String stripToEmpty(String var0) {
      return var0 == null ? "" : strip(var0, (String)null);
   }

   public static String stripToNull(String var0) {
      if (var0 == null) {
         return null;
      } else {
         var0 = strip(var0, (String)null);
         return var0.isEmpty() ? null : var0;
      }
   }

   public static String substring(String var0, int var1) {
      if (var0 == null) {
         return null;
      } else {
         int var2 = var1;
         if (var1 < 0) {
            var2 = var1 + var0.length();
         }

         var1 = var2;
         if (var2 < 0) {
            var1 = 0;
         }

         return var1 > var0.length() ? "" : var0.substring(var1);
      }
   }

   public static String substring(String var0, int var1, int var2) {
      if (var0 == null) {
         return null;
      } else {
         int var3 = var2;
         if (var2 < 0) {
            var3 = var2 + var0.length();
         }

         var2 = var1;
         if (var1 < 0) {
            var2 = var1 + var0.length();
         }

         var1 = var3;
         if (var3 > var0.length()) {
            var1 = var0.length();
         }

         if (var2 > var1) {
            return "";
         } else {
            var3 = var2;
            if (var2 < 0) {
               var3 = 0;
            }

            var2 = var1;
            if (var1 < 0) {
               var2 = 0;
            }

            return var0.substring(var3, var2);
         }
      }
   }

   public static String substringAfter(String var0, String var1) {
      if (isEmpty(var0)) {
         return var0;
      } else if (var1 == null) {
         return "";
      } else {
         int var2 = var0.indexOf(var1);
         return var2 == -1 ? "" : var0.substring(var1.length() + var2);
      }
   }

   public static String substringAfterLast(String var0, String var1) {
      if (isEmpty(var0)) {
         return var0;
      } else if (isEmpty(var1)) {
         return "";
      } else {
         int var2 = var0.lastIndexOf(var1);
         if (var2 != -1) {
            return var2 == var0.length() - var1.length() ? "" : var0.substring(var1.length() + var2);
         } else {
            return "";
         }
      }
   }

   public static String substringBefore(String var0, String var1) {
      if (!isEmpty(var0)) {
         if (var1 == null) {
            return var0;
         } else if (var1.isEmpty()) {
            return "";
         } else {
            int var2 = var0.indexOf(var1);
            return var2 == -1 ? var0 : var0.substring(0, var2);
         }
      } else {
         return var0;
      }
   }

   public static String substringBeforeLast(String var0, String var1) {
      if (!isEmpty(var0)) {
         if (isEmpty(var1)) {
            return var0;
         } else {
            int var2 = var0.lastIndexOf(var1);
            return var2 == -1 ? var0 : var0.substring(0, var2);
         }
      } else {
         return var0;
      }
   }

   public static String substringBetween(String var0, String var1) {
      return substringBetween(var0, var1, var1);
   }

   public static String substringBetween(String var0, String var1, String var2) {
      if (var0 != null && var1 != null) {
         if (var2 == null) {
            return null;
         } else {
            int var3 = var0.indexOf(var1);
            if (var3 != -1) {
               int var4 = var0.indexOf(var2, var1.length() + var3);
               if (var4 != -1) {
                  return var0.substring(var1.length() + var3, var4);
               }
            }

            return null;
         }
      } else {
         return null;
      }
   }

   public static String[] substringsBetween(String var0, String var1, String var2) {
      if (var0 != null && !isEmpty(var1)) {
         if (isEmpty(var2)) {
            return null;
         } else {
            int var4 = var0.length();
            if (var4 == 0) {
               return ArrayUtils.EMPTY_STRING_ARRAY;
            } else {
               int var5 = var2.length();
               int var6 = var1.length();
               ArrayList var8 = new ArrayList();

               int var7;
               for(int var3 = 0; var3 < var4 - var5; var3 = var7 + var5) {
                  var3 = var0.indexOf(var1, var3);
                  if (var3 < 0) {
                     break;
                  }

                  var3 += var6;
                  var7 = var0.indexOf(var2, var3);
                  if (var7 < 0) {
                     break;
                  }

                  var8.add(var0.substring(var3, var7));
               }

               return var8.isEmpty() ? null : (String[])var8.toArray(new String[var8.size()]);
            }
         }
      } else {
         return null;
      }
   }

   public static String swapCase(String var0) {
      if (isEmpty(var0)) {
         return var0;
      } else {
         int var4 = var0.length();
         int[] var5 = new int[var4];
         int var2 = 0;

         for(int var3 = 0; var3 < var4; ++var2) {
            int var1 = var0.codePointAt(var3);
            if (Character.isUpperCase(var1)) {
               var1 = Character.toLowerCase(var1);
            } else if (Character.isTitleCase(var1)) {
               var1 = Character.toLowerCase(var1);
            } else if (Character.isLowerCase(var1)) {
               var1 = Character.toUpperCase(var1);
            }

            var5[var2] = var1;
            var3 += Character.charCount(var1);
         }

         return new String(var5, 0, var2);
      }
   }

   public static int[] toCodePoints(CharSequence var0) {
      if (var0 == null) {
         return null;
      } else if (var0.length() == 0) {
         return ArrayUtils.EMPTY_INT_ARRAY;
      } else {
         String var4 = var0.toString();
         int[] var3 = new int[var4.codePointCount(0, var4.length())];
         int var2 = 0;

         for(int var1 = 0; var1 < var3.length; ++var1) {
            var3[var1] = var4.codePointAt(var2);
            var2 += Character.charCount(var3[var1]);
         }

         return var3;
      }
   }

   public static String toEncodedString(byte[] var0, Charset var1) {
      if (var1 == null) {
         var1 = Charset.defaultCharset();
      }

      return new String(var0, var1);
   }

   @Deprecated
   public static String toString(byte[] var0, String var1) throws UnsupportedEncodingException {
      String var2;
      if (var1 != null) {
         var2 = new String(var0, var1);
         return var2;
      } else {
         var2 = new String(var0, Charset.defaultCharset());
         return var2;
      }
   }

   public static String trim(String var0) {
      return var0 == null ? null : var0.trim();
   }

   public static String trimToEmpty(String var0) {
      return var0 == null ? "" : var0.trim();
   }

   public static String trimToNull(String var0) {
      var0 = trim(var0);
      return isEmpty(var0) ? null : var0;
   }

   public static String truncate(String var0, int var1) {
      return truncate(var0, 0, var1);
   }

   public static String truncate(String var0, int var1, int var2) {
      if (var1 >= 0) {
         if (var2 >= 0) {
            if (var0 == null) {
               return null;
            } else if (var1 > var0.length()) {
               return "";
            } else if (var0.length() > var2) {
               if (var1 + var2 > var0.length()) {
                  var2 = var0.length();
               } else {
                  var2 += var1;
               }

               return var0.substring(var1, var2);
            } else {
               return var0.substring(var1);
            }
         } else {
            throw new IllegalArgumentException("maxWith cannot be negative");
         }
      } else {
         throw new IllegalArgumentException("offset cannot be negative");
      }
   }

   public static String uncapitalize(String var0) {
      if (var0 == null) {
         return var0;
      } else {
         int var3 = var0.length();
         if (var3 == 0) {
            return var0;
         } else {
            int var2 = var0.codePointAt(0);
            int var4 = Character.toLowerCase(var2);
            if (var2 == var4) {
               return var0;
            } else {
               int[] var5 = new int[var3];
               int var1 = 0 + 1;
               var5[0] = var4;

               for(var2 = Character.charCount(var2); var2 < var3; ++var1) {
                  var4 = var0.codePointAt(var2);
                  var5[var1] = var4;
                  var2 += Character.charCount(var4);
               }

               return new String(var5, 0, var1);
            }
         }
      }
   }

   public static String unwrap(String var0, char var1) {
      if (!isEmpty(var0)) {
         if (var1 == 0) {
            return var0;
         } else {
            if (var0.charAt(0) == var1 && var0.charAt(var0.length() - 1) == var1) {
               int var2 = var0.length() - 1;
               if (var2 != -1) {
                  return var0.substring(1, var2);
               }
            }

            return var0;
         }
      } else {
         return var0;
      }
   }

   public static String unwrap(String var0, String var1) {
      if (!isEmpty(var0)) {
         if (isEmpty(var1)) {
            return var0;
         } else {
            if (startsWith(var0, var1) && endsWith(var0, var1)) {
               int var2 = var0.indexOf(var1);
               int var3 = var0.lastIndexOf(var1);
               int var4 = var1.length();
               if (var2 != -1 && var3 != -1) {
                  return var0.substring(var2 + var4, var3);
               }
            }

            return var0;
         }
      } else {
         return var0;
      }
   }

   public static String upperCase(String var0) {
      return var0 == null ? null : var0.toUpperCase();
   }

   public static String upperCase(String var0, Locale var1) {
      return var0 == null ? null : var0.toUpperCase(var1);
   }

   public static String valueOf(char[] var0) {
      return var0 == null ? null : String.valueOf(var0);
   }

   public static String wrap(String var0, char var1) {
      if (!isEmpty(var0)) {
         if (var1 == 0) {
            return var0;
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append(var1);
            var2.append(var0);
            var2.append(var1);
            return var2.toString();
         }
      } else {
         return var0;
      }
   }

   public static String wrap(String var0, String var1) {
      if (!isEmpty(var0)) {
         return isEmpty(var1) ? var0 : var1.concat(var0).concat(var1);
      } else {
         return var0;
      }
   }

   public static String wrapIfMissing(String var0, char var1) {
      if (!isEmpty(var0)) {
         if (var1 == 0) {
            return var0;
         } else {
            StringBuilder var2 = new StringBuilder(var0.length() + 2);
            if (var0.charAt(0) != var1) {
               var2.append(var1);
            }

            var2.append(var0);
            if (var0.charAt(var0.length() - 1) != var1) {
               var2.append(var1);
            }

            return var2.toString();
         }
      } else {
         return var0;
      }
   }

   public static String wrapIfMissing(String var0, String var1) {
      if (!isEmpty(var0)) {
         if (isEmpty(var1)) {
            return var0;
         } else {
            StringBuilder var2 = new StringBuilder(var0.length() + var1.length() + var1.length());
            if (!var0.startsWith(var1)) {
               var2.append(var1);
            }

            var2.append(var0);
            if (!var0.endsWith(var1)) {
               var2.append(var1);
            }

            return var2.toString();
         }
      } else {
         return var0;
      }
   }
}
