package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.CancellationSignal;
import android.util.Log;
import androidx.collection.SimpleArrayMap;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.List;

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
      Method var1;
      Constructor var2;
      Method var3;
      Class var6;
      label15: {
         Object var0;
         try {
            var6 = Class.forName("android.graphics.FontFamily");
            var2 = var6.getConstructor();
            var3 = var6.getMethod("addFontWeightStyle", ByteBuffer.class, Integer.TYPE, List.class, Integer.TYPE, Boolean.TYPE);
            var1 = Typeface.class.getMethod("createFromFamiliesWithDefault", Array.newInstance(var6, 1).getClass());
            break label15;
         } catch (ClassNotFoundException var4) {
            var0 = var4;
         } catch (NoSuchMethodException var5) {
            var0 = var5;
         }

         Log.e("TypefaceCompatApi24Impl", var0.getClass().getName(), (Throwable)var0);
         var2 = null;
         var3 = null;
         var6 = null;
         var1 = null;
      }

      sFontFamilyCtor = var2;
      sFontFamily = var6;
      sAddFontWeightStyle = var3;
      sCreateFromFamiliesWithDefault = var1;
   }

   private static boolean addFontWeightStyle(Object var0, ByteBuffer var1, int var2, int var3, boolean var4) {
      try {
         var4 = (Boolean)sAddFontWeightStyle.invoke(var0, var1, var2, null, var3, var4);
         return var4;
      } catch (IllegalAccessException var5) {
         return false;
      } catch (InvocationTargetException var6) {
         return false;
      }
   }

   private static Typeface createFromFamiliesWithDefault(Object var0) {
      try {
         Object var1 = Array.newInstance(sFontFamily, 1);
         Array.set(var1, 0, var0);
         Typeface var4 = (Typeface)sCreateFromFamiliesWithDefault.invoke((Object)null, var1);
         return var4;
      } catch (IllegalAccessException var2) {
         return null;
      } catch (InvocationTargetException var3) {
         return null;
      }
   }

   public static boolean isUsable() {
      if (sAddFontWeightStyle == null) {
         Log.w("TypefaceCompatApi24Impl", "Unable to collect necessary private methods.Fallback to legacy implementation.");
      }

      return sAddFontWeightStyle != null;
   }

   private static Object newFamily() {
      try {
         Object var0 = sFontFamilyCtor.newInstance();
         return var0;
      } catch (IllegalAccessException var1) {
      } catch (InstantiationException var2) {
      } catch (InvocationTargetException var3) {
      }

      return null;
   }

   public Typeface createFromFontFamilyFilesResourceEntry(Context var1, FontResourcesParserCompat.FontFamilyFilesResourceEntry var2, Resources var3, int var4) {
      Object var6 = newFamily();
      if (var6 == null) {
         return null;
      } else {
         FontResourcesParserCompat.FontFileResourceEntry[] var9 = var2.getEntries();
         int var5 = var9.length;

         for(var4 = 0; var4 < var5; ++var4) {
            FontResourcesParserCompat.FontFileResourceEntry var7 = var9[var4];
            ByteBuffer var8 = TypefaceCompatUtil.copyToDirectBuffer(var1, var3, var7.getResourceId());
            if (var8 == null) {
               return null;
            }

            if (!addFontWeightStyle(var6, var8, var7.getTtcIndex(), var7.getWeight(), var7.isItalic())) {
               return null;
            }
         }

         return createFromFamiliesWithDefault(var6);
      }
   }

   public Typeface createFromFontInfo(Context var1, CancellationSignal var2, FontsContractCompat.FontInfo[] var3, int var4) {
      Object var9 = newFamily();
      if (var9 == null) {
         return null;
      } else {
         SimpleArrayMap var10 = new SimpleArrayMap();
         int var6 = var3.length;

         for(int var5 = 0; var5 < var6; ++var5) {
            FontsContractCompat.FontInfo var11 = var3[var5];
            Uri var12 = var11.getUri();
            ByteBuffer var8 = (ByteBuffer)var10.get(var12);
            ByteBuffer var7 = var8;
            if (var8 == null) {
               var7 = TypefaceCompatUtil.mmap(var1, var2, var12);
               var10.put(var12, var7);
            }

            if (var7 == null) {
               return null;
            }

            if (!addFontWeightStyle(var9, var7, var11.getTtcIndex(), var11.getWeight(), var11.isItalic())) {
               return null;
            }
         }

         Typeface var13 = createFromFamiliesWithDefault(var9);
         if (var13 == null) {
            return null;
         } else {
            return Typeface.create(var13, var4);
         }
      }
   }
}
