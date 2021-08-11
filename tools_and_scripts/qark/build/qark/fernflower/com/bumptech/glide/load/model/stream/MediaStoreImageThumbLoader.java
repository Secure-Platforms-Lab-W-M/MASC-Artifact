package com.bumptech.glide.load.model.stream;

import android.content.Context;
import android.net.Uri;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.mediastore.MediaStoreUtil;
import com.bumptech.glide.load.data.mediastore.ThumbFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;

public class MediaStoreImageThumbLoader implements ModelLoader {
   private final Context context;

   public MediaStoreImageThumbLoader(Context var1) {
      this.context = var1.getApplicationContext();
   }

   public ModelLoader.LoadData buildLoadData(Uri var1, int var2, int var3, Options var4) {
      return MediaStoreUtil.isThumbnailSize(var2, var3) ? new ModelLoader.LoadData(new ObjectKey(var1), ThumbFetcher.buildImageFetcher(this.context, var1)) : null;
   }

   public boolean handles(Uri var1) {
      return MediaStoreUtil.isMediaStoreImageUri(var1);
   }

   public static class Factory implements ModelLoaderFactory {
      private final Context context;

      public Factory(Context var1) {
         this.context = var1;
      }

      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new MediaStoreImageThumbLoader(this.context);
      }

      public void teardown() {
      }
   }
}
