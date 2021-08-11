package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.Preconditions;

class EngineResource implements Resource {
   private int acquired;
   private final boolean isMemoryCacheable;
   private final boolean isRecyclable;
   private boolean isRecycled;
   private final Key key;
   private final EngineResource.ResourceListener listener;
   private final Resource resource;

   EngineResource(Resource var1, boolean var2, boolean var3, Key var4, EngineResource.ResourceListener var5) {
      this.resource = (Resource)Preconditions.checkNotNull(var1);
      this.isMemoryCacheable = var2;
      this.isRecyclable = var3;
      this.key = var4;
      this.listener = (EngineResource.ResourceListener)Preconditions.checkNotNull(var5);
   }

   void acquire() {
      synchronized(this){}

      try {
         if (this.isRecycled) {
            throw new IllegalStateException("Cannot acquire a recycled resource");
         }

         ++this.acquired;
      } finally {
         ;
      }

   }

   public Object get() {
      return this.resource.get();
   }

   Resource getResource() {
      return this.resource;
   }

   public Class getResourceClass() {
      return this.resource.getResourceClass();
   }

   public int getSize() {
      return this.resource.getSize();
   }

   boolean isMemoryCacheable() {
      return this.isMemoryCacheable;
   }

   public void recycle() {
      synchronized(this){}

      try {
         if (this.acquired > 0) {
            throw new IllegalStateException("Cannot recycle a resource while it is still acquired");
         }

         if (this.isRecycled) {
            throw new IllegalStateException("Cannot recycle a resource that has already been recycled");
         }

         this.isRecycled = true;
         if (this.isRecyclable) {
            this.resource.recycle();
         }
      } finally {
         ;
      }

   }

   void release() {
      boolean var1 = false;
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label251: {
         int var2;
         label245: {
            try {
               if (this.acquired > 0) {
                  var2 = this.acquired - 1;
                  this.acquired = var2;
                  break label245;
               }
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label251;
            }

            try {
               throw new IllegalStateException("Cannot release a recycled or not yet acquired resource");
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label251;
            }
         }

         if (var2 == 0) {
            var1 = true;
         }

         try {
            ;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label251;
         }

         if (var1) {
            this.listener.onResourceReleased(this.key, this);
         }

         return;
      }

      while(true) {
         Throwable var3 = var10000;

         try {
            throw var3;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }

   public String toString() {
      synchronized(this){}

      String var4;
      try {
         StringBuilder var1 = new StringBuilder();
         var1.append("EngineResource{isMemoryCacheable=");
         var1.append(this.isMemoryCacheable);
         var1.append(", listener=");
         var1.append(this.listener);
         var1.append(", key=");
         var1.append(this.key);
         var1.append(", acquired=");
         var1.append(this.acquired);
         var1.append(", isRecycled=");
         var1.append(this.isRecycled);
         var1.append(", resource=");
         var1.append(this.resource);
         var1.append('}');
         var4 = var1.toString();
      } finally {
         ;
      }

      return var4;
   }

   interface ResourceListener {
      void onResourceReleased(Key var1, EngineResource var2);
   }
}
