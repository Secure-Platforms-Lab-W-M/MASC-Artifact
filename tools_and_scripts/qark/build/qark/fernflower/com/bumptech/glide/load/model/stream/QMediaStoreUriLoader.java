package com.bumptech.glide.load.model.stream;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.os.Build.VERSION;
import android.provider.MediaStore;
import android.text.TextUtils;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.mediastore.MediaStoreUtil;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public final class QMediaStoreUriLoader implements ModelLoader {
   private final Context context;
   private final Class dataClass;
   private final ModelLoader fileDelegate;
   private final ModelLoader uriDelegate;

   QMediaStoreUriLoader(Context var1, ModelLoader var2, ModelLoader var3, Class var4) {
      this.context = var1.getApplicationContext();
      this.fileDelegate = var2;
      this.uriDelegate = var3;
      this.dataClass = var4;
   }

   public ModelLoader.LoadData buildLoadData(Uri var1, int var2, int var3, Options var4) {
      return new ModelLoader.LoadData(new ObjectKey(var1), new QMediaStoreUriLoader.QMediaStoreUriFetcher(this.context, this.fileDelegate, this.uriDelegate, var1, var2, var3, var4, this.dataClass));
   }

   public boolean handles(Uri var1) {
      return VERSION.SDK_INT >= 29 && MediaStoreUtil.isMediaStoreUri(var1);
   }

   private abstract static class Factory implements ModelLoaderFactory {
      private final Context context;
      private final Class dataClass;

      Factory(Context var1, Class var2) {
         this.context = var1;
         this.dataClass = var2;
      }

      public final ModelLoader build(MultiModelLoaderFactory var1) {
         return new QMediaStoreUriLoader(this.context, var1.build(File.class, this.dataClass), var1.build(Uri.class, this.dataClass), this.dataClass);
      }

      public final void teardown() {
      }
   }

   public static final class FileDescriptorFactory extends QMediaStoreUriLoader.Factory {
      public FileDescriptorFactory(Context var1) {
         super(var1, ParcelFileDescriptor.class);
      }
   }

   public static final class InputStreamFactory extends QMediaStoreUriLoader.Factory {
      public InputStreamFactory(Context var1) {
         super(var1, InputStream.class);
      }
   }

   private static final class QMediaStoreUriFetcher implements DataFetcher {
      private static final String[] PROJECTION = new String[]{"_data"};
      private final Context context;
      private final Class dataClass;
      private volatile DataFetcher delegate;
      private final ModelLoader fileDelegate;
      private final int height;
      private volatile boolean isCancelled;
      private final Options options;
      private final Uri uri;
      private final ModelLoader uriDelegate;
      private final int width;

      QMediaStoreUriFetcher(Context var1, ModelLoader var2, ModelLoader var3, Uri var4, int var5, int var6, Options var7, Class var8) {
         this.context = var1.getApplicationContext();
         this.fileDelegate = var2;
         this.uriDelegate = var3;
         this.uri = var4;
         this.width = var5;
         this.height = var6;
         this.options = var7;
         this.dataClass = var8;
      }

      private ModelLoader.LoadData buildDelegateData() throws FileNotFoundException {
         if (Environment.isExternalStorageLegacy()) {
            return this.fileDelegate.buildLoadData(this.queryForFilePath(this.uri), this.width, this.height, this.options);
         } else {
            Uri var1;
            if (this.isAccessMediaLocationGranted()) {
               var1 = MediaStore.setRequireOriginal(this.uri);
            } else {
               var1 = this.uri;
            }

            return this.uriDelegate.buildLoadData(var1, this.width, this.height, this.options);
         }
      }

      private DataFetcher buildDelegateFetcher() throws FileNotFoundException {
         ModelLoader.LoadData var1 = this.buildDelegateData();
         return var1 != null ? var1.fetcher : null;
      }

      private boolean isAccessMediaLocationGranted() {
         return this.context.checkSelfPermission("android.permission.ACCESS_MEDIA_LOCATION") == 0;
      }

      private File queryForFilePath(Uri var1) throws FileNotFoundException {
         Cursor var2 = null;

         Throwable var10000;
         label1653: {
            Cursor var3;
            boolean var10001;
            try {
               var3 = this.context.getContentResolver().query(var1, PROJECTION, (String)null, (String[])null, (String)null);
            } catch (Throwable var186) {
               var10000 = var186;
               var10001 = false;
               break label1653;
            }

            StringBuilder var189;
            if (var3 != null) {
               label1652: {
                  var2 = var3;

                  try {
                     if (!var3.moveToFirst()) {
                        break label1652;
                     }
                  } catch (Throwable var185) {
                     var10000 = var185;
                     var10001 = false;
                     break label1653;
                  }

                  var2 = var3;

                  String var4;
                  try {
                     var4 = var3.getString(var3.getColumnIndexOrThrow("_data"));
                  } catch (Throwable var180) {
                     var10000 = var180;
                     var10001 = false;
                     break label1653;
                  }

                  var2 = var3;

                  label1651: {
                     try {
                        if (TextUtils.isEmpty(var4)) {
                           break label1651;
                        }
                     } catch (Throwable var179) {
                        var10000 = var179;
                        var10001 = false;
                        break label1653;
                     }

                     var2 = var3;

                     File var187;
                     try {
                        var187 = new File(var4);
                     } catch (Throwable var174) {
                        var10000 = var174;
                        var10001 = false;
                        break label1653;
                     }

                     if (var3 != null) {
                        var3.close();
                     }

                     return var187;
                  }

                  var2 = var3;

                  try {
                     var189 = new StringBuilder();
                  } catch (Throwable var178) {
                     var10000 = var178;
                     var10001 = false;
                     break label1653;
                  }

                  var2 = var3;

                  try {
                     var189.append("File path was empty in media store for: ");
                  } catch (Throwable var177) {
                     var10000 = var177;
                     var10001 = false;
                     break label1653;
                  }

                  var2 = var3;

                  try {
                     var189.append(var1);
                  } catch (Throwable var176) {
                     var10000 = var176;
                     var10001 = false;
                     break label1653;
                  }

                  var2 = var3;

                  try {
                     throw new FileNotFoundException(var189.toString());
                  } catch (Throwable var175) {
                     var10000 = var175;
                     var10001 = false;
                     break label1653;
                  }
               }
            }

            var2 = var3;

            try {
               var189 = new StringBuilder();
            } catch (Throwable var184) {
               var10000 = var184;
               var10001 = false;
               break label1653;
            }

            var2 = var3;

            try {
               var189.append("Failed to media store entry for: ");
            } catch (Throwable var183) {
               var10000 = var183;
               var10001 = false;
               break label1653;
            }

            var2 = var3;

            try {
               var189.append(var1);
            } catch (Throwable var182) {
               var10000 = var182;
               var10001 = false;
               break label1653;
            }

            var2 = var3;

            label1620:
            try {
               throw new FileNotFoundException(var189.toString());
            } catch (Throwable var181) {
               var10000 = var181;
               var10001 = false;
               break label1620;
            }
         }

         Throwable var188 = var10000;
         if (var2 != null) {
            var2.close();
         }

         throw var188;
      }

      public void cancel() {
         this.isCancelled = true;
         DataFetcher var1 = this.delegate;
         if (var1 != null) {
            var1.cancel();
         }

      }

      public void cleanup() {
         DataFetcher var1 = this.delegate;
         if (var1 != null) {
            var1.cleanup();
         }

      }

      public Class getDataClass() {
         return this.dataClass;
      }

      public DataSource getDataSource() {
         return DataSource.LOCAL;
      }

      public void loadData(Priority var1, DataFetcher.DataCallback var2) {
         FileNotFoundException var10000;
         label40: {
            DataFetcher var3;
            boolean var10001;
            try {
               var3 = this.buildDelegateFetcher();
            } catch (FileNotFoundException var8) {
               var10000 = var8;
               var10001 = false;
               break label40;
            }

            if (var3 == null) {
               try {
                  StringBuilder var9 = new StringBuilder();
                  var9.append("Failed to build fetcher for: ");
                  var9.append(this.uri);
                  var2.onLoadFailed(new IllegalArgumentException(var9.toString()));
                  return;
               } catch (FileNotFoundException var4) {
                  var10000 = var4;
                  var10001 = false;
               }
            } else {
               label36: {
                  label35: {
                     try {
                        this.delegate = var3;
                        if (this.isCancelled) {
                           this.cancel();
                           break label35;
                        }
                     } catch (FileNotFoundException var7) {
                        var10000 = var7;
                        var10001 = false;
                        break label36;
                     }

                     try {
                        var3.loadData(var1, var2);
                     } catch (FileNotFoundException var6) {
                        var10000 = var6;
                        var10001 = false;
                        break label36;
                     }
                  }

                  try {
                     return;
                  } catch (FileNotFoundException var5) {
                     var10000 = var5;
                     var10001 = false;
                  }
               }
            }
         }

         FileNotFoundException var10 = var10000;
         var2.onLoadFailed(var10);
      }
   }
}
