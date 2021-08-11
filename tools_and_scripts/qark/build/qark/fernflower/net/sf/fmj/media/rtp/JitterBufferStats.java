package net.sf.fmj.media.rtp;

import javax.media.Buffer;
import javax.media.control.JitterBufferControl;
import javax.media.protocol.PushBufferStream;
import net.sf.fmj.media.Log;
import net.sf.fmj.media.protocol.rtp.DataSource;
import org.atalk.android.util.java.awt.Component;

class JitterBufferStats implements JitterBufferControl {
   private int discardedFull;
   private int discardedLate;
   private int discardedReset;
   private int discardedShrink;
   private int discardedVeryLate;
   private int maxSizeReached;
   private int nbAdd;
   private int nbGrow;
   private int nbReset;
   private int sizePerPacket;
   private final RTPSourceStream stream;

   JitterBufferStats(RTPSourceStream var1) {
      this.stream = var1;
   }

   private void incrementRTPStatsPDUDrop() {
      DataSource var3 = this.stream.datasource;
      if (var3 != null) {
         PushBufferStream[] var4 = var3.getStreams();
         if (var4 != null) {
            int var2 = var4.length;

            for(int var1 = 0; var1 < var2; ++var1) {
               if (var4[var1] == this.stream) {
                  RTPSessionMgr var6 = var3.getMgr();
                  if (var6 != null) {
                     SSRCInfo var7 = var6.getSSRCInfo(var3.getSSRC());
                     if (var7 != null && var7.dsource == var3 && var7.dstream == this.stream) {
                        RTPStats var5 = var7.stats;
                        if (var5 != null) {
                           var5.update(8);
                        }
                     }

                     return;
                  }
                  break;
               }
            }
         }
      }

   }

   public int getAbsoluteMaximumDelay() {
      return this.stream.getBehaviour().getAbsoluteMaximumDelay();
   }

   public Component getControlComponent() {
      return null;
   }

   public int getCurrentDelayMs() {
      return this.getCurrentDelayPackets() * 20;
   }

   public int getCurrentDelayPackets() {
      return this.getCurrentSizePackets() / 2;
   }

   public int getCurrentPacketCount() {
      return this.stream.field_55.getFillCount();
   }

   public int getCurrentSizePackets() {
      return this.stream.field_55.getCapacity();
   }

   public int getDiscarded() {
      return this.getDiscardedFull() + this.getDiscardedLate() + this.getDiscardedReset() + this.getDiscardedShrink() + this.getDiscardedVeryLate();
   }

   public int getDiscardedFull() {
      return this.discardedFull;
   }

   public int getDiscardedLate() {
      return this.discardedLate;
   }

   public int getDiscardedReset() {
      return this.discardedReset;
   }

   public int getDiscardedShrink() {
      return this.discardedShrink;
   }

   public int getDiscardedVeryLate() {
      return this.discardedVeryLate;
   }

   public int getMaxSizeReached() {
      return this.maxSizeReached;
   }

   public int getMaximumDelay() {
      return this.stream.getBehaviour().getMaximumDelay();
   }

   int getNbAdd() {
      return this.nbAdd;
   }

   public int getNominalDelay() {
      return this.stream.getBehaviour().getNominalDelay();
   }

   int getSizePerPacket() {
      return this.sizePerPacket;
   }

   void incrementDiscardedFull() {
      ++this.discardedFull;
      this.incrementRTPStatsPDUDrop();
   }

   void incrementDiscardedLate() {
      ++this.discardedLate;
      this.incrementRTPStatsPDUDrop();
   }

   void incrementDiscardedReset() {
      ++this.discardedReset;
      this.incrementRTPStatsPDUDrop();
   }

   void incrementDiscardedShrink() {
      ++this.discardedShrink;
      this.incrementRTPStatsPDUDrop();
   }

   void incrementDiscardedVeryLate() {
      ++this.discardedVeryLate;
      this.incrementRTPStatsPDUDrop();
   }

   void incrementNbAdd() {
      ++this.nbAdd;
   }

   void incrementNbGrow() {
      ++this.nbGrow;
   }

   void incrementNbReset() {
      ++this.nbReset;
   }

   public boolean isAdaptiveBufferEnabled() {
      return this.stream.getBehaviour().isAdaptive();
   }

   void printStats() {
      StringBuilder var1 = new StringBuilder();
      var1.append(RTPSourceStream.class.getName());
      var1.append(" ");
      String var3 = var1.toString();
      StringBuilder var2 = new StringBuilder();
      var2.append(var3);
      var2.append("Total packets added: ");
      var2.append(this.getNbAdd());
      Log.info(var2.toString());
      var2 = new StringBuilder();
      var2.append(var3);
      var2.append("Times reset() called: ");
      var2.append(this.nbReset);
      Log.info(var2.toString());
      var2 = new StringBuilder();
      var2.append(var3);
      var2.append("Times grow() called: ");
      var2.append(this.nbGrow);
      Log.info(var2.toString());
      var2 = new StringBuilder();
      var2.append(var3);
      var2.append("Packets dropped because full: ");
      var2.append(this.getDiscardedFull());
      Log.info(var2.toString());
      var2 = new StringBuilder();
      var2.append(var3);
      var2.append("Packets dropped while shrinking: ");
      var2.append(this.getDiscardedShrink());
      Log.info(var2.toString());
      var2 = new StringBuilder();
      var2.append(var3);
      var2.append("Packets dropped because they were late: ");
      var2.append(this.getDiscardedLate());
      Log.info(var2.toString());
      var2 = new StringBuilder();
      var2.append(var3);
      var2.append("Packets dropped because they were late by more than MAX_SIZE: ");
      var2.append(this.getDiscardedVeryLate());
      Log.info(var2.toString());
      var2 = new StringBuilder();
      var2.append(var3);
      var2.append("Packets dropped in reset(): ");
      var2.append(this.getDiscardedReset());
      Log.info(var2.toString());
      var2 = new StringBuilder();
      var2.append(var3);
      var2.append("Max size reached: ");
      var2.append(this.getMaxSizeReached());
      Log.info(var2.toString());
      var2 = new StringBuilder();
      var2.append(var3);
      var2.append("Adaptive jitter buffer mode was ");
      if (this.isAdaptiveBufferEnabled()) {
         var3 = "enabled";
      } else {
         var3 = "disabled";
      }

      var2.append(var3);
      Log.info(var2.toString());
   }

   void updateMaxSizeReached() {
      int var1 = this.getCurrentSizePackets();
      if (this.maxSizeReached < var1) {
         this.maxSizeReached = var1;
      }

   }

   void updateSizePerPacket(Buffer var1) {
      int var2 = var1.getLength();
      int var3 = this.sizePerPacket;
      if (var3 != 0) {
         var2 = (var3 + var2) / 2;
      }

      this.sizePerPacket = var2;
   }
}
