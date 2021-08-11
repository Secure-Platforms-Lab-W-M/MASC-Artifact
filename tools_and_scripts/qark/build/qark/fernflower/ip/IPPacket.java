package ip;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

public class IPPacket {
   static Object ID_SYNC = new Object();
   static short curID = (short)((int)(Math.random() * 32767.0D));
   protected byte[] data;
   protected int ipHdrlen;
   protected IntBuffer ipHeader;
   protected int len;
   protected int offset;
   protected int version = 0;

   public IPPacket(byte[] var1, int var2, int var3) {
      this.version = var1[var2] >> 4;
      this.data = var1;
      this.offset = var2;
      this.len = var3;
      if (this.version == 4) {
         this.ipHeader = ByteBuffer.wrap(var1, var2, 20).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
         this.ipHdrlen = 20;
      } else if (this.version == 6) {
         this.ipHeader = ByteBuffer.wrap(var1, var2, 40).order(ByteOrder.BIG_ENDIAN).asIntBuffer();
         this.ipHdrlen = 40;
      } else {
         StringBuilder var4 = new StringBuilder();
         var4.append("Invalid Version:");
         var4.append(this.version);
         throw new IllegalArgumentException(var4.toString());
      }
   }

   private int calculateCheckSum() {
      return this.version == 4 ? CheckSum.chkSum(this.data, this.offset, 20) : 0;
   }

   private int[] copyFromHeader(int var1, int var2) {
      this.ipHeader.position(var1);
      int[] var3 = new int[var2];
      this.ipHeader.get(var3, 0, var2);
      return var3;
   }

   public static IPPacket createIPPacket(byte[] var0, int var1, int var2, int var3, int var4, int var5, int[] var6, int[] var7) {
      var0[var1] = (byte)(var3 << 4 & 255);
      IPPacket var8 = new IPPacket(var0, var1, var2);
      var8.initIPHeader(var4, var5, var6, var7);
      return var8;
   }

   public static IPPacket createInitialIPPacket(byte[] var0, int var1, int var2, int var3) {
      var0[var1] = (byte)(var3 << 4 & 255);
      IPPacket var4 = new IPPacket(var0, var1, var2);
      var4.initInitialIPHeader();
      return var4;
   }

   private static int generateId() {
      // $FF: Couldn't be decompiled
   }

   public static InetAddress int2ip(int[] var0) throws UnknownHostException {
      byte[] var2;
      if (var0.length == 1) {
         byte[] var1 = new byte[]{(byte)(var0[0] >> 24 & 255), (byte)(var0[0] >> 16 & 255), (byte)(var0[0] >> 8 & 255), (byte)(var0[0] & 255)};
         var2 = var1;
      } else {
         if (var0.length != 4) {
            StringBuilder var3 = new StringBuilder();
            var3.append("Invalid array length:");
            var3.append(var0.length);
            throw new IllegalArgumentException(var3.toString());
         }

         var2 = new byte[]{(byte)(var0[0] >> 24 & 255), (byte)(var0[0] >> 16 & 255), (byte)(var0[0] >> 8 & 255), (byte)(var0[0] & 255), (byte)(var0[1] >> 24 & 255), (byte)(var0[1] >> 16 & 255), (byte)(var0[1] >> 8 & 255), (byte)(var0[1] & 255), (byte)(var0[2] >> 24 & 255), (byte)(var0[2] >> 16 & 255), (byte)(var0[2] >> 8 & 255), (byte)(var0[2] & 255), (byte)(var0[3] >> 24 & 255), (byte)(var0[3] >> 16 & 255), (byte)(var0[3] >> 8 & 255), (byte)(var0[3] & 255)};
      }

      return InetAddress.getByAddress(var2);
   }

   public static int[] ip2int(InetAddress var0) {
      byte[] var8 = var0.getAddress();
      byte var1;
      if (var8.length == 4) {
         var1 = var8[3];
         return new int[]{(var8[2] & 255) << 8 | var1 & 255 | (var8[1] & 255) << 16 | (var8[0] & 255) << 24};
      } else {
         var1 = var8[3];
         byte var2 = var8[2];
         byte var3 = var8[1];
         byte var4 = var8[0];
         byte var5 = var8[7];
         byte var6 = var8[6];
         byte var7 = var8[5];
         return new int[]{var1 & 255 | (var2 & 255) << 8 | (var3 & 255) << 16 | (var4 & 255) << 24, (var8[4] & 255) << 24 | var5 & 255 | (var6 & 255) << 8 | (var7 & 255) << 16, var8[11] & 255 | (var8[10] & 255) << 8 | (var8[9] & 255) << 16 | (var8[8] & 255) << 24, var8[15] & 255 | (var8[14] & 255) << 8 | (var8[13] & 255) << 16 | (var8[12] & 255) << 24};
      }
   }

   public int checkCheckSum() {
      return this.calculateCheckSum();
   }

   public byte[] getData() {
      return this.data;
   }

   public int[] getDestIP() {
      if (this.version == 4) {
         return this.copyFromHeader(4, 1);
      } else if (this.version == 6) {
         return this.copyFromHeader(6, 4);
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Illegal Version:");
         var1.append(this.version);
         throw new IllegalStateException(var1.toString());
      }
   }

   public int getHeaderLength() {
      return this.ipHdrlen;
   }

   public int getLength() {
      if (this.version == 4) {
         return this.ipHeader.get(0) & '\uffff';
      } else if (this.version == 6) {
         return 40 + (this.ipHeader.get(1) >>> 16);
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Illegal Version:");
         var1.append(this.version);
         throw new IllegalStateException(var1.toString());
      }
   }

   public int getOffset() {
      return this.offset;
   }

   public int getProt() {
      if (this.version == 4) {
         return this.ipHeader.get(2) >>> 16 & 255;
      } else if (this.version == 6) {
         return this.ipHeader.get(1) >>> 8 & 255;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Illegal Version:");
         var1.append(this.version);
         throw new IllegalStateException(var1.toString());
      }
   }

   public int[] getSourceIP() {
      if (this.version == 4) {
         return this.copyFromHeader(3, 1);
      } else if (this.version == 6) {
         return this.copyFromHeader(2, 4);
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Illegal Version:");
         var1.append(this.version);
         throw new IllegalStateException(var1.toString());
      }
   }

   public int getTTL() {
      if (this.version == 4) {
         return this.ipHeader.get(2) >>> 24;
      } else if (this.version == 6) {
         return this.ipHeader.get(1) & 255;
      } else {
         StringBuilder var1 = new StringBuilder();
         var1.append("Illegal Version:");
         var1.append(this.version);
         throw new IllegalStateException(var1.toString());
      }
   }

   public int getVersion() {
      return this.version;
   }

   protected void initIPHeader(int var1, int var2, int[] var3, int[] var4) {
      if (this.version == 4) {
         int[] var7 = new int[]{1157627904 + this.len, generateId(), (var1 << 24) + (var2 << 16), var3[0], var4[0]};
         this.ipHeader.position(0);
         this.ipHeader.put(var7);
         var7[2] += this.calculateCheckSum();
         this.ipHeader.put(2, var7[2]);
      } else if (this.version == 6) {
         int var5 = this.version;
         int var6 = this.len;
         this.ipHeader.position(0);
         this.ipHeader.put(new int[]{var5 << 28, (var6 - 40 << 16) + (var2 << 8) + var1});
         this.ipHeader.put(var3);
         this.ipHeader.put(var4);
      } else {
         StringBuilder var8 = new StringBuilder();
         var8.append("Illegal Version:");
         var8.append(this.version);
         throw new IllegalStateException(var8.toString());
      }
   }

   protected void initInitialIPHeader() {
      int var1;
      int var2;
      if (this.version == 4) {
         var1 = this.len;
         var2 = generateId();
         this.ipHeader.position(0);
         this.ipHeader.put(new int[]{1157627904 + var1, var2, 0, 0, 0});
      } else if (this.version == 6) {
         var1 = this.version;
         var2 = this.len;
         this.ipHeader.position(0);
         this.ipHeader.put(new int[]{var1 << 28, var2 - 40 << 16});
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Illegal Version:");
         var3.append(this.version);
         throw new IllegalStateException(var3.toString());
      }
   }

   public void updateHeader(int var1, int var2, int[] var3, int[] var4) {
      if (this.version == 4) {
         int[] var6 = new int[3];
         this.ipHeader.position(2);
         this.ipHeader.get(var6);
         var6[0] = (var1 << 24) + (var2 << 16);
         var6[1] = var3[0];
         var6[2] = var4[0];
         this.ipHeader.position(2);
         this.ipHeader.put(var6);
         var6[0] += this.calculateCheckSum();
         this.ipHeader.put(2, var6[0]);
      } else if (this.version == 6) {
         int var5 = this.ipHeader.get(1);
         this.ipHeader.position(1);
         this.ipHeader.put((-65536 & var5) + (var2 << 8) + var1);
         this.ipHeader.put(var3);
         this.ipHeader.put(var4);
      } else {
         StringBuilder var7 = new StringBuilder();
         var7.append("Illegal Version:");
         var7.append(this.version);
         throw new IllegalStateException(var7.toString());
      }
   }
}
