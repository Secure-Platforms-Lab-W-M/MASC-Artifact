package gnu.java.zrtp.packets;

import gnu.java.zrtp.ZrtpConstants;
import gnu.java.zrtp.utils.ZrtpUtils;

public class ZrtpPacketSASRelay extends ZrtpPacketBase {
   private static final int CONFIRM_FIXED_LENGTH = 80;
   private static final int FILLER_OFFSET = 36;
   private static final int FLAGS_OFFSET = 39;
   private static final int HMAC_OFFSET = 12;
   private static final int IV_OFFSET = 20;
   private static final int SAS_OFFSET = 40;
   private static final int SIG_DATA_OFFSET = 76;
   private static final int SIG_LENGTH_OFFSET = 38;
   private static final int TRUSTED_SAS_OFFSET = 44;
   private static final int ZRTP_SAS_RELAY_FIXED_LENGTH = 16;
   private int signatureLength;

   public ZrtpPacketSASRelay() {
      super((byte[])null);
      this.setSignatureLength(0);
      this.setMessageType(ZrtpConstants.SASRelayMsg);
   }

   public ZrtpPacketSASRelay(int var1) {
      super((byte[])null);
      this.setSignatureLength(var1);
      this.setMessageType(ZrtpConstants.SASRelayMsg);
   }

   public ZrtpPacketSASRelay(byte[] var1) {
      super(var1);
      this.signatureLength = this.packetBuffer[38] & 255;
      if (this.packetBuffer[37] == 1) {
         this.signatureLength |= 256;
      }

   }

   public final byte[] getDataToSecure() {
      short var1 = this.getLength();
      return ZrtpUtils.readRegion(this.packetBuffer, 36, (var1 - 9) * 4);
   }

   public final byte[] getHmac() {
      return ZrtpUtils.readRegion(this.packetBuffer, 12, 8);
   }

   public final byte[] getIv() {
      return ZrtpUtils.readRegion(this.packetBuffer, 20, 16);
   }

   public ZrtpConstants.SupportedSASTypes getSas() {
      ZrtpConstants.SupportedSASTypes[] var3 = ZrtpConstants.SupportedSASTypes.values();
      int var2 = var3.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         ZrtpConstants.SupportedSASTypes var4 = var3[var1];
         byte[] var5 = var4.name;
         if (var5[0] == this.packetBuffer[40] && var5[1] == this.packetBuffer[41] && var5[2] == this.packetBuffer[42] && var5[3] == this.packetBuffer[43]) {
            return var4;
         }
      }

      return null;
   }

   public final byte[] getSignatureData() {
      return ZrtpUtils.readRegion(this.packetBuffer, 76, this.signatureLength);
   }

   public final int getSignatureLength() {
      return this.signatureLength;
   }

   public final byte[] getTrustedSas() {
      return ZrtpUtils.readRegion(this.packetBuffer, 44, 32);
   }

   public final boolean isLengthOk() {
      return this.getLength() >= 19;
   }

   public final boolean isSASFlag() {
      return (this.packetBuffer[39] & 4) == 4;
   }

   public final void setDataToSecure(byte[] var1) {
      short var2 = this.getLength();
      System.arraycopy(var1, 0, this.packetBuffer, 36, (var2 - 9) * 4);
   }

   public final void setHmac(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 12, 8);
   }

   public final void setIv(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 20, 16);
   }

   public final void setSASFlag() {
      byte[] var1 = this.packetBuffer;
      var1[39] = (byte)(var1[39] | 4);
   }

   public final void setSasType(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 40, 4);
   }

   public final void setSignatureData(byte[] var1) {
      if (var1.length <= this.signatureLength) {
         System.arraycopy(var1, 0, this.packetBuffer, 76, var1.length);
      }
   }

   public final void setSignatureLength(int var1) {
      if (var1 <= 512) {
         this.signatureLength = var1;
         int var2 = var1 * 4 + 80;
         if (this.packetBuffer == null) {
            this.packetBuffer = new byte[var2];
         } else {
            byte[] var3 = new byte[var2];
            System.arraycopy(this.packetBuffer, 0, var3, 0, 76);
            this.packetBuffer = var3;
         }

         this.packetBuffer[38] = (byte)var1;
         if (var1 > 255) {
            this.packetBuffer[37] = 1;
         }

         this.setLength((var2 - 4) / 4);
         this.setZrtpId();
      }
   }

   public final void setTrustedSas(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 44, 32);
   }
}
