package net.sf.fmj.media.rtp.util;

import javax.media.Buffer;

public class RTPPacket extends Packet {
   public Packet base;
   public int[] csrc;
   public byte[] extension;
   public boolean extensionPresent;
   public int extensionType;
   public Buffer.RTPHeaderExtension headerExtension;
   public int marker;
   public int payloadType;
   public int payloadlength;
   public int payloadoffset;
   public int seqnum;
   public int ssrc;
   public long timestamp;

   public RTPPacket() {
   }

   public RTPPacket(Packet var1) {
      super(var1);
      this.base = var1;
   }

   public void assemble(int param1, boolean param2) {
      // $FF: Couldn't be decompiled
   }

   public int calcLength() {
      int var1 = 0;
      Buffer.RTPHeaderExtension var2 = this.headerExtension;
      if (var2 != null) {
         var1 = ((var2.value.length + 3) / 4 + 1) * 4;
      }

      return var1 + 12 + this.payloadlength;
   }

   public RTPPacket clone() {
      RTPPacket var1 = new RTPPacket(this.base.clone());
      var1.extensionPresent = this.extensionPresent;
      var1.marker = this.marker;
      var1.payloadType = this.payloadType;
      var1.seqnum = this.seqnum;
      var1.timestamp = this.timestamp;
      var1.ssrc = this.ssrc;
      var1.csrc = (int[])this.csrc.clone();
      var1.extensionType = this.extensionType;
      var1.extension = this.extension;
      var1.payloadoffset = this.payloadoffset;
      var1.payloadlength = this.payloadlength;
      var1.headerExtension = this.headerExtension;
      return var1;
   }

   public String toString() {
      StringBuilder var2 = new StringBuilder();
      var2.append("RTP Packet:\n\tPayload Type: ");
      var2.append(this.payloadType);
      var2.append("    Marker: ");
      var2.append(this.marker);
      var2.append("\n\tSequence Number: ");
      var2.append(this.seqnum);
      var2.append("\n\tTimestamp: ");
      var2.append(this.timestamp);
      var2.append("\n\tSSRC (Sync Source): ");
      var2.append(this.ssrc);
      var2.append("\n\tPayload Length: ");
      var2.append(this.payloadlength);
      var2.append("    Payload Offset: ");
      var2.append(this.payloadoffset);
      var2.append("\n");
      String var3 = var2.toString();
      String var4 = var3;
      StringBuilder var5;
      if (this.csrc.length > 0) {
         var2 = new StringBuilder();
         var2.append(var3);
         var2.append("Contributing sources:  ");
         var2.append(this.csrc[0]);
         var4 = var2.toString();

         for(int var1 = 1; var1 < this.csrc.length; ++var1) {
            var5 = new StringBuilder();
            var5.append(var4);
            var5.append(", ");
            var5.append(this.csrc[var1]);
            var4 = var5.toString();
         }

         var5 = new StringBuilder();
         var5.append(var4);
         var5.append("\n");
         var4 = var5.toString();
      }

      var3 = var4;
      if (this.extensionPresent) {
         var5 = new StringBuilder();
         var5.append(var4);
         var5.append("\tExtension:  type ");
         var5.append(this.extensionType);
         var5.append(", length ");
         var5.append(this.extension.length);
         var5.append("\n");
         var3 = var5.toString();
      }

      return var3;
   }
}
