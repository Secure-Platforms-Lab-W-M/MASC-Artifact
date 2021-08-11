package com.jcraft.jzlib;

import java.io.IOException;
import java.io.InputStream;

public class GZIPInputStream extends InflaterInputStream {
   public GZIPInputStream(InputStream var1) throws IOException {
      this(var1, 512, true);
   }

   public GZIPInputStream(InputStream var1, int var2, boolean var3) throws IOException {
      this(var1, new Inflater(31), var2, var3);
      this.myinflater = true;
   }

   public GZIPInputStream(InputStream var1, Inflater var2, int var3, boolean var4) throws IOException {
      super(var1, var2, var3, var4);
   }

   private int fill(byte[] var1) {
      int var5 = var1.length;
      int var2 = 0;

      int var3;
      do {
         var3 = -1;

         label21: {
            int var4;
            try {
               var4 = this.in.read(var1, var2, var1.length - var2);
            } catch (IOException var7) {
               break label21;
            }

            var3 = var4;
         }

         if (var3 == -1) {
            return var2;
         }

         var3 += var2;
         var2 = var3;
      } while(var3 < var5);

      return var3;
   }

   public long getCRC() throws GZIPException {
      if (this.inflater.istate.mode == 12) {
         return this.inflater.istate.getGZIPHeader().getCRC();
      } else {
         throw new GZIPException("checksum is not calculated yet.");
      }
   }

   public String getComment() {
      return this.inflater.istate.getGZIPHeader().getComment();
   }

   public long getModifiedtime() {
      return this.inflater.istate.getGZIPHeader().getModifiedTime();
   }

   public String getName() {
      return this.inflater.istate.getGZIPHeader().getName();
   }

   public int getOS() {
      return this.inflater.istate.getGZIPHeader().getOS();
   }

   public void readHeader() throws IOException {
      byte[] var2 = "".getBytes();
      this.inflater.setOutput(var2, 0, 0);
      this.inflater.setInput(var2, 0, 0, false);
      var2 = new byte[10];
      int var1 = this.fill(var2);
      if (var1 != 10) {
         if (var1 > 0) {
            this.inflater.setInput(var2, 0, var1, false);
            this.inflater.next_in_index = 0;
            this.inflater.avail_in = var1;
         }

         throw new IOException("no input");
      } else {
         this.inflater.setInput(var2, 0, var1, false);
         var2 = new byte[1];

         do {
            if (this.inflater.avail_in <= 0) {
               if (this.in.read(var2) <= 0) {
                  throw new IOException("no input");
               }

               this.inflater.setInput(var2, 0, 1, true);
            }

            if (this.inflater.inflate(0) != 0) {
               var1 = 2048 - this.inflater.next_in.length;
               if (var1 > 0) {
                  var2 = new byte[var1];
                  var1 = this.fill(var2);
                  if (var1 > 0) {
                     Inflater var3 = this.inflater;
                     var3.avail_in += this.inflater.next_in_index;
                     this.inflater.next_in_index = 0;
                     this.inflater.setInput(var2, 0, var1, true);
                  }
               }

               Inflater var4 = this.inflater;
               var4.avail_in += this.inflater.next_in_index;
               this.inflater.next_in_index = 0;
               throw new IOException(this.inflater.msg);
            }
         } while(this.inflater.istate.inParsingHeader());

      }
   }
}
