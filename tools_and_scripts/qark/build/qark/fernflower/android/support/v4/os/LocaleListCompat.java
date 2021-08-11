package android.support.v4.os;

import android.os.LocaleList;
import android.os.Build.VERSION;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.Size;
import java.util.Locale;

public final class LocaleListCompat {
   static final LocaleListInterface IMPL;
   private static final LocaleListCompat sEmptyLocaleList = new LocaleListCompat();

   static {
      if (VERSION.SDK_INT >= 24) {
         IMPL = new LocaleListCompat.LocaleListCompatApi24Impl();
      } else {
         IMPL = new LocaleListCompat.LocaleListCompatBaseImpl();
      }
   }

   private LocaleListCompat() {
   }

   public static LocaleListCompat create(@NonNull Locale... var0) {
      LocaleListCompat var1 = new LocaleListCompat();
      var1.setLocaleListArray(var0);
      return var1;
   }

   @NonNull
   public static LocaleListCompat forLanguageTags(@Nullable String var0) {
      if (var0 != null && !var0.isEmpty()) {
         String[] var3 = var0.split(",");
         Locale[] var2 = new Locale[var3.length];

         for(int var1 = 0; var1 < var2.length; ++var1) {
            Locale var4;
            if (VERSION.SDK_INT >= 21) {
               var4 = Locale.forLanguageTag(var3[var1]);
            } else {
               var4 = LocaleHelper.forLanguageTag(var3[var1]);
            }

            var2[var1] = var4;
         }

         LocaleListCompat var5 = new LocaleListCompat();
         var5.setLocaleListArray(var2);
         return var5;
      } else {
         return getEmptyLocaleList();
      }
   }

   @NonNull
   @Size(
      min = 1L
   )
   public static LocaleListCompat getAdjustedDefault() {
      return VERSION.SDK_INT >= 24 ? wrap(LocaleList.getAdjustedDefault()) : create(Locale.getDefault());
   }

   @NonNull
   @Size(
      min = 1L
   )
   public static LocaleListCompat getDefault() {
      return VERSION.SDK_INT >= 24 ? wrap(LocaleList.getDefault()) : create(Locale.getDefault());
   }

   @NonNull
   public static LocaleListCompat getEmptyLocaleList() {
      return sEmptyLocaleList;
   }

   @RequiresApi(24)
   private void setLocaleList(LocaleList var1) {
      int var3 = var1.size();
      if (var3 > 0) {
         Locale[] var4 = new Locale[var3];

         for(int var2 = 0; var2 < var3; ++var2) {
            var4[var2] = var1.get(var2);
         }

         IMPL.setLocaleList(var4);
      }

   }

   private void setLocaleListArray(Locale... var1) {
      IMPL.setLocaleList(var1);
   }

   @RequiresApi(24)
   public static LocaleListCompat wrap(Object var0) {
      LocaleListCompat var1 = new LocaleListCompat();
      if (var0 instanceof LocaleList) {
         var1.setLocaleList((LocaleList)var0);
      }

      return var1;
   }

   public boolean equals(Object var1) {
      return IMPL.equals(var1);
   }

   public Locale get(int var1) {
      return IMPL.get(var1);
   }

   public Locale getFirstMatch(String[] var1) {
      return IMPL.getFirstMatch(var1);
   }

   public int hashCode() {
      return IMPL.hashCode();
   }

   @IntRange(
      from = -1L
   )
   public int indexOf(Locale var1) {
      return IMPL.indexOf(var1);
   }

   public boolean isEmpty() {
      return IMPL.isEmpty();
   }

   @IntRange(
      from = 0L
   )
   public int size() {
      return IMPL.size();
   }

   @NonNull
   public String toLanguageTags() {
      return IMPL.toLanguageTags();
   }

   public String toString() {
      return IMPL.toString();
   }

   @Nullable
   public Object unwrap() {
      return IMPL.getLocaleList();
   }

   @RequiresApi(24)
   static class LocaleListCompatApi24Impl implements LocaleListInterface {
      private LocaleList mLocaleList = new LocaleList(new Locale[0]);

      public boolean equals(Object var1) {
         return this.mLocaleList.equals(((LocaleListCompat)var1).unwrap());
      }

      public Locale get(int var1) {
         return this.mLocaleList.get(var1);
      }

      @Nullable
      public Locale getFirstMatch(String[] var1) {
         LocaleList var2 = this.mLocaleList;
         return var2 != null ? var2.getFirstMatch(var1) : null;
      }

      public Object getLocaleList() {
         return this.mLocaleList;
      }

      public int hashCode() {
         return this.mLocaleList.hashCode();
      }

      @IntRange(
         from = -1L
      )
      public int indexOf(Locale var1) {
         return this.mLocaleList.indexOf(var1);
      }

      public boolean isEmpty() {
         return this.mLocaleList.isEmpty();
      }

      public void setLocaleList(@NonNull Locale... var1) {
         this.mLocaleList = new LocaleList(var1);
      }

      @IntRange(
         from = 0L
      )
      public int size() {
         return this.mLocaleList.size();
      }

      public String toLanguageTags() {
         return this.mLocaleList.toLanguageTags();
      }

      public String toString() {
         return this.mLocaleList.toString();
      }
   }

   static class LocaleListCompatBaseImpl implements LocaleListInterface {
      private LocaleListHelper mLocaleList = new LocaleListHelper(new Locale[0]);

      public boolean equals(Object var1) {
         return this.mLocaleList.equals(((LocaleListCompat)var1).unwrap());
      }

      public Locale get(int var1) {
         return this.mLocaleList.get(var1);
      }

      @Nullable
      public Locale getFirstMatch(String[] var1) {
         LocaleListHelper var2 = this.mLocaleList;
         return var2 != null ? var2.getFirstMatch(var1) : null;
      }

      public Object getLocaleList() {
         return this.mLocaleList;
      }

      public int hashCode() {
         return this.mLocaleList.hashCode();
      }

      @IntRange(
         from = -1L
      )
      public int indexOf(Locale var1) {
         return this.mLocaleList.indexOf(var1);
      }

      public boolean isEmpty() {
         return this.mLocaleList.isEmpty();
      }

      public void setLocaleList(@NonNull Locale... var1) {
         this.mLocaleList = new LocaleListHelper(var1);
      }

      @IntRange(
         from = 0L
      )
      public int size() {
         return this.mLocaleList.size();
      }

      public String toLanguageTags() {
         return this.mLocaleList.toLanguageTags();
      }

      public String toString() {
         return this.mLocaleList.toString();
      }
   }
}
