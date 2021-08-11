package com.sun.media.controls;

import javax.media.Codec;
import javax.media.control.PacketSizeControl;
import org.atalk.android.util.java.awt.Component;

public class PacketSizeAdapter implements PacketSizeControl {
   protected Codec owner;
   protected int packetSize;
   protected boolean settable;

   public PacketSizeAdapter(Codec var1, int var2, boolean var3) {
      this.owner = var1;
      this.packetSize = var2;
      this.settable = var3;
   }

   public Component getControlComponent() {
      throw new UnsupportedOperationException();
   }

   public int getPacketSize() {
      return this.packetSize;
   }

   public int setPacketSize(int var1) {
      throw new UnsupportedOperationException();
   }
}
