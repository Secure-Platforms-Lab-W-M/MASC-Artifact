package com.jcraft.jzlib;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

@Deprecated
public class ZInputStream extends FilterInputStream {
   private byte[] buf;
   private byte[] buf1;
   protected boolean compress;
   protected Deflater deflater;
   protected int flush;
   protected InflaterInputStream iis;
   // $FF: renamed from: in java.io.InputStream
   protected InputStream field_234;

   public ZInputStream(InputStream var1) throws IOException {
      this(var1, false);
   }

   public ZInputStream(InputStream var1, int var2) throws IOException {
      super(var1);
      this.flush = 0;
      this.field_234 = null;
      this.buf1 = new byte[1];
      this.buf = new byte[512];
      this.field_234 = var1;
      Deflater var3 = new Deflater();
      this.deflater = var3;
      var3.init(var2);
      this.compress = true;
   }

   public ZInputStream(InputStream var1, boolean var2) throws IOException {
      super(var1);
      this.flush = 0;
      this.field_234 = null;
      this.buf1 = new byte[1];
      this.buf = new byte[512];
      this.iis = new InflaterInputStream(var1, var2);
      this.compress = false;
   }

   public void close() throws IOException {
      if (this.compress) {
         this.deflater.end();
      } else {
         this.iis.close();
      }
   }

   public int getFlushMode() {
      return this.flush;
   }

   public long getTotalIn() {
      return this.compress ? this.deflater.total_in : this.iis.getTotalIn();
   }

   public long getTotalOut() {
      return this.compress ? this.deflater.total_out : this.iis.getTotalOut();
   }

   public int read() throws IOException {
      return this.read(this.buf1, 0, 1) == -1 ? -1 : this.buf1[0] & 255;
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (!this.compress) {
         return this.iis.read(var1, var2, var3);
      } else {
         this.deflater.setOutput(var1, var2, var3);

         do {
            InputStream var5 = this.field_234;
            byte[] var4 = this.buf;
            var2 = var5.read(var4, 0, var4.length);
            if (var2 == -1) {
               return -1;
            }

            this.deflater.setInput(this.buf, 0, var2, true);
            var2 = this.deflater.deflate(this.flush);
            if (this.deflater.next_out_index > 0) {
               return this.deflater.next_out_index;
            }

            if (var2 == 1) {
               return 0;
            }
         } while(var2 != -2 && var2 != -3);

         StringBuilder var6 = new StringBuilder();
         var6.append("deflating: ");
         var6.append(this.deflater.msg);
         throw new ZStreamException(var6.toString());
      }
   }

   public void setFlushMode(int var1) {
      this.flush = var1;
   }

   public long skip(long var1) throws IOException {
      int var3 = 512;
      if (var1 < (long)512) {
         var3 = (int)var1;
      }

      return (long)this.read(new byte[var3]);
   }
}
