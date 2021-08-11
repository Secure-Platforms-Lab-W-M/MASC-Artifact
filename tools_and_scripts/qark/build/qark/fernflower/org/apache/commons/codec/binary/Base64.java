package org.apache.commons.codec.binary;

import java.math.BigInteger;
import java.util.Objects;

public class Base64 extends BaseNCodec {
   private static final int BITS_PER_ENCODED_BYTE = 6;
   private static final int BYTES_PER_ENCODED_BLOCK = 4;
   private static final int BYTES_PER_UNENCODED_BLOCK = 3;
   static final byte[] CHUNK_SEPARATOR = new byte[]{13, 10};
   private static final byte[] DECODE_TABLE = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, 62, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51};
   private static final int MASK_2BITS = 3;
   private static final int MASK_4BITS = 15;
   private static final int MASK_6BITS = 63;
   private static final byte[] STANDARD_ENCODE_TABLE = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 43, 47};
   private static final byte[] URL_SAFE_ENCODE_TABLE = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 45, 95};
   private final int decodeSize;
   private final byte[] decodeTable;
   private final int encodeSize;
   private final byte[] encodeTable;
   private final byte[] lineSeparator;

   public Base64() {
      this(0);
   }

   public Base64(int var1) {
      this(var1, CHUNK_SEPARATOR);
   }

   public Base64(int var1, byte[] var2) {
      this(var1, var2, false);
   }

   public Base64(int var1, byte[] var2, boolean var3) {
      int var4;
      if (var2 == null) {
         var4 = 0;
      } else {
         var4 = var2.length;
      }

      super(3, 4, var1, var4);
      this.decodeTable = DECODE_TABLE;
      if (var2 != null) {
         if (this.containsAlphabetOrPad(var2)) {
            String var6 = StringUtils.newStringUtf8(var2);
            StringBuilder var7 = new StringBuilder();
            var7.append("lineSeparator must not contain base64 characters: [");
            var7.append(var6);
            var7.append("]");
            throw new IllegalArgumentException(var7.toString());
         }

         if (var1 > 0) {
            this.encodeSize = var2.length + 4;
            byte[] var5 = new byte[var2.length];
            this.lineSeparator = var5;
            System.arraycopy(var2, 0, var5, 0, var2.length);
         } else {
            this.encodeSize = 4;
            this.lineSeparator = null;
         }
      } else {
         this.encodeSize = 4;
         this.lineSeparator = null;
      }

      this.decodeSize = this.encodeSize - 1;
      if (var3) {
         var2 = URL_SAFE_ENCODE_TABLE;
      } else {
         var2 = STANDARD_ENCODE_TABLE;
      }

      this.encodeTable = var2;
   }

   public Base64(boolean var1) {
      this(76, CHUNK_SEPARATOR, var1);
   }

   public static byte[] decodeBase64(String var0) {
      return (new Base64()).decode(var0);
   }

   public static byte[] decodeBase64(byte[] var0) {
      return (new Base64()).decode(var0);
   }

   public static BigInteger decodeInteger(byte[] var0) {
      return new BigInteger(1, decodeBase64(var0));
   }

   public static byte[] encodeBase64(byte[] var0) {
      return encodeBase64(var0, false);
   }

   public static byte[] encodeBase64(byte[] var0, boolean var1) {
      return encodeBase64(var0, var1, false);
   }

   public static byte[] encodeBase64(byte[] var0, boolean var1, boolean var2) {
      return encodeBase64(var0, var1, var2, Integer.MAX_VALUE);
   }

   public static byte[] encodeBase64(byte[] var0, boolean var1, boolean var2, int var3) {
      if (var0 != null) {
         if (var0.length == 0) {
            return var0;
         } else {
            Base64 var6;
            if (var1) {
               var6 = new Base64(var2);
            } else {
               var6 = new Base64(0, CHUNK_SEPARATOR, var2);
            }

            long var4 = var6.getEncodedLength(var0);
            if (var4 <= (long)var3) {
               return var6.encode(var0);
            } else {
               StringBuilder var7 = new StringBuilder();
               var7.append("Input array too big, the output array would be bigger (");
               var7.append(var4);
               var7.append(") than the specified maximum size of ");
               var7.append(var3);
               throw new IllegalArgumentException(var7.toString());
            }
         }
      } else {
         return var0;
      }
   }

   public static byte[] encodeBase64Chunked(byte[] var0) {
      return encodeBase64(var0, true);
   }

   public static String encodeBase64String(byte[] var0) {
      return StringUtils.newStringUsAscii(encodeBase64(var0, false));
   }

   public static byte[] encodeBase64URLSafe(byte[] var0) {
      return encodeBase64(var0, false, true);
   }

   public static String encodeBase64URLSafeString(byte[] var0) {
      return StringUtils.newStringUsAscii(encodeBase64(var0, false, true));
   }

   public static byte[] encodeInteger(BigInteger var0) {
      Objects.requireNonNull(var0, "bigInteger");
      return encodeBase64(toIntegerBytes(var0), false);
   }

   @Deprecated
   public static boolean isArrayByteBase64(byte[] var0) {
      return isBase64(var0);
   }

   public static boolean isBase64(byte var0) {
      if (var0 != 61) {
         if (var0 < 0) {
            return false;
         }

         byte[] var1 = DECODE_TABLE;
         if (var0 >= var1.length || var1[var0] == -1) {
            return false;
         }
      }

      return true;
   }

   public static boolean isBase64(String var0) {
      return isBase64(StringUtils.getBytesUtf8(var0));
   }

   public static boolean isBase64(byte[] var0) {
      for(int var1 = 0; var1 < var0.length; ++var1) {
         if (!isBase64(var0[var1]) && !isWhiteSpace(var0[var1])) {
            return false;
         }
      }

      return true;
   }

   static byte[] toIntegerBytes(BigInteger var0) {
      int var4 = var0.bitLength() + 7 >> 3 << 3;
      byte[] var5 = var0.toByteArray();
      if (var0.bitLength() % 8 != 0 && var0.bitLength() / 8 + 1 == var4 / 8) {
         return var5;
      } else {
         byte var2 = 0;
         int var3 = var5.length;
         int var1 = var3;
         if (var0.bitLength() % 8 == 0) {
            var2 = 1;
            var1 = var3 - 1;
         }

         var3 = var4 / 8;
         byte[] var6 = new byte[var4 / 8];
         System.arraycopy(var5, var2, var6, var3 - var1, var1);
         return var6;
      }
   }

   private static void validateCharacter(int var0, BaseNCodec.Context var1) {
      if ((var1.ibitWorkArea & var0) != 0) {
         throw new IllegalArgumentException("Last encoded character (before the paddings if any) is a valid base 64 alphabet but not a possible value. Expected the discarded bits to be zero.");
      }
   }

   void decode(byte[] var1, int var2, int var3, BaseNCodec.Context var4) {
      if (!var4.eof) {
         if (var3 < 0) {
            var4.eof = true;
         }

         for(int var5 = 0; var5 < var3; ++var2) {
            byte[] var7 = this.ensureBufferSize(this.decodeSize, var4);
            byte var6 = var1[var2];
            if (var6 == this.pad) {
               var4.eof = true;
               break;
            }

            if (var6 >= 0) {
               byte[] var8 = DECODE_TABLE;
               if (var6 < var8.length) {
                  var6 = var8[var6];
                  if (var6 >= 0) {
                     var4.modulus = (var4.modulus + 1) % 4;
                     var4.ibitWorkArea = (var4.ibitWorkArea << 6) + var6;
                     if (var4.modulus == 0) {
                        int var10 = var4.pos++;
                        var7[var10] = (byte)(var4.ibitWorkArea >> 16 & 255);
                        var10 = var4.pos++;
                        var7[var10] = (byte)(var4.ibitWorkArea >> 8 & 255);
                        var10 = var4.pos++;
                        var7[var10] = (byte)(var4.ibitWorkArea & 255);
                     }
                  }
               }
            }

            ++var5;
         }

         if (var4.eof && var4.modulus != 0) {
            var1 = this.ensureBufferSize(this.decodeSize, var4);
            var2 = var4.modulus;
            if (var2 != 1) {
               if (var2 != 2) {
                  if (var2 == 3) {
                     validateCharacter(3, var4);
                     var4.ibitWorkArea >>= 2;
                     var2 = var4.pos++;
                     var1[var2] = (byte)(var4.ibitWorkArea >> 8 & 255);
                     var2 = var4.pos++;
                     var1[var2] = (byte)(var4.ibitWorkArea & 255);
                     return;
                  }

                  StringBuilder var9 = new StringBuilder();
                  var9.append("Impossible modulus ");
                  var9.append(var4.modulus);
                  throw new IllegalStateException(var9.toString());
               }

               validateCharacter(15, var4);
               var4.ibitWorkArea >>= 4;
               var2 = var4.pos++;
               var1[var2] = (byte)(var4.ibitWorkArea & 255);
            }
         }

      }
   }

   void encode(byte[] var1, int var2, int var3, BaseNCodec.Context var4) {
      if (!var4.eof) {
         if (var3 < 0) {
            var4.eof = true;
            if (var4.modulus != 0 || this.lineLength != 0) {
               var1 = this.ensureBufferSize(this.encodeSize, var4);
               var2 = var4.pos;
               var3 = var4.modulus;
               if (var3 != 0) {
                  if (var3 != 1) {
                     if (var3 != 2) {
                        StringBuilder var9 = new StringBuilder();
                        var9.append("Impossible modulus ");
                        var9.append(var4.modulus);
                        throw new IllegalStateException(var9.toString());
                     }

                     var3 = var4.pos++;
                     var1[var3] = this.encodeTable[var4.ibitWorkArea >> 10 & 63];
                     var3 = var4.pos++;
                     var1[var3] = this.encodeTable[var4.ibitWorkArea >> 4 & 63];
                     var3 = var4.pos++;
                     var1[var3] = this.encodeTable[var4.ibitWorkArea << 2 & 63];
                     if (this.encodeTable == STANDARD_ENCODE_TABLE) {
                        var3 = var4.pos++;
                        var1[var3] = this.pad;
                     }
                  } else {
                     var3 = var4.pos++;
                     var1[var3] = this.encodeTable[var4.ibitWorkArea >> 2 & 63];
                     var3 = var4.pos++;
                     var1[var3] = this.encodeTable[var4.ibitWorkArea << 4 & 63];
                     if (this.encodeTable == STANDARD_ENCODE_TABLE) {
                        var3 = var4.pos++;
                        var1[var3] = this.pad;
                        var3 = var4.pos++;
                        var1[var3] = this.pad;
                     }
                  }
               }

               var4.currentLinePos += var4.pos - var2;
               if (this.lineLength > 0 && var4.currentLinePos > 0) {
                  System.arraycopy(this.lineSeparator, 0, var1, var4.pos, this.lineSeparator.length);
                  var4.pos += this.lineSeparator.length;
               }

            }
         } else {
            for(int var5 = 0; var5 < var3; ++var2) {
               byte[] var8 = this.ensureBufferSize(this.encodeSize, var4);
               var4.modulus = (var4.modulus + 1) % 3;
               byte var7 = var1[var2];
               int var6 = var7;
               if (var7 < 0) {
                  var6 = var7 + 256;
               }

               var4.ibitWorkArea = (var4.ibitWorkArea << 8) + var6;
               if (var4.modulus == 0) {
                  var6 = var4.pos++;
                  var8[var6] = this.encodeTable[var4.ibitWorkArea >> 18 & 63];
                  var6 = var4.pos++;
                  var8[var6] = this.encodeTable[var4.ibitWorkArea >> 12 & 63];
                  var6 = var4.pos++;
                  var8[var6] = this.encodeTable[var4.ibitWorkArea >> 6 & 63];
                  var6 = var4.pos++;
                  var8[var6] = this.encodeTable[var4.ibitWorkArea & 63];
                  var4.currentLinePos += 4;
                  if (this.lineLength > 0 && this.lineLength <= var4.currentLinePos) {
                     System.arraycopy(this.lineSeparator, 0, var8, var4.pos, this.lineSeparator.length);
                     var4.pos += this.lineSeparator.length;
                     var4.currentLinePos = 0;
                  }
               }

               ++var5;
            }

         }
      }
   }

   protected boolean isInAlphabet(byte var1) {
      if (var1 >= 0) {
         byte[] var2 = this.decodeTable;
         if (var1 < var2.length && var2[var1] != -1) {
            return true;
         }
      }

      return false;
   }

   public boolean isUrlSafe() {
      return this.encodeTable == URL_SAFE_ENCODE_TABLE;
   }
}
