package javax.media;

import org.atalk.android.util.java.awt.Component;

public interface Player extends MediaHandler, Controller {
   void addController(Controller var1) throws IncompatibleTimeBaseException;

   Component getControlPanelComponent();

   GainControl getGainControl();

   Component getVisualComponent();

   void removeController(Controller var1);

   void start();
}
