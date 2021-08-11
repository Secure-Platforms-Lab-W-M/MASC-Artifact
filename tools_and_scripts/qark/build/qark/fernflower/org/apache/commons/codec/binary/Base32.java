package org.apache.commons.codec.binary;

public class Base32 extends BaseNCodec {
   private static final int BITS_PER_ENCODED_BYTE = 5;
   private static final int BYTES_PER_ENCODED_BLOCK = 8;
   private static final int BYTES_PER_UNENCODED_BLOCK = 5;
   private static final byte[] CHUNK_SEPARATOR = new byte[]{13, 10};
   private static final byte[] DECODE_TABLE = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25};
   private static final byte[] ENCODE_TABLE = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 50, 51, 52, 53, 54, 55};
   private static final byte[] HEX_DECODE_TABLE = new byte[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31};
   private static final byte[] HEX_ENCODE_TABLE = new byte[]{48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86};
   private static final long MASK_1BITS = 1L;
   private static final long MASK_2BITS = 3L;
   private static final long MASK_3BITS = 7L;
   private static final long MASK_4BITS = 15L;
   private static final int MASK_5BITS = 31;
   private static final long MASK_6BITS = 63L;
   private static final long MASK_7BITS = 127L;
   private final int decodeSize;
   private final byte[] decodeTable;
   private final int encodeSize;
   private final byte[] encodeTable;
   private final byte[] lineSeparator;

   public Base32() {
      this(false);
   }

   public Base32(byte var1) {
      this(false, var1);
   }

   public Base32(int var1) {
      this(var1, CHUNK_SEPARATOR);
   }

   public Base32(int var1, byte[] var2) {
      this(var1, var2, false, (byte)61);
   }

   public Base32(int var1, byte[] var2, boolean var3) {
      this(var1, var2, var3, (byte)61);
   }

   public Base32(int var1, byte[] var2, boolean var3, byte var4) {
      int var5;
      if (var2 == null) {
         var5 = 0;
      } else {
         var5 = var2.length;
      }

      super(5, 8, var1, var5, var4);
      if (var3) {
         this.encodeTable = HEX_ENCODE_TABLE;
         this.decodeTable = HEX_DECODE_TABLE;
      } else {
         this.encodeTable = ENCODE_TABLE;
         this.decodeTable = DECODE_TABLE;
      }

      if (var1 > 0) {
         if (var2 == null) {
            StringBuilder var8 = new StringBuilder();
            var8.append("lineLength ");
            var8.append(var1);
            var8.append(" > 0, but lineSeparator is null");
            throw new IllegalArgumentException(var8.toString());
         }

         if (this.containsAlphabetOrPad(var2)) {
            String var7 = StringUtils.newStringUtf8(var2);
            StringBuilder var9 = new StringBuilder();
            var9.append("lineSeparator must not contain Base32 characters: [");
            var9.append(var7);
            var9.append("]");
            throw new IllegalArgumentException(var9.toString());
         }

         this.encodeSize = var2.length + 8;
         byte[] var6 = new byte[var2.length];
         this.lineSeparator = var6;
         System.arraycopy(var2, 0, var6, 0, var2.length);
      } else {
         this.encodeSize = 8;
         this.lineSeparator = null;
      }

      this.decodeSize = this.encodeSize - 1;
      if (this.isInAlphabet(var4) || isWhiteSpace(var4)) {
         throw new IllegalArgumentException("pad must not be in alphabet or whitespace");
      }
   }

   public Base32(boolean var1) {
      this(0, (byte[])null, var1, (byte)61);
   }

   public Base32(boolean var1, byte var2) {
      this(0, (byte[])null, var1, var2);
   }

   private static void validateCharacter(long var0, BaseNCodec.Context var2) {
      if ((var2.lbitWorkArea & var0) != 0L) {
         throw new IllegalArgumentException("Last encoded character (before the paddings if any) is a valid base 32 alphabet but not a possible value. Expected the discarded bits to be zero.");
      }
   }

   void decode(byte[] var1, int var2, int var3, BaseNCodec.Context var4) {
      if (!var4.eof) {
         if (var3 < 0) {
            var4.eof = true;
         }

         for(int var5 = 0; var5 < var3; ++var2) {
            byte var6 = var1[var2];
            if (var6 == this.pad) {
               var4.eof = true;
               break;
            }

            byte[] var7 = this.ensureBufferSize(this.decodeSize, var4);
            if (var6 >= 0) {
               byte[] var8 = this.decodeTable;
               if (var6 < var8.length) {
                  var6 = var8[var6];
                  if (var6 >= 0) {
                     var4.modulus = (var4.modulus + 1) % 8;
                     var4.lbitWorkArea = (var4.lbitWorkArea << 5) + (long)var6;
                     if (var4.modulus == 0) {
                        int var10 = var4.pos++;
                        var7[var10] = (byte)((int)(var4.lbitWorkArea >> 32 & 255L));
                        var10 = var4.pos++;
                        var7[var10] = (byte)((int)(var4.lbitWorkArea >> 24 & 255L));
                        var10 = var4.pos++;
                        var7[var10] = (byte)((int)(var4.lbitWorkArea >> 16 & 255L));
                        var10 = var4.pos++;
                        var7[var10] = (byte)((int)(var4.lbitWorkArea >> 8 & 255L));
                        var10 = var4.pos++;
                        var7[var10] = (byte)((int)(var4.lbitWorkArea & 255L));
                     }
                  }
               }
            }

            ++var5;
         }

         if (var4.eof && var4.modulus >= 2) {
            var1 = this.ensureBufferSize(this.decodeSize, var4);
            switch(var4.modulus) {
            case 2:
               validateCharacter(3L, var4);
               var2 = var4.pos++;
               var1[var2] = (byte)((int)(var4.lbitWorkArea >> 2 & 255L));
               break;
            case 3:
               validateCharacter(127L, var4);
               var2 = var4.pos++;
               var1[var2] = (byte)((int)(var4.lbitWorkArea >> 7 & 255L));
               return;
            case 4:
               validateCharacter(15L, var4);
               var4.lbitWorkArea >>= 4;
               var2 = var4.pos++;
               var1[var2] = (byte)((int)(var4.lbitWorkArea >> 8 & 255L));
               var2 = var4.pos++;
               var1[var2] = (byte)((int)(var4.lbitWorkArea & 255L));
               return;
            case 5:
               validateCharacter(1L, var4);
               var4.lbitWorkArea >>= 1;
               var2 = var4.pos++;
               var1[var2] = (byte)((int)(var4.lbitWorkArea >> 16 & 255L));
               var2 = var4.pos++;
               var1[var2] = (byte)((int)(var4.lbitWorkArea >> 8 & 255L));
               var2 = var4.pos++;
               var1[var2] = (byte)((int)(var4.lbitWorkArea & 255L));
               return;
            case 6:
               validateCharacter(63L, var4);
               var4.lbitWorkArea >>= 6;
               var2 = var4.pos++;
               var1[var2] = (byte)((int)(var4.lbitWorkArea >> 16 & 255L));
               var2 = var4.pos++;
               var1[var2] = (byte)((int)(var4.lbitWorkArea >> 8 & 255L));
               var2 = var4.pos++;
               var1[var2] = (byte)((int)(var4.lbitWorkArea & 255L));
               return;
            case 7:
               validateCharacter(7L, var4);
               var4.lbitWorkArea >>= 3;
               var2 = var4.pos++;
               var1[var2] = (byte)((int)(var4.lbitWorkArea >> 24 & 255L));
               var2 = var4.pos++;
               var1[var2] = (byte)((int)(var4.lbitWorkArea >> 16 & 255L));
               var2 = var4.pos++;
               var1[var2] = (byte)((int)(var4.lbitWorkArea >> 8 & 255L));
               var2 = var4.pos++;
               var1[var2] = (byte)((int)(var4.lbitWorkArea & 255L));
               return;
            default:
               StringBuilder var9 = new StringBuilder();
               var9.append("Impossible modulus ");
               var9.append(var4.modulus);
               throw new IllegalStateException(var9.toString());
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
                        if (var3 != 3) {
                           if (var3 != 4) {
                              StringBuilder var9 = new StringBuilder();
                              var9.append("Impossible modulus ");
                              var9.append(var4.modulus);
                              throw new IllegalStateException(var9.toString());
                           }

                           var3 = var4.pos++;
                           var1[var3] = this.encodeTable[(int)(var4.lbitWorkArea >> 27) & 31];
                           var3 = var4.pos++;
                           var1[var3] = this.encodeTable[(int)(var4.lbitWorkArea >> 22) & 31];
                           var3 = var4.pos++;
                           var1[var3] = this.encodeTable[(int)(var4.lbitWorkArea >> 17) & 31];
                           var3 = var4.pos++;
                           var1[var3] = this.encodeTable[(int)(var4.lbitWorkArea >> 12) & 31];
                           var3 = var4.pos++;
                           var1[var3] = this.encodeTable[(int)(var4.lbitWorkArea >> 7) & 31];
                           var3 = var4.pos++;
                           var1[var3] = this.encodeTable[(int)(var4.lbitWorkArea >> 2) & 31];
                           var3 = var4.pos++;
                           var1[var3] = this.encodeTable[(int)(var4.lbitWorkArea << 3) & 31];
                           var3 = var4.pos++;
                           var1[var3] = this.pad;
                        } else {
                           var3 = var4.pos++;
                           var1[var3] = this.encodeTable[(int)(var4.lbitWorkArea >> 19) & 31];
                           var3 = var4.pos++;
                           var1[var3] = this.encodeTable[(int)(var4.lbitWorkArea >> 14) & 31];
                           var3 = var4.pos++;
                           var1[var3] = this.encodeTable[(int)(var4.lbitWorkArea >> 9) & 31];
                           var3 = var4.pos++;
                           var1[var3] = this.encodeTable[(int)(var4.lbitWorkArea >> 4) & 31];
                           var3 = var4.pos++;
                           var1[var3] = this.encodeTable[(int)(var4.lbitWorkArea << 1) & 31];
                           var3 = var4.pos++;
                           var1[var3] = this.pad;
                           var3 = var4.pos++;
                           var1[var3] = this.pad;
                           var3 = var4.pos++;
                           var1[var3] = this.pad;
                        }
                     } else {
                        var3 = var4.pos++;
                        var1[var3] = this.encodeTable[(int)(var4.lbitWorkArea >> 11) & 31];
                        var3 = var4.pos++;
                        var1[var3] = this.encodeTable[(int)(var4.lbitWorkArea >> 6) & 31];
                        var3 = var4.pos++;
                        var1[var3] = this.encodeTable[(int)(var4.lbitWorkArea >> 1) & 31];
                        var3 = var4.pos++;
                        var1[var3] = this.encodeTable[(int)(var4.lbitWorkArea << 4) & 31];
                        var3 = var4.pos++;
                        var1[var3] = this.pad;
                        var3 = var4.pos++;
                        var1[var3] = this.pad;
                        var3 = var4.pos++;
                        var1[var3] = this.pad;
                        var3 = var4.pos++;
                        var1[var3] = this.pad;
                     }
                  } else {
                     var3 = var4.pos++;
                     var1[var3] = this.encodeTable[(int)(var4.lbitWorkArea >> 3) & 31];
                     var3 = var4.pos++;
                     var1[var3] = this.encodeTable[(int)(var4.lbitWorkArea << 2) & 31];
                     var3 = var4.pos++;
                     var1[var3] = this.pad;
                     var3 = var4.pos++;
                     var1[var3] = this.pad;
                     var3 = var4.pos++;
                     var1[var3] = this.pad;
                     var3 = var4.pos++;
                     var1[var3] = this.pad;
                     var3 = var4.pos++;
                     var1[var3] = this.pad;
                     var3 = var4.pos++;
                     var1[var3] = this.pad;
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
               var4.modulus = (var4.modulus + 1) % 5;
               byte var7 = var1[var2];
               int var6 = var7;
               if (var7 < 0) {
                  var6 = var7 + 256;
               }

               var4.lbitWorkArea = (var4.lbitWorkArea << 8) + (long)var6;
               if (var4.modulus == 0) {
                  var6 = var4.pos++;
                  var8[var6] = this.encodeTable[(int)(var4.lbitWorkArea >> 35) & 31];
                  var6 = var4.pos++;
                  var8[var6] = this.encodeTable[(int)(var4.lbitWorkArea >> 30) & 31];
                  var6 = var4.pos++;
                  var8[var6] = this.encodeTable[(int)(var4.lbitWorkArea >> 25) & 31];
                  var6 = var4.pos++;
                  var8[var6] = this.encodeTable[(int)(var4.lbitWorkArea >> 20) & 31];
                  var6 = var4.pos++;
                  var8[var6] = this.encodeTable[(int)(var4.lbitWorkArea >> 15) & 31];
                  var6 = var4.pos++;
                  var8[var6] = this.encodeTable[(int)(var4.lbitWorkArea >> 10) & 31];
                  var6 = var4.pos++;
                  var8[var6] = this.encodeTable[(int)(var4.lbitWorkArea >> 5) & 31];
                  var6 = var4.pos++;
                  var8[var6] = this.encodeTable[(int)var4.lbitWorkArea & 31];
                  var4.currentLinePos += 8;
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

   public boolean isInAlphabet(byte var1) {
      if (var1 >= 0) {
         byte[] var2 = this.decodeTable;
         if (var1 < var2.length && var2[var1] != -1) {
            return true;
         }
      }

      return false;
   }
}
