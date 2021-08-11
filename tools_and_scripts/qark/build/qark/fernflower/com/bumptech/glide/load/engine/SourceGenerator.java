package com.bumptech.glide.load.engine;

import android.util.Log;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.util.LogTime;
import java.util.Collections;
import java.util.List;

class SourceGenerator implements DataFetcherGenerator, DataFetcherGenerator.FetcherReadyCallback {
   private static final String TAG = "SourceGenerator";
   // $FF: renamed from: cb com.bumptech.glide.load.engine.DataFetcherGenerator$FetcherReadyCallback
   private final DataFetcherGenerator.FetcherReadyCallback field_112;
   private Object dataToCache;
   private final DecodeHelper helper;
   private volatile ModelLoader.LoadData loadData;
   private int loadDataListIndex;
   private DataCacheKey originalKey;
   private DataCacheGenerator sourceCacheGenerator;

   SourceGenerator(DecodeHelper var1, DataFetcherGenerator.FetcherReadyCallback var2) {
      this.helper = var1;
      this.field_112 = var2;
   }

   private void cacheData(Object var1) {
      long var2 = LogTime.getLogTime();

      try {
         Encoder var4 = this.helper.getSourceEncoder(var1);
         DataCacheWriter var5 = new DataCacheWriter(var4, var1, this.helper.getOptions());
         this.originalKey = new DataCacheKey(this.loadData.sourceKey, this.helper.getSignature());
         this.helper.getDiskCache().put(this.originalKey, var5);
         if (Log.isLoggable("SourceGenerator", 2)) {
            StringBuilder var8 = new StringBuilder();
            var8.append("Finished encoding source to cache, key: ");
            var8.append(this.originalKey);
            var8.append(", data: ");
            var8.append(var1);
            var8.append(", encoder: ");
            var8.append(var4);
            var8.append(", duration: ");
            var8.append(LogTime.getElapsedMillis(var2));
            Log.v("SourceGenerator", var8.toString());
         }
      } finally {
         this.loadData.fetcher.cleanup();
      }

      this.sourceCacheGenerator = new DataCacheGenerator(Collections.singletonList(this.loadData.sourceKey), this.helper, this);
   }

   private boolean hasNextModelLoader() {
      return this.loadDataListIndex < this.helper.getLoadData().size();
   }

   private void startNextLoad(final ModelLoader.LoadData var1) {
      this.loadData.fetcher.loadData(this.helper.getPriority(), new DataFetcher.DataCallback() {
         public void onDataReady(Object var1x) {
            if (SourceGenerator.this.isCurrentRequest(var1)) {
               SourceGenerator.this.onDataReadyInternal(var1, var1x);
            }

         }

         public void onLoadFailed(Exception var1x) {
            if (SourceGenerator.this.isCurrentRequest(var1)) {
               SourceGenerator.this.onLoadFailedInternal(var1, var1x);
            }

         }
      });
   }

   public void cancel() {
      ModelLoader.LoadData var1 = this.loadData;
      if (var1 != null) {
         var1.fetcher.cancel();
      }

   }

   boolean isCurrentRequest(ModelLoader.LoadData var1) {
      ModelLoader.LoadData var2 = this.loadData;
      return var2 != null && var2 == var1;
   }

   public void onDataFetcherFailed(Key var1, Exception var2, DataFetcher var3, DataSource var4) {
      this.field_112.onDataFetcherFailed(var1, var2, var3, this.loadData.fetcher.getDataSource());
   }

   public void onDataFetcherReady(Key var1, Object var2, DataFetcher var3, DataSource var4, Key var5) {
      this.field_112.onDataFetcherReady(var1, var2, var3, this.loadData.fetcher.getDataSource(), var1);
   }

   void onDataReadyInternal(ModelLoader.LoadData var1, Object var2) {
      DiskCacheStrategy var3 = this.helper.getDiskCacheStrategy();
      if (var2 != null && var3.isDataCacheable(var1.fetcher.getDataSource())) {
         this.dataToCache = var2;
         this.field_112.reschedule();
      } else {
         this.field_112.onDataFetcherReady(var1.sourceKey, var2, var1.fetcher, var1.fetcher.getDataSource(), this.originalKey);
      }
   }

   void onLoadFailedInternal(ModelLoader.LoadData var1, Exception var2) {
      this.field_112.onDataFetcherFailed(this.originalKey, var2, var1.fetcher, var1.fetcher.getDataSource());
   }

   public void reschedule() {
      throw new UnsupportedOperationException();
   }

   public boolean startNext() {
      if (this.dataToCache != null) {
         Object var3 = this.dataToCache;
         this.dataToCache = null;
         this.cacheData(var3);
      }

      DataCacheGenerator var4 = this.sourceCacheGenerator;
      if (var4 != null && var4.startNext()) {
         return true;
      } else {
         this.sourceCacheGenerator = null;
         this.loadData = null;
         boolean var2 = false;

         while(!var2 && this.hasNextModelLoader()) {
            List var5 = this.helper.getLoadData();
            int var1 = this.loadDataListIndex++;
            this.loadData = (ModelLoader.LoadData)var5.get(var1);
            if (this.loadData != null && (this.helper.getDiskCacheStrategy().isDataCacheable(this.loadData.fetcher.getDataSource()) || this.helper.hasLoadPath(this.loadData.fetcher.getDataClass()))) {
               var2 = true;
               this.startNextLoad(this.loadData);
            }
         }

         return var2;
      }
   }
}
