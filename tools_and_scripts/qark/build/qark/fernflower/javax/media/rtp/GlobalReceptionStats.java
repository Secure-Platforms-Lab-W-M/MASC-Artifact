package javax.media.rtp;

public interface GlobalReceptionStats {
   int getBadRTCPPkts();

   int getBadRTPkts();

   int getBytesRecd();

   int getLocalColls();

   int getMalformedBye();

   int getMalformedRR();

   int getMalformedSDES();

   int getMalformedSR();

   int getPacketsLooped();

   int getPacketsRecd();

   int getRTCPRecd();

   int getRemoteColls();

   int getSRRecd();

   int getTransmitFailed();

   int getUnknownTypes();
}
