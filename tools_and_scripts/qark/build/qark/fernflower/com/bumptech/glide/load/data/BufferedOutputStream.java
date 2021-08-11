package com.bumptech.glide.load.data;

import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import java.io.IOException;
import java.io.OutputStream;

public final class BufferedOutputStream extends OutputStream {
   private ArrayPool arrayPool;
   private byte[] buffer;
   private int index;
   private final OutputStream out;

   public BufferedOutputStream(OutputStream var1, ArrayPool var2) {
      this(var1, var2, 65536);
   }

   BufferedOutputStream(OutputStream var1, ArrayPool var2, int var3) {
      this.out = var1;
      this.arrayPool = var2;
      this.buffer = (byte[])var2.get(var3, byte[].class);
   }

   private void flushBuffer() throws IOException {
      int var1 = this.index;
      if (var1 > 0) {
         this.out.write(this.buffer, 0, var1);
         this.index = 0;
      }

   }

   private void maybeFlushBuffer() throws IOException {
      if (this.index == this.buffer.length) {
         this.flushBuffer();
      }

   }

   private void release() {
      byte[] var1 = this.buffer;
      if (var1 != null) {
         this.arrayPool.put(var1);
         this.buffer = null;
      }

   }

   public void close() throws IOException {
      try {
         this.flush();
      } finally {
         this.out.close();
      }

      this.release();
   }

   public void flush() throws IOException {
      this.flushBuffer();
      this.out.flush();
   }

   public void write(int var1) throws IOException {
      byte[] var3 = this.buffer;
      int var2 = this.index++;
      var3[var2] = (byte)var1;
      this.maybeFlushBuffer();
   }

   public void write(byte[] var1) throws IOException {
      this.write(var1, 0, var1.length);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      int var4 = 0;

      do {
         int var6 = var3 - var4;
         int var5 = var2 + var4;
         if (this.index == 0 && var6 >= this.buffer.length) {
            this.out.write(var1, var5, var6);
            return;
         }

         var6 = Math.min(var6, this.buffer.length - this.index);
         System.arraycopy(var1, var5, this.buffer, this.index, var6);
         this.index += var6;
         var4 += var6;
         this.maybeFlushBuffer();
      } while(var4 < var3);

   }
}
