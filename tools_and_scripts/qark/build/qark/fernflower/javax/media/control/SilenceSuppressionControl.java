package javax.media.control;

import javax.media.Control;

public interface SilenceSuppressionControl extends Control {
   boolean getSilenceSuppression();

   boolean isSilenceSuppressionSupported();

   boolean setSilenceSuppression(boolean var1);
}
