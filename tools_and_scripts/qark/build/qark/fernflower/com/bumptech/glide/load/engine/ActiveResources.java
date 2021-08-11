package com.bumptech.glide.load.engine;

import android.os.Process;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.util.Preconditions;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

final class ActiveResources {
   final Map activeEngineResources;
   // $FF: renamed from: cb com.bumptech.glide.load.engine.ActiveResources$DequeuedResourceCallback
   private volatile ActiveResources.DequeuedResourceCallback field_209;
   private final boolean isActiveResourceRetentionAllowed;
   private volatile boolean isShutdown;
   private EngineResource.ResourceListener listener;
   private final Executor monitorClearedResourcesExecutor;
   private final ReferenceQueue resourceReferenceQueue;

   ActiveResources(boolean var1) {
      this(var1, Executors.newSingleThreadExecutor(new ThreadFactory() {
         public Thread newThread(final Runnable var1) {
            return new Thread(new Runnable() {
               public void run() {
                  Process.setThreadPriority(10);
                  var1.run();
               }
            }, "glide-active-resources");
         }
      }));
   }

   ActiveResources(boolean var1, Executor var2) {
      this.activeEngineResources = new HashMap();
      this.resourceReferenceQueue = new ReferenceQueue();
      this.isActiveResourceRetentionAllowed = var1;
      this.monitorClearedResourcesExecutor = var2;
      var2.execute(new Runnable() {
         public void run() {
            ActiveResources.this.cleanReferenceQueue();
         }
      });
   }

   void activate(Key var1, EngineResource var2) {
      synchronized(this){}

      Throwable var10000;
      label75: {
         boolean var10001;
         ActiveResources.ResourceWeakReference var9;
         try {
            ActiveResources.ResourceWeakReference var11 = new ActiveResources.ResourceWeakReference(var1, var2, this.resourceReferenceQueue, this.isActiveResourceRetentionAllowed);
            var9 = (ActiveResources.ResourceWeakReference)this.activeEngineResources.put(var1, var11);
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label75;
         }

         if (var9 == null) {
            return;
         }

         label66:
         try {
            var9.reset();
            return;
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label66;
         }
      }

      Throwable var10 = var10000;
      throw var10;
   }

   void cleanReferenceQueue() {
      while(!this.isShutdown) {
         label29: {
            ActiveResources.DequeuedResourceCallback var1;
            boolean var10001;
            try {
               this.cleanupActiveReference((ActiveResources.ResourceWeakReference)this.resourceReferenceQueue.remove());
               var1 = this.field_209;
            } catch (InterruptedException var3) {
               var10001 = false;
               break label29;
            }

            if (var1 == null) {
               continue;
            }

            try {
               var1.onResourceDequeued();
               continue;
            } catch (InterruptedException var2) {
               var10001 = false;
            }
         }

         Thread.currentThread().interrupt();
      }

   }

   void cleanupActiveReference(ActiveResources.ResourceWeakReference var1) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label216: {
         label217: {
            try {
               this.activeEngineResources.remove(var1.key);
               if (var1.isCacheable && var1.resource != null) {
                  break label217;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label216;
            }

            try {
               return;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label216;
            }
         }

         try {
            ;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label216;
         }

         EngineResource var2 = new EngineResource(var1.resource, true, false, var1.key, this.listener);
         this.listener.onResourceReleased(var1.key, var2);
         return;
      }

      while(true) {
         Throwable var23 = var10000;

         try {
            throw var23;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   void deactivate(Key var1) {
      synchronized(this){}

      Throwable var10000;
      label75: {
         boolean var10001;
         ActiveResources.ResourceWeakReference var8;
         try {
            var8 = (ActiveResources.ResourceWeakReference)this.activeEngineResources.remove(var1);
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label75;
         }

         if (var8 == null) {
            return;
         }

         label66:
         try {
            var8.reset();
            return;
         } catch (Throwable var6) {
            var10000 = var6;
            var10001 = false;
            break label66;
         }
      }

      Throwable var9 = var10000;
      throw var9;
   }

   EngineResource get(Key var1) {
      synchronized(this){}

      Throwable var10000;
      label132: {
         boolean var10001;
         ActiveResources.ResourceWeakReference var15;
         try {
            var15 = (ActiveResources.ResourceWeakReference)this.activeEngineResources.get(var1);
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label132;
         }

         if (var15 == null) {
            return null;
         }

         EngineResource var2;
         try {
            var2 = (EngineResource)var15.get();
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label132;
         }

         if (var2 == null) {
            try {
               this.cleanupActiveReference(var15);
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break label132;
            }
         }

         return var2;
      }

      Throwable var16 = var10000;
      throw var16;
   }

   void setDequeuedResourceCallback(ActiveResources.DequeuedResourceCallback var1) {
      this.field_209 = var1;
   }

   void setListener(EngineResource.ResourceListener param1) {
      // $FF: Couldn't be decompiled
   }

   void shutdown() {
      this.isShutdown = true;
      Executor var1 = this.monitorClearedResourcesExecutor;
      if (var1 instanceof ExecutorService) {
         com.bumptech.glide.util.Executors.shutdownAndAwaitTermination((ExecutorService)var1);
      }

   }

   interface DequeuedResourceCallback {
      void onResourceDequeued();
   }

   static final class ResourceWeakReference extends WeakReference {
      final boolean isCacheable;
      final Key key;
      Resource resource;

      ResourceWeakReference(Key var1, EngineResource var2, ReferenceQueue var3, boolean var4) {
         super(var2, var3);
         this.key = (Key)Preconditions.checkNotNull(var1);
         Resource var5;
         if (var2.isMemoryCacheable() && var4) {
            var5 = (Resource)Preconditions.checkNotNull(var2.getResource());
         } else {
            var5 = null;
         }

         this.resource = var5;
         this.isCacheable = var2.isMemoryCacheable();
      }

      void reset() {
         this.resource = null;
         this.clear();
      }
   }
}
