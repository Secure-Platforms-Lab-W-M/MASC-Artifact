package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Option;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.File;

public class BitmapEncoder implements ResourceEncoder {
   public static final Option COMPRESSION_FORMAT = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionFormat");
   public static final Option COMPRESSION_QUALITY = Option.memory("com.bumptech.glide.load.resource.bitmap.BitmapEncoder.CompressionQuality", 90);
   private static final String TAG = "BitmapEncoder";
   private final ArrayPool arrayPool;

   @Deprecated
   public BitmapEncoder() {
      this.arrayPool = null;
   }

   public BitmapEncoder(ArrayPool var1) {
      this.arrayPool = var1;
   }

   private CompressFormat getFormat(Bitmap var1, Options var2) {
      CompressFormat var3 = (CompressFormat)var2.get(COMPRESSION_FORMAT);
      if (var3 != null) {
         return var3;
      } else {
         return var1.hasAlpha() ? CompressFormat.PNG : CompressFormat.JPEG;
      }
   }

   public boolean encode(Resource param1, File param2, Options param3) {
      // $FF: Couldn't be decompiled
   }

   public EncodeStrategy getEncodeStrategy(Options var1) {
      return EncodeStrategy.TRANSFORMED;
   }
}
