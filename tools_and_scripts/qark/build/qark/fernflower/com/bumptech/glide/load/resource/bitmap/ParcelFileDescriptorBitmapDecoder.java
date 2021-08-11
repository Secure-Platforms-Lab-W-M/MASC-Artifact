package com.bumptech.glide.load.resource.bitmap;

import android.os.ParcelFileDescriptor;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import java.io.IOException;

public final class ParcelFileDescriptorBitmapDecoder implements ResourceDecoder {
   private final Downsampler downsampler;

   public ParcelFileDescriptorBitmapDecoder(Downsampler var1) {
      this.downsampler = var1;
   }

   public Resource decode(ParcelFileDescriptor var1, int var2, int var3, Options var4) throws IOException {
      return this.downsampler.decode(var1, var2, var3, var4);
   }

   public boolean handles(ParcelFileDescriptor var1, Options var2) {
      return this.downsampler.handles(var1);
   }
}
