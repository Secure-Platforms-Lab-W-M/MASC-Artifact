package net.sf.fmj.media.rtp;

import java.io.DataOutputStream;
import java.io.IOException;
import net.sf.fmj.media.rtp.util.Packet;

public abstract class RTCPPacket extends Packet {
   public static final int APP = 204;
   public static final int BYE = 203;
   public static final int COMPOUND = -1;
   // $FF: renamed from: RR int
   public static final int field_114 = 201;
   public static final int SDES = 202;
   // $FF: renamed from: SR int
   public static final int field_115 = 200;
   public Packet base;
   public int type;

   public RTCPPacket() {
   }

   public RTCPPacket(RTCPPacket var1) {
      super(var1);
      this.base = var1.base;
   }

   public RTCPPacket(Packet var1) {
      super(var1);
      this.base = var1;
   }

   public abstract void assemble(DataOutputStream var1) throws IOException;

   public abstract int calcLength();
}
