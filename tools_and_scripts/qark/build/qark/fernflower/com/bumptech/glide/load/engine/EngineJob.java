package com.bumptech.glide.load.engine;

import androidx.core.util.Pools;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.util.Executors;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

class EngineJob implements DecodeJob.Callback, FactoryPools.Poolable {
   private static final EngineJob.EngineResourceFactory DEFAULT_FACTORY = new EngineJob.EngineResourceFactory();
   private final GlideExecutor animationExecutor;
   final EngineJob.ResourceCallbacksAndExecutors cbs;
   DataSource dataSource;
   private DecodeJob decodeJob;
   private final GlideExecutor diskCacheExecutor;
   private final EngineJobListener engineJobListener;
   EngineResource engineResource;
   private final EngineJob.EngineResourceFactory engineResourceFactory;
   GlideException exception;
   private boolean hasLoadFailed;
   private boolean hasResource;
   private boolean isCacheable;
   private volatile boolean isCancelled;
   private Key key;
   private boolean onlyRetrieveFromCache;
   private final AtomicInteger pendingCallbacks;
   private final Pools.Pool pool;
   private Resource resource;
   private final EngineResource.ResourceListener resourceListener;
   private final GlideExecutor sourceExecutor;
   private final GlideExecutor sourceUnlimitedExecutor;
   private final StateVerifier stateVerifier;
   private boolean useAnimationPool;
   private boolean useUnlimitedSourceGeneratorPool;

   EngineJob(GlideExecutor var1, GlideExecutor var2, GlideExecutor var3, GlideExecutor var4, EngineJobListener var5, EngineResource.ResourceListener var6, Pools.Pool var7) {
      this(var1, var2, var3, var4, var5, var6, var7, DEFAULT_FACTORY);
   }

   EngineJob(GlideExecutor var1, GlideExecutor var2, GlideExecutor var3, GlideExecutor var4, EngineJobListener var5, EngineResource.ResourceListener var6, Pools.Pool var7, EngineJob.EngineResourceFactory var8) {
      this.cbs = new EngineJob.ResourceCallbacksAndExecutors();
      this.stateVerifier = StateVerifier.newInstance();
      this.pendingCallbacks = new AtomicInteger();
      this.diskCacheExecutor = var1;
      this.sourceExecutor = var2;
      this.sourceUnlimitedExecutor = var3;
      this.animationExecutor = var4;
      this.engineJobListener = var5;
      this.resourceListener = var6;
      this.pool = var7;
      this.engineResourceFactory = var8;
   }

   private GlideExecutor getActiveSourceExecutor() {
      if (this.useUnlimitedSourceGeneratorPool) {
         return this.sourceUnlimitedExecutor;
      } else {
         return this.useAnimationPool ? this.animationExecutor : this.sourceExecutor;
      }
   }

   private boolean isDone() {
      return this.hasLoadFailed || this.hasResource || this.isCancelled;
   }

   private void release() {
      synchronized(this){}

      try {
         if (this.key == null) {
            throw new IllegalArgumentException();
         }

         this.cbs.clear();
         this.key = null;
         this.engineResource = null;
         this.resource = null;
         this.hasLoadFailed = false;
         this.isCancelled = false;
         this.hasResource = false;
         this.decodeJob.release(false);
         this.decodeJob = null;
         this.exception = null;
         this.dataSource = null;
         this.pool.release(this);
      } finally {
         ;
      }

   }

   void addCallback(ResourceCallback var1, Executor var2) {
      synchronized(this){}

      Throwable var10000;
      label292: {
         boolean var4;
         boolean var10001;
         try {
            this.stateVerifier.throwIfRecycled();
            this.cbs.add(var1, var2);
            var4 = this.hasResource;
         } catch (Throwable var34) {
            var10000 = var34;
            var10001 = false;
            break label292;
         }

         boolean var3 = true;
         if (var4) {
            try {
               this.incrementPendingCallbacks(1);
               var2.execute(new EngineJob.CallResourceReady(var1));
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               break label292;
            }
         } else {
            try {
               if (this.hasLoadFailed) {
                  this.incrementPendingCallbacks(1);
                  var2.execute(new EngineJob.CallLoadFailed(var1));
                  return;
               }
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label292;
            }

            label270: {
               try {
                  if (!this.isCancelled) {
                     break label270;
                  }
               } catch (Throwable var32) {
                  var10000 = var32;
                  var10001 = false;
                  break label292;
               }

               var3 = false;
            }

            try {
               Preconditions.checkArgument(var3, "Cannot add callbacks to a cancelled EngineJob");
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label292;
            }
         }

         return;
      }

      Throwable var35 = var10000;
      throw var35;
   }

   void callCallbackOnLoadFailed(ResourceCallback var1) {
      try {
         var1.onLoadFailed(this.exception);
      } catch (Throwable var3) {
         throw new CallbackException(var3);
      }
   }

   void callCallbackOnResourceReady(ResourceCallback var1) {
      try {
         var1.onResourceReady(this.engineResource, this.dataSource);
      } catch (Throwable var3) {
         throw new CallbackException(var3);
      }
   }

   void cancel() {
      if (!this.isDone()) {
         this.isCancelled = true;
         this.decodeJob.cancel();
         this.engineJobListener.onEngineJobCancelled(this, this.key);
      }
   }

   void decrementPendingCallbacks() {
      EngineResource var3 = null;
      synchronized(this){}

      label311: {
         Throwable var10000;
         boolean var10001;
         label312: {
            int var1;
            try {
               this.stateVerifier.throwIfRecycled();
               Preconditions.checkArgument(this.isDone(), "Not yet complete!");
               var1 = this.pendingCallbacks.decrementAndGet();
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label312;
            }

            boolean var2;
            if (var1 >= 0) {
               var2 = true;
            } else {
               var2 = false;
            }

            try {
               Preconditions.checkArgument(var2, "Can't decrement below 0");
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label312;
            }

            if (var1 == 0) {
               try {
                  var3 = this.engineResource;
                  this.release();
               } catch (Throwable var31) {
                  var10000 = var31;
                  var10001 = false;
                  break label312;
               }
            }

            label295:
            try {
               break label311;
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               break label295;
            }
         }

         while(true) {
            Throwable var34 = var10000;

            try {
               throw var34;
            } catch (Throwable var29) {
               var10000 = var29;
               var10001 = false;
               continue;
            }
         }
      }

      if (var3 != null) {
         var3.release();
      }

   }

   public StateVerifier getVerifier() {
      return this.stateVerifier;
   }

   void incrementPendingCallbacks(int var1) {
      synchronized(this){}

      try {
         Preconditions.checkArgument(this.isDone(), "Not yet complete!");
         if (this.pendingCallbacks.getAndAdd(var1) == 0 && this.engineResource != null) {
            this.engineResource.acquire();
         }
      } finally {
         ;
      }

   }

   EngineJob init(Key var1, boolean var2, boolean var3, boolean var4, boolean var5) {
      synchronized(this){}

      try {
         this.key = var1;
         this.isCacheable = var2;
         this.useUnlimitedSourceGeneratorPool = var3;
         this.useAnimationPool = var4;
         this.onlyRetrieveFromCache = var5;
      } finally {
         ;
      }

      return this;
   }

   boolean isCancelled() {
      synchronized(this){}

      boolean var1;
      try {
         var1 = this.isCancelled;
      } finally {
         ;
      }

      return var1;
   }

   void notifyCallbacksOfException() {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label371: {
         try {
            this.stateVerifier.throwIfRecycled();
            if (this.isCancelled) {
               this.release();
               return;
            }
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label371;
         }

         Key var1;
         EngineJob.ResourceCallbacksAndExecutors var2;
         label365: {
            label364: {
               try {
                  if (!this.cbs.isEmpty()) {
                     if (this.hasLoadFailed) {
                        break label364;
                     }

                     this.hasLoadFailed = true;
                     var1 = this.key;
                     var2 = this.cbs.copy();
                     this.incrementPendingCallbacks(var2.size() + 1);
                     break label365;
                  }
               } catch (Throwable var32) {
                  var10000 = var32;
                  var10001 = false;
                  break label371;
               }

               try {
                  throw new IllegalStateException("Received an exception without any callbacks to notify");
               } catch (Throwable var30) {
                  var10000 = var30;
                  var10001 = false;
                  break label371;
               }
            }

            try {
               throw new IllegalStateException("Already failed once");
            } catch (Throwable var29) {
               var10000 = var29;
               var10001 = false;
               break label371;
            }
         }

         this.engineJobListener.onEngineJobComplete(this, var1, (EngineResource)null);
         Iterator var33 = var2.iterator();

         while(var33.hasNext()) {
            EngineJob.ResourceCallbackAndExecutor var35 = (EngineJob.ResourceCallbackAndExecutor)var33.next();
            var35.executor.execute(new EngineJob.CallLoadFailed(var35.field_53));
         }

         this.decrementPendingCallbacks();
         return;
      }

      while(true) {
         Throwable var34 = var10000;

         try {
            throw var34;
         } catch (Throwable var28) {
            var10000 = var28;
            var10001 = false;
            continue;
         }
      }
   }

   void notifyCallbacksOfResult() {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label371: {
         try {
            this.stateVerifier.throwIfRecycled();
            if (this.isCancelled) {
               this.resource.recycle();
               this.release();
               return;
            }
         } catch (Throwable var32) {
            var10000 = var32;
            var10001 = false;
            break label371;
         }

         EngineJob.ResourceCallbacksAndExecutors var1;
         Key var2;
         EngineResource var3;
         label365: {
            label364: {
               try {
                  if (!this.cbs.isEmpty()) {
                     if (this.hasResource) {
                        break label364;
                     }

                     this.engineResource = this.engineResourceFactory.build(this.resource, this.isCacheable, this.key, this.resourceListener);
                     this.hasResource = true;
                     var1 = this.cbs.copy();
                     this.incrementPendingCallbacks(var1.size() + 1);
                     var2 = this.key;
                     var3 = this.engineResource;
                     break label365;
                  }
               } catch (Throwable var33) {
                  var10000 = var33;
                  var10001 = false;
                  break label371;
               }

               try {
                  throw new IllegalStateException("Received a resource without any callbacks to notify");
               } catch (Throwable var31) {
                  var10000 = var31;
                  var10001 = false;
                  break label371;
               }
            }

            try {
               throw new IllegalStateException("Already have resource");
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               break label371;
            }
         }

         this.engineJobListener.onEngineJobComplete(this, var2, var3);
         Iterator var34 = var1.iterator();

         while(var34.hasNext()) {
            EngineJob.ResourceCallbackAndExecutor var36 = (EngineJob.ResourceCallbackAndExecutor)var34.next();
            var36.executor.execute(new EngineJob.CallResourceReady(var36.field_53));
         }

         this.decrementPendingCallbacks();
         return;
      }

      while(true) {
         Throwable var35 = var10000;

         try {
            throw var35;
         } catch (Throwable var29) {
            var10000 = var29;
            var10001 = false;
            continue;
         }
      }
   }

   public void onLoadFailed(GlideException param1) {
      // $FF: Couldn't be decompiled
   }

   public void onResourceReady(Resource param1, DataSource param2) {
      // $FF: Couldn't be decompiled
   }

   boolean onlyRetrieveFromCache() {
      return this.onlyRetrieveFromCache;
   }

   void removeCallback(ResourceCallback var1) {
      synchronized(this){}

      Throwable var10000;
      label126: {
         boolean var10001;
         boolean var2;
         label120: {
            label119: {
               try {
                  this.stateVerifier.throwIfRecycled();
                  this.cbs.remove(var1);
                  if (!this.cbs.isEmpty()) {
                     return;
                  }

                  this.cancel();
                  if (!this.hasResource && !this.hasLoadFailed) {
                     break label119;
                  }
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label126;
               }

               var2 = true;
               break label120;
            }

            var2 = false;
         }

         if (!var2) {
            return;
         }

         label108:
         try {
            if (this.pendingCallbacks.get() == 0) {
               this.release();
            }

            return;
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label108;
         }
      }

      Throwable var9 = var10000;
      throw var9;
   }

   public void reschedule(DecodeJob var1) {
      this.getActiveSourceExecutor().execute(var1);
   }

   public void start(DecodeJob var1) {
      synchronized(this){}

      Throwable var10000;
      label133: {
         boolean var10001;
         GlideExecutor var2;
         label126: {
            try {
               this.decodeJob = var1;
               if (var1.willDecodeFromCache()) {
                  var2 = this.diskCacheExecutor;
                  break label126;
               }
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label133;
            }

            try {
               var2 = this.getActiveSourceExecutor();
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label133;
            }
         }

         label117:
         try {
            var2.execute(var1);
            return;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label117;
         }
      }

      Throwable var15 = var10000;
      throw var15;
   }

   private class CallLoadFailed implements Runnable {
      // $FF: renamed from: cb com.bumptech.glide.request.ResourceCallback
      private final ResourceCallback field_149;

      CallLoadFailed(ResourceCallback var2) {
         this.field_149 = var2;
      }

      public void run() {
         // $FF: Couldn't be decompiled
      }
   }

   private class CallResourceReady implements Runnable {
      // $FF: renamed from: cb com.bumptech.glide.request.ResourceCallback
      private final ResourceCallback field_116;

      CallResourceReady(ResourceCallback var2) {
         this.field_116 = var2;
      }

      public void run() {
         // $FF: Couldn't be decompiled
      }
   }

   static class EngineResourceFactory {
      public EngineResource build(Resource var1, boolean var2, Key var3, EngineResource.ResourceListener var4) {
         return new EngineResource(var1, var2, true, var3, var4);
      }
   }

   static final class ResourceCallbackAndExecutor {
      // $FF: renamed from: cb com.bumptech.glide.request.ResourceCallback
      final ResourceCallback field_53;
      final Executor executor;

      ResourceCallbackAndExecutor(ResourceCallback var1, Executor var2) {
         this.field_53 = var1;
         this.executor = var2;
      }

      public boolean equals(Object var1) {
         if (var1 instanceof EngineJob.ResourceCallbackAndExecutor) {
            EngineJob.ResourceCallbackAndExecutor var2 = (EngineJob.ResourceCallbackAndExecutor)var1;
            return this.field_53.equals(var2.field_53);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.field_53.hashCode();
      }
   }

   static final class ResourceCallbacksAndExecutors implements Iterable {
      private final List callbacksAndExecutors;

      ResourceCallbacksAndExecutors() {
         this(new ArrayList(2));
      }

      ResourceCallbacksAndExecutors(List var1) {
         this.callbacksAndExecutors = var1;
      }

      private static EngineJob.ResourceCallbackAndExecutor defaultCallbackAndExecutor(ResourceCallback var0) {
         return new EngineJob.ResourceCallbackAndExecutor(var0, Executors.directExecutor());
      }

      void add(ResourceCallback var1, Executor var2) {
         this.callbacksAndExecutors.add(new EngineJob.ResourceCallbackAndExecutor(var1, var2));
      }

      void clear() {
         this.callbacksAndExecutors.clear();
      }

      boolean contains(ResourceCallback var1) {
         return this.callbacksAndExecutors.contains(defaultCallbackAndExecutor(var1));
      }

      EngineJob.ResourceCallbacksAndExecutors copy() {
         return new EngineJob.ResourceCallbacksAndExecutors(new ArrayList(this.callbacksAndExecutors));
      }

      boolean isEmpty() {
         return this.callbacksAndExecutors.isEmpty();
      }

      public Iterator iterator() {
         return this.callbacksAndExecutors.iterator();
      }

      void remove(ResourceCallback var1) {
         this.callbacksAndExecutors.remove(defaultCallbackAndExecutor(var1));
      }

      int size() {
         return this.callbacksAndExecutors.size();
      }
   }
}
