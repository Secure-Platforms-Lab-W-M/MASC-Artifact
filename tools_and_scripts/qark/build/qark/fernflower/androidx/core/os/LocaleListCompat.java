package androidx.core.os;

import android.os.LocaleList;
import android.os.Build.VERSION;
import java.util.Locale;

public final class LocaleListCompat {
   private static final LocaleListCompat sEmptyLocaleList = create();
   private LocaleListInterface mImpl;

   private LocaleListCompat(LocaleListInterface var1) {
      this.mImpl = var1;
   }

   public static LocaleListCompat create(Locale... var0) {
      return VERSION.SDK_INT >= 24 ? wrap(new LocaleList(var0)) : new LocaleListCompat(new LocaleListCompatWrapper(var0));
   }

   static Locale forLanguageTagCompat(String var0) {
      String[] var1;
      if (var0.contains("-")) {
         var1 = var0.split("-", -1);
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

         var1 = var0.split("_", -1);
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

   public static LocaleListCompat forLanguageTags(String var0) {
      if (var0 != null && !var0.isEmpty()) {
         String[] var2 = var0.split(",", -1);
         Locale[] var3 = new Locale[var2.length];

         for(int var1 = 0; var1 < var3.length; ++var1) {
            Locale var4;
            if (VERSION.SDK_INT >= 21) {
               var4 = Locale.forLanguageTag(var2[var1]);
            } else {
               var4 = forLanguageTagCompat(var2[var1]);
            }

            var3[var1] = var4;
         }

         return create(var3);
      } else {
         return getEmptyLocaleList();
      }
   }

   public static LocaleListCompat getAdjustedDefault() {
      return VERSION.SDK_INT >= 24 ? wrap(LocaleList.getAdjustedDefault()) : create(Locale.getDefault());
   }

   public static LocaleListCompat getDefault() {
      return VERSION.SDK_INT >= 24 ? wrap(LocaleList.getDefault()) : create(Locale.getDefault());
   }

   public static LocaleListCompat getEmptyLocaleList() {
      return sEmptyLocaleList;
   }

   public static LocaleListCompat wrap(LocaleList var0) {
      return new LocaleListCompat(new LocaleListPlatformWrapper(var0));
   }

   @Deprecated
   public static LocaleListCompat wrap(Object var0) {
      return wrap((LocaleList)var0);
   }

   public boolean equals(Object var1) {
      return var1 instanceof LocaleListCompat && this.mImpl.equals(((LocaleListCompat)var1).mImpl);
   }

   public Locale get(int var1) {
      return this.mImpl.get(var1);
   }

   public Locale getFirstMatch(String[] var1) {
      return this.mImpl.getFirstMatch(var1);
   }

   public int hashCode() {
      return this.mImpl.hashCode();
   }

   public int indexOf(Locale var1) {
      return this.mImpl.indexOf(var1);
   }

   public boolean isEmpty() {
      return this.mImpl.isEmpty();
   }

   public int size() {
      return this.mImpl.size();
   }

   public String toLanguageTags() {
      return this.mImpl.toLanguageTags();
   }

   public String toString() {
      return this.mImpl.toString();
   }

   public Object unwrap() {
      return this.mImpl.getLocaleList();
   }
}
