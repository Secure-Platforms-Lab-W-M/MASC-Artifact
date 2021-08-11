package net.sf.fmj.media.rtp;

class SynchSource {
   double factor;
   long ntpTimestamp;
   long rtpTimestamp;
   int ssrc;

   public SynchSource(int var1, long var2, long var4) {
      this.ssrc = var1;
      this.rtpTimestamp = var2;
      this.ntpTimestamp = var4;
      this.factor = 0.0D;
   }
}
