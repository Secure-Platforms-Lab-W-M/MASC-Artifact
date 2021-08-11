package com.bumptech.glide;

import android.content.Context;
import androidx.collection.ArrayMap;
import androidx.core.os.BuildCompat;
import com.bumptech.glide.load.engine.Engine;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPoolAdapter;
import com.bumptech.glide.load.engine.bitmap_recycle.LruArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemoryCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.engine.executor.GlideExecutor;
import com.bumptech.glide.manager.ConnectivityMonitorFactory;
import com.bumptech.glide.manager.DefaultConnectivityMonitorFactory;
import com.bumptech.glide.manager.RequestManagerRetriever;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public final class GlideBuilder {
   private GlideExecutor animationExecutor;
   private ArrayPool arrayPool;
   private BitmapPool bitmapPool;
   private ConnectivityMonitorFactory connectivityMonitorFactory;
   private List defaultRequestListeners;
   private Glide.RequestOptionsFactory defaultRequestOptionsFactory = new Glide.RequestOptionsFactory() {
      public RequestOptions build() {
         return new RequestOptions();
      }
   };
   private final Map defaultTransitionOptions = new ArrayMap();
   private GlideExecutor diskCacheExecutor;
   private DiskCache.Factory diskCacheFactory;
   private Engine engine;
   private boolean isActiveResourceRetentionAllowed;
   private boolean isImageDecoderEnabledForBitmaps;
   private boolean isLoggingRequestOriginsEnabled;
   private int logLevel = 4;
   private MemoryCache memoryCache;
   private MemorySizeCalculator memorySizeCalculator;
   private RequestManagerRetriever.RequestManagerFactory requestManagerFactory;
   private GlideExecutor sourceExecutor;

   public GlideBuilder addGlobalRequestListener(RequestListener var1) {
      if (this.defaultRequestListeners == null) {
         this.defaultRequestListeners = new ArrayList();
      }

      this.defaultRequestListeners.add(var1);
      return this;
   }

   Glide build(Context var1) {
      if (this.sourceExecutor == null) {
         this.sourceExecutor = GlideExecutor.newSourceExecutor();
      }

      if (this.diskCacheExecutor == null) {
         this.diskCacheExecutor = GlideExecutor.newDiskCacheExecutor();
      }

      if (this.animationExecutor == null) {
         this.animationExecutor = GlideExecutor.newAnimationExecutor();
      }

      if (this.memorySizeCalculator == null) {
         this.memorySizeCalculator = (new MemorySizeCalculator.Builder(var1)).build();
      }

      if (this.connectivityMonitorFactory == null) {
         this.connectivityMonitorFactory = new DefaultConnectivityMonitorFactory();
      }

      if (this.bitmapPool == null) {
         int var2 = this.memorySizeCalculator.getBitmapPoolSize();
         if (var2 > 0) {
            this.bitmapPool = new LruBitmapPool((long)var2);
         } else {
            this.bitmapPool = new BitmapPoolAdapter();
         }
      }

      if (this.arrayPool == null) {
         this.arrayPool = new LruArrayPool(this.memorySizeCalculator.getArrayPoolSizeInBytes());
      }

      if (this.memoryCache == null) {
         this.memoryCache = new LruResourceCache((long)this.memorySizeCalculator.getMemoryCacheSize());
      }

      if (this.diskCacheFactory == null) {
         this.diskCacheFactory = new InternalCacheDiskCacheFactory(var1);
      }

      if (this.engine == null) {
         this.engine = new Engine(this.memoryCache, this.diskCacheFactory, this.diskCacheExecutor, this.sourceExecutor, GlideExecutor.newUnlimitedSourceExecutor(), this.animationExecutor, this.isActiveResourceRetentionAllowed);
      }

      List var3 = this.defaultRequestListeners;
      if (var3 == null) {
         this.defaultRequestListeners = Collections.emptyList();
      } else {
         this.defaultRequestListeners = Collections.unmodifiableList(var3);
      }

      RequestManagerRetriever var4 = new RequestManagerRetriever(this.requestManagerFactory);
      return new Glide(var1, this.engine, this.memoryCache, this.bitmapPool, this.arrayPool, var4, this.connectivityMonitorFactory, this.logLevel, this.defaultRequestOptionsFactory, this.defaultTransitionOptions, this.defaultRequestListeners, this.isLoggingRequestOriginsEnabled, this.isImageDecoderEnabledForBitmaps);
   }

   public GlideBuilder setAnimationExecutor(GlideExecutor var1) {
      this.animationExecutor = var1;
      return this;
   }

   public GlideBuilder setArrayPool(ArrayPool var1) {
      this.arrayPool = var1;
      return this;
   }

   public GlideBuilder setBitmapPool(BitmapPool var1) {
      this.bitmapPool = var1;
      return this;
   }

   public GlideBuilder setConnectivityMonitorFactory(ConnectivityMonitorFactory var1) {
      this.connectivityMonitorFactory = var1;
      return this;
   }

   public GlideBuilder setDefaultRequestOptions(Glide.RequestOptionsFactory var1) {
      this.defaultRequestOptionsFactory = (Glide.RequestOptionsFactory)Preconditions.checkNotNull(var1);
      return this;
   }

   public GlideBuilder setDefaultRequestOptions(final RequestOptions var1) {
      return this.setDefaultRequestOptions(new Glide.RequestOptionsFactory() {
         public RequestOptions build() {
            RequestOptions var1x = var1;
            return var1x != null ? var1x : new RequestOptions();
         }
      });
   }

   public GlideBuilder setDefaultTransitionOptions(Class var1, TransitionOptions var2) {
      this.defaultTransitionOptions.put(var1, var2);
      return this;
   }

   public GlideBuilder setDiskCache(DiskCache.Factory var1) {
      this.diskCacheFactory = var1;
      return this;
   }

   public GlideBuilder setDiskCacheExecutor(GlideExecutor var1) {
      this.diskCacheExecutor = var1;
      return this;
   }

   GlideBuilder setEngine(Engine var1) {
      this.engine = var1;
      return this;
   }

   public GlideBuilder setImageDecoderEnabledForBitmaps(boolean var1) {
      if (!BuildCompat.isAtLeastQ()) {
         return this;
      } else {
         this.isImageDecoderEnabledForBitmaps = var1;
         return this;
      }
   }

   public GlideBuilder setIsActiveResourceRetentionAllowed(boolean var1) {
      this.isActiveResourceRetentionAllowed = var1;
      return this;
   }

   public GlideBuilder setLogLevel(int var1) {
      if (var1 >= 2 && var1 <= 6) {
         this.logLevel = var1;
         return this;
      } else {
         throw new IllegalArgumentException("Log level must be one of Log.VERBOSE, Log.DEBUG, Log.INFO, Log.WARN, or Log.ERROR");
      }
   }

   public GlideBuilder setLogRequestOrigins(boolean var1) {
      this.isLoggingRequestOriginsEnabled = var1;
      return this;
   }

   public GlideBuilder setMemoryCache(MemoryCache var1) {
      this.memoryCache = var1;
      return this;
   }

   public GlideBuilder setMemorySizeCalculator(MemorySizeCalculator.Builder var1) {
      return this.setMemorySizeCalculator(var1.build());
   }

   public GlideBuilder setMemorySizeCalculator(MemorySizeCalculator var1) {
      this.memorySizeCalculator = var1;
      return this;
   }

   void setRequestManagerFactory(RequestManagerRetriever.RequestManagerFactory var1) {
      this.requestManagerFactory = var1;
   }

   @Deprecated
   public GlideBuilder setResizeExecutor(GlideExecutor var1) {
      return this.setSourceExecutor(var1);
   }

   public GlideBuilder setSourceExecutor(GlideExecutor var1) {
      this.sourceExecutor = var1;
      return this;
   }
}
