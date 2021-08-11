package com.bumptech.glide.load.model;

import com.bumptech.glide.util.LruCache;
import com.bumptech.glide.util.Util;
import java.util.Queue;

public class ModelCache {
   private static final int DEFAULT_SIZE = 250;
   private final LruCache cache;

   public ModelCache() {
      this(250L);
   }

   public ModelCache(long var1) {
      this.cache = new LruCache(var1) {
         protected void onItemEvicted(ModelCache.ModelKey var1, Object var2) {
            var1.release();
         }
      };
   }

   public void clear() {
      this.cache.clearMemory();
   }

   public Object get(Object var1, int var2, int var3) {
      ModelCache.ModelKey var5 = ModelCache.ModelKey.get(var1, var2, var3);
      Object var4 = this.cache.get(var5);
      var5.release();
      return var4;
   }

   public void put(Object var1, int var2, int var3, Object var4) {
      ModelCache.ModelKey var5 = ModelCache.ModelKey.get(var1, var2, var3);
      this.cache.put(var5, var4);
   }

   static final class ModelKey {
      private static final Queue KEY_QUEUE = Util.createQueue(0);
      private int height;
      private Object model;
      private int width;

      private ModelKey() {
      }

      static ModelCache.ModelKey get(Object param0, int param1, int param2) {
         // $FF: Couldn't be decompiled
      }

      private void init(Object var1, int var2, int var3) {
         this.model = var1;
         this.width = var2;
         this.height = var3;
      }

      public boolean equals(Object var1) {
         boolean var2 = var1 instanceof ModelCache.ModelKey;
         boolean var3 = false;
         if (var2) {
            ModelCache.ModelKey var4 = (ModelCache.ModelKey)var1;
            var2 = var3;
            if (this.width == var4.width) {
               var2 = var3;
               if (this.height == var4.height) {
                  var2 = var3;
                  if (this.model.equals(var4.model)) {
                     var2 = true;
                  }
               }
            }

            return var2;
         } else {
            return false;
         }
      }

      public int hashCode() {
         return (this.height * 31 + this.width) * 31 + this.model.hashCode();
      }

      public void release() {
         // $FF: Couldn't be decompiled
      }
   }
}
