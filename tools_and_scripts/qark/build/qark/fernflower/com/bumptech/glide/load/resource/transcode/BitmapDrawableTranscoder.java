package com.bumptech.glide.load.resource.transcode;

import android.content.Context;
import android.content.res.Resources;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.LazyBitmapDrawableResource;
import com.bumptech.glide.util.Preconditions;

public class BitmapDrawableTranscoder implements ResourceTranscoder {
   private final Resources resources;

   public BitmapDrawableTranscoder(Context var1) {
      this(var1.getResources());
   }

   public BitmapDrawableTranscoder(Resources var1) {
      this.resources = (Resources)Preconditions.checkNotNull(var1);
   }

   @Deprecated
   public BitmapDrawableTranscoder(Resources var1, BitmapPool var2) {
      this(var1);
   }

   public Resource transcode(Resource var1, Options var2) {
      return LazyBitmapDrawableResource.obtain(this.resources, var1);
   }
}
