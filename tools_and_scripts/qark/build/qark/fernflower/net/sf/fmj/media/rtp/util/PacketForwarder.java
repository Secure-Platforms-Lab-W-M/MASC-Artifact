package net.sf.fmj.media.rtp.util;

import java.io.IOException;

public class PacketForwarder implements Runnable {
   boolean closed = false;
   PacketConsumer consumer = null;
   public IOException exception = null;
   private boolean paused;
   PacketSource source = null;
   RTPMediaThread thread;

   public PacketForwarder(PacketSource var1, PacketConsumer var2) {
      this.source = var1;
      this.consumer = var2;
      this.closed = false;
      this.exception = null;
   }

   private boolean checkForClose() {
      if (this.closed && this.thread != null) {
         PacketSource var1 = this.source;
         if (var1 != null) {
            var1.closeSource();
         }

         return true;
      } else {
         return false;
      }
   }

   public void close() {
      this.closed = true;
      PacketConsumer var1 = this.consumer;
      if (var1 != null) {
         var1.closeConsumer();
      }

   }

   public PacketConsumer getConsumer() {
      return this.consumer;
   }

   public String getId() {
      RTPMediaThread var1 = this.thread;
      if (var1 == null) {
         System.err.println("the packetforwarders thread is null");
         return null;
      } else {
         return var1.getName();
      }
   }

   public PacketSource getSource() {
      return this.source;
   }

   public void run() {
      // $FF: Couldn't be decompiled
   }

   public void setVideoPriority() {
      this.thread.useVideoNetworkPriority();
   }

   public void startPF() {
      this.startPF((String)null);
   }

   public void startPF(String var1) {
      if (this.thread == null) {
         String var2 = var1;
         if (var1 == null) {
            var2 = "RTPMediaThread";
         }

         RTPMediaThread var3 = new RTPMediaThread(this, var2);
         this.thread = var3;
         var3.useNetworkPriority();
         this.thread.setDaemon(true);
         this.thread.start();
      } else {
         throw new IllegalArgumentException("Called start more than once");
      }
   }
}
