package org.apache.http.client.entity;

import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;
import java.util.zip.ZipException;

public class DeflateInputStream extends InputStream {
   private final InputStream sourceStream;

   public DeflateInputStream(InputStream var1) throws IOException {
      PushbackInputStream var6 = new PushbackInputStream(var1, 2);
      int var3 = var6.read();
      int var2 = var6.read();
      if (var3 != -1 && var2 != -1) {
         var6.unread(var2);
         var6.unread(var3);
         boolean var5 = true;
         var3 &= 255;
         boolean var4 = var5;
         if ((var3 & 15) == 8) {
            var4 = var5;
            if ((var3 >> 4 & 15) <= 7) {
               var4 = var5;
               if ((var3 << 8 | var2 & 255) % 31 == 0) {
                  var4 = false;
               }
            }
         }

         this.sourceStream = new DeflateInputStream.DeflateStream(var6, new Inflater(var4));
      } else {
         throw new ZipException("Unexpected end of stream");
      }
   }

   public int available() throws IOException {
      return this.sourceStream.available();
   }

   public void close() throws IOException {
      this.sourceStream.close();
   }

   public void mark(int var1) {
      this.sourceStream.mark(var1);
   }

   public boolean markSupported() {
      return this.sourceStream.markSupported();
   }

   public int read() throws IOException {
      return this.sourceStream.read();
   }

   public int read(byte[] var1) throws IOException {
      return this.sourceStream.read(var1);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      return this.sourceStream.read(var1, var2, var3);
   }

   public void reset() throws IOException {
      this.sourceStream.reset();
   }

   public long skip(long var1) throws IOException {
      return this.sourceStream.skip(var1);
   }

   static class DeflateStream extends InflaterInputStream {
      private boolean closed = false;

      public DeflateStream(InputStream var1, Inflater var2) {
         super(var1, var2);
      }

      public void close() throws IOException {
         if (!this.closed) {
            this.closed = true;
            this.inf.end();
            super.close();
         }
      }
   }
}
