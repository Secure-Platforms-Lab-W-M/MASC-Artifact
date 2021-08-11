package net.sf.fmj.media.rtp;

import com.sun.media.util.Registry;
import javax.media.Buffer;
import javax.media.Format;
import javax.media.control.BufferControl;
import javax.media.format.VideoFormat;
import net.sf.fmj.media.Log;

class VideoJitterBufferBehaviour extends BasicJitterBufferBehaviour {
   private static final int BUF_CHECK_INTERVAL = 7000;
   private static final int DEFAULT_PKTS_TO_BUFFER = 90;
   private static final int DEFAULT_VIDEO_RATE = 15;
   private static final int FUDGE = 5;
   private static final VideoFormat H264 = new VideoFormat("h264/rtp");
   private static final int MIN_BUF_CHECK = 10000;
   private static final VideoFormat MPEG = new VideoFormat("mpeg/rtp");
   private final int MIN_SIZE = Registry.getInt("video_jitter_buffer_MIN_SIZE", 4);
   private int fps = 15;
   private int framesEst = 0;
   private long lastCheckTime = 0L;
   private long lastPktSeq = 0L;
   private int maxPktsToBuffer = 0;
   private int pktsEst;
   private int pktsPerFrame = 15;
   private int tooMuchBufferingCount = 0;

   public VideoJitterBufferBehaviour(RTPSourceStream var1) {
      super(var1);
      int var2 = this.q.getCapacity();
      int var3 = this.MIN_SIZE;
      if (var2 < var3) {
         this.grow(var3);
      }

   }

   private void cutByHalf() {
      int var1 = this.q.getCapacity() / 2;
      if (var1 > 0) {
         this.q.setCapacity(var1);
      }

   }

   private void dropMpegPkt() {
      int var1 = 0;
      int var2 = -1;
      byte var4 = -1;
      int var5 = this.q.getFillCount();

      int var3;
      while(true) {
         var3 = var4;
         if (var1 >= var5) {
            break;
         }

         Buffer var7 = this.q.getFill(var1);
         int var6 = ((byte[])((byte[])var7.getData()))[var7.getOffset() + 2] & 7;
         if (var6 > 2) {
            var3 = var1;
            break;
         }

         var3 = var2;
         if (var6 == 2) {
            var3 = var2;
            if (var2 == -1) {
               var3 = var1;
            }
         }

         ++var1;
         var2 = var3;
      }

      if (var3 == -1) {
         if (var2 != -1) {
            var1 = var2;
         } else {
            var1 = 0;
         }
      }

      this.q.dropFill(var1);
   }

   protected void dropFirstPkt() {
      if (MPEG.matches(this.stream.getFormat())) {
         this.dropMpegPkt();
      } else {
         super.dropFirstPkt();
      }
   }

   public boolean isAdaptive() {
      return true;
   }

   protected int monitorQSize(Buffer var1) {
      super.monitorQSize(var1);
      if (this.lastPktSeq + 1L == var1.getSequenceNumber()) {
         ++this.pktsEst;
      } else {
         this.pktsEst = 1;
      }

      this.lastPktSeq = var1.getSequenceNumber();
      Format var7 = this.stream.getFormat();
      if (MPEG.matches(var7)) {
         if ((((byte[])((byte[])var1.getData()))[var1.getOffset() + 2] & 7) < 3 && (var1.getFlags() & 2048) != 0) {
            this.pktsPerFrame = (this.pktsPerFrame + this.pktsEst) / 2;
            this.pktsEst = 0;
         }

         this.fps = 30;
      } else if (H264.matches(var7)) {
         this.pktsPerFrame = 300;
         this.fps = 15;
      }

      int var2;
      if ((var1.getFlags() & 2048) != 0) {
         this.pktsPerFrame = (this.pktsPerFrame + this.pktsEst) / 2;
         this.pktsEst = 0;
         ++this.framesEst;
         long var5 = System.currentTimeMillis();
         if (var5 - this.lastCheckTime >= 1000L) {
            this.lastCheckTime = var5;
            var2 = (this.fps + this.framesEst) / 2;
            this.fps = var2;
            this.framesEst = 0;
            if (var2 > 30) {
               this.fps = 30;
            }
         }
      }

      BufferControl var8 = this.getBufferControl();
      int var3;
      if (var8 != null) {
         var3 = (int)(var8.getBufferLength() * (long)this.fps / 1000L);
         var2 = var3;
         if (var3 <= 0) {
            var2 = 1;
         }

         var2 = this.pktsPerFrame * var2;
      } else {
         var2 = 90;
      }

      if (H264.matches(var7)) {
         this.maxPktsToBuffer = 200;
      } else {
         var3 = this.maxPktsToBuffer;
         if (var3 > 0) {
            this.maxPktsToBuffer = (var3 + var2) / 2;
         } else {
            this.maxPktsToBuffer = var2;
         }
      }

      var3 = this.q.getCapacity();
      int var4 = this.q.getFillCount();
      if (var3 > 10000 && var4 < var3 / 4) {
         var3 = this.tooMuchBufferingCount++;
         if (var3 > this.pktsPerFrame * this.fps * 7000) {
            this.cutByHalf();
            this.tooMuchBufferingCount = 0;
            return var2;
         }
      } else {
         if (var4 >= var3 / 2) {
            var4 = this.maxPktsToBuffer;
            if (var3 < var4) {
               var2 = var3 / 2 + var3;
               if (var2 > var4) {
                  var2 = this.maxPktsToBuffer;
               }

               this.q.setCapacity(var2 + 5);
               var3 = this.q.getCapacity();
               StringBuilder var9 = new StringBuilder();
               var9.append("RTP video buffer size: ");
               var9.append(var3);
               var9.append(" pkts, ");
               var9.append(this.stats.getSizePerPacket() * var2);
               var9.append(" bytes.\n");
               Log.comment(var9.toString());
               this.tooMuchBufferingCount = 0;
               return var2;
            }
         }

         this.tooMuchBufferingCount = 0;
      }

      return var2;
   }

   public void reset() {
      super.reset();
      this.tooMuchBufferingCount = 0;
   }
}
