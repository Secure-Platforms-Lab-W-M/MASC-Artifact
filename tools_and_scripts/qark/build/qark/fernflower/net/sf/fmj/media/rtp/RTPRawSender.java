package net.sf.fmj.media.rtp;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Vector;
import javax.media.rtp.RTPConnector;
import javax.media.rtp.SessionAddress;
import net.sf.fmj.media.Log;
import net.sf.fmj.media.rtp.util.Packet;
import net.sf.fmj.media.rtp.util.PacketFilter;
import net.sf.fmj.media.rtp.util.RTPPacket;
import net.sf.fmj.media.rtp.util.RTPPacketSender;
import net.sf.fmj.media.rtp.util.UDPPacket;
import net.sf.fmj.media.rtp.util.UDPPacketSender;

public class RTPRawSender extends PacketFilter {
   private InetAddress destaddr;
   private int destport;
   private RTPConnector rtpConnector;
   private DatagramSocket socket;

   public RTPRawSender(int var1, String var2) throws UnknownHostException, IOException {
      this.socket = null;
      this.rtpConnector = null;
      this.destaddr = InetAddress.getByName(var2);
      this.destport = var1;
      super.destAddressList = null;
   }

   public RTPRawSender(int var1, String var2, UDPPacketSender var3) throws UnknownHostException, IOException {
      this(var1, var2);
      this.socket = var3.getSocket();
      this.setConsumer(var3);
      super.destAddressList = null;
   }

   public RTPRawSender(RTPPacketSender var1) {
      this.socket = null;
      this.rtpConnector = null;
      this.rtpConnector = var1.getConnector();
      this.setConsumer(var1);
   }

   public void assemble(RTPPacket var1) {
      var1.assemble(var1.calcLength(), false);
   }

   public String filtername() {
      return "RTP Raw Packet Sender";
   }

   public InetAddress getRemoteAddr() {
      return this.destaddr;
   }

   public int getSendBufSize() {
      try {
         if (this.socket != null) {
            return (Integer)this.socket.getClass().getMethod("getSendBufferSize").invoke(this.socket);
         }

         if (this.rtpConnector != null) {
            int var1 = this.rtpConnector.getSendBufferSize();
            return var1;
         }
      } catch (Exception var3) {
      }

      return -1;
   }

   public Packet handlePacket(Packet var1) {
      this.assemble((RTPPacket)var1);
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
      return null;
   }

   public Packet handlePacket(Packet var1, SessionAddress var2) {
      this.assemble((RTPPacket)var1);
      if (this.getConsumer() instanceof RTPPacketSender) {
         return var1;
      } else {
         UDPPacket var3 = new UDPPacket();
         var3.received = false;
         var3.data = var1.data;
         var3.offset = var1.offset;
         var3.length = var1.length;
         var3.remoteAddress = var2.getDataAddress();
         var3.remotePort = var2.getDataPort();
         return var3;
      }
   }

   public void setDestAddresses(Vector var1) {
      super.destAddressList = var1;
   }

   public void setSendBufSize(int var1) {
      try {
         if (this.socket != null) {
            this.socket.getClass().getMethod("setSendBufferSize", Integer.TYPE).invoke(this.socket, new Integer(var1));
         } else if (this.rtpConnector != null) {
            this.rtpConnector.setSendBufferSize(var1);
         }

      } catch (Exception var4) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Cannot set send buffer size: ");
         var3.append(var4);
         Log.comment(var3.toString());
      }
   }
}
