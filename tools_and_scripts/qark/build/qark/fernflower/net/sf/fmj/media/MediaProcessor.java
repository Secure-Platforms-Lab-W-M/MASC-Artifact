package net.sf.fmj.media;

import java.io.IOException;
import javax.media.GainControl;
import javax.media.IncompatibleSourceException;
import javax.media.NotConfiguredError;
import javax.media.NotRealizedError;
import javax.media.Time;
import javax.media.TimeBase;
import javax.media.control.TrackControl;
import javax.media.protocol.ContentDescriptor;
import javax.media.protocol.DataSource;
import org.atalk.android.util.java.awt.Component;

public class MediaProcessor extends BasicProcessor {
   protected ProcessEngine engine = new ProcessEngine(this);

   protected boolean audioEnabled() {
      return this.engine.audioEnabled();
   }

   public ContentDescriptor getContentDescriptor() throws NotConfiguredError {
      return this.engine.getContentDescriptor();
   }

   public DataSource getDataOutput() throws NotRealizedError {
      return this.engine.getDataOutput();
   }

   public GainControl getGainControl() {
      super.getGainControl();
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

   public ContentDescriptor[] getSupportedContentDescriptors() throws NotConfiguredError {
      return this.engine.getSupportedContentDescriptors();
   }

   public TrackControl[] getTrackControls() throws NotConfiguredError {
      return this.engine.getTrackControls();
   }

   public Component getVisualComponent() {
      super.getVisualComponent();
      return this.engine.getVisualComponent();
   }

   public ContentDescriptor setContentDescriptor(ContentDescriptor var1) throws NotConfiguredError {
      return this.engine.setContentDescriptor(var1);
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
