package net.sf.fmj.media.datasink.rtp;

import javax.media.format.AudioFormat;
import javax.media.rtp.RTPManager;

public class RTPBonusFormatsMgr {
   public static final int ALAW_RTP_INDEX = 8;
   public static final int ILBC_RTP_INDEX = 97;
   public static final int SPEEX_RTP_INDEX = 110;

   public static void addBonusFormats(RTPManager var0) {
      var0.addFormat(new AudioFormat("ALAW/rtp", 8000.0D, 8, 1, -1, 1), 8);
      var0.addFormat(new AudioFormat("speex/rtp", 8000.0D, 8, 1, -1, 1), 110);
      var0.addFormat(new AudioFormat("ilbc/rtp", 8000.0D, 16, 1, 0, 1), 97);
   }
}
