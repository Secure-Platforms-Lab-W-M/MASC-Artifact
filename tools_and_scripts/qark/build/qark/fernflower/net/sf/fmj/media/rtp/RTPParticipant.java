package net.sf.fmj.media.rtp;

import java.util.HashMap;
import java.util.Vector;
import javax.media.rtp.Participant;
import javax.media.rtp.RTPStream;
import javax.media.rtp.rtcp.Report;
import javax.media.rtp.rtcp.SourceDescription;

public class RTPParticipant implements Participant {
   private boolean active = false;
   private String cName = "";
   protected long lastReportTime = System.currentTimeMillis();
   private HashMap rtcpReports = new HashMap();
   private int sdesSize = 0;
   protected HashMap sourceDescriptions = new HashMap();
   private Vector streams = new Vector();

   public RTPParticipant(String var1) {
      this.cName = var1;
      this.addSourceDescription(new SourceDescription(1, var1, 1, false));
      this.addSourceDescription(new SourceDescription(2, var1, 1, false));
   }

   public void addReport(Report var1) {
      this.lastReportTime = System.currentTimeMillis();
      this.rtcpReports.put(new Long(var1.getSSRC()), var1);
      Vector var3 = var1.getSourceDescription();

      for(int var2 = 0; var2 < var3.size(); ++var2) {
         this.addSourceDescription((SourceDescription)var3.get(var2));
      }

      if (this.streams.size() == 0 && var1 instanceof RTCPReport) {
         ((RTCPReport)var1).sourceDescriptions = new Vector(this.sourceDescriptions.values());
      }

   }

   protected void addSourceDescription(SourceDescription var1) {
      SourceDescription var3 = (SourceDescription)this.sourceDescriptions.get(new Integer(var1.getType()));
      int var2;
      if (var3 != null) {
         var2 = this.sdesSize - var3.getDescription().length();
         this.sdesSize = var2;
         this.sdesSize = var2 - 2;
      }

      this.sourceDescriptions.put(new Integer(var1.getType()), var1);
      var2 = this.sdesSize + 2;
      this.sdesSize = var2;
      this.sdesSize = var2 + var1.getDescription().length();
   }

   protected void addStream(RTPStream var1) {
      this.streams.add(var1);
   }

   public String getCNAME() {
      return this.cName;
   }

   public long getLastReportTime() {
      return this.lastReportTime;
   }

   public Vector getReports() {
      return new Vector(this.rtcpReports.values());
   }

   public int getSdesSize() {
      return this.sdesSize;
   }

   public Vector getSourceDescription() {
      return new Vector(this.sourceDescriptions.values());
   }

   public Vector getStreams() {
      return this.streams;
   }

   public boolean isActive() {
      return this.active;
   }

   protected void removeStream(RTPStream var1) {
      this.streams.remove(var1);
   }

   protected void setActive(boolean var1) {
      this.active = var1;
   }
}
