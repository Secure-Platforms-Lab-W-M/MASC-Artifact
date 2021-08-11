package com.bumptech.glide.load.model;

import androidx.core.util.Pools;
import com.bumptech.glide.Registry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ModelLoaderRegistry {
   private final ModelLoaderRegistry.ModelLoaderCache cache;
   private final MultiModelLoaderFactory multiModelLoaderFactory;

   public ModelLoaderRegistry(Pools.Pool var1) {
      this(new MultiModelLoaderFactory(var1));
   }

   private ModelLoaderRegistry(MultiModelLoaderFactory var1) {
      this.cache = new ModelLoaderRegistry.ModelLoaderCache();
      this.multiModelLoaderFactory = var1;
   }

   private static Class getClass(Object var0) {
      return var0.getClass();
   }

   private List getModelLoadersForClass(Class var1) {
      synchronized(this){}

      Throwable var10000;
      label75: {
         boolean var10001;
         List var3;
         try {
            var3 = this.cache.get(var1);
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label75;
         }

         List var2 = var3;
         if (var3 != null) {
            return var2;
         }

         label66:
         try {
            var2 = Collections.unmodifiableList(this.multiModelLoaderFactory.build(var1));
            this.cache.put(var1, var2);
            return var2;
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label66;
         }
      }

      Throwable var10 = var10000;
      throw var10;
   }

   private void tearDown(List var1) {
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         ((ModelLoaderFactory)var2.next()).teardown();
      }

   }

   public void append(Class var1, Class var2, ModelLoaderFactory var3) {
      synchronized(this){}

      try {
         this.multiModelLoaderFactory.append(var1, var2, var3);
         this.cache.clear();
      } finally {
         ;
      }

   }

   public ModelLoader build(Class var1, Class var2) {
      synchronized(this){}

      ModelLoader var5;
      try {
         var5 = this.multiModelLoaderFactory.build(var1, var2);
      } finally {
         ;
      }

      return var5;
   }

   public List getDataClasses(Class var1) {
      synchronized(this){}

      List var4;
      try {
         var4 = this.multiModelLoaderFactory.getDataClasses(var1);
      } finally {
         ;
      }

      return var4;
   }

   public List getModelLoaders(Object var1) {
      List var8 = this.getModelLoadersForClass(getClass(var1));
      if (!var8.isEmpty()) {
         int var5 = var8.size();
         boolean var2 = true;
         Object var6 = Collections.emptyList();

         Object var7;
         for(int var4 = 0; var4 < var5; var6 = var7) {
            ModelLoader var9 = (ModelLoader)var8.get(var4);
            boolean var3 = var2;
            var7 = var6;
            if (var9.handles(var1)) {
               var3 = var2;
               if (var2) {
                  var6 = new ArrayList(var5 - var4);
                  var3 = false;
               }

               ((List)var6).add(var9);
               var7 = var6;
            }

            ++var4;
            var2 = var3;
         }

         if (!((List)var6).isEmpty()) {
            return (List)var6;
         } else {
            throw new Registry.NoModelLoaderAvailableException(var1, var8);
         }
      } else {
         throw new Registry.NoModelLoaderAvailableException(var1);
      }
   }

   public void prepend(Class var1, Class var2, ModelLoaderFactory var3) {
      synchronized(this){}

      try {
         this.multiModelLoaderFactory.prepend(var1, var2, var3);
         this.cache.clear();
      } finally {
         ;
      }

   }

   public void remove(Class var1, Class var2) {
      synchronized(this){}

      try {
         this.tearDown(this.multiModelLoaderFactory.remove(var1, var2));
         this.cache.clear();
      } finally {
         ;
      }

   }

   public void replace(Class var1, Class var2, ModelLoaderFactory var3) {
      synchronized(this){}

      try {
         this.tearDown(this.multiModelLoaderFactory.replace(var1, var2, var3));
         this.cache.clear();
      } finally {
         ;
      }

   }

   private static class ModelLoaderCache {
      private final Map cachedModelLoaders = new HashMap();

      ModelLoaderCache() {
      }

      public void clear() {
         this.cachedModelLoaders.clear();
      }

      public List get(Class var1) {
         ModelLoaderRegistry.ModelLoaderCache.Entry var2 = (ModelLoaderRegistry.ModelLoaderCache.Entry)this.cachedModelLoaders.get(var1);
         return var2 == null ? null : var2.loaders;
      }

      public void put(Class var1, List var2) {
         if ((ModelLoaderRegistry.ModelLoaderCache.Entry)this.cachedModelLoaders.put(var1, new ModelLoaderRegistry.ModelLoaderCache.Entry(var2)) != null) {
            StringBuilder var3 = new StringBuilder();
            var3.append("Already cached loaders for model: ");
            var3.append(var1);
            throw new IllegalStateException(var3.toString());
         }
      }

      private static class Entry {
         final List loaders;

         public Entry(List var1) {
            this.loaders = var1;
         }
      }
   }
}
