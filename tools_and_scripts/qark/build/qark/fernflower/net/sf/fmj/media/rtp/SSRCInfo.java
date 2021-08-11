package net.sf.fmj.media.rtp;

import java.net.InetAddress;
import java.util.Enumeration;
import java.util.NoSuchElementException;
import java.util.Vector;
import javax.media.Format;
import javax.media.rtp.LocalParticipant;
import javax.media.rtp.Participant;
import javax.media.rtp.rtcp.Report;
import javax.media.rtp.rtcp.SourceDescription;
import net.sf.fmj.media.protocol.rtp.DataSource;
import net.sf.fmj.media.rtp.util.SSRCTable;

public abstract class SSRCInfo implements Report {
   static final int INITIALPROBATION = 2;
   static final int PAYLOAD_UNASSIGNED = -1;
   boolean active;
   InetAddress address;
   boolean aging;
   boolean alive = false;
   public int baseseq;
   boolean byeReceived = false;
   long byeTime = 0L;
   String byereason = null;
   public int bytesreceived;
   private SSRCCache cache;
   int clockrate;
   Format currentformat;
   public int cycles;
   DataSource dsource;
   RTPSourceStream dstream;
   public SourceDescription email = null;
   boolean inactivesent;
   public double jitter;
   long lastHeardFrom;
   int lastPayloadType = -1;
   long lastRTCPreceiptTime;
   long lastRTPReceiptTime;
   public long lastSRntptimestamp;
   long lastSRoctetcount;
   long lastSRpacketcount;
   public long lastSRreceiptTime;
   long lastSRrtptimestamp;
   int lastbadseq;
   boolean lastsr;
   long lasttimestamp;
   public SourceDescription loc = null;
   public int maxseq;
   public SourceDescription name = null;
   boolean newpartsent;
   boolean newrecvstream;
   public SourceDescription note = null;
   public boolean ours;
   int payloadType;
   boolean payloadchange = false;
   javax.media.protocol.DataSource pds;
   public SourceDescription phone = null;
   int port;
   public int prevlost;
   public int prevmaxseq;
   SourceDescription priv = null;
   int probation;
   boolean quiet;
   public int received;
   boolean recvstrmap;
   RTCPReporter reporter;
   SSRCTable reports;
   public long rtptime;
   public boolean sender;
   RTPSinkStream sinkstream;
   public RTPSourceInfo sourceInfo = null;
   public int ssrc;
   long starttime;
   RTPStats stats;
   boolean streamconnect;
   public long systime;
   public SourceDescription tool = null;
   boolean wassender;
   boolean wrapped;

   SSRCInfo(SSRCCache var1, int var2) {
      this.lastSRntptimestamp = 0L;
      this.lastSRrtptimestamp = 0L;
      this.lastSRoctetcount = 0L;
      this.lastSRpacketcount = 0L;
      this.lastRTCPreceiptTime = 0L;
      this.lastSRreceiptTime = 0L;
      this.lastHeardFrom = 0L;
      this.quiet = false;
      this.inactivesent = false;
      this.sender = false;
      this.ours = false;
      this.streamconnect = false;
      this.reports = new SSRCTable();
      this.active = false;
      this.newrecvstream = false;
      this.recvstrmap = false;
      this.newpartsent = false;
      this.lastsr = false;
      this.wrapped = false;
      this.probation = 2;
      this.wassender = false;
      this.currentformat = null;
      this.payloadType = -1;
      this.dsource = null;
      this.pds = null;
      this.dstream = null;
      this.sinkstream = null;
      this.maxseq = 0;
      this.cycles = 0;
      this.lasttimestamp = 0L;
      this.jitter = 0.0D;
      this.clockrate = 0;
      this.cache = var1;
      this.ssrc = var2;
      this.stats = new RTPStats();
   }

   SSRCInfo(SSRCInfo var1) {
      this.lastSRoctetcount = 0L;
      this.lastSRpacketcount = 0L;
      this.lastSRreceiptTime = 0L;
      this.lastHeardFrom = 0L;
      this.quiet = false;
      this.inactivesent = false;
      this.sender = false;
      this.ours = false;
      this.streamconnect = false;
      this.reports = new SSRCTable();
      this.active = false;
      this.newrecvstream = false;
      this.recvstrmap = false;
      this.newpartsent = false;
      this.lastsr = false;
      this.wrapped = false;
      this.probation = 2;
      this.wassender = false;
      this.currentformat = null;
      this.payloadType = -1;
      this.pds = null;
      this.sinkstream = null;
      this.maxseq = 0;
      this.cycles = 0;
      this.lasttimestamp = 0L;
      this.jitter = 0.0D;
      this.clockrate = 0;
      this.cache = var1.cache;
      this.alive = var1.alive;
      RTPSourceInfo var2 = var1.sourceInfo;
      this.sourceInfo = var2;
      if (var2 != null) {
         var2.addSSRC(this);
      }

      this.cache.remove(var1.ssrc);
      this.name = var1.name;
      this.email = var1.email;
      this.phone = var1.phone;
      this.loc = var1.loc;
      this.tool = var1.tool;
      this.note = var1.note;
      this.priv = var1.priv;
      this.lastSRntptimestamp = var1.lastSRntptimestamp;
      this.lastSRrtptimestamp = var1.lastSRrtptimestamp;
      this.lastSRoctetcount = var1.lastSRoctetcount;
      this.lastSRpacketcount = var1.lastSRpacketcount;
      this.lastRTCPreceiptTime = var1.lastRTCPreceiptTime;
      this.lastSRreceiptTime = var1.lastSRreceiptTime;
      this.lastHeardFrom = var1.lastHeardFrom;
      this.quiet = var1.quiet;
      this.inactivesent = var1.inactivesent;
      this.aging = var1.aging;
      this.reports = var1.reports;
      this.ours = var1.ours;
      this.ssrc = var1.ssrc;
      this.streamconnect = var1.streamconnect;
      this.newrecvstream = var1.newrecvstream;
      this.recvstrmap = var1.recvstrmap;
      this.newpartsent = var1.newpartsent;
      this.lastsr = var1.lastsr;
      this.probation = var1.probation;
      this.wassender = var1.wassender;
      this.prevmaxseq = var1.prevmaxseq;
      this.prevlost = var1.prevlost;
      this.starttime = var1.starttime;
      RTCPReporter var3 = var1.reporter;
      this.reporter = var3;
      if (var1.reporter != null) {
         var3.transmit.setSSRCInfo(this);
      }

      this.payloadType = var1.payloadType;
      this.dsource = var1.dsource;
      this.pds = var1.pds;
      this.dstream = var1.dstream;
      this.lastRTPReceiptTime = var1.lastRTPReceiptTime;
      this.maxseq = var1.maxseq;
      this.cycles = var1.cycles;
      this.baseseq = var1.baseseq;
      this.lastbadseq = var1.lastbadseq;
      this.received = var1.received;
      this.lasttimestamp = var1.lasttimestamp;
      this.lastPayloadType = var1.lastPayloadType;
      this.jitter = var1.jitter;
      this.bytesreceived = var1.bytesreceived;
      this.address = var1.address;
      this.port = var1.port;
      this.stats = var1.stats;
      this.clockrate = var1.clockrate;
      this.byeTime = var1.byeTime;
      this.byeReceived = var1.byeReceived;
   }

   private void InitSDES() {
      this.name = new SourceDescription(2, (String)null, 0, false);
      this.email = new SourceDescription(3, (String)null, 0, false);
      this.phone = new SourceDescription(4, (String)null, 0, false);
      this.loc = new SourceDescription(5, (String)null, 0, false);
      this.tool = new SourceDescription(6, (String)null, 0, false);
      this.note = new SourceDescription(7, (String)null, 0, false);
      this.priv = new SourceDescription(8, (String)null, 0, false);
   }

   void addSDESInfo(RTCPSDES var1) {
      int var2;
      for(var2 = 0; var2 < var1.items.length && var1.items[var2].type != 1; ++var2) {
      }

      String var4 = new String(var1.items[var2].data);
      String var3 = null;
      RTPSourceInfo var5 = this.sourceInfo;
      if (var5 != null) {
         var3 = var5.getCNAME();
      }

      if (this.sourceInfo != null && !var4.equals(var3)) {
         this.sourceInfo.removeSSRC(this);
         this.sourceInfo = null;
      }

      if (this.sourceInfo == null) {
         RTPSourceInfo var6 = this.cache.sourceInfoCache.get(var4, this.ours);
         this.sourceInfo = var6;
         var6.addSSRC(this);
      }

      if (var1.items.length > 1) {
         for(var2 = 0; var2 < var1.items.length; ++var2) {
            var3 = new String(var1.items[var2].data);
            SourceDescription var7;
            switch(var1.items[var2].type) {
            case 2:
               var7 = this.name;
               if (var7 == null) {
                  this.name = new SourceDescription(2, var3, 0, false);
               } else {
                  var7.setDescription(var3);
               }
               break;
            case 3:
               var7 = this.email;
               if (var7 == null) {
                  this.email = new SourceDescription(3, var3, 0, false);
               } else {
                  var7.setDescription(var3);
               }
               break;
            case 4:
               var7 = this.phone;
               if (var7 == null) {
                  this.phone = new SourceDescription(4, var3, 0, false);
               } else {
                  var7.setDescription(var3);
               }
               break;
            case 5:
               var7 = this.loc;
               if (var7 == null) {
                  this.loc = new SourceDescription(5, var3, 0, false);
               } else {
                  var7.setDescription(var3);
               }
               break;
            case 6:
               var7 = this.tool;
               if (var7 == null) {
                  this.tool = new SourceDescription(6, var3, 0, false);
               } else {
                  var7.setDescription(var3);
               }
               break;
            case 7:
               var7 = this.note;
               if (var7 == null) {
                  this.note = new SourceDescription(7, var3, 0, false);
               } else {
                  var7.setDescription(var3);
               }
               break;
            case 8:
               var7 = this.priv;
               if (var7 == null) {
                  this.priv = new SourceDescription(8, var3, 0, false);
               } else {
                  var7.setDescription(var3);
               }
            }
         }
      }

   }

   void delete() {
      RTPSourceInfo var1 = this.sourceInfo;
      if (var1 != null) {
         var1.removeSSRC(this);
      }

   }

   public int extendSequenceNumber(int var1) {
      int var3 = this.cycles;
      int var4 = var1 - this.maxseq;
      int var2;
      if (var4 >= 0) {
         var2 = var3;
         if (var4 > 32767) {
            var2 = var3 - 65536;
         }
      } else {
         var2 = var3;
         if (var4 < -32767) {
            var2 = var3 + 65536;
         }
      }

      return var1 + var2;
   }

   public String getCNAME() {
      RTPSourceInfo var1 = this.sourceInfo;
      return var1 != null ? var1.getCNAME() : null;
   }

   public long getExpectedPacketCount() {
      return ((long)this.maxseq & 65535L) + (long)this.cycles - (65535L & (long)this.baseseq) + 1L;
   }

   public Vector getFeedbackReports() {
      Vector var1 = new Vector(this.reports.size());
      if (this.reports.size() == 0) {
         return var1;
      } else {
         Enumeration var2 = this.reports.elements();

         while(true) {
            try {
               if (!var2.hasMoreElements()) {
                  break;
               }

               var1.addElement(((RTCPReportBlock[])var2.nextElement())[0]);
            } catch (NoSuchElementException var3) {
               System.err.println("No more elements");
               break;
            }
         }

         var1.trimToSize();
         return var1;
      }
   }

   public Participant getParticipant() {
      return this.sourceInfo instanceof LocalParticipant && this.cache.field_173.IsNonParticipating() ? null : this.sourceInfo;
   }

   int getPayloadType() {
      return this.payloadType;
   }

   RTPSourceInfo getRTPSourceInfo() {
      return this.sourceInfo;
   }

   public long getSSRC() {
      return (long)this.ssrc;
   }

   public SSRCCache getSSRCCache() {
      return this.cache;
   }

   public Vector getSourceDescription() {
      Vector var1 = new Vector();
      var1.addElement(this.sourceInfo.getCNAMESDES());
      SourceDescription var2 = this.name;
      if (var2 != null) {
         var1.addElement(var2);
      }

      var2 = this.email;
      if (var2 != null) {
         var1.addElement(var2);
      }

      var2 = this.phone;
      if (var2 != null) {
         var1.addElement(var2);
      }

      var2 = this.loc;
      if (var2 != null) {
         var1.addElement(var2);
      }

      var2 = this.tool;
      if (var2 != null) {
         var1.addElement(var2);
      }

      var2 = this.note;
      if (var2 != null) {
         var1.addElement(var2);
      }

      var2 = this.priv;
      if (var2 != null) {
         var1.addElement(var2);
      }

      var1.trimToSize();
      return var1;
   }

   void initsource(int var1) {
      if (this.probation <= 0) {
         this.active = true;
         this.setSender(true);
      }

      this.baseseq = var1--;
      this.maxseq = var1;
      this.lastbadseq = -2;
      this.cycles = 0;
      this.received = 0;
      this.bytesreceived = 0;
      this.lastRTPReceiptTime = 0L;
      this.lasttimestamp = 0L;
      this.jitter = 0.0D;
      this.prevmaxseq = var1;
      this.prevlost = 0;
   }

   boolean isActive() {
      return this.active;
   }

   public RTCPReportBlock makeReceiverReport(long var1) {
      RTCPReportBlock var7 = new RTCPReportBlock();
      var7.ssrc = this.ssrc;
      var7.lastseq = (long)(this.maxseq + this.cycles);
      var7.jitter = (int)this.jitter;
      var7.lsr = (long)((int)((this.lastSRntptimestamp & 281474976645120L) >> 16));
      var7.dlsr = (long)((int)((double)(var1 - this.lastSRreceiptTime) * 65.536D));
      var7.packetslost = (int)(var7.lastseq - (long)this.baseseq + 1L - (long)this.received);
      if (var7.packetslost < 0) {
         var7.packetslost = 0;
      }

      double var5 = (double)(var7.packetslost - this.prevlost) / (double)(var7.lastseq - (long)this.prevmaxseq);
      double var3 = var5;
      if (var5 < 0.0D) {
         var3 = 0.0D;
      }

      var7.fractionlost = (int)(256.0D * var3);
      this.prevmaxseq = (int)var7.lastseq;
      this.prevlost = var7.packetslost;
      return var7;
   }

   void setAging(boolean var1) {
      if (this.aging != var1) {
         this.aging = var1;
      }

   }

   void setAlive(boolean var1) {
      this.setAging(false);
      if (this.alive != var1) {
         if (var1) {
            this.reports.removeAll();
         } else {
            this.setSender(false);
         }

         this.alive = var1;
      }
   }

   public void setOurs(boolean var1) {
      if (this.ours != var1) {
         if (var1) {
            this.setAlive(true);
         } else {
            this.setAlive(false);
         }

         this.ours = var1;
      }
   }

   void setSender(boolean var1) {
      if (this.sender != var1) {
         SSRCCache var2;
         if (var1) {
            var2 = this.cache;
            ++var2.sendercount;
            this.setAlive(true);
         } else {
            var2 = this.cache;
            --var2.sendercount;
         }

         this.sender = var1;
      }
   }

   void setSourceDescription(SourceDescription[] var1) {
      if (var1 != null) {
         String var5 = null;
         int var3 = var1.length;
         int var2 = 0;

         String var4;
         SourceDescription var7;
         while(true) {
            var4 = var5;
            if (var2 >= var3) {
               break;
            }

            var7 = var1[var2];
            if (var7 != null && var7.getType() == 1) {
               var4 = var7.getDescription();
               break;
            }

            ++var2;
         }

         var5 = null;
         RTPSourceInfo var6 = this.sourceInfo;
         if (var6 != null) {
            var5 = var6.getCNAME();
         }

         if (this.sourceInfo != null && var4 != null && !var4.equals(var5)) {
            this.sourceInfo.removeSSRC(this);
            this.sourceInfo = null;
         }

         if (this.sourceInfo == null) {
            RTPSourceInfo var8 = this.cache.sourceInfoCache.get(var4, true);
            this.sourceInfo = var8;
            var8.addSSRC(this);
         }

         var3 = var1.length;

         for(var2 = 0; var2 < var3; ++var2) {
            var7 = var1[var2];
            if (var7 != null) {
               SourceDescription var9;
               switch(var7.getType()) {
               case 2:
                  var9 = this.name;
                  if (var9 == null) {
                     this.name = new SourceDescription(2, var7.getDescription(), 0, false);
                  } else {
                     var9.setDescription(var7.getDescription());
                  }
                  break;
               case 3:
                  var9 = this.email;
                  if (var9 == null) {
                     this.email = new SourceDescription(3, var7.getDescription(), 0, false);
                  } else {
                     var9.setDescription(var7.getDescription());
                  }
                  break;
               case 4:
                  var9 = this.phone;
                  if (var9 == null) {
                     this.phone = new SourceDescription(4, var7.getDescription(), 0, false);
                  } else {
                     var9.setDescription(var7.getDescription());
                  }
                  break;
               case 5:
                  var9 = this.loc;
                  if (var9 == null) {
                     this.loc = new SourceDescription(5, var7.getDescription(), 0, false);
                  } else {
                     var9.setDescription(var7.getDescription());
                  }
                  break;
               case 6:
                  var9 = this.tool;
                  if (var9 == null) {
                     this.tool = new SourceDescription(6, var7.getDescription(), 0, false);
                  } else {
                     var9.setDescription(var7.getDescription());
                  }
                  break;
               case 7:
                  var9 = this.note;
                  if (var9 == null) {
                     this.note = new SourceDescription(7, var7.getDescription(), 0, false);
                  } else {
                     var9.setDescription(var7.getDescription());
                  }
                  break;
               case 8:
                  var9 = this.priv;
                  if (var9 == null) {
                     this.priv = new SourceDescription(8, var7.getDescription(), 0, false);
                  } else {
                     var9.setDescription(var7.getDescription());
                  }
               }
            }
         }

      }
   }
}
