package com.jcraft.jzlib;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@Deprecated
public class ZOutputStream extends FilterOutputStream {
   protected byte[] buf;
   private byte[] buf1;
   protected int bufsize;
   protected boolean compress;
   private DeflaterOutputStream dos;
   private boolean end;
   protected int flush;
   private Inflater inflater;
   protected OutputStream out;

   public ZOutputStream(OutputStream var1) throws IOException {
      super(var1);
      this.bufsize = 512;
      this.flush = 0;
      this.buf = new byte[512];
      this.end = false;
      this.buf1 = new byte[1];
      this.out = var1;
      Inflater var2 = new Inflater();
      this.inflater = var2;
      var2.init();
      this.compress = false;
   }

   public ZOutputStream(OutputStream var1, int var2) throws IOException {
      this(var1, var2, false);
   }

   public ZOutputStream(OutputStream var1, int var2, boolean var3) throws IOException {
      super(var1);
      this.bufsize = 512;
      this.flush = 0;
      this.buf = new byte[512];
      this.end = false;
      this.buf1 = new byte[1];
      this.out = var1;
      this.dos = new DeflaterOutputStream(var1, new Deflater(var2, var3));
      this.compress = true;
   }

   public void close() throws IOException {
      try {
         this.finish();
      } catch (IOException var4) {
      } finally {
         this.end();
         this.out.close();
         this.out = null;
      }

   }

   public void end() {
      // $FF: Couldn't be decompiled
   }

   public void finish() throws IOException {
      if (this.compress) {
         int var1 = this.flush;

         try {
            this.write("".getBytes(), 0, 0);
         } finally {
            ;
         }
      } else {
         this.dos.finish();
      }

      this.flush();
   }

   public void flush() throws IOException {
      this.out.flush();
   }

   public int getFlushMode() {
      return this.flush;
   }

   public long getTotalIn() {
      return this.compress ? this.dos.getTotalIn() : this.inflater.total_in;
   }

   public long getTotalOut() {
      return this.compress ? this.dos.getTotalOut() : this.inflater.total_out;
   }

   public void setFlushMode(int var1) {
      this.flush = var1;
   }

   public void write(int var1) throws IOException {
      byte[] var2 = this.buf1;
      var2[0] = (byte)var1;
      this.write(var2, 0, 1);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      if (var3 != 0) {
         if (this.compress) {
            this.dos.write(var1, var2, var3);
         } else {
            this.inflater.setInput(var1, var2, var3, true);
            var2 = 0;

            while(this.inflater.avail_in > 0) {
               Inflater var5 = this.inflater;
               byte[] var4 = this.buf;
               var5.setOutput(var4, 0, var4.length);
               var3 = this.inflater.inflate(this.flush);
               if (this.inflater.next_out_index > 0) {
                  this.out.write(this.buf, 0, this.inflater.next_out_index);
               }

               var2 = var3;
               if (var3 != 0) {
                  var2 = var3;
                  break;
               }
            }

            if (var2 != 0) {
               StringBuilder var6 = new StringBuilder();
               var6.append("inflating: ");
               var6.append(this.inflater.msg);
               throw new ZStreamException(var6.toString());
            }
         }
      }
   }
}
