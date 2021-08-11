package org.apache.http.impl.io;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.io.SessionOutputBuffer;

public class ChunkedOutputStream extends OutputStream {
   private final byte[] cache;
   private int cachePosition;
   private boolean closed;
   private final SessionOutputBuffer out;
   private boolean wroteLastChunk;

   public ChunkedOutputStream(int var1, SessionOutputBuffer var2) {
      this.cachePosition = 0;
      this.wroteLastChunk = false;
      this.closed = false;
      this.cache = new byte[var1];
      this.out = var2;
   }

   @Deprecated
   public ChunkedOutputStream(SessionOutputBuffer var1) throws IOException {
      this(2048, var1);
   }

   @Deprecated
   public ChunkedOutputStream(SessionOutputBuffer var1, int var2) throws IOException {
      this(var2, var1);
   }

   public void close() throws IOException {
      if (!this.closed) {
         this.closed = true;
         this.finish();
         this.out.flush();
      }

   }

   public void finish() throws IOException {
      if (!this.wroteLastChunk) {
         this.flushCache();
         this.writeClosingChunk();
         this.wroteLastChunk = true;
      }

   }

   public void flush() throws IOException {
      this.flushCache();
      this.out.flush();
   }

   protected void flushCache() throws IOException {
      int var1 = this.cachePosition;
      if (var1 > 0) {
         this.out.writeLine(Integer.toHexString(var1));
         this.out.write(this.cache, 0, this.cachePosition);
         this.out.writeLine("");
         this.cachePosition = 0;
      }

   }

   protected void flushCacheWithAppend(byte[] var1, int var2, int var3) throws IOException {
      this.out.writeLine(Integer.toHexString(this.cachePosition + var3));
      this.out.write(this.cache, 0, this.cachePosition);
      this.out.write(var1, var2, var3);
      this.out.writeLine("");
      this.cachePosition = 0;
   }

   public void write(int var1) throws IOException {
      if (!this.closed) {
         byte[] var3 = this.cache;
         int var2 = this.cachePosition;
         var3[var2] = (byte)var1;
         var1 = var2 + 1;
         this.cachePosition = var1;
         if (var1 == var3.length) {
            this.flushCache();
         }

      } else {
         throw new IOException("Attempted write to closed stream.");
      }
   }

   public void write(byte[] var1) throws IOException {
      this.write(var1, 0, var1.length);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (!this.closed) {
         byte[] var6 = this.cache;
         int var4 = var6.length;
         int var5 = this.cachePosition;
         if (var3 >= var4 - var5) {
            this.flushCacheWithAppend(var1, var2, var3);
         } else {
            System.arraycopy(var1, var2, var6, var5, var3);
            this.cachePosition += var3;
         }
      } else {
         throw new IOException("Attempted write to closed stream.");
      }
   }

   protected void writeClosingChunk() throws IOException {
      this.out.writeLine("0");
      this.out.writeLine("");
   }
}
