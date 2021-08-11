package com.bumptech.glide.load.resource.bitmap;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class ByteBufferBitmapDecoder implements ResourceDecoder {
   private final Downsampler downsampler;

   public ByteBufferBitmapDecoder(Downsampler var1) {
      this.downsampler = var1;
   }

   public Resource decode(ByteBuffer var1, int var2, int var3, Options var4) throws IOException {
      InputStream var5 = ByteBufferUtil.toStream(var1);
      return this.downsampler.decode(var5, var2, var3, var4);
   }

   public boolean handles(ByteBuffer var1, Options var2) {
      return this.downsampler.handles(var1);
   }
}
