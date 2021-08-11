package gnu.java.zrtp.packets;

import gnu.java.zrtp.utils.ZrtpUtils;

public class ZrtpPacketConfirm extends ZrtpPacketBase {
   private static final int CONFIRM_FIXED_LENGTH = 80;
   private static final int EXP_TIME_OFFSET = 72;
   private static final int FILLER_OFFSET = 68;
   private static final int FLAGS_OFFSET = 71;
   private static final int HASH_H0_OFFSET = 36;
   private static final int HMAC_OFFSET = 12;
   private static final int IV_OFFSET = 20;
   private static final int SIG_DATA_OFFSET = 76;
   private static final int SIG_LENGTH_OFFSET = 70;
   private static final int ZRTP_CONFIRM_FIXED_LENGTH = 16;
   private int signatureLength;

   public ZrtpPacketConfirm() {
      super((byte[])null);
      this.setSignatureLength(0);
   }

   public ZrtpPacketConfirm(int var1) {
      super((byte[])null);
      this.setSignatureLength(var1);
   }

   public ZrtpPacketConfirm(byte[] var1) {
      super(var1);
   }

   public final byte[] getDataToSecure() {
      short var1 = this.getLength();
      return ZrtpUtils.readRegion(this.packetBuffer, 36, (var1 - 9) * 4);
   }

   public final int getExpTime() {
      return ZrtpUtils.readInt(this.packetBuffer, 72);
   }

   public final byte[] getHashH0() {
      return ZrtpUtils.readRegion(this.packetBuffer, 36, 32);
   }

   public final byte[] getHmac() {
      return ZrtpUtils.readRegion(this.packetBuffer, 12, 8);
   }

   public final byte[] getIv() {
      return ZrtpUtils.readRegion(this.packetBuffer, 20, 16);
   }

   public final byte[] getSignatureData() {
      return ZrtpUtils.readRegion(this.packetBuffer, 76, this.signatureLength * 4);
   }

   public final int getSignatureLength() {
      this.signatureLength = this.packetBuffer[70] & 255;
      if (this.packetBuffer[69] == 1) {
         this.signatureLength |= 256;
      }

      return this.signatureLength;
   }

   public final boolean isLengthOk() {
      return this.getLength() >= 19;
   }

   public final boolean isPBXEnrollment() {
      return (this.packetBuffer[71] & 8) == 8;
   }

   public final boolean isSASFlag() {
      return (this.packetBuffer[71] & 4) == 4;
   }

   public final void setDataToSecure(byte[] var1) {
      short var2 = this.getLength();
      System.arraycopy(var1, 0, this.packetBuffer, 36, (var2 - 9) * 4);
   }

   public final void setExpTime(int var1) {
      ZrtpUtils.int32ToArrayInPlace(var1, this.packetBuffer, 72);
   }

   public final void setHashH0(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 36, 32);
   }

   public final void setHmac(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 12, 8);
   }

   public final void setIv(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 20, 16);
   }

   public final void setPBXEnrollment() {
      byte[] var1 = this.packetBuffer;
      var1[71] = (byte)(var1[71] | 8);
   }

   public final void setSASFlag() {
      byte[] var1 = this.packetBuffer;
      var1[71] = (byte)(var1[71] | 4);
   }

   public final boolean setSignatureData(byte[] var1) {
      if (var1.length / 4 > this.signatureLength) {
         return false;
      } else {
         System.arraycopy(var1, 0, this.packetBuffer, 76, var1.length);
         return true;
      }
   }

   public final boolean setSignatureLength(int var1) {
      if (var1 > 512) {
         return false;
      } else {
         this.signatureLength = var1;
         int var2 = var1 * 4 + 80;
         if (this.packetBuffer == null) {
            this.packetBuffer = new byte[var2];
         } else {
            byte[] var3 = new byte[var2];
            System.arraycopy(this.packetBuffer, 0, var3, 0, 76);
            this.packetBuffer = var3;
         }

         this.packetBuffer[70] = (byte)var1;
         if (var1 > 255) {
            this.packetBuffer[69] = 1;
         }

         this.setLength((var2 - 4) / 4);
         this.setZrtpId();
         return true;
      }
   }
}
