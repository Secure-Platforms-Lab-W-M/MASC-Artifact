package com.bumptech.glide.load.model;

import android.util.Base64;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.signature.ObjectKey;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class DataUrlLoader implements ModelLoader {
   private static final String BASE64_TAG = ";base64";
   private static final String DATA_SCHEME_IMAGE = "data:image";
   private final DataUrlLoader.DataDecoder dataDecoder;

   public DataUrlLoader(DataUrlLoader.DataDecoder var1) {
      this.dataDecoder = var1;
   }

   public ModelLoader.LoadData buildLoadData(Object var1, int var2, int var3, Options var4) {
      return new ModelLoader.LoadData(new ObjectKey(var1), new DataUrlLoader.DataUriFetcher(var1.toString(), this.dataDecoder));
   }

   public boolean handles(Object var1) {
      return var1.toString().startsWith("data:image");
   }

   public interface DataDecoder {
      void close(Object var1) throws IOException;

      Object decode(String var1) throws IllegalArgumentException;

      Class getDataClass();
   }

   private static final class DataUriFetcher implements DataFetcher {
      private Object data;
      private final String dataUri;
      private final DataUrlLoader.DataDecoder reader;

      DataUriFetcher(String var1, DataUrlLoader.DataDecoder var2) {
         this.dataUri = var1;
         this.reader = var2;
      }

      public void cancel() {
      }

      public void cleanup() {
         try {
            this.reader.close(this.data);
         } catch (IOException var2) {
         }
      }

      public Class getDataClass() {
         return this.reader.getDataClass();
      }

      public DataSource getDataSource() {
         return DataSource.LOCAL;
      }

      public void loadData(Priority var1, DataFetcher.DataCallback var2) {
         try {
            Object var4 = this.reader.decode(this.dataUri);
            this.data = var4;
            var2.onDataReady(var4);
         } catch (IllegalArgumentException var3) {
            var2.onLoadFailed(var3);
         }
      }
   }

   public static final class StreamFactory implements ModelLoaderFactory {
      private final DataUrlLoader.DataDecoder opener = new DataUrlLoader.DataDecoder() {
         public void close(InputStream var1) throws IOException {
            var1.close();
         }

         public InputStream decode(String var1) {
            if (var1.startsWith("data:image")) {
               int var2 = var1.indexOf(44);
               if (var2 != -1) {
                  if (var1.substring(0, var2).endsWith(";base64")) {
                     return new ByteArrayInputStream(Base64.decode(var1.substring(var2 + 1), 0));
                  } else {
                     throw new IllegalArgumentException("Not a base64 image data URL.");
                  }
               } else {
                  throw new IllegalArgumentException("Missing comma in data URL.");
               }
            } else {
               throw new IllegalArgumentException("Not a valid image data URL.");
            }
         }

         public Class getDataClass() {
            return InputStream.class;
         }
      };

      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new DataUrlLoader(this.opener);
      }

      public void teardown() {
      }
   }
}
