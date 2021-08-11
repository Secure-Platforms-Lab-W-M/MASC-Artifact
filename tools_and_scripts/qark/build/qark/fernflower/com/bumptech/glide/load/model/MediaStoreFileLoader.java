package com.bumptech.glide.load.model;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.mediastore.MediaStoreUtil;
import com.bumptech.glide.signature.ObjectKey;
import java.io.File;
import java.io.FileNotFoundException;

public final class MediaStoreFileLoader implements ModelLoader {
   private final Context context;

   public MediaStoreFileLoader(Context var1) {
      this.context = var1;
   }

   public ModelLoader.LoadData buildLoadData(Uri var1, int var2, int var3, Options var4) {
      return new ModelLoader.LoadData(new ObjectKey(var1), new MediaStoreFileLoader.FilePathFetcher(this.context, var1));
   }

   public boolean handles(Uri var1) {
      return MediaStoreUtil.isMediaStoreUri(var1);
   }

   public static final class Factory implements ModelLoaderFactory {
      private final Context context;

      public Factory(Context var1) {
         this.context = var1;
      }

      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new MediaStoreFileLoader(this.context);
      }

      public void teardown() {
      }
   }

   private static class FilePathFetcher implements DataFetcher {
      private static final String[] PROJECTION = new String[]{"_data"};
      private final Context context;
      private final Uri uri;

      FilePathFetcher(Context var1, Uri var2) {
         this.context = var1;
         this.uri = var2;
      }

      public void cancel() {
      }

      public void cleanup() {
      }

      public Class getDataClass() {
         return File.class;
      }

      public DataSource getDataSource() {
         return DataSource.LOCAL;
      }

      public void loadData(Priority var1, DataFetcher.DataCallback var2) {
         Cursor var4 = this.context.getContentResolver().query(this.uri, PROJECTION, (String)null, (String[])null, (String)null);
         String var7 = null;
         Object var3 = null;
         if (var4 != null) {
            var7 = (String)var3;

            try {
               if (var4.moveToFirst()) {
                  var7 = var4.getString(var4.getColumnIndexOrThrow("_data"));
               }
            } finally {
               var4.close();
            }
         }

         if (TextUtils.isEmpty(var7)) {
            StringBuilder var8 = new StringBuilder();
            var8.append("Failed to find file path for: ");
            var8.append(this.uri);
            var2.onLoadFailed(new FileNotFoundException(var8.toString()));
         } else {
            var2.onDataReady(new File(var7));
         }
      }
   }
}
