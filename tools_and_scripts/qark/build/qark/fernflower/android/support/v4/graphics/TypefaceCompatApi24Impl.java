package android.support.v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v4.provider.FontsContractCompat;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.List;

@RequiresApi(24)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
class TypefaceCompatApi24Impl extends TypefaceCompatBaseImpl {
   private static final String ADD_FONT_WEIGHT_STYLE_METHOD = "addFontWeightStyle";
   private static final String CREATE_FROM_FAMILIES_WITH_DEFAULT_METHOD = "createFromFamiliesWithDefault";
   private static final String FONT_FAMILY_CLASS = "android.graphics.FontFamily";
   private static final String TAG = "TypefaceCompatApi24Impl";
   private static final Method sAddFontWeightStyle;
   private static final Method sCreateFromFamiliesWithDefault;
   private static final Class sFontFamily;
   private static final Constructor sFontFamilyCtor;

   static {
      Class var1;
      Constructor var2;
      Method var3;
      Method var12;
      label39: {
         Object var0;
         label41: {
            try {
               var1 = Class.forName("android.graphics.FontFamily");
            } catch (ClassNotFoundException var10) {
               var0 = var10;
               break label41;
            } catch (NoSuchMethodException var11) {
               var0 = var11;
               break label41;
            }

            try {
               var2 = var1.getConstructor();
            } catch (ClassNotFoundException var8) {
               var0 = var8;
               break label41;
            } catch (NoSuchMethodException var9) {
               var0 = var9;
               break label41;
            }

            try {
               var3 = var1.getMethod("addFontWeightStyle", ByteBuffer.class, Integer.TYPE, List.class, Integer.TYPE, Boolean.TYPE);
            } catch (ClassNotFoundException var6) {
               var0 = var6;
               break label41;
            } catch (NoSuchMethodException var7) {
               var0 = var7;
               break label41;
            }

            try {
               var12 = Typeface.class.getMethod("createFromFamiliesWithDefault", Array.newInstance(var1, 1).getClass());
               break label39;
            } catch (ClassNotFoundException var4) {
               var0 = var4;
            } catch (NoSuchMethodException var5) {
               var0 = var5;
            }
         }

         Log.e("TypefaceCompatApi24Impl", var0.getClass().getName(), (Throwable)var0);
         var1 = null;
         var2 = null;
         var3 = null;
         var12 = null;
      }

      sFontFamilyCtor = var2;
      sFontFamily = var1;
      sAddFontWeightStyle = var3;
      sCreateFromFamiliesWithDefault = var12;
   }

   private static boolean addFontWeightStyle(Object var0, ByteBuffer var1, int var2, int var3, boolean var4) {
      try {
         var4 = (Boolean)sAddFontWeightStyle.invoke(var0, var1, var2, null, var3, var4);
         return var4;
      } catch (IllegalAccessException var5) {
         var0 = var5;
      } catch (InvocationTargetException var6) {
         var0 = var6;
      }

      throw new RuntimeException((Throwable)var0);
   }

   private static Typeface createFromFamiliesWithDefault(Object var0) {
      try {
         Object var1 = Array.newInstance(sFontFamily, 1);
         Array.set(var1, 0, var0);
         Typeface var4 = (Typeface)sCreateFromFamiliesWithDefault.invoke((Object)null, var1);
         return var4;
      } catch (IllegalAccessException var2) {
         var0 = var2;
      } catch (InvocationTargetException var3) {
         var0 = var3;
      }

      throw new RuntimeException((Throwable)var0);
   }

   public static boolean isUsable() {
      if (sAddFontWeightStyle == null) {
         Log.w("TypefaceCompatApi24Impl", "Unable to collect necessary private methods.Fallback to legacy implementation.");
      }

      return sAddFontWeightStyle != null;
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
      Object var6 = newFamily();
      FontResourcesParserCompat.FontFileResourceEntry[] var8 = var2.getEntries();
      int var5 = var8.length;

      for(var4 = 0; var4 < var5; ++var4) {
         FontResourcesParserCompat.FontFileResourceEntry var7 = var8[var4];
         if (!addFontWeightStyle(var6, TypefaceCompatUtil.copyToDirectBuffer(var1, var3, var7.getResourceId()), 0, var7.getWeight(), var7.isItalic())) {
            return null;
         }
      }

      return createFromFamiliesWithDefault(var6);
   }

   public Typeface createFromFontInfo(Context var1, @Nullable CancellationSignal var2, @NonNull FontsContractCompat.FontInfo[] var3, int var4) {
      Object var8 = newFamily();
      SimpleArrayMap var9 = new SimpleArrayMap();
      int var5 = var3.length;

      for(var4 = 0; var4 < var5; ++var4) {
         FontsContractCompat.FontInfo var10 = var3[var4];
         Uri var11 = var10.getUri();
         ByteBuffer var7 = (ByteBuffer)var9.get(var11);
         ByteBuffer var6 = var7;
         if (var7 == null) {
            var6 = TypefaceCompatUtil.mmap(var1, var2, var11);
            var9.put(var11, var6);
         }

         if (!addFontWeightStyle(var8, var6, var10.getTtcIndex(), var10.getWeight(), var10.isItalic())) {
            return null;
         }
      }

      return createFromFamiliesWithDefault(var8);
   }
}
