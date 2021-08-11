package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import javax.annotation.Nullable;

final class RealBufferedSource implements BufferedSource {
   public final Buffer buffer = new Buffer();
   boolean closed;
   public final Source source;

   RealBufferedSource(Source var1) {
      if (var1 != null) {
         this.source = var1;
      } else {
         throw new NullPointerException("source == null");
      }
   }

   public Buffer buffer() {
      return this.buffer;
   }

   public void close() throws IOException {
      if (!this.closed) {
         this.closed = true;
         this.source.close();
         this.buffer.clear();
      }
   }

   public boolean exhausted() throws IOException {
      if (!this.closed) {
         return this.buffer.exhausted() && this.source.read(this.buffer, 8192L) == -1L;
      } else {
         throw new IllegalStateException("closed");
      }
   }

   public long indexOf(byte var1) throws IOException {
      return this.indexOf(var1, 0L, Long.MAX_VALUE);
   }

   public long indexOf(byte var1, long var2) throws IOException {
      return this.indexOf(var1, var2, Long.MAX_VALUE);
   }

   public long indexOf(byte var1, long var2, long var4) throws IOException {
      if (!this.closed) {
         if (var2 >= 0L && var4 >= var2) {
            while(var2 < var4) {
               long var6 = this.buffer.indexOf(var1, var2, var4);
               if (var6 != -1L) {
                  return var6;
               }

               var6 = this.buffer.size;
               if (var6 >= var4) {
                  return -1L;
               }

               if (this.source.read(this.buffer, 8192L) == -1L) {
                  return -1L;
               }

               var2 = Math.max(var2, var6);
            }

            return -1L;
         } else {
            throw new IllegalArgumentException(String.format("fromIndex=%s toIndex=%s", var2, var4));
         }
      } else {
         throw new IllegalStateException("closed");
      }
   }

   public long indexOf(ByteString var1) throws IOException {
      return this.indexOf(var1, 0L);
   }

   public long indexOf(ByteString var1, long var2) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         while(true) {
            long var4 = this.buffer.indexOf(var1, var2);
            if (var4 != -1L) {
               return var4;
            }

            var4 = this.buffer.size;
            if (this.source.read(this.buffer, 8192L) == -1L) {
               return -1L;
            }

            var2 = Math.max(var2, var4 - (long)var1.size() + 1L);
         }
      }
   }

   public long indexOfElement(ByteString var1) throws IOException {
      return this.indexOfElement(var1, 0L);
   }

   public long indexOfElement(ByteString var1, long var2) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         while(true) {
            long var4 = this.buffer.indexOfElement(var1, var2);
            if (var4 != -1L) {
               return var4;
            }

            var4 = this.buffer.size;
            if (this.source.read(this.buffer, 8192L) == -1L) {
               return -1L;
            }

            var2 = Math.max(var2, var4);
         }
      }
   }

   public InputStream inputStream() {
      return new InputStream() {
         public int available() throws IOException {
            if (!RealBufferedSource.this.closed) {
               return (int)Math.min(RealBufferedSource.this.buffer.size, 2147483647L);
            } else {
               throw new IOException("closed");
            }
         }

         public void close() throws IOException {
            RealBufferedSource.this.close();
         }

         public int read() throws IOException {
            if (!RealBufferedSource.this.closed) {
               return RealBufferedSource.this.buffer.size == 0L && RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, 8192L) == -1L ? -1 : RealBufferedSource.this.buffer.readByte() & 255;
            } else {
               throw new IOException("closed");
            }
         }

         public int read(byte[] var1, int var2, int var3) throws IOException {
            if (!RealBufferedSource.this.closed) {
               Util.checkOffsetAndCount((long)var1.length, (long)var2, (long)var3);
               return RealBufferedSource.this.buffer.size == 0L && RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, 8192L) == -1L ? -1 : RealBufferedSource.this.buffer.read(var1, var2, var3);
            } else {
               throw new IOException("closed");
            }
         }

         public String toString() {
            StringBuilder var1 = new StringBuilder();
            var1.append(RealBufferedSource.this);
            var1.append(".inputStream()");
            return var1.toString();
         }
      };
   }

   public boolean rangeEquals(long var1, ByteString var3) throws IOException {
      return this.rangeEquals(var1, var3, 0, var3.size());
   }

   public boolean rangeEquals(long var1, ByteString var3, int var4, int var5) throws IOException {
      if (!this.closed) {
         if (var1 >= 0L && var4 >= 0 && var5 >= 0) {
            if (var3.size() - var4 < var5) {
               return false;
            } else {
               for(int var6 = 0; var6 < var5; ++var6) {
                  long var7 = (long)var6 + var1;
                  if (!this.request(1L + var7)) {
                     return false;
                  }

                  if (this.buffer.getByte(var7) != var3.getByte(var4 + var6)) {
                     return false;
                  }
               }

               return true;
            }
         } else {
            return false;
         }
      } else {
         throw new IllegalStateException("closed");
      }
   }

   public int read(byte[] var1) throws IOException {
      return this.read(var1, 0, var1.length);
   }

   public int read(byte[] var1, int var2, int var3) throws IOException {
      Util.checkOffsetAndCount((long)var1.length, (long)var2, (long)var3);
      if (this.buffer.size == 0L && this.source.read(this.buffer, 8192L) == -1L) {
         return -1;
      } else {
         var3 = (int)Math.min((long)var3, this.buffer.size);
         return this.buffer.read(var1, var2, var3);
      }
   }

   public long read(Buffer var1, long var2) throws IOException {
      if (var1 != null) {
         if (var2 >= 0L) {
            if (!this.closed) {
               if (this.buffer.size == 0L && this.source.read(this.buffer, 8192L) == -1L) {
                  return -1L;
               } else {
                  var2 = Math.min(var2, this.buffer.size);
                  return this.buffer.read(var1, var2);
               }
            } else {
               throw new IllegalStateException("closed");
            }
         } else {
            StringBuilder var4 = new StringBuilder();
            var4.append("byteCount < 0: ");
            var4.append(var2);
            throw new IllegalArgumentException(var4.toString());
         }
      } else {
         throw new IllegalArgumentException("sink == null");
      }
   }

   public long readAll(Sink var1) throws IOException {
      if (var1 != null) {
         long var2;
         long var4;
         for(var2 = 0L; this.source.read(this.buffer, 8192L) != -1L; var2 = var4) {
            long var6 = this.buffer.completeSegmentByteCount();
            var4 = var2;
            if (var6 > 0L) {
               var4 = var2 + var6;
               var1.write(this.buffer, var6);
            }
         }

         var4 = var2;
         if (this.buffer.size() > 0L) {
            var4 = var2 + this.buffer.size();
            Buffer var8 = this.buffer;
            var1.write(var8, var8.size());
         }

         return var4;
      } else {
         throw new IllegalArgumentException("sink == null");
      }
   }

   public byte readByte() throws IOException {
      this.require(1L);
      return this.buffer.readByte();
   }

   public byte[] readByteArray() throws IOException {
      this.buffer.writeAll(this.source);
      return this.buffer.readByteArray();
   }

   public byte[] readByteArray(long var1) throws IOException {
      this.require(var1);
      return this.buffer.readByteArray(var1);
   }

   public ByteString readByteString() throws IOException {
      this.buffer.writeAll(this.source);
      return this.buffer.readByteString();
   }

   public ByteString readByteString(long var1) throws IOException {
      this.require(var1);
      return this.buffer.readByteString(var1);
   }

   public long readDecimalLong() throws IOException {
      this.require(1L);

      for(int var2 = 0; this.request((long)(var2 + 1)); ++var2) {
         byte var1 = this.buffer.getByte((long)var2);
         if ((var1 < 48 || var1 > 57) && (var2 != 0 || var1 != 45)) {
            if (var2 == 0) {
               throw new NumberFormatException(String.format("Expected leading [0-9] or '-' character but was %#x", var1));
            }
            break;
         }
      }

      return this.buffer.readDecimalLong();
   }

   public void readFully(Buffer var1, long var2) throws IOException {
      try {
         this.require(var2);
      } catch (EOFException var5) {
         var1.writeAll(this.buffer);
         throw var5;
      }

      this.buffer.readFully(var1, var2);
   }

   public void readFully(byte[] var1) throws IOException {
      try {
         this.require((long)var1.length);
      } catch (EOFException var6) {
         int var3;
         for(int var2 = 0; this.buffer.size > 0L; var2 += var3) {
            Buffer var5 = this.buffer;
            var3 = var5.read(var1, var2, (int)var5.size);
            if (var3 == -1) {
               throw new AssertionError();
            }
         }

         throw var6;
      }

      this.buffer.readFully(var1);
   }

   public long readHexadecimalUnsignedLong() throws IOException {
      this.require(1L);

      for(int var2 = 0; this.request((long)(var2 + 1)); ++var2) {
         byte var1 = this.buffer.getByte((long)var2);
         if ((var1 < 48 || var1 > 57) && (var1 < 97 || var1 > 102) && (var1 < 65 || var1 > 70)) {
            if (var2 == 0) {
               throw new NumberFormatException(String.format("Expected leading [0-9a-fA-F] character but was %#x", var1));
            }
            break;
         }
      }

      return this.buffer.readHexadecimalUnsignedLong();
   }

   public int readInt() throws IOException {
      this.require(4L);
      return this.buffer.readInt();
   }

   public int readIntLe() throws IOException {
      this.require(4L);
      return this.buffer.readIntLe();
   }

   public long readLong() throws IOException {
      this.require(8L);
      return this.buffer.readLong();
   }

   public long readLongLe() throws IOException {
      this.require(8L);
      return this.buffer.readLongLe();
   }

   public short readShort() throws IOException {
      this.require(2L);
      return this.buffer.readShort();
   }

   public short readShortLe() throws IOException {
      this.require(2L);
      return this.buffer.readShortLe();
   }

   public String readString(long var1, Charset var3) throws IOException {
      this.require(var1);
      if (var3 != null) {
         return this.buffer.readString(var1, var3);
      } else {
         throw new IllegalArgumentException("charset == null");
      }
   }

   public String readString(Charset var1) throws IOException {
      if (var1 != null) {
         this.buffer.writeAll(this.source);
         return this.buffer.readString(var1);
      } else {
         throw new IllegalArgumentException("charset == null");
      }
   }

   public String readUtf8() throws IOException {
      this.buffer.writeAll(this.source);
      return this.buffer.readUtf8();
   }

   public String readUtf8(long var1) throws IOException {
      this.require(var1);
      return this.buffer.readUtf8(var1);
   }

   public int readUtf8CodePoint() throws IOException {
      this.require(1L);
      byte var1 = this.buffer.getByte(0L);
      if ((var1 & 224) == 192) {
         this.require(2L);
      } else if ((var1 & 240) == 224) {
         this.require(3L);
      } else if ((var1 & 248) == 240) {
         this.require(4L);
      }

      return this.buffer.readUtf8CodePoint();
   }

   @Nullable
   public String readUtf8Line() throws IOException {
      long var1 = this.indexOf((byte)10);
      if (var1 == -1L) {
         return this.buffer.size != 0L ? this.readUtf8(this.buffer.size) : null;
      } else {
         return this.buffer.readUtf8Line(var1);
      }
   }

   public String readUtf8LineStrict() throws IOException {
      return this.readUtf8LineStrict(Long.MAX_VALUE);
   }

   public String readUtf8LineStrict(long var1) throws IOException {
      if (var1 >= 0L) {
         long var3;
         if (var1 == Long.MAX_VALUE) {
            var3 = Long.MAX_VALUE;
         } else {
            var3 = var1 + 1L;
         }

         long var5 = this.indexOf((byte)10, 0L, var3);
         if (var5 != -1L) {
            return this.buffer.readUtf8Line(var5);
         } else if (var3 < Long.MAX_VALUE && this.request(var3) && this.buffer.getByte(var3 - 1L) == 13 && this.request(1L + var3) && this.buffer.getByte(var3) == 10) {
            return this.buffer.readUtf8Line(var3);
         } else {
            Buffer var9 = new Buffer();
            Buffer var8 = this.buffer;
            var8.copyTo(var9, 0L, Math.min(32L, var8.size()));
            StringBuilder var10 = new StringBuilder();
            var10.append("\\n not found: limit=");
            var10.append(Math.min(this.buffer.size(), var1));
            var10.append(" content=");
            var10.append(var9.readByteString().hex());
            var10.append('…');
            throw new EOFException(var10.toString());
         }
      } else {
         StringBuilder var7 = new StringBuilder();
         var7.append("limit < 0: ");
         var7.append(var1);
         throw new IllegalArgumentException(var7.toString());
      }
   }

   public boolean request(long var1) throws IOException {
      if (var1 >= 0L) {
         if (!this.closed) {
            do {
               if (this.buffer.size >= var1) {
                  return true;
               }
            } while(this.source.read(this.buffer, 8192L) != -1L);

            return false;
         } else {
            throw new IllegalStateException("closed");
         }
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("byteCount < 0: ");
         var3.append(var1);
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public void require(long var1) throws IOException {
      if (!this.request(var1)) {
         throw new EOFException();
      }
   }

   public int select(Options var1) throws IOException {
      if (this.closed) {
         throw new IllegalStateException("closed");
      } else {
         do {
            int var2 = this.buffer.selectPrefix(var1);
            if (var2 == -1) {
               return -1;
            }

            int var3 = var1.byteStrings[var2].size();
            if ((long)var3 <= this.buffer.size) {
               this.buffer.skip((long)var3);
               return var2;
            }
         } while(this.source.read(this.buffer, 8192L) != -1L);

         return -1;
      }
   }

   public void skip(long var1) throws IOException {
      if (!this.closed) {
         while(var1 > 0L) {
            if (this.buffer.size == 0L && this.source.read(this.buffer, 8192L) == -1L) {
               throw new EOFException();
            }

            long var3 = Math.min(var1, this.buffer.size());
            this.buffer.skip(var3);
            var1 -= var3;
         }

      } else {
         throw new IllegalStateException("closed");
      }
   }

   public Timeout timeout() {
      return this.source.timeout();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("buffer(");
      var1.append(this.source);
      var1.append(")");
      return var1.toString();
   }
}
