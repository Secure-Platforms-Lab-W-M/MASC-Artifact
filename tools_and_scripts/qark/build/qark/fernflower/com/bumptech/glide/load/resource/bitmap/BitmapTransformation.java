package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Util;

public abstract class BitmapTransformation implements Transformation {
   protected abstract Bitmap transform(BitmapPool var1, Bitmap var2, int var3, int var4);

   public final Resource transform(Context var1, Resource var2, int var3, int var4) {
      if (Util.isValidDimensions(var3, var4)) {
         BitmapPool var8 = Glide.get(var1).getBitmapPool();
         Bitmap var5 = (Bitmap)var2.get();
         if (var3 == Integer.MIN_VALUE) {
            var3 = var5.getWidth();
         }

         if (var4 == Integer.MIN_VALUE) {
            var4 = var5.getHeight();
         }

         Bitmap var6 = this.transform(var8, var5, var3, var4);
         return (Resource)(var5.equals(var6) ? var2 : BitmapResource.obtain(var6, var8));
      } else {
         StringBuilder var7 = new StringBuilder();
         var7.append("Cannot apply transformation on width: ");
         var7.append(var3);
         var7.append(" or height: ");
         var7.append(var4);
         var7.append(" less than or equal to zero and not Target.SIZE_ORIGINAL");
         throw new IllegalArgumentException(var7.toString());
      }
   }
}
