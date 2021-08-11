package com.bumptech.glide.load.model;

import android.content.ContentResolver;
import android.net.Uri;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.AssetFileDescriptorLocalUriFetcher;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.data.FileDescriptorLocalUriFetcher;
import com.bumptech.glide.load.data.StreamLocalUriFetcher;
import com.bumptech.glide.signature.ObjectKey;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class UriLoader implements ModelLoader {
   private static final Set SCHEMES = Collections.unmodifiableSet(new HashSet(Arrays.asList("file", "android.resource", "content")));
   private final UriLoader.LocalUriFetcherFactory factory;

   public UriLoader(UriLoader.LocalUriFetcherFactory var1) {
      this.factory = var1;
   }

   public ModelLoader.LoadData buildLoadData(Uri var1, int var2, int var3, Options var4) {
      return new ModelLoader.LoadData(new ObjectKey(var1), this.factory.build(var1));
   }

   public boolean handles(Uri var1) {
      return SCHEMES.contains(var1.getScheme());
   }

   public static final class AssetFileDescriptorFactory implements ModelLoaderFactory, UriLoader.LocalUriFetcherFactory {
      private final ContentResolver contentResolver;

      public AssetFileDescriptorFactory(ContentResolver var1) {
         this.contentResolver = var1;
      }

      public DataFetcher build(Uri var1) {
         return new AssetFileDescriptorLocalUriFetcher(this.contentResolver, var1);
      }

      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new UriLoader(this);
      }

      public void teardown() {
      }
   }

   public static class FileDescriptorFactory implements ModelLoaderFactory, UriLoader.LocalUriFetcherFactory {
      private final ContentResolver contentResolver;

      public FileDescriptorFactory(ContentResolver var1) {
         this.contentResolver = var1;
      }

      public DataFetcher build(Uri var1) {
         return new FileDescriptorLocalUriFetcher(this.contentResolver, var1);
      }

      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new UriLoader(this);
      }

      public void teardown() {
      }
   }

   public interface LocalUriFetcherFactory {
      DataFetcher build(Uri var1);
   }

   public static class StreamFactory implements ModelLoaderFactory, UriLoader.LocalUriFetcherFactory {
      private final ContentResolver contentResolver;

      public StreamFactory(ContentResolver var1) {
         this.contentResolver = var1;
      }

      public DataFetcher build(Uri var1) {
         return new StreamLocalUriFetcher(this.contentResolver, var1);
      }

      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new UriLoader(this);
      }

      public void teardown() {
      }
   }
}
