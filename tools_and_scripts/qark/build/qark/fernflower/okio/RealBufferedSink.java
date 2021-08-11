package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

final class RealBufferedSink implements BufferedSink {
   public final Buffer buffer = new Buffer();
   boolean closed;
   public final Sink sink;

   RealBufferedSink(Sink var1) {
      if (var1 != null) {
         this.sink = var1;
      } else {
         throw new NullPointerException("sink == null");
      }
   }

   public Buffer buffer() {
      return this.buffer;
   }

   public void close() throws IOException {
      if (!this.closed) {
         Object var1 = null;

         label98:
         try {
            if (this.buffer.size > 0L) {
               this.sink.write(this.buffer, this.buffer.size);
            }
         } finally {
            break label98;
         }

         Throwable var2;
         label95: {
            try {
               this.sink.close();
            } catch (Throwable var8) {
               var2 = (Throwable)var1;
               if (var1 == null) {
                  var2 = var8;
               }
               break label95;
            }

            var2 = (Throwable)var1;
         }

         this.closed = true;
         if (var2 != null) {
            Util.sneakyRethrow(var2);
         }

      }
   }

   public BufferedSink emit() throws IOException {
      if (!this.closed) {
         long var1 = this.buffer.size();
         if (var1 > 0L) {
            this.sink.write(this.buffer, var1);
         }

         return this;
      } else {
         throw new IllegalStateException("closed");
      }
   }

   public BufferedSink emitCompleteSegments() throws IOException {
      if (!this.closed) {
         long var1 = this.buffer.completeSegmentByteCount();
         if (var1 > 0L) {
            this.sink.write(this.buffer, var1);
         }

         return this;
      } else {
         throw new IllegalStateException("closed");
      }
   }

   public void flush() throws IOException {
      if (!this.closed) {
         if (this.buffer.size > 0L) {
            Sink var1 = this.sink;
            Buffer var2 = this.buffer;
            var1.write(var2, var2.size);
         }

         this.sink.flush();
      } else {
         throw new IllegalStateException("closed");
      }
   }

   public OutputStream outputStream() {
      return new OutputStream() {
         public void close() throws IOException {
            RealBufferedSink.this.close();
         }

         public void flush() throws IOException {
            if (!RealBufferedSink.this.closed) {
               RealBufferedSink.this.flush();
            }

         }

         public String toString() {
            StringBuilder var1 = new StringBuilder();
            var1.append(RealBufferedSink.this);
            var1.append(".outputStream()");
            return var1.toString();
         }

         public void write(int var1) throws IOException {
            if (!RealBufferedSink.this.closed) {
               RealBufferedSink.this.buffer.writeByte((byte)var1);
               RealBufferedSink.this.emitCompleteSegments();
            } else {
               throw new IOException("closed");
            }
         }

         public void write(byte[] var1, int var2, int var3) throws IOException {
            if (!RealBufferedSink.this.closed) {
               RealBufferedSink.this.buffer.write(var1, var2, var3);
               RealBufferedSink.this.emitCompleteSegments();
            } else {
               throw new IOException("closed");
            }
         }
      };
   }

   public Timeout timeout() {
      return this.sink.timeout();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("buffer(");
      var1.append(this.sink);
      var1.append(")");
      return var1.toString();
   }

   public BufferedSink write(ByteString var1) throws IOException {
      if (!this.closed) {
         this.buffer.write(var1);
         return this.emitCompleteSegments();
      } else {
         throw new IllegalStateException("closed");
      }
   }

   public BufferedSink write(Source var1, long var2) throws IOException {
      while(true) {
         if (var2 > 0L) {
            long var4 = var1.read(this.buffer, var2);
            if (var4 != -1L) {
               var2 -= var4;
               this.emitCompleteSegments();
               continue;
            }

            throw new EOFException();
         }

         return this;
      }
   }

   public BufferedSink write(byte[] var1) throws IOException {
      if (!this.closed) {
         this.buffer.write(var1);
         return this.emitCompleteSegments();
      } else {
         throw new IllegalStateException("closed");
      }
   }

   public BufferedSink write(byte[] var1, int var2, int var3) throws IOException {
      if (!this.closed) {
         this.buffer.write(var1, var2, var3);
         return this.emitCompleteSegments();
      } else {
         throw new IllegalStateException("closed");
      }
   }

   public void write(Buffer var1, long var2) throws IOException {
      if (!this.closed) {
         this.buffer.write(var1, var2);
         this.emitCompleteSegments();
      } else {
         throw new IllegalStateException("closed");
      }
   }

   public long writeAll(Source var1) throws IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("source == null");
      } else {
         long var2 = 0L;

         while(true) {
            long var4 = var1.read(this.buffer, 8192L);
            if (var4 == -1L) {
               return var2;
            }

            var2 += var4;
            this.emitCompleteSegments();
         }
      }
   }

   public BufferedSink writeByte(int var1) throws IOException {
      if (!this.closed) {
         this.buffer.writeByte(var1);
         return this.emitCompleteSegments();
      } else {
         throw new IllegalStateException("closed");
      }
   }

   public BufferedSink writeDecimalLong(long var1) throws IOException {
      if (!this.closed) {
         this.buffer.writeDecimalLong(var1);
         return this.emitCompleteSegments();
      } else {
         throw new IllegalStateException("closed");
      }
   }

   public BufferedSink writeHexadecimalUnsignedLong(long var1) throws IOException {
      if (!this.closed) {
         this.buffer.writeHexadecimalUnsignedLong(var1);
         return this.emitCompleteSegments();
      } else {
         throw new IllegalStateException("closed");
      }
   }

   public BufferedSink writeInt(int var1) throws IOException {
      if (!this.closed) {
         this.buffer.writeInt(var1);
         return this.emitCompleteSegments();
      } else {
         throw new IllegalStateException("closed");
      }
   }

   public BufferedSink writeIntLe(int var1) throws IOException {
      if (!this.closed) {
         this.buffer.writeIntLe(var1);
         return this.emitCompleteSegments();
      } else {
         throw new IllegalStateException("closed");
      }
   }

   public BufferedSink writeLong(long var1) throws IOException {
      if (!this.closed) {
         this.buffer.writeLong(var1);
         return this.emitCompleteSegments();
      } else {
         throw new IllegalStateException("closed");
      }
   }

   public BufferedSink writeLongLe(long var1) throws IOException {
      if (!this.closed) {
         this.buffer.writeLongLe(var1);
         return this.emitCompleteSegments();
      } else {
         throw new IllegalStateException("closed");
      }
   }

   public BufferedSink writeShort(int var1) throws IOException {
      if (!this.closed) {
         this.buffer.writeShort(var1);
         return this.emitCompleteSegments();
      } else {
         throw new IllegalStateException("closed");
      }
   }

   public BufferedSink writeShortLe(int var1) throws IOException {
      if (!this.closed) {
         this.buffer.writeShortLe(var1);
         return this.emitCompleteSegments();
      } else {
         throw new IllegalStateException("closed");
      }
   }

   public BufferedSink writeString(String var1, int var2, int var3, Charset var4) throws IOException {
      if (!this.closed) {
         this.buffer.writeString(var1, var2, var3, var4);
         return this.emitCompleteSegments();
      } else {
         throw new IllegalStateException("closed");
      }
   }

   public BufferedSink writeString(String var1, Charset var2) throws IOException {
      if (!this.closed) {
         this.buffer.writeString(var1, var2);
         return this.emitCompleteSegments();
      } else {
         throw new IllegalStateException("closed");
      }
   }

   public BufferedSink writeUtf8(String var1) throws IOException {
      if (!this.closed) {
         this.buffer.writeUtf8(var1);
         return this.emitCompleteSegments();
      } else {
         throw new IllegalStateException("closed");
      }
   }

   public BufferedSink writeUtf8(String var1, int var2, int var3) throws IOException {
      if (!this.closed) {
         this.buffer.writeUtf8(var1, var2, var3);
         return this.emitCompleteSegments();
      } else {
         throw new IllegalStateException("closed");
      }
   }

   public BufferedSink writeUtf8CodePoint(int var1) throws IOException {
      if (!this.closed) {
         this.buffer.writeUtf8CodePoint(var1);
         return this.emitCompleteSegments();
      } else {
         throw new IllegalStateException("closed");
      }
   }
}
