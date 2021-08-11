package com.bumptech.glide.load.resource.bitmap;

import android.graphics.Bitmap;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.ResourceDecoder;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.util.ExceptionCatchingInputStream;
import com.bumptech.glide.util.MarkEnforcingInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamBitmapDecoder implements ResourceDecoder {
   private final ArrayPool byteArrayPool;
   private final Downsampler downsampler;

   public StreamBitmapDecoder(Downsampler var1, ArrayPool var2) {
      this.downsampler = var1;
      this.byteArrayPool = var2;
   }

   public Resource decode(InputStream var1, int var2, int var3, Options var4) throws IOException {
      boolean var5;
      RecyclableBufferedInputStream var11;
      if (var1 instanceof RecyclableBufferedInputStream) {
         var11 = (RecyclableBufferedInputStream)var1;
         var5 = false;
      } else {
         var11 = new RecyclableBufferedInputStream(var1, this.byteArrayPool);
         var5 = true;
      }

      ExceptionCatchingInputStream var6 = ExceptionCatchingInputStream.obtain(var11);
      MarkEnforcingInputStream var7 = new MarkEnforcingInputStream(var6);
      StreamBitmapDecoder.UntrustedCallbacks var8 = new StreamBitmapDecoder.UntrustedCallbacks(var11, var6);

      Resource var12;
      try {
         var12 = this.downsampler.decode((InputStream)var7, var2, var3, var4, var8);
      } finally {
         var6.release();
         if (var5) {
            var11.release();
         }

      }

      return var12;
   }

   public boolean handles(InputStream var1, Options var2) {
      return this.downsampler.handles(var1);
   }

   static class UntrustedCallbacks implements Downsampler.DecodeCallbacks {
      private final RecyclableBufferedInputStream bufferedStream;
      private final ExceptionCatchingInputStream exceptionStream;

      UntrustedCallbacks(RecyclableBufferedInputStream var1, ExceptionCatchingInputStream var2) {
         this.bufferedStream = var1;
         this.exceptionStream = var2;
      }

      public void onDecodeComplete(BitmapPool var1, Bitmap var2) throws IOException {
         IOException var3 = this.exceptionStream.getException();
         if (var3 != null) {
            if (var2 != null) {
               var1.put(var2);
            }

            throw var3;
         }
      }

      public void onObtainBounds() {
         this.bufferedStream.fixMarkLimit();
      }
   }
}
