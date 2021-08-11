package net.sf.fmj.media.control;

import javax.media.Control;
import org.atalk.android.util.java.awt.Component;

public class StringControlAdapter extends AtomicControlAdapter implements StringControl {
   String title;
   String value;

   public StringControlAdapter() {
      super((Component)null, true, (Control)null);
   }

   public StringControlAdapter(Component var1, boolean var2, Control var3) {
      super(var1, var2, var3);
   }

   public String getTitle() {
      return this.title;
   }

   public String getValue() {
      return this.value;
   }

   public String setTitle(String var1) {
      this.title = var1;
      this.informListeners();
      return var1;
   }

   public String setValue(String var1) {
      this.value = var1;
      this.informListeners();
      return var1;
   }
}
