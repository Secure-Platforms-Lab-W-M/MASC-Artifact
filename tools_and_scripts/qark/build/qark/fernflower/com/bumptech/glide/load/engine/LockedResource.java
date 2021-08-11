package com.bumptech.glide.load.engine;

import androidx.core.util.Pools;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;

final class LockedResource implements Resource, FactoryPools.Poolable {
   private static final Pools.Pool POOL = FactoryPools.threadSafe(20, new FactoryPools.Factory() {
      public LockedResource create() {
         return new LockedResource();
      }
   });
   private boolean isLocked;
   private boolean isRecycled;
   private final StateVerifier stateVerifier = StateVerifier.newInstance();
   private Resource toWrap;

   private void init(Resource var1) {
      this.isRecycled = false;
      this.isLocked = true;
      this.toWrap = var1;
   }

   static LockedResource obtain(Resource var0) {
      LockedResource var1 = (LockedResource)Preconditions.checkNotNull((LockedResource)POOL.acquire());
      var1.init(var0);
      return var1;
   }

   private void release() {
      this.toWrap = null;
      POOL.release(this);
   }

   public Object get() {
      return this.toWrap.get();
   }

   public Class getResourceClass() {
      return this.toWrap.getResourceClass();
   }

   public int getSize() {
      return this.toWrap.getSize();
   }

   public StateVerifier getVerifier() {
      return this.stateVerifier;
   }

   public void recycle() {
      synchronized(this){}

      try {
         this.stateVerifier.throwIfRecycled();
         this.isRecycled = true;
         if (!this.isLocked) {
            this.toWrap.recycle();
            this.release();
         }
      } finally {
         ;
      }

   }

   void unlock() {
      synchronized(this){}

      try {
         this.stateVerifier.throwIfRecycled();
         if (!this.isLocked) {
            throw new IllegalStateException("Already unlocked");
         }

         this.isLocked = false;
         if (this.isRecycled) {
            this.recycle();
         }
      } finally {
         ;
      }

   }
}
