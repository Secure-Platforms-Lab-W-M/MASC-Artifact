package androidx.core.os;

import android.os.Build.VERSION;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

final class LocaleListCompatWrapper implements LocaleListInterface {
   private static final Locale EN_LATN = LocaleListCompat.forLanguageTagCompat("en-Latn");
   private static final Locale LOCALE_AR_XB = new Locale("ar", "XB");
   private static final Locale LOCALE_EN_XA = new Locale("en", "XA");
   private static final Locale[] sEmptyList = new Locale[0];
   private final Locale[] mList;
   private final String mStringRepresentation;

   LocaleListCompatWrapper(Locale... var1) {
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
            toLanguageTag(var5, var6);
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
            var5 = this.findFirstMatchIndex(LocaleListCompat.forLanguageTagCompat((String)var7.next()));
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

   private static String getLikelyScript(Locale var0) {
      if (VERSION.SDK_INT >= 21) {
         String var1 = var0.getScript();
         return !var1.isEmpty() ? var1 : "";
      } else {
         return "";
      }
   }

   private static boolean isPseudoLocale(Locale var0) {
      return LOCALE_EN_XA.equals(var0) || LOCALE_AR_XB.equals(var0);
   }

   private static int matchScore(Locale var0, Locale var1) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:659)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   static void toLanguageTag(StringBuilder var0, Locale var1) {
      var0.append(var1.getLanguage());
      String var2 = var1.getCountry();
      if (var2 != null && !var2.isEmpty()) {
         var0.append('-');
         var0.append(var1.getCountry());
      }

   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof LocaleListCompatWrapper)) {
         return false;
      } else {
         Locale[] var4 = ((LocaleListCompatWrapper)var1).mList;
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

   public Locale get(int var1) {
      if (var1 >= 0) {
         Locale[] var2 = this.mList;
         if (var1 < var2.length) {
            return var2[var1];
         }
      }

      return null;
   }

   public Locale getFirstMatch(String[] var1) {
      return this.computeFirstMatch(Arrays.asList(var1), false);
   }

   public Object getLocaleList() {
      return null;
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

   public int indexOf(Locale var1) {
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

   public boolean isEmpty() {
      return this.mList.length == 0;
   }

   public int size() {
      return this.mList.length;
   }

   public String toLanguageTags() {
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
