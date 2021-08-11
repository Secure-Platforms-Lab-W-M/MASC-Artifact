package com.bumptech.glide.load.engine.cache;

import java.io.File;

public class DiskLruCacheFactory implements DiskCache.Factory {
   private final DiskLruCacheFactory.CacheDirectoryGetter cacheDirectoryGetter;
   private final long diskCacheSize;

   public DiskLruCacheFactory(DiskLruCacheFactory.CacheDirectoryGetter var1, long var2) {
      this.diskCacheSize = var2;
      this.cacheDirectoryGetter = var1;
   }

   public DiskLruCacheFactory(final String var1, long var2) {
      this(new DiskLruCacheFactory.CacheDirectoryGetter() {
         public File getCacheDirectory() {
            return new File(var1);
         }
      }, var2);
   }

   public DiskLruCacheFactory(final String var1, final String var2, long var3) {
      this(new DiskLruCacheFactory.CacheDirectoryGetter() {
         public File getCacheDirectory() {
            return new File(var1, var2);
         }
      }, var3);
   }

   public DiskCache build() {
      File var1 = this.cacheDirectoryGetter.getCacheDirectory();
      if (var1 == null) {
         return null;
      } else {
         return var1.mkdirs() || var1.exists() && var1.isDirectory() ? DiskLruCacheWrapper.create(var1, this.diskCacheSize) : null;
      }
   }

   public interface CacheDirectoryGetter {
      File getCacheDirectory();
   }
}
