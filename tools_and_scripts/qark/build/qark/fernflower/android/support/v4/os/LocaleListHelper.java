package android.support.v4.os;

import android.os.Build.VERSION;
import android.support.annotation.GuardedBy;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.annotation.Size;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

@RequiresApi(14)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
final class LocaleListHelper {
   private static final Locale EN_LATN = LocaleHelper.forLanguageTag("en-Latn");
   private static final Locale LOCALE_AR_XB = new Locale("ar", "XB");
   private static final Locale LOCALE_EN_XA = new Locale("en", "XA");
   private static final int NUM_PSEUDO_LOCALES = 2;
   private static final String STRING_AR_XB = "ar-XB";
   private static final String STRING_EN_XA = "en-XA";
   @GuardedBy("sLock")
   private static LocaleListHelper sDefaultAdjustedLocaleList = null;
   @GuardedBy("sLock")
   private static LocaleListHelper sDefaultLocaleList = null;
   private static final Locale[] sEmptyList = new Locale[0];
   private static final LocaleListHelper sEmptyLocaleList = new LocaleListHelper(new Locale[0]);
   @GuardedBy("sLock")
   private static Locale sLastDefaultLocale = null;
   @GuardedBy("sLock")
   private static LocaleListHelper sLastExplicitlySetLocaleList = null;
   private static final Object sLock = new Object();
   private final Locale[] mList;
   @NonNull
   private final String mStringRepresentation;

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   LocaleListHelper(@NonNull Locale var1, LocaleListHelper var2) {
      if (var1 == null) {
         NullPointerException var9 = new NullPointerException("topLocale is null");
         throw var9;
      } else {
         int var4;
         if (var2 == null) {
            var4 = 0;
         } else {
            var4 = var2.mList.length;
         }

         byte var6 = -1;
         int var3 = 0;

         int var5;
         while(true) {
            var5 = var6;
            if (var3 >= var4) {
               break;
            }

            if (var1.equals(var2.mList[var3])) {
               var5 = var3;
               break;
            }

            ++var3;
         }

         byte var10;
         if (var5 == -1) {
            var10 = 1;
         } else {
            var10 = 0;
         }

         int var11 = var10 + var4;
         Locale[] var7 = new Locale[var11];
         var7[0] = (Locale)var1.clone();
         if (var5 == -1) {
            for(var3 = 0; var3 < var4; ++var3) {
               var7[var3 + 1] = (Locale)var2.mList[var3].clone();
            }
         } else {
            for(var3 = 0; var3 < var5; ++var3) {
               var7[var3 + 1] = (Locale)var2.mList[var3].clone();
            }

            for(var3 = var5 + 1; var3 < var4; ++var3) {
               var7[var3] = (Locale)var2.mList[var3].clone();
            }
         }

         StringBuilder var8 = new StringBuilder();

         for(var3 = 0; var3 < var11; ++var3) {
            var8.append(LocaleHelper.toLanguageTag(var7[var3]));
            if (var3 < var11 - 1) {
               var8.append(',');
            }
         }

         this.mList = var7;
         this.mStringRepresentation = var8.toString();
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   LocaleListHelper(@NonNull Locale... var1) {
      if (var1.length == 0) {
         this.mList = sEmptyList;
         this.mStringRepresentation = "";
      } else {
         Locale[] var3 = new Locale[var1.length];
         HashSet var4 = new HashSet();
         StringBuilder var5 = new StringBuilder();

         for(int var2 = 0; var2 < var1.length; ++var2) {
            Locale var6 = var1[var2];
            StringBuilder var7;
            if (var6 == null) {
               var7 = new StringBuilder();
               var7.append("list[");
               var7.append(var2);
               var7.append("] is null");
               throw new NullPointerException(var7.toString());
            }

            if (var4.contains(var6)) {
               var7 = new StringBuilder();
               var7.append("list[");
               var7.append(var2);
               var7.append("] is a repetition");
               throw new IllegalArgumentException(var7.toString());
            }

            var6 = (Locale)var6.clone();
            var3[var2] = var6;
            var5.append(LocaleHelper.toLanguageTag(var6));
            if (var2 < var1.length - 1) {
               var5.append(',');
            }

            var4.add(var6);
         }

         this.mList = var3;
         this.mStringRepresentation = var5.toString();
      }
   }

   private Locale computeFirstMatch(Collection var1, boolean var2) {
      int var3 = this.computeFirstMatchIndex(var1, var2);
      return var3 == -1 ? null : this.mList[var3];
   }

   private int computeFirstMatchIndex(Collection var1, boolean var2) {
      Locale[] var6 = this.mList;
      if (var6.length == 1) {
         return 0;
      } else if (var6.length == 0) {
         return -1;
      } else {
         int var4 = Integer.MAX_VALUE;
         int var3 = var4;
         int var5;
         if (var2) {
            var5 = this.findFirstMatchIndex(EN_LATN);
            if (var5 == 0) {
               return 0;
            }

            var3 = var4;
            if (var5 < Integer.MAX_VALUE) {
               var3 = var5;
            }
         }

         for(Iterator var7 = var1.iterator(); var7.hasNext(); var3 = var4) {
            var5 = this.findFirstMatchIndex(LocaleHelper.forLanguageTag((String)var7.next()));
            if (var5 == 0) {
               return 0;
            }

            var4 = var3;
            if (var5 < var3) {
               var4 = var5;
            }
         }

         if (var3 == Integer.MAX_VALUE) {
            return 0;
         } else {
            return var3;
         }
      }
   }

   private int findFirstMatchIndex(Locale var1) {
      int var2 = 0;

      while(true) {
         Locale[] var3 = this.mList;
         if (var2 >= var3.length) {
            return Integer.MAX_VALUE;
         }

         if (matchScore(var1, var3[var2]) > 0) {
            return var2;
         }

         ++var2;
      }
   }

   @NonNull
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   static LocaleListHelper forLanguageTags(@Nullable String var0) {
      if (var0 != null && !var0.isEmpty()) {
         String[] var3 = var0.split(",");
         Locale[] var2 = new Locale[var3.length];

         for(int var1 = 0; var1 < var2.length; ++var1) {
            var2[var1] = LocaleHelper.forLanguageTag(var3[var1]);
         }

         return new LocaleListHelper(var2);
      } else {
         return getEmptyLocaleList();
      }
   }

   @NonNull
   @Size(
      min = 1L
   )
   static LocaleListHelper getAdjustedDefault() {
      // $FF: Couldn't be decompiled
   }

   @NonNull
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   @Size(
      min = 1L
   )
   static LocaleListHelper getDefault() {
      Locale var1 = Locale.getDefault();
      Object var0 = sLock;
      synchronized(var0){}

      Throwable var10000;
      boolean var10001;
      label208: {
         LocaleListHelper var22;
         label207: {
            try {
               if (var1.equals(sLastDefaultLocale)) {
                  break label207;
               }

               sLastDefaultLocale = var1;
               if (sDefaultLocaleList != null && var1.equals(sDefaultLocaleList.get(0))) {
                  var22 = sDefaultLocaleList;
                  return var22;
               }
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label208;
            }

            try {
               sDefaultLocaleList = new LocaleListHelper(var1, sLastExplicitlySetLocaleList);
               sDefaultAdjustedLocaleList = sDefaultLocaleList;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label208;
            }
         }

         label197:
         try {
            var22 = sDefaultLocaleList;
            return var22;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            break label197;
         }
      }

      while(true) {
         Throwable var23 = var10000;

         try {
            throw var23;
         } catch (Throwable var18) {
            var10000 = var18;
            var10001 = false;
            continue;
         }
      }
   }

   @NonNull
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   static LocaleListHelper getEmptyLocaleList() {
      return sEmptyLocaleList;
   }

   private static String getLikelyScript(Locale var0) {
      if (VERSION.SDK_INT >= 21) {
         String var1 = var0.getScript();
         return !var1.isEmpty() ? var1 : "";
      } else {
         return "";
      }
   }

   private static boolean isPseudoLocale(String var0) {
      return "en-XA".equals(var0) || "ar-XB".equals(var0);
   }

   private static boolean isPseudoLocale(Locale var0) {
      return LOCALE_EN_XA.equals(var0) || LOCALE_AR_XB.equals(var0);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   static boolean isPseudoLocalesOnly(@Nullable String[] var0) {
      if (var0 == null) {
         return true;
      } else if (var0.length > 3) {
         return false;
      } else {
         int var2 = var0.length;

         for(int var1 = 0; var1 < var2; ++var1) {
            String var3 = var0[var1];
            if (!var3.isEmpty() && !isPseudoLocale(var3)) {
               return false;
            }
         }

         return true;
      }
   }

   @IntRange(
      from = 0L,
      to = 1L
   )
   private static int matchScore(Locale var0, Locale var1) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:659)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   static void setDefault(@NonNull @Size(min = 1L) LocaleListHelper var0) {
      setDefault(var0, 0);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   static void setDefault(@NonNull @Size(min = 1L) LocaleListHelper var0, int var1) {
      if (var0 == null) {
         throw new NullPointerException("locales is null");
      } else if (!var0.isEmpty()) {
         Object var2 = sLock;
         synchronized(var2){}

         Throwable var10000;
         boolean var10001;
         label295: {
            try {
               sLastDefaultLocale = var0.get(var1);
               Locale.setDefault(sLastDefaultLocale);
               sLastExplicitlySetLocaleList = var0;
               sDefaultLocaleList = var0;
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label295;
            }

            if (var1 == 0) {
               try {
                  sDefaultAdjustedLocaleList = sDefaultLocaleList;
               } catch (Throwable var31) {
                  var10000 = var31;
                  var10001 = false;
                  break label295;
               }
            } else {
               try {
                  sDefaultAdjustedLocaleList = new LocaleListHelper(sLastDefaultLocale, sDefaultLocaleList);
               } catch (Throwable var30) {
                  var10000 = var30;
                  var10001 = false;
                  break label295;
               }
            }

            label278:
            try {
               return;
            } catch (Throwable var29) {
               var10000 = var29;
               var10001 = false;
               break label278;
            }
         }

         while(true) {
            Throwable var33 = var10000;

            try {
               throw var33;
            } catch (Throwable var28) {
               var10000 = var28;
               var10001 = false;
               continue;
            }
         }
      } else {
         throw new IllegalArgumentException("locales is empty");
      }
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof LocaleListHelper)) {
         return false;
      } else {
         Locale[] var4 = ((LocaleListHelper)var1).mList;
         if (this.mList.length != var4.length) {
            return false;
         } else {
            int var2 = 0;

            while(true) {
               Locale[] var3 = this.mList;
               if (var2 >= var3.length) {
                  return true;
               }

               if (!var3[var2].equals(var4[var2])) {
                  return false;
               }

               ++var2;
            }
         }
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   Locale get(int var1) {
      if (var1 >= 0) {
         Locale[] var2 = this.mList;
         if (var1 < var2.length) {
            return var2[var1];
         }
      }

      return null;
   }

   @Nullable
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   Locale getFirstMatch(String[] var1) {
      return this.computeFirstMatch(Arrays.asList(var1), false);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   int getFirstMatchIndex(String[] var1) {
      return this.computeFirstMatchIndex(Arrays.asList(var1), false);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   int getFirstMatchIndexWithEnglishSupported(Collection var1) {
      return this.computeFirstMatchIndex(var1, true);
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   int getFirstMatchIndexWithEnglishSupported(String[] var1) {
      return this.getFirstMatchIndexWithEnglishSupported((Collection)Arrays.asList(var1));
   }

   @Nullable
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   Locale getFirstMatchWithEnglishSupported(String[] var1) {
      return this.computeFirstMatch(Arrays.asList(var1), true);
   }

   public int hashCode() {
      int var2 = 1;
      int var1 = 0;

      while(true) {
         Locale[] var3 = this.mList;
         if (var1 >= var3.length) {
            return var2;
         }

         var2 = var2 * 31 + var3[var1].hashCode();
         ++var1;
      }
   }

   @IntRange(
      from = -1L
   )
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   int indexOf(Locale var1) {
      int var2 = 0;

      while(true) {
         Locale[] var3 = this.mList;
         if (var2 >= var3.length) {
            return -1;
         }

         if (var3[var2].equals(var1)) {
            return var2;
         }

         ++var2;
      }
   }

   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   boolean isEmpty() {
      return this.mList.length == 0;
   }

   @IntRange(
      from = 0L
   )
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   int size() {
      return this.mList.length;
   }

   @NonNull
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   String toLanguageTags() {
      return this.mStringRepresentation;
   }

   public String toString() {
      StringBuilder var2 = new StringBuilder();
      var2.append("[");
      int var1 = 0;

      while(true) {
         Locale[] var3 = this.mList;
         if (var1 >= var3.length) {
            var2.append("]");
            return var2.toString();
         }

         var2.append(var3[var1]);
         if (var1 < this.mList.length - 1) {
            var2.append(',');
         }

         ++var1;
      }
   }
}
