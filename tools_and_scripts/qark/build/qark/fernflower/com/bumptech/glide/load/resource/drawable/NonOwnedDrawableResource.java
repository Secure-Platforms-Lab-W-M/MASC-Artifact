package com.bumptech.glide.load.resource.drawable;

import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.engine.Resource;

final class NonOwnedDrawableResource extends DrawableResource {
   private NonOwnedDrawableResource(Drawable var1) {
      super(var1);
   }

   static Resource newInstance(Drawable var0) {
      return var0 != null ? new NonOwnedDrawableResource(var0) : null;
   }

   public Class getResourceClass() {
      return this.drawable.getClass();
   }

   public int getSize() {
      return Math.max(1, this.drawable.getIntrinsicWidth() * this.drawable.getIntrinsicHeight() * 4);
   }

   public void recycle() {
   }
}
