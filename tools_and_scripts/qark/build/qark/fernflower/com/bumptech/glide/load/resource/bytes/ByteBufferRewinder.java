package com.bumptech.glide.load.resource.bytes;

import com.bumptech.glide.load.data.DataRewinder;
import java.nio.ByteBuffer;

public class ByteBufferRewinder implements DataRewinder {
   private final ByteBuffer buffer;

   public ByteBufferRewinder(ByteBuffer var1) {
      this.buffer = var1;
   }

   public void cleanup() {
   }

   public ByteBuffer rewindAndGet() {
      this.buffer.position(0);
      return this.buffer;
   }

   public static class Factory implements DataRewinder.Factory {
      public DataRewinder build(ByteBuffer var1) {
         return new ByteBufferRewinder(var1);
      }

      public Class getDataClass() {
         return ByteBuffer.class;
      }
   }
}
