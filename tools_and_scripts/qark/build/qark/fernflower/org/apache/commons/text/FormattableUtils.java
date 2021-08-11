package org.apache.commons.text;

import java.util.Formattable;
import java.util.Formatter;

public class FormattableUtils {
   private static final String SIMPLEST_FORMAT = "%s";

   public static Formatter append(CharSequence var0, Formatter var1, int var2, int var3, int var4) {
      return append(var0, var1, var2, var3, var4, ' ', (CharSequence)null);
   }

   public static Formatter append(CharSequence var0, Formatter var1, int var2, int var3, int var4, char var5) {
      return append(var0, var1, var2, var3, var4, var5, (CharSequence)null);
   }

   public static Formatter append(CharSequence var0, Formatter var1, int var2, int var3, int var4, char var5, CharSequence var6) {
      boolean var7 = true;
      if (var6 != null && var4 >= 0 && ((CharSequence)var6).length() > var4) {
         throw new IllegalArgumentException(String.format("Specified ellipsis '%s' exceeds precision of %s", var6, var4));
      } else {
         StringBuilder var8 = new StringBuilder(var0);
         if (var4 >= 0 && var4 < var0.length()) {
            if (var6 == null) {
               var6 = "";
            }

            var8.replace(var4 - ((CharSequence)var6).length(), var0.length(), ((CharSequence)var6).toString());
         }

         boolean var9;
         if ((var2 & 1) == 1) {
            var9 = var7;
         } else {
            var9 = false;
         }

         for(var2 = var8.length(); var2 < var3; ++var2) {
            int var10;
            if (var9) {
               var10 = var2;
            } else {
               var10 = 0;
            }

            var8.insert(var10, var5);
         }

         var1.format(var8.toString());
         return var1;
      }
   }

   public static Formatter append(CharSequence var0, Formatter var1, int var2, int var3, int var4, CharSequence var5) {
      return append(var0, var1, var2, var3, var4, ' ', var5);
   }

   public static String toString(Formattable var0) {
      return String.format("%s", var0);
   }
}
