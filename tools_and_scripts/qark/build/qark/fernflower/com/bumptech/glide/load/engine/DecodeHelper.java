package com.bumptech.glide.load.engine;

import com.bumptech.glide.GlideContext;
import com.bumptech.glide.Priority;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.resource.UnitTransformation;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

final class DecodeHelper {
   private final List cacheKeys = new ArrayList();
   private DecodeJob.DiskCacheProvider diskCacheProvider;
   private DiskCacheStrategy diskCacheStrategy;
   private GlideContext glideContext;
   private int height;
   private boolean isCacheKeysSet;
   private boolean isLoadDataSet;
   private boolean isScaleOnlyOrNoTransform;
   private boolean isTransformationRequired;
   private final List loadData = new ArrayList();
   private Object model;
   private Options options;
   private Priority priority;
   private Class resourceClass;
   private Key signature;
   private Class transcodeClass;
   private Map transformations;
   private int width;

   void clear() {
      this.glideContext = null;
      this.model = null;
      this.signature = null;
      this.resourceClass = null;
      this.transcodeClass = null;
      this.options = null;
      this.priority = null;
      this.transformations = null;
      this.diskCacheStrategy = null;
      this.loadData.clear();
      this.isLoadDataSet = false;
      this.cacheKeys.clear();
      this.isCacheKeysSet = false;
   }

   ArrayPool getArrayPool() {
      return this.glideContext.getArrayPool();
   }

   List getCacheKeys() {
      if (!this.isCacheKeysSet) {
         this.isCacheKeysSet = true;
         this.cacheKeys.clear();
         List var4 = this.getLoadData();
         int var1 = 0;

         for(int var3 = var4.size(); var1 < var3; ++var1) {
            ModelLoader.LoadData var5 = (ModelLoader.LoadData)var4.get(var1);
            if (!this.cacheKeys.contains(var5.sourceKey)) {
               this.cacheKeys.add(var5.sourceKey);
            }

            for(int var2 = 0; var2 < var5.alternateKeys.size(); ++var2) {
               if (!this.cacheKeys.contains(var5.alternateKeys.get(var2))) {
                  this.cacheKeys.add(var5.alternateKeys.get(var2));
               }
            }
         }
      }

      return this.cacheKeys;
   }

   DiskCache getDiskCache() {
      return this.diskCacheProvider.getDiskCache();
   }

   DiskCacheStrategy getDiskCacheStrategy() {
      return this.diskCacheStrategy;
   }

   int getHeight() {
      return this.height;
   }

   List getLoadData() {
      if (!this.isLoadDataSet) {
         this.isLoadDataSet = true;
         this.loadData.clear();
         List var3 = this.glideContext.getRegistry().getModelLoaders(this.model);
         int var1 = 0;

         for(int var2 = var3.size(); var1 < var2; ++var1) {
            ModelLoader.LoadData var4 = ((ModelLoader)var3.get(var1)).buildLoadData(this.model, this.width, this.height, this.options);
            if (var4 != null) {
               this.loadData.add(var4);
            }
         }
      }

      return this.loadData;
   }

   LoadPath getLoadPath(Class var1) {
      return this.glideContext.getRegistry().getLoadPath(var1, this.resourceClass, this.transcodeClass);
   }

   Class getModelClass() {
      return this.model.getClass();
   }

   List getModelLoaders(File var1) throws Registry.NoModelLoaderAvailableException {
      return this.glideContext.getRegistry().getModelLoaders(var1);
   }

   Options getOptions() {
      return this.options;
   }

   Priority getPriority() {
      return this.priority;
   }

   List getRegisteredResourceClasses() {
      return this.glideContext.getRegistry().getRegisteredResourceClasses(this.model.getClass(), this.resourceClass, this.transcodeClass);
   }

   ResourceEncoder getResultEncoder(Resource var1) {
      return this.glideContext.getRegistry().getResultEncoder(var1);
   }

   Key getSignature() {
      return this.signature;
   }

   Encoder getSourceEncoder(Object var1) throws Registry.NoSourceEncoderAvailableException {
      return this.glideContext.getRegistry().getSourceEncoder(var1);
   }

   Class getTranscodeClass() {
      return this.transcodeClass;
   }

   Transformation getTransformation(Class var1) {
      Transformation var3 = (Transformation)this.transformations.get(var1);
      Transformation var2 = var3;
      if (var3 == null) {
         Iterator var4 = this.transformations.entrySet().iterator();

         while(true) {
            var2 = var3;
            if (!var4.hasNext()) {
               break;
            }

            Entry var5 = (Entry)var4.next();
            if (((Class)var5.getKey()).isAssignableFrom(var1)) {
               var2 = (Transformation)var5.getValue();
               break;
            }
         }
      }

      if (var2 == null) {
         if (this.transformations.isEmpty() && this.isTransformationRequired) {
            StringBuilder var6 = new StringBuilder();
            var6.append("Missing transformation for ");
            var6.append(var1);
            var6.append(". If you wish to ignore unknown resource types, use the optional transformation methods.");
            throw new IllegalArgumentException(var6.toString());
         } else {
            return UnitTransformation.get();
         }
      } else {
         return var2;
      }
   }

   int getWidth() {
      return this.width;
   }

   boolean hasLoadPath(Class var1) {
      return this.getLoadPath(var1) != null;
   }

   void init(GlideContext var1, Object var2, Key var3, int var4, int var5, DiskCacheStrategy var6, Class var7, Class var8, Priority var9, Options var10, Map var11, boolean var12, boolean var13, DecodeJob.DiskCacheProvider var14) {
      this.glideContext = var1;
      this.model = var2;
      this.signature = var3;
      this.width = var4;
      this.height = var5;
      this.diskCacheStrategy = var6;
      this.resourceClass = var7;
      this.diskCacheProvider = var14;
      this.transcodeClass = var8;
      this.priority = var9;
      this.options = var10;
      this.transformations = var11;
      this.isTransformationRequired = var12;
      this.isScaleOnlyOrNoTransform = var13;
   }

   boolean isResourceEncoderAvailable(Resource var1) {
      return this.glideContext.getRegistry().isResourceEncoderAvailable(var1);
   }

   boolean isScaleOnlyOrNoTransform() {
      return this.isScaleOnlyOrNoTransform;
   }

   boolean isSourceKey(Key var1) {
      List var4 = this.getLoadData();
      int var2 = 0;

      for(int var3 = var4.size(); var2 < var3; ++var2) {
         if (((ModelLoader.LoadData)var4.get(var2)).sourceKey.equals(var1)) {
            return true;
         }
      }

      return false;
   }
}
