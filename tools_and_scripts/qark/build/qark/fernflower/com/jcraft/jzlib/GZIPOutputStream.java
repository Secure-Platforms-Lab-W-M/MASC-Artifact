package com.jcraft.jzlib;

import java.io.IOException;
import java.io.OutputStream;

public class GZIPOutputStream extends DeflaterOutputStream {
   public GZIPOutputStream(OutputStream var1) throws IOException {
      this(var1, 512);
   }

   public GZIPOutputStream(OutputStream var1, int var2) throws IOException {
      this(var1, var2, true);
   }

   public GZIPOutputStream(OutputStream var1, int var2, boolean var3) throws IOException {
      this(var1, new Deflater(-1, 31), var2, var3);
      this.mydeflater = true;
   }

   public GZIPOutputStream(OutputStream var1, Deflater var2, int var3, boolean var4) throws IOException {
      super(var1, var2, var3, var4);
   }

   private void check() throws GZIPException {
      if (this.deflater.dstate.status != 42) {
         throw new GZIPException("header is already written.");
      }
   }

   public long getCRC() throws GZIPException {
      if (this.deflater.dstate.status == 666) {
         return this.deflater.dstate.getGZIPHeader().getCRC();
      } else {
         throw new GZIPException("checksum is not calculated yet.");
      }
   }

   public void setComment(String var1) throws GZIPException {
      this.check();
      this.deflater.dstate.getGZIPHeader().setComment(var1);
   }

   public void setModifiedTime(long var1) throws GZIPException {
      this.check();
      this.deflater.dstate.getGZIPHeader().setModifiedTime(var1);
   }

   public void setName(String var1) throws GZIPException {
      this.check();
      this.deflater.dstate.getGZIPHeader().setName(var1);
   }

   public void setOS(int var1) throws GZIPException {
      this.check();
      this.deflater.dstate.getGZIPHeader().setOS(var1);
   }
}
