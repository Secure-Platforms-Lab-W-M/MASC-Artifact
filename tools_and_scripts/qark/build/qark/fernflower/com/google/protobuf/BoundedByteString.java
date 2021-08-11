package com.google.protobuf;

import java.util.NoSuchElementException;

class BoundedByteString extends LiteralByteString {
   private final int bytesLength;
   private final int bytesOffset;

   BoundedByteString(byte[] var1, int var2, int var3) {
      super(var1);
      StringBuilder var4;
      if (var2 >= 0) {
         if (var3 >= 0) {
            if ((long)var2 + (long)var3 <= (long)var1.length) {
               this.bytesOffset = var2;
               this.bytesLength = var3;
            } else {
               var4 = new StringBuilder();
               var4.append("Offset+Length too large: ");
               var4.append(var2);
               var4.append("+");
               var4.append(var3);
               throw new IllegalArgumentException(var4.toString());
            }
         } else {
            var4 = new StringBuilder();
            var4.append("Length too small: ");
            var4.append(var2);
            throw new IllegalArgumentException(var4.toString());
         }
      } else {
         var4 = new StringBuilder();
         var4.append("Offset too small: ");
         var4.append(var2);
         throw new IllegalArgumentException(var4.toString());
      }
   }

   public byte byteAt(int var1) {
      StringBuilder var2;
      if (var1 >= 0) {
         if (var1 < this.size()) {
            return this.bytes[this.bytesOffset + var1];
         } else {
            var2 = new StringBuilder();
            var2.append("Index too large: ");
            var2.append(var1);
            var2.append(", ");
            var2.append(this.size());
            throw new ArrayIndexOutOfBoundsException(var2.toString());
         }
      } else {
         var2 = new StringBuilder();
         var2.append("Index too small: ");
         var2.append(var1);
         throw new ArrayIndexOutOfBoundsException(var2.toString());
      }
   }

   protected void copyToInternal(byte[] var1, int var2, int var3, int var4) {
      System.arraycopy(this.bytes, this.getOffsetIntoBytes() + var2, var1, var3, var4);
   }

   protected int getOffsetIntoBytes() {
      return this.bytesOffset;
   }

   public ByteString.ByteIterator iterator() {
      return new BoundedByteString.BoundedByteIterator();
   }

   public int size() {
      return this.bytesLength;
   }

   private class BoundedByteIterator implements ByteString.ByteIterator {
      private final int limit;
      private int position;

      private BoundedByteIterator() {
         int var2 = BoundedByteString.this.getOffsetIntoBytes();
         this.position = var2;
         this.limit = var2 + BoundedByteString.this.size();
      }

      // $FF: synthetic method
      BoundedByteIterator(Object var2) {
         this();
      }

      public boolean hasNext() {
         return this.position < this.limit;
      }

      public Byte next() {
         return this.nextByte();
      }

      public byte nextByte() {
         if (this.position < this.limit) {
            byte[] var2 = BoundedByteString.this.bytes;
            int var1 = this.position++;
            return var2[var1];
         } else {
            throw new NoSuchElementException();
         }
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }
   }
}
