package com.bumptech.glide.load.model;

import androidx.core.util.Pools;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Key;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.util.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

class MultiModelLoader implements ModelLoader {
   private final Pools.Pool exceptionListPool;
   private final List modelLoaders;

   MultiModelLoader(List var1, Pools.Pool var2) {
      this.modelLoaders = var1;
      this.exceptionListPool = var2;
   }

   public ModelLoader.LoadData buildLoadData(Object var1, int var2, int var3, Options var4) {
      Key var7 = null;
      int var6 = this.modelLoaders.size();
      ArrayList var9 = new ArrayList(var6);

      Key var8;
      for(int var5 = 0; var5 < var6; var7 = var8) {
         ModelLoader var10 = (ModelLoader)this.modelLoaders.get(var5);
         var8 = var7;
         if (var10.handles(var1)) {
            ModelLoader.LoadData var11 = var10.buildLoadData(var1, var2, var3, var4);
            var8 = var7;
            if (var11 != null) {
               var8 = var11.sourceKey;
               var9.add(var11.fetcher);
            }
         }

         ++var5;
      }

      if (!var9.isEmpty() && var7 != null) {
         return new ModelLoader.LoadData(var7, new MultiModelLoader.MultiFetcher(var9, this.exceptionListPool));
      } else {
         return null;
      }
   }

   public boolean handles(Object var1) {
      Iterator var2 = this.modelLoaders.iterator();

      do {
         if (!var2.hasNext()) {
            return false;
         }
      } while(!((ModelLoader)var2.next()).handles(var1));

      return true;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("MultiModelLoader{modelLoaders=");
      var1.append(Arrays.toString(this.modelLoaders.toArray()));
      var1.append('}');
      return var1.toString();
   }

   static class MultiFetcher implements DataFetcher, DataFetcher.DataCallback {
      private DataFetcher.DataCallback callback;
      private int currentIndex;
      private List exceptions;
      private final List fetchers;
      private boolean isCancelled;
      private Priority priority;
      private final Pools.Pool throwableListPool;

      MultiFetcher(List var1, Pools.Pool var2) {
         this.throwableListPool = var2;
         Preconditions.checkNotEmpty((Collection)var1);
         this.fetchers = var1;
         this.currentIndex = 0;
      }

      private void startNextOrFail() {
         if (!this.isCancelled) {
            if (this.currentIndex < this.fetchers.size() - 1) {
               ++this.currentIndex;
               this.loadData(this.priority, this.callback);
            } else {
               Preconditions.checkNotNull(this.exceptions);
               this.callback.onLoadFailed(new GlideException("Fetch failed", new ArrayList(this.exceptions)));
            }
         }
      }

      public void cancel() {
         this.isCancelled = true;
         Iterator var1 = this.fetchers.iterator();

         while(var1.hasNext()) {
            ((DataFetcher)var1.next()).cancel();
         }

      }

      public void cleanup() {
         List var1 = this.exceptions;
         if (var1 != null) {
            this.throwableListPool.release(var1);
         }

         this.exceptions = null;
         Iterator var2 = this.fetchers.iterator();

         while(var2.hasNext()) {
            ((DataFetcher)var2.next()).cleanup();
         }

      }

      public Class getDataClass() {
         return ((DataFetcher)this.fetchers.get(0)).getDataClass();
      }

      public DataSource getDataSource() {
         return ((DataFetcher)this.fetchers.get(0)).getDataSource();
      }

      public void loadData(Priority var1, DataFetcher.DataCallback var2) {
         this.priority = var1;
         this.callback = var2;
         this.exceptions = (List)this.throwableListPool.acquire();
         ((DataFetcher)this.fetchers.get(this.currentIndex)).loadData(var1, this);
         if (this.isCancelled) {
            this.cancel();
         }

      }

      public void onDataReady(Object var1) {
         if (var1 != null) {
            this.callback.onDataReady(var1);
         } else {
            this.startNextOrFail();
         }
      }

      public void onLoadFailed(Exception var1) {
         ((List)Preconditions.checkNotNull(this.exceptions)).add(var1);
         this.startNextOrFail();
      }
   }
}
