package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPoolAdapter;
import java.util.concurrent.locks.Lock;

final class DrawableToBitmapConverter {
   private static final BitmapPool NO_RECYCLE_BITMAP_POOL = new BitmapPoolAdapter() {
      public void put(Bitmap var1) {
      }
   };
   private static final String TAG = "DrawableToBitmap";

   private DrawableToBitmapConverter() {
   }

   static Resource convert(BitmapPool var0, Drawable var1, int var2, int var3) {
      Drawable var5 = var1.getCurrent();
      Bitmap var6 = null;
      boolean var4 = false;
      if (var5 instanceof BitmapDrawable) {
         var6 = ((BitmapDrawable)var5).getBitmap();
      } else if (!(var5 instanceof Animatable)) {
         var6 = drawToBitmap(var0, var5, var2, var3);
         var4 = true;
      }

      if (!var4) {
         var0 = NO_RECYCLE_BITMAP_POOL;
      }

      return BitmapResource.obtain(var6, var0);
   }

   private static Bitmap drawToBitmap(BitmapPool var0, Drawable var1, int var2, int var3) {
      StringBuilder var9;
      if (var2 == Integer.MIN_VALUE && var1.getIntrinsicWidth() <= 0) {
         if (Log.isLoggable("DrawableToBitmap", 5)) {
            var9 = new StringBuilder();
            var9.append("Unable to draw ");
            var9.append(var1);
            var9.append(" to Bitmap with Target.SIZE_ORIGINAL because the Drawable has no intrinsic width");
            Log.w("DrawableToBitmap", var9.toString());
         }

         return null;
      } else if (var3 == Integer.MIN_VALUE && var1.getIntrinsicHeight() <= 0) {
         if (Log.isLoggable("DrawableToBitmap", 5)) {
            var9 = new StringBuilder();
            var9.append("Unable to draw ");
            var9.append(var1);
            var9.append(" to Bitmap with Target.SIZE_ORIGINAL because the Drawable has no intrinsic height");
            Log.w("DrawableToBitmap", var9.toString());
         }

         return null;
      } else {
         if (var1.getIntrinsicWidth() > 0) {
            var2 = var1.getIntrinsicWidth();
         }

         if (var1.getIntrinsicHeight() > 0) {
            var3 = var1.getIntrinsicHeight();
         }

         Lock var4 = TransformationUtils.getBitmapDrawableLock();
         var4.lock();
         Bitmap var8 = var0.get(var2, var3, Config.ARGB_8888);

         try {
            Canvas var5 = new Canvas(var8);
            var1.setBounds(0, 0, var2, var3);
            var1.draw(var5);
            var5.setBitmap((Bitmap)null);
         } finally {
            var4.unlock();
         }

         return var8;
      }
   }
}
