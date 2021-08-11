package net.sf.fmj.media.rtp.util;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Date;

public class UDPPacket extends Packet {
   public DatagramPacket datagrampacket;
   public int localPort;
   public InetAddress remoteAddress;
   public int remotePort;

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("UDP Packet of size ");
      var1.append(super.length);
      String var2 = var1.toString();
      String var3 = var2;
      if (super.received) {
         var1 = new StringBuilder();
         var1.append(var2);
         var1.append(" received at ");
         var1.append(new Date(super.receiptTime));
         var1.append(" on port ");
         var1.append(this.localPort);
         var1.append(" from ");
         var1.append(this.remoteAddress);
         var1.append(" port ");
         var1.append(this.remotePort);
         var3 = var1.toString();
      }

      return var3;
   }
}
