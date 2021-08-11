package com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public abstract class ByteString implements Iterable {
   // $FF: synthetic field
   static final boolean $assertionsDisabled = false;
   static final int CONCATENATE_BY_COPY_SIZE = 128;
   public static final ByteString EMPTY = new LiteralByteString(new byte[0]);
   static final int MAX_READ_FROM_CHUNK_SIZE = 8192;
   static final int MIN_READ_FROM_CHUNK_SIZE = 256;

   ByteString() {
   }

   private static ByteString balancedConcat(Iterator var0, int var1) {
      if (var1 == 1) {
         return (ByteString)var0.next();
      } else {
         int var2 = var1 >>> 1;
         return balancedConcat(var0, var2).concat(balancedConcat(var0, var1 - var2));
      }
   }

   public static ByteString copyFrom(Iterable var0) {
      Object var3;
      if (!(var0 instanceof Collection)) {
         ArrayList var1 = new ArrayList();
         Iterator var2 = var0.iterator();

         while(var2.hasNext()) {
            var1.add((ByteString)var2.next());
         }

         var3 = var1;
      } else {
         var3 = (Collection)var0;
      }

      return ((Collection)var3).isEmpty() ? EMPTY : balancedConcat(((Collection)var3).iterator(), ((Collection)var3).size());
   }

   public static ByteString copyFrom(String var0, String var1) throws UnsupportedEncodingException {
      return new LiteralByteString(var0.getBytes(var1));
   }

   public static ByteString copyFrom(ByteBuffer var0) {
      return copyFrom(var0, var0.remaining());
   }

   public static ByteString copyFrom(ByteBuffer var0, int var1) {
      byte[] var2 = new byte[var1];
      var0.get(var2);
      return new LiteralByteString(var2);
   }

   public static ByteString copyFrom(byte[] var0) {
      return copyFrom(var0, 0, var0.length);
   }

   public static ByteString copyFrom(byte[] var0, int var1, int var2) {
      byte[] var3 = new byte[var2];
      System.arraycopy(var0, var1, var3, 0, var2);
      return new LiteralByteString(var3);
   }

   public static ByteString copyFromUtf8(String var0) {
      try {
         LiteralByteString var2 = new LiteralByteString(var0.getBytes("UTF-8"));
         return var2;
      } catch (UnsupportedEncodingException var1) {
         throw new RuntimeException("UTF-8 not supported?", var1);
      }
   }

   static ByteString.CodedBuilder newCodedBuilder(int var0) {
      return new ByteString.CodedBuilder(var0);
   }

   public static ByteString.Output newOutput() {
      return new ByteString.Output(128);
   }

   public static ByteString.Output newOutput(int var0) {
      return new ByteString.Output(var0);
   }

   private static ByteString readChunk(InputStream var0, int var1) throws IOException {
      byte[] var4 = new byte[var1];

      int var2;
      int var3;
      for(var2 = 0; var2 < var1; var2 += var3) {
         var3 = var0.read(var4, var2, var1 - var2);
         if (var3 == -1) {
            break;
         }
      }

      return var2 == 0 ? null : copyFrom(var4, 0, var2);
   }

   public static ByteString readFrom(InputStream var0) throws IOException {
      return readFrom(var0, 256, 8192);
   }

   public static ByteString readFrom(InputStream var0, int var1) throws IOException {
      return readFrom(var0, var1, var1);
   }

   public static ByteString readFrom(InputStream var0, int var1, int var2) throws IOException {
      ArrayList var3 = new ArrayList();

      while(true) {
         ByteString var4 = readChunk(var0, var1);
         if (var4 == null) {
            return copyFrom((Iterable)var3);
         }

         var3.add(var4);
         var1 = Math.min(var1 * 2, var2);
      }
   }

   public abstract ByteBuffer asReadOnlyByteBuffer();

   public abstract List asReadOnlyByteBufferList();

   public abstract byte byteAt(int var1);

   public ByteString concat(ByteString var1) {
      int var2 = this.size();
      int var3 = var1.size();
      if ((long)var2 + (long)var3 < 2147483647L) {
         return RopeByteString.concatenate(this, var1);
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("ByteString would be too long: ");
         var4.append(var2);
         var4.append("+");
         var4.append(var3);
         throw new IllegalArgumentException(var4.toString());
      }
   }

   public abstract void copyTo(ByteBuffer var1);

   public void copyTo(byte[] var1, int var2) {
      this.copyTo(var1, 0, var2, this.size());
   }

   public void copyTo(byte[] var1, int var2, int var3, int var4) {
      StringBuilder var5;
      if (var2 >= 0) {
         if (var3 >= 0) {
            if (var4 >= 0) {
               if (var2 + var4 <= this.size()) {
                  if (var3 + var4 <= var1.length) {
                     if (var4 > 0) {
                        this.copyToInternal(var1, var2, var3, var4);
                     }

                  } else {
                     var5 = new StringBuilder();
                     var5.append("Target end offset < 0: ");
                     var5.append(var3 + var4);
                     throw new IndexOutOfBoundsException(var5.toString());
                  }
               } else {
                  var5 = new StringBuilder();
                  var5.append("Source end offset < 0: ");
                  var5.append(var2 + var4);
                  throw new IndexOutOfBoundsException(var5.toString());
               }
            } else {
               var5 = new StringBuilder();
               var5.append("Length < 0: ");
               var5.append(var4);
               throw new IndexOutOfBoundsException(var5.toString());
            }
         } else {
            var5 = new StringBuilder();
            var5.append("Target offset < 0: ");
            var5.append(var3);
            throw new IndexOutOfBoundsException(var5.toString());
         }
      } else {
         var5 = new StringBuilder();
         var5.append("Source offset < 0: ");
         var5.append(var2);
         throw new IndexOutOfBoundsException(var5.toString());
      }
   }

   protected abstract void copyToInternal(byte[] var1, int var2, int var3, int var4);

   public abstract boolean equals(Object var1);

   protected abstract int getTreeDepth();

   public abstract int hashCode();

   protected abstract boolean isBalanced();

   public boolean isEmpty() {
      return this.size() == 0;
   }

   public abstract boolean isValidUtf8();

   public abstract ByteString.ByteIterator iterator();

   public abstract CodedInputStream newCodedInput();

   public abstract InputStream newInput();

   protected abstract int partialHash(int var1, int var2, int var3);

   protected abstract int partialIsValidUtf8(int var1, int var2, int var3);

   protected abstract int peekCachedHashCode();

   public abstract int size();

   public boolean startsWith(ByteString var1) {
      int var2 = this.size();
      int var3 = var1.size();
      boolean var5 = false;
      boolean var4 = var5;
      if (var2 >= var3) {
         var4 = var5;
         if (this.substring(0, var1.size()).equals(var1)) {
            var4 = true;
         }
      }

      return var4;
   }

   public ByteString substring(int var1) {
      return this.substring(var1, this.size());
   }

   public abstract ByteString substring(int var1, int var2);

   public byte[] toByteArray() {
      int var1 = this.size();
      byte[] var2 = new byte[var1];
      this.copyToInternal(var2, 0, 0, var1);
      return var2;
   }

   public String toString() {
      return String.format("<ByteString@%s size=%d>", Integer.toHexString(System.identityHashCode(this)), this.size());
   }

   public abstract String toString(String var1) throws UnsupportedEncodingException;

   public String toStringUtf8() {
      try {
         String var1 = this.toString("UTF-8");
         return var1;
      } catch (UnsupportedEncodingException var2) {
         throw new RuntimeException("UTF-8 not supported?", var2);
      }
   }

   public abstract void writeTo(OutputStream var1) throws IOException;

   public interface ByteIterator extends Iterator {
      byte nextByte();
   }

   static final class CodedBuilder {
      private final byte[] buffer;
      private final CodedOutputStream output;

      private CodedBuilder(int var1) {
         byte[] var2 = new byte[var1];
         this.buffer = var2;
         this.output = CodedOutputStream.newInstance(var2);
      }

      // $FF: synthetic method
      CodedBuilder(int var1, Object var2) {
         this(var1);
      }

      public ByteString build() {
         this.output.checkNoSpaceLeft();
         return new LiteralByteString(this.buffer);
      }

      public CodedOutputStream getCodedOutput() {
         return this.output;
      }
   }

   public static final class Output extends OutputStream {
      private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
      private byte[] buffer;
      private int bufferPos;
      private final ArrayList flushedBuffers;
      private int flushedBuffersTotalBytes;
      private final int initialCapacity;

      Output(int var1) {
         if (var1 >= 0) {
            this.initialCapacity = var1;
            this.flushedBuffers = new ArrayList();
            this.buffer = new byte[var1];
         } else {
            throw new IllegalArgumentException("Buffer size < 0");
         }
      }

      private byte[] copyArray(byte[] var1, int var2) {
         byte[] var3 = new byte[var2];
         System.arraycopy(var1, 0, var3, 0, Math.min(var1.length, var2));
         return var3;
      }

      private void flushFullBuffer(int var1) {
         this.flushedBuffers.add(new LiteralByteString(this.buffer));
         int var2 = this.flushedBuffersTotalBytes + this.buffer.length;
         this.flushedBuffersTotalBytes = var2;
         this.buffer = new byte[Math.max(this.initialCapacity, Math.max(var1, var2 >>> 1))];
         this.bufferPos = 0;
      }

      private void flushLastBuffer() {
         int var1 = this.bufferPos;
         byte[] var2 = this.buffer;
         if (var1 < var2.length) {
            if (var1 > 0) {
               var2 = this.copyArray(var2, var1);
               this.flushedBuffers.add(new LiteralByteString(var2));
            }
         } else {
            this.flushedBuffers.add(new LiteralByteString(var2));
            this.buffer = EMPTY_BYTE_ARRAY;
         }

         this.flushedBuffersTotalBytes += this.bufferPos;
         this.bufferPos = 0;
      }

      public void reset() {
         synchronized(this){}

         try {
            this.flushedBuffers.clear();
            this.flushedBuffersTotalBytes = 0;
            this.bufferPos = 0;
         } finally {
            ;
         }

      }

      public int size() {
         synchronized(this){}

         int var1;
         int var2;
         try {
            var1 = this.flushedBuffersTotalBytes;
            var2 = this.bufferPos;
         } finally {
            ;
         }

         return var1 + var2;
      }

      public ByteString toByteString() {
         synchronized(this){}

         ByteString var1;
         try {
            this.flushLastBuffer();
            var1 = ByteString.copyFrom((Iterable)this.flushedBuffers);
         } finally {
            ;
         }

         return var1;
      }

      public String toString() {
         return String.format("<ByteString.Output@%s size=%d>", Integer.toHexString(System.identityHashCode(this)), this.size());
      }

      public void write(int var1) {
         synchronized(this){}
         boolean var5 = false;

         int var2;
         byte[] var3;
         try {
            var5 = true;
            if (this.bufferPos == this.buffer.length) {
               this.flushFullBuffer(1);
            }

            var3 = this.buffer;
            var2 = this.bufferPos++;
            var5 = false;
         } finally {
            if (var5) {
               ;
            }
         }

         var3[var2] = (byte)var1;
      }

      public void write(byte[] var1, int var2, int var3) {
         synchronized(this){}

         Throwable var10000;
         label133: {
            boolean var10001;
            try {
               if (var3 <= this.buffer.length - this.bufferPos) {
                  System.arraycopy(var1, var2, this.buffer, this.bufferPos, var3);
                  this.bufferPos += var3;
                  return;
               }
            } catch (Throwable var16) {
               var10000 = var16;
               var10001 = false;
               break label133;
            }

            int var4;
            try {
               var4 = this.buffer.length - this.bufferPos;
               System.arraycopy(var1, var2, this.buffer, this.bufferPos, var4);
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label133;
            }

            var3 -= var4;

            label118:
            try {
               this.flushFullBuffer(var3);
               System.arraycopy(var1, var2 + var4, this.buffer, 0, var3);
               this.bufferPos = var3;
               return;
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label118;
            }
         }

         Throwable var17 = var10000;
         throw var17;
      }

      public void writeTo(OutputStream param1) throws IOException {
         // $FF: Couldn't be decompiled
      }
   }
}
