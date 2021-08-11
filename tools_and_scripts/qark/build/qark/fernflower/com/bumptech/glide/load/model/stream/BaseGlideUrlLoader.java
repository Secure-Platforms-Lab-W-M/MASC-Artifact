package com.bumptech.glide.load.model.stream;

import android.text.TextUtils;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.model.ModelCache;
import com.bumptech.glide.load.model.ModelLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public abstract class BaseGlideUrlLoader implements ModelLoader {
   private final ModelLoader concreteLoader;
   private final ModelCache modelCache;

   protected BaseGlideUrlLoader(ModelLoader var1) {
      this(var1, (ModelCache)null);
   }

   protected BaseGlideUrlLoader(ModelLoader var1, ModelCache var2) {
      this.concreteLoader = var1;
      this.modelCache = var2;
   }

   private static List getAlternateKeys(Collection var0) {
      ArrayList var1 = new ArrayList(var0.size());
      Iterator var2 = var0.iterator();

      while(var2.hasNext()) {
         var1.add(new GlideUrl((String)var2.next()));
      }

      return var1;
   }

   public ModelLoader.LoadData buildLoadData(Object var1, int var2, int var3, Options var4) {
      GlideUrl var5 = null;
      ModelCache var6 = this.modelCache;
      if (var6 != null) {
         var5 = (GlideUrl)var6.get(var1, var2, var3);
      }

      GlideUrl var11 = var5;
      if (var5 == null) {
         String var10 = this.getUrl(var1, var2, var3, var4);
         if (TextUtils.isEmpty(var10)) {
            return null;
         }

         var5 = new GlideUrl(var10, this.getHeaders(var1, var2, var3, var4));
         ModelCache var7 = this.modelCache;
         var11 = var5;
         if (var7 != null) {
            var7.put(var1, var2, var3, var5);
            var11 = var5;
         }
      }

      List var8 = this.getAlternateUrls(var1, var2, var3, var4);
      ModelLoader.LoadData var9 = this.concreteLoader.buildLoadData(var11, var2, var3, var4);
      if (var9 != null) {
         return var8.isEmpty() ? var9 : new ModelLoader.LoadData(var9.sourceKey, getAlternateKeys(var8), var9.fetcher);
      } else {
         return var9;
      }
   }

   protected List getAlternateUrls(Object var1, int var2, int var3, Options var4) {
      return Collections.emptyList();
   }

   protected Headers getHeaders(Object var1, int var2, int var3, Options var4) {
      return Headers.DEFAULT;
   }

   protected abstract String getUrl(Object var1, int var2, int var3, Options var4);
}
