package dnsfilter;

import ip.UDPPacket;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public class DNSResolver implements Runnable {
   private static Object CNT_SYNC = new Object();
   private static boolean IO_ERROR = false;
   private static int THR_COUNT = 0;
   private DatagramPacket dataGramRequest;
   private boolean datagramPacketMode = false;
   private DatagramSocket replySocket;
   private OutputStream responseOut;
   private UDPPacket udpRequestPacket;

   public DNSResolver(UDPPacket var1, OutputStream var2) {
      this.udpRequestPacket = var1;
      this.responseOut = var2;
   }

   public DNSResolver(DatagramPacket var1, DatagramSocket var2) {
      this.datagramPacketMode = true;
      this.dataGramRequest = var1;
      this.replySocket = var2;
   }

   public static int getResolverCount() {
      return THR_COUNT;
   }

   private void processDatagramPackageMode() throws Exception {
      SocketAddress var1 = this.dataGramRequest.getSocketAddress();
      byte[] var2 = this.dataGramRequest.getData();
      DatagramPacket var3 = new DatagramPacket(var2, this.dataGramRequest.getOffset(), var2.length - this.dataGramRequest.getOffset());
      DNSCommunicator.getInstance().requestDNS(this.dataGramRequest, var3);
      DNSResponsePatcher.patchResponse(var1.toString(), var3.getData(), var3.getOffset());
      var3.setSocketAddress(var1);
      this.replySocket.send(var3);
   }

   private void processIPPackageMode() throws Exception {
      // $FF: Couldn't be decompiled
   }

   public void run() {
      // $FF: Couldn't be decompiled
   }
}
