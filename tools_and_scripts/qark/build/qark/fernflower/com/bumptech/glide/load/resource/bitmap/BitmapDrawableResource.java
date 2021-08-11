package com.bumptech.glide.load.resource.bitmap;

import android.graphics.drawable.BitmapDrawable;
import com.bumptech.glide.load.engine.Initializable;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.drawable.DrawableResource;
import com.bumptech.glide.util.Util;

public class BitmapDrawableResource extends DrawableResource implements Initializable {
   private final BitmapPool bitmapPool;

   public BitmapDrawableResource(BitmapDrawable var1, BitmapPool var2) {
      super(var1);
      this.bitmapPool = var2;
   }

   public Class getResourceClass() {
      return BitmapDrawable.class;
   }

   public int getSize() {
      return Util.getBitmapByteSize(((BitmapDrawable)this.drawable).getBitmap());
   }

   public void initialize() {
      ((BitmapDrawable)this.drawable).getBitmap().prepareToDraw();
   }

   public void recycle() {
      this.bitmapPool.put(((BitmapDrawable)this.drawable).getBitmap());
   }
}
