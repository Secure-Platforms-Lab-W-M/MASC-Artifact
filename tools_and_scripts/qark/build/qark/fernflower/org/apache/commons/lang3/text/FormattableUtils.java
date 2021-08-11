package org.apache.commons.lang3.text;

import java.util.Formattable;
import java.util.Formatter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.Validate;

@Deprecated
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
      boolean var8;
      if (var6 != null && var4 >= 0 && var6.length() > var4) {
         var8 = false;
      } else {
         var8 = true;
      }

      Validate.isTrue(var8, "Specified ellipsis '%1$s' exceeds precision of %2$s", var6, var4);
      StringBuilder var9 = new StringBuilder(var0);
      if (var4 >= 0 && var4 < var0.length()) {
         var6 = (CharSequence)ObjectUtils.defaultIfNull(var6, "");
         var9.replace(var4 - var6.length(), var0.length(), var6.toString());
      }

      boolean var10;
      if ((var2 & 1) == 1) {
         var10 = var7;
      } else {
         var10 = false;
      }

      for(var2 = var9.length(); var2 < var3; ++var2) {
         int var11;
         if (var10) {
            var11 = var2;
         } else {
            var11 = 0;
         }

         var9.insert(var11, var5);
      }

      var1.format(var9.toString());
      return var1;
   }

   public static Formatter append(CharSequence var0, Formatter var1, int var2, int var3, int var4, CharSequence var5) {
      return append(var0, var1, var2, var3, var4, ' ', var5);
   }

   public static String toString(Formattable var0) {
      return String.format("%s", var0);
   }
}
