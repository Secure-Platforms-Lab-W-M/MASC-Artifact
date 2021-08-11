package gnu.java.zrtp.packets;

import gnu.java.zrtp.ZrtpConstants;

public class ZrtpPacketRelayAck extends ZrtpPacketBase {
   private static final int RELAY_ACK_LENGTH = 16;

   public ZrtpPacketRelayAck() {
      super(new byte[16]);
      this.setZrtpId();
      this.setLength(3);
      this.setMessageType(ZrtpConstants.RelayAckMsg);
   }

   public ZrtpPacketRelayAck(byte[] var1) {
      super(var1);
   }
}
