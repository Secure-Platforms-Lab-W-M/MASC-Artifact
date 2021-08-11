package com.bumptech.glide.load.model;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.signature.ObjectKey;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteArrayLoader implements ModelLoader {
   private final ByteArrayLoader.Converter converter;

   public ByteArrayLoader(ByteArrayLoader.Converter var1) {
      this.converter = var1;
   }

   public ModelLoader.LoadData buildLoadData(byte[] var1, int var2, int var3, Options var4) {
      return new ModelLoader.LoadData(new ObjectKey(var1), new ByteArrayLoader.Fetcher(var1, this.converter));
   }

   public boolean handles(byte[] var1) {
      return true;
   }

   public static class ByteBufferFactory implements ModelLoaderFactory {
      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new ByteArrayLoader(new ByteArrayLoader.Converter() {
            public ByteBuffer convert(byte[] var1) {
               return ByteBuffer.wrap(var1);
            }

            public Class getDataClass() {
               return ByteBuffer.class;
            }
         });
      }

      public void teardown() {
      }
   }

   public interface Converter {
      Object convert(byte[] var1);

      Class getDataClass();
   }

   private static class Fetcher implements DataFetcher {
      private final ByteArrayLoader.Converter converter;
      private final byte[] model;

      Fetcher(byte[] var1, ByteArrayLoader.Converter var2) {
         this.model = var1;
         this.converter = var2;
      }

      public void cancel() {
      }

      public void cleanup() {
      }

      public Class getDataClass() {
         return this.converter.getDataClass();
      }

      public DataSource getDataSource() {
         return DataSource.LOCAL;
      }

      public void loadData(Priority var1, DataFetcher.DataCallback var2) {
         var2.onDataReady(this.converter.convert(this.model));
      }
   }

   public static class StreamFactory implements ModelLoaderFactory {
      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new ByteArrayLoader(new ByteArrayLoader.Converter() {
            public InputStream convert(byte[] var1) {
               return new ByteArrayInputStream(var1);
            }

            public Class getDataClass() {
               return InputStream.class;
            }
         });
      }

      public void teardown() {
      }
   }
}
