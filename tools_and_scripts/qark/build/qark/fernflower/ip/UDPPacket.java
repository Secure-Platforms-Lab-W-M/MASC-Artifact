package ip;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class UDPPacket extends IPPacket {
   private IntBuffer udpHeader;

   public UDPPacket(byte[] var1, int var2, int var3) {
      super(var1, var2, var3);
      this.udpHeader = ByteBuffer.wrap(var1, this.ipHdrlen + var2, 8).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
   }

   private int calculateCheckSum(boolean var1) {
      int var2;
      int var3;
      if (this.version == 4) {
         var3 = this.ipHeader.get(2);
         this.ipHeader.put(2, 1114112 + this.len - this.ipHdrlen);
         var2 = CheckSum.chkSum(this.data, this.offset + 8, this.len - 8);
         this.ipHeader.put(2, var3);
      } else {
         if (this.version != 6) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Illegal version:");
            var5.append(this.version);
            throw new IllegalStateException(var5.toString());
         }

         var3 = this.ipHeader.get(0);
         int var4 = this.ipHeader.get(1);
         this.ipHeader.put(0, this.len - this.ipHdrlen);
         this.ipHeader.put(1, 17);
         var2 = CheckSum.chkSum(this.data, this.offset, this.len);
         this.ipHeader.position(0);
         this.ipHeader.put(new int[]{var3, var4});
      }

      var3 = var2;
      if (var1) {
         var3 = var2;
         if (var2 == 0) {
            var3 = 65535;
         }
      }

      return var3;
   }

   public static UDPPacket createUDPPacket(byte[] var0, int var1, int var2, int var3) {
      var0[var1] = (byte)(var3 << 4 & 255);
      UDPPacket var4 = new UDPPacket(var0, var1, var2);
      var4.initInitialIPHeader();
      return var4;
   }

   public int checkCheckSum() {
      return this.calculateCheckSum(false);
   }

   public int getDestPort() {
      return this.udpHeader.get(0) & '\uffff';
   }

   public int getHeaderLength() {
      return this.ipHdrlen + 8;
   }

   public int getIPPacketLength() {
      return super.getLength();
   }

   public int getIPPacketOffset() {
      return super.getOffset();
   }

   public int getLength() {
      return this.udpHeader.get(1) >>> 16;
   }

   public int getOffset() {
      return super.getOffset() + this.ipHdrlen;
   }

   public int getSourcePort() {
      return this.udpHeader.get(0) >>> 16;
   }

   public void updateHeader(int var1, int var2) {
      int[] var3 = new int[]{(var1 << 16) + var2, this.len - this.ipHdrlen << 16};
      this.udpHeader.position(0);
      this.udpHeader.put(var3);
      var3[1] += this.calculateCheckSum(true);
      this.udpHeader.put(1, var3[1]);
   }
}
