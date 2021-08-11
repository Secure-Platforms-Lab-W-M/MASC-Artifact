package net.sf.fmj.media.control;

import javax.media.Control;

public interface AtomicControl extends Control {
   void addControlChangeListener(ControlChangeListener var1);

   boolean getEnabled();

   Control getParent();

   String getTip();

   boolean getVisible();

   boolean isDefault();

   boolean isReadOnly();

   void removeControlChangeListener(ControlChangeListener var1);

   void setEnabled(boolean var1);

   void setTip(String var1);

   void setVisible(boolean var1);
}
