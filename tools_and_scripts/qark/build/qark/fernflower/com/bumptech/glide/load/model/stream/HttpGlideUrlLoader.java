package com.bumptech.glide.load.model.stream;

import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.HttpUrlFetcher;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;

public class HttpGlideUrlLoader implements ModelLoader {
   public static final Option TIMEOUT = Option.memory("com.bumptech.glide.load.model.stream.HttpGlideUrlLoader.Timeout", 2500);
   private final ModelCache modelCache;

   public HttpGlideUrlLoader() {
      this((ModelCache)null);
   }

   public HttpGlideUrlLoader(ModelCache var1) {
      this.modelCache = var1;
   }

   public ModelLoader.LoadData buildLoadData(GlideUrl var1, int var2, int var3, Options var4) {
      GlideUrl var5 = var1;
      ModelCache var6 = this.modelCache;
      if (var6 != null) {
         GlideUrl var7 = (GlideUrl)var6.get(var1, 0, 0);
         var5 = var7;
         if (var7 == null) {
            this.modelCache.put(var1, 0, 0, var1);
            var5 = var1;
         }
      }

      return new ModelLoader.LoadData(var5, new HttpUrlFetcher(var5, (Integer)var4.get(TIMEOUT)));
   }

   public boolean handles(GlideUrl var1) {
      return true;
   }

   public static class Factory implements ModelLoaderFactory {
      private final ModelCache modelCache = new ModelCache(500L);

      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new HttpGlideUrlLoader(this.modelCache);
      }

      public void teardown() {
      }
   }
}
