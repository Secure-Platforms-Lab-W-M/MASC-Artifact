package net.sf.fmj.media.rtp;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Vector;
import javax.media.rtp.Participant;
import javax.media.rtp.rtcp.Report;
import javax.media.rtp.rtcp.SourceDescription;

public abstract class RTCPReport implements Report {
   private String byeReason = "";
   private String cName = null;
   protected Vector feedbackReports = new Vector();
   protected RTCPHeader header;
   private boolean isBye = false;
   protected Participant participant;
   protected int sdesBytes = 0;
   protected Vector sourceDescriptions = new Vector();
   private long ssrc = 0L;
   private long systemTimeStamp;

   public RTCPReport(byte[] var1, int var2, int var3) throws IOException {
      RTCPHeader var6 = new RTCPHeader(var1, var2, var3);
      this.header = var6;
      if (var6.getPadding() != 1) {
         if ((this.header.getLength() + 1) * 4 <= var3) {
            this.ssrc = this.header.getSsrc();
            int var4 = 8;
            short var5 = this.header.getPacketType();
            if (var5 != 200) {
               if (var5 != 201) {
                  return;
               }
            } else {
               var4 = 8 + 20;
            }

            this.readFeedbackReports(var1, var2 + var4, var3 - var4);
            var2 += (this.header.getLength() + 1) * 4;
            var3 -= (this.header.getLength() + 1) * 4;
            this.readSourceDescription(var1, var2, var3);
            var4 = this.sdesBytes;
            this.readBye(var1, var2 + var4, var3 - var4);
         } else {
            throw new IOException("Invalid Length");
         }
      } else {
         throw new IOException("First packet has padding");
      }
   }

   public String getByeReason() {
      return this.byeReason;
   }

   public String getCName() {
      return this.cName;
   }

   public Vector getFeedbackReports() {
      return this.feedbackReports;
   }

   public Participant getParticipant() {
      return this.participant;
   }

   public long getSSRC() {
      return this.ssrc;
   }

   public Vector getSourceDescription() {
      return this.sourceDescriptions;
   }

   public long getSystemTimeStamp() {
      return this.systemTimeStamp;
   }

   public boolean isByePacket() {
      return this.isBye;
   }

   protected void readBye(byte[] var1, int var2, int var3) throws IOException {
      if (var3 > 0) {
         RTCPHeader var5 = new RTCPHeader(var1, var2, var3);
         if (var5.getPacketType() == 203) {
            this.isBye = true;
            if ((var5.getLength() + 1) * 4 > 8) {
               int var4 = var1[var2 + 8] & 255;
               if (var4 < var3 - 8 && var4 > 0) {
                  this.byeReason = new String(var1, var2 + 8 + 1, var4);
               }
            }
         }
      }

   }

   protected void readFeedbackReports(byte[] var1, int var2, int var3) throws IOException {
      byte var5 = 0;
      int var4 = var2;

      for(var2 = var5; var2 < this.header.getReceptionCount(); ++var2) {
         RTCPFeedback var6 = new RTCPFeedback(var1, var4, var3);
         this.feedbackReports.add(var6);
         var4 += 24;
      }

   }

   protected void readSourceDescription(byte[] var1, int var2, int var3) throws IOException {
      if (var3 > 0) {
         RTCPHeader var4 = new RTCPHeader(var1, var2, var3);
         if (var4.getPacketType() == 202) {
            this.ssrc = var4.getSsrc();
            this.sdesBytes = (var4.getLength() + 1) * 4;
            DataInputStream var6 = new DataInputStream(new ByteArrayInputStream(var1, var2 + 8, var3));
            var2 = 1;

            while(var2 != 0) {
               var3 = var6.readUnsignedByte();
               var2 = var3;
               if (var3 != 0) {
                  byte[] var7 = new byte[var6.readUnsignedByte()];
                  var6.readFully(var7);
                  String var8 = new String(var7, "UTF-8");
                  SourceDescription var5 = new SourceDescription(var3, var8, 0, false);
                  this.sourceDescriptions.add(var5);
                  if (var3 == 1) {
                     this.cName = var8;
                  }

                  var2 = var3;
               }
            }
         }
      }

   }

   protected void setParticipant(RTPParticipant var1) {
      this.participant = var1;
      if (var1.getStreams().size() == 0) {
         Vector var3 = var1.getSourceDescription();

         for(int var2 = 0; var2 < var3.size(); ++var2) {
            var1.addSourceDescription((SourceDescription)var3.get(var2));
         }
      }

   }

   public void setSystemTimeStamp(long var1) {
      this.systemTimeStamp = var1;
   }
}
