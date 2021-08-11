package com.bumptech.glide.load;

public enum DataSource {
   DATA_DISK_CACHE,
   LOCAL,
   MEMORY_CACHE,
   REMOTE,
   RESOURCE_DISK_CACHE;

   static {
      DataSource var0 = new DataSource("MEMORY_CACHE", 4);
      MEMORY_CACHE = var0;
   }
}
