package net.sf.fmj.media.rtp.util;

import java.io.IOException;
import java.util.Vector;
import javax.media.rtp.SessionAddress;

public abstract class PacketFilter implements PacketSource, PacketConsumer {
   PacketConsumer consumer;
   public boolean control = false;
   public Vector destAddressList = null;
   public Vector peerlist = null;
   PacketSource source;

   public void close() {
   }

   public void closeConsumer() {
      this.close();
      PacketConsumer var1 = this.consumer;
      if (var1 != null) {
         var1.closeConsumer();
      }

   }

   public void closeSource() {
      this.close();
      PacketSource var1 = this.source;
      if (var1 != null) {
         var1.closeSource();
      }

   }

   public String consumerString() {
      if (this.consumer == null) {
         return this.filtername();
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append(this.filtername());
         var1.append(" connected to ");
         var1.append(this.consumer.consumerString());
         return var1.toString();
      }
   }

   public String filtername() {
      return this.getClass().getName();
   }

   public PacketConsumer getConsumer() {
      return this.consumer;
   }

   public Vector getDestList() {
      return null;
   }

   public PacketSource getSource() {
      return this.source;
   }

   public abstract Packet handlePacket(Packet var1);

   public abstract Packet handlePacket(Packet var1, int var2);

   public abstract Packet handlePacket(Packet var1, SessionAddress var2);

   public Packet receiveFrom() throws IOException {
      Packet var1 = null;
      Packet var2 = this.source.receiveFrom();
      if (var2 != null) {
         var1 = this.handlePacket(var2);
      }

      return var1;
   }

   public void sendTo(Packet var1) throws IOException {
      int var2;
      PacketConsumer var5;
      if (this.peerlist != null) {
         var1 = this.handlePacket(var1);

         for(var2 = 0; var2 < this.peerlist.size(); ++var2) {
            SessionAddress var7 = (SessionAddress)this.peerlist.elementAt(var2);
            if (!this.control) {
               ((UDPPacket)var1).remoteAddress = var7.getDataAddress();
               ((UDPPacket)var1).remotePort = var7.getDataPort();
            } else {
               ((UDPPacket)var1).remoteAddress = var7.getControlAddress();
               ((UDPPacket)var1).remotePort = var7.getControlPort();
            }

            if (var1 != null) {
               var5 = this.consumer;
               if (var5 != null) {
                  var5.sendTo(var1);
               }
            }
         }

      } else {
         Vector var3 = this.destAddressList;
         if (var3 != null) {
            for(var2 = 0; var2 < this.destAddressList.size(); ++var2) {
               Packet var6 = this.handlePacket(var1, (SessionAddress)this.destAddressList.elementAt(var2));
               if (var6 != null) {
                  PacketConsumer var4 = this.consumer;
                  if (var4 != null) {
                     var4.sendTo(var6);
                  }
               }
            }

         } else {
            if (var3 == null) {
               var1 = this.handlePacket(var1);
               if (var1 != null) {
                  var5 = this.consumer;
                  if (var5 != null) {
                     var5.sendTo(var1);
                  }
               }
            }

         }
      }
   }

   public void setConsumer(PacketConsumer var1) {
      this.consumer = var1;
   }

   public void setSource(PacketSource var1) {
      this.source = var1;
   }

   public String sourceString() {
      if (this.source == null) {
         return this.filtername();
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append(this.filtername());
         var1.append(" attached to ");
         var1.append(this.source.sourceString());
         return var1.toString();
      }
   }
}
