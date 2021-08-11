package com.bumptech.glide.load.resource.gif;

import android.content.Context;
import android.graphics.Bitmap;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;

public class GifDrawableTransformation implements Transformation {
   private final Transformation wrapped;

   public GifDrawableTransformation(Transformation var1) {
      this.wrapped = (Transformation)Preconditions.checkNotNull(var1);
   }

   public boolean equals(Object var1) {
      if (var1 instanceof GifDrawableTransformation) {
         GifDrawableTransformation var2 = (GifDrawableTransformation)var1;
         return this.wrapped.equals(var2.wrapped);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.wrapped.hashCode();
   }

   public Resource transform(Context var1, Resource var2, int var3, int var4) {
      GifDrawable var5 = (GifDrawable)var2.get();
      BitmapPool var6 = Glide.get(var1).getBitmapPool();
      BitmapResource var7 = new BitmapResource(var5.getFirstFrame(), var6);
      Resource var8 = this.wrapped.transform(var1, var7, var3, var4);
      if (!var7.equals(var8)) {
         var7.recycle();
      }

      Bitmap var9 = (Bitmap)var8.get();
      var5.setFrameTransformation(this.wrapped, var9);
      return var2;
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      this.wrapped.updateDiskCacheKey(var1);
   }
}
