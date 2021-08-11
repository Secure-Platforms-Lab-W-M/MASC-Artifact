package org.apache.commons.codec.net;

import java.nio.ByteBuffer;
import java.util.BitSet;
import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

public class PercentCodec implements BinaryEncoder, BinaryDecoder {
   private static final byte ESCAPE_CHAR = 37;
   private final BitSet alwaysEncodeChars = new BitSet();
   private int alwaysEncodeCharsMax = Integer.MIN_VALUE;
   private int alwaysEncodeCharsMin = Integer.MAX_VALUE;
   private final boolean plusForSpace;

   public PercentCodec() {
      this.plusForSpace = false;
      this.insertAlwaysEncodeChar((byte)37);
   }

   public PercentCodec(byte[] var1, boolean var2) {
      this.plusForSpace = var2;
      this.insertAlwaysEncodeChars(var1);
   }

   private boolean canEncode(byte var1) {
      return !this.isAsciiChar(var1) || this.inAlwaysEncodeCharsRange(var1) && this.alwaysEncodeChars.get(var1);
   }

   private boolean containsSpace(byte[] var1) {
      int var3 = var1.length;

      for(int var2 = 0; var2 < var3; ++var2) {
         if (var1[var2] == 32) {
            return true;
         }
      }

      return false;
   }

   private byte[] doEncode(byte[] var1, int var2, boolean var3) {
      ByteBuffer var8 = ByteBuffer.allocate(var2);
      int var7 = var1.length;

      for(var2 = 0; var2 < var7; ++var2) {
         byte var4 = var1[var2];
         if (var3 && this.canEncode(var4)) {
            byte var5 = var4;
            if (var4 < 0) {
               var5 = (byte)(var4 + 256);
            }

            char var6 = Utils.hexDigit(var5 >> 4);
            char var9 = Utils.hexDigit(var5);
            var8.put((byte)37);
            var8.put((byte)var6);
            var8.put((byte)var9);
         } else if (this.plusForSpace && var4 == 32) {
            var8.put((byte)43);
         } else {
            var8.put(var4);
         }
      }

      return var8.array();
   }

   private int expectedDecodingBytes(byte[] var1) {
      int var2 = 0;

      for(int var3 = 0; var3 < var1.length; ++var2) {
         byte var4;
         if (var1[var3] == 37) {
            var4 = 3;
         } else {
            var4 = 1;
         }

         var3 += var4;
      }

      return var2;
   }

   private int expectedEncodingBytes(byte[] var1) {
      int var3 = 0;
      int var5 = var1.length;

      for(int var2 = 0; var2 < var5; ++var2) {
         byte var4;
         if (this.canEncode(var1[var2])) {
            var4 = 3;
         } else {
            var4 = 1;
         }

         var3 += var4;
      }

      return var3;
   }

   private boolean inAlwaysEncodeCharsRange(byte var1) {
      return var1 >= this.alwaysEncodeCharsMin && var1 <= this.alwaysEncodeCharsMax;
   }

   private void insertAlwaysEncodeChar(byte var1) {
      this.alwaysEncodeChars.set(var1);
      if (var1 < this.alwaysEncodeCharsMin) {
         this.alwaysEncodeCharsMin = var1;
      }

      if (var1 > this.alwaysEncodeCharsMax) {
         this.alwaysEncodeCharsMax = var1;
      }

   }

   private void insertAlwaysEncodeChars(byte[] var1) {
      if (var1 != null) {
         int var3 = var1.length;

         for(int var2 = 0; var2 < var3; ++var2) {
            this.insertAlwaysEncodeChar(var1[var2]);
         }
      }

      this.insertAlwaysEncodeChar((byte)37);
   }

   private boolean isAsciiChar(byte var1) {
      return var1 >= 0;
   }

   public Object decode(Object var1) throws DecoderException {
      if (var1 == null) {
         return null;
      } else if (var1 instanceof byte[]) {
         return this.decode((byte[])((byte[])var1));
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Objects of type ");
         var2.append(var1.getClass().getName());
         var2.append(" cannot be Percent decoded");
         throw new DecoderException(var2.toString());
      }
   }

   public byte[] decode(byte[] var1) throws DecoderException {
      if (var1 == null) {
         return null;
      } else {
         ByteBuffer var5 = ByteBuffer.allocate(this.expectedDecodingBytes(var1));

         for(int var3 = 0; var3 < var1.length; ++var3) {
            byte var2 = var1[var3];
            if (var2 == 37) {
               ++var3;

               ArrayIndexOutOfBoundsException var10000;
               label52: {
                  boolean var10001;
                  int var4;
                  try {
                     var4 = Utils.digit16(var1[var3]);
                  } catch (ArrayIndexOutOfBoundsException var7) {
                     var10000 = var7;
                     var10001 = false;
                     break label52;
                  }

                  ++var3;

                  try {
                     var5.put((byte)((var4 << 4) + Utils.digit16(var1[var3])));
                     continue;
                  } catch (ArrayIndexOutOfBoundsException var6) {
                     var10000 = var6;
                     var10001 = false;
                  }
               }

               ArrayIndexOutOfBoundsException var8 = var10000;
               throw new DecoderException("Invalid percent decoding: ", var8);
            } else if (this.plusForSpace && var2 == 43) {
               var5.put((byte)32);
            } else {
               var5.put(var2);
            }
         }

         return var5.array();
      }
   }

   public Object encode(Object var1) throws EncoderException {
      if (var1 == null) {
         return null;
      } else if (var1 instanceof byte[]) {
         return this.encode((byte[])((byte[])var1));
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Objects of type ");
         var2.append(var1.getClass().getName());
         var2.append(" cannot be Percent encoded");
         throw new EncoderException(var2.toString());
      }
   }

   public byte[] encode(byte[] var1) throws EncoderException {
      if (var1 == null) {
         return null;
      } else {
         int var2 = this.expectedEncodingBytes(var1);
         boolean var3;
         if (var2 != var1.length) {
            var3 = true;
         } else {
            var3 = false;
         }

         return var3 || this.plusForSpace && this.containsSpace(var1) ? this.doEncode(var1, var2, var3) : var1;
      }
   }
}
