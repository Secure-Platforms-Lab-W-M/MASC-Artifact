package androidx.core.graphics;

import android.graphics.Bitmap;
import android.os.Build.VERSION;

public final class BitmapCompat {
   private BitmapCompat() {
   }

   public static int getAllocationByteCount(Bitmap var0) {
      return VERSION.SDK_INT >= 19 ? var0.getAllocationByteCount() : var0.getByteCount();
   }

   public static boolean hasMipMap(Bitmap var0) {
      return VERSION.SDK_INT >= 18 ? var0.hasMipMap() : false;
   }

   public static void setHasMipMap(Bitmap var0, boolean var1) {
      if (VERSION.SDK_INT >= 18) {
         var0.setHasMipMap(var1);
      }

   }
}
