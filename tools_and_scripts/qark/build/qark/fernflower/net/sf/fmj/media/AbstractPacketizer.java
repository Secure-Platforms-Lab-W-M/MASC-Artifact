package net.sf.fmj.media;

import javax.media.Buffer;
import javax.media.Owned;
import javax.media.control.PacketSizeControl;
import org.atalk.android.util.java.awt.Component;

public abstract class AbstractPacketizer extends AbstractCodec {
   private static final boolean TRACE = false;
   private int bytesInPacketBuffer = 0;
   private boolean doNotSpanInputBuffers = false;
   private byte[] packetBuffer;
   private int packetSize;

   public AbstractPacketizer() {
      this.addControl(new AbstractPacketizer.PSC());
   }

   protected int doBuildPacketHeader(Buffer var1, byte[] var2) {
      return 0;
   }

   public int process(Buffer var1, Buffer var2) {
      boolean var6 = this.checkInputBuffer(var1);
      boolean var4 = true;
      if (!var6) {
         return 1;
      } else if (this.isEOM(var1)) {
         this.propagateEOM(var2);
         return 0;
      } else {
         int var3;
         if (this.bytesInPacketBuffer == 0) {
            var3 = this.doBuildPacketHeader(var1, this.packetBuffer);
            this.bytesInPacketBuffer += var3;
         }

         var3 = this.packetSize - this.bytesInPacketBuffer;
         int var5 = var1.getLength();
         if (var3 >= var5) {
            var3 = var5;
         }

         System.arraycopy(var1.getData(), var1.getOffset(), this.packetBuffer, this.bytesInPacketBuffer, var3);
         this.bytesInPacketBuffer += var3;
         var1.setOffset(var1.getOffset() + var3);
         var1.setLength(var1.getLength() - var3);
         boolean var7;
         if ((!this.doNotSpanInputBuffers || var1.getLength() != 0) && this.bytesInPacketBuffer != this.packetSize) {
            var7 = false;
         } else {
            var7 = var4;
         }

         if (var7) {
            var2.setData(this.packetBuffer);
            var2.setOffset(0);
            var2.setLength(this.bytesInPacketBuffer);
            this.bytesInPacketBuffer = 0;
            return var1.getLength() == 0 ? 0 : 2;
         } else {
            return 4;
         }
      }
   }

   protected void setDoNotSpanInputBuffers(boolean var1) {
      this.doNotSpanInputBuffers = var1;
   }

   protected void setPacketSize(int var1) {
      this.setPacketSizeImpl(var1);
   }

   protected void setPacketSizeImpl(int var1) {
      this.packetSize = var1;
      this.packetBuffer = new byte[var1];
   }

   private class PSC implements PacketSizeControl, Owned {
      private PSC() {
      }

      // $FF: synthetic method
      PSC(Object var2) {
         this();
      }

      public Component getControlComponent() {
         return null;
      }

      public Object getOwner() {
         return AbstractPacketizer.this;
      }

      public int getPacketSize() {
         return AbstractPacketizer.this.packetSize;
      }

      public int setPacketSize(int var1) {
         AbstractPacketizer.this.setPacketSizeImpl(var1);
         return AbstractPacketizer.this.packetSize;
      }
   }
}
