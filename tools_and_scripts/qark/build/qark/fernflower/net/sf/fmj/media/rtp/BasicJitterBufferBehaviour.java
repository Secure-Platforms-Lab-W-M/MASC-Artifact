package net.sf.fmj.media.rtp;

import javax.media.Buffer;
import javax.media.control.BufferControl;
import net.sf.fmj.media.Log;

class BasicJitterBufferBehaviour implements JitterBufferBehaviour {
   // $FF: renamed from: q net.sf.fmj.media.rtp.JitterBuffer
   protected final JitterBuffer field_66;
   private int recvBufSize;
   protected final JitterBufferStats stats;
   protected final RTPSourceStream stream;

   protected BasicJitterBufferBehaviour(RTPSourceStream var1) {
      this.stream = var1;
      this.field_66 = var1.field_55;
      this.stats = this.stream.stats;
   }

   protected void dropFirstPkt() {
      this.field_66.dropFirstFill();
   }

   public void dropPkt() {
      this.dropFirstPkt();
   }

   public int getAbsoluteMaximumDelay() {
      return this.getMaximumDelay();
   }

   protected BufferControl getBufferControl() {
      return this.stream.getBufferControl();
   }

   public int getMaximumDelay() {
      return 65535;
   }

   public int getNominalDelay() {
      return 0;
   }

   protected void grow(int var1) {
      if (var1 >= 1) {
         int var2 = this.field_66.getCapacity();
         if (var1 != var2) {
            if (var1 >= var2) {
               StringBuilder var3 = new StringBuilder();
               var3.append("Growing packet queue to ");
               var3.append(var1);
               Log.info(var3.toString());
               this.stats.incrementNbGrow();
               this.field_66.setCapacity(var1);
            } else {
               throw new IllegalArgumentException("capacity");
            }
         }
      } else {
         throw new IllegalArgumentException("capacity");
      }
   }

   public boolean isAdaptive() {
      return false;
   }

   protected int monitorQSize(Buffer var1) {
      return 0;
   }

   public boolean preAdd(Buffer var1, RTPRawReceiver var2) {
      this.stats.updateSizePerPacket(var1);
      int var3 = this.monitorQSize(var1);
      if (var3 > 0) {
         this.setRecvBufSize(var2, var3);
      }

      return true;
   }

   public void read(Buffer var1) {
      if (this.field_66.getFillCount() == 0) {
         var1.setDiscard(true);
      } else {
         Buffer var2 = this.field_66.getFill();

         try {
            Object var3 = var1.getData();
            Object var4 = var1.getHeader();
            var1.copy(var2);
            var2.setData(var3);
            var2.setHeader(var4);
         } finally {
            this.field_66.returnFree(var2);
         }

      }
   }

   public void reset() {
   }

   protected void setRecvBufSize(RTPRawReceiver var1, int var2) {
      var2 = var2 * this.stats.getSizePerPacket() / 2;
      if (var1 != null && var2 > this.recvBufSize) {
         var1.setRecvBufSize(var2);
         int var3 = var1.getRecvBufSize();
         if (var3 < var2) {
            var2 = Integer.MAX_VALUE;
         }

         this.recvBufSize = var2;
         StringBuilder var4 = new StringBuilder();
         var4.append("RTP socket receive buffer size: ");
         var4.append(var3);
         var4.append(" bytes.\n");
         Log.comment(var4.toString());
      }

   }

   public boolean willReadBlock() {
      return this.field_66.noMoreFill();
   }
}
