package net.sf.fmj.media.rtp;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.DatagramPacket;

public class RTPHeader {
   private static final int CSRC_MASK = 3840;
   private static final int CSRC_SHIFT = 8;
   private static final int EXTENSION_MASK = 4096;
   private static final int EXTENSION_SHIFT = 12;
   private static final int MARKER_MASK = 128;
   private static final int MARKER_SHIFT = 7;
   public static final int MAX_PAYLOAD = 127;
   public static final int MAX_SEQUENCE = 65535;
   private static final int PADDING_MASK = 8192;
   private static final int PADDING_SHIFT = 13;
   public static final int SIZE = 12;
   private static final int TYPE_MASK = 127;
   private static final int TYPE_SHIFT = 0;
   public static final long UINT_TO_LONG_CONVERT = 4294967295L;
   public static final int VERSION = 2;
   private static final int VERSION_MASK = 49152;
   private static final int VERSION_SHIFT = 14;
   private int flags;
   private int sequence;
   private long ssrc;
   private long timestamp;

   public RTPHeader(DatagramPacket var1) throws IOException {
      this(var1.getData(), var1.getOffset(), var1.getLength());
   }

   public RTPHeader(byte[] var1, int var2, int var3) throws IOException {
      DataInputStream var4 = new DataInputStream(new ByteArrayInputStream(var1, var2, var3));
      this.flags = var4.readUnsignedShort();
      this.sequence = var4.readUnsignedShort();
      this.timestamp = (long)var4.readInt() & 4294967295L;
      this.ssrc = (long)var4.readInt() & 4294967295L;
      if (this.getVersion() != 2) {
         throw new IOException("Invalid Version");
      }
   }

   short getCsrcCount() {
      return (short)((this.getFlags() & 3840) >> 8);
   }

   short getExtension() {
      return (short)((this.getFlags() & 4096) >> 12);
   }

   public int getFlags() {
      return this.flags;
   }

   short getMarker() {
      return (short)((this.getFlags() & 128) >> 7);
   }

   short getPacketType() {
      return (short)((this.getFlags() & 127) >> 0);
   }

   short getPadding() {
      return (short)((this.getFlags() & 8192) >> 13);
   }

   int getSequence() {
      return this.sequence;
   }

   int getSize() {
      return this.getCsrcCount() * 4 + 12;
   }

   long getSsrc() {
      return this.ssrc;
   }

   long getTimestamp() {
      return this.timestamp;
   }

   short getVersion() {
      return (short)((this.getFlags() & '쀀') >> 14);
   }

   public void print() {
      PrintStream var1 = System.err;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.getVersion());
      var2.append("|");
      var2.append(this.getPadding());
      var2.append("|");
      var2.append(this.getExtension());
      var2.append("|");
      var2.append(this.getCsrcCount());
      var2.append("|");
      var2.append(this.getMarker());
      var2.append("|");
      var2.append(this.getPacketType());
      var2.append("|");
      var2.append(this.getSequence());
      var2.append("|");
      var2.append(this.getTimestamp());
      var2.append("|");
      var2.append(this.getSsrc());
      var1.println(var2.toString());
   }
}
