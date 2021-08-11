package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Preconditions;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public final class RoundedCorners extends BitmapTransformation {
   // $FF: renamed from: ID java.lang.String
   private static final String field_67 = "com.bumptech.glide.load.resource.bitmap.RoundedCorners";
   private static final byte[] ID_BYTES;
   private final int roundingRadius;

   static {
      ID_BYTES = "com.bumptech.glide.load.resource.bitmap.RoundedCorners".getBytes(CHARSET);
   }

   public RoundedCorners(int var1) {
      boolean var2;
      if (var1 > 0) {
         var2 = true;
      } else {
         var2 = false;
      }

      Preconditions.checkArgument(var2, "roundingRadius must be greater than 0.");
      this.roundingRadius = var1;
   }

   public boolean equals(Object var1) {
      boolean var3 = var1 instanceof RoundedCorners;
      boolean var2 = false;
      if (var3) {
         RoundedCorners var4 = (RoundedCorners)var1;
         if (this.roundingRadius == var4.roundingRadius) {
            var2 = true;
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Util.hashCode("com.bumptech.glide.load.resource.bitmap.RoundedCorners".hashCode(), Util.hashCode(this.roundingRadius));
   }

   protected Bitmap transform(BitmapPool var1, Bitmap var2, int var3, int var4) {
      return TransformationUtils.roundedCorners(var1, var2, this.roundingRadius);
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      var1.update(ID_BYTES);
      var1.update(ByteBuffer.allocate(4).putInt(this.roundingRadius).array());
   }
}
