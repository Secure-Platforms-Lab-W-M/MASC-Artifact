package net.sf.fmj.media.rtp;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import javax.media.rtp.rtcp.Feedback;

public class RTCPFeedback implements Feedback {
   public static final int SIZE = 24;
   private long dlsr = 0L;
   private int fractionLost = 0;
   private long jitter = 0L;
   private long lsr = 0L;
   private long numLost = 0L;
   private long ssrc = 0L;
   private long xtndSeqNum = 0L;

   public RTCPFeedback(byte[] var1, int var2, int var3) throws IOException {
      DataInputStream var4 = new DataInputStream(new ByteArrayInputStream(var1, var2, var3));
      this.ssrc = (long)var4.readInt() & 4294967295L;
      this.fractionLost = var4.readUnsignedByte();
      this.numLost = (long)(var4.readUnsignedShort() << 8 | var4.readUnsignedByte());
      this.xtndSeqNum = (long)var4.readInt() & 4294967295L;
      this.jitter = (long)var4.readInt() & 4294967295L;
      this.lsr = (long)var4.readInt() & 4294967295L;
      this.dlsr = (long)var4.readInt() & 4294967295L;
   }

   public long getDLSR() {
      return this.dlsr;
   }

   public int getFractionLost() {
      return this.fractionLost;
   }

   public long getJitter() {
      return this.jitter;
   }

   public long getLSR() {
      return this.lsr;
   }

   public long getNumLost() {
      return this.numLost;
   }

   public long getSSRC() {
      return this.ssrc;
   }

   public long getXtndSeqNum() {
      return this.xtndSeqNum;
   }
}
