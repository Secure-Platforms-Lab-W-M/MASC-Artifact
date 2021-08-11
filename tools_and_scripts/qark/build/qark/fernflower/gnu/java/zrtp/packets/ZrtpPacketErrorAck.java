package gnu.java.zrtp.packets;

import gnu.java.zrtp.ZrtpConstants;

public class ZrtpPacketErrorAck extends ZrtpPacketBase {
   private static final int ERROR_ACK_LENGTH = 16;

   public ZrtpPacketErrorAck() {
      super(new byte[16]);
      this.setZrtpId();
      this.setLength(3);
      this.setMessageType(ZrtpConstants.ErrorAckMsg);
   }

   public ZrtpPacketErrorAck(byte[] var1) {
      super(var1);
   }
}
