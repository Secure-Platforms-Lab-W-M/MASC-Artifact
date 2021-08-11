package com.bumptech.glide.load.model;

import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.signature.ObjectKey;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class FileLoader implements ModelLoader {
   private static final String TAG = "FileLoader";
   private final FileLoader.FileOpener fileOpener;

   public FileLoader(FileLoader.FileOpener var1) {
      this.fileOpener = var1;
   }

   public ModelLoader.LoadData buildLoadData(File var1, int var2, int var3, Options var4) {
      return new ModelLoader.LoadData(new ObjectKey(var1), new FileLoader.FileFetcher(var1, this.fileOpener));
   }

   public boolean handles(File var1) {
      return true;
   }

   public static class Factory implements ModelLoaderFactory {
      private final FileLoader.FileOpener opener;

      public Factory(FileLoader.FileOpener var1) {
         this.opener = var1;
      }

      public final ModelLoader build(MultiModelLoaderFactory var1) {
         return new FileLoader(this.opener);
      }

      public final void teardown() {
      }
   }

   public static class FileDescriptorFactory extends FileLoader.Factory {
      public FileDescriptorFactory() {
         super(new FileLoader.FileOpener() {
            public void close(ParcelFileDescriptor var1) throws IOException {
               var1.close();
            }

            public Class getDataClass() {
               return ParcelFileDescriptor.class;
            }

            public ParcelFileDescriptor open(File var1) throws FileNotFoundException {
               return ParcelFileDescriptor.open(var1, 268435456);
            }
         });
      }
   }

   private static final class FileFetcher implements DataFetcher {
      private Object data;
      private final File file;
      private final FileLoader.FileOpener opener;

      FileFetcher(File var1, FileLoader.FileOpener var2) {
         this.file = var1;
         this.opener = var2;
      }

      public void cancel() {
      }

      public void cleanup() {
         Object var1 = this.data;
         if (var1 != null) {
            try {
               this.opener.close(var1);
               return;
            } catch (IOException var2) {
            }
         }

      }

      public Class getDataClass() {
         return this.opener.getDataClass();
      }

      public DataSource getDataSource() {
         return DataSource.LOCAL;
      }

      public void loadData(Priority var1, DataFetcher.DataCallback var2) {
         Object var4;
         try {
            var4 = this.opener.open(this.file);
            this.data = var4;
         } catch (FileNotFoundException var3) {
            if (Log.isLoggable("FileLoader", 3)) {
               Log.d("FileLoader", "Failed to open file", var3);
            }

            var2.onLoadFailed(var3);
            return;
         }

         var2.onDataReady(var4);
      }
   }

   public interface FileOpener {
      void close(Object var1) throws IOException;

      Class getDataClass();

      Object open(File var1) throws FileNotFoundException;
   }

   public static class StreamFactory extends FileLoader.Factory {
      public StreamFactory() {
         super(new FileLoader.FileOpener() {
            public void close(InputStream var1) throws IOException {
               var1.close();
            }

            public Class getDataClass() {
               return InputStream.class;
            }

            public InputStream open(File var1) throws FileNotFoundException {
               return new FileInputStream(var1);
            }
         });
      }
   }
}
