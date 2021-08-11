package net.sf.fmj.media.rtp;

import javax.media.rtp.rtcp.ReceiverReport;

public class PassiveSSRCInfo extends SSRCInfo implements ReceiverReport {
   PassiveSSRCInfo(SSRCCache var1, int var2) {
      super(var1, var2);
   }

   PassiveSSRCInfo(SSRCInfo var1) {
      super(var1);
   }
}
