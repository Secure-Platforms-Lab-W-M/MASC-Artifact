package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import java.security.MessageDigest;

public class CenterCrop extends BitmapTransformation {
   // $FF: renamed from: ID java.lang.String
   private static final String field_68 = "com.bumptech.glide.load.resource.bitmap.CenterCrop";
   private static final byte[] ID_BYTES;

   static {
      ID_BYTES = "com.bumptech.glide.load.resource.bitmap.CenterCrop".getBytes(CHARSET);
   }

   public boolean equals(Object var1) {
      return var1 instanceof CenterCrop;
   }

   public int hashCode() {
      return "com.bumptech.glide.load.resource.bitmap.CenterCrop".hashCode();
   }

   protected Bitmap transform(BitmapPool var1, Bitmap var2, int var3, int var4) {
      return TransformationUtils.centerCrop(var1, var2, var3, var4);
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      var1.update(ID_BYTES);
   }
}
