package com.bumptech.glide.load.model.stream;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import java.io.InputStream;
import java.net.URL;

public class UrlLoader implements ModelLoader {
   private final ModelLoader glideUrlLoader;

   public UrlLoader(ModelLoader var1) {
      this.glideUrlLoader = var1;
   }

   public ModelLoader.LoadData buildLoadData(URL var1, int var2, int var3, Options var4) {
      return this.glideUrlLoader.buildLoadData(new GlideUrl(var1), var2, var3, var4);
   }

   public boolean handles(URL var1) {
      return true;
   }

   public static class StreamFactory implements ModelLoaderFactory {
      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new UrlLoader(var1.build(GlideUrl.class, InputStream.class));
      }

      public void teardown() {
      }
   }
}
