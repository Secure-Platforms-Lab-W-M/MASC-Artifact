package org.apache.commons.codec.digest;

import org.apache.commons.codec.binary.StringUtils;

public final class MurmurHash3 {
   // $FF: renamed from: C1 long
   private static final long field_214 = -8663945395140668459L;
   private static final int C1_32 = -862048943;
   // $FF: renamed from: C2 long
   private static final long field_215 = 5545529020109919103L;
   private static final int C2_32 = 461845907;
   public static final int DEFAULT_SEED = 104729;
   static final int INTEGER_BYTES = 4;
   static final int LONG_BYTES = 8;
   // $FF: renamed from: M int
   private static final int field_216 = 5;
   private static final int M_32 = 5;
   // $FF: renamed from: N1 int
   private static final int field_217 = 1390208809;
   // $FF: renamed from: N2 int
   private static final int field_218 = 944331445;
   @Deprecated
   public static final long NULL_HASHCODE = 2862933555777941757L;
   private static final int N_32 = -430675100;
   // $FF: renamed from: R1 int
   private static final int field_219 = 31;
   private static final int R1_32 = 15;
   // $FF: renamed from: R2 int
   private static final int field_220 = 27;
   private static final int R2_32 = 13;
   // $FF: renamed from: R3 int
   private static final int field_221 = 33;
   static final int SHORT_BYTES = 2;

   private MurmurHash3() {
   }

   private static int fmix32(int var0) {
      var0 = (var0 ^ var0 >>> 16) * -2048144789;
      var0 = (var0 ^ var0 >>> 13) * -1028477387;
      return var0 ^ var0 >>> 16;
   }

   private static long fmix64(long var0) {
      var0 = (var0 ^ var0 >>> 33) * -49064778989728563L;
      var0 = (var0 ^ var0 >>> 33) * -4265267296055464877L;
      return var0 ^ var0 >>> 33;
   }

   private static int getLittleEndianInt(byte[] var0, int var1) {
      return var0[var1] & 255 | (var0[var1 + 1] & 255) << 8 | (var0[var1 + 2] & 255) << 16 | (var0[var1 + 3] & 255) << 24;
   }

   private static long getLittleEndianLong(byte[] var0, int var1) {
      return (long)var0[var1] & 255L | ((long)var0[var1 + 1] & 255L) << 8 | ((long)var0[var1 + 2] & 255L) << 16 | ((long)var0[var1 + 3] & 255L) << 24 | ((long)var0[var1 + 4] & 255L) << 32 | ((long)var0[var1 + 5] & 255L) << 40 | ((long)var0[var1 + 6] & 255L) << 48 | (255L & (long)var0[var1 + 7]) << 56;
   }

   @Deprecated
   public static long[] hash128(String var0) {
      byte[] var1 = StringUtils.getBytesUtf8(var0);
      return hash128(var1, 0, var1.length, 104729);
   }

   public static long[] hash128(byte[] var0) {
      return hash128(var0, 0, var0.length, 104729);
   }

   @Deprecated
   public static long[] hash128(byte[] var0, int var1, int var2, int var3) {
      return hash128x64(var0, var1, var2, var3);
   }

   public static long[] hash128x64(byte[] var0) {
      return hash128x64(var0, 0, var0.length, 0);
   }

   public static long[] hash128x64(byte[] var0, int var1, int var2, int var3) {
      return hash128x64(var0, var1, var2, (long)var3 & 4294967295L);
   }

   private static long[] hash128x64(byte[] var0, int var1, int var2, long var3) {
      long var12 = var3;
      int var6 = var2 >> 4;

      int var5;
      long var8;
      long var10;
      for(var5 = 0; var5 < var6; ++var5) {
         int var7 = var1 + (var5 << 4);
         var10 = getLittleEndianLong(var0, var7);
         var8 = getLittleEndianLong(var0, var7 + 8);
         var12 = (Long.rotateLeft(var12 ^ Long.rotateLeft(var10 * -8663945395140668459L, 31) * 5545529020109919103L, 27) + var3) * 5L + 1390208809L;
         var3 = 5L * (Long.rotateLeft(var3 ^ Long.rotateLeft(var8 * 5545529020109919103L, 33) * -8663945395140668459L, 31) + var12) + 944331445L;
      }

      var8 = 0L;
      var10 = 0L;
      var5 = var1 + (var6 << 4);
      switch(var1 + var2 - var5) {
      case 15:
         var10 = 0L ^ ((long)var0[var5 + 14] & 255L) << 48;
      case 14:
         var10 ^= ((long)var0[var5 + 13] & 255L) << 40;
      case 13:
         var10 ^= ((long)var0[var5 + 12] & 255L) << 32;
      case 12:
         var10 ^= ((long)var0[var5 + 11] & 255L) << 24;
      case 11:
         var10 ^= ((long)var0[var5 + 10] & 255L) << 16;
      case 10:
         var10 ^= ((long)var0[var5 + 9] & 255L) << 8;
      case 9:
         var3 ^= Long.rotateLeft(((long)(var0[var5 + 8] & 255) ^ var10) * 5545529020109919103L, 33) * -8663945395140668459L;
      case 8:
         var8 = 0L ^ ((long)var0[var5 + 7] & 255L) << 56;
      case 7:
         var8 ^= ((long)var0[var5 + 6] & 255L) << 48;
      case 6:
         var8 ^= ((long)var0[var5 + 5] & 255L) << 40;
      case 5:
         var8 ^= ((long)var0[var5 + 4] & 255L) << 32;
      case 4:
         var8 ^= ((long)var0[var5 + 3] & 255L) << 24;
      case 3:
         var8 ^= ((long)var0[var5 + 2] & 255L) << 16;
      case 2:
         var8 ^= ((long)var0[var5 + 1] & 255L) << 8;
      case 1:
         var12 ^= Long.rotateLeft((var8 ^ (long)(var0[var5] & 255)) * -8663945395140668459L, 31) * 5545529020109919103L;
      default:
         var8 = (long)var2;
         var3 ^= (long)var2;
         var10 = (var12 ^ var8) + var3;
         var8 = fmix64(var10);
         var3 = fmix64(var3 + var10);
         var8 += var3;
         return new long[]{var8, var3 + var8};
      }
   }

   public static int hash32(long var0) {
      return hash32(var0, 104729);
   }

   public static int hash32(long var0, int var2) {
      var0 = Long.reverseBytes(var0);
      var2 = mix32((int)var0, var2);
      return fmix32(mix32((int)(var0 >>> 32), var2) ^ 8);
   }

   public static int hash32(long var0, long var2) {
      return hash32(var0, var2, 104729);
   }

   public static int hash32(long var0, long var2, int var4) {
      var0 = Long.reverseBytes(var0);
      var2 = Long.reverseBytes(var2);
      var4 = mix32((int)var0, var4);
      var4 = mix32((int)(var0 >>> 32), var4);
      var4 = mix32((int)var2, var4);
      return fmix32(mix32((int)(var2 >>> 32), var4) ^ 16);
   }

   @Deprecated
   public static int hash32(String var0) {
      byte[] var1 = StringUtils.getBytesUtf8(var0);
      return hash32(var1, 0, var1.length, 104729);
   }

   @Deprecated
   public static int hash32(byte[] var0) {
      return hash32(var0, 0, var0.length, 104729);
   }

   @Deprecated
   public static int hash32(byte[] var0, int var1) {
      return hash32(var0, var1, 104729);
   }

   @Deprecated
   public static int hash32(byte[] var0, int var1, int var2) {
      return hash32(var0, 0, var1, var2);
   }

   @Deprecated
   public static int hash32(byte[] var0, int var1, int var2, int var3) {
      int var5 = var2 >> 2;

      for(int var4 = 0; var4 < var5; ++var4) {
         var3 = mix32(getLittleEndianInt(var0, (var4 << 2) + var1), var3);
      }

      int var6 = (var5 << 2) + var1;
      byte var9 = 0;
      byte var8 = 0;
      int var7 = var1 + var2 - var6;
      var1 = var9;
      if (var7 != 1) {
         var1 = var8;
         if (var7 != 2) {
            if (var7 != 3) {
               return fmix32(var3 ^ var2);
            }

            var1 = 0 ^ var0[var6 + 2] << 16;
         }

         var1 ^= var0[var6 + 1] << 8;
      }

      var3 ^= Integer.rotateLeft((var1 ^ var0[var6]) * -862048943, 15) * 461845907;
      return fmix32(var3 ^ var2);
   }

   public static int hash32x86(byte[] var0) {
      return hash32x86(var0, 0, var0.length, 0);
   }

   public static int hash32x86(byte[] var0, int var1, int var2, int var3) {
      int var5 = var2 >> 2;

      for(int var4 = 0; var4 < var5; ++var4) {
         var3 = mix32(getLittleEndianInt(var0, (var4 << 2) + var1), var3);
      }

      int var6 = (var5 << 2) + var1;
      byte var9 = 0;
      byte var8 = 0;
      int var7 = var1 + var2 - var6;
      var1 = var9;
      if (var7 != 1) {
         var1 = var8;
         if (var7 != 2) {
            if (var7 != 3) {
               return fmix32(var3 ^ var2);
            }

            var1 = 0 ^ (var0[var6 + 2] & 255) << 16;
         }

         var1 ^= (var0[var6 + 1] & 255) << 8;
      }

      var3 ^= Integer.rotateLeft((var1 ^ var0[var6] & 255) * -862048943, 15) * 461845907;
      return fmix32(var3 ^ var2);
   }

   @Deprecated
   public static long hash64(int var0) {
      return fmix64(104729L ^ Long.rotateLeft(((long)Integer.reverseBytes(var0) & 4294967295L) * -8663945395140668459L, 31) * 5545529020109919103L ^ 4L);
   }

   @Deprecated
   public static long hash64(long var0) {
      return fmix64(8L ^ Long.rotateLeft(104729L ^ Long.rotateLeft(Long.reverseBytes(var0) * -8663945395140668459L, 31) * 5545529020109919103L, 27) * 5L + 1390208809L);
   }

   @Deprecated
   public static long hash64(short var0) {
      return fmix64(104729L ^ Long.rotateLeft((0L ^ ((long)var0 & 255L) << 8 ^ (long)(('\uff00' & var0) >> 8) & 255L) * -8663945395140668459L, 31) * 5545529020109919103L ^ 2L);
   }

   @Deprecated
   public static long hash64(byte[] var0) {
      return hash64(var0, 0, var0.length, 104729);
   }

   @Deprecated
   public static long hash64(byte[] var0, int var1, int var2) {
      return hash64(var0, var1, var2, 104729);
   }

   @Deprecated
   public static long hash64(byte[] var0, int var1, int var2, int var3) {
      long var7 = (long)var3;
      int var4 = var2 >> 3;

      for(var3 = 0; var3 < var4; ++var3) {
         var7 = Long.rotateLeft(var7 ^ Long.rotateLeft(getLittleEndianLong(var0, var1 + (var3 << 3)) * -8663945395140668459L, 31) * 5545529020109919103L, 27) * 5L + 1390208809L;
      }

      long var5 = 0L;
      var3 = var1 + (var4 << 3);
      long var9 = var5;
      long var11 = var5;
      long var13 = var5;
      long var15 = var5;
      long var17 = var5;
      switch(var1 + var2 - var3) {
      case 7:
         var9 = 0L ^ ((long)var0[var3 + 6] & 255L) << 48;
      case 6:
         var11 = var9 ^ ((long)var0[var3 + 5] & 255L) << 40;
      case 5:
         var13 = var11 ^ ((long)var0[var3 + 4] & 255L) << 32;
      case 4:
         var15 = var13 ^ ((long)var0[var3 + 3] & 255L) << 24;
      case 3:
         var17 = var15 ^ ((long)var0[var3 + 2] & 255L) << 16;
      case 2:
         var5 = var17 ^ ((long)var0[var3 + 1] & 255L) << 8;
      case 1:
         var7 ^= Long.rotateLeft(((long)var0[var3] & 255L ^ var5) * -8663945395140668459L, 31) * 5545529020109919103L;
      default:
         return fmix64(var7 ^ (long)var2);
      }
   }

   private static int mix32(int var0, int var1) {
      return Integer.rotateLeft(var1 ^ Integer.rotateLeft(var0 * -862048943, 15) * 461845907, 13) * 5 - 430675100;
   }

   @Deprecated
   public static class IncrementalHash32 extends MurmurHash3.IncrementalHash32x86 {
      @Deprecated
      int finalise(int var1, int var2, byte[] var3, int var4) {
         int var5 = 0;
         byte var6 = 0;
         if (var2 != 1) {
            var5 = var6;
            if (var2 != 2) {
               if (var2 != 3) {
                  return MurmurHash3.fmix32(var1 ^ var4);
               }

               var5 = 0 ^ var3[2] << 16;
            }

            var5 ^= var3[1] << 8;
         }

         var1 ^= Integer.rotateLeft((var5 ^ var3[0]) * -862048943, 15) * 461845907;
         return MurmurHash3.fmix32(var1 ^ var4);
      }
   }

   public static class IncrementalHash32x86 {
      private static final int BLOCK_SIZE = 4;
      private int hash;
      private int totalLen;
      private final byte[] unprocessed = new byte[3];
      private int unprocessedLength;

      private static int orBytes(byte var0, byte var1, byte var2, byte var3) {
         return var0 & 255 | (var1 & 255) << 8 | (var2 & 255) << 16 | (var3 & 255) << 24;
      }

      public final void add(byte[] var1, int var2, int var3) {
         if (var3 > 0) {
            this.totalLen += var3;
            int var4 = this.unprocessedLength;
            if (var4 + var3 - 4 < 0) {
               System.arraycopy(var1, var2, this.unprocessed, var4, var3);
               this.unprocessedLength += var3;
            } else {
               if (var4 > 0) {
                  if (var4 != 1) {
                     byte[] var6;
                     if (var4 != 2) {
                        if (var4 != 3) {
                           StringBuilder var7 = new StringBuilder();
                           var7.append("Unprocessed length should be 1, 2, or 3: ");
                           var7.append(this.unprocessedLength);
                           throw new IllegalStateException(var7.toString());
                        }

                        var6 = this.unprocessed;
                        var4 = orBytes(var6[0], var6[1], var6[2], var1[var2]);
                     } else {
                        var6 = this.unprocessed;
                        var4 = orBytes(var6[0], var6[1], var1[var2], var1[var2 + 1]);
                     }
                  } else {
                     var4 = orBytes(this.unprocessed[0], var1[var2], var1[var2 + 1], var1[var2 + 2]);
                  }

                  this.hash = MurmurHash3.mix32(var4, this.hash);
                  var4 = 4 - this.unprocessedLength;
                  var2 += var4;
                  var3 -= var4;
               }

               int var5 = var3 >> 2;

               for(var4 = 0; var4 < var5; ++var4) {
                  this.hash = MurmurHash3.mix32(MurmurHash3.getLittleEndianInt(var1, (var4 << 2) + var2), this.hash);
               }

               var4 = var5 << 2;
               var3 -= var4;
               this.unprocessedLength = var3;
               if (var3 != 0) {
                  System.arraycopy(var1, var2 + var4, this.unprocessed, 0, var3);
               }

            }
         }
      }

      public final int end() {
         return this.finalise(this.hash, this.unprocessedLength, this.unprocessed, this.totalLen);
      }

      int finalise(int var1, int var2, byte[] var3, int var4) {
         int var5 = 0;
         byte var6 = 0;
         if (var2 != 1) {
            var5 = var6;
            if (var2 != 2) {
               if (var2 != 3) {
                  return MurmurHash3.fmix32(var1 ^ var4);
               }

               var5 = 0 ^ (var3[2] & 255) << 16;
            }

            var5 ^= (var3[1] & 255) << 8;
         }

         var1 ^= Integer.rotateLeft((var5 ^ var3[0] & 255) * -862048943, 15) * 461845907;
         return MurmurHash3.fmix32(var1 ^ var4);
      }

      public final void start(int var1) {
         this.totalLen = 0;
         this.unprocessedLength = 0;
         this.hash = var1;
      }
   }
}
