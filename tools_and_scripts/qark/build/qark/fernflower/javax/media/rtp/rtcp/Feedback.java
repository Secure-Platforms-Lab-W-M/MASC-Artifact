package javax.media.rtp.rtcp;

public interface Feedback {
   long getDLSR();

   int getFractionLost();

   long getJitter();

   long getLSR();

   long getNumLost();

   long getSSRC();

   long getXtndSeqNum();
}
