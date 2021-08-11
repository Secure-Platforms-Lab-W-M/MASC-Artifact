package net.sf.fmj.media.rtp;

public interface RTCPTransmitter {
   void bye(String var1);

   void close();

   SSRCCache getCache();

   SSRCInfo getSSRCInfo();

   RTCPRawSender getSender();

   void report();

   void setSSRCInfo(SSRCInfo var1);
}
