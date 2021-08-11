package com.bumptech.glide.load.model;

import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import com.bumptech.glide.load.Options;
import java.io.File;
import java.io.InputStream;

public class StringLoader implements ModelLoader {
   private final ModelLoader uriLoader;

   public StringLoader(ModelLoader var1) {
      this.uriLoader = var1;
   }

   private static Uri parseUri(String var0) {
      if (TextUtils.isEmpty(var0)) {
         return null;
      } else if (var0.charAt(0) == '/') {
         return toFileUri(var0);
      } else {
         Uri var2 = Uri.parse(var0);
         Uri var1 = var2;
         if (var2.getScheme() == null) {
            var1 = toFileUri(var0);
         }

         return var1;
      }
   }

   private static Uri toFileUri(String var0) {
      return Uri.fromFile(new File(var0));
   }

   public ModelLoader.LoadData buildLoadData(String var1, int var2, int var3, Options var4) {
      Uri var5 = parseUri(var1);
      return var5 != null && this.uriLoader.handles(var5) ? this.uriLoader.buildLoadData(var5, var2, var3, var4) : null;
   }

   public boolean handles(String var1) {
      return true;
   }

   public static final class AssetFileDescriptorFactory implements ModelLoaderFactory {
      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new StringLoader(var1.build(Uri.class, AssetFileDescriptor.class));
      }

      public void teardown() {
      }
   }

   public static class FileDescriptorFactory implements ModelLoaderFactory {
      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new StringLoader(var1.build(Uri.class, ParcelFileDescriptor.class));
      }

      public void teardown() {
      }
   }

   public static class StreamFactory implements ModelLoaderFactory {
      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new StringLoader(var1.build(Uri.class, InputStream.class));
      }

      public void teardown() {
      }
   }
}
