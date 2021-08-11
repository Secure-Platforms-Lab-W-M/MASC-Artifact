package net.sf.fmj.media.rtp;

import net.sf.fmj.media.rtp.util.RTPPacket;

public class SourceRTPPacket {
   // $FF: renamed from: p net.sf.fmj.media.rtp.util.RTPPacket
   RTPPacket field_39;
   SSRCInfo ssrcinfo;

   public SourceRTPPacket(RTPPacket var1, SSRCInfo var2) {
      this.field_39 = var1;
      this.ssrcinfo = var2;
   }
}
