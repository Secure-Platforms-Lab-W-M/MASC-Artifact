package org.apache.commons.codec.binary;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class BaseNCodecInputStream extends FilterInputStream {
   private final BaseNCodec baseNCodec;
   private final BaseNCodec.Context context = new BaseNCodec.Context();
   private final boolean doEncode;
   private final byte[] singleByte = new byte[1];

   protected BaseNCodecInputStream(InputStream var1, BaseNCodec var2, boolean var3) {
      super(var1);
      this.doEncode = var3;
      this.baseNCodec = var2;
   }

   public int available() throws IOException {
      return this.context.eof ^ 1;
   }

   public void mark(int var1) {
      synchronized(this){}
   }

   public boolean markSupported() {
      return false;
   }

   public int read() throws IOException {
      int var1;
      for(var1 = this.read(this.singleByte, 0, 1); var1 == 0; var1 = this.read(this.singleByte, 0, 1)) {
      }

      if (var1 > 0) {
         byte var2 = this.singleByte[0];
         return var2 < 0 ? var2 + 256 : var2;
      } else {
         return -1;
      }
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      Objects.requireNonNull(var1, "array");
      if (var2 >= 0 && var3 >= 0) {
         if (var2 <= var1.length && var2 + var3 <= var1.length) {
            if (var3 == 0) {
               return 0;
            } else {
               int var4;
               for(var4 = 0; var4 == 0; var4 = this.baseNCodec.readResults(var1, var2, var3, this.context)) {
                  if (!this.baseNCodec.hasData(this.context)) {
                     short var6;
                     if (this.doEncode) {
                        var6 = 4096;
                     } else {
                        var6 = 8192;
                     }

                     byte[] var5 = new byte[var6];
                     var4 = this.in.read(var5);
                     if (this.doEncode) {
                        this.baseNCodec.encode(var5, 0, var4, this.context);
                     } else {
                        this.baseNCodec.decode(var5, 0, var4, this.context);
                     }
                  }
               }

               return var4;
            }
         } else {
            throw new IndexOutOfBoundsException();
         }
      } else {
         throw new IndexOutOfBoundsException();
      }
   }

   public void reset() throws IOException {
      synchronized(this){}

      try {
         throw new IOException("mark/reset not supported");
      } finally {
         ;
      }
   }

   public long skip(long var1) throws IOException {
      if (var1 < 0L) {
         StringBuilder var7 = new StringBuilder();
         var7.append("Negative skip length: ");
         var7.append(var1);
         throw new IllegalArgumentException(var7.toString());
      } else {
         byte[] var6 = new byte[512];

         int var3;
         long var4;
         for(var4 = var1; var4 > 0L; var4 -= (long)var3) {
            var3 = this.read(var6, 0, (int)Math.min((long)var6.length, var4));
            if (var3 == -1) {
               break;
            }
         }

         return var1 - var4;
      }
   }
}
