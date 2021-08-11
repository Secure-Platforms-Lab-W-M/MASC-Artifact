package net.sf.fmj.media.rtp.util;

import java.util.Date;

public class Packet {
   public byte[] data;
   public int flags;
   public int length;
   public int offset;
   public long receiptTime;
   public boolean received = true;

   public Packet() {
   }

   public Packet(Packet var1) {
      this.data = var1.data;
      this.offset = var1.offset;
      this.length = var1.length;
      this.received = var1.received;
      this.receiptTime = var1.receiptTime;
      this.flags = var1.flags;
   }

   public Packet clone() {
      Packet var1 = new Packet(this);
      var1.data = (byte[])this.data.clone();
      return var1;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Packet of size ");
      var1.append(this.length);
      String var2 = var1.toString();
      String var3 = var2;
      if (this.received) {
         var1 = new StringBuilder();
         var1.append(var2);
         var1.append(" received at ");
         var1.append(new Date(this.receiptTime));
         var3 = var1.toString();
      }

      return var3;
   }
}
