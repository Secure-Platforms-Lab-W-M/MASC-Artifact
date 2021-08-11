package com.t4l.jmf;

import java.io.IOException;
import java.io.OutputStream;

class CustomByteArrayOutputStream extends OutputStream {
   int ctr = 0;
   byte[] data;

   public CustomByteArrayOutputStream(byte[] var1) {
      this.data = var1;
   }

   public int getBytesWritten() {
      return this.ctr;
   }

   public void write(int var1) throws IOException {
      byte[] var3 = this.data;
      int var2 = this.ctr;
      var3[var2] = (byte)var1;
      this.ctr = var2 + 1;
   }

   public void write(byte[] var1) throws IOException {
      this.write(var1, 0, var1.length);
   }

   public void write(byte[] var1, int var2, int var3) throws IOException {
      System.arraycopy(this.data, this.ctr, var1, var2, var3);
      this.ctr += var3;
   }
}
