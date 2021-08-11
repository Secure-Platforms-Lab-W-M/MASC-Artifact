package com.bumptech.glide.load.data;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.load.resource.bitmap.RecyclableBufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class InputStreamRewinder implements DataRewinder {
   private static final int MARK_READ_LIMIT = 5242880;
   private final RecyclableBufferedInputStream bufferedStream;

   public InputStreamRewinder(InputStream var1, ArrayPool var2) {
      RecyclableBufferedInputStream var3 = new RecyclableBufferedInputStream(var1, var2);
      this.bufferedStream = var3;
      var3.mark(5242880);
   }

   public void cleanup() {
      this.bufferedStream.release();
   }

   public void fixMarkLimits() {
      this.bufferedStream.fixMarkLimit();
   }

   public InputStream rewindAndGet() throws IOException {
      this.bufferedStream.reset();
      return this.bufferedStream;
   }

   public static final class Factory implements DataRewinder.Factory {
      private final ArrayPool byteArrayPool;

      public Factory(ArrayPool var1) {
         this.byteArrayPool = var1;
      }

      public DataRewinder build(InputStream var1) {
         return new InputStreamRewinder(var1, this.byteArrayPool);
      }

      public Class getDataClass() {
         return InputStream.class;
      }
   }
}
