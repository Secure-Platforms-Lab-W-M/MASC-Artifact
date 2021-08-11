package net.sf.fmj.media.rtp;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;
import javax.media.rtp.SessionAddress;
import net.sf.fmj.media.rtp.util.Packet;
import net.sf.fmj.media.rtp.util.PacketFilter;
import net.sf.fmj.media.rtp.util.RTPPacketSender;
import net.sf.fmj.media.rtp.util.UDPPacket;
import net.sf.fmj.media.rtp.util.UDPPacketSender;

public class RTCPRawSender extends PacketFilter {
   private InetAddress destaddr;
   private int destport;

   public RTCPRawSender(int var1, String var2) throws UnknownHostException, IOException {
      this.destaddr = InetAddress.getByName(var2);
      this.destport = var1 | 1;
      super.destAddressList = null;
   }

   public RTCPRawSender(int var1, String var2, UDPPacketSender var3) throws UnknownHostException, IOException {
      this(var1, var2);
      this.setConsumer(var3);
      super.destAddressList = null;
   }

   public RTCPRawSender(RTPPacketSender var1) {
      this.setConsumer(var1);
   }

   public void addDestAddr(InetAddress var1) {
      if (super.destAddressList == null) {
         super.destAddressList = new Vector();
         super.destAddressList.addElement(this.destaddr);
      }

      int var2;
      for(var2 = 0; var2 < super.destAddressList.size() && !((InetAddress)super.destAddressList.elementAt(var2)).equals(var1); ++var2) {
      }

      if (var2 == super.destAddressList.size()) {
         super.destAddressList.addElement(var1);
      }

   }

   public void assemble(RTCPCompoundPacket var1) {
      var1.assemble(var1.calcLength(), false);
   }

   public String filtername() {
      return "RTCP Raw Packet Sender";
   }

   public InetAddress getRemoteAddr() {
      return this.destaddr;
   }

   public Packet handlePacket(Packet var1) {
      this.assemble((RTCPCompoundPacket)var1);
      if (this.getConsumer() instanceof RTPPacketSender) {
         return var1;
      } else {
         UDPPacket var2 = new UDPPacket();
         var2.received = false;
         var2.data = var1.data;
         var2.offset = var1.offset;
         var2.length = var1.length;
         var2.remoteAddress = this.destaddr;
         var2.remotePort = this.destport;
         return var2;
      }
   }

   public Packet handlePacket(Packet var1, int var2) {
      this.assemble((RTCPCompoundPacket)var1);
      UDPPacket var3 = new UDPPacket();
      var3.received = false;
      var3.data = var1.data;
      var3.offset = var1.offset;
      var3.length = var1.length;
      var3.remoteAddress = (InetAddress)super.destAddressList.elementAt(var2);
      var3.remotePort = this.destport;
      return var3;
   }

   public Packet handlePacket(Packet var1, SessionAddress var2) {
      this.assemble((RTCPCompoundPacket)var1);
      if (this.getConsumer() instanceof RTPPacketSender) {
         return var1;
      } else {
         UDPPacket var3 = new UDPPacket();
         var3.received = false;
         var3.data = var1.data;
         var3.offset = var1.offset;
         var3.length = var1.length;
         var3.remoteAddress = var2.getControlAddress();
         var3.remotePort = var2.getControlPort();
         return var3;
      }
   }

   public void setDestAddresses(Vector var1) {
      super.destAddressList = var1;
   }
}
