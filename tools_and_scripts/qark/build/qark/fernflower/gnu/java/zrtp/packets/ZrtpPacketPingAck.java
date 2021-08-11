package gnu.java.zrtp.packets;

import gnu.java.zrtp.ZrtpConstants;
import gnu.java.zrtp.utils.ZrtpUtils;

public class ZrtpPacketPingAck extends ZrtpPacketBase {
   private static final int LOCAL_EP_OFFSET = 16;
   private static final int PEER_SSRC_OFFSET = 32;
   private static final int PING_ACK_LENGTH = 40;
   private static final int REMOTE_EP_OFFSET = 24;
   private static final int VERSION_OFFSET = 12;
   private static final int ZRTP_PING_ACK_LENGTH = 6;

   public ZrtpPacketPingAck() {
      super(new byte[40]);
      this.setZrtpId();
      this.setVersion(ZrtpConstants.zrtpVersion_11);
      this.setLength(9);
      this.setMessageType(ZrtpConstants.PingAckMsg);
   }

   public ZrtpPacketPingAck(byte[] var1) {
      super(var1);
   }

   private void setVersion(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 12, 4);
   }

   public final byte[] getLocalEpHash() {
      return ZrtpUtils.readRegion(this.packetBuffer, 16, 8);
   }

   public final byte[] getRemoteEpHash() {
      return ZrtpUtils.readRegion(this.packetBuffer, 24, 8);
   }

   public final void setLocalEpHash(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 16, 8);
   }

   public final void setPeerSSRC(int var1) {
      ZrtpUtils.int32ToArrayInPlace(var1, this.packetBuffer, 32);
   }

   public final void setRemoteEpHash(byte[] var1) {
      System.arraycopy(var1, 0, this.packetBuffer, 24, 8);
   }
}
