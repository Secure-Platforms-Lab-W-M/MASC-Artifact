package com.google.protobuf;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

class LiteralByteString extends ByteString {
   protected final byte[] bytes;
   private int hash = 0;

   LiteralByteString(byte[] var1) {
      this.bytes = var1;
   }

   public ByteBuffer asReadOnlyByteBuffer() {
      return ByteBuffer.wrap(this.bytes, this.getOffsetIntoBytes(), this.size()).asReadOnlyBuffer();
   }

   public List asReadOnlyByteBufferList() {
      ArrayList var1 = new ArrayList(1);
      var1.add(this.asReadOnlyByteBuffer());
      return var1;
   }

   public byte byteAt(int var1) {
      return this.bytes[var1];
   }

   public void copyTo(ByteBuffer var1) {
      var1.put(this.bytes, this.getOffsetIntoBytes(), this.size());
   }

   protected void copyToInternal(byte[] var1, int var2, int var3, int var4) {
      System.arraycopy(this.bytes, var2, var1, var3, var4);
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof ByteString)) {
         return false;
      } else if (this.size() != ((ByteString)var1).size()) {
         return false;
      } else if (this.size() == 0) {
         return true;
      } else if (var1 instanceof LiteralByteString) {
         return this.equalsRange((LiteralByteString)var1, 0, this.size());
      } else if (var1 instanceof RopeByteString) {
         return var1.equals(this);
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Has a new type of ByteString been created? Found ");
         var2.append(var1.getClass());
         throw new IllegalArgumentException(var2.toString());
      }
   }

   boolean equalsRange(LiteralByteString var1, int var2, int var3) {
      if (var3 <= var1.size()) {
         if (var2 + var3 <= var1.size()) {
            byte[] var9 = this.bytes;
            byte[] var7 = var1.bytes;
            int var5 = this.getOffsetIntoBytes();
            int var4 = this.getOffsetIntoBytes();

            for(var2 += var1.getOffsetIntoBytes(); var4 < var5 + var3; ++var2) {
               if (var9[var4] != var7[var2]) {
                  return false;
               }

               ++var4;
            }

            return true;
         } else {
            StringBuilder var6 = new StringBuilder();
            var6.append("Ran off end of other: ");
            var6.append(var2);
            var6.append(", ");
            var6.append(var3);
            var6.append(", ");
            var6.append(var1.size());
            throw new IllegalArgumentException(var6.toString());
         }
      } else {
         StringBuilder var8 = new StringBuilder();
         var8.append("Length too large: ");
         var8.append(var3);
         var8.append(this.size());
         throw new IllegalArgumentException(var8.toString());
      }
   }

   protected int getOffsetIntoBytes() {
      return 0;
   }

   protected int getTreeDepth() {
      return 0;
   }

   public int hashCode() {
      int var2 = this.hash;
      int var1 = var2;
      if (var2 == 0) {
         var1 = this.size();
         var2 = this.partialHash(var1, 0, var1);
         var1 = var2;
         if (var2 == 0) {
            var1 = 1;
         }

         this.hash = var1;
      }

      return var1;
   }

   protected boolean isBalanced() {
      return true;
   }

   public boolean isValidUtf8() {
      int var1 = this.getOffsetIntoBytes();
      return Utf8.isValidUtf8(this.bytes, var1, this.size() + var1);
   }

   public ByteString.ByteIterator iterator() {
      return new LiteralByteString.LiteralByteIterator();
   }

   public CodedInputStream newCodedInput() {
      return CodedInputStream.newInstance(this.bytes, this.getOffsetIntoBytes(), this.size());
   }

   public InputStream newInput() {
      return new ByteArrayInputStream(this.bytes, this.getOffsetIntoBytes(), this.size());
   }

   protected int partialHash(int var1, int var2, int var3) {
      byte[] var5 = this.bytes;
      int var4 = this.getOffsetIntoBytes() + var2;
      var2 = var1;

      for(var1 = var4; var1 < var4 + var3; ++var1) {
         var2 = var2 * 31 + var5[var1];
      }

      return var2;
   }

   protected int partialIsValidUtf8(int var1, int var2, int var3) {
      var2 += this.getOffsetIntoBytes();
      return Utf8.partialIsValidUtf8(var1, this.bytes, var2, var2 + var3);
   }

   protected int peekCachedHashCode() {
      return this.hash;
   }

   public int size() {
      return this.bytes.length;
   }

   public ByteString substring(int var1, int var2) {
      StringBuilder var4;
      if (var1 >= 0) {
         if (var2 <= this.size()) {
            int var3 = var2 - var1;
            if (var3 >= 0) {
               return (ByteString)(var3 == 0 ? ByteString.EMPTY : new BoundedByteString(this.bytes, this.getOffsetIntoBytes() + var1, var3));
            } else {
               var4 = new StringBuilder();
               var4.append("Beginning index larger than ending index: ");
               var4.append(var1);
               var4.append(", ");
               var4.append(var2);
               throw new IndexOutOfBoundsException(var4.toString());
            }
         } else {
            var4 = new StringBuilder();
            var4.append("End index: ");
            var4.append(var2);
            var4.append(" > ");
            var4.append(this.size());
            throw new IndexOutOfBoundsException(var4.toString());
         }
      } else {
         var4 = new StringBuilder();
         var4.append("Beginning index: ");
         var4.append(var1);
         var4.append(" < 0");
         throw new IndexOutOfBoundsException(var4.toString());
      }
   }

   public String toString(String var1) throws UnsupportedEncodingException {
      return new String(this.bytes, this.getOffsetIntoBytes(), this.size(), var1);
   }

   public void writeTo(OutputStream var1) throws IOException {
      var1.write(this.toByteArray());
   }

   private class LiteralByteIterator implements ByteString.ByteIterator {
      private final int limit;
      private int position;

      private LiteralByteIterator() {
         this.position = 0;
         this.limit = LiteralByteString.this.size();
      }

      // $FF: synthetic method
      LiteralByteIterator(Object var2) {
         this();
      }

      public boolean hasNext() {
         return this.position < this.limit;
      }

      public Byte next() {
         return this.nextByte();
      }

      public byte nextByte() {
         int var2;
         byte[] var3;
         try {
            var3 = LiteralByteString.this.bytes;
            var2 = this.position++;
         } catch (ArrayIndexOutOfBoundsException var4) {
            throw new NoSuchElementException(var4.getMessage());
         }

         byte var1 = var3[var2];
         return var1;
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }
   }
}
