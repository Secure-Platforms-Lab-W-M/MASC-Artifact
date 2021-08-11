package com.bumptech.glide.load.engine;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import java.io.File;
import java.util.List;

class ResourceCacheGenerator implements DataFetcherGenerator, DataFetcher.DataCallback {
   private File cacheFile;
   // $FF: renamed from: cb com.bumptech.glide.load.engine.DataFetcherGenerator$FetcherReadyCallback
   private final DataFetcherGenerator.FetcherReadyCallback field_152;
   private ResourceCacheKey currentKey;
   private final DecodeHelper helper;
   private volatile ModelLoader.LoadData loadData;
   private int modelLoaderIndex;
   private List modelLoaders;
   private int resourceClassIndex = -1;
   private int sourceIdIndex;
   private Key sourceKey;

   ResourceCacheGenerator(DecodeHelper var1, DataFetcherGenerator.FetcherReadyCallback var2) {
      this.helper = var1;
      this.field_152 = var2;
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
      this.field_152.onDataFetcherReady(this.sourceKey, var1, this.loadData.fetcher, DataSource.RESOURCE_DISK_CACHE, this.currentKey);
   }

   public void onLoadFailed(Exception var1) {
      this.field_152.onDataFetcherFailed(this.currentKey, var1, this.loadData.fetcher, DataSource.RESOURCE_DISK_CACHE);
   }

   public boolean startNext() {
      List var4 = this.helper.getCacheKeys();
      if (var4.isEmpty()) {
         return false;
      } else {
         List var5 = this.helper.getRegisteredResourceClasses();
         if (var5.isEmpty()) {
            if (File.class.equals(this.helper.getTranscodeClass())) {
               return false;
            } else {
               StringBuilder var9 = new StringBuilder();
               var9.append("Failed to find any load path from ");
               var9.append(this.helper.getModelClass());
               var9.append(" to ");
               var9.append(this.helper.getTranscodeClass());
               throw new IllegalStateException(var9.toString());
            }
         } else {
            int var1;
            while(this.modelLoaders == null || !this.hasNextModelLoader()) {
               var1 = this.resourceClassIndex + 1;
               this.resourceClassIndex = var1;
               if (var1 >= var5.size()) {
                  var1 = this.sourceIdIndex + 1;
                  this.sourceIdIndex = var1;
                  if (var1 >= var4.size()) {
                     return false;
                  }

                  this.resourceClassIndex = 0;
               }

               Key var6 = (Key)var4.get(this.sourceIdIndex);
               Class var7 = (Class)var5.get(this.resourceClassIndex);
               Transformation var8 = this.helper.getTransformation(var7);
               this.currentKey = new ResourceCacheKey(this.helper.getArrayPool(), var6, this.helper.getSignature(), this.helper.getWidth(), this.helper.getHeight(), var8, var7, this.helper.getOptions());
               File var10 = this.helper.getDiskCache().get(this.currentKey);
               this.cacheFile = var10;
               if (var10 != null) {
                  this.sourceKey = var6;
                  this.modelLoaders = this.helper.getModelLoaders(var10);
                  this.modelLoaderIndex = 0;
               }
            }

            this.loadData = null;

            boolean var2;
            boolean var3;
            for(var2 = false; !var2 && this.hasNextModelLoader(); var2 = var3) {
               var4 = this.modelLoaders;
               var1 = this.modelLoaderIndex++;
               this.loadData = ((ModelLoader)var4.get(var1)).buildLoadData(this.cacheFile, this.helper.getWidth(), this.helper.getHeight(), this.helper.getOptions());
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
   }
}
