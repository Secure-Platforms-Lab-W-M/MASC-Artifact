package gnu.java.zrtp.packets;

import gnu.java.zrtp.ZrtpConstants;
import gnu.java.zrtp.utils.ZrtpUtils;

public class ZrtpPacketCommit extends ZrtpPacketBase {
   private static final int AUTHLENGTHS_OFFSET = 64;
   private static final int CIPHER_OFFSET = 60;
   private static final int COMMIT_LENGTH = 120;
   private static final int HASH_H2_OFFSET = 12;
   private static final int HASH_OFFSET = 56;
   private static final int HMAC_OFFSET = 108;
   private static final int HVI_OFFSET = 76;
   private static final int PUBKEY_OFFSET = 68;
   private static final int SAS_OFFSET = 72;
   private static final int ZID_OFFSET = 44;
   private static final int ZRTP_COMMIT_LENGTH = 26;

   public ZrtpPacketCommit() {
      super(new byte[120]);
      this.setZrtpId();
      this.setLength(29);
      this.setMessageType(ZrtpConstants.CommitMsg);
   }

   public ZrtpPacketCommit(byte[] var1) {
      super(var1);
   }

   public final ZrtpConstants.SupportedAuthLengths getAuthlen() {
      ZrtpConstants.SupportedAuthLengths[] var3 = ZrtpConstants.SupportedAuthLengths.values();
      int var2 = var3.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         ZrtpConstants.SupportedAuthLengths var4 = var3[var1];
         byte[] var5 = var4.name;
         if (var5[0] == this.packetBuffer[64] && var5[1] == this.packetBuffer[65] && var5[2] == this.packetBuffer[66] && var5[3] == this.packetBuffer[67]) {
            return var4;
         }
      }

      return null;
   }

   public final ZrtpConstants.SupportedSymCiphers getCipher() {
      ZrtpConstants.SupportedSymCiphers[] var3 = ZrtpConstants.SupportedSymCiphers.values();
      int var2 = var3.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         ZrtpConstants.SupportedSymCiphers var4 = var3[var1];
         byte[] var5 = var4.name;
         if (var5[0] == this.packetBuffer[60] && var5[1] == this.packetBuffer[61] && var5[2] == this.packetBuffer[62] && var5[3] == this.packetBuffer[63]) {
            return var4;
         }
      }

      return null;
   }

   public final byte[] getH2() {
      return ZrtpUtils.readRegion(this.packetBuffer, 12, 32);
   }

   public final byte[] getHMAC() {
      return ZrtpUtils.readRegion(this.packetBuffer, 108, 8);
   }

   public final byte[] getHMACMulti() {
      return ZrtpUtils.readRegion(this.packetBuffer, 92, 8);
   }

   public final ZrtpConstants.SupportedHashes getHash() {
      ZrtpConstants.SupportedHashes[] var3 = ZrtpConstants.SupportedHashes.values();
      int var2 = var3.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         ZrtpConstants.SupportedHashes var4 = var3[var1];
         byte[] var5 = var4.name;
         if (var5[0] == this.packetBuffer[56] && var5[1] == this.packetBuffer[57] && var5[2] == this.packetBuffer[58] && var5[3] == this.packetBuffer[59]) {
            return var4;
         }
      }

      return null;
   }

   public final byte[] getHvi() {
      return ZrtpUtils.readRegion(this.packetBuffer, 76, 32);
   }

   public final byte[] getNonce() {
      return ZrtpUtils.readRegion(this.packetBuffer, 76, 16);
   }

   public final ZrtpConstants.SupportedPubKeys getPubKey() {
      ZrtpConstants.SupportedPubKeys[] var3 = ZrtpConstants.SupportedPubKeys.values();
      int var2 = var3.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         ZrtpConstants.SupportedPubKeys var4 = var3[var1];
         byte[] var5 = var4.name;
         if (var5[0] == this.packetBuffer[68] && var5[1] == this.packetBuffer[69] && var5[2] == this.packetBuffer[70] && var5[3] == this.packetBuffer[71]) {
            return var4;
         }
      }

      return null;
   }

   public ZrtpConstants.SupportedSASTypes getSas() {
      ZrtpConstants.SupportedSASTypes[] var3 = ZrtpConstants.SupportedSASTypes.values();
      int var2 = var3.length;

      for(int var1 = 0; var1 < var2; ++var1) {
         ZrtpConstants.SupportedSASTypes var4 = var3[var1];
         byte[] var5 = var4.name;
         if (var5[0] == this.packetBuffer[72] && var5[1] == this.packetBuffer[73] && var5[2] == this.packetBuffer[74] && var5[3] == this.packetBuffer[75]) {
            return var4;
         }
      }

      return null;
   }

   public final byte[] getZid() {
      return ZrtpUtils.readRegion(this.packetBuffer, 44, 12);
   }

   public final boolean isLengthOk() {
      return this.getLength() >= 25;
   }

   public final void setAuthLen(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 64, 4);
   }

   public final void setCipherType(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 60, 4);
   }

   public final void setH2(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 12, 32);
   }

   public final void setHMAC(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 108, 8);
   }

   public final void setHMACMulti(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 92, 8);
   }

   public final void setHashType(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 56, 4);
   }

   public final void setHvi(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 76, 32);
   }

   public final void setNonce(byte[] var1) {
      byte[] var2 = new byte[104];
      System.arraycopy(this.packetBuffer, 0, var2, 0, 104);
      this.packetBuffer = var2;
      System.arraycopy(var1, 0, this.packetBuffer, 76, 16);
      this.setLength(25);
   }

   public final void setPubKeyType(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 68, 4);
   }

   public final void setSasType(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 72, 4);
   }

   public final void setZid(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 44, 12);
   }
}
