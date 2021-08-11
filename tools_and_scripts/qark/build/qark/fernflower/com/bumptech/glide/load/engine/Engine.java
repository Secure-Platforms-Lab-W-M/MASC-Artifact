package com.bumptech.glide.load.engine;

import android.util.Log;
import androidx.core.util.Pools;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskCacheAdapter;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.request.ResourceCallback;
import com.bumptech.glide.util.Executors;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.pool.FactoryPools;
import java.util.Map;
import java.util.concurrent.Executor;

public class Engine implements EngineJobListener, MemoryCache.ResourceRemovedListener, EngineResource.ResourceListener {
   private static final int JOB_POOL_SIZE = 150;
   private static final String TAG = "Engine";
   private static final boolean VERBOSE_IS_LOGGABLE = Log.isLoggable("Engine", 2);
   private final ActiveResources activeResources;
   private final MemoryCache cache;
   private final Engine.DecodeJobFactory decodeJobFactory;
   private final Engine.LazyDiskCacheProvider diskCacheProvider;
   private final Engine.EngineJobFactory engineJobFactory;
   private final Jobs jobs;
   private final EngineKeyFactory keyFactory;
   private final ResourceRecycler resourceRecycler;

   Engine(MemoryCache var1, DiskCache.Factory var2, GlideExecutor var3, GlideExecutor var4, GlideExecutor var5, GlideExecutor var6, Jobs var7, EngineKeyFactory var8, ActiveResources var9, Engine.EngineJobFactory var10, Engine.DecodeJobFactory var11, ResourceRecycler var12, boolean var13) {
      this.cache = var1;
      this.diskCacheProvider = new Engine.LazyDiskCacheProvider(var2);
      if (var9 == null) {
         var9 = new ActiveResources(var13);
      }

      this.activeResources = var9;
      var9.setListener(this);
      if (var8 == null) {
         var8 = new EngineKeyFactory();
      }

      this.keyFactory = var8;
      if (var7 == null) {
         var7 = new Jobs();
      }

      this.jobs = var7;
      if (var10 == null) {
         var10 = new Engine.EngineJobFactory(var3, var4, var5, var6, this, this);
      }

      this.engineJobFactory = var10;
      if (var11 == null) {
         var11 = new Engine.DecodeJobFactory(this.diskCacheProvider);
      }

      this.decodeJobFactory = var11;
      if (var12 == null) {
         var12 = new ResourceRecycler();
      }

      this.resourceRecycler = var12;
      var1.setResourceRemovedListener(this);
   }

   public Engine(MemoryCache var1, DiskCache.Factory var2, GlideExecutor var3, GlideExecutor var4, GlideExecutor var5, GlideExecutor var6, boolean var7) {
      this(var1, var2, var3, var4, var5, var6, (Jobs)null, (EngineKeyFactory)null, (ActiveResources)null, (Engine.EngineJobFactory)null, (Engine.DecodeJobFactory)null, (ResourceRecycler)null, var7);
   }

   private EngineResource getEngineResourceFromCache(Key var1) {
      Resource var2 = this.cache.remove(var1);
      if (var2 == null) {
         return null;
      } else {
         return var2 instanceof EngineResource ? (EngineResource)var2 : new EngineResource(var2, true, true, var1, this);
      }
   }

   private EngineResource loadFromActiveResources(Key var1) {
      EngineResource var2 = this.activeResources.get(var1);
      if (var2 != null) {
         var2.acquire();
      }

      return var2;
   }

   private EngineResource loadFromCache(Key var1) {
      EngineResource var2 = this.getEngineResourceFromCache(var1);
      if (var2 != null) {
         var2.acquire();
         this.activeResources.activate(var1, var2);
      }

      return var2;
   }

   private EngineResource loadFromMemory(EngineKey var1, boolean var2, long var3) {
      if (!var2) {
         return null;
      } else {
         EngineResource var5 = this.loadFromActiveResources(var1);
         if (var5 != null) {
            if (VERBOSE_IS_LOGGABLE) {
               logWithTimeAndKey("Loaded resource from active resources", var3, var1);
            }

            return var5;
         } else {
            var5 = this.loadFromCache(var1);
            if (var5 != null) {
               if (VERBOSE_IS_LOGGABLE) {
                  logWithTimeAndKey("Loaded resource from cache", var3, var1);
               }

               return var5;
            } else {
               return null;
            }
         }
      }
   }

   private static void logWithTimeAndKey(String var0, long var1, Key var3) {
      StringBuilder var4 = new StringBuilder();
      var4.append(var0);
      var4.append(" in ");
      var4.append(LogTime.getElapsedMillis(var1));
      var4.append("ms, key: ");
      var4.append(var3);
      Log.v("Engine", var4.toString());
   }

   private Engine.LoadStatus waitForExistingOrStartNewJob(GlideContext var1, Object var2, Key var3, int var4, int var5, Class var6, Class var7, Priority var8, DiskCacheStrategy var9, Map var10, boolean var11, boolean var12, Options var13, boolean var14, boolean var15, boolean var16, boolean var17, ResourceCallback var18, Executor var19, EngineKey var20, long var21) {
      EngineJob var23 = this.jobs.get(var20, var17);
      if (var23 != null) {
         var23.addCallback(var18, var19);
         if (VERBOSE_IS_LOGGABLE) {
            logWithTimeAndKey("Added to existing load", var21, var20);
         }

         return new Engine.LoadStatus(var18, var23);
      } else {
         var23 = this.engineJobFactory.build(var20, var14, var15, var16, var17);
         DecodeJob var24 = this.decodeJobFactory.build(var1, var2, var20, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var17, var13, var23);
         this.jobs.put(var20, var23);
         var23.addCallback(var18, var19);
         var23.start(var24);
         if (VERBOSE_IS_LOGGABLE) {
            logWithTimeAndKey("Started new load", var21, var20);
         }

         return new Engine.LoadStatus(var18, var23);
      }
   }

   public void clearDiskCache() {
      this.diskCacheProvider.getDiskCache().clear();
   }

   public Engine.LoadStatus load(GlideContext param1, Object param2, Key param3, int param4, int param5, Class param6, Class param7, Priority param8, DiskCacheStrategy param9, Map param10, boolean param11, boolean param12, Options param13, boolean param14, boolean param15, boolean param16, boolean param17, ResourceCallback param18, Executor param19) {
      // $FF: Couldn't be decompiled
   }

   public void onEngineJobCancelled(EngineJob var1, Key var2) {
      synchronized(this){}

      try {
         this.jobs.removeIfCurrent(var2, var1);
      } finally {
         ;
      }

   }

   public void onEngineJobComplete(EngineJob var1, Key var2, EngineResource var3) {
      Throwable var10000;
      label64: {
         synchronized(this){}
         boolean var10001;
         if (var3 != null) {
            try {
               if (var3.isMemoryCacheable()) {
                  this.activeResources.activate(var2, var3);
               }
            } catch (Throwable var9) {
               var10000 = var9;
               var10001 = false;
               break label64;
            }
         }

         label60:
         try {
            this.jobs.removeIfCurrent(var2, var1);
            return;
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label60;
         }
      }

      Throwable var10 = var10000;
      throw var10;
   }

   public void onResourceReleased(Key var1, EngineResource var2) {
      this.activeResources.deactivate(var1);
      if (var2.isMemoryCacheable()) {
         this.cache.put(var1, var2);
      } else {
         this.resourceRecycler.recycle(var2, false);
      }
   }

   public void onResourceRemoved(Resource var1) {
      this.resourceRecycler.recycle(var1, true);
   }

   public void release(Resource var1) {
      if (var1 instanceof EngineResource) {
         ((EngineResource)var1).release();
      } else {
         throw new IllegalArgumentException("Cannot release anything but an EngineResource");
      }
   }

   public void shutdown() {
      this.engineJobFactory.shutdown();
      this.diskCacheProvider.clearDiskCacheIfCreated();
      this.activeResources.shutdown();
   }

   static class DecodeJobFactory {
      private int creationOrder;
      final DecodeJob.DiskCacheProvider diskCacheProvider;
      final Pools.Pool pool = FactoryPools.threadSafe(150, new FactoryPools.Factory() {
         public DecodeJob create() {
            return new DecodeJob(DecodeJobFactory.this.diskCacheProvider, DecodeJobFactory.this.pool);
         }
      });

      DecodeJobFactory(DecodeJob.DiskCacheProvider var1) {
         this.diskCacheProvider = var1;
      }

      DecodeJob build(GlideContext var1, Object var2, EngineKey var3, Key var4, int var5, int var6, Class var7, Class var8, Priority var9, DiskCacheStrategy var10, Map var11, boolean var12, boolean var13, boolean var14, Options var15, DecodeJob.Callback var16) {
         DecodeJob var18 = (DecodeJob)Preconditions.checkNotNull((DecodeJob)this.pool.acquire());
         int var17 = this.creationOrder++;
         return var18.init(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10, var11, var12, var13, var14, var15, var16, var17);
      }
   }

   static class EngineJobFactory {
      final GlideExecutor animationExecutor;
      final GlideExecutor diskCacheExecutor;
      final EngineJobListener engineJobListener;
      final Pools.Pool pool = FactoryPools.threadSafe(150, new FactoryPools.Factory() {
         public EngineJob create() {
            return new EngineJob(EngineJobFactory.this.diskCacheExecutor, EngineJobFactory.this.sourceExecutor, EngineJobFactory.this.sourceUnlimitedExecutor, EngineJobFactory.this.animationExecutor, EngineJobFactory.this.engineJobListener, EngineJobFactory.this.resourceListener, EngineJobFactory.this.pool);
         }
      });
      final EngineResource.ResourceListener resourceListener;
      final GlideExecutor sourceExecutor;
      final GlideExecutor sourceUnlimitedExecutor;

      EngineJobFactory(GlideExecutor var1, GlideExecutor var2, GlideExecutor var3, GlideExecutor var4, EngineJobListener var5, EngineResource.ResourceListener var6) {
         this.diskCacheExecutor = var1;
         this.sourceExecutor = var2;
         this.sourceUnlimitedExecutor = var3;
         this.animationExecutor = var4;
         this.engineJobListener = var5;
         this.resourceListener = var6;
      }

      EngineJob build(Key var1, boolean var2, boolean var3, boolean var4, boolean var5) {
         return ((EngineJob)Preconditions.checkNotNull((EngineJob)this.pool.acquire())).init(var1, var2, var3, var4, var5);
      }

      void shutdown() {
         Executors.shutdownAndAwaitTermination(this.diskCacheExecutor);
         Executors.shutdownAndAwaitTermination(this.sourceExecutor);
         Executors.shutdownAndAwaitTermination(this.sourceUnlimitedExecutor);
         Executors.shutdownAndAwaitTermination(this.animationExecutor);
      }
   }

   private static class LazyDiskCacheProvider implements DecodeJob.DiskCacheProvider {
      private volatile DiskCache diskCache;
      private final DiskCache.Factory factory;

      LazyDiskCacheProvider(DiskCache.Factory var1) {
         this.factory = var1;
      }

      void clearDiskCacheIfCreated() {
         synchronized(this){}

         Throwable var10000;
         label78: {
            DiskCache var1;
            boolean var10001;
            try {
               var1 = this.diskCache;
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break label78;
            }

            if (var1 == null) {
               return;
            }

            try {
               this.diskCache.clear();
            } catch (Throwable var6) {
               var10000 = var6;
               var10001 = false;
               break label78;
            }

            return;
         }

         Throwable var8 = var10000;
         throw var8;
      }

      public DiskCache getDiskCache() {
         if (this.diskCache == null) {
            synchronized(this){}

            Throwable var10000;
            boolean var10001;
            label226: {
               try {
                  if (this.diskCache == null) {
                     this.diskCache = this.factory.build();
                  }
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label226;
               }

               try {
                  if (this.diskCache == null) {
                     this.diskCache = new DiskCacheAdapter();
                  }
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label226;
               }

               label214:
               try {
                  return this.diskCache;
               } catch (Throwable var19) {
                  var10000 = var19;
                  var10001 = false;
                  break label214;
               }
            }

            while(true) {
               Throwable var1 = var10000;

               try {
                  throw var1;
               } catch (Throwable var18) {
                  var10000 = var18;
                  var10001 = false;
                  continue;
               }
            }
         } else {
            return this.diskCache;
         }
      }
   }

   public class LoadStatus {
      // $FF: renamed from: cb com.bumptech.glide.request.ResourceCallback
      private final ResourceCallback field_233;
      private final EngineJob engineJob;

      LoadStatus(ResourceCallback var2, EngineJob var3) {
         this.field_233 = var2;
         this.engineJob = var3;
      }

      public void cancel() {
         // $FF: Couldn't be decompiled
      }
   }
}
