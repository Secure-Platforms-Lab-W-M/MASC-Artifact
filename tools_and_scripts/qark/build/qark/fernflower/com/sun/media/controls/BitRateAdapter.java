package com.sun.media.controls;

import com.sun.media.ui.TextComp;
import javax.media.control.BitRateControl;
import org.atalk.android.util.java.awt.Component;
import org.atalk.android.util.java.awt.event.ActionEvent;
import org.atalk.android.util.java.awt.event.ActionListener;

public class BitRateAdapter implements BitRateControl, ActionListener {
   protected int max;
   protected int min;
   protected boolean settable;
   protected final TextComp textComp;
   protected int value;

   public BitRateAdapter(int var1, int var2, int var3, boolean var4) {
      this.value = var1;
      this.min = var2;
      this.max = var3;
      this.settable = var4;
      this.textComp = new TextComp();
   }

   public void actionPerformed(ActionEvent var1) {
      throw new UnsupportedOperationException();
   }

   public int getBitRate() {
      return this.value;
   }

   public Component getControlComponent() {
      throw new UnsupportedOperationException();
   }

   public int getMaxSupportedBitRate() {
      return this.max;
   }

   public int getMinSupportedBitRate() {
      return this.min;
   }

   public int setBitRate(int var1) {
      throw new UnsupportedOperationException();
   }
}
