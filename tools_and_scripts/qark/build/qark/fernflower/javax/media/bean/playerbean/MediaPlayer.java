package javax.media.bean.playerbean;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.ClockStoppedException;
import javax.media.Control;
import javax.media.Controller;
import javax.media.ControllerListener;
import javax.media.GainControl;
import javax.media.IncompatibleSourceException;
import javax.media.IncompatibleTimeBaseException;
import javax.media.Manager;
import javax.media.MediaLocator;
import javax.media.NoPlayerException;
import javax.media.Player;
import javax.media.Time;
import javax.media.TimeBase;
import javax.media.protocol.DataSource;
import net.sf.fmj.utility.LoggerSingleton;
import org.atalk.android.util.java.awt.Component;
import org.atalk.android.util.java.awt.Container;
import org.atalk.android.util.java.awt.Dimension;
import org.atalk.android.util.java.awt.Panel;
import org.atalk.android.util.java.beans.PropertyChangeListener;

public class MediaPlayer extends Container implements Player, Externalizable {
   private static final Logger logger;
   transient Component cachingComponent;
   private boolean cachingVisible;
   transient Component controlComponent;
   private transient int controlPanelHeight;
   protected transient String curVolumeLevel;
   protected transient float curVolumeValue;
   protected transient String curZoomLevel;
   protected transient float curZoomValue;
   private boolean displayURL;
   protected transient GainControl gainControl;
   private boolean looping;
   protected transient Time mediaTime;
   transient Panel newPanel;
   transient Panel panel;
   private boolean panelVisible;
   transient Player player;
   private transient int urlFieldHeight;
   transient Panel vPanel;
   transient Component visualComponent;

   static {
      logger = LoggerSingleton.logger;
   }

   public void addController(Controller var1) throws IncompatibleTimeBaseException {
      Player var2 = this.player;
      if (var2 != null) {
         var2.addController(var1);
      }
   }

   public void addControllerListener(ControllerListener var1) {
      Player var2 = this.player;
      if (var2 != null) {
         var2.addControllerListener(var1);
      }
   }

   public void addPropertyChangeListener(PropertyChangeListener var1) {
      throw new UnsupportedOperationException();
   }

   public void close() {
      Player var1 = this.player;
      if (var1 != null) {
         var1.close();
      }
   }

   public void deallocate() {
      Player var1 = this.player;
      if (var1 != null) {
         var1.deallocate();
      }
   }

   public Control getControl(String var1) {
      Player var2 = this.player;
      return var2 == null ? null : var2.getControl(var1);
   }

   public Component getControlPanelComponent() {
      Player var1 = this.player;
      return var1 == null ? null : var1.getControlPanelComponent();
   }

   public int getControlPanelHeight() {
      return this.controlPanelHeight;
   }

   public Control[] getControls() {
      Player var1 = this.player;
      return var1 == null ? new Control[0] : var1.getControls();
   }

   public Time getDuration() {
      Player var1 = this.player;
      return var1 == null ? DURATION_UNKNOWN : var1.getDuration();
   }

   public GainControl getGainControl() {
      Player var1 = this.player;
      return var1 == null ? null : var1.getGainControl();
   }

   public String getMediaLocation() {
      if (this.player == null) {
         return " ";
      } else {
         throw new UnsupportedOperationException();
      }
   }

   public int getMediaLocationHeight() {
      return this.urlFieldHeight;
   }

   protected MediaLocator getMediaLocator(String var1) {
      StringBuilder var2 = new StringBuilder();
      var2.append("file://");
      var2.append(var1);
      return new MediaLocator(var2.toString());
   }

   public long getMediaNanoseconds() {
      Player var1 = this.player;
      return var1 == null ? Long.MAX_VALUE : var1.getMediaNanoseconds();
   }

   public Time getMediaTime() {
      Player var1 = this.player;
      return var1 == null ? new Time(Long.MAX_VALUE) : var1.getMediaTime();
   }

   public boolean getPlaybackLoop() {
      return this.looping;
   }

   public Player getPlayer() {
      return this.player;
   }

   public Dimension getPreferredSize() {
      throw new UnsupportedOperationException();
   }

   public float getRate() {
      Player var1 = this.player;
      return var1 == null ? 0.0F : var1.getRate();
   }

   public Time getStartLatency() {
      Player var1 = this.player;
      return var1 == null ? new Time(Long.MAX_VALUE) : var1.getStartLatency();
   }

   public int getState() {
      Player var1 = this.player;
      return var1 == null ? 100 : var1.getState();
   }

   public Time getStopTime() {
      Player var1 = this.player;
      return var1 == null ? null : var1.getStopTime();
   }

   public Time getSyncTime() {
      Player var1 = this.player;
      return var1 == null ? new Time(Long.MAX_VALUE) : var1.getSyncTime();
   }

   public int getTargetState() {
      Player var1 = this.player;
      return var1 == null ? 100 : var1.getTargetState();
   }

   public TimeBase getTimeBase() {
      Player var1 = this.player;
      return var1 == null ? null : var1.getTimeBase();
   }

   public Component getVisualComponent() {
      Player var1 = this.player;
      return var1 == null ? null : var1.getVisualComponent();
   }

   public String getVolumeLevel() {
      return this.curVolumeLevel;
   }

   public String getZoomTo() {
      return this.curZoomLevel;
   }

   public boolean isCachingControlVisible() {
      return this.cachingVisible;
   }

   public boolean isControlPanelVisible() {
      return this.panelVisible;
   }

   public boolean isFixedAspectRatio() {
      throw new UnsupportedOperationException();
   }

   public boolean isMediaLocationVisible() {
      return this.displayURL;
   }

   public boolean isPlayBackLoop() {
      return this.looping;
   }

   public Time mapToTimeBase(Time var1) throws ClockStoppedException {
      Player var2 = this.player;
      return var2 == null ? new Time(Long.MAX_VALUE) : var2.mapToTimeBase(var1);
   }

   public void prefetch() {
      Player var1 = this.player;
      if (var1 != null) {
         var1.prefetch();
      }
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      throw new UnsupportedOperationException();
   }

   public void realize() {
      Player var1 = this.player;
      if (var1 != null) {
         var1.realize();
      }
   }

   public void removeController(Controller var1) {
      Player var2 = this.player;
      if (var2 != null) {
         var2.removeController(var1);
      }
   }

   public void removeControllerListener(ControllerListener var1) {
      Player var2 = this.player;
      if (var2 != null) {
         var2.removeControllerListener(var1);
      }
   }

   public void removePropertyChangeListener(PropertyChangeListener var1) {
      throw new UnsupportedOperationException();
   }

   public void restoreMediaTime() {
      throw new UnsupportedOperationException();
   }

   public void saveMediaTime() {
      throw new UnsupportedOperationException();
   }

   public void setBounds(int var1, int var2, int var3, int var4) {
      throw new UnsupportedOperationException();
   }

   public void setCachingControlVisible(boolean var1) {
      this.cachingVisible = var1;
   }

   public void setCodeBase(URL var1) {
   }

   public void setControlPanelVisible(boolean var1) {
      this.panelVisible = var1;
   }

   public void setDataSource(DataSource var1) {
      Logger var2;
      Level var3;
      StringBuilder var4;
      try {
         this.player = Manager.createPlayer(var1);
      } catch (NoPlayerException var5) {
         var2 = logger;
         var3 = Level.WARNING;
         var4 = new StringBuilder();
         var4.append("");
         var4.append(var5);
         var2.log(var3, var4.toString(), var5);
      } catch (IOException var6) {
         var2 = logger;
         var3 = Level.WARNING;
         var4 = new StringBuilder();
         var4.append("");
         var4.append(var6);
         var2.log(var3, var4.toString(), var6);
         return;
      }

   }

   public void setFixedAspectRatio(boolean var1) {
      throw new UnsupportedOperationException();
   }

   public void setMediaLocation(String var1) {
      throw new UnsupportedOperationException();
   }

   public void setMediaLocationVisible(boolean var1) {
      this.displayURL = var1;
   }

   public void setMediaLocator(MediaLocator var1) {
      Logger var2;
      Level var3;
      StringBuilder var4;
      try {
         this.player = Manager.createPlayer(var1);
      } catch (NoPlayerException var5) {
         var2 = logger;
         var3 = Level.WARNING;
         var4 = new StringBuilder();
         var4.append("");
         var4.append(var5);
         var2.log(var3, var4.toString(), var5);
      } catch (IOException var6) {
         var2 = logger;
         var3 = Level.WARNING;
         var4 = new StringBuilder();
         var4.append("");
         var4.append(var6);
         var2.log(var3, var4.toString(), var6);
         return;
      }

   }

   public void setMediaTime(Time var1) {
      Player var2 = this.player;
      if (var2 != null) {
         var2.setMediaTime(var1);
      }
   }

   public void setPlaybackLoop(boolean var1) {
      this.looping = var1;
   }

   public void setPlayer(Player var1) {
      this.player = var1;
   }

   public void setPopupActive(boolean var1) {
   }

   public float setRate(float var1) {
      Player var2 = this.player;
      return var2 == null ? 0.0F : var2.setRate(var1);
   }

   public void setSource(DataSource var1) throws IOException, IncompatibleSourceException {
      Player var2 = this.player;
      if (var2 != null) {
         var2.setSource(var1);
      }
   }

   public void setStopTime(Time var1) {
      Player var2 = this.player;
      if (var2 != null) {
         var2.setStopTime(var1);
      }
   }

   public void setTimeBase(TimeBase var1) throws IncompatibleTimeBaseException {
      Player var2 = this.player;
      if (var2 != null) {
         var2.setTimeBase(var1);
      }
   }

   public void setVolumeLevel(String var1) {
      this.curVolumeLevel = var1;
   }

   public void setZoomTo(String var1) {
      this.curZoomLevel = var1;
   }

   public void start() {
      Player var1 = this.player;
      if (var1 != null) {
         var1.start();
      }
   }

   public void stop() {
      Player var1 = this.player;
      if (var1 != null) {
         var1.stop();
      }
   }

   public void stopAndDeallocate() {
      this.stop();
      this.deallocate();
   }

   public void syncStart(Time var1) {
      Player var2 = this.player;
      if (var2 != null) {
         var2.syncStart(var1);
      }
   }

   public void waitForState(int var1) {
      synchronized(this){}

      try {
         throw new UnsupportedOperationException();
      } finally {
         ;
      }
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      throw new UnsupportedOperationException();
   }
}
