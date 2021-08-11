package net.sf.fmj.media;

import java.io.IOException;
import javax.media.GainControl;
import javax.media.IncompatibleSourceException;
import javax.media.NotRealizedError;
import javax.media.Time;
import javax.media.TimeBase;
import javax.media.protocol.DataSource;
import net.sf.fmj.media.control.ProgressControl;
import org.atalk.android.util.java.awt.Component;

public class MediaPlayer extends BasicPlayer {
   protected PlaybackEngine engine = new PlaybackEngine(this);

   protected boolean audioEnabled() {
      return this.engine.audioEnabled();
   }

   public GainControl getGainControl() {
      if (this.getState() < 300) {
         this.throwError(new NotRealizedError("Cannot get gain control on an unrealized player"));
      }

      return this.engine.getGainControl();
   }

   protected TimeBase getMasterTimeBase() {
      return this.engine.getTimeBase();
   }

   public long getMediaNanoseconds() {
      return this.controllerList.size() > 1 ? super.getMediaNanoseconds() : this.engine.getMediaNanoseconds();
   }

   public Time getMediaTime() {
      return this.controllerList.size() > 1 ? super.getMediaTime() : this.engine.getMediaTime();
   }

   public Component getVisualComponent() {
      super.getVisualComponent();
      return this.engine.getVisualComponent();
   }

   public void setProgressControl(ProgressControl var1) {
      this.engine.setProgressControl(var1);
   }

   public void setSource(DataSource var1) throws IOException, IncompatibleSourceException {
      this.engine.setSource(var1);
      this.manageController(this.engine);
      super.setSource(var1);
   }

   public void updateStats() {
      this.engine.updateRates();
   }

   protected boolean videoEnabled() {
      return this.engine.videoEnabled();
   }
}
