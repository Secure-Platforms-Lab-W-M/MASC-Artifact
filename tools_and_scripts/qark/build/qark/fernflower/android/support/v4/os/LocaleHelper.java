package android.support.v4.os;

import android.support.annotation.RestrictTo;
import java.util.Locale;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
final class LocaleHelper {
   static Locale forLanguageTag(String var0) {
      String[] var1;
      if (var0.contains("-")) {
         var1 = var0.split("-");
         if (var1.length > 2) {
            return new Locale(var1[0], var1[1], var1[2]);
         }

         if (var1.length > 1) {
            return new Locale(var1[0], var1[1]);
         }

         if (var1.length == 1) {
            return new Locale(var1[0]);
         }
      } else {
         if (!var0.contains("_")) {
            return new Locale(var0);
         }

         var1 = var0.split("_");
         if (var1.length > 2) {
            return new Locale(var1[0], var1[1], var1[2]);
         }

         if (var1.length > 1) {
            return new Locale(var1[0], var1[1]);
         }

         if (var1.length == 1) {
            return new Locale(var1[0]);
         }
      }

      StringBuilder var2 = new StringBuilder();
      var2.append("Can not parse language tag: [");
      var2.append(var0);
      var2.append("]");
      throw new IllegalArgumentException(var2.toString());
   }

   static String toLanguageTag(Locale var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append(var0.getLanguage());
      String var2 = var0.getCountry();
      if (var2 != null && !var2.isEmpty()) {
         var1.append("-");
         var1.append(var0.getCountry());
      }

      return var1.toString();
   }
}
