package net.sf.fmj.media.rtp;

import java.io.IOException;
import java.util.Vector;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.DataSource;
import javax.media.rtp.LocalParticipant;
import javax.media.rtp.Participant;
import javax.media.rtp.RTPStream;
import javax.media.rtp.SendStream;
import javax.media.rtp.TransmissionStats;
import javax.media.rtp.rtcp.Feedback;
import javax.media.rtp.rtcp.Report;
import javax.media.rtp.rtcp.SenderReport;
import javax.media.rtp.rtcp.SourceDescription;

public class SendSSRCInfo extends SSRCInfo implements SenderReport, SendStream {
   static AudioFormat dviAudio = new AudioFormat("dvi/rtp");
   static AudioFormat g723Audio = new AudioFormat("g723/rtp");
   static AudioFormat gsmAudio = new AudioFormat("gsm/rtp");
   static AudioFormat mpegAudio = new AudioFormat("mpegaudio/rtp");
   static VideoFormat mpegVideo = new VideoFormat("mpeg/rtp");
   static AudioFormat ulawAudio = new AudioFormat("ULAW/rtp");
   private boolean inited = false;
   private long lastBufSeq = -1L;
   private long lastSeq = -1L;
   protected Format myformat;
   protected int packetsize = 0;
   public RTPTransStats stats;
   private long totalSamples = 0L;

   public SendSSRCInfo(SSRCCache var1, int var2) {
      super(var1, var2);
      this.init();
   }

   public SendSSRCInfo(SSRCInfo var1) {
      super(var1);
      this.init();
   }

   private int calculateSampleCount(Buffer var1) {
      AudioFormat var4 = (AudioFormat)var1.getFormat();
      if (var4 == null) {
         return -1;
      } else {
         long var2 = var4.computeDuration((long)var1.getLength());
         if (var2 == -1L) {
            return -1;
         } else if (var4.getSampleRate() != -1.0D) {
            return (int)((double)var2 * var4.getSampleRate() / 1.0E9D);
         } else {
            return var4.getFrameRate() != -1.0D ? (int)((double)var2 * var4.getFrameRate() / 1.0E9D) : -1;
         }
      }
   }

   private void init() {
      super.baseseq = TrueRandom.nextInt();
      super.maxseq = super.baseseq;
      super.lasttimestamp = TrueRandom.nextLong();
      super.sender = true;
      super.wassender = true;
      super.sinkstream = new RTPSinkStream();
      this.stats = new RTPTransStats();
   }

   public void close() {
      try {
         this.stop();
      } catch (IOException var2) {
      }

      this.getSSRCCache().field_173.removeSendStream(this);
   }

   public DataSource getDataSource() {
      return super.pds;
   }

   public long getNTPTimeStampLSW() {
      return super.lastSRntptimestamp;
   }

   public long getNTPTimeStampMSW() {
      return super.lastSRntptimestamp >> 32;
   }

   public Participant getParticipant() {
      SSRCCache var1 = this.getSSRCCache();
      return super.sourceInfo instanceof LocalParticipant && var1.field_173.IsNonParticipating() ? null : super.sourceInfo;
   }

   public long getRTPTimeStamp() {
      return super.lastSRrtptimestamp;
   }

   public long getSenderByteCount() {
      return super.lastSRoctetcount;
   }

   public Feedback getSenderFeedback() {
      SSRCCache var7 = this.getSSRCCache();

      boolean var10001;
      Vector var13;
      try {
         var13 = var7.field_173.getLocalParticipant().getReports();
      } catch (NullPointerException var12) {
         var10001 = false;
         return null;
      }

      int var1 = 0;

      while(true) {
         Vector var8;
         try {
            if (var1 >= var13.size()) {
               return null;
            }

            var8 = ((Report)var13.elementAt(var1)).getFeedbackReports();
         } catch (NullPointerException var11) {
            var10001 = false;
            return null;
         }

         int var2 = 0;

         while(true) {
            long var3;
            long var5;
            Feedback var9;
            try {
               if (var2 >= var8.size()) {
                  break;
               }

               var9 = (Feedback)var8.elementAt(var2);
               var3 = var9.getSSRC();
               var5 = this.getSSRC();
            } catch (NullPointerException var10) {
               var10001 = false;
               return null;
            }

            if (var3 == var5) {
               return var9;
            }

            ++var2;
         }

         ++var1;
      }
   }

   public long getSenderPacketCount() {
      return super.lastSRpacketcount;
   }

   public SenderReport getSenderReport() {
      SSRCCache var3 = this.getSSRCCache();

      boolean var10001;
      Vector var9;
      try {
         var9 = var3.field_173.getLocalParticipant().getReports();
      } catch (NullPointerException var8) {
         var10001 = false;
         return null;
      }

      int var1 = 0;

      while(true) {
         Report var4;
         Vector var5;
         try {
            if (var1 >= var9.size()) {
               return null;
            }

            var4 = (Report)var9.elementAt(var1);
            var5 = var4.getFeedbackReports();
         } catch (NullPointerException var7) {
            var10001 = false;
            return null;
         }

         int var2 = 0;

         while(true) {
            label36: {
               try {
                  if (var2 < var5.size()) {
                     if (((Feedback)var5.elementAt(var2)).getSSRC() != this.getSSRC()) {
                        break label36;
                     }

                     SenderReport var10 = (SenderReport)var4;
                     return var10;
                  }
               } catch (NullPointerException var6) {
                  var10001 = false;
                  return null;
               }

               ++var1;
               break;
            }

            ++var2;
         }
      }
   }

   public long getSequenceNumber(Buffer var1) {
      long var2 = var1.getSequenceNumber();
      long var4 = this.lastSeq;
      if (var4 == -1L) {
         var4 = (long)((double)System.currentTimeMillis() * Math.random());
         this.lastSeq = var4;
         this.lastBufSeq = var2;
         return var4;
      } else {
         long var6 = this.lastBufSeq;
         if (var2 - var6 > 1L) {
            this.lastSeq = var4 + (var2 - var6);
         } else {
            this.lastSeq = var4 + 1L;
         }

         this.lastBufSeq = var2;
         return this.lastSeq;
      }
   }

   public TransmissionStats getSourceTransmissionStats() {
      return this.stats;
   }

   public RTPStream getStream() {
      return this;
   }

   public long getTimeStamp(Buffer var1) {
      long var2 = var1.getTimeStamp();
      Format var4 = var1.getFormat();
      if ((var1.getFlags() & 4096) != 0 && var2 != -1L) {
         return var2;
      } else if (var4 instanceof AudioFormat) {
         if (mpegAudio.matches(var4)) {
            return var2 >= 0L ? 90L * var2 / 1000000L : System.currentTimeMillis() * 90L;
         } else {
            var2 = this.totalSamples + (long)this.calculateSampleCount(var1);
            this.totalSamples = var2;
            return var2;
         }
      } else if (var4 instanceof VideoFormat) {
         return var2 >= 0L ? 90L * var2 / 1000000L : System.currentTimeMillis() * 90L;
      } else {
         return var2;
      }
   }

   public int setBitRate(int var1) {
      if (super.sinkstream != null) {
         super.sinkstream.rate = var1;
      }

      return var1;
   }

   protected void setFormat(Format var1) {
      this.myformat = var1;
      if (super.sinkstream == null) {
         System.err.println("RTPSinkStream is NULL");
      } else {
         int var2 = 0;
         if (var1 instanceof AudioFormat) {
            if (!ulawAudio.matches(var1) && !dviAudio.matches(var1) && !mpegAudio.matches(var1)) {
               if (gsmAudio.matches(var1)) {
                  var2 = 13200;
               } else if (g723Audio.matches(var1)) {
                  var2 = 6300;
               }
            } else {
               var2 = (int)((AudioFormat)var1).getSampleRate() * ((AudioFormat)var1).getSampleSizeInBits();
            }

            super.sinkstream.rate = var2;
         }

      }
   }

   public void setSourceDescription(SourceDescription[] var1) {
      super.setSourceDescription(var1);
   }

   public void start() throws IOException {
      if (!this.inited) {
         this.inited = true;
         super.probation = 0;
         this.initsource(TrueRandom.nextInt());
         super.lasttimestamp = TrueRandom.nextLong();
      }

      if (super.pds != null) {
         super.pds.start();
      }

      if (super.sinkstream != null) {
         super.sinkstream.start();
      }

   }

   public void stop() throws IOException {
      if (super.pds != null) {
         super.pds.stop();
      }

      if (super.sinkstream != null) {
         super.sinkstream.stop();
      }

   }
}
