package net.sf.fmj.media;

import javax.media.Buffer;
import javax.media.Duration;
import javax.media.Format;
import javax.media.Time;
import javax.media.Track;
import javax.media.TrackListener;

public abstract class AbstractTrack implements Track {
   private boolean enabled = true;

   public Time getDuration() {
      return Duration.DURATION_UNKNOWN;
   }

   public abstract Format getFormat();

   public Time getStartTime() {
      return TIME_UNKNOWN;
   }

   public boolean isEnabled() {
      return this.enabled;
   }

   public Time mapFrameToTime(int var1) {
      return TIME_UNKNOWN;
   }

   public int mapTimeToFrame(Time var1) {
      return Integer.MAX_VALUE;
   }

   public abstract void readFrame(Buffer var1);

   public void setEnabled(boolean var1) {
      this.enabled = var1;
   }

   public void setTrackListener(TrackListener var1) {
   }
}
