package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.Resource;

public interface MemoryCache {
   void clearMemory();

   long getCurrentSize();

   long getMaxSize();

   Resource put(Key var1, Resource var2);

   Resource remove(Key var1);

   void setResourceRemovedListener(MemoryCache.ResourceRemovedListener var1);

   void setSizeMultiplier(float var1);

   void trimMemory(int var1);

   public interface ResourceRemovedListener {
      void onResourceRemoved(Resource var1);
   }
}
