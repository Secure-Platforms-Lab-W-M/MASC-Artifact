package gnu.java.zrtp.packets;

import gnu.java.zrtp.ZrtpConstants;

public class ZrtpPacketHelloAck extends ZrtpPacketBase {
   private static final int HELLO_ACK_LENGTH = 16;

   public ZrtpPacketHelloAck() {
      super(new byte[16]);
      this.setZrtpId();
      this.setLength(3);
      this.setMessageType(ZrtpConstants.HelloAckMsg);
   }

   public ZrtpPacketHelloAck(byte[] var1) {
      super(var1);
   }
}
