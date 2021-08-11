package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.Key;
import java.io.File;

public class DiskCacheAdapter implements DiskCache {
   public void clear() {
   }

   public void delete(Key var1) {
   }

   public File get(Key var1) {
      return null;
   }

   public void put(Key var1, DiskCache.Writer var2) {
   }

   public static final class Factory implements DiskCache.Factory {
      public DiskCache build() {
         return new DiskCacheAdapter();
      }
   }
}
