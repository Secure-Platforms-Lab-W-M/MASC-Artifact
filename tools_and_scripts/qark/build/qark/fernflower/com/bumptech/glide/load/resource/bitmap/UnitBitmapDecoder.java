package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.Util;

public final class UnitBitmapDecoder implements ResourceDecoder {
   public Resource decode(Bitmap var1, int var2, int var3, Options var4) {
      return new UnitBitmapDecoder.NonOwnedBitmapResource(var1);
   }

   public boolean handles(Bitmap var1, Options var2) {
      return true;
   }

   private static final class NonOwnedBitmapResource implements Resource {
      private final Bitmap bitmap;

      NonOwnedBitmapResource(Bitmap var1) {
         this.bitmap = var1;
      }

      public Bitmap get() {
         return this.bitmap;
      }

      public Class getResourceClass() {
         return Bitmap.class;
      }

      public int getSize() {
         return Util.getBitmapByteSize(this.bitmap);
      }

      public void recycle() {
      }
   }
}
