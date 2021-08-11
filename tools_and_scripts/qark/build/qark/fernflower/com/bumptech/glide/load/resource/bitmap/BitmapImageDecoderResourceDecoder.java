package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.ImageDecoder.OnHeaderDecodedListener;
import android.graphics.ImageDecoder.Source;
import android.util.Log;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPoolAdapter;
import com.bumptech.glide.load.resource.ImageDecoderResourceDecoder;
import java.io.IOException;

public final class BitmapImageDecoderResourceDecoder extends ImageDecoderResourceDecoder {
   private static final String TAG = "BitmapImageDecoder";
   private final BitmapPool bitmapPool = new BitmapPoolAdapter();

   protected Resource decode(Source var1, int var2, int var3, OnHeaderDecodedListener var4) throws IOException {
      Bitmap var5 = ImageDecoder.decodeBitmap(var1, var4);
      if (Log.isLoggable("BitmapImageDecoder", 2)) {
         StringBuilder var6 = new StringBuilder();
         var6.append("Decoded [");
         var6.append(var5.getWidth());
         var6.append("x");
         var6.append(var5.getHeight());
         var6.append("] for [");
         var6.append(var2);
         var6.append("x");
         var6.append(var3);
         var6.append("]");
         Log.v("BitmapImageDecoder", var6.toString());
      }

      return new BitmapResource(var5, this.bitmapPool);
   }
}
