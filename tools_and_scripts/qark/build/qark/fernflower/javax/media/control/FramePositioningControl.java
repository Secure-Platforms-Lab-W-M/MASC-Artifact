package javax.media.control;

import javax.media.Control;
import javax.media.Time;

public interface FramePositioningControl extends Control {
   int FRAME_UNKNOWN = Integer.MAX_VALUE;
   Time TIME_UNKNOWN = Time.TIME_UNKNOWN;

   Time mapFrameToTime(int var1);

   int mapTimeToFrame(Time var1);

   int seek(int var1);

   int skip(int var1);
}
