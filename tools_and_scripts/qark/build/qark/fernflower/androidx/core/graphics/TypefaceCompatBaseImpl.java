package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.util.Log;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.provider.FontsContractCompat;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;

class TypefaceCompatBaseImpl {
   private static final int INVALID_KEY = 0;
   private static final String TAG = "TypefaceCompatBaseImpl";
   private ConcurrentHashMap mFontFamilies = new ConcurrentHashMap();

   private void addFontFamily(Typeface var1, FontResourcesParserCompat.FontFamilyFilesResourceEntry var2) {
      long var3 = getUniqueKey(var1);
      if (var3 != 0L) {
         this.mFontFamilies.put(var3, var2);
      }

   }

   private FontResourcesParserCompat.FontFileResourceEntry findBestEntry(FontResourcesParserCompat.FontFamilyFilesResourceEntry var1, int var2) {
      return (FontResourcesParserCompat.FontFileResourceEntry)findBestFont(var1.getEntries(), var2, new TypefaceCompatBaseImpl.StyleExtractor() {
         public int getWeight(FontResourcesParserCompat.FontFileResourceEntry var1) {
            return var1.getWeight();
         }

         public boolean isItalic(FontResourcesParserCompat.FontFileResourceEntry var1) {
            return var1.isItalic();
         }
      });
   }

   private static Object findBestFont(Object[] var0, int var1, TypefaceCompatBaseImpl.StyleExtractor var2) {
      short var3;
      if ((var1 & 1) == 0) {
         var3 = 400;
      } else {
         var3 = 700;
      }

      boolean var8;
      if ((var1 & 2) != 0) {
         var8 = true;
      } else {
         var8 = false;
      }

      Object var9 = null;
      int var4 = Integer.MAX_VALUE;
      int var7 = var0.length;

      int var11;
      for(var1 = 0; var1 < var7; var4 = var11) {
         Object var10 = var0[var1];
         int var6 = Math.abs(var2.getWeight(var10) - var3);
         byte var5;
         if (var2.isItalic(var10) == var8) {
            var5 = 0;
         } else {
            var5 = 1;
         }

         label30: {
            var6 = var6 * 2 + var5;
            if (var9 != null) {
               var11 = var4;
               if (var4 <= var6) {
                  break label30;
               }
            }

            var9 = var10;
            var11 = var6;
         }

         ++var1;
      }

      return var9;
   }

   private static long getUniqueKey(Typeface var0) {
      if (var0 == null) {
         return 0L;
      } else {
         try {
            Field var3 = Typeface.class.getDeclaredField("native_instance");
            var3.setAccessible(true);
            long var1 = ((Number)var3.get(var0)).longValue();
            return var1;
         } catch (NoSuchFieldException var4) {
            Log.e("TypefaceCompatBaseImpl", "Could not retrieve font from family.", var4);
            return 0L;
         } catch (IllegalAccessException var5) {
            Log.e("TypefaceCompatBaseImpl", "Could not retrieve font from family.", var5);
            return 0L;
         }
      }
   }

   public Typeface createFromFontFamilyFilesResourceEntry(Context var1, FontResourcesParserCompat.FontFamilyFilesResourceEntry var2, Resources var3, int var4) {
      FontResourcesParserCompat.FontFileResourceEntry var5 = this.findBestEntry(var2, var4);
      if (var5 == null) {
         return null;
      } else {
         Typeface var6 = TypefaceCompat.createFromResourcesFontFile(var1, var3, var5.getResourceId(), var5.getFileName(), var4);
         this.addFontFamily(var6, var2);
         return var6;
      }
   }

   public Typeface createFromFontInfo(Context param1, CancellationSignal param2, FontsContractCompat.FontInfo[] param3, int param4) {
      // $FF: Couldn't be decompiled
   }

   protected Typeface createFromInputStream(Context param1, InputStream param2) {
      // $FF: Couldn't be decompiled
   }

   public Typeface createFromResourcesFontFile(Context param1, Resources param2, int param3, String param4, int param5) {
      // $FF: Couldn't be decompiled
   }

   protected FontsContractCompat.FontInfo findBestInfo(FontsContractCompat.FontInfo[] var1, int var2) {
      return (FontsContractCompat.FontInfo)findBestFont(var1, var2, new TypefaceCompatBaseImpl.StyleExtractor() {
         public int getWeight(FontsContractCompat.FontInfo var1) {
            return var1.getWeight();
         }

         public boolean isItalic(FontsContractCompat.FontInfo var1) {
            return var1.isItalic();
         }
      });
   }

   FontResourcesParserCompat.FontFamilyFilesResourceEntry getFontFamily(Typeface var1) {
      long var2 = getUniqueKey(var1);
      return var2 == 0L ? null : (FontResourcesParserCompat.FontFamilyFilesResourceEntry)this.mFontFamilies.get(var2);
   }

   private interface StyleExtractor {
      int getWeight(Object var1);

      boolean isItalic(Object var1);
   }
}
