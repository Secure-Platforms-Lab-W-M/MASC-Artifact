package org.apache.commons.lang3.text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

@Deprecated
public class WordUtils {
   public static String capitalize(String var0) {
      return capitalize(var0, (char[])null);
   }

   public static String capitalize(String var0, char... var1) {
      int var3;
      if (var1 == null) {
         var3 = -1;
      } else {
         var3 = var1.length;
      }

      if (!StringUtils.isEmpty(var0)) {
         if (var3 == 0) {
            return var0;
         } else {
            char[] var6 = var0.toCharArray();
            boolean var5 = true;

            boolean var7;
            for(int var4 = 0; var4 < var6.length; var5 = var7) {
               char var2 = var6[var4];
               if (isDelimiter(var2, var1)) {
                  var7 = true;
               } else {
                  var7 = var5;
                  if (var5) {
                     var6[var4] = Character.toTitleCase(var2);
                     var7 = false;
                  }
               }

               ++var4;
            }

            return new String(var6);
         }
      } else {
         return var0;
      }
   }

   public static String capitalizeFully(String var0) {
      return capitalizeFully(var0, (char[])null);
   }

   public static String capitalizeFully(String var0, char... var1) {
      int var2;
      if (var1 == null) {
         var2 = -1;
      } else {
         var2 = var1.length;
      }

      if (!StringUtils.isEmpty(var0)) {
         return var2 == 0 ? var0 : capitalize(var0.toLowerCase(), var1);
      } else {
         return var0;
      }
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

   public static String initials(String var0) {
      return initials(var0, (char[])null);
   }

   public static String initials(String var0, char... var1) {
      if (StringUtils.isEmpty(var0)) {
         return var0;
      } else if (var1 != null && var1.length == 0) {
         return "";
      } else {
         int var8 = var0.length();
         char[] var9 = new char[var8 / 2 + 1];
         int var5 = 0;
         boolean var6 = true;

         boolean var3;
         for(int var4 = 0; var4 < var8; var6 = var3) {
            char var2 = var0.charAt(var4);
            int var7;
            if (isDelimiter(var2, var1)) {
               var3 = true;
               var7 = var5;
            } else {
               var7 = var5;
               var3 = var6;
               if (var6) {
                  var9[var5] = var2;
                  var3 = false;
                  var7 = var5 + 1;
               }
            }

            ++var4;
            var5 = var7;
         }

         return new String(var9, 0, var5);
      }
   }

   private static boolean isDelimiter(char var0, char[] var1) {
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

   public static String swapCase(String var0) {
      if (StringUtils.isEmpty(var0)) {
         return var0;
      } else {
         char[] var4 = var0.toCharArray();
         boolean var3 = true;

         for(int var2 = 0; var2 < var4.length; ++var2) {
            char var1 = var4[var2];
            if (Character.isUpperCase(var1)) {
               var4[var2] = Character.toLowerCase(var1);
               var3 = false;
            } else if (Character.isTitleCase(var1)) {
               var4[var2] = Character.toLowerCase(var1);
               var3 = false;
            } else if (Character.isLowerCase(var1)) {
               if (var3) {
                  var4[var2] = Character.toTitleCase(var1);
                  var3 = false;
               } else {
                  var4[var2] = Character.toUpperCase(var1);
               }
            } else {
               var3 = Character.isWhitespace(var1);
            }
         }

         return new String(var4);
      }
   }

   public static String uncapitalize(String var0) {
      return uncapitalize(var0, (char[])null);
   }

   public static String uncapitalize(String var0, char... var1) {
      int var3;
      if (var1 == null) {
         var3 = -1;
      } else {
         var3 = var1.length;
      }

      if (!StringUtils.isEmpty(var0)) {
         if (var3 == 0) {
            return var0;
         } else {
            char[] var6 = var0.toCharArray();
            boolean var5 = true;

            boolean var7;
            for(int var4 = 0; var4 < var6.length; var5 = var7) {
               char var2 = var6[var4];
               if (isDelimiter(var2, var1)) {
                  var7 = true;
               } else {
                  var7 = var5;
                  if (var5) {
                     var6[var4] = Character.toLowerCase(var2);
                     var7 = false;
                  }
               }

               ++var4;
            }

            return new String(var6);
         }
      } else {
         return var0;
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
         String var8 = var2;
         if (var2 == null) {
            var8 = System.lineSeparator();
         }

         int var6 = var1;
         if (var1 < 1) {
            var6 = 1;
         }

         var2 = var4;
         if (StringUtils.isBlank(var4)) {
            var2 = " ";
         }

         Pattern var10 = Pattern.compile(var2);
         int var7 = var0.length();
         var1 = 0;
         StringBuilder var11 = new StringBuilder(var7 + 32);

         while(var1 < var7) {
            int var5 = -1;
            Matcher var9 = var10.matcher(var0.substring(var1, Math.min((int)Math.min(2147483647L, (long)(var1 + var6) + 1L), var7)));
            if (var9.find()) {
               if (var9.start() == 0) {
                  var1 += var9.end();
                  continue;
               }

               var5 = var9.start() + var1;
            }

            if (var7 - var1 <= var6) {
               break;
            }

            while(var9.find()) {
               var5 = var9.start() + var1;
            }

            if (var5 >= var1) {
               var11.append(var0, var1, var5);
               var11.append(var8);
               var1 = var5 + 1;
            } else if (var3) {
               var11.append(var0, var1, var6 + var1);
               var11.append(var8);
               var1 += var6;
            } else {
               var9 = var10.matcher(var0.substring(var1 + var6));
               if (var9.find()) {
                  var5 = var9.start() + var1 + var6;
               }

               if (var5 >= 0) {
                  var11.append(var0, var1, var5);
                  var11.append(var8);
                  var1 = var5 + 1;
               } else {
                  var11.append(var0, var1, var0.length());
                  var1 = var7;
               }
            }
         }

         var11.append(var0, var1, var0.length());
         return var11.toString();
      }
   }
}
