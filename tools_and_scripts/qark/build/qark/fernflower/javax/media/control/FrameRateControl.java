package javax.media.control;

import javax.media.Control;

public interface FrameRateControl extends Control {
   float getFrameRate();

   float getMaxSupportedFrameRate();

   float getPreferredFrameRate();

   float setFrameRate(float var1);
}
