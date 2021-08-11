package com.bumptech.glide.load.data;

import android.content.res.AssetManager;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import java.io.IOException;

public abstract class AssetPathFetcher implements DataFetcher {
   private static final String TAG = "AssetPathFetcher";
   private final AssetManager assetManager;
   private final String assetPath;
   private Object data;

   public AssetPathFetcher(AssetManager var1, String var2) {
      this.assetManager = var1;
      this.assetPath = var2;
   }

   public void cancel() {
   }

   public void cleanup() {
      Object var1 = this.data;
      if (var1 != null) {
         try {
            this.close(var1);
         } catch (IOException var2) {
         }
      }
   }

   protected abstract void close(Object var1) throws IOException;

   public DataSource getDataSource() {
      return DataSource.LOCAL;
   }

   public void loadData(Priority var1, DataFetcher.DataCallback var2) {
      Object var4;
      try {
         var4 = this.loadResource(this.assetManager, this.assetPath);
         this.data = var4;
      } catch (IOException var3) {
         if (Log.isLoggable("AssetPathFetcher", 3)) {
            Log.d("AssetPathFetcher", "Failed to load data from asset manager", var3);
         }

         var2.onLoadFailed(var3);
         return;
      }

      var2.onDataReady(var4);
   }

   protected abstract Object loadResource(AssetManager var1, String var2) throws IOException;
}
