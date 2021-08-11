package javax.media.control;

import javax.media.Control;

public interface FrameProcessingControl extends Control {
   int getFramesDropped();

   void setFramesBehind(float var1);

   boolean setMinimalProcessing(boolean var1);
}
