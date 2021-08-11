package com.bumptech.glide.load.resource.bitmap;

import android.content.Context;
import android.content.res.Resources;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;
import java.io.IOException;

public class BitmapDrawableDecoder implements ResourceDecoder {
   private final ResourceDecoder decoder;
   private final Resources resources;

   public BitmapDrawableDecoder(Context var1, ResourceDecoder var2) {
      this(var1.getResources(), var2);
   }

   public BitmapDrawableDecoder(Resources var1, ResourceDecoder var2) {
      this.resources = (Resources)Preconditions.checkNotNull(var1);
      this.decoder = (ResourceDecoder)Preconditions.checkNotNull(var2);
   }

   @Deprecated
   public BitmapDrawableDecoder(Resources var1, BitmapPool var2, ResourceDecoder var3) {
      this(var1, var3);
   }

   public Resource decode(Object var1, int var2, int var3, Options var4) throws IOException {
      Resource var5 = this.decoder.decode(var1, var2, var3, var4);
      return LazyBitmapDrawableResource.obtain(this.resources, var5);
   }

   public boolean handles(Object var1, Options var2) throws IOException {
      return this.decoder.handles(var1, var2);
   }
}
