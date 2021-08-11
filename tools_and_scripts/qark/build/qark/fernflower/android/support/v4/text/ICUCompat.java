package android.support.v4.text;

import android.os.Build.VERSION;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import java.util.Locale;

public final class ICUCompat {
   private static final ICUCompat.ICUCompatBaseImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 21) {
         IMPL = new ICUCompat.ICUCompatApi21Impl();
      } else {
         IMPL = new ICUCompat.ICUCompatBaseImpl();
      }
   }

   private ICUCompat() {
   }

   @Nullable
   public static String maximizeAndGetScript(Locale var0) {
      return IMPL.maximizeAndGetScript(var0);
   }

   @RequiresApi(21)
   static class ICUCompatApi21Impl extends ICUCompat.ICUCompatBaseImpl {
      public String maximizeAndGetScript(Locale var1) {
         return ICUCompatApi21.maximizeAndGetScript(var1);
      }
   }

   static class ICUCompatBaseImpl {
      public String maximizeAndGetScript(Locale var1) {
         return ICUCompatIcs.maximizeAndGetScript(var1);
      }
   }
}
