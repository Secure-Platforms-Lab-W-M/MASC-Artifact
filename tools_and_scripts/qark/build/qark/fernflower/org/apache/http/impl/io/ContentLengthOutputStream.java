package org.apache.http.impl.io;

import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.util.Args;

public class ContentLengthOutputStream extends OutputStream {
   private boolean closed;
   private final long contentLength;
   private final SessionOutputBuffer out;
   private long total;

   public ContentLengthOutputStream(SessionOutputBuffer var1, long var2) {
      this.out = (SessionOutputBuffer)Args.notNull(var1, "Session output buffer");
      this.contentLength = Args.notNegative(var2, "Content length");
   }

   public void close() throws IOException {
      if (!this.closed) {
         this.closed = true;
         this.out.flush();
      }

   }

   public void flush() throws IOException {
      this.out.flush();
   }

   public void write(int var1) throws IOException {
      if (!this.closed) {
         if (this.total < this.contentLength) {
            this.out.write(var1);
            ++this.total;
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
         long var5 = this.total;
         long var7 = this.contentLength;
         if (var5 < var7) {
            var5 = var7 - var5;
            int var4 = var3;
            if ((long)var3 > var5) {
               var4 = (int)var5;
            }

            this.out.write(var1, var2, var4);
            this.total += (long)var4;
         }

      } else {
         throw new IOException("Attempted write to closed stream.");
      }
   }
}
