package javax.media.rtp;

public interface GlobalTransmissionStats {
   int getBytesSent();

   int getLocalColls();

   int getRTCPSent();

   int getRTPSent();

   int getRemoteColls();

   int getTransmitFailed();
}
