package com.sun.media.controls;

import com.sun.media.ui.TextComp;
import javax.media.control.KeyFrameControl;
import org.atalk.android.util.java.awt.Component;

public class KeyFrameAdapter implements KeyFrameControl {
   private int preferred;
   private boolean settable;
   private final TextComp textComp = new TextComp();
   private int value;

   public KeyFrameAdapter(int var1, boolean var2) {
      this.preferred = var1;
      this.settable = var2;
   }

   public Component getControlComponent() {
      throw new UnsupportedOperationException();
   }

   public int getKeyFrameInterval() {
      return this.value;
   }

   public int getPreferredKeyFrameInterval() {
      return this.preferred;
   }

   public int setKeyFrameInterval(int var1) {
      throw new UnsupportedOperationException();
   }
}
