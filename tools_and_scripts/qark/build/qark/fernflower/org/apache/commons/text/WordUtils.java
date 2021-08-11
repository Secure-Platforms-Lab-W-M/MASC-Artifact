package org.apache.commons.text;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;

public class WordUtils {
   public static String abbreviate(String var0, int var1, int var2, String var3) {
      boolean var6 = true;
      boolean var5;
      if (var2 >= -1) {
         var5 = true;
      } else {
         var5 = false;
      }

      Validate.isTrue(var5, "upper value cannot be less than -1");
      var5 = var6;
      if (var2 < var1) {
         if (var2 == -1) {
            var5 = var6;
         } else {
            var5 = false;
         }
      }

      Validate.isTrue(var5, "upper value is less than lower value");
      if (StringUtils.isEmpty(var0)) {
         return var0;
      } else {
         int var4 = var1;
         if (var1 > var0.length()) {
            var4 = var0.length();
         }

         label37: {
            if (var2 != -1) {
               var1 = var2;
               if (var2 <= var0.length()) {
                  break label37;
               }
            }

            var1 = var0.length();
         }

         StringBuilder var7 = new StringBuilder();
         var2 = StringUtils.indexOf(var0, " ", var4);
         if (var2 == -1) {
            var7.append(var0, 0, var1);
            if (var1 != var0.length()) {
               var7.append(StringUtils.defaultString(var3));
            }
         } else if (var2 > var1) {
            var7.append(var0, 0, var1);
            var7.append(StringUtils.defaultString(var3));
         } else {
            var7.append(var0, 0, var2);
            var7.append(StringUtils.defaultString(var3));
         }

         return var7.toString();
      }
   }

   public static String capitalize(String var0) {
      return capitalize(var0, (char[])null);
   }

   public static String capitalize(String var0, char... var1) {
      if (StringUtils.isEmpty(var0)) {
         return var0;
      } else {
         Set var8 = generateDelimiterSet(var1);
         int var5 = var0.length();
         int[] var7 = new int[var5];
         int var2 = 0;
         boolean var4 = true;
         int var3 = 0;

         while(var3 < var5) {
            int var6 = var0.codePointAt(var3);
            if (var8.contains(var6)) {
               var4 = true;
               var7[var2] = var6;
               var3 += Character.charCount(var6);
               ++var2;
            } else if (var4) {
               int var9 = Character.toTitleCase(var6);
               var7[var2] = var9;
               var3 += Character.charCount(var9);
               var4 = false;
               ++var2;
            } else {
               var7[var2] = var6;
               var3 += Character.charCount(var6);
               ++var2;
            }
         }

         return new String(var7, 0, var2);
      }
   }

   public static String capitalizeFully(String var0) {
      return capitalizeFully(var0, (char[])null);
   }

   public static String capitalizeFully(String var0, char... var1) {
      return StringUtils.isEmpty(var0) ? var0 : capitalize(var0.toLowerCase(), var1);
   }

   public static boolean containsAllWords(CharSequence var0, CharSequence... var1) {
      if (!StringUtils.isEmpty(var0)) {
         if (ArrayUtils.isEmpty((Object[])var1)) {
            return false;
         } else {
            int var3 = var1.length;

            for(int var2 = 0; var2 < var3; ++var2) {
               CharSequence var4 = var1[var2];
               if (StringUtils.isBlank(var4)) {
                  return false;
               }

               StringBuilder var5 = new StringBuilder();
               var5.append(".*\\b");
               var5.append(var4);
               var5.append("\\b.*");
               if (!Pattern.compile(var5.toString()).matcher(var0).matches()) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   private static Set generateDelimiterSet(char[] var0) {
      HashSet var2 = new HashSet();
      if (var0 != null && var0.length != 0) {
         for(int var1 = 0; var1 < var0.length; ++var1) {
            var2.add(Character.codePointAt(var0, var1));
         }

         return var2;
      } else {
         if (var0 == null) {
            var2.add(Character.codePointAt(new char[]{' '}, 0));
         }

         return var2;
      }
   }

   public static String initials(String var0) {
      return initials(var0, (char[])null);
   }

   public static String initials(String var0, char... var1) {
      if (StringUtils.isEmpty(var0)) {
         return var0;
      } else if (var1 != null && var1.length == 0) {
         return "";
      } else {
         Set var9 = generateDelimiterSet(var1);
         int var7 = var0.length();
         int[] var10 = new int[var7 / 2 + 1];
         int var4 = 0;
         boolean var5 = true;

         boolean var2;
         for(int var3 = 0; var3 < var7; var5 = var2) {
            int var8 = var0.codePointAt(var3);
            int var6;
            if (var9.contains(var8) || var1 == null && Character.isWhitespace(var8)) {
               var2 = true;
               var6 = var4;
            } else {
               var6 = var4;
               var2 = var5;
               if (var5) {
                  var10[var4] = var8;
                  var2 = false;
                  var6 = var4 + 1;
               }
            }

            var3 += Character.charCount(var8);
            var4 = var6;
         }

         return new String(var10, 0, var4);
      }
   }

   @Deprecated
   public static boolean isDelimiter(char var0, char[] var1) {
      if (var1 == null) {
         return Character.isWhitespace(var0);
      } else {
         int var3 = var1.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            if (var0 == var1[var2]) {
               return true;
            }
         }

         return false;
      }
   }

   @Deprecated
   public static boolean isDelimiter(int var0, char[] var1) {
      if (var1 == null) {
         return Character.isWhitespace(var0);
      } else {
         for(int var2 = 0; var2 < var1.length; ++var2) {
            if (Character.codePointAt(var1, var2) == var0) {
               return true;
            }
         }

         return false;
      }
   }

   public static String swapCase(String var0) {
      if (StringUtils.isEmpty(var0)) {
         return var0;
      } else {
         int var4 = var0.length();
         int[] var6 = new int[var4];
         int var2 = 0;
         boolean var5 = true;

         for(int var3 = 0; var3 < var4; ++var2) {
            int var1 = var0.codePointAt(var3);
            if (!Character.isUpperCase(var1) && !Character.isTitleCase(var1)) {
               if (Character.isLowerCase(var1)) {
                  if (var5) {
                     var1 = Character.toTitleCase(var1);
                     var5 = false;
                  } else {
                     var1 = Character.toUpperCase(var1);
                  }
               } else {
                  var5 = Character.isWhitespace(var1);
               }
            } else {
               var1 = Character.toLowerCase(var1);
               var5 = false;
            }

            var6[var2] = var1;
            var3 += Character.charCount(var1);
         }

         return new String(var6, 0, var2);
      }
   }

   public static String uncapitalize(String var0) {
      return uncapitalize(var0, (char[])null);
   }

   public static String uncapitalize(String var0, char... var1) {
      if (StringUtils.isEmpty(var0)) {
         return var0;
      } else {
         Set var8 = generateDelimiterSet(var1);
         int var5 = var0.length();
         int[] var7 = new int[var5];
         int var2 = 0;
         boolean var4 = true;
         int var3 = 0;

         while(var3 < var5) {
            int var6 = var0.codePointAt(var3);
            if (var8.contains(var6)) {
               var4 = true;
               var7[var2] = var6;
               var3 += Character.charCount(var6);
               ++var2;
            } else if (var4) {
               int var9 = Character.toLowerCase(var6);
               var7[var2] = var9;
               var3 += Character.charCount(var9);
               var4 = false;
               ++var2;
            } else {
               var7[var2] = var6;
               var3 += Character.charCount(var6);
               ++var2;
            }
         }

         return new String(var7, 0, var2);
      }
   }

   public static String wrap(String var0, int var1) {
      return wrap(var0, var1, (String)null, false);
   }

   public static String wrap(String var0, int var1, String var2, boolean var3) {
      return wrap(var0, var1, var2, var3, " ");
   }

   public static String wrap(String var0, int var1, String var2, boolean var3, String var4) {
      if (var0 == null) {
         return null;
      } else {
         if (var2 == null) {
            var2 = System.lineSeparator();
         }

         int var9;
         if (var1 < 1) {
            var9 = 1;
         } else {
            var9 = var1;
         }

         if (StringUtils.isBlank(var4)) {
            var4 = " ";
         }

         Pattern var13 = Pattern.compile(var4);
         int var10 = var0.length();
         int var5 = 0;
         StringBuilder var11 = new StringBuilder(var10 + 32);
         int var6 = -1;

         int var7;
         while(true) {
            var1 = var5;
            var7 = var6;
            if (var5 >= var10) {
               break;
            }

            int var8 = -1;
            Matcher var12 = var13.matcher(var0.substring(var5, Math.min((int)Math.min(2147483647L, (long)(var5 + var9) + 1L), var10)));
            var1 = var5;
            var7 = var6;
            if (var12.find()) {
               var1 = var5;
               if (var12.start() == 0) {
                  var6 = var12.end() - var12.start();
                  if (var6 != 0) {
                     var5 += var12.end();
                     continue;
                  }

                  var1 = var5 + 1;
               }

               var8 = var12.start() + var1;
               var7 = var6;
            }

            if (var10 - var1 <= var9) {
               break;
            }

            while(var12.find()) {
               var8 = var12.start() + var1;
            }

            if (var8 >= var1) {
               var11.append(var0, var1, var8);
               var11.append(var2);
               var5 = var8 + 1;
               var6 = var7;
            } else if (var3) {
               var5 = var1;
               if (var7 == 0) {
                  var5 = var1 - 1;
               }

               var11.append(var0, var5, var9 + var5);
               var11.append(var2);
               var5 += var9;
               var6 = -1;
            } else {
               var12 = var13.matcher(var0.substring(var1 + var9));
               var6 = var7;
               if (var12.find()) {
                  var6 = var12.end() - var12.start();
                  var8 = var12.start() + var1 + var9;
               }

               if (var8 >= 0) {
                  var5 = var1;
                  if (var6 == 0) {
                     var5 = var1;
                     if (var1 != 0) {
                        var5 = var1 - 1;
                     }
                  }

                  var11.append(var0, var5, var8);
                  var11.append(var2);
                  var5 = var8 + 1;
               } else {
                  var5 = var1;
                  if (var6 == 0) {
                     var5 = var1;
                     if (var1 != 0) {
                        var5 = var1 - 1;
                     }
                  }

                  var11.append(var0, var5, var0.length());
                  var5 = var10;
                  var6 = -1;
               }
            }
         }

         var5 = var1;
         if (var7 == 0) {
            var5 = var1;
            if (var1 < var10) {
               var5 = var1 - 1;
            }
         }

         var11.append(var0, var5, var0.length());
         return var11.toString();
      }
   }
}
