package net.sf.fmj.media;

import javax.media.Buffer;
import javax.media.control.FrameGrabbingControl;
import javax.media.renderer.VideoRenderer;
import org.atalk.android.util.java.awt.Component;
import org.atalk.android.util.java.awt.Rectangle;

public abstract class AbstractVideoRenderer extends AbstractRenderer implements VideoRenderer, FrameGrabbingControl {
   private Rectangle bounds = null;
   private Buffer lastBuffer;

   protected abstract int doProcess(Buffer var1);

   public Rectangle getBounds() {
      return this.bounds;
   }

   public abstract Component getComponent();

   public Component getControlComponent() {
      return null;
   }

   public Buffer grabFrame() {
      return this.lastBuffer;
   }

   public final int process(Buffer var1) {
      this.lastBuffer = var1;
      return this.doProcess(var1);
   }

   public void setBounds(Rectangle var1) {
      this.bounds = var1;
   }

   public boolean setComponent(Component var1) {
      return false;
   }
}
