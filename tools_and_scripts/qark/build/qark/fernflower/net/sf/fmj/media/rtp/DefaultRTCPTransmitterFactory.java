package net.sf.fmj.media.rtp;

public class DefaultRTCPTransmitterFactory implements RTCPTransmitterFactory {
   public RTCPTransmitter newRTCPTransmitter(SSRCCache var1, RTCPRawSender var2) {
      return new DefaultRTCPTransmitterImpl(var1, var2);
   }
}
