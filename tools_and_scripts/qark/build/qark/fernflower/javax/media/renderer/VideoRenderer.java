package javax.media.renderer;

import javax.media.Renderer;
import org.atalk.android.util.java.awt.Component;
import org.atalk.android.util.java.awt.Rectangle;

public interface VideoRenderer extends Renderer {
   Rectangle getBounds();

   Component getComponent();

   void setBounds(Rectangle var1);

   boolean setComponent(Component var1);
}
