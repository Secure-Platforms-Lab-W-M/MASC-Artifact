package com.bumptech.glide.load.model;

import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.bumptech.glide.load.Options;
import java.io.InputStream;

public class ResourceLoader implements ModelLoader {
   private static final String TAG = "ResourceLoader";
   private final Resources resources;
   private final ModelLoader uriLoader;

   public ResourceLoader(Resources var1, ModelLoader var2) {
      this.resources = var1;
      this.uriLoader = var2;
   }

   private Uri getResourceUri(Integer var1) {
      try {
         StringBuilder var2 = new StringBuilder();
         var2.append("android.resource://");
         var2.append(this.resources.getResourcePackageName(var1));
         var2.append('/');
         var2.append(this.resources.getResourceTypeName(var1));
         var2.append('/');
         var2.append(this.resources.getResourceEntryName(var1));
         Uri var5 = Uri.parse(var2.toString());
         return var5;
      } catch (NotFoundException var4) {
         if (Log.isLoggable("ResourceLoader", 5)) {
            StringBuilder var3 = new StringBuilder();
            var3.append("Received invalid resource id: ");
            var3.append(var1);
            Log.w("ResourceLoader", var3.toString(), var4);
         }

         return null;
      }
   }

   public ModelLoader.LoadData buildLoadData(Integer var1, int var2, int var3, Options var4) {
      Uri var5 = this.getResourceUri(var1);
      return var5 == null ? null : this.uriLoader.buildLoadData(var5, var2, var3, var4);
   }

   public boolean handles(Integer var1) {
      return true;
   }

   public static final class AssetFileDescriptorFactory implements ModelLoaderFactory {
      private final Resources resources;

      public AssetFileDescriptorFactory(Resources var1) {
         this.resources = var1;
      }

      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new ResourceLoader(this.resources, var1.build(Uri.class, AssetFileDescriptor.class));
      }

      public void teardown() {
      }
   }

   public static class FileDescriptorFactory implements ModelLoaderFactory {
      private final Resources resources;

      public FileDescriptorFactory(Resources var1) {
         this.resources = var1;
      }

      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new ResourceLoader(this.resources, var1.build(Uri.class, ParcelFileDescriptor.class));
      }

      public void teardown() {
      }
   }

   public static class StreamFactory implements ModelLoaderFactory {
      private final Resources resources;

      public StreamFactory(Resources var1) {
         this.resources = var1;
      }

      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new ResourceLoader(this.resources, var1.build(Uri.class, InputStream.class));
      }

      public void teardown() {
      }
   }

   public static class UriFactory implements ModelLoaderFactory {
      private final Resources resources;

      public UriFactory(Resources var1) {
         this.resources = var1;
      }

      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new ResourceLoader(this.resources, UnitModelLoader.getInstance());
      }

      public void teardown() {
      }
   }
}
