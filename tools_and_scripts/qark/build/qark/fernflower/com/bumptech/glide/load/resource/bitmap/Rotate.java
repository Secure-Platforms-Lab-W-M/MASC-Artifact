package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public class Rotate extends BitmapTransformation {
   // $FF: renamed from: ID java.lang.String
   private static final String field_69 = "com.bumptech.glide.load.resource.bitmap.Rotate";
   private static final byte[] ID_BYTES;
   private final int degreesToRotate;

   static {
      ID_BYTES = "com.bumptech.glide.load.resource.bitmap.Rotate".getBytes(CHARSET);
   }

   public Rotate(int var1) {
      this.degreesToRotate = var1;
   }

   public boolean equals(Object var1) {
      boolean var3 = var1 instanceof Rotate;
      boolean var2 = false;
      if (var3) {
         Rotate var4 = (Rotate)var1;
         if (this.degreesToRotate == var4.degreesToRotate) {
            var2 = true;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Util.hashCode("com.bumptech.glide.load.resource.bitmap.Rotate".hashCode(), Util.hashCode(this.degreesToRotate));
   }

   protected Bitmap transform(BitmapPool var1, Bitmap var2, int var3, int var4) {
      return TransformationUtils.rotateImage(var2, this.degreesToRotate);
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      var1.update(ID_BYTES);
      var1.update(ByteBuffer.allocate(4).putInt(this.degreesToRotate).array());
   }
}
