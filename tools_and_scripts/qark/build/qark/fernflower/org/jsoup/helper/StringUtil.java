package org.jsoup.helper;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public final class StringUtil {
   private static final String[] padding = new String[]{"", " ", "  ", "   ", "    ", "     ", "      ", "       ", "        ", "         ", "          "};

   public static void appendNormalisedWhitespace(StringBuilder var0, String var1, boolean var2) {
      boolean var6 = false;
      boolean var5 = false;
      int var8 = var1.length();

      boolean var7;
      for(int var4 = 0; var4 < var8; var5 = var7) {
         int var9 = var1.codePointAt(var4);
         boolean var3;
         if (isWhitespace(var9)) {
            label20: {
               if (var2) {
                  var3 = var6;
                  var7 = var5;
                  if (!var5) {
                     break label20;
                  }
               }

               if (var6) {
                  var7 = var5;
                  var3 = var6;
               } else {
                  var0.append(' ');
                  var3 = true;
                  var7 = var5;
               }
            }
         } else {
            var0.appendCodePoint(var9);
            var3 = false;
            var7 = true;
         }

         var4 += Character.charCount(var9);
         var6 = var3;
      }

   }

   // $FF: renamed from: in (java.lang.String, java.lang.String[]) boolean
   public static boolean method_16(String var0, String... var1) {
      boolean var5 = false;
      int var3 = var1.length;
      int var2 = 0;

      boolean var4;
      while(true) {
         var4 = var5;
         if (var2 >= var3) {
            break;
         }

         if (var1[var2].equals(var0)) {
            var4 = true;
            break;
         }

         ++var2;
      }

      return var4;
   }

   public static boolean inSorted(String var0, String[] var1) {
      return Arrays.binarySearch(var1, var0) >= 0;
   }

   public static boolean isBlank(String var0) {
      if (var0 != null && var0.length() != 0) {
         int var2 = var0.length();

         for(int var1 = 0; var1 < var2; ++var1) {
            if (!isWhitespace(var0.codePointAt(var1))) {
               return false;
            }
         }
      }

      return true;
   }

   public static boolean isNumeric(String var0) {
      if (var0 != null && var0.length() != 0) {
         int var2 = var0.length();
         int var1 = 0;

         while(true) {
            if (var1 >= var2) {
               return true;
            }

            if (!Character.isDigit(var0.codePointAt(var1))) {
               break;
            }

            ++var1;
         }
      }

      return false;
   }

   public static boolean isWhitespace(int var0) {
      return var0 == 32 || var0 == 9 || var0 == 10 || var0 == 12 || var0 == 13;
   }

   public static String join(Collection var0, String var1) {
      return join(var0.iterator(), var1);
   }

   public static String join(Iterator var0, String var1) {
      String var2;
      if (!var0.hasNext()) {
         var2 = "";
      } else {
         String var3 = var0.next().toString();
         var2 = var3;
         if (var0.hasNext()) {
            StringBuilder var4 = (new StringBuilder(64)).append(var3);

            while(var0.hasNext()) {
               var4.append(var1);
               var4.append(var0.next());
            }

            return var4.toString();
         }
      }

      return var2;
   }

   public static String normaliseWhitespace(String var0) {
      StringBuilder var1 = new StringBuilder(var0.length());
      appendNormalisedWhitespace(var1, var0, false);
      return var1.toString();
   }

   public static String padding(int var0) {
      if (var0 < 0) {
         throw new IllegalArgumentException("width must be > 0");
      } else if (var0 < padding.length) {
         return padding[var0];
      } else {
         char[] var2 = new char[var0];

         for(int var1 = 0; var1 < var0; ++var1) {
            var2[var1] = ' ';
         }

         return String.valueOf(var2);
      }
   }

   public static String resolve(String var0, String var1) {
      boolean var10001;
      URL var5;
      try {
         var5 = new URL(var0);
      } catch (MalformedURLException var4) {
         try {
            var0 = (new URL(var1)).toExternalForm();
            return var0;
         } catch (MalformedURLException var2) {
            var10001 = false;
            return "";
         }
      }

      try {
         return resolve(var5, var1).toExternalForm();
      } catch (MalformedURLException var3) {
         var10001 = false;
         return "";
      }
   }

   public static URL resolve(URL var0, String var1) throws MalformedURLException {
      String var2 = var1;
      if (var1.startsWith("?")) {
         var2 = var0.getPath() + var1;
      }

      URL var3 = var0;
      if (var2.indexOf(46) == 0) {
         var3 = var0;
         if (var0.getFile().indexOf(47) != 0) {
            var3 = new URL(var0.getProtocol(), var0.getHost(), var0.getPort(), "/" + var0.getFile());
         }
      }

      return new URL(var3, var2);
   }
}
