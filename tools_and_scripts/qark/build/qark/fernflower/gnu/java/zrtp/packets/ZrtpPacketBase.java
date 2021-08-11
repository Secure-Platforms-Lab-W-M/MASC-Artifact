package gnu.java.zrtp.packets;

import gnu.java.zrtp.utils.ZrtpUtils;

public class ZrtpPacketBase {
   public static final int CLIENT_ID_SIZE = 16;
   public static final int CRC_SIZE = 4;
   public static final int HASH_IMAGE_SIZE = 32;
   public static final int HMAC_SIZE = 8;
   public static final int HVI_SIZE = 32;
   private static final int ID_OFFSET = 0;
   private static final int LENGTH_OFFSET = 2;
   private static final int TYPE_LENGTH = 8;
   private static final int TYPE_OFFSET = 4;
   public static final int ZID_SIZE = 12;
   protected static final int ZRTP_HEADER_LENGTH = 3;
   public static final int ZRTP_WORD_SIZE = 4;
   private static byte[] zrtpId;
   protected byte[] packetBuffer = null;

   static {
      byte[] var0 = new byte[2];
      zrtpId = var0;
      var0[0] = 80;
      var0[1] = 90;
   }

   protected ZrtpPacketBase(byte[] var1) {
      this.packetBuffer = var1;
   }

   public final byte[] getHeaderBase() {
      return this.packetBuffer;
   }

   public final short getLength() {
      return ZrtpUtils.readShort(this.packetBuffer, 2);
   }

   public final String getMessageType() {
      return new String(this.packetBuffer, 4, 8);
   }

   public final boolean isZrtpPacket() {
      byte[] var4 = this.packetBuffer;
      boolean var3 = false;
      byte var1 = var4[0];
      byte[] var5 = zrtpId;
      boolean var2 = var3;
      if (var1 == var5[0]) {
         var2 = var3;
         if (var4[1] == var5[1]) {
            var2 = true;
         }
      }

      return var2;
   }

   protected final void setLength(int var1) {
      ZrtpUtils.short16ToArrayInPlace(var1, this.packetBuffer, 2);
   }

   public final void setMessageType(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 4, 8);
   }

   protected final void setZrtpId() {
      byte[] var1 = zrtpId;
      System.arraycopy(var1, 0, this.packetBuffer, 0, var1.length);
   }
}
