package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.Util;
import java.nio.ByteBuffer;
import java.security.MessageDigest;

public final class GranularRoundedCorners extends BitmapTransformation {
   // $FF: renamed from: ID java.lang.String
   private static final String field_70 = "com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners";
   private static final byte[] ID_BYTES;
   private final float bottomLeft;
   private final float bottomRight;
   private final float topLeft;
   private final float topRight;

   static {
      ID_BYTES = "com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners".getBytes(CHARSET);
   }

   public GranularRoundedCorners(float var1, float var2, float var3, float var4) {
      this.topLeft = var1;
      this.topRight = var2;
      this.bottomRight = var3;
      this.bottomLeft = var4;
   }

   public boolean equals(Object var1) {
      boolean var2 = var1 instanceof GranularRoundedCorners;
      boolean var3 = false;
      if (var2) {
         GranularRoundedCorners var4 = (GranularRoundedCorners)var1;
         var2 = var3;
         if (this.topLeft == var4.topLeft) {
            var2 = var3;
            if (this.topRight == var4.topRight) {
               var2 = var3;
               if (this.bottomRight == var4.bottomRight) {
                  var2 = var3;
                  if (this.bottomLeft == var4.bottomLeft) {
                     var2 = true;
                  }
               }
            }
         }

         return var2;
      } else {
         return false;
      }
   }

   public int hashCode() {
      int var1 = Util.hashCode("com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners".hashCode(), Util.hashCode(this.topLeft));
      var1 = Util.hashCode(this.topRight, var1);
      var1 = Util.hashCode(this.bottomRight, var1);
      return Util.hashCode(this.bottomLeft, var1);
   }

   protected Bitmap transform(BitmapPool var1, Bitmap var2, int var3, int var4) {
      return TransformationUtils.roundedCorners(var1, var2, this.topLeft, this.topRight, this.bottomRight, this.bottomLeft);
   }

   public void updateDiskCacheKey(MessageDigest var1) {
      var1.update(ID_BYTES);
      var1.update(ByteBuffer.allocate(16).putFloat(this.topLeft).putFloat(this.topRight).putFloat(this.bottomRight).putFloat(this.bottomLeft).array());
   }
}
