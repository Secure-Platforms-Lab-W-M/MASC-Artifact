package net.sf.fmj.media.rtp;

import javax.media.rtp.LocalParticipant;
import javax.media.rtp.rtcp.SourceDescription;

public class RTPLocalSourceInfo extends RTPSourceInfo implements LocalParticipant {
   public RTPLocalSourceInfo(String var1, RTPSourceInfoCache var2) {
      super(var1, var2);
   }

   public void setSourceDescription(SourceDescription[] var1) {
      super.sic.ssrccache.ourssrc.setSourceDescription(var1);
   }
}
