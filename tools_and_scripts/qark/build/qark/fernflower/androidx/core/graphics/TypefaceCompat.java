package androidx.core.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.os.Handler;
import android.os.Build.VERSION;
import androidx.collection.LruCache;
import androidx.core.content.res.FontResourcesParserCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.provider.FontsContractCompat;

public class TypefaceCompat {
   private static final LruCache sTypefaceCache;
   private static final TypefaceCompatBaseImpl sTypefaceCompatImpl;

   static {
      if (VERSION.SDK_INT >= 28) {
         sTypefaceCompatImpl = new TypefaceCompatApi28Impl();
      } else if (VERSION.SDK_INT >= 26) {
         sTypefaceCompatImpl = new TypefaceCompatApi26Impl();
      } else if (VERSION.SDK_INT >= 24 && TypefaceCompatApi24Impl.isUsable()) {
         sTypefaceCompatImpl = new TypefaceCompatApi24Impl();
      } else if (VERSION.SDK_INT >= 21) {
         sTypefaceCompatImpl = new TypefaceCompatApi21Impl();
      } else {
         sTypefaceCompatImpl = new TypefaceCompatBaseImpl();
      }

      sTypefaceCache = new LruCache(16);
   }

   private TypefaceCompat() {
   }

   public static Typeface create(Context var0, Typeface var1, int var2) {
      if (var0 != null) {
         if (VERSION.SDK_INT < 21) {
            Typeface var3 = getBestFontFromFamily(var0, var1, var2);
            if (var3 != null) {
               return var3;
            }
         }

         return Typeface.create(var1, var2);
      } else {
         throw new IllegalArgumentException("Context cannot be null");
      }
   }

   public static Typeface createFromFontInfo(Context var0, CancellationSignal var1, FontsContractCompat.FontInfo[] var2, int var3) {
      return sTypefaceCompatImpl.createFromFontInfo(var0, var1, var2, var3);
   }

   public static Typeface createFromResourcesFamilyXml(Context var0, FontResourcesParserCompat.FamilyResourceEntry var1, Resources var2, int var3, int var4, ResourcesCompat.FontCallback var5, Handler var6, boolean var7) {
      Typeface var10;
      if (var1 instanceof FontResourcesParserCompat.ProviderResourceEntry) {
         boolean var9;
         FontResourcesParserCompat.ProviderResourceEntry var11;
         label34: {
            label33: {
               var11 = (FontResourcesParserCompat.ProviderResourceEntry)var1;
               if (var7) {
                  if (var11.getFetchStrategy() == 0) {
                     break label33;
                  }
               } else if (var5 == null) {
                  break label33;
               }

               var9 = false;
               break label34;
            }

            var9 = true;
         }

         int var8;
         if (var7) {
            var8 = var11.getTimeout();
         } else {
            var8 = -1;
         }

         var10 = FontsContractCompat.getFontSync(var0, var11.getRequest(), var5, var6, var9, var8, var4);
      } else {
         Typeface var12 = sTypefaceCompatImpl.createFromFontFamilyFilesResourceEntry(var0, (FontResourcesParserCompat.FontFamilyFilesResourceEntry)var1, var2, var4);
         var10 = var12;
         if (var5 != null) {
            if (var12 != null) {
               var5.callbackSuccessAsync(var12, var6);
               var10 = var12;
            } else {
               var5.callbackFailAsync(-3, var6);
               var10 = var12;
            }
         }
      }

      if (var10 != null) {
         sTypefaceCache.put(createResourceUid(var2, var3, var4), var10);
      }

      return var10;
   }

   public static Typeface createFromResourcesFontFile(Context var0, Resources var1, int var2, String var3, int var4) {
      Typeface var5 = sTypefaceCompatImpl.createFromResourcesFontFile(var0, var1, var2, var3, var4);
      if (var5 != null) {
         String var6 = createResourceUid(var1, var2, var4);
         sTypefaceCache.put(var6, var5);
      }

      return var5;
   }

   private static String createResourceUid(Resources var0, int var1, int var2) {
      StringBuilder var3 = new StringBuilder();
      var3.append(var0.getResourcePackageName(var1));
      var3.append("-");
      var3.append(var1);
      var3.append("-");
      var3.append(var2);
      return var3.toString();
   }

   public static Typeface findFromCache(Resources var0, int var1, int var2) {
      return (Typeface)sTypefaceCache.get(createResourceUid(var0, var1, var2));
   }

   private static Typeface getBestFontFromFamily(Context var0, Typeface var1, int var2) {
      FontResourcesParserCompat.FontFamilyFilesResourceEntry var3 = sTypefaceCompatImpl.getFontFamily(var1);
      return var3 == null ? null : sTypefaceCompatImpl.createFromFontFamilyFilesResourceEntry(var0, var3, var0.getResources(), var2);
   }
}
