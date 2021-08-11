package net.sf.fmj.media.control;

import java.util.Vector;
import javax.media.Control;
import org.atalk.android.util.java.awt.Component;

public class AtomicControlAdapter implements AtomicControl {
   protected Component component = null;
   protected boolean enabled = true;
   protected boolean isdefault = false;
   private Vector listeners = null;
   protected Control parent = null;

   public AtomicControlAdapter(Component var1, boolean var2, Control var3) {
      this.component = var1;
      this.isdefault = var2;
      this.parent = var3;
   }

   public void addControlChangeListener(ControlChangeListener var1) {
      if (this.listeners == null) {
         this.listeners = new Vector();
      }

      if (var1 != null) {
         this.listeners.addElement(var1);
      }

   }

   public Component getControlComponent() {
      return this.component;
   }

   public boolean getEnabled() {
      return this.enabled;
   }

   public Control getParent() {
      return this.parent;
   }

   public String getTip() {
      return null;
   }

   public boolean getVisible() {
      return true;
   }

   public void informListeners() {
      if (this.listeners != null) {
         for(int var1 = 0; var1 < this.listeners.size(); ++var1) {
            ((ControlChangeListener)this.listeners.elementAt(var1)).controlChanged(new ControlChangeEvent(this));
         }
      }

   }

   public boolean isDefault() {
      return this.isdefault;
   }

   public boolean isReadOnly() {
      return false;
   }

   public void removeControlChangeListener(ControlChangeListener var1) {
      Vector var2 = this.listeners;
      if (var2 != null && var1 != null) {
         var2.removeElement(var1);
      }

   }

   public void setEnabled(boolean var1) {
      this.enabled = var1;
      Component var2 = this.component;
      if (var2 != null) {
         var2.setEnabled(var1);
      }

      this.informListeners();
   }

   public void setParent(Control var1) {
      this.parent = var1;
   }

   public void setTip(String var1) {
   }

   public void setVisible(boolean var1) {
   }
}
