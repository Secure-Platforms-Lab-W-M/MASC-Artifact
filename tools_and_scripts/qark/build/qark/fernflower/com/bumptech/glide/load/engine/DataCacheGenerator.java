package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import java.io.File;
import java.util.List;

class DataCacheGenerator implements DataFetcherGenerator, DataFetcher.DataCallback {
   private File cacheFile;
   private final List cacheKeys;
   // $FF: renamed from: cb com.bumptech.glide.load.engine.DataFetcherGenerator$FetcherReadyCallback
   private final DataFetcherGenerator.FetcherReadyCallback field_130;
   private final DecodeHelper helper;
   private volatile ModelLoader.LoadData loadData;
   private int modelLoaderIndex;
   private List modelLoaders;
   private int sourceIdIndex;
   private Key sourceKey;

   DataCacheGenerator(DecodeHelper var1, DataFetcherGenerator.FetcherReadyCallback var2) {
      this(var1.getCacheKeys(), var1, var2);
   }

   DataCacheGenerator(List var1, DecodeHelper var2, DataFetcherGenerator.FetcherReadyCallback var3) {
      this.sourceIdIndex = -1;
      this.cacheKeys = var1;
      this.helper = var2;
      this.field_130 = var3;
   }

   private boolean hasNextModelLoader() {
      return this.modelLoaderIndex < this.modelLoaders.size();
   }

   public void cancel() {
      ModelLoader.LoadData var1 = this.loadData;
      if (var1 != null) {
         var1.fetcher.cancel();
      }

   }

   public void onDataReady(Object var1) {
      this.field_130.onDataFetcherReady(this.sourceKey, var1, this.loadData.fetcher, DataSource.DATA_DISK_CACHE, this.sourceKey);
   }

   public void onLoadFailed(Exception var1) {
      this.field_130.onDataFetcherFailed(this.sourceKey, var1, this.loadData.fetcher, DataSource.DATA_DISK_CACHE);
   }

   public boolean startNext() {
      int var1;
      while(this.modelLoaders == null || !this.hasNextModelLoader()) {
         var1 = this.sourceIdIndex + 1;
         this.sourceIdIndex = var1;
         if (var1 >= this.cacheKeys.size()) {
            return false;
         }

         Key var4 = (Key)this.cacheKeys.get(this.sourceIdIndex);
         DataCacheKey var5 = new DataCacheKey(var4, this.helper.getSignature());
         File var7 = this.helper.getDiskCache().get(var5);
         this.cacheFile = var7;
         if (var7 != null) {
            this.sourceKey = var4;
            this.modelLoaders = this.helper.getModelLoaders(var7);
            this.modelLoaderIndex = 0;
         }
      }

      this.loadData = null;

      boolean var2;
      boolean var3;
      for(var2 = false; !var2 && this.hasNextModelLoader(); var2 = var3) {
         List var6 = this.modelLoaders;
         var1 = this.modelLoaderIndex++;
         this.loadData = ((ModelLoader)var6.get(var1)).buildLoadData(this.cacheFile, this.helper.getWidth(), this.helper.getHeight(), this.helper.getOptions());
         var3 = var2;
         if (this.loadData != null) {
            var3 = var2;
            if (this.helper.hasLoadPath(this.loadData.fetcher.getDataClass())) {
               var3 = true;
               this.loadData.fetcher.loadData(this.helper.getPriority(), this);
            }
         }
      }

      return var2;
   }
}
