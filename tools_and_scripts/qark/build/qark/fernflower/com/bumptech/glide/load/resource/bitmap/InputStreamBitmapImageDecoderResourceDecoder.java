package com.bumptech.glide.load.resource.bitmap;

import android.graphics.ImageDecoder;
import android.graphics.ImageDecoder.Source;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.util.ByteBufferUtil;
import java.io.IOException;
import java.io.InputStream;

public final class InputStreamBitmapImageDecoderResourceDecoder implements ResourceDecoder {
   private final BitmapImageDecoderResourceDecoder wrapped = new BitmapImageDecoderResourceDecoder();

   public Resource decode(InputStream var1, int var2, int var3, Options var4) throws IOException {
      Source var5 = ImageDecoder.createSource(ByteBufferUtil.fromStream(var1));
      return this.wrapped.decode(var5, var2, var3, var4);
   }

   public boolean handles(InputStream var1, Options var2) throws IOException {
      return true;
   }
}
