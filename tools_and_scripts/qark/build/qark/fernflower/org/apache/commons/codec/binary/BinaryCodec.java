package org.apache.commons.codec.binary;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;

public class BinaryCodec implements BinaryDecoder, BinaryEncoder {
   private static final int[] BITS = new int[]{1, 2, 4, 8, 16, 32, 64, 128};
   private static final int BIT_0 = 1;
   private static final int BIT_1 = 2;
   private static final int BIT_2 = 4;
   private static final int BIT_3 = 8;
   private static final int BIT_4 = 16;
   private static final int BIT_5 = 32;
   private static final int BIT_6 = 64;
   private static final int BIT_7 = 128;
   private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
   private static final char[] EMPTY_CHAR_ARRAY = new char[0];

   public static byte[] fromAscii(byte[] var0) {
      if (isEmpty(var0)) {
         return EMPTY_BYTE_ARRAY;
      } else {
         byte[] var5 = new byte[var0.length >> 3];
         int var2 = 0;

         for(int var1 = var0.length - 1; var2 < var5.length; var1 -= 8) {
            int var3 = 0;

            while(true) {
               int[] var6 = BITS;
               if (var3 >= var6.length) {
                  ++var2;
                  break;
               }

               if (var0[var1 - var3] == 49) {
                  byte var4 = var5[var2];
                  var5[var2] = (byte)(var6[var3] | var4);
               }

               ++var3;
            }
         }

         return var5;
      }
   }

   public static byte[] fromAscii(char[] var0) {
      if (var0 != null && var0.length != 0) {
         byte[] var5 = new byte[var0.length >> 3];
         int var2 = 0;

         for(int var1 = var0.length - 1; var2 < var5.length; var1 -= 8) {
            int var3 = 0;

            while(true) {
               int[] var6 = BITS;
               if (var3 >= var6.length) {
                  ++var2;
                  break;
               }

               if (var0[var1 - var3] == '1') {
                  byte var4 = var5[var2];
                  var5[var2] = (byte)(var6[var3] | var4);
               }

               ++var3;
            }
         }

         return var5;
      } else {
         return EMPTY_BYTE_ARRAY;
      }
   }

   private static boolean isEmpty(byte[] var0) {
      return var0 == null || var0.length == 0;
   }

   public static byte[] toAsciiBytes(byte[] var0) {
      if (isEmpty(var0)) {
         return EMPTY_BYTE_ARRAY;
      } else {
         byte[] var5 = new byte[var0.length << 3];
         int var2 = 0;

         for(int var1 = var5.length - 1; var2 < var0.length; var1 -= 8) {
            int var3 = 0;

            while(true) {
               int[] var6 = BITS;
               if (var3 >= var6.length) {
                  ++var2;
                  break;
               }

               byte var4 = var0[var2];
               if ((var6[var3] & var4) == 0) {
                  var5[var1 - var3] = 48;
               } else {
                  var5[var1 - var3] = 49;
               }

               ++var3;
            }
         }

         return var5;
      }
   }

   public static char[] toAsciiChars(byte[] var0) {
      if (isEmpty(var0)) {
         return EMPTY_CHAR_ARRAY;
      } else {
         char[] var5 = new char[var0.length << 3];
         int var2 = 0;

         for(int var1 = var5.length - 1; var2 < var0.length; var1 -= 8) {
            int var3 = 0;

            while(true) {
               int[] var6 = BITS;
               if (var3 >= var6.length) {
                  ++var2;
                  break;
               }

               byte var4 = var0[var2];
               if ((var6[var3] & var4) == 0) {
                  var5[var1 - var3] = '0';
               } else {
                  var5[var1 - var3] = '1';
               }

               ++var3;
            }
         }

         return var5;
      }
   }

   public static String toAsciiString(byte[] var0) {
      return new String(toAsciiChars(var0));
   }

   public Object decode(Object var1) throws DecoderException {
      if (var1 == null) {
         return EMPTY_BYTE_ARRAY;
      } else if (var1 instanceof byte[]) {
         return fromAscii((byte[])((byte[])var1));
      } else if (var1 instanceof char[]) {
         return fromAscii((char[])((char[])var1));
      } else if (var1 instanceof String) {
         return fromAscii(((String)var1).toCharArray());
      } else {
         throw new DecoderException("argument not a byte array");
      }
   }

   public byte[] decode(byte[] var1) {
      return fromAscii(var1);
   }

   public Object encode(Object var1) throws EncoderException {
      if (var1 instanceof byte[]) {
         return toAsciiChars((byte[])((byte[])var1));
      } else {
         throw new EncoderException("argument not a byte array");
      }
   }

   public byte[] encode(byte[] var1) {
      return toAsciiBytes(var1);
   }

   public byte[] toByteArray(String var1) {
      return var1 == null ? EMPTY_BYTE_ARRAY : fromAscii(var1.toCharArray());
   }
}
