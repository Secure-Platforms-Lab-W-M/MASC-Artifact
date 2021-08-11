package net.sf.fmj.media.rtp;

import java.io.DataOutputStream;
import java.io.IOException;

public class RTCPBYEPacket extends RTCPPacket {
   byte[] reason;
   int[] ssrc;

   public RTCPBYEPacket(RTCPPacket var1) {
      super(var1);
      super.type = 203;
   }

   public RTCPBYEPacket(int[] var1, byte[] var2) {
      if (var1.length <= 31) {
         this.ssrc = var1;
         if (var2 == null) {
            var2 = new byte[0];
         }

         this.reason = var2;
      } else {
         throw new IllegalArgumentException("Too many SSRCs");
      }
   }

   public void assemble(DataOutputStream var1) throws IOException {
      var1.writeByte(this.ssrc.length + 128);
      var1.writeByte(203);
      int var3 = this.ssrc.length;
      byte[] var4 = this.reason;
      int var2;
      if (var4.length <= 0) {
         var2 = 0;
      } else {
         var2 = var4.length + 4 >> 2;
      }

      var1.writeShort(var3 + var2);
      var2 = 0;

      while(true) {
         int[] var5 = this.ssrc;
         if (var2 >= var5.length) {
            var4 = this.reason;
            if (var4.length > 0) {
               var1.writeByte(var4.length);
               var1.write(this.reason);
               var4 = this.reason;

               for(var2 = (var4.length + 4 & -4) - var4.length - 1; var2 > 0; --var2) {
                  var1.writeByte(0);
               }
            }

            return;
         }

         var1.writeInt(var5[var2]);
         ++var2;
      }
   }

   public int calcLength() {
      int var2 = this.ssrc.length;
      byte[] var3 = this.reason;
      int var1;
      if (var3.length <= 0) {
         var1 = 0;
      } else {
         var1 = var3.length + 4 & -4;
      }

      return (var2 << 2) + 4 + var1;
   }

   public String toString() {
      StringBuilder var2 = new StringBuilder();
      var2.append("\tRTCP BYE packet for sync source(s) ");
      var2.append(this.toString(this.ssrc));
      var2.append(" for ");
      String var1;
      if (this.reason.length <= 0) {
         var1 = "no reason";
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("reason ");
         var3.append(new String(this.reason));
         var1 = var3.toString();
      }

      var2.append(var1);
      var2.append("\n");
      return var2.toString();
   }

   public String toString(int[] var1) {
      if (var1.length == 0) {
         return "(none)";
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("");
         var3.append(var1[0]);
         String var5 = var3.toString();

         for(int var2 = 1; var2 < var1.length; ++var2) {
            StringBuilder var4 = new StringBuilder();
            var4.append(var5);
            var4.append(", ");
            var4.append(var1[var2]);
            var5 = var4.toString();
         }

         return var5;
      }
   }
}
