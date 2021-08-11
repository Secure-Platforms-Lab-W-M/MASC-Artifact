package net.sf.fmj.media.rtp.util;

import java.io.IOException;
import javax.media.rtp.OutputDataStream;
import javax.media.rtp.RTPConnector;
import javax.media.rtp.RTPPushDataSource;

public class RTPPacketSender implements PacketConsumer {
   RTPConnector connector = null;
   RTPPushDataSource dest = null;
   OutputDataStream outstream = null;

   public RTPPacketSender(OutputDataStream var1) {
      this.outstream = var1;
   }

   public RTPPacketSender(RTPConnector var1) throws IOException {
      this.connector = var1;
      this.outstream = var1.getDataOutputStream();
   }

   public RTPPacketSender(RTPPushDataSource var1) {
      this.dest = var1;
      this.outstream = var1.getInputStream();
   }

   public void closeConsumer() {
   }

   public String consumerString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("RTPPacketSender for ");
      var1.append(this.dest);
      return var1.toString();
   }

   public RTPConnector getConnector() {
      return this.connector;
   }

   public void sendTo(Packet var1) throws IOException {
      OutputDataStream var2 = this.outstream;
      if (var2 != null) {
         var2.write(var1.data, 0, var1.length);
      } else {
         throw new IOException();
      }
   }
}
