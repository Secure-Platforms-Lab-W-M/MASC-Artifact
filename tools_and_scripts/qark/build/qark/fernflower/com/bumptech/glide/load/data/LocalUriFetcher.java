package com.bumptech.glide.load.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;

public abstract class LocalUriFetcher implements DataFetcher {
   private static final String TAG = "LocalUriFetcher";
   private final ContentResolver contentResolver;
   private Object data;
   private final Uri uri;

   public LocalUriFetcher(ContentResolver var1, Uri var2) {
      this.contentResolver = var1;
      this.uri = var2;
   }

   public void cancel() {
   }

   public void cleanup() {
      Object var1 = this.data;
      if (var1 != null) {
         try {
            this.close(var1);
            return;
         } catch (IOException var2) {
         }
      }

   }

   protected abstract void close(Object var1) throws IOException;

   public DataSource getDataSource() {
      return DataSource.LOCAL;
   }

   public final void loadData(Priority var1, DataFetcher.DataCallback var2) {
      Object var4;
      try {
         var4 = this.loadResource(this.uri, this.contentResolver);
         this.data = var4;
      } catch (FileNotFoundException var3) {
         if (Log.isLoggable("LocalUriFetcher", 3)) {
            Log.d("LocalUriFetcher", "Failed to open Uri", var3);
         }

         var2.onLoadFailed(var3);
         return;
      }

      var2.onDataReady(var4);
   }

   protected abstract Object loadResource(Uri var1, ContentResolver var2) throws FileNotFoundException;
}
