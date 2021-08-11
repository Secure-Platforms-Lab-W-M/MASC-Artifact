package android.support.v4.graphics;

import android.graphics.Bitmap;
import android.os.Build.VERSION;
import android.support.annotation.RequiresApi;

public final class BitmapCompat {
   static final BitmapCompat.BitmapCompatBaseImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 19) {
         IMPL = new BitmapCompat.BitmapCompatApi19Impl();
      } else if (VERSION.SDK_INT >= 18) {
         IMPL = new BitmapCompat.BitmapCompatApi18Impl();
      } else {
         IMPL = new BitmapCompat.BitmapCompatBaseImpl();
      }
   }

   private BitmapCompat() {
   }

   public static int getAllocationByteCount(Bitmap var0) {
      return IMPL.getAllocationByteCount(var0);
   }

   public static boolean hasMipMap(Bitmap var0) {
      return IMPL.hasMipMap(var0);
   }

   public static void setHasMipMap(Bitmap var0, boolean var1) {
      IMPL.setHasMipMap(var0, var1);
   }

   @RequiresApi(18)
   static class BitmapCompatApi18Impl extends BitmapCompat.BitmapCompatBaseImpl {
      public boolean hasMipMap(Bitmap var1) {
         return var1.hasMipMap();
      }

      public void setHasMipMap(Bitmap var1, boolean var2) {
         var1.setHasMipMap(var2);
      }
   }

   @RequiresApi(19)
   static class BitmapCompatApi19Impl extends BitmapCompat.BitmapCompatApi18Impl {
      public int getAllocationByteCount(Bitmap var1) {
         return var1.getAllocationByteCount();
      }
   }

   static class BitmapCompatBaseImpl {
      public int getAllocationByteCount(Bitmap var1) {
         return var1.getByteCount();
      }

      public boolean hasMipMap(Bitmap var1) {
         return false;
      }

      public void setHasMipMap(Bitmap var1, boolean var2) {
      }
   }
}
