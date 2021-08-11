package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.Key;
import java.io.File;

public interface DiskCache {
   void clear();

   void delete(Key var1);

   File get(Key var1);

   void put(Key var1, DiskCache.Writer var2);

   public interface Factory {
      String DEFAULT_DISK_CACHE_DIR = "image_manager_disk_cache";
      int DEFAULT_DISK_CACHE_SIZE = 262144000;

      DiskCache build();
   }

   public interface Writer {
      boolean write(File var1);
   }
}
