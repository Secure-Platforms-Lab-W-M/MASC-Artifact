package javax.media.rtp.event;

import javax.media.rtp.SessionManager;
import javax.media.rtp.rtcp.ReceiverReport;

public class ReceiverReportEvent extends RemoteEvent {
   private ReceiverReport report;

   public ReceiverReportEvent(SessionManager var1, ReceiverReport var2) {
      super(var1);
      this.report = var2;
   }

   public ReceiverReport getReport() {
      return this.report;
   }
}
