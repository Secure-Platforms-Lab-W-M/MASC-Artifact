package net.sf.fmj.ejmf.toolkit.controls;

import javax.media.Control;
import javax.media.Controller;
import org.atalk.android.util.java.awt.Component;

public class RateControl implements Control {
   private Component controlComponent;
   private Controller controller;

   public RateControl(Controller var1) {
      this.controller = var1;
   }

   public Component getControlComponent() {
      if (this.controlComponent == null) {
         this.controlComponent = new RateControlComponent(this.controller);
      }

      return this.controlComponent;
   }
}
