package com.bumptech.glide.load.model.stream;

import android.net.Uri;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class HttpUriLoader implements ModelLoader {
   private static final Set SCHEMES = Collections.unmodifiableSet(new HashSet(Arrays.asList("http", "https")));
   private final ModelLoader urlLoader;

   public HttpUriLoader(ModelLoader var1) {
      this.urlLoader = var1;
   }

   public ModelLoader.LoadData buildLoadData(Uri var1, int var2, int var3, Options var4) {
      return this.urlLoader.buildLoadData(new GlideUrl(var1.toString()), var2, var3, var4);
   }

   public boolean handles(Uri var1) {
      return SCHEMES.contains(var1.getScheme());
   }

   public static class Factory implements ModelLoaderFactory {
      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new HttpUriLoader(var1.build(GlideUrl.class, InputStream.class));
      }

      public void teardown() {
      }
   }
}
