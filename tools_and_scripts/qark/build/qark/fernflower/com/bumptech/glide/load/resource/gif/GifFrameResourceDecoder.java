package com.bumptech.glide.load.resource.gif;

import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;

public final class GifFrameResourceDecoder implements ResourceDecoder {
   private final BitmapPool bitmapPool;

   public GifFrameResourceDecoder(BitmapPool var1) {
      this.bitmapPool = var1;
   }

   public Resource decode(GifDecoder var1, int var2, int var3, Options var4) {
      return BitmapResource.obtain(var1.getNextFrame(), this.bitmapPool);
   }

   public boolean handles(GifDecoder var1, Options var2) {
      return true;
   }
}
