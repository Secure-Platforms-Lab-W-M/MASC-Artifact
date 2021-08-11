package gnu.java.zrtp.packets;

import gnu.java.zrtp.ZrtpConstants;
import gnu.java.zrtp.utils.ZrtpUtils;

public class ZrtpPacketError extends ZrtpPacketBase {
   private static final int CODE_OFFSET = 12;
   private static final int ERROR_LENGTH = 20;
   private static final int ZRTP_ERROR_LENGTH = 1;

   public ZrtpPacketError() {
      super(new byte[20]);
      this.setZrtpId();
      this.setLength(4);
      this.setMessageType(ZrtpConstants.ErrorMsg);
   }

   public ZrtpPacketError(byte[] var1) {
      super(var1);
   }

   public final int getErrorCode() {
      return ZrtpUtils.readInt(this.packetBuffer, 12);
   }

   public final void setErrorCode(int var1) {
      ZrtpUtils.int32ToArrayInPlace(var1, this.packetBuffer, 12);
   }
}
