package net.sf.fmj.media.rtp;

import java.io.DataOutputStream;
import java.io.IOException;

public class RTCPAPPPacket extends RTCPPacket {
   byte[] data;
   int name;
   int ssrc;
   int subtype;

   public RTCPAPPPacket(int var1, int var2, int var3, byte[] var4) {
      if ((var4.length & 3) == 0) {
         if (var3 >= 0 && var3 <= 31) {
            this.ssrc = var1;
            this.name = var2;
            this.subtype = var3;
            this.data = var4;
            super.type = 204;
            super.received = false;
         } else {
            throw new IllegalArgumentException("Bad subtype");
         }
      } else {
         throw new IllegalArgumentException("Bad data length");
      }
   }

   public RTCPAPPPacket(RTCPPacket var1) {
      super(var1);
      super.type = 204;
   }

   public void assemble(DataOutputStream var1) throws IOException {
      var1.writeByte(this.subtype + 128);
      var1.writeByte(204);
      var1.writeShort((this.data.length >> 2) + 2);
      var1.writeInt(this.ssrc);
      var1.writeInt(this.name);
      var1.write(this.data);
   }

   public int calcLength() {
      return this.data.length + 12;
   }

   public String nameString(int var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("");
      var2.append((char)(var1 >>> 24));
      var2.append((char)(var1 >>> 16 & 255));
      var2.append((char)(var1 >>> 8 & 255));
      var2.append((char)(var1 & 255));
      return var2.toString();
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("\tRTCP APP Packet from SSRC ");
      var1.append(this.ssrc);
      var1.append(" with name ");
      var1.append(this.nameString(this.name));
      var1.append(" and subtype ");
      var1.append(this.subtype);
      var1.append("\n\tData (length ");
      var1.append(this.data.length);
      var1.append("): ");
      var1.append(new String(this.data));
      var1.append("\n");
      return var1.toString();
   }
}
