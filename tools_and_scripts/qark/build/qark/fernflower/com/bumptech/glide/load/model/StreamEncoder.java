package com.bumptech.glide.load.model;

import com.bumptech.glide.load.Encoder;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.File;
import java.io.InputStream;

public class StreamEncoder implements Encoder {
   private static final String TAG = "StreamEncoder";
   private final ArrayPool byteArrayPool;

   public StreamEncoder(ArrayPool var1) {
      this.byteArrayPool = var1;
   }

   public boolean encode(InputStream param1, File param2, Options param3) {
      // $FF: Couldn't be decompiled
   }
}
