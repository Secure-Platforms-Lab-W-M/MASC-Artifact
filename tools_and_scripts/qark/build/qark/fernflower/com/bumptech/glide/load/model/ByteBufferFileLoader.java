package com.bumptech.glide.load.model;

import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.signature.ObjectKey;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteBufferFileLoader implements ModelLoader {
   private static final String TAG = "ByteBufferFileLoader";

   public ModelLoader.LoadData buildLoadData(File var1, int var2, int var3, Options var4) {
      return new ModelLoader.LoadData(new ObjectKey(var1), new ByteBufferFileLoader.ByteBufferFetcher(var1));
   }

   public boolean handles(File var1) {
      return true;
   }

   private static final class ByteBufferFetcher implements DataFetcher {
      private final File file;

      ByteBufferFetcher(File var1) {
         this.file = var1;
      }

      public void cancel() {
      }

      public void cleanup() {
      }

      public Class getDataClass() {
         return ByteBuffer.class;
      }

      public DataSource getDataSource() {
         return DataSource.LOCAL;
      }

      public void loadData(Priority var1, DataFetcher.DataCallback var2) {
         ByteBuffer var4;
         try {
            var4 = ByteBufferUtil.fromFile(this.file);
         } catch (IOException var3) {
            if (Log.isLoggable("ByteBufferFileLoader", 3)) {
               Log.d("ByteBufferFileLoader", "Failed to obtain ByteBuffer for file", var3);
            }

            var2.onLoadFailed(var3);
            return;
         }

         var2.onDataReady(var4);
      }
   }

   public static class Factory implements ModelLoaderFactory {
      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new ByteBufferFileLoader();
      }

      public void teardown() {
      }
   }
}
