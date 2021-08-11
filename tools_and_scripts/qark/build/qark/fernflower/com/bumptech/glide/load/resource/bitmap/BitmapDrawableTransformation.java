package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Preconditions;
import java.security.MessageDigest;

@Deprecated
public class BitmapDrawableTransformation implements Transformation {
   private final Transformation wrapped;

   public BitmapDrawableTransformation(Transformation var1) {
      this.wrapped = (Transformation)Preconditions.checkNotNull(new DrawableTransformation(var1, false));
   }

   private static Resource convertToBitmapDrawableResource(Resource var0) {
      if (var0.get() instanceof BitmapDrawable) {
         return var0;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Wrapped transformation unexpectedly returned a non BitmapDrawable resource: ");
         var1.append(var0.get());
         throw new IllegalArgumentException(var1.toString());
      }
   }

   private static Resource convertToDrawableResource(Resource var0) {
      return var0;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof BitmapDrawableTransformation) {
         BitmapDrawableTransformation var2 = (BitmapDrawableTransformation)var1;
         return this.wrapped.equals(var2.wrapped);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return this.wrapped.hashCode();
   }

   public Resource transform(Context var1, Resource var2, int var3, int var4) {
      var2 = convertToDrawableResource(var2);
      return convertToBitmapDrawableResource(this.wrapped.transform(var1, var2, var3, var4));
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      this.wrapped.updateDiskCacheKey(var1);
   }
}
