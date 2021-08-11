package net.sf.fmj.media.rtp;

import java.io.DataOutputStream;
import java.io.IOException;
import net.sf.fmj.media.rtp.util.Packet;
import net.sf.fmj.utility.ByteBufferOutputStream;

public class RTCPCompoundPacket extends RTCPPacket {
   public RTCPPacket[] packets;

   public RTCPCompoundPacket(Packet var1) {
      super(var1);
      super.type = -1;
   }

   public RTCPCompoundPacket(RTCPPacket[] var1) {
      this.packets = var1;
      super.type = -1;
      super.received = false;
   }

   public void assemble(int var1, boolean var2) {
      this.length = var1;
      this.offset = 0;
      byte[] var7 = new byte[var1];
      ByteBufferOutputStream var8 = new ByteBufferOutputStream(var7, 0, var1);
      DataOutputStream var9 = new DataOutputStream(var8);
      boolean var10001;
      if (var2) {
         try {
            this.offset += 4;
         } catch (IOException var12) {
            var10001 = false;
            throw new NullPointerException("Impossible IO Exception");
         }
      }

      int var3;
      try {
         var3 = this.offset;
      } catch (IOException var11) {
         var10001 = false;
         throw new NullPointerException("Impossible IO Exception");
      }

      int var4 = 0;

      while(true) {
         try {
            if (var4 >= this.packets.length) {
               break;
            }

            var3 = var8.size();
            this.packets[var4].assemble(var9);
         } catch (IOException var10) {
            var10001 = false;
            throw new NullPointerException("Impossible IO Exception");
         }

         ++var4;
      }

      var4 = var8.size();
      super.data = var7;
      if (var4 <= var1) {
         if (var4 < var1) {
            if (this.data.length < var1) {
               var7 = this.data;
               byte[] var13 = new byte[var1];
               this.data = var13;
               System.arraycopy(var7, 0, var13, 0, var4);
            }

            var7 = this.data;
            var7[var3] = (byte)(var7[var3] | 32);
            this.data[var1 - 1] = (byte)(var1 - var4);
            int var5 = (this.data[var3 + 3] & 255) + (var1 - var4 >> 2);
            if (var5 >= 256) {
               var7 = this.data;
               int var6 = var3 + 2;
               var7[var6] = (byte)(var7[var6] + (var1 - var4 >> 10));
            }

            this.data[var3 + 3] = (byte)var5;
         }

      } else {
         throw new NullPointerException("RTCP Packet overflow");
      }
   }

   public void assemble(DataOutputStream var1) throws IOException {
      throw new IllegalArgumentException("Recursive Compound Packet");
   }

   public int calcLength() {
      RTCPPacket[] var3 = this.packets;
      if (var3 != null && var3.length >= 1) {
         int var2 = 0;
         int var1 = 0;

         while(true) {
            var3 = this.packets;
            if (var1 >= var3.length) {
               return var2;
            }

            var2 += var3[var1].calcLength();
            ++var1;
         }
      } else {
         throw new IllegalArgumentException("Bad RTCP Compound Packet");
      }
   }

   public String toString() {
      StringBuilder var1 = new StringBuilder();
      var1.append("RTCP Packet with the following subpackets:\n");
      var1.append(this.toString(this.packets));
      return var1.toString();
   }

   public String toString(RTCPPacket[] var1) {
      String var3 = "";

      for(int var2 = 0; var2 < var1.length; ++var2) {
         StringBuilder var4 = new StringBuilder();
         var4.append(var3);
         var4.append(var1[var2]);
         var3 = var4.toString();
      }

      return var3;
   }
}
