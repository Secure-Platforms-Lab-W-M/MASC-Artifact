package com.bumptech.glide.load.resource.bitmap;

import android.graphics.drawable.BitmapDrawable;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.io.File;

public class BitmapDrawableEncoder implements ResourceEncoder {
   private final BitmapPool bitmapPool;
   private final ResourceEncoder encoder;

   public BitmapDrawableEncoder(BitmapPool var1, ResourceEncoder var2) {
      this.bitmapPool = var1;
      this.encoder = var2;
   }

   public boolean encode(Resource var1, File var2, Options var3) {
      return this.encoder.encode(new BitmapResource(((BitmapDrawable)var1.get()).getBitmap(), this.bitmapPool), var2, var3);
   }

   public EncodeStrategy getEncodeStrategy(Options var1) {
      return this.encoder.getEncodeStrategy(var1);
   }
}
