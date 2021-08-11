package javax.media.rtp.event;

import javax.media.rtp.SessionManager;
import javax.media.rtp.rtcp.SenderReport;

public class SenderReportEvent extends RemoteEvent {
   private SenderReport report;

   public SenderReportEvent(SessionManager var1, SenderReport var2) {
      super(var1);
      this.report = var2;
   }

   public SenderReport getReport() {
      return this.report;
   }
}
