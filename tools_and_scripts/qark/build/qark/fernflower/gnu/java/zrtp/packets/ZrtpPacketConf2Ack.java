package gnu.java.zrtp.packets;

import gnu.java.zrtp.ZrtpConstants;

public class ZrtpPacketConf2Ack extends ZrtpPacketBase {
   private static final int CONF2_ACK_LENGTH = 16;

   public ZrtpPacketConf2Ack() {
      super(new byte[16]);
      this.setZrtpId();
      this.setLength(3);
      this.setMessageType(ZrtpConstants.Conf2AckMsg);
   }

   public ZrtpPacketConf2Ack(byte[] var1) {
      super(var1);
   }
}
