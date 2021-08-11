package com.sun.media.controls;

import javax.media.control.QualityControl;
import org.atalk.android.util.java.awt.Component;

public class QualityAdapter implements QualityControl {
   protected boolean isTSsupported;
   protected float maxValue;
   protected float minValue;
   protected float preferredValue;
   protected boolean settable;
   protected float value;

   public QualityAdapter(float var1, float var2, float var3, boolean var4) {
      this.preferredValue = var1;
      this.minValue = var2;
      this.maxValue = var3;
      this.settable = var4;
   }

   public QualityAdapter(float var1, float var2, float var3, boolean var4, boolean var5) {
      this.preferredValue = var1;
      this.minValue = var2;
      this.maxValue = var3;
      this.isTSsupported = var4;
      this.settable = var5;
   }

   public Component getControlComponent() {
      throw new UnsupportedOperationException();
   }

   public float getPreferredQuality() {
      throw new UnsupportedOperationException();
   }

   public float getQuality() {
      throw new UnsupportedOperationException();
   }

   public boolean isTemporalSpatialTradeoffSupported() {
      throw new UnsupportedOperationException();
   }

   public float setQuality(float var1) {
      throw new UnsupportedOperationException();
   }
}
