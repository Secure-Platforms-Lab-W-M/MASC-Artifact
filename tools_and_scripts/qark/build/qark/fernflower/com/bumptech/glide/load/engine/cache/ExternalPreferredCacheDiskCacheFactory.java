package com.bumptech.glide.load.engine.cache;

import android.content.Context;
import java.io.File;

public final class ExternalPreferredCacheDiskCacheFactory extends DiskLruCacheFactory {
   public ExternalPreferredCacheDiskCacheFactory(Context var1) {
      this(var1, "image_manager_disk_cache", 262144000L);
   }

   public ExternalPreferredCacheDiskCacheFactory(Context var1, long var2) {
      this(var1, "image_manager_disk_cache", var2);
   }

   public ExternalPreferredCacheDiskCacheFactory(final Context var1, final String var2, long var3) {
      super(new DiskLruCacheFactory.CacheDirectoryGetter() {
         private File getInternalCacheDirectory() {
            File var1x = var1.getCacheDir();
            if (var1x == null) {
               return null;
            } else {
               return var2 != null ? new File(var1x, var2) : var1x;
            }
         }

         public File getCacheDirectory() {
            File var1x = this.getInternalCacheDirectory();
            if (var1x != null && var1x.exists()) {
               return var1x;
            } else {
               File var2x = var1.getExternalCacheDir();
               if (var2x != null) {
                  if (!var2x.canWrite()) {
                     return var1x;
                  } else {
                     return var2 != null ? new File(var2x, var2) : var2x;
                  }
               } else {
                  return var1x;
               }
            }
         }
      }, var3);
   }
}
