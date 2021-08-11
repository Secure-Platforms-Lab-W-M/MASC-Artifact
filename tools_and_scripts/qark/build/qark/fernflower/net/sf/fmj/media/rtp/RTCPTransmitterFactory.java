package net.sf.fmj.media.rtp;

public interface RTCPTransmitterFactory {
   RTCPTransmitter newRTCPTransmitter(SSRCCache var1, RTCPRawSender var2);
}
