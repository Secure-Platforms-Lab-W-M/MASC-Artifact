package com.bumptech.glide.load.engine;

import android.os.Build.VERSION;
import android.util.Log;
import androidx.core.util.Pools;
import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.DataRewinder;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.util.LogTime;
import com.bumptech.glide.util.pool.FactoryPools;
import com.bumptech.glide.util.pool.GlideTrace;
import com.bumptech.glide.util.pool.StateVerifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class DecodeJob implements DataFetcherGenerator.FetcherReadyCallback, Runnable, Comparable, FactoryPools.Poolable {
   private static final String TAG = "DecodeJob";
   private DecodeJob.Callback callback;
   private Key currentAttemptingKey;
   private Object currentData;
   private DataSource currentDataSource;
   private DataFetcher currentFetcher;
   private volatile DataFetcherGenerator currentGenerator;
   private Key currentSourceKey;
   private Thread currentThread;
   private final DecodeHelper decodeHelper = new DecodeHelper();
   private final DecodeJob.DeferredEncodeManager deferredEncodeManager = new DecodeJob.DeferredEncodeManager();
   private final DecodeJob.DiskCacheProvider diskCacheProvider;
   private DiskCacheStrategy diskCacheStrategy;
   private GlideContext glideContext;
   private int height;
   private volatile boolean isCallbackNotified;
   private volatile boolean isCancelled;
   private EngineKey loadKey;
   private Object model;
   private boolean onlyRetrieveFromCache;
   private Options options;
   private int order;
   private final Pools.Pool pool;
   private Priority priority;
   private final DecodeJob.ReleaseManager releaseManager = new DecodeJob.ReleaseManager();
   private DecodeJob.RunReason runReason;
   private Key signature;
   private DecodeJob.Stage stage;
   private long startFetchTime;
   private final StateVerifier stateVerifier = StateVerifier.newInstance();
   private final List throwables = new ArrayList();
   private int width;

   DecodeJob(DecodeJob.DiskCacheProvider var1, Pools.Pool var2) {
      this.diskCacheProvider = var1;
      this.pool = var2;
   }

   private Resource decodeFromData(DataFetcher var1, Object var2, DataSource var3) throws GlideException {
      if (var2 == null) {
         var1.cleanup();
         return null;
      } else {
         Resource var8;
         try {
            long var4 = LogTime.getLogTime();
            var8 = this.decodeFromFetcher(var2, var3);
            if (Log.isLoggable("DecodeJob", 2)) {
               StringBuilder var9 = new StringBuilder();
               var9.append("Decoded result ");
               var9.append(var8);
               this.logWithTimeAndKey(var9.toString(), var4);
            }
         } finally {
            var1.cleanup();
         }

         return var8;
      }
   }

   private Resource decodeFromFetcher(Object var1, DataSource var2) throws GlideException {
      return this.runLoadPath(var1, var2, this.decodeHelper.getLoadPath(var1.getClass()));
   }

   private void decodeFromRetrievedData() {
      if (Log.isLoggable("DecodeJob", 2)) {
         long var1 = this.startFetchTime;
         StringBuilder var3 = new StringBuilder();
         var3.append("data: ");
         var3.append(this.currentData);
         var3.append(", cache key: ");
         var3.append(this.currentSourceKey);
         var3.append(", fetcher: ");
         var3.append(this.currentFetcher);
         this.logWithTimeAndKey("Retrieved data", var1, var3.toString());
      }

      Resource var6 = null;

      label19: {
         Resource var4;
         try {
            var4 = this.decodeFromData(this.currentFetcher, this.currentData, this.currentDataSource);
         } catch (GlideException var5) {
            var5.setLoggingDetails(this.currentAttemptingKey, this.currentDataSource);
            this.throwables.add(var5);
            break label19;
         }

         var6 = var4;
      }

      if (var6 != null) {
         this.notifyEncodeAndRelease(var6, this.currentDataSource);
      } else {
         this.runGenerators();
      }
   }

   private DataFetcherGenerator getNextGenerator() {
      int var1 = null.$SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage[this.stage.ordinal()];
      if (var1 != 1) {
         if (var1 != 2) {
            if (var1 != 3) {
               if (var1 == 4) {
                  return null;
               } else {
                  StringBuilder var2 = new StringBuilder();
                  var2.append("Unrecognized stage: ");
                  var2.append(this.stage);
                  throw new IllegalStateException(var2.toString());
               }
            } else {
               return new SourceGenerator(this.decodeHelper, this);
            }
         } else {
            return new DataCacheGenerator(this.decodeHelper, this);
         }
      } else {
         return new ResourceCacheGenerator(this.decodeHelper, this);
      }
   }

   private DecodeJob.Stage getNextStage(DecodeJob.Stage var1) {
      int var2 = null.$SwitchMap$com$bumptech$glide$load$engine$DecodeJob$Stage[var1.ordinal()];
      if (var2 != 1) {
         if (var2 != 2) {
            if (var2 != 3 && var2 != 4) {
               if (var2 == 5) {
                  return this.diskCacheStrategy.decodeCachedResource() ? DecodeJob.Stage.RESOURCE_CACHE : this.getNextStage(DecodeJob.Stage.RESOURCE_CACHE);
               } else {
                  StringBuilder var3 = new StringBuilder();
                  var3.append("Unrecognized stage: ");
                  var3.append(var1);
                  throw new IllegalArgumentException(var3.toString());
               }
            } else {
               return DecodeJob.Stage.FINISHED;
            }
         } else {
            return this.onlyRetrieveFromCache ? DecodeJob.Stage.FINISHED : DecodeJob.Stage.SOURCE;
         }
      } else {
         return this.diskCacheStrategy.decodeCachedData() ? DecodeJob.Stage.DATA_CACHE : this.getNextStage(DecodeJob.Stage.DATA_CACHE);
      }
   }

   private Options getOptionsWithHardwareConfig(DataSource var1) {
      Options var3 = this.options;
      if (VERSION.SDK_INT < 26) {
         return var3;
      } else {
         boolean var2;
         if (var1 != DataSource.RESOURCE_DISK_CACHE && !this.decodeHelper.isScaleOnlyOrNoTransform()) {
            var2 = false;
         } else {
            var2 = true;
         }

         Boolean var4 = (Boolean)var3.get(Downsampler.ALLOW_HARDWARE_CONFIG);
         if (var4 == null || var4 && !var2) {
            Options var5 = new Options();
            var5.putAll(this.options);
            var5.set(Downsampler.ALLOW_HARDWARE_CONFIG, var2);
            return var5;
         } else {
            return var3;
         }
      }
   }

   private int getPriority() {
      return this.priority.ordinal();
   }

   private void logWithTimeAndKey(String var1, long var2) {
      this.logWithTimeAndKey(var1, var2, (String)null);
   }

   private void logWithTimeAndKey(String var1, long var2, String var4) {
      StringBuilder var5 = new StringBuilder();
      var5.append(var1);
      var5.append(" in ");
      var5.append(LogTime.getElapsedMillis(var2));
      var5.append(", load key: ");
      var5.append(this.loadKey);
      if (var4 != null) {
         StringBuilder var6 = new StringBuilder();
         var6.append(", ");
         var6.append(var4);
         var1 = var6.toString();
      } else {
         var1 = "";
      }

      var5.append(var1);
      var5.append(", thread: ");
      var5.append(Thread.currentThread().getName());
      Log.v("DecodeJob", var5.toString());
   }

   private void notifyComplete(Resource var1, DataSource var2) {
      this.setNotifiedOrThrow();
      this.callback.onResourceReady(var1, var2);
   }

   private void notifyEncodeAndRelease(Resource var1, DataSource var2) {
      if (var1 instanceof Initializable) {
         ((Initializable)var1).initialize();
      }

      Object var4 = var1;
      LockedResource var3 = null;
      if (this.deferredEncodeManager.hasResourceToEncode()) {
         var3 = LockedResource.obtain(var1);
         var4 = var3;
      }

      this.notifyComplete((Resource)var4, var2);
      this.stage = DecodeJob.Stage.ENCODE;

      try {
         if (this.deferredEncodeManager.hasResourceToEncode()) {
            this.deferredEncodeManager.encode(this.diskCacheProvider, this.options);
         }
      } finally {
         if (var3 != null) {
            var3.unlock();
         }

      }

      this.onEncodeComplete();
   }

   private void notifyFailed() {
      this.setNotifiedOrThrow();
      GlideException var1 = new GlideException("Failed to load resource", new ArrayList(this.throwables));
      this.callback.onLoadFailed(var1);
      this.onLoadFailed();
   }

   private void onEncodeComplete() {
      if (this.releaseManager.onEncodeComplete()) {
         this.releaseInternal();
      }

   }

   private void onLoadFailed() {
      if (this.releaseManager.onFailed()) {
         this.releaseInternal();
      }

   }

   private void releaseInternal() {
      this.releaseManager.reset();
      this.deferredEncodeManager.clear();
      this.decodeHelper.clear();
      this.isCallbackNotified = false;
      this.glideContext = null;
      this.signature = null;
      this.options = null;
      this.priority = null;
      this.loadKey = null;
      this.callback = null;
      this.stage = null;
      this.currentGenerator = null;
      this.currentThread = null;
      this.currentSourceKey = null;
      this.currentData = null;
      this.currentDataSource = null;
      this.currentFetcher = null;
      this.startFetchTime = 0L;
      this.isCancelled = false;
      this.model = null;
      this.throwables.clear();
      this.pool.release(this);
   }

   private void runGenerators() {
      this.currentThread = Thread.currentThread();
      this.startFetchTime = LogTime.getLogTime();
      boolean var1 = false;

      boolean var2;
      while(true) {
         var2 = var1;
         if (this.isCancelled) {
            break;
         }

         var2 = var1;
         if (this.currentGenerator == null) {
            break;
         }

         boolean var3 = this.currentGenerator.startNext();
         var1 = var3;
         var2 = var3;
         if (var3) {
            break;
         }

         this.stage = this.getNextStage(this.stage);
         this.currentGenerator = this.getNextGenerator();
         if (this.stage == DecodeJob.Stage.SOURCE) {
            this.reschedule();
            return;
         }
      }

      if ((this.stage == DecodeJob.Stage.FINISHED || this.isCancelled) && !var2) {
         this.notifyFailed();
      }

   }

   private Resource runLoadPath(Object var1, DataSource var2, LoadPath var3) throws GlideException {
      Options var4 = this.getOptionsWithHardwareConfig(var2);
      DataRewinder var7 = this.glideContext.getRegistry().getRewinder(var1);

      Resource var8;
      try {
         var8 = var3.load(var7, var4, this.width, this.height, new DecodeJob.DecodeCallback(var2));
      } finally {
         var7.cleanup();
      }

      return var8;
   }

   private void runWrapped() {
      int var1 = null.$SwitchMap$com$bumptech$glide$load$engine$DecodeJob$RunReason[this.runReason.ordinal()];
      if (var1 != 1) {
         if (var1 != 2) {
            if (var1 == 3) {
               this.decodeFromRetrievedData();
            } else {
               StringBuilder var2 = new StringBuilder();
               var2.append("Unrecognized run reason: ");
               var2.append(this.runReason);
               throw new IllegalStateException(var2.toString());
            }
         } else {
            this.runGenerators();
         }
      } else {
         this.stage = this.getNextStage(DecodeJob.Stage.INITIALIZE);
         this.currentGenerator = this.getNextGenerator();
         this.runGenerators();
      }
   }

   private void setNotifiedOrThrow() {
      this.stateVerifier.throwIfRecycled();
      if (this.isCallbackNotified) {
         Throwable var1;
         if (this.throwables.isEmpty()) {
            var1 = null;
         } else {
            List var2 = this.throwables;
            var1 = (Throwable)var2.get(var2.size() - 1);
         }

         throw new IllegalStateException("Already notified", var1);
      } else {
         this.isCallbackNotified = true;
      }
   }

   public void cancel() {
      this.isCancelled = true;
      DataFetcherGenerator var1 = this.currentGenerator;
      if (var1 != null) {
         var1.cancel();
      }

   }

   public int compareTo(DecodeJob var1) {
      int var3 = this.getPriority() - var1.getPriority();
      int var2 = var3;
      if (var3 == 0) {
         var2 = this.order - var1.order;
      }

      return var2;
   }

   public StateVerifier getVerifier() {
      return this.stateVerifier;
   }

   DecodeJob init(GlideContext var1, Object var2, EngineKey var3, Key var4, int var5, int var6, Class var7, Class var8, Priority var9, DiskCacheStrategy var10, Map var11, boolean var12, boolean var13, boolean var14, Options var15, DecodeJob.Callback var16, int var17) {
      this.decodeHelper.init(var1, var2, var4, var5, var6, var10, var7, var8, var9, var15, var11, var12, var13, this.diskCacheProvider);
      this.glideContext = var1;
      this.signature = var4;
      this.priority = var9;
      this.loadKey = var3;
      this.width = var5;
      this.height = var6;
      this.diskCacheStrategy = var10;
      this.onlyRetrieveFromCache = var14;
      this.options = var15;
      this.callback = var16;
      this.order = var17;
      this.runReason = DecodeJob.RunReason.INITIALIZE;
      this.model = var2;
      return this;
   }

   public void onDataFetcherFailed(Key var1, Exception var2, DataFetcher var3, DataSource var4) {
      var3.cleanup();
      GlideException var5 = new GlideException("Fetching data failed", var2);
      var5.setLoggingDetails(var1, var4, var3.getDataClass());
      this.throwables.add(var5);
      if (Thread.currentThread() != this.currentThread) {
         this.runReason = DecodeJob.RunReason.SWITCH_TO_SOURCE_SERVICE;
         this.callback.reschedule(this);
      } else {
         this.runGenerators();
      }
   }

   public void onDataFetcherReady(Key var1, Object var2, DataFetcher var3, DataSource var4, Key var5) {
      this.currentSourceKey = var1;
      this.currentData = var2;
      this.currentFetcher = var3;
      this.currentDataSource = var4;
      this.currentAttemptingKey = var5;
      if (Thread.currentThread() != this.currentThread) {
         this.runReason = DecodeJob.RunReason.DECODE_DATA;
         this.callback.reschedule(this);
      } else {
         GlideTrace.beginSection("DecodeJob.decodeFromRetrievedData");

         try {
            this.decodeFromRetrievedData();
         } finally {
            GlideTrace.endSection();
         }

      }
   }

   Resource onResourceDecoded(DataSource var1, Resource var2) {
      Class var8 = var2.get().getClass();
      Resource var5;
      Transformation var6;
      if (var1 != DataSource.RESOURCE_DISK_CACHE) {
         var6 = this.decodeHelper.getTransformation(var8);
         var5 = var6.transform(this.glideContext, var2, this.width, this.height);
      } else {
         var6 = null;
         var5 = var2;
      }

      if (!var2.equals(var5)) {
         var2.recycle();
      }

      EncodeStrategy var7;
      ResourceEncoder var11;
      if (this.decodeHelper.isResourceEncoderAvailable(var5)) {
         var11 = this.decodeHelper.getResultEncoder(var5);
         var7 = var11.getEncodeStrategy(this.options);
      } else {
         var7 = EncodeStrategy.NONE;
         var11 = null;
      }

      boolean var4 = this.decodeHelper.isSourceKey(this.currentSourceKey);
      if (this.diskCacheStrategy.isResourceCacheable(var4 ^ true, var1, var7)) {
         if (var11 != null) {
            int var3 = null.$SwitchMap$com$bumptech$glide$load$EncodeStrategy[var7.ordinal()];
            Object var9;
            if (var3 != 1) {
               if (var3 != 2) {
                  StringBuilder var10 = new StringBuilder();
                  var10.append("Unknown strategy: ");
                  var10.append(var7);
                  throw new IllegalArgumentException(var10.toString());
               }

               var9 = new ResourceCacheKey(this.decodeHelper.getArrayPool(), this.currentSourceKey, this.signature, this.width, this.height, var6, var8, this.options);
            } else {
               var9 = new DataCacheKey(this.currentSourceKey, this.signature);
            }

            LockedResource var12 = LockedResource.obtain(var5);
            this.deferredEncodeManager.init((Key)var9, var11, var12);
            return var12;
         } else {
            throw new Registry.NoResultEncoderAvailableException(var5.get().getClass());
         }
      } else {
         return var5;
      }
   }

   void release(boolean var1) {
      if (this.releaseManager.release(var1)) {
         this.releaseInternal();
      }

   }

   public void reschedule() {
      this.runReason = DecodeJob.RunReason.SWITCH_TO_SOURCE_SERVICE;
      this.callback.reschedule(this);
   }

   public void run() {
      // $FF: Couldn't be decompiled
   }

   boolean willDecodeFromCache() {
      DecodeJob.Stage var1 = this.getNextStage(DecodeJob.Stage.INITIALIZE);
      return var1 == DecodeJob.Stage.RESOURCE_CACHE || var1 == DecodeJob.Stage.DATA_CACHE;
   }

   interface Callback {
      void onLoadFailed(GlideException var1);

      void onResourceReady(Resource var1, DataSource var2);

      void reschedule(DecodeJob var1);
   }

   private final class DecodeCallback implements DecodePath.DecodeCallback {
      private final DataSource dataSource;

      DecodeCallback(DataSource var2) {
         this.dataSource = var2;
      }

      public Resource onResourceDecoded(Resource var1) {
         return DecodeJob.this.onResourceDecoded(this.dataSource, var1);
      }
   }

   private static class DeferredEncodeManager {
      private ResourceEncoder encoder;
      private Key key;
      private LockedResource toEncode;

      DeferredEncodeManager() {
      }

      void clear() {
         this.key = null;
         this.encoder = null;
         this.toEncode = null;
      }

      void encode(DecodeJob.DiskCacheProvider var1, Options var2) {
         GlideTrace.beginSection("DecodeJob.encode");

         try {
            var1.getDiskCache().put(this.key, new DataCacheWriter(this.encoder, this.toEncode, var2));
         } finally {
            this.toEncode.unlock();
            GlideTrace.endSection();
         }

      }

      boolean hasResourceToEncode() {
         return this.toEncode != null;
      }

      void init(Key var1, ResourceEncoder var2, LockedResource var3) {
         this.key = var1;
         this.encoder = var2;
         this.toEncode = var3;
      }
   }

   interface DiskCacheProvider {
      DiskCache getDiskCache();
   }

   private static class ReleaseManager {
      private boolean isEncodeComplete;
      private boolean isFailed;
      private boolean isReleased;

      ReleaseManager() {
      }

      private boolean isComplete(boolean var1) {
         return (this.isFailed || var1 || this.isEncodeComplete) && this.isReleased;
      }

      boolean onEncodeComplete() {
         synchronized(this){}

         boolean var1;
         try {
            this.isEncodeComplete = true;
            var1 = this.isComplete(false);
         } finally {
            ;
         }

         return var1;
      }

      boolean onFailed() {
         synchronized(this){}

         boolean var1;
         try {
            this.isFailed = true;
            var1 = this.isComplete(false);
         } finally {
            ;
         }

         return var1;
      }

      boolean release(boolean var1) {
         synchronized(this){}

         try {
            this.isReleased = true;
            var1 = this.isComplete(var1);
         } finally {
            ;
         }

         return var1;
      }

      void reset() {
         synchronized(this){}

         try {
            this.isEncodeComplete = false;
            this.isReleased = false;
            this.isFailed = false;
         } finally {
            ;
         }

      }
   }

   private static enum RunReason {
      DECODE_DATA,
      INITIALIZE,
      SWITCH_TO_SOURCE_SERVICE;

      static {
         DecodeJob.RunReason var0 = new DecodeJob.RunReason("DECODE_DATA", 2);
         DECODE_DATA = var0;
      }
   }

   private static enum Stage {
      DATA_CACHE,
      ENCODE,
      FINISHED,
      INITIALIZE,
      RESOURCE_CACHE,
      SOURCE;

      static {
         DecodeJob.Stage var0 = new DecodeJob.Stage("FINISHED", 5);
         FINISHED = var0;
      }
   }
}
