package com.bumptech.glide.load.resource.transcode;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.resource.bytes.BytesResource;
import java.io.ByteArrayOutputStream;

public class BitmapBytesTranscoder implements ResourceTranscoder {
   private final CompressFormat compressFormat;
   private final int quality;

   public BitmapBytesTranscoder() {
      this(CompressFormat.JPEG, 100);
   }

   public BitmapBytesTranscoder(CompressFormat var1, int var2) {
      this.compressFormat = var1;
      this.quality = var2;
   }

   public Resource transcode(Resource var1, Options var2) {
      ByteArrayOutputStream var3 = new ByteArrayOutputStream();
      ((Bitmap)var1.get()).compress(this.compressFormat, this.quality, var3);
      var1.recycle();
      return new BytesResource(var3.toByteArray());
   }
}
