package org.apache.commons.codec.binary;

import java.util.Arrays;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

public abstract class BaseNCodec implements BinaryEncoder, BinaryDecoder {
   private static final int DEFAULT_BUFFER_RESIZE_FACTOR = 2;
   private static final int DEFAULT_BUFFER_SIZE = 8192;
   static final int EOF = -1;
   protected static final int MASK_8BITS = 255;
   private static final int MAX_BUFFER_SIZE = 2147483639;
   public static final int MIME_CHUNK_SIZE = 76;
   protected static final byte PAD_DEFAULT = 61;
   public static final int PEM_CHUNK_SIZE = 64;
   @Deprecated
   protected final byte PAD;
   private final int chunkSeparatorLength;
   private final int encodedBlockSize;
   protected final int lineLength;
   protected final byte pad;
   private final int unencodedBlockSize;

   protected BaseNCodec(int var1, int var2, int var3, int var4) {
      this(var1, var2, var3, var4, (byte)61);
   }

   protected BaseNCodec(int var1, int var2, int var3, int var4, byte var5) {
      this.PAD = 61;
      this.unencodedBlockSize = var1;
      this.encodedBlockSize = var2;
      int var6 = 0;
      boolean var7;
      if (var3 > 0 && var4 > 0) {
         var7 = true;
      } else {
         var7 = false;
      }

      if (var7) {
         var6 = var3 / var2 * var2;
      }

      this.lineLength = var6;
      this.chunkSeparatorLength = var4;
      this.pad = var5;
   }

   private static int compareUnsigned(int var0, int var1) {
      return Integer.compare(var0 - Integer.MIN_VALUE, Integer.MIN_VALUE + var1);
   }

   private static int createPositiveCapacity(int var0) {
      if (var0 >= 0) {
         int var1 = 2147483639;
         if (var0 > 2147483639) {
            var1 = var0;
         }

         return var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Unable to allocate array size: ");
         var2.append((long)var0 & 4294967295L);
         throw new OutOfMemoryError(var2.toString());
      }
   }

   protected static boolean isWhiteSpace(byte var0) {
      return var0 == 9 || var0 == 10 || var0 == 13 || var0 == 32;
   }

   private static byte[] resizeBuffer(BaseNCodec.Context var0, int var1) {
      int var3 = var0.buffer.length * 2;
      int var2 = var3;
      if (compareUnsigned(var3, var1) < 0) {
         var2 = var1;
      }

      var3 = var2;
      if (compareUnsigned(var2, 2147483639) > 0) {
         var3 = createPositiveCapacity(var1);
      }

      byte[] var4 = new byte[var3];
      System.arraycopy(var0.buffer, 0, var4, 0, var0.buffer.length);
      var0.buffer = var4;
      return var4;
   }

   int available(BaseNCodec.Context var1) {
      return var1.buffer != null ? var1.pos - var1.readPos : 0;
   }

   protected boolean containsAlphabetOrPad(byte[] var1) {
      if (var1 == null) {
         return false;
      } else {
         int var4 = var1.length;

         for(int var3 = 0; var3 < var4; ++var3) {
            byte var2 = var1[var3];
            if (this.pad == var2 || this.isInAlphabet(var2)) {
               return true;
            }
         }

         return false;
      }
   }

   public Object decode(Object var1) throws DecoderException {
      if (var1 instanceof byte[]) {
         return this.decode((byte[])((byte[])var1));
      } else if (var1 instanceof String) {
         return this.decode((String)var1);
      } else {
         throw new DecoderException("Parameter supplied to Base-N decode is not a byte[] or a String");
      }
   }

   abstract void decode(byte[] var1, int var2, int var3, BaseNCodec.Context var4);

   public byte[] decode(String var1) {
      return this.decode(StringUtils.getBytesUtf8(var1));
   }

   public byte[] decode(byte[] var1) {
      if (var1 != null) {
         if (var1.length == 0) {
            return var1;
         } else {
            BaseNCodec.Context var2 = new BaseNCodec.Context();
            this.decode(var1, 0, var1.length, var2);
            this.decode(var1, 0, -1, var2);
            var1 = new byte[var2.pos];
            this.readResults(var1, 0, var1.length, var2);
            return var1;
         }
      } else {
         return var1;
      }
   }

   public Object encode(Object var1) throws EncoderException {
      if (var1 instanceof byte[]) {
         return this.encode((byte[])((byte[])var1));
      } else {
         throw new EncoderException("Parameter supplied to Base-N encode is not a byte[]");
      }
   }

   abstract void encode(byte[] var1, int var2, int var3, BaseNCodec.Context var4);

   public byte[] encode(byte[] var1) {
      if (var1 != null) {
         return var1.length == 0 ? var1 : this.encode(var1, 0, var1.length);
      } else {
         return var1;
      }
   }

   public byte[] encode(byte[] var1, int var2, int var3) {
      if (var1 != null) {
         if (var1.length == 0) {
            return var1;
         } else {
            BaseNCodec.Context var4 = new BaseNCodec.Context();
            this.encode(var1, var2, var3, var4);
            this.encode(var1, var2, -1, var4);
            var1 = new byte[var4.pos - var4.readPos];
            this.readResults(var1, 0, var1.length, var4);
            return var1;
         }
      } else {
         return var1;
      }
   }

   public String encodeAsString(byte[] var1) {
      return StringUtils.newStringUtf8(this.encode(var1));
   }

   public String encodeToString(byte[] var1) {
      return StringUtils.newStringUtf8(this.encode(var1));
   }

   protected byte[] ensureBufferSize(int var1, BaseNCodec.Context var2) {
      if (var2.buffer == null) {
         var2.buffer = new byte[this.getDefaultBufferSize()];
         var2.pos = 0;
         var2.readPos = 0;
      } else if (var2.pos + var1 - var2.buffer.length > 0) {
         return resizeBuffer(var2, var2.pos + var1);
      }

      return var2.buffer;
   }

   protected int getDefaultBufferSize() {
      return 8192;
   }

   public long getEncodedLength(byte[] var1) {
      int var2 = var1.length;
      int var3 = this.unencodedBlockSize;
      long var6 = (long)((var2 + var3 - 1) / var3) * (long)this.encodedBlockSize;
      var2 = this.lineLength;
      long var4 = var6;
      if (var2 > 0) {
         var4 = var6 + ((long)var2 + var6 - 1L) / (long)var2 * (long)this.chunkSeparatorLength;
      }

      return var4;
   }

   boolean hasData(BaseNCodec.Context var1) {
      return var1.buffer != null;
   }

   protected abstract boolean isInAlphabet(byte var1);

   public boolean isInAlphabet(String var1) {
      return this.isInAlphabet(StringUtils.getBytesUtf8(var1), true);
   }

   public boolean isInAlphabet(byte[] var1, boolean var2) {
      int var5 = var1.length;

      for(int var4 = 0; var4 < var5; ++var4) {
         byte var3 = var1[var4];
         if (!this.isInAlphabet(var3) && (!var2 || var3 != this.pad && !isWhiteSpace(var3))) {
            return false;
         }
      }

      return true;
   }

   int readResults(byte[] var1, int var2, int var3, BaseNCodec.Context var4) {
      if (var4.buffer != null) {
         var3 = Math.min(this.available(var4), var3);
         System.arraycopy(var4.buffer, var4.readPos, var1, var2, var3);
         var4.readPos += var3;
         if (var4.readPos >= var4.pos) {
            var4.buffer = null;
         }

         return var3;
      } else {
         return var4.eof ? -1 : 0;
      }
   }

   static class Context {
      byte[] buffer;
      int currentLinePos;
      boolean eof;
      int ibitWorkArea;
      long lbitWorkArea;
      int modulus;
      int pos;
      int readPos;

      public String toString() {
         return String.format("%s[buffer=%s, currentLinePos=%s, eof=%s, ibitWorkArea=%s, lbitWorkArea=%s, modulus=%s, pos=%s, readPos=%s]", this.getClass().getSimpleName(), Arrays.toString(this.buffer), this.currentLinePos, this.eof, this.ibitWorkArea, this.lbitWorkArea, this.modulus, this.pos, this.readPos);
      }
   }
}
