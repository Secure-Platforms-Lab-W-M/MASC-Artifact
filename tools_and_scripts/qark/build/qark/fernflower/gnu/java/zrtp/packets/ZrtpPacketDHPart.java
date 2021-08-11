package gnu.java.zrtp.packets;

import gnu.java.zrtp.ZrtpConstants;
import gnu.java.zrtp.utils.ZrtpUtils;

public class ZrtpPacketDHPart extends ZrtpPacketBase {
   private static final int DHPART_FIXED_LENGTH = 80;
   private static final int HASH_H1_OFFSET = 12;
   private static final int PBX_SECRET_ID_OFFSET = 68;
   private static final int PUBLIC_KEY_OFFSET = 76;
   private static final int RS1ID_OFFSET = 44;
   private static final int RS2ID_OFFSET = 52;
   private static final int S3_ID_OFFSET = 60;
   private static final int ZRTP_DHPART_FIXED_LENGTH = 16;
   private int dhLength;

   public ZrtpPacketDHPart() {
      super((byte[])null);
   }

   public ZrtpPacketDHPart(ZrtpConstants.SupportedPubKeys var1) {
      super((byte[])null);
      this.setPubKeyType(var1);
   }

   public ZrtpPacketDHPart(byte[] var1) {
      super(var1);
      short var2 = this.getLength();
      if (var2 == 85) {
         this.dhLength = 256;
      } else if (var2 == 117) {
         this.dhLength = 384;
      } else if (var2 == 37) {
         this.dhLength = 64;
      } else if (var2 == 45) {
         this.dhLength = 96;
      } else if (var2 == 29) {
         this.dhLength = 32;
      } else {
         this.dhLength = 0;
      }
   }

   public final byte[] getAuxSecretId() {
      return ZrtpUtils.readRegion(this.packetBuffer, 60, 8);
   }

   public final byte[] getH1() {
      return ZrtpUtils.readRegion(this.packetBuffer, 12, 32);
   }

   public final byte[] getHMAC() {
      return ZrtpUtils.readRegion(this.packetBuffer, this.dhLength + 76, 8);
   }

   public final byte[] getPbxSecretId() {
      return ZrtpUtils.readRegion(this.packetBuffer, 68, 8);
   }

   public final byte[] getPv() {
      return ZrtpUtils.readRegion(this.packetBuffer, 76, this.dhLength);
   }

   public final byte[] getRs1Id() {
      return ZrtpUtils.readRegion(this.packetBuffer, 44, 8);
   }

   public final byte[] getRs2Id() {
      return ZrtpUtils.readRegion(this.packetBuffer, 52, 8);
   }

   public final boolean isLengthOk() {
      return this.getLength() >= 29;
   }

   public final void setAuxSecretId(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 60, 8);
   }

   public final void setH1(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 12, 32);
   }

   public final void setHMAC(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, this.dhLength + 76, 8);
   }

   public final void setPbxSecretId(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 68, 8);
   }

   public void setPubKeyType(ZrtpConstants.SupportedPubKeys var1) {
      if (var1 == ZrtpConstants.SupportedPubKeys.DH2K) {
         this.dhLength = 256;
      } else if (var1 == ZrtpConstants.SupportedPubKeys.DH3K) {
         this.dhLength = 384;
      } else if (var1 == ZrtpConstants.SupportedPubKeys.EC25) {
         this.dhLength = 64;
      } else if (var1 == ZrtpConstants.SupportedPubKeys.EC38) {
         this.dhLength = 96;
      } else {
         if (var1 != ZrtpConstants.SupportedPubKeys.E255) {
            return;
         }

         this.dhLength = 32;
      }

      int var2 = this.dhLength + 80 + 8;
      this.packetBuffer = new byte[var2];
      this.setLength((var2 - 4) / 4);
      this.setZrtpId();
   }

   public final void setPv(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 76, this.dhLength);
   }

   public final void setRs1Id(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 44, 8);
   }

   public final void setRs2Id(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 52, 8);
   }
}
