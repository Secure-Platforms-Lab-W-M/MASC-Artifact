package net.sf.fmj.media.rtp;

public interface RTCPPacketParserListener {
   void enterSenderReport();

   void malformedEndOfParticipation();

   void malformedReceiverReport();

   void malformedSenderReport();

   void malformedSourceDescription();

   void uknownPayloadType();

   void visitSendeReport(RTCPSRPacket var1);
}
