package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.security.MessageDigest;

public class DrawableTransformation implements Transformation {
   private final boolean isRequired;
   private final Transformation wrapped;

   public DrawableTransformation(Transformation var1, boolean var2) {
      this.wrapped = var1;
      this.isRequired = var2;
   }

   private Resource newDrawableResource(Context var1, Resource var2) {
      return LazyBitmapDrawableResource.obtain(var1.getResources(), var2);
   }

   public Transformation asBitmapDrawable() {
      return this;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof DrawableTransformation) {
         DrawableTransformation var2 = (DrawableTransformation)var1;
         return this.wrapped.equals(var2.wrapped);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.wrapped.hashCode();
   }

   public Resource transform(Context var1, Resource var2, int var3, int var4) {
      BitmapPool var6 = Glide.get(var1).getBitmapPool();
      Drawable var5 = (Drawable)var2.get();
      Resource var8 = DrawableToBitmapConverter.convert(var6, var5, var3, var4);
      if (var8 == null) {
         if (!this.isRequired) {
            return var2;
         } else {
            StringBuilder var9 = new StringBuilder();
            var9.append("Unable to convert ");
            var9.append(var5);
            var9.append(" to a Bitmap");
            throw new IllegalArgumentException(var9.toString());
         }
      } else {
         Resource var7 = this.wrapped.transform(var1, var8, var3, var4);
         if (var7.equals(var8)) {
            var7.recycle();
            return var2;
         } else {
            return this.newDrawableResource(var1, var7);
         }
      }
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      this.wrapped.updateDiskCacheKey(var1);
   }
}
