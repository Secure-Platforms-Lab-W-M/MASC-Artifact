package com.bumptech.glide.load.engine.cache;

import android.content.Context;
import java.io.File;

public final class InternalCacheDiskCacheFactory extends DiskLruCacheFactory {
   public InternalCacheDiskCacheFactory(Context var1) {
      this(var1, "image_manager_disk_cache", 262144000L);
   }

   public InternalCacheDiskCacheFactory(Context var1, long var2) {
      this(var1, "image_manager_disk_cache", var2);
   }

   public InternalCacheDiskCacheFactory(final Context var1, final String var2, long var3) {
      super(new DiskLruCacheFactory.CacheDirectoryGetter() {
         public File getCacheDirectory() {
            File var1x = var1.getCacheDir();
            if (var1x == null) {
               return null;
            } else {
               return var2 != null ? new File(var1x, var2) : var1x;
            }
         }
      }, var3);
   }
}
