package net.sf.fmj.media.rtp;

import java.io.DataOutputStream;
import java.io.IOException;

public class RTCPSDESPacket extends RTCPPacket {
   public RTCPSDES[] sdes;

   public RTCPSDESPacket(RTCPPacket var1) {
      super(var1);
      super.type = 202;
   }

   public RTCPSDESPacket(RTCPSDES[] var1) {
      if (var1.length <= 31) {
         this.sdes = var1;
      } else {
         throw new IllegalArgumentException("Too many SDESs");
      }
   }

   public void assemble(DataOutputStream var1) throws IOException {
      var1.writeByte(this.sdes.length + 128);
      var1.writeByte(202);
      var1.writeShort(this.calcLength() - 4 >> 2);
      int var2 = 0;

      while(true) {
         RTCPSDES[] var5 = this.sdes;
         if (var2 >= var5.length) {
            return;
         }

         var1.writeInt(var5[var2].ssrc);
         int var4 = 0;

         int var3;
         for(var3 = 0; var3 < this.sdes[var2].items.length; ++var3) {
            var1.writeByte(this.sdes[var2].items[var3].type);
            var1.writeByte(this.sdes[var2].items[var3].data.length);
            var1.write(this.sdes[var2].items[var3].data);
            var4 += this.sdes[var2].items[var3].data.length + 2;
         }

         for(var3 = (var4 + 4 & -4) - var4; var3 > 0; --var3) {
            var1.writeByte(0);
         }

         ++var2;
      }
   }

   public int calcLength() {
      int var2 = 4;

      for(int var1 = 0; var1 < this.sdes.length; ++var1) {
         int var4 = 5;

         for(int var3 = 0; var3 < this.sdes[var1].items.length; ++var3) {
            var4 += this.sdes[var1].items[var3].data.length + 2;
         }

         var2 += var4 + 3 & -4;
      }

      return var2;
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("\tRTCP SDES Packet:\n");
      var1.append(RTCPSDES.toString(this.sdes));
      return var1.toString();
   }
}
