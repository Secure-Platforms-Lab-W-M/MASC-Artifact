package net.sf.fmj.media.rtp.util;

import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.SocketException;

public class UDPPacketSender implements PacketConsumer {
   private InetAddress address;
   private int port;
   private DatagramSocket sock;
   private int ttl;

   public UDPPacketSender() throws IOException {
      this(new DatagramSocket());
   }

   public UDPPacketSender(int var1) throws IOException {
      this(new DatagramSocket(var1));
   }

   public UDPPacketSender(int var1, InetAddress var2, InetAddress var3, int var4) throws IOException {
      if (var3.isMulticastAddress()) {
         MulticastSocket var5 = new MulticastSocket(var1);
         if (var2 != null) {
            var5.setInterface(var2);
         }

         this.sock = var5;
      } else if (var2 != null) {
         try {
            this.sock = new DatagramSocket(var1, var2);
         } catch (SocketException var7) {
            System.out.println(var7);
            PrintStream var8 = System.out;
            StringBuilder var6 = new StringBuilder();
            var6.append("localPort: ");
            var6.append(var1);
            var8.println(var6.toString());
            var8 = System.out;
            var6 = new StringBuilder();
            var6.append("localAddress: ");
            var6.append(var2);
            var8.println(var6.toString());
            throw var7;
         }
      } else {
         this.sock = new DatagramSocket(var1);
      }

      this.setRemoteAddress(var3, var4);
   }

   public UDPPacketSender(DatagramSocket var1) {
      this.sock = var1;
   }

   public UDPPacketSender(InetAddress var1, int var2) throws IOException {
      if (var1.isMulticastAddress()) {
         this.sock = new MulticastSocket();
      } else {
         this.sock = new DatagramSocket();
      }

      this.setRemoteAddress(var1, var2);
   }

   public void closeConsumer() {
      DatagramSocket var1 = this.sock;
      if (var1 != null) {
         var1.close();
         this.sock = null;
      }

   }

   public String consumerString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("UDP Datagram Packet Sender on port ");
      var1.append(this.sock.getLocalPort());
      String var2 = var1.toString();
      String var3 = var2;
      if (this.address != null) {
         var1 = new StringBuilder();
         var1.append(var2);
         var1.append(" sending to address ");
         var1.append(this.address);
         var1.append(", port ");
         var1.append(this.port);
         var1.append(", ttl");
         var1.append(this.ttl);
         var3 = var1.toString();
      }

      return var3;
   }

   public InetAddress getLocalAddress() {
      return this.sock.getLocalAddress();
   }

   public int getLocalPort() {
      return this.sock.getLocalPort();
   }

   public DatagramSocket getSocket() {
      return this.sock;
   }

   public void send(Packet var1, InetAddress var2, int var3) throws IOException {
      byte[] var6 = var1.data;
      byte[] var5 = var6;
      if (var1.offset > 0) {
         int var4 = var1.offset;
         var5 = new byte[var1.length];
         System.arraycopy(var6, var4, var5, 0, var1.length);
      }

      DatagramPacket var7 = new DatagramPacket(var5, var1.length, var2, var3);
      this.sock.send(var7);
   }

   public void sendTo(Packet var1) throws IOException {
      InetAddress var3 = null;
      int var2 = 0;
      if (var1 instanceof UDPPacket) {
         UDPPacket var4 = (UDPPacket)var1;
         var3 = var4.remoteAddress;
         var2 = var4.remotePort;
      }

      if (var3 != null) {
         this.send(var1, var3, var2);
      } else {
         throw new IllegalArgumentException("No address set");
      }
   }

   public void setRemoteAddress(InetAddress var1, int var2) {
      this.address = var1;
      this.port = var2;
   }

   public void setttl(int var1) throws IOException {
      this.ttl = var1;
      DatagramSocket var2 = this.sock;
      if (var2 instanceof MulticastSocket) {
         ((MulticastSocket)var2).setTTL((byte)var1);
      }

   }
}
