package com.bumptech.glide.load.resource.gif;

import android.util.Log;
import com.bumptech.glide.load.EncodeStrategy;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceEncoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.File;
import java.io.IOException;

public class GifDrawableEncoder implements ResourceEncoder {
   private static final String TAG = "GifEncoder";

   public boolean encode(Resource var1, File var2, Options var3) {
      GifDrawable var5 = (GifDrawable)var1.get();

      try {
         ByteBufferUtil.toFile(var5.getBuffer(), var2);
         return true;
      } catch (IOException var4) {
         if (Log.isLoggable("GifEncoder", 5)) {
            Log.w("GifEncoder", "Failed to encode GIF drawable data", var4);
         }

         return false;
      }
   }

   public EncodeStrategy getEncodeStrategy(Options var1) {
      return EncodeStrategy.SOURCE;
   }
}
