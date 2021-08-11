package net.sf.fmj.media;

import java.io.IOException;
import java.util.Vector;
import javax.media.AudioDeviceUnavailableEvent;
import javax.media.CachingControl;
import javax.media.CachingControlEvent;
import javax.media.Clock;
import javax.media.ClockStartedError;
import javax.media.Control;
import javax.media.Controller;
import javax.media.ControllerClosedEvent;
import javax.media.ControllerErrorEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DownloadProgressListener;
import javax.media.DurationUpdateEvent;
import javax.media.EndOfMediaEvent;
import javax.media.ExtendedCachingControl;
import javax.media.GainControl;
import javax.media.IncompatibleSourceException;
import javax.media.IncompatibleTimeBaseException;
import javax.media.MediaLocator;
import javax.media.NotPrefetchedError;
import javax.media.NotRealizedError;
import javax.media.Player;
import javax.media.ResourceUnavailableEvent;
import javax.media.RestartingEvent;
import javax.media.SizeChangeEvent;
import javax.media.StartEvent;
import javax.media.StopAtTimeEvent;
import javax.media.StopTimeChangeEvent;
import javax.media.Time;
import javax.media.TimeBase;
import javax.media.control.BufferControl;
import javax.media.protocol.DataSource;
import javax.media.protocol.Positionable;
import javax.media.protocol.RateConfiguration;
import javax.media.protocol.RateConfigureable;
import javax.media.protocol.RateRange;
import net.sf.fmj.media.control.SliderRegionControl;
import net.sf.fmj.media.control.SliderRegionControlAdapter;
import org.atalk.android.util.java.awt.Component;

public abstract class BasicPlayer extends BasicController implements Player, ControllerListener, DownloadProgressListener {
   static final int LOCAL_STOP = 0;
   static final int RESTARTING = 2;
   static final int STOP_BY_REQUEST = 1;
   private boolean aboutToRestart;
   protected BufferControl bufferControl;
   protected CachingControl cachingControl;
   private boolean closing;
   private Vector configureEventList = new Vector();
   protected Component controlComp;
   protected Vector controllerList = new Vector();
   protected Control[] controls;
   private Vector currentControllerList = new Vector();
   private Time duration;
   private Vector eomEventsReceivedFrom = new Vector();
   protected ExtendedCachingControl extendedCachingControl;
   protected boolean framePositioning;
   long lastTime;
   private Time mediaTimeAtStart;
   private Object mediaTimeSync;
   private Vector optionalControllerList = new Vector();
   private PlayThread playThread = null;
   private Vector potentialEventsList = null;
   private Vector prefetchEventList = new Vector();
   private boolean prefetchFailed;
   private Vector realizeEventList = new Vector();
   private boolean receivedAllEvents = false;
   private Vector receivedEventList = new Vector();
   public SliderRegionControl regionControl;
   private Vector removedControllerList = new Vector();
   private Controller restartFrom = null;
   protected DataSource source = null;
   private Object startSync;
   private Time startTime;
   private StatsThread statsThread = null;
   private Vector stopAtTimeReceivedFrom = new Vector();
   private Vector stopEventList = new Vector();

   public BasicPlayer() {
      this.duration = DURATION_UNKNOWN;
      this.aboutToRestart = false;
      this.closing = false;
      this.prefetchFailed = false;
      this.framePositioning = true;
      this.controls = null;
      this.controlComp = null;
      this.regionControl = null;
      this.cachingControl = null;
      this.extendedCachingControl = null;
      this.bufferControl = null;
      this.startSync = new Object();
      this.mediaTimeSync = new Object();
      this.lastTime = 0L;
      this.configureEventList.addElement("javax.media.ConfigureCompleteEvent");
      this.configureEventList.addElement("javax.media.ResourceUnavailableEvent");
      this.realizeEventList.addElement("javax.media.RealizeCompleteEvent");
      this.realizeEventList.addElement("javax.media.ResourceUnavailableEvent");
      this.prefetchEventList.addElement("javax.media.PrefetchCompleteEvent");
      this.prefetchEventList.addElement("javax.media.ResourceUnavailableEvent");
      this.stopEventList.addElement("javax.media.StopEvent");
      this.stopEventList.addElement("javax.media.StopByRequestEvent");
      this.stopEventList.addElement("javax.media.StopAtTimeEvent");
      this.stopThreadEnabled = false;
   }

   private void doSetStopTime(Time var1) {
      this.getClock().setStopTime(var1);
      int var2 = this.controllerList.size();

      while(true) {
         --var2;
         if (var2 < 0) {
            return;
         }

         ((Controller)this.controllerList.elementAt(var2)).setStopTime(var1);
      }
   }

   private Vector getPotentialEventsList() {
      return this.potentialEventsList;
   }

   private Vector getReceivedEventsList() {
      return this.receivedEventList;
   }

   private void notifyIfAllEventsArrived(Vector param1, Vector param2) {
      // $FF: Couldn't be decompiled
   }

   private void resetReceivedEventList() {
      Vector var1 = this.receivedEventList;
      if (var1 != null) {
         var1.removeAllElements();
      }

   }

   private void stop(int param1) {
      // $FF: Couldn't be decompiled
   }

   private boolean trySetRate(float var1) {
      int var2 = this.controllerList.size();

      do {
         --var2;
         if (var2 < 0) {
            return true;
         }
      } while(((Controller)this.controllerList.elementAt(var2)).setRate(var1) == var1);

      return false;
   }

   private void updateReceivedEventsList(ControllerEvent var1) {
      if (this.receivedEventList != null) {
         Controller var2 = var1.getSourceController();
         if (this.receivedEventList.contains(var2)) {
            return;
         }

         this.receivedEventList.addElement(var2);
      }

   }

   protected final void abortPrefetch() {
      // $FF: Couldn't be decompiled
   }

   protected final void abortRealize() {
      // $FF: Couldn't be decompiled
   }

   public void addController(Controller var1) throws IncompatibleTimeBaseException {
      synchronized(this){}

      Throwable var10000;
      label788: {
         int var2;
         boolean var10001;
         try {
            var2 = this.getState();
         } catch (Throwable var76) {
            var10000 = var76;
            var10001 = false;
            break label788;
         }

         if (var2 == 600) {
            try {
               this.throwError(new ClockStartedError("Cannot add controller to a started player"));
            } catch (Throwable var75) {
               var10000 = var75;
               var10001 = false;
               break label788;
            }
         }

         if (var2 == 100 || var2 == 200) {
            try {
               this.throwError(new NotRealizedError("A Controller cannot be added to an Unrealized Player"));
            } catch (Throwable var74) {
               var10000 = var74;
               var10001 = false;
               break label788;
            }
         }

         if (var1 == null || var1 == this) {
            return;
         }

         int var3;
         try {
            var3 = var1.getState();
         } catch (Throwable var73) {
            var10000 = var73;
            var10001 = false;
            break label788;
         }

         if (var3 == 100 || var3 == 200) {
            try {
               this.throwError(new NotRealizedError("An Unrealized Controller cannot be added to a Player"));
            } catch (Throwable var72) {
               var10000 = var72;
               var10001 = false;
               break label788;
            }
         }

         boolean var4;
         try {
            var4 = this.controllerList.contains(var1);
         } catch (Throwable var71) {
            var10000 = var71;
            var10001 = false;
            break label788;
         }

         if (var4) {
            return;
         }

         if (var2 == 500 && (var3 == 300 || var3 == 400)) {
            try {
               this.deallocate();
            } catch (Throwable var70) {
               var10000 = var70;
               var10001 = false;
               break label788;
            }
         }

         try {
            this.manageController(var1);
            var1.setTimeBase(this.getTimeBase());
            var1.setMediaTime(this.getMediaTime());
            var1.setStopTime(this.getStopTime());
            if (var1.setRate(this.getRate()) != this.getRate()) {
               this.setRate(1.0F);
            }
         } catch (Throwable var69) {
            var10000 = var69;
            var10001 = false;
            break label788;
         }

         return;
      }

      Throwable var77 = var10000;
      throw var77;
   }

   protected abstract boolean audioEnabled();

   float checkRateConfig(RateConfigureable var1, float var2) {
      RateConfiguration[] var6 = var1.getRateConfigurations();
      if (var6 == null) {
         return 1.0F;
      } else {
         float var4 = 1.0F;
         int var5 = 0;

         float var3;
         while(true) {
            var3 = var4;
            if (var5 >= var6.length) {
               break;
            }

            RateRange var7 = var6[var5].getRate();
            if (var7 != null && var7.inRange(var2)) {
               var7.setCurrentRate(var2);
               RateConfiguration var8 = var1.setRateConfiguration(var6[var5]);
               var3 = var2;
               if (var8 != null) {
                  RateRange var9 = var8.getRate();
                  var3 = var2;
                  if (var9 != null) {
                     return var9.getCurrentRate();
                  }
               }
               break;
            }

            ++var5;
         }

         return var3;
      }
   }

   protected void completeConfigure() {
      // $FF: Couldn't be decompiled
   }

   protected void completePrefetch() {
      // $FF: Couldn't be decompiled
   }

   protected void completeRealize() {
      // $FF: Couldn't be decompiled
   }

   protected void controllerSetStopTime(Time var1) {
      super.setStopTime(var1);
   }

   protected void controllerStopAtTime() {
      super.stopAtTime();
   }

   public final void controllerUpdate(ControllerEvent var1) {
      this.processEvent(var1);
   }

   protected boolean deviceBusy(BasicController var1) {
      return true;
   }

   protected void doClose() {
      // $FF: Couldn't be decompiled
   }

   protected boolean doConfigure() {
      // $FF: Couldn't be decompiled
   }

   protected void doFailedConfigure() {
      // $FF: Couldn't be decompiled
   }

   protected void doFailedPrefetch() {
      // $FF: Couldn't be decompiled
   }

   protected void doFailedRealize() {
      // $FF: Couldn't be decompiled
   }

   protected boolean doPrefetch() {
      // $FF: Couldn't be decompiled
   }

   protected boolean doRealize() {
      // $FF: Couldn't be decompiled
   }

   protected void doSetMediaTime(Time var1) {
   }

   protected float doSetRate(float var1) {
      return var1;
   }

   protected void doStart() {
   }

   protected void doStop() {
   }

   public void downloadUpdate() {
      if (this.extendedCachingControl != null) {
         CachingControl var5 = this.cachingControl;
         this.sendEvent(new CachingControlEvent(this, var5, var5.getContentProgress()));
         if (this.regionControl != null) {
            long var3 = this.cachingControl.getContentLength();
            int var1;
            if (var3 != -1L && var3 > 0L) {
               int var2 = (int)((double)this.extendedCachingControl.getEndOffset() * 100.0D / (double)var3);
               if (var2 < 0) {
                  var1 = 0;
               } else {
                  var1 = var2;
                  if (var2 > 100) {
                     var1 = 100;
                  }
               }
            } else {
               var1 = 0;
            }

            this.regionControl.setMinValue(0L);
            this.regionControl.setMaxValue((long)var1);
         }
      }
   }

   public String getContentType() {
      DataSource var1 = this.source;
      return var1 != null ? var1.getContentType() : null;
   }

   public Component getControlPanelComponent() {
      if (this.getState() < 300) {
         this.throwError(new NotRealizedError("Cannot get control panel component on an unrealized player"));
      }

      return this.controlComp;
   }

   public final Vector getControllerList() {
      return this.controllerList;
   }

   public Control[] getControls() {
      Control[] var4 = this.controls;
      if (var4 != null) {
         return var4;
      } else {
         Vector var6 = new Vector();
         CachingControl var5 = this.cachingControl;
         if (var5 != null) {
            var6.addElement(var5);
         }

         BufferControl var7 = this.bufferControl;
         if (var7 != null) {
            var6.addElement(var7);
         }

         int var3 = this.controllerList.size();

         int var1;
         int var2;
         Control[] var8;
         for(var1 = 0; var1 < var3; ++var1) {
            var8 = ((Controller)this.controllerList.elementAt(var1)).getControls();
            if (var8 != null) {
               for(var2 = 0; var2 < var8.length; ++var2) {
                  var6.addElement(var8[var2]);
               }
            }
         }

         var2 = var6.size();
         var8 = new Control[var2];

         for(var1 = 0; var1 < var2; ++var1) {
            var8[var1] = (Control)var6.elementAt(var1);
         }

         if (this.getState() >= 300) {
            this.controls = var8;
         }

         return var8;
      }
   }

   public Time getDuration() {
      long var1 = this.getMediaNanoseconds();
      if (var1 > this.lastTime) {
         this.lastTime = var1;
         this.updateDuration();
      }

      return this.duration;
   }

   public GainControl getGainControl() {
      if (this.getState() < 300) {
         this.throwError(new NotRealizedError("Cannot get gain control on an unrealized player"));
         return null;
      } else {
         return (GainControl)this.getControl("javax.media.GainControl");
      }
   }

   protected abstract TimeBase getMasterTimeBase();

   public MediaLocator getMediaLocator() {
      DataSource var1 = this.source;
      return var1 != null ? var1.getLocator() : null;
   }

   protected DataSource getSource() {
      return this.source;
   }

   public Time getStartLatency() {
      super.getStartLatency();
      long var2 = 0L;

      long var4;
      for(int var1 = 0; var1 < this.controllerList.size(); var2 = var4) {
         Time var6 = ((Controller)this.controllerList.elementAt(var1)).getStartLatency();
         if (var6 == LATENCY_UNKNOWN) {
            var4 = var2;
         } else {
            var4 = var2;
            if (var6.getNanoseconds() > var2) {
               var4 = var6.getNanoseconds();
            }
         }

         ++var1;
      }

      if (var2 == 0L) {
         return LATENCY_UNKNOWN;
      } else {
         return new Time(var2);
      }
   }

   public Component getVisualComponent() {
      if (this.getState() < 300) {
         this.throwError(new NotRealizedError("Cannot get visual component on an unrealized player"));
      }

      return null;
   }

   public boolean isAboutToRestart() {
      return this.aboutToRestart;
   }

   protected boolean isConfigurable() {
      return false;
   }

   public boolean isFramePositionable() {
      return this.framePositioning;
   }

   protected final void manageController(Controller var1) {
      this.manageController(var1, false);
   }

   protected final void manageController(Controller var1, boolean var2) {
      if (var1 != null && !this.controllerList.contains(var1)) {
         this.controllerList.addElement(var1);
         if (var2) {
            this.optionalControllerList.addElement(var1);
         }

         var1.addControllerListener(this);
      }

      this.updateDuration();
   }

   final void play() {
      // $FF: Couldn't be decompiled
   }

   protected final void processEndOfMedia() {
      super.stop();
      this.sendEvent(new EndOfMediaEvent(this, 600, 500, this.getTargetState(), this.getMediaTime()));
   }

   protected void processEvent(ControllerEvent var1) {
      Controller var5 = var1.getSourceController();
      if (var1 instanceof AudioDeviceUnavailableEvent) {
         this.sendEvent(new AudioDeviceUnavailableEvent(this));
      } else {
         if (var1 instanceof ControllerClosedEvent && !this.closing && this.controllerList.contains(var5) && !(var1 instanceof ResourceUnavailableEvent)) {
            this.controllerList.removeElement(var5);
            if (var1 instanceof ControllerErrorEvent) {
               this.sendEvent(new ControllerErrorEvent(this, ((ControllerErrorEvent)var1).getMessage()));
            }

            this.close();
         }

         if (var1 instanceof SizeChangeEvent && this.controllerList.contains(var5)) {
            this.sendEvent(new SizeChangeEvent(this, ((SizeChangeEvent)var1).getWidth(), ((SizeChangeEvent)var1).getHeight(), ((SizeChangeEvent)var1).getScale()));
         } else if (var1 instanceof DurationUpdateEvent && this.controllerList.contains(var5)) {
            this.updateDuration();
         } else {
            int var2;
            int var3;
            Controller var6;
            if (var1 instanceof RestartingEvent && this.controllerList.contains(var5)) {
               this.restartFrom = var5;
               var3 = this.controllerList.size();
               super.stop();
               this.setTargetState(500);

               for(var2 = 0; var2 < var3; ++var2) {
                  var6 = (Controller)this.controllerList.elementAt(var2);
                  if (var6 != var5) {
                     var6.stop();
                  }
               }

               super.stop();
               this.sendEvent(new RestartingEvent(this, 600, 400, 600, this.getMediaTime()));
            }

            if (var1 instanceof StartEvent && var5 == this.restartFrom) {
               this.restartFrom = null;
               this.start();
            }

            if (var1 instanceof SeekFailedEvent && this.controllerList.contains(var5)) {
               var3 = this.controllerList.size();
               super.stop();
               this.setTargetState(500);

               for(var2 = 0; var2 < var3; ++var2) {
                  var6 = (Controller)this.controllerList.elementAt(var2);
                  if (var6 != var5) {
                     var6.stop();
                  }
               }

               this.sendEvent(new SeekFailedEvent(this, 600, 500, 500, this.getMediaTime()));
            }

            if (var1 instanceof EndOfMediaEvent && this.controllerList.contains(var5)) {
               if (!this.eomEventsReceivedFrom.contains(var5)) {
                  this.eomEventsReceivedFrom.addElement(var5);
                  if (this.eomEventsReceivedFrom.size() == this.controllerList.size()) {
                     super.stop();
                     this.sendEvent(new EndOfMediaEvent(this, 600, 500, this.getTargetState(), this.getMediaTime()));
                  }

               }
            } else if (var1 instanceof StopAtTimeEvent && this.controllerList.contains(var5) && this.getState() == 600) {
               Vector var51 = this.stopAtTimeReceivedFrom;
               synchronized(var51){}

               Throwable var10000;
               boolean var10001;
               label1371: {
                  try {
                     if (this.stopAtTimeReceivedFrom.contains(var5)) {
                        return;
                     }
                  } catch (Throwable var49) {
                     var10000 = var49;
                     var10001 = false;
                     break label1371;
                  }

                  boolean var53;
                  label1327: {
                     label1326: {
                        try {
                           this.stopAtTimeReceivedFrom.addElement(var5);
                           if (this.stopAtTimeReceivedFrom.size() == this.controllerList.size()) {
                              break label1326;
                           }
                        } catch (Throwable var48) {
                           var10000 = var48;
                           var10001 = false;
                           break label1371;
                        }

                        var53 = false;
                        break label1327;
                     }

                     var53 = true;
                  }

                  boolean var52 = var53;
                  if (!var53) {
                     label1372: {
                        boolean var4 = true;
                        var3 = 0;

                        while(true) {
                           var52 = var4;

                           try {
                              if (var3 >= this.controllerList.size()) {
                                 break label1372;
                              }

                              var5 = (Controller)this.controllerList.elementAt(var3);
                              if (!this.stopAtTimeReceivedFrom.contains(var5) && !this.eomEventsReceivedFrom.contains(var5)) {
                                 break;
                              }
                           } catch (Throwable var47) {
                              var10000 = var47;
                              var10001 = false;
                              break label1371;
                           }

                           ++var3;
                        }

                        var52 = false;
                     }
                  }

                  if (var52) {
                     try {
                        super.stop();
                        this.doSetStopTime(Clock.RESET);
                        this.sendEvent(new StopAtTimeEvent(this, 600, 500, this.getTargetState(), this.getMediaTime()));
                     } catch (Throwable var46) {
                        var10000 = var46;
                        var10001 = false;
                        break label1371;
                     }
                  }

                  label1299:
                  try {
                     return;
                  } catch (Throwable var45) {
                     var10000 = var45;
                     var10001 = false;
                     break label1299;
                  }
               }

               while(true) {
                  Throwable var54 = var10000;

                  try {
                     throw var54;
                  } catch (Throwable var44) {
                     var10000 = var44;
                     var10001 = false;
                     continue;
                  }
               }
            } else if (var1 instanceof CachingControlEvent && this.controllerList.contains(var5)) {
               CachingControl var50 = ((CachingControlEvent)var1).getCachingControl();
               this.sendEvent(new CachingControlEvent(this, var50, var50.getContentProgress()));
            } else {
               Vector var55 = this.potentialEventsList;
               Vector var7 = this.controllerList;
               if (var7 != null && var7.contains(var5) && var55 != null && var55.contains(var1.getClass().getName())) {
                  this.updateReceivedEventsList(var1);
                  this.notifyIfAllEventsArrived(this.controllerList, this.getReceivedEventsList());
               }

            }
         }
      }
   }

   public final void removeController(Controller param1) {
      // $FF: Couldn't be decompiled
   }

   protected void setMediaLength(long var1) {
      this.duration = new Time(var1);
      super.setMediaLength(var1);
   }

   public final void setMediaTime(Time var1) {
      if (this.state < 300) {
         this.throwError(new NotRealizedError(MediaTimeError));
      }

      Object var4 = this.mediaTimeSync;
      synchronized(var4){}

      Throwable var10000;
      boolean var10001;
      label696: {
         try {
            if (this.syncStartInProgress()) {
               return;
            }
         } catch (Throwable var76) {
            var10000 = var76;
            var10001 = false;
            break label696;
         }

         try {
            if (this.getState() == 600) {
               this.aboutToRestart = true;
               this.stop(2);
            }
         } catch (Throwable var75) {
            var10000 = var75;
            var10001 = false;
            break label696;
         }

         Time var3 = var1;

         try {
            if (this.source instanceof Positionable) {
               var3 = ((Positionable)this.source).setPosition(var1, 2);
            }
         } catch (Throwable var74) {
            var10000 = var74;
            var10001 = false;
            break label696;
         }

         int var2;
         try {
            super.setMediaTime(var3);
            var2 = this.controllerList.size();
         } catch (Throwable var73) {
            var10000 = var73;
            var10001 = false;
            break label696;
         }

         while(true) {
            --var2;
            if (var2 < 0) {
               try {
                  this.doSetMediaTime(var3);
                  if (this.aboutToRestart) {
                     this.syncStart(this.getTimeBase().getTime());
                     this.aboutToRestart = false;
                  }
               } catch (Throwable var71) {
                  var10000 = var71;
                  var10001 = false;
                  break;
               }

               try {
                  return;
               } catch (Throwable var70) {
                  var10000 = var70;
                  var10001 = false;
                  break;
               }
            }

            try {
               ((Controller)this.controllerList.elementAt(var2)).setMediaTime(var3);
            } catch (Throwable var72) {
               var10000 = var72;
               var10001 = false;
               break;
            }
         }
      }

      while(true) {
         Throwable var77 = var10000;

         try {
            throw var77;
         } catch (Throwable var69) {
            var10000 = var69;
            var10001 = false;
            continue;
         }
      }
   }

   public float setRate(float var1) {
      if (this.state < 300) {
         this.throwError(new NotRealizedError("Cannot set rate on an unrealized Player."));
      }

      DataSource var3 = this.source;
      float var2 = var1;
      if (var3 instanceof RateConfigureable) {
         var2 = this.checkRateConfig((RateConfigureable)var3, var1);
      }

      var1 = this.getRate();
      if (var1 == var2) {
         return var2;
      } else {
         if (this.getState() == 600) {
            this.aboutToRestart = true;
            this.stop(2);
         }

         if (!this.trySetRate(var2)) {
            if (!this.trySetRate(var1)) {
               this.trySetRate(1.0F);
               var1 = 1.0F;
            }
         } else {
            var1 = var2;
         }

         super.setRate(var1);
         if (this.aboutToRestart) {
            this.syncStart(this.getTimeBase().getTime());
            this.aboutToRestart = false;
         }

         return var1;
      }
   }

   public void setSource(DataSource var1) throws IOException, IncompatibleSourceException {
      this.source = var1;

      boolean var10001;
      CachingControl var5;
      try {
         var5 = (CachingControl)var1.getControl(CachingControl.class.getName());
         this.cachingControl = var5;
      } catch (ClassCastException var4) {
         var10001 = false;
         return;
      }

      if (var5 != null) {
         ExtendedCachingControl var6;
         try {
            if (!(var5 instanceof ExtendedCachingControl)) {
               return;
            }

            var6 = (ExtendedCachingControl)var5;
            this.extendedCachingControl = var6;
         } catch (ClassCastException var3) {
            var10001 = false;
            return;
         }

         if (var6 != null) {
            try {
               this.regionControl = new SliderRegionControlAdapter();
               this.extendedCachingControl.addDownloadProgressListener(this, 100);
            } catch (ClassCastException var2) {
               var10001 = false;
            }
         }
      }
   }

   public void setStopTime(Time var1) {
      if (this.state < 300) {
         this.throwError(new NotRealizedError("Cannot set stop time on an unrealized controller."));
      }

      if (this.getClock().getStopTime() == null || this.getClock().getStopTime().getNanoseconds() != var1.getNanoseconds()) {
         this.sendEvent(new StopTimeChangeEvent(this, var1));
      }

      this.doSetStopTime(var1);
   }

   public void setTimeBase(TimeBase var1) throws IncompatibleTimeBaseException {
      TimeBase var5 = this.getMasterTimeBase();
      TimeBase var3 = var1;
      if (var1 == null) {
         var3 = var5;
      }

      Controller var10 = null;
      Controller var4 = null;
      Vector var6 = this.controllerList;
      if (var6 != null) {
         label62: {
            int var2;
            IncompatibleTimeBaseException var10000;
            label59: {
               boolean var10001;
               try {
                  var2 = var6.size();
               } catch (IncompatibleTimeBaseException var9) {
                  var10000 = var9;
                  var10001 = false;
                  break label59;
               }

               var10 = var4;

               while(true) {
                  --var2;
                  if (var2 < 0) {
                     break label62;
                  }

                  try {
                     var4 = (Controller)this.controllerList.elementAt(var2);
                  } catch (IncompatibleTimeBaseException var8) {
                     var10000 = var8;
                     var10001 = false;
                     break;
                  }

                  var10 = var4;

                  try {
                     var4.setTimeBase(var3);
                  } catch (IncompatibleTimeBaseException var7) {
                     var10000 = var7;
                     var10001 = false;
                     break;
                  }

                  var10 = var4;
               }
            }

            IncompatibleTimeBaseException var11 = var10000;
            var2 = this.controllerList.size();

            while(true) {
               --var2;
               if (var2 < 0) {
                  break;
               }

               var4 = (Controller)this.controllerList.elementAt(var2);
               if (var4 == var10) {
                  break;
               }

               var4.setTimeBase(var5);
            }

            Log.dumpStack(var11);
            throw var11;
         }
      }

      super.setTimeBase(var3);
   }

   protected void slaveToMasterTimeBase(TimeBase var1) throws IncompatibleTimeBaseException {
      this.setTimeBase(var1);
   }

   public final void start() {
      Object var1 = this.startSync;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label381: {
         try {
            if (this.restartFrom != null) {
               return;
            }
         } catch (Throwable var44) {
            var10000 = var44;
            var10001 = false;
            break label381;
         }

         try {
            if (this.getState() == 600) {
               this.sendEvent(new StartEvent(this, 600, 600, 600, this.mediaTimeAtStart, this.startTime));
               return;
            }
         } catch (Throwable var43) {
            var10000 = var43;
            var10001 = false;
            break label381;
         }

         label367: {
            try {
               if (this.playThread != null && this.playThread.isAlive()) {
                  break label367;
               }
            } catch (Throwable var42) {
               var10000 = var42;
               var10001 = false;
               break label381;
            }

            try {
               this.setTargetState(600);
               PlayThread var2 = new PlayThread(this);
               this.playThread = var2;
               var2.start();
            } catch (Throwable var41) {
               var10000 = var41;
               var10001 = false;
               break label381;
            }
         }

         label358:
         try {
            return;
         } catch (Throwable var40) {
            var10000 = var40;
            var10001 = false;
            break label358;
         }
      }

      while(true) {
         Throwable var45 = var10000;

         try {
            throw var45;
         } catch (Throwable var39) {
            var10000 = var39;
            var10001 = false;
            continue;
         }
      }
   }

   public final void stop() {
      this.stop(1);
   }

   protected void stopAtTime() {
   }

   public final void syncStart(Time var1) {
      Object var4 = this.mediaTimeSync;
      synchronized(var4){}

      Throwable var10000;
      boolean var10001;
      label821: {
         try {
            if (this.syncStartInProgress()) {
               return;
            }
         } catch (Throwable var94) {
            var10000 = var94;
            var10001 = false;
            break label821;
         }

         int var2;
         try {
            var2 = this.getState();
         } catch (Throwable var93) {
            var10000 = var93;
            var10001 = false;
            break label821;
         }

         if (var2 == 600) {
            try {
               this.throwError(new ClockStartedError("syncStart() cannot be used on an already started player"));
            } catch (Throwable var92) {
               var10000 = var92;
               var10001 = false;
               break label821;
            }
         }

         if (var2 != 500) {
            try {
               this.throwError(new NotPrefetchedError("Cannot start player before it has been prefetched"));
            } catch (Throwable var91) {
               var10000 = var91;
               var10001 = false;
               break label821;
            }
         }

         try {
            this.eomEventsReceivedFrom.removeAllElements();
            this.stopAtTimeReceivedFrom.removeAllElements();
            this.setTargetState(600);
            var2 = this.controllerList.size();
         } catch (Throwable var89) {
            var10000 = var89;
            var10001 = false;
            break label821;
         }

         while(true) {
            int var3 = var2 - 1;
            if (var3 < 0) {
               try {
                  if (this.getTargetState() == 600) {
                     this.startTime = var1;
                     this.mediaTimeAtStart = this.getMediaTime();
                     super.syncStart(var1);
                  }
               } catch (Throwable var88) {
                  var10000 = var88;
                  var10001 = false;
                  break;
               }

               try {
                  return;
               } catch (Throwable var87) {
                  var10000 = var87;
                  var10001 = false;
                  break;
               }
            }

            var2 = var3;

            try {
               if (this.getTargetState() != 600) {
                  continue;
               }

               ((Controller)this.controllerList.elementAt(var3)).syncStart(var1);
            } catch (Throwable var90) {
               var10000 = var90;
               var10001 = false;
               break;
            }

            var2 = var3;
         }
      }

      while(true) {
         Throwable var95 = var10000;

         try {
            throw var95;
         } catch (Throwable var86) {
            var10000 = var86;
            var10001 = false;
            continue;
         }
      }
   }

   public final void unmanageController(Controller var1) {
      if (var1 != null && this.controllerList.contains(var1)) {
         this.controllerList.removeElement(var1);
         var1.removeControllerListener(this);
      }

   }

   protected void updateDuration() {
      synchronized(this){}

      Throwable var10000;
      label557: {
         Time var2;
         boolean var10001;
         try {
            var2 = this.duration;
            this.duration = DURATION_UNKNOWN;
         } catch (Throwable var46) {
            var10000 = var46;
            var10001 = false;
            break label557;
         }

         int var1 = 0;

         while(true) {
            label561: {
               Time var4;
               try {
                  if (var1 >= this.controllerList.size()) {
                     break;
                  }

                  Controller var3 = (Controller)this.controllerList.elementAt(var1);
                  var4 = var3.getDuration();
                  if (var4.equals(DURATION_UNKNOWN)) {
                     if (var3 instanceof BasicController) {
                        break label561;
                     }

                     this.duration = DURATION_UNKNOWN;
                     break;
                  }
               } catch (Throwable var45) {
                  var10000 = var45;
                  var10001 = false;
                  break label557;
               }

               try {
                  if (var4.equals(DURATION_UNBOUNDED)) {
                     this.duration = DURATION_UNBOUNDED;
                     break;
                  }
               } catch (Throwable var44) {
                  var10000 = var44;
                  var10001 = false;
                  break label557;
               }

               try {
                  if (this.duration.equals(DURATION_UNKNOWN)) {
                     this.duration = var4;
                     break label561;
                  }
               } catch (Throwable var43) {
                  var10000 = var43;
                  var10001 = false;
                  break label557;
               }

               try {
                  if (this.duration.getNanoseconds() < var4.getNanoseconds()) {
                     this.duration = var4;
                  }
               } catch (Throwable var42) {
                  var10000 = var42;
                  var10001 = false;
                  break label557;
               }
            }

            ++var1;
         }

         label516:
         try {
            if (this.duration.getNanoseconds() != var2.getNanoseconds()) {
               this.setMediaLength(this.duration.getNanoseconds());
               this.sendEvent(new DurationUpdateEvent(this, this.duration));
            }

            return;
         } catch (Throwable var41) {
            var10000 = var41;
            var10001 = false;
            break label516;
         }
      }

      Throwable var47 = var10000;
      throw var47;
   }

   public abstract void updateStats();

   protected abstract boolean videoEnabled();
}
