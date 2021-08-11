package net.sf.fmj.media.rtp;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import javax.media.rtp.RTPConnector;
import javax.media.rtp.RTPPushDataSource;
import javax.media.rtp.SessionAddress;
import net.sf.fmj.media.Log;
import net.sf.fmj.media.rtp.util.BadFormatException;
import net.sf.fmj.media.rtp.util.Packet;
import net.sf.fmj.media.rtp.util.PacketFilter;
import net.sf.fmj.media.rtp.util.RTPPacket;
import net.sf.fmj.media.rtp.util.RTPPacketParser;
import net.sf.fmj.media.rtp.util.RTPPacketReceiver;
import net.sf.fmj.media.rtp.util.UDPPacketReceiver;

public class RTPRawReceiver extends PacketFilter {
   private final RTPPacketParser parser = new RTPPacketParser();
   private boolean recvBufSizeSet = false;
   private RTPConnector rtpConnector = null;
   public DatagramSocket socket;
   private OverallStats stats = null;

   public RTPRawReceiver() {
   }

   public RTPRawReceiver(int var1, String var2, OverallStats var3) throws UnknownHostException, IOException, SocketException {
      UDPPacketReceiver var4 = new UDPPacketReceiver(var1 & -2, var2, -1, (String)null, 2000, (DatagramSocket)null);
      this.setSource(var4);
      this.socket = var4.getSocket();
      this.stats = var3;
   }

   public RTPRawReceiver(DatagramSocket var1, OverallStats var2) {
      this.setSource(new UDPPacketReceiver(var1, 2000));
      this.stats = var2;
   }

   public RTPRawReceiver(RTPConnector var1, OverallStats var2) {
      try {
         this.setSource(new RTPPacketReceiver(var1.getDataInputStream()));
      } catch (IOException var4) {
         var4.printStackTrace();
      }

      this.rtpConnector = var1;
      this.stats = var2;
   }

   public RTPRawReceiver(RTPPushDataSource var1, OverallStats var2) {
      this.setSource(new RTPPacketReceiver(var1));
      this.stats = var2;
   }

   public RTPRawReceiver(SessionAddress var1, SessionAddress var2, OverallStats var3, DatagramSocket var4) throws UnknownHostException, IOException, SocketException {
      this.stats = var3;
      UDPPacketReceiver var5 = new UDPPacketReceiver(var1.getDataPort(), var1.getDataHostAddress(), var2.getDataPort(), var2.getDataHostAddress(), 2000, var4);
      this.setSource(var5);
      this.socket = var5.getSocket();
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

   public String filtername() {
      return "RTP Raw Packet Receiver";
   }

   public int getRecvBufSize() {
      try {
         int var1 = (Integer)this.socket.getClass().getMethod("getReceiveBufferSize").invoke(this.socket);
         return var1;
      } catch (Exception var3) {
         RTPConnector var2 = this.rtpConnector;
         return var2 != null ? var2.getReceiveBufferSize() : -1;
      }
   }

   public Packet handlePacket(Packet var1) {
      this.stats.update(0, 1);
      this.stats.update(1, var1.length);

      RTPPacket var4;
      try {
         var4 = this.parser.parse(var1);
      } catch (BadFormatException var3) {
         this.stats.update(2, 1);
         return null;
      }

      if (!this.recvBufSizeSet) {
         this.recvBufSizeSet = true;
         int var2 = var4.payloadType;
         if (var2 != 14 && var2 != 26 && var2 != 34 && var2 != 42) {
            if (var2 == 31) {
               this.setRecvBufSize(128000);
               return var4;
            }

            if (var2 == 32) {
               this.setRecvBufSize(128000);
               return var4;
            }

            if (var4.payloadType >= 96 && var4.payloadType <= 127) {
               this.setRecvBufSize(64000);
               return var4;
            }
         } else {
            this.setRecvBufSize(64000);
         }
      }

      return var4;
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

   public void setRecvBufSize(int var1) {
      try {
         if (this.socket == null && this.rtpConnector != null) {
            this.rtpConnector.setReceiveBufferSize(var1);
         }

      } catch (Exception var4) {
         StringBuilder var3 = new StringBuilder();
         var3.append("Cannot set receive buffer size: ");
         var3.append(var4);
         Log.comment(var3.toString());
      }
   }
}
