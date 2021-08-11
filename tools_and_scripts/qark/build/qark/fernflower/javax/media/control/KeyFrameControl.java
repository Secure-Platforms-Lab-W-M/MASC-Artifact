package javax.media.control;

import javax.media.Control;

public interface KeyFrameControl extends Control {
   int getKeyFrameInterval();

   int getPreferredKeyFrameInterval();

   int setKeyFrameInterval(int var1);
}
