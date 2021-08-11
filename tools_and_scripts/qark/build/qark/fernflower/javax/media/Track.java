package javax.media;

public interface Track extends Duration {
   int FRAME_UNKNOWN = Integer.MAX_VALUE;
   Time TIME_UNKNOWN = Time.TIME_UNKNOWN;

   Format getFormat();

   Time getStartTime();

   boolean isEnabled();

   Time mapFrameToTime(int var1);

   int mapTimeToFrame(Time var1);

   void readFrame(Buffer var1);

   void setEnabled(boolean var1);

   void setTrackListener(TrackListener var1);
}
