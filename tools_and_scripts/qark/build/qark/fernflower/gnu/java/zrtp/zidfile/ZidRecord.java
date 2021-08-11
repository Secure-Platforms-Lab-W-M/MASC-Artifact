package gnu.java.zrtp.zidfile;

import gnu.java.zrtp.utils.ZrtpUtils;
import java.io.PrintStream;

public class ZidRecord {
   private static final int FILLER_OFFSET = 2;
   private static final int FLAGS_OFFSET = 1;
   public static final int IDENTIFIER_LENGTH = 12;
   private static final int IDENTIFIER_OFFSET = 4;
   private static final int MITMKEY_OFFSET = 96;
   private static final int MITMKeyAvailable = 16;
   private static final int OwnZIDRecord = 32;
   private static final int RS1DATA_OFFSET = 24;
   private static final int RS1INTERVAL_OFFSET = 16;
   private static final int RS1Valid = 4;
   private static final int RS2DATA_OFFSET = 64;
   private static final int RS2INTERVAL_OFFSET = 56;
   private static final int RS2Valid = 8;
   public static final int RS_LENGTH = 32;
   private static final int SASVerified = 2;
   private static final int TIME_LENGTH = 8;
   private static final int VERSION = 2;
   private static final int VERSION_OFFSET = 0;
   private static final int Valid = 1;
   private static final int ZID_RECORD_LENGTH = 128;
   private byte[] buffer;
   private long position;

   public ZidRecord() {
      byte[] var1 = new byte[128];
      this.buffer = var1;
      var1[0] = 2;
   }

   public static void main(String[] var0) {
      byte[] var1 = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32};
      ZidRecord var4 = new ZidRecord();
      var4.setIdentifier(new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12});
      var4.setNewRs1(var1, 10);
      var4.setMiTMData(var1);
      PrintStream var2 = System.err;
      StringBuilder var3 = new StringBuilder();
      var3.append("is rs 1 valid: ");
      var3.append(var4.isRs1NotExpired());
      var2.println(var3.toString());
      ++var1[0];
      var4.setNewRs1(var1, 16);
      PrintStream var5 = System.err;
      StringBuilder var6 = new StringBuilder();
      var6.append("is rs 2 valid: ");
      var6.append(var4.isRs2NotExpired());
      var5.println(var6.toString());
      ZrtpUtils.hexdump("ZID record", var4.getBuffer(), var4.getBuffer().length);
   }

   protected byte[] getBuffer() {
      return this.buffer;
   }

   public byte[] getIdentifier() {
      return ZrtpUtils.readRegion(this.buffer, 4, 12);
   }

   public void getIdentifierInto(byte[] var1) {
      System.arraycopy(this.buffer, 4, var1, 0, 12);
   }

   public byte[] getMiTMData() {
      return ZrtpUtils.readRegion(this.buffer, 96, 32);
   }

   public long getPosition() {
      return this.position;
   }

   public byte[] getRs1() {
      return ZrtpUtils.readRegion(this.buffer, 24, 32);
   }

   public byte[] getRs2() {
      return ZrtpUtils.readRegion(this.buffer, 64, 32);
   }

   public boolean isMITMKeyAvailable() {
      return (this.buffer[1] & 16) == 16;
   }

   public boolean isOwnZIDRecord() {
      return this.buffer[1] == 32;
   }

   public boolean isRs1NotExpired() {
      long var8 = System.currentTimeMillis() / 1000L;
      byte[] var12 = this.buffer;
      byte var1 = var12[16];
      byte var2 = var12[17];
      byte var3 = var12[18];
      byte var4 = var12[19];
      byte var5 = var12[20];
      byte var6 = var12[21];
      byte var7 = var12[22];
      long var10 = (long)((var12[23] & 255) << 56 | (var3 & 255) << 16 | var1 & 255 | (var2 & 255) << 8 | (var4 & 255) << 24 | (var5 & 255) << 32 | (var6 & 255) << 40 | (var7 & 255) << 48);
      if (var10 == -1L) {
         return true;
      } else if (var10 == 0L) {
         return false;
      } else {
         return var8 <= var10;
      }
   }

   public boolean isRs1Valid() {
      return (this.buffer[1] & 4) == 4;
   }

   public boolean isRs2NotExpired() {
      long var8 = System.currentTimeMillis() / 1000L;
      byte[] var12 = this.buffer;
      byte var1 = var12[56];
      byte var2 = var12[57];
      byte var3 = var12[58];
      byte var4 = var12[59];
      byte var5 = var12[60];
      byte var6 = var12[61];
      byte var7 = var12[62];
      long var10 = (long)((var12[63] & 255) << 56 | var1 & 255 | (var2 & 255) << 8 | (var3 & 255) << 16 | (var4 & 255) << 24 | (var5 & 255) << 32 | (var6 & 255) << 40 | (var7 & 255) << 48);
      if (var10 == -1L) {
         return true;
      } else if (var10 == 0L) {
         return false;
      } else {
         return var8 <= var10;
      }
   }

   public boolean isRs2Valid() {
      return (this.buffer[1] & 8) == 8;
   }

   public boolean isSameIdentifier(byte[] var1) {
      for(int var2 = 0; var2 < 12; ++var2) {
         if (this.buffer[var2 + 4] != var1[var2]) {
            return false;
         }
      }

      return true;
   }

   public boolean isSameRs1(byte[] var1) {
      for(int var2 = 0; var2 < 32; ++var2) {
         if (this.buffer[var2 + 24] != var1[var2]) {
            return false;
         }
      }

      return true;
   }

   public boolean isSameRs2(byte[] var1) {
      for(int var2 = 0; var2 < 32; ++var2) {
         if (this.buffer[var2 + 64] != var1[var2]) {
            return false;
         }
      }

      return true;
   }

   public boolean isSasVerified() {
      return (this.buffer[1] & 2) == 2;
   }

   protected boolean isValid() {
      return (this.buffer[1] & 1) == 1;
   }

   public void resetMITMKeyAvailable() {
      byte[] var1 = this.buffer;
      var1[1] &= -17;
   }

   public void resetOwnZIDRecord() {
      this.buffer[1] = 0;
   }

   public void resetRs1Valid() {
      byte[] var1 = this.buffer;
      var1[1] &= -5;
   }

   public void resetRs2Valid() {
      byte[] var1 = this.buffer;
      var1[1] &= -9;
   }

   public void resetSasVerified() {
      byte[] var1 = this.buffer;
      var1[1] &= -3;
   }

   public void setIdentifier(byte[] var1) {
      System.arraycopy(var1, 0, this.buffer, 4, 12);
   }

   public void setMITMKeyAvailable() {
      byte[] var1 = this.buffer;
      var1[1] = (byte)(var1[1] | 16);
   }

   public void setMiTMData(byte[] var1) {
      System.arraycopy(var1, 0, this.buffer, 96, 32);
      this.setMITMKeyAvailable();
   }

   public void setNewRs1(byte[] var1, int var2) {
      long var3;
      if (var2 == -1) {
         var3 = -1L;
      } else if (var2 <= 0) {
         var3 = 0L;
      } else {
         var3 = System.currentTimeMillis() / 1000L + (long)var2;
      }

      if (var3 != 0L) {
         byte[] var5 = this.buffer;
         System.arraycopy(var5, 24, var5, 64, 32);
         var5 = this.buffer;
         System.arraycopy(var5, 16, var5, 56, 8);
         System.arraycopy(var1, 0, this.buffer, 24, 32);
         this.setRs1Valid();
         this.resetRs2Valid();
      }

      var1 = this.buffer;
      var1[16] = (byte)((int)var3);
      var1[17] = (byte)((int)(var3 >> 8));
      var1[18] = (byte)((int)(var3 >> 16));
      var1[19] = (byte)((int)(var3 >> 24));
      var1[20] = (byte)((int)(var3 >> 32));
      var1[21] = (byte)((int)(var3 >> 40));
      var1[22] = (byte)((int)(var3 >> 48));
      var1[23] = (byte)((int)(var3 >> 56));
   }

   public void setOwnZIDRecord() {
      this.buffer[1] = 32;
   }

   public void setPosition(long var1) {
      this.position = var1;
   }

   public void setRs1Valid() {
      byte[] var1 = this.buffer;
      var1[1] = (byte)(var1[1] | 4);
   }

   public void setRs2Valid() {
      byte[] var1 = this.buffer;
      var1[1] = (byte)(var1[1] | 8);
   }

   public void setSasVerified() {
      byte[] var1 = this.buffer;
      var1[1] = (byte)(var1[1] | 2);
   }

   protected void setValid() {
      byte[] var1 = this.buffer;
      var1[1] = (byte)(var1[1] | 1);
   }
}
