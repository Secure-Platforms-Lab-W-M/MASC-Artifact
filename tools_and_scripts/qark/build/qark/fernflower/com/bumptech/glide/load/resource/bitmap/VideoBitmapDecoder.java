package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;

@Deprecated
public class VideoBitmapDecoder extends VideoDecoder {
   public VideoBitmapDecoder(Context var1) {
      this(Glide.get(var1).getBitmapPool());
   }

   public VideoBitmapDecoder(BitmapPool var1) {
      super(var1, new VideoDecoder.ParcelFileDescriptorInitializer());
   }
}
