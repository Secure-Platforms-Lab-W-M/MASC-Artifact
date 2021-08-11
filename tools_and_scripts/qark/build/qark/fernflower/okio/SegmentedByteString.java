package okio;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;

final class SegmentedByteString extends ByteString {
   final transient int[] directory;
   final transient byte[][] segments;

   SegmentedByteString(Buffer var1, int var2) {
      super((byte[])null);
      Util.checkOffsetAndCount(var1.size, 0L, (long)var2);
      int var4 = 0;
      int var3 = 0;

      for(Segment var6 = var1.head; var4 < var2; var6 = var6.next) {
         if (var6.limit == var6.pos) {
            throw new AssertionError("s.limit == s.pos");
         }

         var4 += var6.limit - var6.pos;
         ++var3;
      }

      this.segments = new byte[var3][];
      this.directory = new int[var3 * 2];
      var3 = 0;
      var4 = 0;

      for(Segment var7 = var1.head; var3 < var2; var7 = var7.next) {
         this.segments[var4] = var7.data;
         int var5 = var3 + (var7.limit - var7.pos);
         var3 = var5;
         if (var5 > var2) {
            var3 = var2;
         }

         int[] var8 = this.directory;
         var8[var4] = var3;
         var8[this.segments.length + var4] = var7.pos;
         var7.shared = true;
         ++var4;
      }

   }

   private int segment(int var1) {
      var1 = Arrays.binarySearch(this.directory, 0, this.segments.length, var1 + 1);
      return var1 >= 0 ? var1 : var1;
   }

   private ByteString toByteString() {
      return new ByteString(this.toByteArray());
   }

   private Object writeReplace() {
      return this.toByteString();
   }

   public ByteBuffer asByteBuffer() {
      return ByteBuffer.wrap(this.toByteArray()).asReadOnlyBuffer();
   }

   public String base64() {
      return this.toByteString().base64();
   }

   public String base64Url() {
      return this.toByteString().base64Url();
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else {
         return var1 instanceof ByteString && ((ByteString)var1).size() == this.size() && this.rangeEquals(0, (ByteString)((ByteString)var1), 0, this.size());
      }
   }

   public byte getByte(int var1) {
      Util.checkOffsetAndCount((long)this.directory[this.segments.length - 1], (long)var1, 1L);
      int var3 = this.segment(var1);
      int var2;
      if (var3 == 0) {
         var2 = 0;
      } else {
         var2 = this.directory[var3 - 1];
      }

      int[] var5 = this.directory;
      byte[][] var6 = this.segments;
      int var4 = var5[var6.length + var3];
      return var6[var3][var1 - var2 + var4];
   }

   public int hashCode() {
      int var1 = this.hashCode;
      if (var1 != 0) {
         return var1;
      } else {
         int var3 = 1;
         int var2 = 0;
         var1 = 0;

         for(int var7 = this.segments.length; var1 < var7; ++var1) {
            byte[] var8 = this.segments[var1];
            int[] var9 = this.directory;
            int var5 = var9[var7 + var1];
            int var6 = var9[var1];

            for(int var4 = var5; var4 < var5 + (var6 - var2); ++var4) {
               var3 = var3 * 31 + var8[var4];
            }

            var2 = var6;
         }

         this.hashCode = var3;
         return var3;
      }
   }

   public String hex() {
      return this.toByteString().hex();
   }

   public ByteString hmacSha1(ByteString var1) {
      return this.toByteString().hmacSha1(var1);
   }

   public ByteString hmacSha256(ByteString var1) {
      return this.toByteString().hmacSha256(var1);
   }

   public int indexOf(byte[] var1, int var2) {
      return this.toByteString().indexOf(var1, var2);
   }

   byte[] internalArray() {
      return this.toByteArray();
   }

   public int lastIndexOf(byte[] var1, int var2) {
      return this.toByteString().lastIndexOf(var1, var2);
   }

   public ByteString md5() {
      return this.toByteString().md5();
   }

   public boolean rangeEquals(int var1, ByteString var2, int var3, int var4) {
      if (var1 >= 0) {
         if (var1 > this.size() - var4) {
            return false;
         } else {
            int var6 = this.segment(var1);
            int var5 = var1;

            for(var1 = var6; var4 > 0; ++var1) {
               if (var1 == 0) {
                  var6 = 0;
               } else {
                  var6 = this.directory[var1 - 1];
               }

               int var7 = Math.min(var4, var6 + (this.directory[var1] - var6) - var5);
               int[] var9 = this.directory;
               byte[][] var10 = this.segments;
               int var8 = var9[var10.length + var1];
               if (!var2.rangeEquals(var3, var10[var1], var5 - var6 + var8, var7)) {
                  return false;
               }

               var5 += var7;
               var3 += var7;
               var4 -= var7;
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public boolean rangeEquals(int var1, byte[] var2, int var3, int var4) {
      if (var1 >= 0 && var1 <= this.size() - var4 && var3 >= 0) {
         if (var3 > var2.length - var4) {
            return false;
         } else {
            int var6 = this.segment(var1);
            int var5 = var1;

            for(var1 = var6; var4 > 0; ++var1) {
               if (var1 == 0) {
                  var6 = 0;
               } else {
                  var6 = this.directory[var1 - 1];
               }

               int var7 = Math.min(var4, var6 + (this.directory[var1] - var6) - var5);
               int[] var9 = this.directory;
               byte[][] var10 = this.segments;
               int var8 = var9[var10.length + var1];
               if (!Util.arrayRangeEquals(var10[var1], var5 - var6 + var8, var2, var3, var7)) {
                  return false;
               }

               var5 += var7;
               var3 += var7;
               var4 -= var7;
            }

            return true;
         }
      } else {
         return false;
      }
   }

   public ByteString sha1() {
      return this.toByteString().sha1();
   }

   public ByteString sha256() {
      return this.toByteString().sha256();
   }

   public int size() {
      return this.directory[this.segments.length - 1];
   }

   public String string(Charset var1) {
      return this.toByteString().string(var1);
   }

   public ByteString substring(int var1) {
      return this.toByteString().substring(var1);
   }

   public ByteString substring(int var1, int var2) {
      return this.toByteString().substring(var1, var2);
   }

   public ByteString toAsciiLowercase() {
      return this.toByteString().toAsciiLowercase();
   }

   public ByteString toAsciiUppercase() {
      return this.toByteString().toAsciiUppercase();
   }

   public byte[] toByteArray() {
      int[] var7 = this.directory;
      byte[][] var6 = this.segments;
      byte[] var9 = new byte[var7[var6.length - 1]];
      int var2 = 0;
      int var1 = 0;

      for(int var4 = var6.length; var1 < var4; ++var1) {
         int[] var8 = this.directory;
         int var5 = var8[var4 + var1];
         int var3 = var8[var1];
         System.arraycopy(this.segments[var1], var5, var9, var2, var3 - var2);
         var2 = var3;
      }

      return var9;
   }

   public String toString() {
      return this.toByteString().toString();
   }

   public String utf8() {
      return this.toByteString().utf8();
   }

   public void write(OutputStream var1) throws IOException {
      if (var1 == null) {
         throw new IllegalArgumentException("out == null");
      } else {
         int var3 = 0;
         int var2 = 0;

         for(int var5 = this.segments.length; var2 < var5; ++var2) {
            int[] var7 = this.directory;
            int var6 = var7[var5 + var2];
            int var4 = var7[var2];
            var1.write(this.segments[var2], var6, var4 - var3);
            var3 = var4;
         }

      }
   }

   void write(Buffer var1) {
      int var3 = 0;
      int var2 = 0;

      for(int var5 = this.segments.length; var2 < var5; ++var2) {
         int[] var7 = this.directory;
         int var6 = var7[var5 + var2];
         int var4 = var7[var2];
         Segment var8 = new Segment(this.segments[var2], var6, var6 + var4 - var3);
         if (var1.head == null) {
            var8.prev = var8;
            var8.next = var8;
            var1.head = var8;
         } else {
            var1.head.prev.push(var8);
         }

         var3 = var4;
      }

      var1.size += (long)var3;
   }
}
