package com.bumptech.glide.load.engine.bitmap_recycle;

import com.bumptech.glide.util.Util;
import java.util.Queue;

abstract class BaseKeyPool {
   private static final int MAX_SIZE = 20;
   private final Queue keyPool = Util.createQueue(20);

   abstract Poolable create();

   Poolable get() {
      Poolable var2 = (Poolable)this.keyPool.poll();
      Poolable var1 = var2;
      if (var2 == null) {
         var1 = this.create();
      }

      return var1;
   }

   public void offer(Poolable var1) {
      if (this.keyPool.size() < 20) {
         this.keyPool.offer(var1);
      }

   }
}
