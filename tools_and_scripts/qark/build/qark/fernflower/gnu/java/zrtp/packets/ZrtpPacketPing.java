package gnu.java.zrtp.packets;

import gnu.java.zrtp.ZrtpConstants;
import gnu.java.zrtp.utils.ZrtpUtils;

public class ZrtpPacketPing extends ZrtpPacketBase {
   private static final int EP_OFFSET = 16;
   private static final int PING_LENGTH = 28;
   private static final int VERSION_OFFSET = 12;
   private static final int ZRTP_PING_LENGTH = 3;

   protected ZrtpPacketPing() {
      super(new byte[28]);
      this.setZrtpId();
      this.setVersion(ZrtpConstants.zrtpVersion_11);
      this.setLength(6);
      this.setMessageType(ZrtpConstants.PingMsg);
   }

   public ZrtpPacketPing(byte[] var1) {
      super(var1);
   }

   private void setVersion(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 12, 4);
   }

   public final byte[] getEpHash() {
      return ZrtpUtils.readRegion(this.packetBuffer, 16, 8);
   }

   public final void setEpHash(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 16, 8);
   }
}
