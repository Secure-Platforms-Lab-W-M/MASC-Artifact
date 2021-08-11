package javax.media.rtp.event;

import javax.media.rtp.Participant;
import javax.media.rtp.ReceiveStream;
import javax.media.rtp.SessionManager;

public class ApplicationEvent extends ReceiveStreamEvent {
   private byte[] appData;
   private String appString;
   private int appSubtype;

   public ApplicationEvent(SessionManager var1, Participant var2, ReceiveStream var3, int var4, String var5, byte[] var6) {
      super(var1, var3, var2);
      this.appSubtype = var4;
      this.appString = var5;
      this.appData = var6;
   }

   public byte[] getAppData() {
      return this.appData;
   }

   public String getAppString() {
      return this.appString;
   }

   public int getAppSubType() {
      return this.appSubtype;
   }
}
