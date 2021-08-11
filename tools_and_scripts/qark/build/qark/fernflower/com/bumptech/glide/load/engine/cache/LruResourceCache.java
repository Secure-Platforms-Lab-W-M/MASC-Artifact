package com.bumptech.glide.load.engine.cache;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.LruCache;

public class LruResourceCache extends LruCache implements MemoryCache {
   private MemoryCache.ResourceRemovedListener listener;

   public LruResourceCache(long var1) {
      super(var1);
   }

   protected int getSize(Resource var1) {
      return var1 == null ? super.getSize((Object)null) : var1.getSize();
   }

   protected void onItemEvicted(Key var1, Resource var2) {
      MemoryCache.ResourceRemovedListener var3 = this.listener;
      if (var3 != null && var2 != null) {
         var3.onResourceRemoved(var2);
      }

   }

   public void setResourceRemovedListener(MemoryCache.ResourceRemovedListener var1) {
      this.listener = var1;
   }

   public void trimMemory(int var1) {
      if (var1 >= 40) {
         this.clearMemory();
      } else {
         if (var1 >= 20 || var1 == 15) {
            this.trimToSize(this.getMaxSize() / 2L);
         }

      }
   }
}
