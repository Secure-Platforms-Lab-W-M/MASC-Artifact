package net.sf.fmj.media.control;

import javax.media.Control;
import org.atalk.android.util.java.awt.Component;

public class SliderRegionControlAdapter extends AtomicControlAdapter implements SliderRegionControl {
   boolean enable;
   long max;
   long min;

   public SliderRegionControlAdapter() {
      super((Component)null, true, (Control)null);
      this.enable = true;
   }

   public SliderRegionControlAdapter(Component var1, boolean var2, Control var3) {
      super(var1, var2, var3);
   }

   public long getMaxValue() {
      return this.max;
   }

   public long getMinValue() {
      return this.min;
   }

   public boolean isEnable() {
      return this.enable;
   }

   public void setEnable(boolean var1) {
      this.enable = var1;
   }

   public long setMaxValue(long var1) {
      this.max = var1;
      this.informListeners();
      return this.max;
   }

   public long setMinValue(long var1) {
      this.min = var1;
      this.informListeners();
      return this.min;
   }
}
