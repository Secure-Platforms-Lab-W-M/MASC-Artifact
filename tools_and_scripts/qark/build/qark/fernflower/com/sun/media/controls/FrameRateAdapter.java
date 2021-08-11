package com.sun.media.controls;

import com.sun.media.ui.TextComp;
import javax.media.control.FrameRateControl;
import org.atalk.android.util.java.awt.Component;

public class FrameRateAdapter implements FrameRateControl {
   protected float max;
   protected float min;
   protected Object owner;
   protected boolean settable;
   protected final TextComp textComp = new TextComp();
   protected float value;

   public FrameRateAdapter(float var1, float var2, float var3, boolean var4) {
      this.value = var1;
      this.min = var2;
      this.max = var3;
      this.settable = var4;
   }

   public FrameRateAdapter(Object var1, float var2, float var3, float var4, boolean var5) {
      this.owner = var1;
      this.value = var2;
      this.min = var3;
      this.max = var4;
      this.settable = var5;
   }

   public Component getControlComponent() {
      throw new UnsupportedOperationException();
   }

   public float getFrameRate() {
      return this.value;
   }

   public float getMaxSupportedFrameRate() {
      return this.max;
   }

   public Object getOwner() {
      return this.owner;
   }

   public float getPreferredFrameRate() {
      throw new UnsupportedOperationException();
   }

   public float setFrameRate(float var1) {
      throw new UnsupportedOperationException();
   }

   public void setOwner(Object var1) {
      this.owner = var1;
   }
}
