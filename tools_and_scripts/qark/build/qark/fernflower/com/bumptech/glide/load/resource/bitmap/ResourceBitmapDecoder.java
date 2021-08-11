package com.bumptech.glide.load.resource.bitmap;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.drawable.ResourceDrawableDecoder;

public class ResourceBitmapDecoder implements ResourceDecoder {
   private final BitmapPool bitmapPool;
   private final ResourceDrawableDecoder drawableDecoder;

   public ResourceBitmapDecoder(ResourceDrawableDecoder var1, BitmapPool var2) {
      this.drawableDecoder = var1;
      this.bitmapPool = var2;
   }

   public Resource decode(Uri var1, int var2, int var3, Options var4) {
      Resource var5 = this.drawableDecoder.decode(var1, var2, var3, var4);
      if (var5 == null) {
         return null;
      } else {
         Drawable var6 = (Drawable)var5.get();
         return DrawableToBitmapConverter.convert(this.bitmapPool, var6, var2, var3);
      }
   }

   public boolean handles(Uri var1, Options var2) {
      return "android.resource".equals(var1.getScheme());
   }
}
