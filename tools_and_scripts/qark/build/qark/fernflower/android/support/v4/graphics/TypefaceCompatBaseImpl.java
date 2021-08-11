package android.support.v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v4.provider.FontsContractCompat;
import java.io.InputStream;

@RequiresApi(14)
@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
class TypefaceCompatBaseImpl implements TypefaceCompat.TypefaceCompatImpl {
   private static final String CACHE_FILE_PREFIX = "cached_font_";
   private static final String TAG = "TypefaceCompatBaseImpl";

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

      int var7 = var0.length;
      int var4 = Integer.MAX_VALUE;
      Object var9 = null;

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

   @Nullable
   public Typeface createFromFontFamilyFilesResourceEntry(Context var1, FontResourcesParserCompat.FontFamilyFilesResourceEntry var2, Resources var3, int var4) {
      FontResourcesParserCompat.FontFileResourceEntry var5 = this.findBestEntry(var2, var4);
      return var5 == null ? null : TypefaceCompat.createFromResourcesFontFile(var1, var3, var5.getResourceId(), var5.getFileName(), var4);
   }

   public Typeface createFromFontInfo(Context param1, @Nullable CancellationSignal param2, @NonNull FontsContractCompat.FontInfo[] param3, int param4) {
      // $FF: Couldn't be decompiled
   }

   protected Typeface createFromInputStream(Context param1, InputStream param2) {
      // $FF: Couldn't be decompiled
   }

   @Nullable
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

   private interface StyleExtractor {
      int getWeight(Object var1);

      boolean isItalic(Object var1);
   }
}
