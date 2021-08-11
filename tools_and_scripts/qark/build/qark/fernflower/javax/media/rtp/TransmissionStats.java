package javax.media.rtp;

public interface TransmissionStats {
   int getBytesTransmitted();

   int getPDUTransmitted();

   int getRTCPSent();
}
