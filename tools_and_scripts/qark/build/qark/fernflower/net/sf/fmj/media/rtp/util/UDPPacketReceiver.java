package net.sf.fmj.media.rtp.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UDPPacketReceiver implements PacketSource {
   byte[] dataBuf = new byte[1];
   private int maxsize;
   private DatagramSocket sock;

   public UDPPacketReceiver(int var1, String var2, int var3, String var4, int var5, DatagramSocket var6) throws SocketException, UnknownHostException, IOException {
      InetAddress var8 = InetAddress.getByName(var2);
      InetAddress var10 = InetAddress.getByName(var4);
      if (var10.isMulticastAddress()) {
         MulticastSocket var9 = new MulticastSocket(var3);
         var9.joinGroup(var10);
         this.sock = var9;
         this.maxsize = var5;
      } else {
         if (var6 != null) {
            this.sock = var6;
         } else {
            this.sock = new DatagramSocket(var1, var8);
         }

         this.maxsize = var5;
      }

      try {
         this.sock.setSoTimeout(5000);
      } catch (SocketException var7) {
         System.out.println("could not set timeout on socket");
      }
   }

   public UDPPacketReceiver(DatagramSocket var1, int var2) {
      this.sock = var1;
      this.maxsize = var2;

      try {
         var1.setSoTimeout(5000);
      } catch (SocketException var3) {
         System.out.println("could not set timeout on socket");
      }
   }

   public void closeSource() {
      DatagramSocket var1 = this.sock;
      if (var1 != null) {
         var1.close();
         this.sock = null;
      }

   }

   public DatagramSocket getSocket() {
      return this.sock;
   }

   public Packet receiveFrom() throws IOException {
      int var1;
      DatagramPacket var3;
      do {
         var1 = this.dataBuf.length;
         int var2 = this.maxsize;
         if (var1 < var2) {
            this.dataBuf = new byte[var2];
         }

         var3 = new DatagramPacket(this.dataBuf, this.maxsize);
         this.sock.receive(var3);
         var1 = var3.getLength();
         if (var1 > this.maxsize >> 1) {
            this.maxsize = var1 << 1;
         }
      } while(var1 >= var3.getData().length);

      UDPPacket var4 = new UDPPacket();
      var4.receiptTime = System.currentTimeMillis();
      var4.data = var3.getData();
      var4.offset = 0;
      var4.length = var1;
      var4.datagrampacket = var3;
      var4.localPort = this.sock.getLocalPort();
      var4.remotePort = var3.getPort();
      var4.remoteAddress = var3.getAddress();
      return var4;
   }

   public String sourceString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("UDP Datagram Packet Receiver on port ");
      var1.append(this.sock.getLocalPort());
      var1.append("on local address ");
      var1.append(this.sock.getLocalAddress());
      return var1.toString();
   }
}
