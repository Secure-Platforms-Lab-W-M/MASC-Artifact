package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.cache.DiskCache;
import java.io.File;

class DataCacheWriter implements DiskCache.Writer {
   private final Object data;
   private final Encoder encoder;
   private final Options options;

   DataCacheWriter(Encoder var1, Object var2, Options var3) {
      this.encoder = var1;
      this.data = var2;
      this.options = var3;
   }

   public boolean write(File var1) {
      return this.encoder.encode(this.data, var1, this.options);
   }
}
