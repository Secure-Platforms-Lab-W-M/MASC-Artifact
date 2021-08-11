package com.ibm.media.codec.audio;

import javax.media.Buffer;

public abstract class AudioPacketizer extends AudioCodec {
   protected byte[] history;
   protected int historyLength;
   protected int packetSize;
   protected int sample_count;

   public int process(Buffer var1, Buffer var2) {
      synchronized(this){}

      try {
         int var3 = var1.getLength();
         int var4 = this.packetSize;
         byte[] var7 = (byte[])((byte[])var1.getData());
         byte[] var8 = this.validateByteArraySize(var2, var4);
         if (this.historyLength + var3 >= this.packetSize) {
            int var5 = Math.min(this.historyLength, this.packetSize);
            System.arraycopy(this.history, 0, var8, 0, var5);
            int var6 = this.packetSize - var5;
            System.arraycopy(var7, var1.getOffset(), var8, this.historyLength, var6);
            this.historyLength -= var5;
            var1.setOffset(var1.getOffset() + var6);
            var1.setLength(var3 - var6);
            this.updateOutput(var2, this.outputFormat, var4, 0);
            return 2;
         }

         if (var1.isEOM()) {
            System.arraycopy(this.history, 0, var8, 0, this.historyLength);
            System.arraycopy(var7, var1.getOffset(), var8, this.historyLength, var3);
            this.updateOutput(var2, this.outputFormat, this.historyLength + var3, 0);
            this.historyLength = 0;
            return 0;
         }

         System.arraycopy(var7, var1.getOffset(), this.history, this.historyLength, var3);
         this.historyLength += var3;
      } finally {
         ;
      }

      return 4;
   }

   public void reset() {
      this.historyLength = 0;
   }
}
