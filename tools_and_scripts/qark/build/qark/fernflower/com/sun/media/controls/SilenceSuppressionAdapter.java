package com.sun.media.controls;

import javax.media.Codec;
import javax.media.control.SilenceSuppressionControl;
import org.atalk.android.util.java.awt.Component;

public class SilenceSuppressionAdapter implements SilenceSuppressionControl {
   String CONTROL_STRING;
   Component component;
   protected boolean isSetable;
   protected Codec owner;
   protected boolean silenceSuppression;

   public SilenceSuppressionAdapter(Codec var1, boolean var2, boolean var3) {
      this.owner = var1;
      this.silenceSuppression = var2;
      this.isSetable = var3;
   }

   public Component getControlComponent() {
      return this.component;
   }

   public boolean getSilenceSuppression() {
      return this.silenceSuppression;
   }

   public boolean isSilenceSuppressionSupported() {
      return this.isSetable;
   }

   public boolean setSilenceSuppression(boolean var1) {
      if (this.isSetable) {
         this.silenceSuppression = var1;
      }

      return this.silenceSuppression;
   }
}
