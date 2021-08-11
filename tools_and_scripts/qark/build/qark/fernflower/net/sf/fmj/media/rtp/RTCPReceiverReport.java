package net.sf.fmj.media.rtp;

import java.io.IOException;
import javax.media.rtp.rtcp.ReceiverReport;

public class RTCPReceiverReport extends RTCPReport implements ReceiverReport {
   public RTCPReceiverReport(byte[] var1, int var2, int var3) throws IOException {
      super(var1, var2, var3);
   }
}
