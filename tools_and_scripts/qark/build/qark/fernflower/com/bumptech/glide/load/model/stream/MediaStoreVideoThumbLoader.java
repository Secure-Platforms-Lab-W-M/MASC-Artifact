package com.bumptech.glide.load.model.stream;

import android.content.Context;
import android.net.Uri;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.mediastore.MediaStoreUtil;
import com.bumptech.glide.load.data.mediastore.ThumbFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.load.resource.bitmap.VideoDecoder;
import com.bumptech.glide.signature.ObjectKey;

public class MediaStoreVideoThumbLoader implements ModelLoader {
   private final Context context;

   public MediaStoreVideoThumbLoader(Context var1) {
      this.context = var1.getApplicationContext();
   }

   private boolean isRequestingDefaultFrame(Options var1) {
      Long var2 = (Long)var1.get(VideoDecoder.TARGET_FRAME);
      return var2 != null && var2 == -1L;
   }

   public ModelLoader.LoadData buildLoadData(Uri var1, int var2, int var3, Options var4) {
      return MediaStoreUtil.isThumbnailSize(var2, var3) && this.isRequestingDefaultFrame(var4) ? new ModelLoader.LoadData(new ObjectKey(var1), ThumbFetcher.buildVideoFetcher(this.context, var1)) : null;
   }

   public boolean handles(Uri var1) {
      return MediaStoreUtil.isMediaStoreVideoUri(var1);
   }

   public static class Factory implements ModelLoaderFactory {
      private final Context context;

      public Factory(Context var1) {
         this.context = var1;
      }

      public ModelLoader build(MultiModelLoaderFactory var1) {
         return new MediaStoreVideoThumbLoader(this.context);
      }

      public void teardown() {
      }
   }
}
