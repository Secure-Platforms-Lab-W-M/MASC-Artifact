package android.support.v4.graphics;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.FontVariationAxis;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v4.provider.FontsContractCompat;
import android.util.Log;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

@RequiresApi(26)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class TypefaceCompatApi26Impl extends TypefaceCompatApi21Impl {
   private static final String ABORT_CREATION_METHOD = "abortCreation";
   private static final String ADD_FONT_FROM_ASSET_MANAGER_METHOD = "addFontFromAssetManager";
   private static final String ADD_FONT_FROM_BUFFER_METHOD = "addFontFromBuffer";
   private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
   private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
   private static final String FREEZE_METHOD = "freeze";
   private static final int RESOLVE_BY_FONT_TABLE = -1;
   private static final String TAG = "TypefaceCompatApi26Impl";
   private static final Method sAbortCreation;
   private static final Method sAddFontFromAssetManager;
   private static final Method sAddFontFromBuffer;
   private static final Method sCreateFromFamiliesWithDefault;
   private static final Class sFontFamily;
   private static final Constructor sFontFamilyCtor;
   private static final Method sFreeze;

   static {
      Class var1;
      Constructor var2;
      Method var3;
      Method var4;
      Method var5;
      Method var6;
      Method var24;
      label71: {
         Object var0;
         label73: {
            try {
               var1 = Class.forName("android.graphics.FontFamily");
            } catch (ClassNotFoundException var21) {
               var0 = var21;
               break label73;
            } catch (NoSuchMethodException var22) {
               var0 = var22;
               break label73;
            }

            try {
               var2 = var1.getConstructor();
            } catch (ClassNotFoundException var19) {
               var0 = var19;
               break label73;
            } catch (NoSuchMethodException var20) {
               var0 = var20;
               break label73;
            }

            try {
               var3 = var1.getMethod("addFontFromAssetManager", AssetManager.class, String.class, Integer.TYPE, Boolean.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, FontVariationAxis[].class);
            } catch (ClassNotFoundException var17) {
               var0 = var17;
               break label73;
            } catch (NoSuchMethodException var18) {
               var0 = var18;
               break label73;
            }

            try {
               var4 = var1.getMethod("addFontFromBuffer", ByteBuffer.class, Integer.TYPE, FontVariationAxis[].class, Integer.TYPE, Integer.TYPE);
            } catch (ClassNotFoundException var15) {
               var0 = var15;
               break label73;
            } catch (NoSuchMethodException var16) {
               var0 = var16;
               break label73;
            }

            try {
               var5 = var1.getMethod("freeze");
            } catch (ClassNotFoundException var13) {
               var0 = var13;
               break label73;
            } catch (NoSuchMethodException var14) {
               var0 = var14;
               break label73;
            }

            try {
               var6 = var1.getMethod("abortCreation");
            } catch (ClassNotFoundException var11) {
               var0 = var11;
               break label73;
            } catch (NoSuchMethodException var12) {
               var0 = var12;
               break label73;
            }

            try {
               var24 = Typeface.class.getDeclaredMethod("createFromFamiliesWithDefault", Array.newInstance(var1, 1).getClass(), Integer.TYPE, Integer.TYPE);
            } catch (ClassNotFoundException var9) {
               var0 = var9;
               break label73;
            } catch (NoSuchMethodException var10) {
               var0 = var10;
               break label73;
            }

            try {
               var24.setAccessible(true);
               break label71;
            } catch (ClassNotFoundException var7) {
               var0 = var7;
            } catch (NoSuchMethodException var8) {
               var0 = var8;
            }
         }

         StringBuilder var23 = new StringBuilder();
         var23.append("Unable to collect necessary methods for class ");
         var23.append(var0.getClass().getName());
         Log.e("TypefaceCompatApi26Impl", var23.toString(), (Throwable)var0);
         var1 = null;
         var2 = null;
         var3 = null;
         var4 = null;
         var5 = null;
         var6 = null;
         var24 = null;
      }

      sFontFamilyCtor = var2;
      sFontFamily = var1;
      sAddFontFromAssetManager = var3;
      sAddFontFromBuffer = var4;
      sFreeze = var5;
      sAbortCreation = var6;
      sCreateFromFamiliesWithDefault = var24;
   }

   private static boolean abortCreation(Object var0) {
      try {
         boolean var1 = (Boolean)sAbortCreation.invoke(var0);
         return var1;
      } catch (IllegalAccessException var2) {
         var0 = var2;
      } catch (InvocationTargetException var3) {
         var0 = var3;
      }

      throw new RuntimeException((Throwable)var0);
   }

   private static boolean addFontFromAssetManager(Context var0, Object var1, String var2, int var3, int var4, int var5) {
      Object var9;
      try {
         boolean var6 = (Boolean)sAddFontFromAssetManager.invoke(var1, var0.getAssets(), var2, 0, false, var3, var4, var5, null);
         return var6;
      } catch (IllegalAccessException var7) {
         var9 = var7;
      } catch (InvocationTargetException var8) {
         var9 = var8;
      }

      throw new RuntimeException((Throwable)var9);
   }

   private static boolean addFontFromBuffer(Object var0, ByteBuffer var1, int var2, int var3, int var4) {
      try {
         boolean var5 = (Boolean)sAddFontFromBuffer.invoke(var0, var1, var2, null, var3, var4);
         return var5;
      } catch (IllegalAccessException var6) {
         var0 = var6;
      } catch (InvocationTargetException var7) {
         var0 = var7;
      }

      throw new RuntimeException((Throwable)var0);
   }

   private static Typeface createFromFamiliesWithDefault(Object var0) {
      try {
         Object var1 = Array.newInstance(sFontFamily, 1);
         Array.set(var1, 0, var0);
         Typeface var4 = (Typeface)sCreateFromFamiliesWithDefault.invoke((Object)null, var1, -1, -1);
         return var4;
      } catch (IllegalAccessException var2) {
         var0 = var2;
      } catch (InvocationTargetException var3) {
         var0 = var3;
      }

      throw new RuntimeException((Throwable)var0);
   }

   private static boolean freeze(Object var0) {
      try {
         boolean var1 = (Boolean)sFreeze.invoke(var0);
         return var1;
      } catch (IllegalAccessException var2) {
         var0 = var2;
      } catch (InvocationTargetException var3) {
         var0 = var3;
      }

      throw new RuntimeException((Throwable)var0);
   }

   private static boolean isFontFamilyPrivateAPIAvailable() {
      if (sAddFontFromAssetManager == null) {
         Log.w("TypefaceCompatApi26Impl", "Unable to collect necessary private methods.Fallback to legacy implementation.");
      }

      return sAddFontFromAssetManager != null;
   }

   private static Object newFamily() {
      Object var0;
      try {
         var0 = sFontFamilyCtor.newInstance();
         return var0;
      } catch (IllegalAccessException var1) {
         var0 = var1;
      } catch (InstantiationException var2) {
         var0 = var2;
      } catch (InvocationTargetException var3) {
         var0 = var3;
      }

      throw new RuntimeException((Throwable)var0);
   }

   public Typeface createFromFontFamilyFilesResourceEntry(Context var1, FontResourcesParserCompat.FontFamilyFilesResourceEntry var2, Resources var3, int var4) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:659)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:629)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   public Typeface createFromFontInfo(Context var1, @Nullable CancellationSignal var2, @NonNull FontsContractCompat.FontInfo[] var3, int var4) {
      throw new RuntimeException("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:659)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:698)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.e2expr(TypeTransformer.java:629)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:716)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\n");
   }

   @Nullable
   public Typeface createFromResourcesFontFile(Context var1, Resources var2, int var3, String var4, int var5) {
      if (!isFontFamilyPrivateAPIAvailable()) {
         return super.createFromResourcesFontFile(var1, var2, var3, var4, var5);
      } else {
         Object var6 = newFamily();
         if (!addFontFromAssetManager(var1, var6, var4, 0, -1, -1)) {
            abortCreation(var6);
            return null;
         } else {
            return !freeze(var6) ? null : createFromFamiliesWithDefault(var6);
         }
      }
   }
}
