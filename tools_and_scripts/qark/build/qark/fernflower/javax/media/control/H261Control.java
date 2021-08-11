package javax.media.control;

import javax.media.Control;

public interface H261Control extends Control {
   boolean getStillImageTransmission();

   boolean isStillImageTransmissionSupported();

   boolean setStillImageTransmission(boolean var1);
}
