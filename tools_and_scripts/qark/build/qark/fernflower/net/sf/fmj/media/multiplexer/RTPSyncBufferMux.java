package net.sf.fmj.media.multiplexer;

import javax.media.Format;
import javax.media.protocol.ContentDescriptor;
import net.sf.fmj.media.rtp.FormatInfo;
import net.sf.fmj.media.rtp.RTPSessionMgr;

public class RTPSyncBufferMux extends RawSyncBufferMux {
   FormatInfo rtpFormats = new FormatInfo();

   public RTPSyncBufferMux() {
      this.supported = new ContentDescriptor[1];
      this.supported[0] = new ContentDescriptor("raw.rtp");
      this.monoIncrTime = true;
   }

   public String getName() {
      return "RTP Sync Buffer Multiplexer";
   }

   public Format setInputFormat(Format var1, int var2) {
      return !RTPSessionMgr.formatSupported(var1) ? null : super.setInputFormat(var1, var2);
   }
}
