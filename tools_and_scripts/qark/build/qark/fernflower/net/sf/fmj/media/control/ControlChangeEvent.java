package net.sf.fmj.media.control;

import javax.media.Control;

public class ControlChangeEvent {
   // $FF: renamed from: c javax.media.Control
   private Control field_110;

   public ControlChangeEvent(Control var1) {
      this.field_110 = var1;
   }

   public Control getControl() {
      return this.field_110;
   }
}
