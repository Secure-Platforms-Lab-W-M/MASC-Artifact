package com.jcraft.jzlib;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class DeflaterOutputStream extends FilterOutputStream {
   protected static final int DEFAULT_BUFSIZE = 512;
   private final byte[] buf1;
   protected byte[] buffer;
   private boolean close_out;
   private boolean closed;
   protected final Deflater deflater;
   protected boolean mydeflater;
   private boolean syncFlush;

   public DeflaterOutputStream(OutputStream var1) throws IOException {
      this(var1, new Deflater(-1), 512, true);
      this.mydeflater = true;
   }

   public DeflaterOutputStream(OutputStream var1, Deflater var2) throws IOException {
      this(var1, var2, 512, true);
   }

   public DeflaterOutputStream(OutputStream var1, Deflater var2, int var3) throws IOException {
      this(var1, var2, var3, true);
   }

   public DeflaterOutputStream(OutputStream var1, Deflater var2, int var3, boolean var4) throws IOException {
      super(var1);
      this.closed = false;
      this.syncFlush = false;
      this.buf1 = new byte[1];
      this.mydeflater = false;
      this.close_out = true;
      if (var1 != null && var2 != null) {
         if (var3 > 0) {
            this.deflater = var2;
            this.buffer = new byte[var3];
            this.close_out = var4;
         } else {
            throw new IllegalArgumentException("buffer size must be greater than 0");
         }
      } else {
         throw null;
      }
   }

   public void close() throws IOException {
      if (!this.closed) {
         this.finish();
         if (this.mydeflater) {
            this.deflater.end();
         }

         if (this.close_out) {
            this.out.close();
         }

         this.closed = true;
      }

   }

   protected int deflate(int var1) throws IOException {
      Deflater var3 = this.deflater;
      byte[] var4 = this.buffer;
      var3.setOutput(var4, 0, var4.length);
      int var2 = this.deflater.deflate(var1);
      if (var2 != -5) {
         if (var2 != 0 && var2 != 1) {
            throw new IOException("failed to deflate");
         }
      } else if (this.deflater.avail_in > 0 || var1 == 4) {
         throw new IOException("failed to deflate");
      }

      var1 = this.deflater.next_out_index;
      if (var1 > 0) {
         this.out.write(this.buffer, 0, var1);
      }

      return var2;
   }

   public void finish() throws IOException {
      while(!this.deflater.finished()) {
         this.deflate(4);
      }

   }

   public void flush() throws IOException {
      int var1;
      if (this.syncFlush && !this.deflater.finished()) {
         do {
            var1 = this.deflate(2);
         } while(this.deflater.next_out_index >= this.buffer.length && var1 != 1);
      }

      this.out.flush();
   }

   public Deflater getDeflater() {
      return this.deflater;
   }

   public boolean getSyncFlush() {
      return this.syncFlush;
   }

   public long getTotalIn() {
      return this.deflater.getTotalIn();
   }

   public long getTotalOut() {
      return this.deflater.getTotalOut();
   }

   public void setSyncFlush(boolean var1) {
      this.syncFlush = var1;
   }

   public void write(int var1) throws IOException {
      byte[] var2 = this.buf1;
      var2[0] = (byte)(var1 & 255);
      this.write(var2, 0, 1);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (!this.deflater.finished()) {
         byte var7 = 0;
         boolean var4;
         if (var2 < 0) {
            var4 = true;
         } else {
            var4 = false;
         }

         boolean var5;
         if (var3 < 0) {
            var5 = true;
         } else {
            var5 = false;
         }

         boolean var6;
         if (var2 + var3 > var1.length) {
            var6 = true;
         } else {
            var6 = false;
         }

         if (!(var4 | var5 | var6)) {
            if (var3 != 0) {
               byte var8 = var7;
               if (this.syncFlush) {
                  var8 = 2;
               }

               this.deflater.setInput(var1, var2, var3, true);

               do {
                  if (this.deflater.avail_in <= 0) {
                     return;
                  }
               } while(this.deflate(var8) != 1);

            }
         } else {
            throw new IndexOutOfBoundsException();
         }
      } else {
         throw new IOException("finished");
      }
   }
}
