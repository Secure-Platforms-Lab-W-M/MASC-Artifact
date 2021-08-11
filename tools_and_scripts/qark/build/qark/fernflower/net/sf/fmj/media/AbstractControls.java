package net.sf.fmj.media;

import javax.media.Control;
import javax.media.Controls;
import net.sf.fmj.utility.ControlCollection;

public abstract class AbstractControls implements Controls {
   private final ControlCollection controls = new ControlCollection();

   protected void addControl(Control var1) {
      this.controls.addControl(var1);
   }

   public Object getControl(String var1) {
      return this.controls.getControl(var1);
   }

   public Object[] getControls() {
      return this.controls.getControls();
   }

   protected void removeControl(Control var1) {
      this.controls.removeControl(var1);
   }
}
