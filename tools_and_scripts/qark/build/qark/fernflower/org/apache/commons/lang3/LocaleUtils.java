package org.apache.commons.lang3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class LocaleUtils {
   private static final ConcurrentMap cCountriesByLanguage = new ConcurrentHashMap();
   private static final ConcurrentMap cLanguagesByCountry = new ConcurrentHashMap();

   public static List availableLocaleList() {
      return LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_LIST;
   }

   public static Set availableLocaleSet() {
      return LocaleUtils.SyncAvoid.AVAILABLE_LOCALE_SET;
   }

   public static List countriesByLanguage(String var0) {
      if (var0 == null) {
         return Collections.emptyList();
      } else {
         List var2 = (List)cCountriesByLanguage.get(var0);
         List var1 = var2;
         if (var2 == null) {
            ArrayList var4 = new ArrayList();
            Iterator var5 = availableLocaleList().iterator();

            while(var5.hasNext()) {
               Locale var3 = (Locale)var5.next();
               if (var0.equals(var3.getLanguage()) && !var3.getCountry().isEmpty() && var3.getVariant().isEmpty()) {
                  var4.add(var3);
               }
            }

            var1 = Collections.unmodifiableList(var4);
            cCountriesByLanguage.putIfAbsent(var0, var1);
            var1 = (List)cCountriesByLanguage.get(var0);
         }

         return var1;
      }
   }

   public static boolean isAvailableLocale(Locale var0) {
      return availableLocaleList().contains(var0);
   }

   private static boolean isISO3166CountryCode(String var0) {
      return StringUtils.isAllUpperCase(var0) && var0.length() == 2;
   }

   private static boolean isISO639LanguageCode(String var0) {
      return StringUtils.isAllLowerCase(var0) && (var0.length() == 2 || var0.length() == 3);
   }

   private static boolean isNumericAreaCode(String var0) {
      return StringUtils.isNumeric(var0) && var0.length() == 3;
   }

   public static List languagesByCountry(String var0) {
      if (var0 == null) {
         return Collections.emptyList();
      } else {
         List var2 = (List)cLanguagesByCountry.get(var0);
         List var1 = var2;
         if (var2 == null) {
            ArrayList var4 = new ArrayList();
            Iterator var5 = availableLocaleList().iterator();

            while(var5.hasNext()) {
               Locale var3 = (Locale)var5.next();
               if (var0.equals(var3.getCountry()) && var3.getVariant().isEmpty()) {
                  var4.add(var3);
               }
            }

            var1 = Collections.unmodifiableList(var4);
            cLanguagesByCountry.putIfAbsent(var0, var1);
            var1 = (List)cLanguagesByCountry.get(var0);
         }

         return var1;
      }
   }

   public static List localeLookupList(Locale var0) {
      return localeLookupList(var0, var0);
   }

   public static List localeLookupList(Locale var0, Locale var1) {
      ArrayList var2 = new ArrayList(4);
      if (var0 != null) {
         var2.add(var0);
         if (!var0.getVariant().isEmpty()) {
            var2.add(new Locale(var0.getLanguage(), var0.getCountry()));
         }

         if (!var0.getCountry().isEmpty()) {
            var2.add(new Locale(var0.getLanguage(), ""));
         }

         if (!var2.contains(var1)) {
            var2.add(var1);
         }
      }

      return Collections.unmodifiableList(var2);
   }

   private static Locale parseLocale(String var0) {
      if (isISO639LanguageCode(var0)) {
         return new Locale(var0);
      } else {
         String[] var3 = var0.split("_", -1);
         String var1 = var3[0];
         String var2;
         if (var3.length != 2) {
            if (var3.length == 3) {
               var2 = var3[1];
               String var5 = var3[2];
               if (isISO639LanguageCode(var1) && (var2.isEmpty() || isISO3166CountryCode(var2) || isNumericAreaCode(var2)) && !var5.isEmpty()) {
                  return new Locale(var1, var2, var5);
               }
            }
         } else {
            var2 = var3[1];
            if (isISO639LanguageCode(var1) && isISO3166CountryCode(var2) || isNumericAreaCode(var2)) {
               return new Locale(var1, var2);
            }
         }

         StringBuilder var4 = new StringBuilder();
         var4.append("Invalid locale format: ");
         var4.append(var0);
         throw new IllegalArgumentException(var4.toString());
      }
   }

   public static Locale toLocale(String var0) {
      if (var0 == null) {
         return null;
      } else if (var0.isEmpty()) {
         return new Locale("", "");
      } else {
         StringBuilder var4;
         if (!var0.contains("#")) {
            int var3 = var0.length();
            if (var3 >= 2) {
               if (var0.charAt(0) == '_') {
                  if (var3 >= 3) {
                     char var1 = var0.charAt(1);
                     char var2 = var0.charAt(2);
                     if (Character.isUpperCase(var1) && Character.isUpperCase(var2)) {
                        if (var3 == 3) {
                           return new Locale("", var0.substring(1, 3));
                        } else if (var3 >= 5) {
                           if (var0.charAt(3) == '_') {
                              return new Locale("", var0.substring(1, 3), var0.substring(4));
                           } else {
                              var4 = new StringBuilder();
                              var4.append("Invalid locale format: ");
                              var4.append(var0);
                              throw new IllegalArgumentException(var4.toString());
                           }
                        } else {
                           var4 = new StringBuilder();
                           var4.append("Invalid locale format: ");
                           var4.append(var0);
                           throw new IllegalArgumentException(var4.toString());
                        }
                     } else {
                        var4 = new StringBuilder();
                        var4.append("Invalid locale format: ");
                        var4.append(var0);
                        throw new IllegalArgumentException(var4.toString());
                     }
                  } else {
                     var4 = new StringBuilder();
                     var4.append("Invalid locale format: ");
                     var4.append(var0);
                     throw new IllegalArgumentException(var4.toString());
                  }
               } else {
                  return parseLocale(var0);
               }
            } else {
               var4 = new StringBuilder();
               var4.append("Invalid locale format: ");
               var4.append(var0);
               throw new IllegalArgumentException(var4.toString());
            }
         } else {
            var4 = new StringBuilder();
            var4.append("Invalid locale format: ");
            var4.append(var0);
            throw new IllegalArgumentException(var4.toString());
         }
      }
   }

   static class SyncAvoid {
      private static final List AVAILABLE_LOCALE_LIST;
      private static final Set AVAILABLE_LOCALE_SET;

      static {
         ArrayList var0 = new ArrayList(Arrays.asList(Locale.getAvailableLocales()));
         AVAILABLE_LOCALE_LIST = Collections.unmodifiableList(var0);
         AVAILABLE_LOCALE_SET = Collections.unmodifiableSet(new HashSet(var0));
      }
   }
}
