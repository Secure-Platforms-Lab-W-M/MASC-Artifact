package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.Initializable;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;

public class BitmapResource implements Resource, Initializable {
   private final Bitmap bitmap;
   private final BitmapPool bitmapPool;

   public BitmapResource(Bitmap var1, BitmapPool var2) {
      this.bitmap = (Bitmap)Preconditions.checkNotNull(var1, "Bitmap must not be null");
      this.bitmapPool = (BitmapPool)Preconditions.checkNotNull(var2, "BitmapPool must not be null");
   }

   public static BitmapResource obtain(Bitmap var0, BitmapPool var1) {
      return var0 == null ? null : new BitmapResource(var0, var1);
   }

   public Bitmap get() {
      return this.bitmap;
   }

   public Class getResourceClass() {
      return Bitmap.class;
   }

   public int getSize() {
      return Util.getBitmapByteSize(this.bitmap);
   }

   public void initialize() {
      this.bitmap.prepareToDraw();
   }

   public void recycle() {
      this.bitmapPool.put(this.bitmap);
   }
}
