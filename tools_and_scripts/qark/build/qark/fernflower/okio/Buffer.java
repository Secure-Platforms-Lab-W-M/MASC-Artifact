package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;

public final class Buffer implements BufferedSource, BufferedSink, Cloneable {
   private static final byte[] DIGITS = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 97, 98, 99, 100, 101, 102};
   static final int REPLACEMENT_CHARACTER = 65533;
   @Nullable
   Segment head;
   long size;

   private ByteString digest(String param1) {
      // $FF: Couldn't be decompiled
   }

   private ByteString hmac(String param1, ByteString param2) {
      // $FF: Couldn't be decompiled
   }

   private boolean rangeEquals(Segment var1, int var2, ByteString var3, int var4, int var5) {
      int var6 = var1.limit;

      Segment var9;
      for(byte[] var10 = var1.data; var4 < var5; var1 = var9) {
         int var7 = var6;
         var9 = var1;
         int var8 = var2;
         if (var2 == var6) {
            var9 = var1.next;
            var10 = var9.data;
            var8 = var9.pos;
            var7 = var9.limit;
         }

         if (var10[var8] != var3.getByte(var4)) {
            return false;
         }

         var2 = var8 + 1;
         ++var4;
         var6 = var7;
      }

      return true;
   }

   private void readFrom(InputStream var1, long var2, boolean var4) throws IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("in == null");
      } else {
         while(var2 > 0L || var4) {
            Segment var6 = this.writableSegment(1);
            int var5 = (int)Math.min(var2, (long)(8192 - var6.limit));
            var5 = var1.read(var6.data, var6.limit, var5);
            if (var5 == -1) {
               if (var4) {
                  return;
               }

               throw new EOFException();
            }

            var6.limit += var5;
            this.size += (long)var5;
            var2 -= (long)var5;
         }

      }
   }

   public Buffer buffer() {
      return this;
   }

   public void clear() {
      try {
         this.skip(this.size);
      } catch (EOFException var2) {
         throw new AssertionError(var2);
      }
   }

   public Buffer clone() {
      Buffer var2 = new Buffer();
      if (this.size == 0L) {
         return var2;
      } else {
         Segment var1 = new Segment(this.head);
         var2.head = var1;
         var1.prev = var1;
         var1.next = var1;

         for(var1 = this.head.next; var1 != this.head; var1 = var1.next) {
            var2.head.prev.push(new Segment(var1));
         }

         var2.size = this.size;
         return var2;
      }
   }

   public void close() {
   }

   public long completeSegmentByteCount() {
      long var3 = this.size;
      if (var3 == 0L) {
         return 0L;
      } else {
         Segment var5 = this.head.prev;
         long var1 = var3;
         if (var5.limit < 8192) {
            var1 = var3;
            if (var5.owner) {
               var1 = var3 - (long)(var5.limit - var5.pos);
            }
         }

         return var1;
      }
   }

   public Buffer copyTo(OutputStream var1) throws IOException {
      return this.copyTo(var1, 0L, this.size);
   }

   public Buffer copyTo(OutputStream var1, long var2, long var4) throws IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("out == null");
      } else {
         Util.checkOffsetAndCount(this.size, var2, var4);
         if (var4 == 0L) {
            return this;
         } else {
            Segment var12 = this.head;

            while(true) {
               Segment var13 = var12;
               long var8 = var2;
               long var10 = var4;
               if (var2 < (long)(var12.limit - var12.pos)) {
                  while(var10 > 0L) {
                     int var6 = (int)((long)var13.pos + var8);
                     int var7 = (int)Math.min((long)(var13.limit - var6), var10);
                     var1.write(var13.data, var6, var7);
                     var10 -= (long)var7;
                     var8 = 0L;
                     var13 = var13.next;
                  }

                  return this;
               }

               var2 -= (long)(var12.limit - var12.pos);
               var12 = var12.next;
            }
         }
      }
   }

   public Buffer copyTo(Buffer var1, long var2, long var4) {
      if (var1 == null) {
         throw new IllegalArgumentException("out == null");
      } else {
         Util.checkOffsetAndCount(this.size, var2, var4);
         if (var4 == 0L) {
            return this;
         } else {
            var1.size += var4;
            Segment var10 = this.head;

            while(true) {
               Segment var11 = var10;
               long var6 = var2;
               long var8 = var4;
               if (var2 < (long)(var10.limit - var10.pos)) {
                  while(var8 > 0L) {
                     var10 = new Segment(var11);
                     var10.pos = (int)((long)var10.pos + var6);
                     var10.limit = Math.min(var10.pos + (int)var8, var10.limit);
                     Segment var12 = var1.head;
                     if (var12 == null) {
                        var10.prev = var10;
                        var10.next = var10;
                        var1.head = var10;
                     } else {
                        var12.prev.push(var10);
                     }

                     var8 -= (long)(var10.limit - var10.pos);
                     var6 = 0L;
                     var11 = var11.next;
                  }

                  return this;
               }

               var2 -= (long)(var10.limit - var10.pos);
               var10 = var10.next;
            }
         }
      }
   }

   public BufferedSink emit() {
      return this;
   }

   public Buffer emitCompleteSegments() {
      return this;
   }

   public boolean equals(Object var1) {
      if (this == var1) {
         return true;
      } else if (!(var1 instanceof Buffer)) {
         return false;
      } else {
         Buffer var13 = (Buffer)var1;
         long var6 = this.size;
         if (var6 != var13.size) {
            return false;
         } else if (var6 == 0L) {
            return true;
         } else {
            Segment var11 = this.head;
            Segment var14 = var13.head;
            int var3 = var11.pos;
            int var2 = var14.pos;

            int var5;
            for(var6 = 0L; var6 < this.size; var2 = var5) {
               long var8 = (long)Math.min(var11.limit - var3, var14.limit - var2);

               int var4;
               for(var4 = 0; (long)var4 < var8; ++var2) {
                  if (var11.data[var3] != var14.data[var2]) {
                     return false;
                  }

                  ++var4;
                  ++var3;
               }

               Segment var10 = var11;
               var4 = var3;
               if (var3 == var11.limit) {
                  var10 = var11.next;
                  var4 = var10.pos;
               }

               Segment var12 = var14;
               var5 = var2;
               if (var2 == var14.limit) {
                  var12 = var14.next;
                  var5 = var12.pos;
               }

               var6 += var8;
               var11 = var10;
               var14 = var12;
               var3 = var4;
            }

            return true;
         }
      }
   }

   public boolean exhausted() {
      return this.size == 0L;
   }

   public void flush() {
   }

   public byte getByte(long var1) {
      Util.checkOffsetAndCount(this.size, var1, 1L);
      Segment var4 = this.head;

      while(true) {
         int var3 = var4.limit - var4.pos;
         if (var1 < (long)var3) {
            return var4.data[var4.pos + (int)var1];
         }

         var1 -= (long)var3;
         var4 = var4.next;
      }
   }

   public int hashCode() {
      Segment var4 = this.head;
      if (var4 == null) {
         return 0;
      } else {
         int var2 = 1;

         do {
            int var1 = var4.pos;

            for(int var3 = var4.limit; var1 < var3; ++var1) {
               var2 = var2 * 31 + var4.data[var1];
            }

            var4 = var4.next;
         } while(var4 != this.head);

         return var2;
      }
   }

   public ByteString hmacSha1(ByteString var1) {
      return this.hmac("HmacSHA1", var1);
   }

   public ByteString hmacSha256(ByteString var1) {
      return this.hmac("HmacSHA256", var1);
   }

   public ByteString hmacSha512(ByteString var1) {
      return this.hmac("HmacSHA512", var1);
   }

   public long indexOf(byte var1) {
      return this.indexOf(var1, 0L, Long.MAX_VALUE);
   }

   public long indexOf(byte var1, long var2) {
      return this.indexOf(var1, var2, Long.MAX_VALUE);
   }

   public long indexOf(byte var1, long var2, long var4) {
      if (var2 >= 0L && var4 >= var2) {
         long var10 = var4;
         if (var4 > this.size) {
            var10 = this.size;
         }

         if (var2 == var10) {
            return -1L;
         } else {
            Segment var15 = this.head;
            if (var15 == null) {
               return -1L;
            } else {
               long var8;
               long var12;
               Segment var14;
               if (this.size - var2 < var2) {
                  var8 = this.size;

                  while(true) {
                     var14 = var15;
                     var4 = var8;
                     var12 = var2;
                     if (var8 <= var2) {
                        break;
                     }

                     var15 = var15.prev;
                     var8 -= (long)(var15.limit - var15.pos);
                  }
               } else {
                  var4 = 0L;

                  while(true) {
                     var8 = (long)(var15.limit - var15.pos) + var4;
                     var14 = var15;
                     var12 = var2;
                     if (var8 >= var2) {
                        break;
                     }

                     var15 = var15.next;
                     var4 = var8;
                  }
               }

               while(var4 < var10) {
                  byte[] var16 = var14.data;
                  int var7 = (int)Math.min((long)var14.limit, (long)var14.pos + var10 - var4);

                  for(int var6 = (int)((long)var14.pos + var12 - var4); var6 < var7; ++var6) {
                     if (var16[var6] == var1) {
                        return (long)(var6 - var14.pos) + var4;
                     }
                  }

                  var4 += (long)(var14.limit - var14.pos);
                  var12 = var4;
                  var14 = var14.next;
               }

               return -1L;
            }
         }
      } else {
         throw new IllegalArgumentException(String.format("size=%s fromIndex=%s toIndex=%s", this.size, var2, var4));
      }
   }

   public long indexOf(ByteString var1) throws IOException {
      return this.indexOf(var1, 0L);
   }

   public long indexOf(ByteString var1, long var2) throws IOException {
      if (var1.size() == 0) {
         throw new IllegalArgumentException("bytes is empty");
      } else if (var2 >= 0L) {
         Segment var13 = this.head;
         if (var13 == null) {
            return -1L;
         } else {
            long var8;
            long var10;
            Segment var12;
            if (this.size - var2 < var2) {
               var10 = this.size;

               while(true) {
                  var12 = var13;
                  var8 = var10;
                  if (var10 <= var2) {
                     break;
                  }

                  var13 = var13.prev;
                  var10 -= (long)(var13.limit - var13.pos);
               }
            } else {
               var8 = 0L;

               while(true) {
                  var10 = (long)(var13.limit - var13.pos) + var8;
                  var12 = var13;
                  if (var10 >= var2) {
                     break;
                  }

                  var13 = var13.next;
                  var8 = var10;
               }
            }

            byte var6 = var1.getByte(0);
            int var7 = var1.size();

            for(var10 = 1L + (this.size - (long)var7); var8 < var10; var12 = var13.next) {
               byte[] var14 = var12.data;
               int var4 = (int)Math.min((long)var12.limit, (long)var12.pos + var10 - var8);
               int var5 = (int)((long)var12.pos + var2 - var8);
               var13 = var12;

               for(byte[] var15 = var14; var5 < var4; ++var5) {
                  if (var15[var5] == var6 && this.rangeEquals(var13, var5 + 1, var1, 1, var7)) {
                     return (long)(var5 - var13.pos) + var8;
                  }
               }

               var8 += (long)(var13.limit - var13.pos);
               var2 = var8;
            }

            return -1L;
         }
      } else {
         throw new IllegalArgumentException("fromIndex < 0");
      }
   }

   public long indexOfElement(ByteString var1) {
      return this.indexOfElement(var1, 0L);
   }

   public long indexOfElement(ByteString var1, long var2) {
      Buffer var15 = this;
      if (var2 >= 0L) {
         Segment var14 = this.head;
         if (var14 == null) {
            return -1L;
         } else {
            long var9;
            long var11;
            Segment var13;
            if (this.size - var2 < var2) {
               var11 = this.size;

               while(true) {
                  var13 = var14;
                  var9 = var11;
                  if (var11 <= var2) {
                     break;
                  }

                  var14 = var14.prev;
                  var11 -= (long)(var14.limit - var14.pos);
               }
            } else {
               var9 = 0L;

               while(true) {
                  var11 = (long)(var14.limit - var14.pos) + var9;
                  var13 = var14;
                  if (var11 >= var2) {
                     break;
                  }

                  var14 = var14.next;
                  var9 = var11;
               }
            }

            int var4;
            byte[] var16;
            if (var1.size() != 2) {
               for(var16 = var1.internalArray(); var9 < this.size; var13 = var13.next) {
                  byte[] var21 = var13.data;
                  var4 = (int)((long)var13.pos + var2 - var9);
                  int var18 = var13.limit;

                  while(true) {
                     int var17 = 0;
                     if (var4 >= var18) {
                        var9 += (long)(var13.limit - var13.pos);
                        var2 = var9;
                        break;
                     }

                     byte var19 = var21[var4];

                     for(int var20 = var16.length; var17 < var20; ++var17) {
                        if (var19 == var16[var17]) {
                           return (long)(var4 - var13.pos) + var9;
                        }
                     }

                     ++var4;
                  }
               }
            } else {
               byte var5 = var1.getByte(0);

               for(byte var6 = var1.getByte(1); var9 < var15.size; var13 = var13.next) {
                  var16 = var13.data;
                  var4 = (int)((long)var13.pos + var2 - var9);

                  for(int var7 = var13.limit; var4 < var7; ++var4) {
                     byte var8 = var16[var4];
                     if (var8 == var5 || var8 == var6) {
                        return (long)(var4 - var13.pos) + var9;
                     }
                  }

                  var9 += (long)(var13.limit - var13.pos);
                  var2 = var9;
               }
            }

            return -1L;
         }
      } else {
         throw new IllegalArgumentException("fromIndex < 0");
      }
   }

   public InputStream inputStream() {
      return new InputStream() {
         public int available() {
            return (int)Math.min(Buffer.this.size, 2147483647L);
         }

         public void close() {
         }

         public int read() {
            return Buffer.this.size > 0L ? Buffer.this.readByte() & 255 : -1;
         }

         public int read(byte[] var1, int var2, int var3) {
            return Buffer.this.read(var1, var2, var3);
         }

         public String toString() {
            StringBuilder var1 = new StringBuilder();
            var1.append(Buffer.this);
            var1.append(".inputStream()");
            return var1.toString();
         }
      };
   }

   public ByteString md5() {
      return this.digest("MD5");
   }

   public OutputStream outputStream() {
      return new OutputStream() {
         public void close() {
         }

         public void flush() {
         }

         public String toString() {
            StringBuilder var1 = new StringBuilder();
            var1.append(Buffer.this);
            var1.append(".outputStream()");
            return var1.toString();
         }

         public void write(int var1) {
            Buffer.this.writeByte((byte)var1);
         }

         public void write(byte[] var1, int var2, int var3) {
            Buffer.this.write(var1, var2, var3);
         }
      };
   }

   public boolean rangeEquals(long var1, ByteString var3) {
      return this.rangeEquals(var1, var3, 0, var3.size());
   }

   public boolean rangeEquals(long var1, ByteString var3, int var4, int var5) {
      if (var1 >= 0L && var4 >= 0 && var5 >= 0 && this.size - var1 >= (long)var5) {
         if (var3.size() - var4 < var5) {
            return false;
         } else {
            for(int var6 = 0; var6 < var5; ++var6) {
               if (this.getByte((long)var6 + var1) != var3.getByte(var4 + var6)) {
                  return false;
               }
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public int read(byte[] var1) {
      return this.read(var1, 0, var1.length);
   }

   public int read(byte[] var1, int var2, int var3) {
      Util.checkOffsetAndCount((long)var1.length, (long)var2, (long)var3);
      Segment var4 = this.head;
      if (var4 == null) {
         return -1;
      } else {
         var3 = Math.min(var3, var4.limit - var4.pos);
         System.arraycopy(var4.data, var4.pos, var1, var2, var3);
         var4.pos += var3;
         this.size -= (long)var3;
         if (var4.pos == var4.limit) {
            this.head = var4.pop();
            SegmentPool.recycle(var4);
         }

         return var3;
      }
   }

   public long read(Buffer var1, long var2) {
      if (var1 != null) {
         if (var2 >= 0L) {
            long var6 = this.size;
            if (var6 == 0L) {
               return -1L;
            } else {
               long var4 = var2;
               if (var2 > var6) {
                  var4 = this.size;
               }

               var1.write(this, var4);
               return var4;
            }
         } else {
            StringBuilder var8 = new StringBuilder();
            var8.append("byteCount < 0: ");
            var8.append(var2);
            throw new IllegalArgumentException(var8.toString());
         }
      } else {
         throw new IllegalArgumentException("sink == null");
      }
   }

   public long readAll(Sink var1) throws IOException {
      long var2 = this.size;
      if (var2 > 0L) {
         var1.write(this, var2);
      }

      return var2;
   }

   public byte readByte() {
      if (this.size != 0L) {
         Segment var5 = this.head;
         int var2 = var5.pos;
         int var3 = var5.limit;
         byte[] var6 = var5.data;
         int var4 = var2 + 1;
         byte var1 = var6[var2];
         --this.size;
         if (var4 == var3) {
            this.head = var5.pop();
            SegmentPool.recycle(var5);
            return var1;
         } else {
            var5.pos = var4;
            return var1;
         }
      } else {
         throw new IllegalStateException("size == 0");
      }
   }

   public byte[] readByteArray() {
      try {
         byte[] var1 = this.readByteArray(this.size);
         return var1;
      } catch (EOFException var2) {
         throw new AssertionError(var2);
      }
   }

   public byte[] readByteArray(long var1) throws EOFException {
      Util.checkOffsetAndCount(this.size, 0L, var1);
      if (var1 <= 2147483647L) {
         byte[] var4 = new byte[(int)var1];
         this.readFully(var4);
         return var4;
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("byteCount > Integer.MAX_VALUE: ");
         var3.append(var1);
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public ByteString readByteString() {
      return new ByteString(this.readByteArray());
   }

   public ByteString readByteString(long var1) throws EOFException {
      return new ByteString(this.readByteArray(var1));
   }

   public long readDecimalLong() {
      if (this.size == 0L) {
         throw new IllegalStateException("size == 0");
      } else {
         long var12 = 0L;
         int var3 = 0;
         boolean var4 = false;
         boolean var1 = false;
         long var8 = -922337203685477580L;
         long var10 = -7L;

         do {
            Segment var15 = this.head;
            byte[] var14 = var15.data;
            int var2 = var15.pos;

            int var5;
            for(var5 = var15.limit; var2 < var5; ++var3) {
               byte var6 = var14[var2];
               if (var6 >= 48 && var6 <= 57) {
                  int var7 = 48 - var6;
                  if (var12 < var8 || var12 == var8 && (long)var7 < var10) {
                     Buffer var17 = (new Buffer()).writeDecimalLong(var12).writeByte(var6);
                     if (!var4) {
                        var17.readByte();
                     }

                     StringBuilder var18 = new StringBuilder();
                     var18.append("Number too large: ");
                     var18.append(var17.readUtf8());
                     throw new NumberFormatException(var18.toString());
                  }

                  var12 = var12 * 10L + (long)var7;
               } else {
                  if (var6 != 45 || var3 != 0) {
                     if (var3 == 0) {
                        StringBuilder var16 = new StringBuilder();
                        var16.append("Expected leading [0-9] or '-' character but was 0x");
                        var16.append(Integer.toHexString(var6));
                        throw new NumberFormatException(var16.toString());
                     }

                     var1 = true;
                     break;
                  }

                  var4 = true;
                  --var10;
               }

               ++var2;
            }

            if (var2 == var5) {
               this.head = var15.pop();
               SegmentPool.recycle(var15);
            } else {
               var15.pos = var2;
            }
         } while(!var1 && this.head != null);

         this.size -= (long)var3;
         if (var4) {
            return var12;
         } else {
            return -var12;
         }
      }
   }

   public Buffer readFrom(InputStream var1) throws IOException {
      this.readFrom(var1, Long.MAX_VALUE, true);
      return this;
   }

   public Buffer readFrom(InputStream var1, long var2) throws IOException {
      if (var2 >= 0L) {
         this.readFrom(var1, var2, false);
         return this;
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("byteCount < 0: ");
         var4.append(var2);
         throw new IllegalArgumentException(var4.toString());
      }
   }

   public void readFully(Buffer var1, long var2) throws EOFException {
      long var4 = this.size;
      if (var4 >= var2) {
         var1.write(this, var2);
      } else {
         var1.write(this, var4);
         throw new EOFException();
      }
   }

   public void readFully(byte[] var1) throws EOFException {
      int var3;
      for(int var2 = 0; var2 < var1.length; var2 += var3) {
         var3 = this.read(var1, var2, var1.length - var2);
         if (var3 == -1) {
            throw new EOFException();
         }
      }

   }

   public long readHexadecimalUnsignedLong() {
      if (this.size == 0L) {
         throw new IllegalStateException("size == 0");
      } else {
         long var7 = 0L;
         int var1 = 0;
         boolean var2 = false;

         int var3;
         long var9;
         do {
            Segment var11 = this.head;
            byte[] var12 = var11.data;
            int var4 = var11.pos;
            int var6 = var11.limit;
            var3 = var1;
            var9 = var7;

            boolean var5;
            while(true) {
               var5 = var2;
               if (var4 >= var6) {
                  break;
               }

               byte var13 = var12[var4];
               if (var13 >= 48 && var13 <= 57) {
                  var1 = var13 - 48;
               } else if (var13 >= 97 && var13 <= 102) {
                  var1 = var13 - 97 + 10;
               } else {
                  if (var13 < 65 || var13 > 70) {
                     if (var3 == 0) {
                        StringBuilder var14 = new StringBuilder();
                        var14.append("Expected leading [0-9a-fA-F] character but was 0x");
                        var14.append(Integer.toHexString(var13));
                        throw new NumberFormatException(var14.toString());
                     }

                     var5 = true;
                     break;
                  }

                  var1 = var13 - 65 + 10;
               }

               if ((-1152921504606846976L & var9) != 0L) {
                  Buffer var15 = (new Buffer()).writeHexadecimalUnsignedLong(var9).writeByte(var13);
                  StringBuilder var16 = new StringBuilder();
                  var16.append("Number too large: ");
                  var16.append(var15.readUtf8());
                  throw new NumberFormatException(var16.toString());
               }

               var9 = var9 << 4 | (long)var1;
               ++var4;
               ++var3;
            }

            if (var4 == var6) {
               this.head = var11.pop();
               SegmentPool.recycle(var11);
            } else {
               var11.pos = var4;
            }

            if (var5) {
               break;
            }

            var7 = var9;
            var1 = var3;
            var2 = var5;
         } while(this.head != null);

         this.size -= (long)var3;
         return var9;
      }
   }

   public int readInt() {
      if (this.size >= 4L) {
         Segment var11 = this.head;
         int var2 = var11.pos;
         int var1 = var11.limit;
         if (var1 - var2 < 4) {
            return (this.readByte() & 255) << 24 | (this.readByte() & 255) << 16 | (this.readByte() & 255) << 8 | this.readByte() & 255;
         } else {
            byte[] var8 = var11.data;
            int var3 = var2 + 1;
            byte var9 = var8[var2];
            int var5 = var3 + 1;
            byte var10 = var8[var3];
            int var4 = var5 + 1;
            byte var6 = var8[var5];
            var5 = var4 + 1;
            var2 = (var9 & 255) << 24 | (var10 & 255) << 16 | (var6 & 255) << 8 | var8[var4] & 255;
            this.size -= 4L;
            if (var5 == var1) {
               this.head = var11.pop();
               SegmentPool.recycle(var11);
               return var2;
            } else {
               var11.pos = var5;
               return var2;
            }
         }
      } else {
         StringBuilder var7 = new StringBuilder();
         var7.append("size < 4: ");
         var7.append(this.size);
         throw new IllegalStateException(var7.toString());
      }
   }

   public int readIntLe() {
      return Util.reverseBytesInt(this.readInt());
   }

   public long readLong() {
      if (this.size >= 8L) {
         Segment var20 = this.head;
         int var3 = var20.pos;
         int var1 = var20.limit;
         if (var1 - var3 < 8) {
            return ((long)this.readInt() & 4294967295L) << 32 | (long)this.readInt() & 4294967295L;
         } else {
            byte[] var19 = var20.data;
            int var2 = var3 + 1;
            long var4 = (long)var19[var3];
            var3 = var2 + 1;
            long var6 = (long)var19[var2];
            var2 = var3 + 1;
            long var8 = (long)var19[var3];
            var3 = var2 + 1;
            long var10 = (long)var19[var2];
            var2 = var3 + 1;
            long var12 = (long)var19[var3];
            var3 = var2 + 1;
            long var14 = (long)var19[var2];
            var2 = var3 + 1;
            long var16 = (long)var19[var3];
            var3 = var2 + 1;
            var4 = (var4 & 255L) << 56 | (var6 & 255L) << 48 | (var8 & 255L) << 40 | (var10 & 255L) << 32 | (var12 & 255L) << 24 | (var14 & 255L) << 16 | (var16 & 255L) << 8 | (long)var19[var2] & 255L;
            this.size -= 8L;
            if (var3 == var1) {
               this.head = var20.pop();
               SegmentPool.recycle(var20);
               return var4;
            } else {
               var20.pos = var3;
               return var4;
            }
         }
      } else {
         StringBuilder var18 = new StringBuilder();
         var18.append("size < 8: ");
         var18.append(this.size);
         throw new IllegalStateException(var18.toString());
      }
   }

   public long readLongLe() {
      return Util.reverseBytesLong(this.readLong());
   }

   public short readShort() {
      if (this.size >= 2L) {
         Segment var9 = this.head;
         int var3 = var9.pos;
         int var1 = var9.limit;
         if (var1 - var3 < 2) {
            return (short)((this.readByte() & 255) << 8 | this.readByte() & 255);
         } else {
            byte[] var6 = var9.data;
            int var2 = var3 + 1;
            byte var8 = var6[var3];
            int var4 = var2 + 1;
            byte var7 = var6[var2];
            this.size -= 2L;
            if (var4 == var1) {
               this.head = var9.pop();
               SegmentPool.recycle(var9);
            } else {
               var9.pos = var4;
            }

            return (short)((var8 & 255) << 8 | var7 & 255);
         }
      } else {
         StringBuilder var5 = new StringBuilder();
         var5.append("size < 2: ");
         var5.append(this.size);
         throw new IllegalStateException(var5.toString());
      }
   }

   public short readShortLe() {
      return Util.reverseBytesShort(this.readShort());
   }

   public String readString(long var1, Charset var3) throws EOFException {
      Util.checkOffsetAndCount(this.size, 0L, var1);
      if (var3 != null) {
         if (var1 <= 2147483647L) {
            if (var1 == 0L) {
               return "";
            } else {
               Segment var4 = this.head;
               if ((long)var4.pos + var1 > (long)var4.limit) {
                  return new String(this.readByteArray(var1), var3);
               } else {
                  String var6 = new String(var4.data, var4.pos, (int)var1, var3);
                  var4.pos = (int)((long)var4.pos + var1);
                  this.size -= var1;
                  if (var4.pos == var4.limit) {
                     this.head = var4.pop();
                     SegmentPool.recycle(var4);
                  }

                  return var6;
               }
            }
         } else {
            StringBuilder var5 = new StringBuilder();
            var5.append("byteCount > Integer.MAX_VALUE: ");
            var5.append(var1);
            throw new IllegalArgumentException(var5.toString());
         }
      } else {
         throw new IllegalArgumentException("charset == null");
      }
   }

   public String readString(Charset var1) {
      try {
         String var3 = this.readString(this.size, var1);
         return var3;
      } catch (EOFException var2) {
         throw new AssertionError(var2);
      }
   }

   public String readUtf8() {
      try {
         String var1 = this.readString(this.size, Util.UTF_8);
         return var1;
      } catch (EOFException var2) {
         throw new AssertionError(var2);
      }
   }

   public String readUtf8(long var1) throws EOFException {
      return this.readString(var1, Util.UTF_8);
   }

   public int readUtf8CodePoint() throws EOFException {
      if (this.size != 0L) {
         byte var4 = this.getByte(0L);
         int var1;
         byte var2;
         int var3;
         if ((var4 & 128) == 0) {
            var1 = var4 & 127;
            var2 = 1;
            var3 = 0;
         } else if ((var4 & 224) == 192) {
            var1 = var4 & 31;
            var2 = 2;
            var3 = 128;
         } else if ((var4 & 240) == 224) {
            var1 = var4 & 15;
            var2 = 3;
            var3 = 2048;
         } else {
            if ((var4 & 248) != 240) {
               this.skip(1L);
               return 65533;
            }

            var1 = var4 & 7;
            var2 = 4;
            var3 = 65536;
         }

         if (this.size >= (long)var2) {
            for(int var7 = 1; var7 < var2; ++var7) {
               byte var5 = this.getByte((long)var7);
               if ((var5 & 192) != 128) {
                  this.skip((long)var7);
                  return 65533;
               }

               var1 = var1 << 6 | var5 & 63;
            }

            this.skip((long)var2);
            if (var1 > 1114111) {
               return 65533;
            } else if (var1 >= 55296 && var1 <= 57343) {
               return 65533;
            } else if (var1 < var3) {
               return 65533;
            } else {
               return var1;
            }
         } else {
            StringBuilder var6 = new StringBuilder();
            var6.append("size < ");
            var6.append(var2);
            var6.append(": ");
            var6.append(this.size);
            var6.append(" (to read code point prefixed 0x");
            var6.append(Integer.toHexString(var4));
            var6.append(")");
            throw new EOFException(var6.toString());
         }
      } else {
         throw new EOFException();
      }
   }

   @Nullable
   public String readUtf8Line() throws EOFException {
      long var1 = this.indexOf((byte)10);
      if (var1 == -1L) {
         var1 = this.size;
         return var1 != 0L ? this.readUtf8(var1) : null;
      } else {
         return this.readUtf8Line(var1);
      }
   }

   String readUtf8Line(long var1) throws EOFException {
      String var3;
      if (var1 > 0L && this.getByte(var1 - 1L) == 13) {
         var3 = this.readUtf8(var1 - 1L);
         this.skip(2L);
         return var3;
      } else {
         var3 = this.readUtf8(var1);
         this.skip(1L);
         return var3;
      }
   }

   public String readUtf8LineStrict() throws EOFException {
      return this.readUtf8LineStrict(Long.MAX_VALUE);
   }

   public String readUtf8LineStrict(long var1) throws EOFException {
      if (var1 >= 0L) {
         long var3 = Long.MAX_VALUE;
         if (var1 != Long.MAX_VALUE) {
            var3 = var1 + 1L;
         }

         long var5 = this.indexOf((byte)10, 0L, var3);
         if (var5 != -1L) {
            return this.readUtf8Line(var5);
         } else if (var3 < this.size() && this.getByte(var3 - 1L) == 13 && this.getByte(var3) == 10) {
            return this.readUtf8Line(var3);
         } else {
            Buffer var9 = new Buffer();
            this.copyTo(var9, 0L, Math.min(32L, this.size()));
            StringBuilder var8 = new StringBuilder();
            var8.append("\\n not found: limit=");
            var8.append(Math.min(this.size(), var1));
            var8.append(" content=");
            var8.append(var9.readByteString().hex());
            var8.append('…');
            throw new EOFException(var8.toString());
         }
      } else {
         StringBuilder var7 = new StringBuilder();
         var7.append("limit < 0: ");
         var7.append(var1);
         throw new IllegalArgumentException(var7.toString());
      }
   }

   public boolean request(long var1) {
      return this.size >= var1;
   }

   public void require(long var1) throws EOFException {
      if (this.size < var1) {
         throw new EOFException();
      }
   }

   List segmentSizes() {
      if (this.head == null) {
         return Collections.emptyList();
      } else {
         ArrayList var2 = new ArrayList();
         var2.add(this.head.limit - this.head.pos);

         for(Segment var1 = this.head.next; var1 != this.head; var1 = var1.next) {
            var2.add(var1.limit - var1.pos);
         }

         return var2;
      }
   }

   public int select(Options var1) {
      Segment var4 = this.head;
      if (var4 == null) {
         return var1.indexOf(ByteString.EMPTY);
      } else {
         ByteString[] var7 = var1.byteStrings;
         int var3 = var7.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            ByteString var5 = var7[var2];
            if (this.size >= (long)var5.size() && this.rangeEquals(var4, var4.pos, var5, 0, var5.size())) {
               try {
                  this.skip((long)var5.size());
                  return var2;
               } catch (EOFException var6) {
                  throw new AssertionError(var6);
               }
            }
         }

         return -1;
      }
   }

   int selectPrefix(Options var1) {
      Segment var5 = this.head;
      ByteString[] var7 = var1.byteStrings;
      int var3 = var7.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         ByteString var6 = var7[var2];
         int var4 = (int)Math.min(this.size, (long)var6.size());
         if (var4 == 0) {
            return var2;
         }

         if (this.rangeEquals(var5, var5.pos, var6, 0, var4)) {
            return var2;
         }
      }

      return -1;
   }

   public ByteString sha1() {
      return this.digest("SHA-1");
   }

   public ByteString sha256() {
      return this.digest("SHA-256");
   }

   public ByteString sha512() {
      return this.digest("SHA-512");
   }

   public long size() {
      return this.size;
   }

   public void skip(long var1) throws EOFException {
      while(true) {
         if (var1 > 0L) {
            Segment var4 = this.head;
            if (var4 != null) {
               int var3 = (int)Math.min(var1, (long)(var4.limit - this.head.pos));
               this.size -= (long)var3;
               var1 -= (long)var3;
               var4 = this.head;
               var4.pos += var3;
               if (this.head.pos == this.head.limit) {
                  var4 = this.head;
                  this.head = var4.pop();
                  SegmentPool.recycle(var4);
               }
               continue;
            }

            throw new EOFException();
         }

         return;
      }
   }

   public ByteString snapshot() {
      long var1 = this.size;
      if (var1 <= 2147483647L) {
         return this.snapshot((int)var1);
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("size > Integer.MAX_VALUE: ");
         var3.append(this.size);
         throw new IllegalArgumentException(var3.toString());
      }
   }

   public ByteString snapshot(int var1) {
      return (ByteString)(var1 == 0 ? ByteString.EMPTY : new SegmentedByteString(this, var1));
   }

   public Timeout timeout() {
      return Timeout.NONE;
   }

   public String toString() {
      return this.snapshot().toString();
   }

   Segment writableSegment(int var1) {
      if (var1 >= 1 && var1 <= 8192) {
         Segment var2 = this.head;
         if (var2 == null) {
            var2 = SegmentPool.take();
            this.head = var2;
            var2.prev = var2;
            var2.next = var2;
            return var2;
         } else {
            Segment var3 = var2.prev;
            if (var3.limit + var1 <= 8192) {
               var2 = var3;
               if (var3.owner) {
                  return var2;
               }
            }

            var2 = var3.push(SegmentPool.take());
            return var2;
         }
      } else {
         throw new IllegalArgumentException();
      }
   }

   public Buffer write(ByteString var1) {
      if (var1 != null) {
         var1.write(this);
         return this;
      } else {
         throw new IllegalArgumentException("byteString == null");
      }
   }

   public Buffer write(byte[] var1) {
      if (var1 != null) {
         return this.write(var1, 0, var1.length);
      } else {
         throw new IllegalArgumentException("source == null");
      }
   }

   public Buffer write(byte[] var1, int var2, int var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("source == null");
      } else {
         Util.checkOffsetAndCount((long)var1.length, (long)var2, (long)var3);

         int var5;
         Segment var6;
         for(int var4 = var2 + var3; var2 < var4; var6.limit += var5) {
            var6 = this.writableSegment(1);
            var5 = Math.min(var4 - var2, 8192 - var6.limit);
            System.arraycopy(var1, var2, var6.data, var6.limit, var5);
            var2 += var5;
         }

         this.size += (long)var3;
         return this;
      }
   }

   public BufferedSink write(Source var1, long var2) throws IOException {
      while(true) {
         if (var2 > 0L) {
            long var4 = var1.read(this, var2);
            if (var4 != -1L) {
               var2 -= var4;
               continue;
            }

            throw new EOFException();
         }

         return this;
      }
   }

   public void write(Buffer var1, long var2) {
      if (var1 != null) {
         if (var1 != this) {
            Util.checkOffsetAndCount(var1.size, 0L, var2);

            while(var2 > 0L) {
               long var5;
               Segment var7;
               if (var2 < (long)(var1.head.limit - var1.head.pos)) {
                  var7 = this.head;
                  if (var7 != null) {
                     var7 = var7.prev;
                  } else {
                     var7 = null;
                  }

                  if (var7 != null && var7.owner) {
                     var5 = (long)var7.limit;
                     int var4;
                     if (var7.shared) {
                        var4 = 0;
                     } else {
                        var4 = var7.pos;
                     }

                     if (var5 + var2 - (long)var4 <= 8192L) {
                        var1.head.writeTo(var7, (int)var2);
                        var1.size -= var2;
                        this.size += var2;
                        return;
                     }
                  }

                  var1.head = var1.head.split((int)var2);
               }

               var7 = var1.head;
               var5 = (long)(var7.limit - var7.pos);
               var1.head = var7.pop();
               Segment var8 = this.head;
               if (var8 == null) {
                  this.head = var7;
                  var7.prev = var7;
                  var7.next = var7;
               } else {
                  var8.prev.push(var7).compact();
               }

               var1.size -= var5;
               this.size += var5;
               var2 -= var5;
            }

         } else {
            throw new IllegalArgumentException("source == this");
         }
      } else {
         throw new IllegalArgumentException("source == null");
      }
   }

   public long writeAll(Source var1) throws IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("source == null");
      } else {
         long var2 = 0L;

         while(true) {
            long var4 = var1.read(this, 8192L);
            if (var4 == -1L) {
               return var2;
            }

            var2 += var4;
         }
      }
   }

   public Buffer writeByte(int var1) {
      Segment var3 = this.writableSegment(1);
      byte[] var4 = var3.data;
      int var2 = var3.limit++;
      var4[var2] = (byte)var1;
      ++this.size;
      return this;
   }

   public Buffer writeDecimalLong(long var1) {
      if (var1 == 0L) {
         return this.writeByte(48);
      } else {
         boolean var4 = false;
         long var7 = var1;
         if (var1 < 0L) {
            var7 = -var1;
            if (var7 < 0L) {
               return this.writeUtf8("-9223372036854775808");
            }

            var4 = true;
         }

         byte var3;
         if (var7 < 100000000L) {
            if (var7 < 10000L) {
               if (var7 < 100L) {
                  if (var7 < 10L) {
                     var3 = 1;
                  } else {
                     var3 = 2;
                  }
               } else if (var7 < 1000L) {
                  var3 = 3;
               } else {
                  var3 = 4;
               }
            } else if (var7 < 1000000L) {
               if (var7 < 100000L) {
                  var3 = 5;
               } else {
                  var3 = 6;
               }
            } else if (var7 < 10000000L) {
               var3 = 7;
            } else {
               var3 = 8;
            }
         } else if (var7 < 1000000000000L) {
            if (var7 < 10000000000L) {
               if (var7 < 1000000000L) {
                  var3 = 9;
               } else {
                  var3 = 10;
               }
            } else if (var7 < 100000000000L) {
               var3 = 11;
            } else {
               var3 = 12;
            }
         } else if (var7 < 1000000000000000L) {
            if (var7 < 10000000000000L) {
               var3 = 13;
            } else if (var7 < 100000000000000L) {
               var3 = 14;
            } else {
               var3 = 15;
            }
         } else if (var7 < 100000000000000000L) {
            if (var7 < 10000000000000000L) {
               var3 = 16;
            } else {
               var3 = 17;
            }
         } else if (var7 < 1000000000000000000L) {
            var3 = 18;
         } else {
            var3 = 19;
         }

         int var5 = var3;
         if (var4) {
            var5 = var3 + 1;
         }

         Segment var9 = this.writableSegment(var5);
         byte[] var10 = var9.data;

         int var11;
         for(var11 = var9.limit + var5; var7 != 0L; var7 /= 10L) {
            int var6 = (int)(var7 % 10L);
            --var11;
            var10[var11] = DIGITS[var6];
         }

         if (var4) {
            var10[var11 - 1] = 45;
         }

         var9.limit += var5;
         this.size += (long)var5;
         return this;
      }
   }

   public Buffer writeHexadecimalUnsignedLong(long var1) {
      if (var1 == 0L) {
         return this.writeByte(48);
      } else {
         int var4 = Long.numberOfTrailingZeros(Long.highestOneBit(var1)) / 4 + 1;
         Segment var6 = this.writableSegment(var4);
         byte[] var7 = var6.data;
         int var3 = var6.limit + var4 - 1;

         for(int var5 = var6.limit; var3 >= var5; --var3) {
            var7[var3] = DIGITS[(int)(15L & var1)];
            var1 >>>= 4;
         }

         var6.limit += var4;
         this.size += (long)var4;
         return this;
      }
   }

   public Buffer writeInt(int var1) {
      Segment var4 = this.writableSegment(4);
      byte[] var5 = var4.data;
      int var3 = var4.limit;
      int var2 = var3 + 1;
      var5[var3] = (byte)(var1 >>> 24 & 255);
      var3 = var2 + 1;
      var5[var2] = (byte)(var1 >>> 16 & 255);
      var2 = var3 + 1;
      var5[var3] = (byte)(var1 >>> 8 & 255);
      var5[var2] = (byte)(var1 & 255);
      var4.limit = var2 + 1;
      this.size += 4L;
      return this;
   }

   public Buffer writeIntLe(int var1) {
      return this.writeInt(Util.reverseBytesInt(var1));
   }

   public Buffer writeLong(long var1) {
      Segment var5 = this.writableSegment(8);
      byte[] var6 = var5.data;
      int var4 = var5.limit;
      int var3 = var4 + 1;
      var6[var4] = (byte)((int)(var1 >>> 56 & 255L));
      var4 = var3 + 1;
      var6[var3] = (byte)((int)(var1 >>> 48 & 255L));
      var3 = var4 + 1;
      var6[var4] = (byte)((int)(var1 >>> 40 & 255L));
      var4 = var3 + 1;
      var6[var3] = (byte)((int)(var1 >>> 32 & 255L));
      var3 = var4 + 1;
      var6[var4] = (byte)((int)(var1 >>> 24 & 255L));
      var4 = var3 + 1;
      var6[var3] = (byte)((int)(var1 >>> 16 & 255L));
      var3 = var4 + 1;
      var6[var4] = (byte)((int)(var1 >>> 8 & 255L));
      var6[var3] = (byte)((int)(var1 & 255L));
      var5.limit = var3 + 1;
      this.size += 8L;
      return this;
   }

   public Buffer writeLongLe(long var1) {
      return this.writeLong(Util.reverseBytesLong(var1));
   }

   public Buffer writeShort(int var1) {
      Segment var4 = this.writableSegment(2);
      byte[] var5 = var4.data;
      int var2 = var4.limit;
      int var3 = var2 + 1;
      var5[var2] = (byte)(var1 >>> 8 & 255);
      var5[var3] = (byte)(var1 & 255);
      var4.limit = var3 + 1;
      this.size += 2L;
      return this;
   }

   public Buffer writeShortLe(int var1) {
      return this.writeShort(Util.reverseBytesShort((short)var1));
   }

   public Buffer writeString(String var1, int var2, int var3, Charset var4) {
      if (var1 != null) {
         StringBuilder var5;
         if (var2 >= 0) {
            if (var3 >= var2) {
               if (var3 <= var1.length()) {
                  if (var4 != null) {
                     if (var4.equals(Util.UTF_8)) {
                        return this.writeUtf8(var1, var2, var3);
                     } else {
                        byte[] var6 = var1.substring(var2, var3).getBytes(var4);
                        return this.write(var6, 0, var6.length);
                     }
                  } else {
                     throw new IllegalArgumentException("charset == null");
                  }
               } else {
                  StringBuilder var7 = new StringBuilder();
                  var7.append("endIndex > string.length: ");
                  var7.append(var3);
                  var7.append(" > ");
                  var7.append(var1.length());
                  throw new IllegalArgumentException(var7.toString());
               }
            } else {
               var5 = new StringBuilder();
               var5.append("endIndex < beginIndex: ");
               var5.append(var3);
               var5.append(" < ");
               var5.append(var2);
               throw new IllegalArgumentException(var5.toString());
            }
         } else {
            var5 = new StringBuilder();
            var5.append("beginIndex < 0: ");
            var5.append(var2);
            throw new IllegalAccessError(var5.toString());
         }
      } else {
         throw new IllegalArgumentException("string == null");
      }
   }

   public Buffer writeString(String var1, Charset var2) {
      return this.writeString(var1, 0, var1.length(), var2);
   }

   public Buffer writeTo(OutputStream var1) throws IOException {
      return this.writeTo(var1, this.size);
   }

   public Buffer writeTo(OutputStream var1, long var2) throws IOException {
      if (var1 != null) {
         Util.checkOffsetAndCount(this.size, 0L, var2);

         Segment var6;
         for(Segment var5 = this.head; var2 > 0L; var5 = var6) {
            int var4 = (int)Math.min(var2, (long)(var5.limit - var5.pos));
            var1.write(var5.data, var5.pos, var4);
            var5.pos += var4;
            this.size -= (long)var4;
            var2 -= (long)var4;
            var6 = var5;
            if (var5.pos == var5.limit) {
               Segment var7 = var5.pop();
               var6 = var7;
               this.head = var7;
               SegmentPool.recycle(var5);
            }
         }

         return this;
      } else {
         throw new IllegalArgumentException("out == null");
      }
   }

   public Buffer writeUtf8(String var1) {
      return this.writeUtf8(var1, 0, var1.length());
   }

   public Buffer writeUtf8(String var1, int var2, int var3) {
      if (var1 == null) {
         throw new IllegalArgumentException("string == null");
      } else {
         StringBuilder var10;
         if (var2 < 0) {
            var10 = new StringBuilder();
            var10.append("beginIndex < 0: ");
            var10.append(var2);
            throw new IllegalArgumentException(var10.toString());
         } else if (var3 < var2) {
            var10 = new StringBuilder();
            var10.append("endIndex < beginIndex: ");
            var10.append(var3);
            var10.append(" < ");
            var10.append(var2);
            throw new IllegalArgumentException(var10.toString());
         } else if (var3 > var1.length()) {
            StringBuilder var12 = new StringBuilder();
            var12.append("endIndex > string.length: ");
            var12.append(var3);
            var12.append(" > ");
            var12.append(var1.length());
            throw new IllegalArgumentException(var12.toString());
         } else {
            while(true) {
               while(var2 < var3) {
                  char var5 = var1.charAt(var2);
                  char var4;
                  int var11;
                  if (var5 < 128) {
                     Segment var8 = this.writableSegment(1);
                     byte[] var9 = var8.data;
                     int var6 = var8.limit - var2;
                     int var7 = Math.min(var3, 8192 - var6);
                     var11 = var2 + 1;
                     var9[var2 + var6] = (byte)var5;

                     for(var2 = var11; var2 < var7; ++var2) {
                        var4 = var1.charAt(var2);
                        if (var4 >= 128) {
                           break;
                        }

                        var9[var2 + var6] = (byte)var4;
                     }

                     var11 = var2 + var6 - var8.limit;
                     var8.limit += var11;
                     this.size += (long)var11;
                  } else if (var5 < 2048) {
                     this.writeByte(var5 >> 6 | 192);
                     this.writeByte(128 | var5 & 63);
                     ++var2;
                  } else if (var5 >= '\ud800' && var5 <= '\udfff') {
                     if (var2 + 1 < var3) {
                        var4 = var1.charAt(var2 + 1);
                     } else {
                        var4 = 0;
                     }

                     if (var5 <= '\udbff' && var4 >= '\udc00' && var4 <= '\udfff') {
                        var11 = ((-55297 & var5) << 10 | -56321 & var4) + 65536;
                        this.writeByte(var11 >> 18 | 240);
                        this.writeByte(var11 >> 12 & 63 | 128);
                        this.writeByte(var11 >> 6 & 63 | 128);
                        this.writeByte(128 | var11 & 63);
                        var2 += 2;
                     } else {
                        this.writeByte(63);
                        ++var2;
                     }
                  } else {
                     this.writeByte(var5 >> 12 | 224);
                     this.writeByte(var5 >> 6 & 63 | 128);
                     this.writeByte(128 | var5 & 63);
                     ++var2;
                  }
               }

               return this;
            }
         }
      }
   }

   public Buffer writeUtf8CodePoint(int var1) {
      if (var1 < 128) {
         this.writeByte(var1);
         return this;
      } else if (var1 < 2048) {
         this.writeByte(var1 >> 6 | 192);
         this.writeByte(128 | var1 & 63);
         return this;
      } else if (var1 < 65536) {
         if (var1 >= 55296 && var1 <= 57343) {
            this.writeByte(63);
            return this;
         } else {
            this.writeByte(var1 >> 12 | 224);
            this.writeByte(var1 >> 6 & 63 | 128);
            this.writeByte(128 | var1 & 63);
            return this;
         }
      } else if (var1 <= 1114111) {
         this.writeByte(var1 >> 18 | 240);
         this.writeByte(var1 >> 12 & 63 | 128);
         this.writeByte(var1 >> 6 & 63 | 128);
         this.writeByte(128 | var1 & 63);
         return this;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Unexpected code point: ");
         var2.append(Integer.toHexString(var1));
         throw new IllegalArgumentException(var2.toString());
      }
   }
}
