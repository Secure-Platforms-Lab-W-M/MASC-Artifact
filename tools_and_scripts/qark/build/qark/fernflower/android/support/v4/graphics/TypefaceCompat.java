package android.support.v4.graphics;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.CancellationSignal;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RestrictTo;
import android.support.v4.content.res.FontResourcesParserCompat;
import android.support.v4.provider.FontsContractCompat;
import android.support.v4.util.LruCache;
import android.widget.TextView;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class TypefaceCompat {
   private static final String TAG = "TypefaceCompat";
   private static final LruCache sTypefaceCache;
   private static final TypefaceCompat.TypefaceCompatImpl sTypefaceCompatImpl;

   static {
      if (VERSION.SDK_INT >= 26) {
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

   public static Typeface createFromFontInfo(Context var0, @Nullable CancellationSignal var1, @NonNull FontsContractCompat.FontInfo[] var2, int var3) {
      return sTypefaceCompatImpl.createFromFontInfo(var0, var1, var2, var3);
   }

   public static Typeface createFromResourcesFamilyXml(Context var0, FontResourcesParserCompat.FamilyResourceEntry var1, Resources var2, int var3, int var4, @Nullable TextView var5) {
      Typeface var6;
      if (var1 instanceof FontResourcesParserCompat.ProviderResourceEntry) {
         FontResourcesParserCompat.ProviderResourceEntry var7 = (FontResourcesParserCompat.ProviderResourceEntry)var1;
         var6 = FontsContractCompat.getFontSync(var0, var7.getRequest(), var5, var7.getFetchStrategy(), var7.getTimeout(), var4);
      } else {
         var6 = sTypefaceCompatImpl.createFromFontFamilyFilesResourceEntry(var0, (FontResourcesParserCompat.FontFamilyFilesResourceEntry)var1, var2, var4);
      }

      if (var6 != null) {
         sTypefaceCache.put(createResourceUid(var2, var3, var4), var6);
      }

      return var6;
   }

   @Nullable
   public static Typeface createFromResourcesFontFile(Context var0, Resources var1, int var2, String var3, int var4) {
      Typeface var5 = sTypefaceCompatImpl.createFromResourcesFontFile(var0, var1, var2, var3, var4);
      if (var5 != null) {
         sTypefaceCache.put(createResourceUid(var1, var2, var4), var5);
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

   interface TypefaceCompatImpl {
      Typeface createFromFontFamilyFilesResourceEntry(Context var1, FontResourcesParserCompat.FontFamilyFilesResourceEntry var2, Resources var3, int var4);

      Typeface createFromFontInfo(Context var1, @Nullable CancellationSignal var2, @NonNull FontsContractCompat.FontInfo[] var3, int var4);

      Typeface createFromResourcesFontFile(Context var1, Resources var2, int var3, String var4, int var5);
   }
}
