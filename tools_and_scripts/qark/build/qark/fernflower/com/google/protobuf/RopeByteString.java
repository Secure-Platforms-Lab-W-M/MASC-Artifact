package com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

class RopeByteString extends ByteString {
   private static final int[] minLengthByDepth;
   private int hash;
   private final ByteString left;
   private final int leftLength;
   private final ByteString right;
   private final int totalLength;
   private final int treeDepth;

   static {
      ArrayList var3 = new ArrayList();
      int var1 = 1;
      int var0 = 1;

      while(true) {
         int var2 = var1;
         if (var0 <= 0) {
            var3.add(Integer.MAX_VALUE);
            minLengthByDepth = new int[var3.size()];
            var0 = 0;

            while(true) {
               int[] var4 = minLengthByDepth;
               if (var0 >= var4.length) {
                  return;
               }

               var4[var0] = (Integer)var3.get(var0);
               ++var0;
            }
         }

         var3.add(var0);
         var1 = var0;
         var0 += var2;
      }
   }

   private RopeByteString(ByteString var1, ByteString var2) {
      this.hash = 0;
      this.left = var1;
      this.right = var2;
      int var3 = var1.size();
      this.leftLength = var3;
      this.totalLength = var3 + var2.size();
      this.treeDepth = Math.max(var1.getTreeDepth(), var2.getTreeDepth()) + 1;
   }

   // $FF: synthetic method
   RopeByteString(ByteString var1, ByteString var2, Object var3) {
      this(var1, var2);
   }

   static ByteString concatenate(ByteString var0, ByteString var1) {
      RopeByteString var4;
      if (var0 instanceof RopeByteString) {
         var4 = (RopeByteString)var0;
      } else {
         var4 = null;
      }

      if (var1.size() == 0) {
         return var0;
      } else if (var0.size() == 0) {
         return var1;
      } else {
         int var2 = var0.size() + var1.size();
         if (var2 < 128) {
            return concatenateBytes(var0, var1);
         } else if (var4 != null && var4.right.size() + var1.size() < 128) {
            LiteralByteString var6 = concatenateBytes(var4.right, var1);
            return new RopeByteString(var4.left, var6);
         } else if (var4 != null && var4.left.getTreeDepth() > var4.right.getTreeDepth() && var4.getTreeDepth() > var1.getTreeDepth()) {
            RopeByteString var5 = new RopeByteString(var4.right, var1);
            return new RopeByteString(var4.left, var5);
         } else {
            int var3 = Math.max(var0.getTreeDepth(), var1.getTreeDepth());
            return (ByteString)(var2 >= minLengthByDepth[var3 + 1] ? new RopeByteString(var0, var1) : (new RopeByteString.Balancer()).balance(var0, var1));
         }
      }
   }

   private static LiteralByteString concatenateBytes(ByteString var0, ByteString var1) {
      int var2 = var0.size();
      int var3 = var1.size();
      byte[] var4 = new byte[var2 + var3];
      var0.copyTo(var4, 0, 0, var2);
      var1.copyTo(var4, 0, var2, var3);
      return new LiteralByteString(var4);
   }

   private boolean equalsFragments(ByteString var1) {
      int var2 = 0;
      RopeByteString.PieceIterator var11 = new RopeByteString.PieceIterator(this);
      LiteralByteString var10 = (LiteralByteString)var11.next();
      int var3 = 0;
      RopeByteString.PieceIterator var12 = new RopeByteString.PieceIterator(var1);
      LiteralByteString var13 = (LiteralByteString)var12.next();
      int var4 = 0;

      while(true) {
         int var7 = var10.size() - var2;
         int var5 = var13.size() - var3;
         int var6 = Math.min(var7, var5);
         boolean var9;
         if (var2 == 0) {
            var9 = var10.equalsRange(var13, var3, var6);
         } else {
            var9 = var13.equalsRange(var10, var2, var6);
         }

         if (!var9) {
            return false;
         }

         var4 += var6;
         int var8 = this.totalLength;
         if (var4 >= var8) {
            if (var4 == var8) {
               return true;
            }

            throw new IllegalStateException();
         }

         if (var6 == var7) {
            var2 = 0;
            var10 = (LiteralByteString)var11.next();
         } else {
            var2 += var6;
         }

         if (var6 == var5) {
            var3 = 0;
            var13 = (LiteralByteString)var12.next();
         } else {
            var3 += var6;
         }
      }
   }

   static RopeByteString newInstanceForTest(ByteString var0, ByteString var1) {
      return new RopeByteString(var0, var1);
   }

   public ByteBuffer asReadOnlyByteBuffer() {
      return ByteBuffer.wrap(this.toByteArray()).asReadOnlyBuffer();
   }

   public List asReadOnlyByteBufferList() {
      ArrayList var1 = new ArrayList();
      RopeByteString.PieceIterator var2 = new RopeByteString.PieceIterator(this);

      while(var2.hasNext()) {
         var1.add(var2.next().asReadOnlyByteBuffer());
      }

      return var1;
   }

   public byte byteAt(int var1) {
      StringBuilder var3;
      if (var1 >= 0) {
         if (var1 <= this.totalLength) {
            int var2 = this.leftLength;
            return var1 < var2 ? this.left.byteAt(var1) : this.right.byteAt(var1 - var2);
         } else {
            var3 = new StringBuilder();
            var3.append("Index > length: ");
            var3.append(var1);
            var3.append(", ");
            var3.append(this.totalLength);
            throw new ArrayIndexOutOfBoundsException(var3.toString());
         }
      } else {
         var3 = new StringBuilder();
         var3.append("Index < 0: ");
         var3.append(var1);
         throw new ArrayIndexOutOfBoundsException(var3.toString());
      }
   }

   public void copyTo(ByteBuffer var1) {
      this.left.copyTo(var1);
      this.right.copyTo(var1);
   }

   protected void copyToInternal(byte[] var1, int var2, int var3, int var4) {
      int var5 = this.leftLength;
      if (var2 + var4 <= var5) {
         this.left.copyToInternal(var1, var2, var3, var4);
      } else if (var2 >= var5) {
         this.right.copyToInternal(var1, var2 - var5, var3, var4);
      } else {
         var5 -= var2;
         this.left.copyToInternal(var1, var2, var3, var5);
         this.right.copyToInternal(var1, 0, var3 + var5, var4 - var5);
      }
   }

   public boolean equals(Object var1) {
      if (var1 == this) {
         return true;
      } else if (!(var1 instanceof ByteString)) {
         return false;
      } else {
         ByteString var3 = (ByteString)var1;
         if (this.totalLength != var3.size()) {
            return false;
         } else if (this.totalLength == 0) {
            return true;
         } else {
            if (this.hash != 0) {
               int var2 = var3.peekCachedHashCode();
               if (var2 != 0 && this.hash != var2) {
                  return false;
               }
            }

            return this.equalsFragments(var3);
         }
      }
   }

   protected int getTreeDepth() {
      return this.treeDepth;
   }

   public int hashCode() {
      int var2 = this.hash;
      int var1 = var2;
      if (var2 == 0) {
         var2 = this.partialHash(this.totalLength, 0, this.totalLength);
         var1 = var2;
         if (var2 == 0) {
            var1 = 1;
         }

         this.hash = var1;
      }

      return var1;
   }

   protected boolean isBalanced() {
      return this.totalLength >= minLengthByDepth[this.treeDepth];
   }

   public boolean isValidUtf8() {
      ByteString var3 = this.left;
      int var1 = this.leftLength;
      boolean var2 = false;
      var1 = var3.partialIsValidUtf8(0, 0, var1);
      var3 = this.right;
      if (var3.partialIsValidUtf8(var1, 0, var3.size()) == 0) {
         var2 = true;
      }

      return var2;
   }

   public ByteString.ByteIterator iterator() {
      return new RopeByteString.RopeByteIterator();
   }

   public CodedInputStream newCodedInput() {
      return CodedInputStream.newInstance((InputStream)(new RopeByteString.RopeInputStream()));
   }

   public InputStream newInput() {
      return new RopeByteString.RopeInputStream();
   }

   protected int partialHash(int var1, int var2, int var3) {
      int var4 = this.leftLength;
      if (var2 + var3 <= var4) {
         return this.left.partialHash(var1, var2, var3);
      } else if (var2 >= var4) {
         return this.right.partialHash(var1, var2 - var4, var3);
      } else {
         var4 -= var2;
         var1 = this.left.partialHash(var1, var2, var4);
         return this.right.partialHash(var1, 0, var3 - var4);
      }
   }

   protected int partialIsValidUtf8(int var1, int var2, int var3) {
      int var4 = this.leftLength;
      if (var2 + var3 <= var4) {
         return this.left.partialIsValidUtf8(var1, var2, var3);
      } else if (var2 >= var4) {
         return this.right.partialIsValidUtf8(var1, var2 - var4, var3);
      } else {
         var4 -= var2;
         var1 = this.left.partialIsValidUtf8(var1, var2, var4);
         return this.right.partialIsValidUtf8(var1, 0, var3 - var4);
      }
   }

   protected int peekCachedHashCode() {
      return this.hash;
   }

   public int size() {
      return this.totalLength;
   }

   public ByteString substring(int var1, int var2) {
      StringBuilder var5;
      if (var1 >= 0) {
         int var3 = this.totalLength;
         if (var2 <= var3) {
            int var4 = var2 - var1;
            if (var4 >= 0) {
               if (var4 == 0) {
                  return ByteString.EMPTY;
               } else if (var4 == var3) {
                  return this;
               } else {
                  var3 = this.leftLength;
                  if (var2 <= var3) {
                     return this.left.substring(var1, var2);
                  } else {
                     return (ByteString)(var1 >= var3 ? this.right.substring(var1 - var3, var2 - var3) : new RopeByteString(this.left.substring(var1), this.right.substring(0, var2 - this.leftLength)));
                  }
               }
            } else {
               var5 = new StringBuilder();
               var5.append("Beginning index larger than ending index: ");
               var5.append(var1);
               var5.append(", ");
               var5.append(var2);
               throw new IndexOutOfBoundsException(var5.toString());
            }
         } else {
            var5 = new StringBuilder();
            var5.append("End index: ");
            var5.append(var2);
            var5.append(" > ");
            var5.append(this.totalLength);
            throw new IndexOutOfBoundsException(var5.toString());
         }
      } else {
         var5 = new StringBuilder();
         var5.append("Beginning index: ");
         var5.append(var1);
         var5.append(" < 0");
         throw new IndexOutOfBoundsException(var5.toString());
      }
   }

   public String toString(String var1) throws UnsupportedEncodingException {
      return new String(this.toByteArray(), var1);
   }

   public void writeTo(OutputStream var1) throws IOException {
      this.left.writeTo(var1);
      this.right.writeTo(var1);
   }

   private static class Balancer {
      private final Stack prefixesStack;

      private Balancer() {
         this.prefixesStack = new Stack();
      }

      // $FF: synthetic method
      Balancer(Object var1) {
         this();
      }

      private ByteString balance(ByteString var1, ByteString var2) {
         this.doBalance(var1);
         this.doBalance(var2);

         Object var3;
         for(var3 = (ByteString)this.prefixesStack.pop(); !this.prefixesStack.isEmpty(); var3 = new RopeByteString((ByteString)this.prefixesStack.pop(), (ByteString)var3)) {
         }

         return (ByteString)var3;
      }

      private void doBalance(ByteString var1) {
         if (var1.isBalanced()) {
            this.insert(var1);
         } else if (var1 instanceof RopeByteString) {
            RopeByteString var3 = (RopeByteString)var1;
            this.doBalance(var3.left);
            this.doBalance(var3.right);
         } else {
            StringBuilder var2 = new StringBuilder();
            var2.append("Has a new type of ByteString been created? Found ");
            var2.append(var1.getClass());
            throw new IllegalArgumentException(var2.toString());
         }
      }

      private int getDepthBinForLength(int var1) {
         int var2 = Arrays.binarySearch(RopeByteString.minLengthByDepth, var1);
         var1 = var2;
         if (var2 < 0) {
            var1 = -(var2 + 1) - 1;
         }

         return var1;
      }

      private void insert(ByteString var1) {
         int var2 = this.getDepthBinForLength(var1.size());
         int var3 = RopeByteString.minLengthByDepth[var2 + 1];
         if (!this.prefixesStack.isEmpty() && ((ByteString)this.prefixesStack.peek()).size() < var3) {
            var2 = RopeByteString.minLengthByDepth[var2];

            Object var4;
            for(var4 = (ByteString)this.prefixesStack.pop(); !this.prefixesStack.isEmpty() && ((ByteString)this.prefixesStack.peek()).size() < var2; var4 = new RopeByteString((ByteString)this.prefixesStack.pop(), (ByteString)var4)) {
            }

            RopeByteString var5;
            for(var5 = new RopeByteString((ByteString)var4, var1); !this.prefixesStack.isEmpty(); var5 = new RopeByteString((ByteString)this.prefixesStack.pop(), var5)) {
               var2 = this.getDepthBinForLength(var5.size());
               var2 = RopeByteString.minLengthByDepth[var2 + 1];
               if (((ByteString)this.prefixesStack.peek()).size() >= var2) {
                  break;
               }
            }

            this.prefixesStack.push(var5);
         } else {
            this.prefixesStack.push(var1);
         }
      }
   }

   private static class PieceIterator implements Iterator {
      private final Stack breadCrumbs;
      private LiteralByteString next;

      private PieceIterator(ByteString var1) {
         this.breadCrumbs = new Stack();
         this.next = this.getLeafByLeft(var1);
      }

      // $FF: synthetic method
      PieceIterator(ByteString var1, Object var2) {
         this(var1);
      }

      private LiteralByteString getLeafByLeft(ByteString var1) {
         while(var1 instanceof RopeByteString) {
            RopeByteString var2 = (RopeByteString)var1;
            this.breadCrumbs.push(var2);
            var1 = var2.left;
         }

         return (LiteralByteString)var1;
      }

      private LiteralByteString getNextNonEmptyLeaf() {
         LiteralByteString var1;
         do {
            if (this.breadCrumbs.isEmpty()) {
               return null;
            }

            var1 = this.getLeafByLeft(((RopeByteString)this.breadCrumbs.pop()).right);
         } while(var1.isEmpty());

         return var1;
      }

      public boolean hasNext() {
         return this.next != null;
      }

      public LiteralByteString next() {
         if (this.next != null) {
            LiteralByteString var1 = this.next;
            this.next = this.getNextNonEmptyLeaf();
            return var1;
         } else {
            throw new NoSuchElementException();
         }
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }
   }

   private class RopeByteIterator implements ByteString.ByteIterator {
      private ByteString.ByteIterator bytes;
      int bytesRemaining;
      private final RopeByteString.PieceIterator pieces;

      private RopeByteIterator() {
         RopeByteString.PieceIterator var2 = new RopeByteString.PieceIterator(RopeByteString.this);
         this.pieces = var2;
         this.bytes = var2.next().iterator();
         this.bytesRemaining = RopeByteString.this.size();
      }

      // $FF: synthetic method
      RopeByteIterator(Object var2) {
         this();
      }

      public boolean hasNext() {
         return this.bytesRemaining > 0;
      }

      public Byte next() {
         return this.nextByte();
      }

      public byte nextByte() {
         if (!this.bytes.hasNext()) {
            this.bytes = this.pieces.next().iterator();
         }

         --this.bytesRemaining;
         return this.bytes.nextByte();
      }

      public void remove() {
         throw new UnsupportedOperationException();
      }
   }

   private class RopeInputStream extends InputStream {
      private LiteralByteString currentPiece;
      private int currentPieceIndex;
      private int currentPieceOffsetInRope;
      private int currentPieceSize;
      private int mark;
      private RopeByteString.PieceIterator pieceIterator;

      public RopeInputStream() {
         this.initialize();
      }

      private void advanceIfCurrentPieceFullyRead() {
         if (this.currentPiece != null) {
            int var1 = this.currentPieceIndex;
            int var2 = this.currentPieceSize;
            if (var1 == var2) {
               this.currentPieceOffsetInRope += var2;
               this.currentPieceIndex = 0;
               if (this.pieceIterator.hasNext()) {
                  LiteralByteString var3 = this.pieceIterator.next();
                  this.currentPiece = var3;
                  this.currentPieceSize = var3.size();
                  return;
               }

               this.currentPiece = null;
               this.currentPieceSize = 0;
            }
         }

      }

      private void initialize() {
         RopeByteString.PieceIterator var1 = new RopeByteString.PieceIterator(RopeByteString.this);
         this.pieceIterator = var1;
         LiteralByteString var2 = var1.next();
         this.currentPiece = var2;
         this.currentPieceSize = var2.size();
         this.currentPieceIndex = 0;
         this.currentPieceOffsetInRope = 0;
      }

      private int readSkipInternal(byte[] var1, int var2, int var3) {
         int var4 = var2;

         int var5;
         for(var2 = var3; var2 > 0; var4 = var5) {
            this.advanceIfCurrentPieceFullyRead();
            if (this.currentPiece == null) {
               if (var2 == var3) {
                  return -1;
               }
               break;
            }

            int var6 = Math.min(this.currentPieceSize - this.currentPieceIndex, var2);
            var5 = var4;
            if (var1 != null) {
               this.currentPiece.copyTo(var1, this.currentPieceIndex, var4, var6);
               var5 = var4 + var6;
            }

            this.currentPieceIndex += var6;
            var2 -= var6;
         }

         return var3 - var2;
      }

      public int available() throws IOException {
         int var1 = this.currentPieceOffsetInRope;
         int var2 = this.currentPieceIndex;
         return RopeByteString.this.size() - (var1 + var2);
      }

      public void mark(int var1) {
         this.mark = this.currentPieceOffsetInRope + this.currentPieceIndex;
      }

      public boolean markSupported() {
         return true;
      }

      public int read() throws IOException {
         this.advanceIfCurrentPieceFullyRead();
         LiteralByteString var2 = this.currentPiece;
         if (var2 == null) {
            return -1;
         } else {
            int var1 = this.currentPieceIndex++;
            return var2.byteAt(var1) & 255;
         }
      }

      public int read(byte[] var1, int var2, int var3) {
         if (var1 != null) {
            if (var2 >= 0 && var3 >= 0 && var3 <= var1.length - var2) {
               return this.readSkipInternal(var1, var2, var3);
            } else {
               throw new IndexOutOfBoundsException();
            }
         } else {
            throw null;
         }
      }

      public void reset() {
         synchronized(this){}

         try {
            this.initialize();
            this.readSkipInternal((byte[])null, 0, this.mark);
         } finally {
            ;
         }

      }

      public long skip(long var1) {
         if (var1 >= 0L) {
            long var3 = var1;
            if (var1 > 2147483647L) {
               var3 = 2147483647L;
            }

            return (long)this.readSkipInternal((byte[])null, 0, (int)var3);
         } else {
            throw new IndexOutOfBoundsException();
         }
      }
   }
}
