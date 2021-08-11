package net.sf.fmj.media.rtp;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.media.rtp.RTPConnector;
import javax.media.rtp.RTPPushDataSource;
import javax.media.rtp.SessionAddress;
import net.sf.fmj.media.rtp.util.BadFormatException;
import net.sf.fmj.media.rtp.util.Packet;
import net.sf.fmj.media.rtp.util.PacketFilter;
import net.sf.fmj.media.rtp.util.RTPPacketReceiver;
import net.sf.fmj.media.rtp.util.UDPPacketReceiver;

public class RTCPRawReceiver extends PacketFilter implements RTCPPacketParserListener {
   private RTCPPacketParser parser;
   public DatagramSocket socket;
   private OverallStats stats = null;
   private StreamSynch streamSynch;

   public RTCPRawReceiver() {
   }

   public RTCPRawReceiver(int var1, String var2, OverallStats var3, StreamSynch var4) throws UnknownHostException, IOException, SocketException {
      this.streamSynch = var4;
      this.stats = var3;
      UDPPacketReceiver var5 = new UDPPacketReceiver(var1, var2, -1, (String)null, 1000, (DatagramSocket)null);
      this.setSource(var5);
      this.socket = var5.getSocket();
   }

   public RTCPRawReceiver(DatagramSocket var1, OverallStats var2, StreamSynch var3) {
      this.setSource(new UDPPacketReceiver(var1, 1000));
      this.stats = var2;
      this.streamSynch = var3;
   }

   public RTCPRawReceiver(RTPConnector var1, OverallStats var2, StreamSynch var3) {
      this.streamSynch = var3;

      try {
         this.setSource(new RTPPacketReceiver(var1.getControlInputStream()));
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      this.stats = var2;
   }

   public RTCPRawReceiver(RTPPushDataSource var1, OverallStats var2, StreamSynch var3) {
      this.streamSynch = var3;
      this.setSource(new RTPPacketReceiver(var1));
      this.stats = var2;
   }

   public RTCPRawReceiver(SessionAddress var1, SessionAddress var2, OverallStats var3, StreamSynch var4, DatagramSocket var5) throws UnknownHostException, IOException, SocketException {
      this.streamSynch = var4;
      this.stats = var3;
      UDPPacketReceiver var6 = new UDPPacketReceiver(var1.getControlPort(), var1.getControlHostAddress(), var2.getControlPort(), var2.getControlHostAddress(), 1000, var5);
      this.setSource(var6);
      this.socket = var6.getSocket();
   }

   public void close() {
      DatagramSocket var1 = this.socket;
      if (var1 != null) {
         var1.close();
      }

      if (this.getSource() instanceof RTPPacketReceiver) {
         this.getSource().closeSource();
      }

   }

   public void enterSenderReport() {
      this.stats.update(12, 1);
   }

   public String filtername() {
      return "RTCP Raw Receiver";
   }

   public Packet handlePacket(Packet var1) {
      this.stats.update(0, 1);
      this.stats.update(11, 1);
      this.stats.update(1, var1.length);
      if (this.parser == null) {
         RTCPPacketParser var2 = new RTCPPacketParser();
         this.parser = var2;
         var2.addRTCPPacketParserListener(this);
      }

      try {
         RTCPPacket var4 = this.parser.parse(var1);
         return var4;
      } catch (BadFormatException var3) {
         this.stats.update(13, 1);
         return null;
      }
   }

   public Packet handlePacket(Packet var1, int var2) {
      return null;
   }

   public Packet handlePacket(Packet var1, SessionAddress var2) {
      return null;
   }

   public Packet handlePacket(Packet var1, SessionAddress var2, boolean var3) {
      return null;
   }

   public void malformedEndOfParticipation() {
      this.stats.update(17, 1);
   }

   public void malformedReceiverReport() {
      this.stats.update(15, 1);
   }

   public void malformedSenderReport() {
      this.stats.update(18, 1);
   }

   public void malformedSourceDescription() {
      this.stats.update(16, 1);
   }

   public void uknownPayloadType() {
      this.stats.update(14, 1);
   }

   public void visitSendeReport(RTCPSRPacket var1) {
      this.streamSynch.update(var1.ssrc, var1.rtptimestamp, var1.ntptimestampmsw, var1.ntptimestamplsw);
   }
}
