package com.bumptech.glide.load.engine.cache;

import android.content.Context;
import java.io.File;

@Deprecated
public final class ExternalCacheDiskCacheFactory extends DiskLruCacheFactory {
   public ExternalCacheDiskCacheFactory(Context var1) {
      this(var1, "image_manager_disk_cache", 262144000);
   }

   public ExternalCacheDiskCacheFactory(Context var1, int var2) {
      this(var1, "image_manager_disk_cache", var2);
   }

   public ExternalCacheDiskCacheFactory(final Context var1, final String var2, int var3) {
      super(new DiskLruCacheFactory.CacheDirectoryGetter() {
         public File getCacheDirectory() {
            File var1x = var1.getExternalCacheDir();
            if (var1x == null) {
               return null;
            } else {
               return var2 != null ? new File(var1x, var2) : var1x;
            }
         }
      }, (long)var3);
   }
}
