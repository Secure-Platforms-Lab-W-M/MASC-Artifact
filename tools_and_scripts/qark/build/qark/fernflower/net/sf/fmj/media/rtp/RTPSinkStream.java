package net.sf.fmj.media.rtp;

import javax.media.Buffer;
import javax.media.format.AudioFormat;
import javax.media.format.VideoFormat;
import javax.media.protocol.BufferTransferHandler;
import javax.media.protocol.PushBufferStream;
import net.sf.fmj.media.rtp.util.RTPMediaThread;

public class RTPSinkStream implements BufferTransferHandler {
   static int LEEWAY = 5;
   static int THRESHOLD = 80;
   static AudioFormat mpegAudio = new AudioFormat("mpegaudio/rtp");
   static VideoFormat mpegVideo = new VideoFormat("mpeg/rtp");
   long audioPT = 0L;
   boolean bufSizeSet = false;
   Buffer current = new Buffer();
   SendSSRCInfo info = null;
   boolean mpegBFrame = false;
   boolean mpegPFrame = false;
   int rate;
   RTPRawSender sender = null;
   long startPT = -1L;
   Object startReq = new Integer(0);
   long startTime = 0L;
   boolean started = false;
   private RTPMediaThread thread = null;
   RTPTransmitter transmitter = null;

   private void transmitAudio() {
      if (!this.current.isEOM() && !this.current.isDiscard()) {
         if (this.startPT == -1L) {
            this.startTime = System.currentTimeMillis();
            long var3 = this.current.getTimeStamp();
            long var1 = 0L;
            if (var3 > 0L) {
               var1 = this.current.getTimeStamp() / 1000000L;
            }

            this.startPT = var1;
            this.audioPT = var1;
         }

         if ((this.current.getFlags() & 96) == 0) {
            if (mpegAudio.matches(this.current.getFormat())) {
               this.audioPT = this.current.getTimeStamp() / 1000000L;
            } else {
               this.audioPT += ((AudioFormat)this.info.myformat).computeDuration((long)this.current.getLength()) / 1000000L;
            }

            this.waitForPT(this.startTime, this.startPT, this.audioPT);
         }

         this.transmitter.TransmitPacket(this.current, this.info);
      } else {
         this.startPT = -1L;
      }
   }

   private void transmitVideo() {
      if (!this.current.isEOM() && !this.current.isDiscard()) {
         if (this.startPT == -1L) {
            this.startTime = System.currentTimeMillis();
            this.startPT = this.current.getTimeStamp() / 1000000L;
         }

         if (this.current.getTimeStamp() > 0L && (this.current.getFlags() & 96) == 0 && (this.current.getFlags() & 2048) != 0) {
            if (mpegVideo.matches(this.info.myformat)) {
               int var1 = ((byte[])((byte[])this.current.getData()))[this.current.getOffset() + 2] & 7;
               if (var1 > 2) {
                  this.mpegBFrame = true;
               } else if (var1 == 2) {
                  this.mpegPFrame = true;
               }

               if (var1 > 2 || var1 == 2 && !this.mpegBFrame || var1 == 1 && !(this.mpegBFrame | this.mpegPFrame)) {
                  this.waitForPT(this.startTime, this.startPT, this.current.getTimeStamp() / 1000000L);
               }
            } else {
               this.waitForPT(this.startTime, this.startPT, this.current.getTimeStamp() / 1000000L);
            }
         }

         this.transmitter.TransmitPacket(this.current, this.info);
      } else {
         this.startPT = -1L;
         this.mpegBFrame = false;
         this.mpegPFrame = false;
      }
   }

   private void waitForPT(long var1, long var3, long var5) {
      for(long var8 = var5 - var3 - (System.currentTimeMillis() - var1); var8 > (long)LEEWAY; var8 = var5 - var3 - (System.currentTimeMillis() - var1)) {
         int var7 = THRESHOLD;
         long var10 = var8;
         if (var8 > (long)var7) {
            var10 = (long)var7;
         }

         try {
            Thread.currentThread();
            Thread.sleep(var10);
         } catch (Exception var13) {
            break;
         }
      }

   }

   protected void close() {
      this.stop();
   }

   protected void setSSRCInfo(SendSSRCInfo var1) {
      this.info = var1;
   }

   protected void setTransmitter(RTPTransmitter var1) {
      this.transmitter = var1;
      if (var1 != null) {
         this.sender = var1.getSender();
      }

   }

   public void start() {
      // $FF: Couldn't be decompiled
   }

   public void startStream() {
   }

   public void stop() {
      // $FF: Couldn't be decompiled
   }

   public void transferData(PushBufferStream param1) {
      // $FF: Couldn't be decompiled
   }
}
