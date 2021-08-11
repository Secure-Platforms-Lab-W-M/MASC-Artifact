package org.apache.commons.codec.digest;

import java.util.zip.Checksum;

public class XXHash32 implements Checksum {
   private static final int BUF_SIZE = 16;
   private static final int PRIME1 = -1640531535;
   private static final int PRIME2 = -2048144777;
   private static final int PRIME3 = -1028477379;
   private static final int PRIME4 = 668265263;
   private static final int PRIME5 = 374761393;
   private static final int ROTATE_BITS = 13;
   private final byte[] buffer;
   private final byte[] oneByte;
   private int pos;
   private final int seed;
   private final int[] state;
   private boolean stateUpdated;
   private int totalLen;

   public XXHash32() {
      this(0);
   }

   public XXHash32(int var1) {
      this.oneByte = new byte[1];
      this.state = new int[4];
      this.buffer = new byte[16];
      this.seed = var1;
      this.initializeState();
   }

   private static int getInt(byte[] var0, int var1) {
      return var0[var1] & 255 | (var0[var1 + 1] & 255) << 8 | (var0[var1 + 2] & 255) << 16 | (var0[var1 + 3] & 255) << 24;
   }

   private void initializeState() {
      int[] var2 = this.state;
      int var1 = this.seed;
      var2[0] = var1 - 1640531535 - 2048144777;
      var2[1] = -2048144777 + var1;
      var2[2] = var1;
      var2[3] = var1 + 1640531535;
   }

   private void process(byte[] var1, int var2) {
      int[] var7 = this.state;
      int var4 = var7[0];
      int var6 = var7[1];
      int var5 = var7[2];
      int var3 = var7[3];
      var4 = Integer.rotateLeft(getInt(var1, var2) * -2048144777 + var4, 13);
      var6 = Integer.rotateLeft(getInt(var1, var2 + 4) * -2048144777 + var6, 13);
      var5 = Integer.rotateLeft(getInt(var1, var2 + 8) * -2048144777 + var5, 13);
      var2 = Integer.rotateLeft(getInt(var1, var2 + 12) * -2048144777 + var3, 13);
      int[] var8 = this.state;
      var8[0] = var4 * -1640531535;
      var8[1] = var6 * -1640531535;
      var8[2] = var5 * -1640531535;
      var8[3] = var2 * -1640531535;
      this.stateUpdated = true;
   }

   public long getValue() {
      int var1;
      if (this.stateUpdated) {
         var1 = Integer.rotateLeft(this.state[0], 1) + Integer.rotateLeft(this.state[1], 7) + Integer.rotateLeft(this.state[2], 12) + Integer.rotateLeft(this.state[3], 18);
      } else {
         var1 = this.state[2] + 374761393;
      }

      int var2 = var1 + this.totalLen;
      var1 = 0;
      int var5 = this.pos;

      while(true) {
         int var3 = var2;
         int var4 = var1;
         if (var1 > var5 - 4) {
            while(var4 < this.pos) {
               var3 = Integer.rotateLeft((this.buffer[var4] & 255) * 374761393 + var3, 11) * -1640531535;
               ++var4;
            }

            var1 = (var3 ^ var3 >>> 15) * -2048144777;
            var1 = (var1 ^ var1 >>> 13) * -1028477379;
            return (long)(var1 ^ var1 >>> 16) & 4294967295L;
         }

         var2 = Integer.rotateLeft(getInt(this.buffer, var1) * -1028477379 + var2, 17) * 668265263;
         var1 += 4;
      }
   }

   public void reset() {
      this.initializeState();
      this.totalLen = 0;
      this.pos = 0;
      this.stateUpdated = false;
   }

   public void update(int var1) {
      byte[] var2 = this.oneByte;
      var2[0] = (byte)(var1 & 255);
      this.update(var2, 0, 1);
   }

   public void update(byte[] var1, int var2, int var3) {
      if (var3 > 0) {
         this.totalLen += var3;
         int var4 = var2 + var3;
         int var5 = this.pos;
         if (var5 + var3 - 16 < 0) {
            System.arraycopy(var1, var2, this.buffer, var5, var3);
            this.pos += var3;
         } else {
            var3 = var2;
            if (var5 > 0) {
               var3 = 16 - var5;
               System.arraycopy(var1, var2, this.buffer, var5, var3);
               this.process(this.buffer, 0);
               var3 += var2;
            }

            while(var3 <= var4 - 16) {
               this.process(var1, var3);
               var3 += 16;
            }

            if (var3 < var4) {
               var2 = var4 - var3;
               this.pos = var2;
               System.arraycopy(var1, var3, this.buffer, 0, var2);
            } else {
               this.pos = 0;
            }
         }
      }
   }
}
