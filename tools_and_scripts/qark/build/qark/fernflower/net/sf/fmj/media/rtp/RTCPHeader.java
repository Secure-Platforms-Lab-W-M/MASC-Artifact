package net.sf.fmj.media.rtp;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;

public class RTCPHeader {
   private static final int PADDING_MASK = 8192;
   private static final int PADDING_SHIFT = 13;
   private static final int RCOUNT_MASK = 7936;
   private static final int RCOUNT_SHIFT = 8;
   public static final int SDES_CNAME = 1;
   public static final int SDES_EMAIL = 3;
   public static final int SDES_LOC = 5;
   public static final int SDES_NAME = 2;
   public static final int SDES_NOTE = 7;
   public static final int SDES_PHONE = 4;
   public static final int SDES_SKIP = 8;
   public static final int SDES_TOOL = 6;
   public static final int SIZE = 8;
   private static final int TYPE_MASK = 255;
   private static final int TYPE_SHIFT = 0;
   public static final int VERSION = 2;
   private static final int VERSION_MASK = 49152;
   private static final int VERSION_SHIFT = 14;
   private int flags;
   private int length;
   private long ssrc;

   public RTCPHeader(DatagramPacket var1) throws IOException {
      this(var1.getData(), var1.getOffset(), var1.getLength());
   }

   public RTCPHeader(byte[] var1, int var2, int var3) throws IOException {
      DataInputStream var4 = new DataInputStream(new ByteArrayInputStream(var1, var2, var3));
      this.flags = var4.readUnsignedShort();
      this.length = var4.readUnsignedShort();
      this.ssrc = (long)var4.readInt() & 4294967295L;
      if (this.getVersion() == 2) {
         if (this.getLength() > var3) {
            throw new IOException("Invalid Length");
         }
      } else {
         throw new IOException("Invalid RTCP Version");
      }
   }

   public int getFlags() {
      return this.flags;
   }

   public int getLength() {
      return this.length;
   }

   public short getPacketType() {
      return (short)((this.getFlags() & 255) >> 0);
   }

   public short getPadding() {
      return (short)((this.getFlags() & 8192) >> 13);
   }

   public short getReceptionCount() {
      return (short)((this.getFlags() & 7936) >> 8);
   }

   public long getSsrc() {
      return this.ssrc;
   }

   public short getVersion() {
      return (short)((this.getFlags() & '쀀') >> 14);
   }

   public void print() {
      PrintStream var1 = System.err;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getVersion());
      var2.append("|");
      var2.append(this.getPadding());
      var2.append("|");
      var2.append(this.getReceptionCount());
      var2.append("|");
      var2.append(this.getPacketType());
      var2.append("|");
      var2.append(this.getLength());
      var2.append("|");
      var2.append(this.getSsrc());
      var1.println(var2.toString());
   }
}
