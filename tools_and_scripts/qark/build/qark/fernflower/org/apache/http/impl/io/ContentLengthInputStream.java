package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InputStream;
import org.apache.http.ConnectionClosedException;
import org.apache.http.io.BufferInfo;
import org.apache.http.io.SessionInputBuffer;
import org.apache.http.util.Args;

public class ContentLengthInputStream extends InputStream {
   private static final int BUFFER_SIZE = 2048;
   private boolean closed = false;
   private final long contentLength;
   // $FF: renamed from: in org.apache.http.io.SessionInputBuffer
   private SessionInputBuffer field_206 = null;
   private long pos = 0L;

   public ContentLengthInputStream(SessionInputBuffer var1, long var2) {
      this.field_206 = (SessionInputBuffer)Args.notNull(var1, "Session input buffer");
      this.contentLength = Args.notNegative(var2, "Content length");
   }

   public int available() throws IOException {
      SessionInputBuffer var1 = this.field_206;
      return var1 instanceof BufferInfo ? Math.min(((BufferInfo)var1).length(), (int)(this.contentLength - this.pos)) : 0;
   }

   public void close() throws IOException {
      if (!this.closed) {
         label88: {
            Throwable var10000;
            label87: {
               boolean var10001;
               byte[] var2;
               try {
                  if (this.pos >= this.contentLength) {
                     break label88;
                  }

                  var2 = new byte[2048];
               } catch (Throwable var8) {
                  var10000 = var8;
                  var10001 = false;
                  break label87;
               }

               while(true) {
                  int var1;
                  try {
                     var1 = this.read(var2);
                  } catch (Throwable var7) {
                     var10000 = var7;
                     var10001 = false;
                     break;
                  }

                  if (var1 < 0) {
                     break label88;
                  }
               }
            }

            Throwable var9 = var10000;
            this.closed = true;
            throw var9;
         }

         this.closed = true;
      }
   }

   public int read() throws IOException {
      if (!this.closed) {
         if (this.pos >= this.contentLength) {
            return -1;
         } else {
            int var1 = this.field_206.read();
            if (var1 == -1) {
               if (this.pos >= this.contentLength) {
                  return var1;
               } else {
                  throw new ConnectionClosedException("Premature end of Content-Length delimited message body (expected: %,d; received: %,d)", new Object[]{this.contentLength, this.pos});
               }
            } else {
               ++this.pos;
               return var1;
            }
         }
      } else {
         throw new IOException("Attempted read from closed stream.");
      }
   }

   public int read(byte[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      if (!this.closed) {
         long var5 = this.pos;
         long var7 = this.contentLength;
         if (var5 >= var7) {
            return -1;
         } else {
            int var4 = var3;
            if ((long)var3 + var5 > var7) {
               var4 = (int)(var7 - var5);
            }

            var2 = this.field_206.read(var1, var2, var4);
            if (var2 == -1 && this.pos < this.contentLength) {
               throw new ConnectionClosedException("Premature end of Content-Length delimited message body (expected: %,d; received: %,d)", new Object[]{this.contentLength, this.pos});
            } else {
               if (var2 > 0) {
                  this.pos += (long)var2;
               }

               return var2;
            }
         }
      } else {
         throw new IOException("Attempted read from closed stream.");
      }
   }

   public long skip(long var1) throws IOException {
      if (var1 <= 0L) {
         return 0L;
      } else {
         byte[] var6 = new byte[2048];
         var1 = Math.min(var1, this.contentLength - this.pos);

         int var3;
         long var4;
         for(var4 = 0L; var1 > 0L; var1 -= (long)var3) {
            var3 = this.read(var6, 0, (int)Math.min(2048L, var1));
            if (var3 == -1) {
               return var4;
            }

            var4 += (long)var3;
         }

         return var4;
      }
   }
}
