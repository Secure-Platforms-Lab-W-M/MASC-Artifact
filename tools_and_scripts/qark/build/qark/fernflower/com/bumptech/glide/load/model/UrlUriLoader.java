package com.bumptech.glide.load.model;

import android.net.Uri;
import com.bumptech.glide.load.Options;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UrlUriLoader implements ModelLoader {
   private static final Set SCHEMES = Collections.unmodifiableSet(new HashSet(Arrays.asList("http", "https")));
   private final ModelLoader urlLoader;

   public UrlUriLoader(ModelLoader var1) {
      this.urlLoader = var1;
   }

   public ModelLoader.LoadData buildLoadData(Uri var1, int var2, int var3, Options var4) {
      GlideUrl var5 = new GlideUrl(var1.toString());
      return this.urlLoader.buildLoadData(var5, var2, var3, var4);
   }

   public boolean handles(Uri var1) {
      return SCHEMES.contains(var1.getScheme());
   }

   public static class StreamFactory implements ModelLoaderFactory {
      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new UrlUriLoader(var1.build(GlideUrl.class, InputStream.class));
      }

      public void teardown() {
      }
   }
}
