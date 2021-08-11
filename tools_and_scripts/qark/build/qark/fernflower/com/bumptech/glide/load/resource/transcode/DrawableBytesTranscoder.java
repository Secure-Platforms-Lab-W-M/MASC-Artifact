package com.bumptech.glide.load.resource.transcode;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.bumptech.glide.load.resource.gif.GifDrawable;

public final class DrawableBytesTranscoder implements ResourceTranscoder {
   private final ResourceTranscoder bitmapBytesTranscoder;
   private final BitmapPool bitmapPool;
   private final ResourceTranscoder gifDrawableBytesTranscoder;

   public DrawableBytesTranscoder(BitmapPool var1, ResourceTranscoder var2, ResourceTranscoder var3) {
      this.bitmapPool = var1;
      this.bitmapBytesTranscoder = var2;
      this.gifDrawableBytesTranscoder = var3;
   }

   private static Resource toGifDrawableResource(Resource var0) {
      return var0;
   }

   public Resource transcode(Resource var1, Options var2) {
      Drawable var3 = (Drawable)var1.get();
      if (var3 instanceof BitmapDrawable) {
         return this.bitmapBytesTranscoder.transcode(BitmapResource.obtain(((BitmapDrawable)var3).getBitmap(), this.bitmapPool), var2);
      } else {
         return var3 instanceof GifDrawable ? this.gifDrawableBytesTranscoder.transcode(toGifDrawableResource(var1), var2) : null;
      }
   }
}
