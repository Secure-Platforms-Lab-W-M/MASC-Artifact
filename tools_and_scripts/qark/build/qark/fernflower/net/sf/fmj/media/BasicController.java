package net.sf.fmj.media;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.media.Clock;
import javax.media.ClockStoppedException;
import javax.media.ConfigureCompleteEvent;
import javax.media.Control;
import javax.media.Controller;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Duration;
import javax.media.IncompatibleTimeBaseException;
import javax.media.MediaTimeSetEvent;
import javax.media.NotPrefetchedError;
import javax.media.NotRealizedError;
import javax.media.PrefetchCompleteEvent;
import javax.media.RateChangeEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.ResourceUnavailableEvent;
import javax.media.StartEvent;
import javax.media.StopAtTimeEvent;
import javax.media.StopTimeChangeEvent;
import javax.media.Time;
import javax.media.TimeBase;
import javax.media.TransitionEvent;

public abstract class BasicController implements Controller, Duration {
   static final int Configured = 180;
   static final int Configuring = 140;
   static String DeallocateError = "deallocate cannot be used on a started controller.";
   static String GetTimeBaseError = "Cannot get Time Base from an unrealized controller";
   static String LatencyError = "Cannot get start latency from an unrealized controller";
   static String MediaTimeError = "Cannot set media time on a unrealized controller";
   static String SetRateError = "Cannot set rate on an unrealized controller.";
   static String StopTimeError = "Cannot set stop time on an unrealized controller.";
   static String SyncStartError = "Cannot start the controller before it has been prefetched.";
   static String TimeBaseError = "Cannot set time base on an unrealized controller.";
   private Clock clock;
   private ConfigureWorkThread configureThread = null;
   private Object interruptSync = new Object();
   private boolean interrupted = false;
   private List listenerList = null;
   private PrefetchWorkThread prefetchThread = null;
   protected String processError = null;
   private RealizeWorkThread realizeThread = null;
   private SendEventQueue sendEvtQueue;
   private TimedStartThread startThread = null;
   protected int state = 100;
   protected boolean stopThreadEnabled = true;
   private StopTimeThread stopTimeThread = null;
   private int targetState = 100;

   public BasicController() {
      SendEventQueue var1 = new SendEventQueue(this);
      this.sendEvtQueue = var1;
      StringBuilder var2 = new StringBuilder();
      var2.append(this.sendEvtQueue.getName());
      var2.append(": SendEventQueue: ");
      var2.append(this.getClass().getName());
      var1.setName(var2.toString());
      this.sendEvtQueue.start();
      this.clock = new BasicClock();
   }

   private boolean activateStopThread(long var1) {
      if (this.getStopTime().getNanoseconds() == Long.MAX_VALUE) {
         return false;
      } else {
         StopTimeThread var3 = this.stopTimeThread;
         if (var3 != null && var3.isAlive()) {
            this.stopTimeThread.abort();
            this.stopTimeThread = null;
         }

         if (var1 > 100000000L) {
            var3 = new StopTimeThread(this, var1);
            this.stopTimeThread = var3;
            var3.start();
            return false;
         } else {
            return true;
         }
      }
   }

   private long checkStopTime() {
      long var1 = this.getStopTime().getNanoseconds();
      return var1 == Long.MAX_VALUE ? 1L : (long)((float)(var1 - this.getMediaTime().getNanoseconds()) / this.getRate());
   }

   protected void abortConfigure() {
   }

   protected abstract void abortPrefetch();

   protected abstract void abortRealize();

   public final void addControllerListener(ControllerListener var1) {
      if (this.listenerList == null) {
         this.listenerList = new ArrayList();
      }

      List var2 = this.listenerList;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label136: {
         try {
            if (!this.listenerList.contains(var1)) {
               this.listenerList.add(var1);
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label136;
         }

         label133:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label133;
         }
      }

      while(true) {
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public final void close() {
      this.doClose();
      this.interrupt();
      TimedStartThread var1 = this.startThread;
      if (var1 != null) {
         var1.abort();
      }

      StopTimeThread var2 = this.stopTimeThread;
      if (var2 != null) {
         var2.abort();
      }

      SendEventQueue var3 = this.sendEvtQueue;
      if (var3 != null) {
         var3.kill();
         this.sendEvtQueue = null;
      }

   }

   protected void completeConfigure() {
      synchronized(this){}

      try {
         this.state = 180;
         this.sendEvent(new ConfigureCompleteEvent(this, 140, 180, this.getTargetState()));
         if (this.getTargetState() >= 300) {
            this.realize();
         }
      } finally {
         ;
      }

   }

   protected void completePrefetch() {
      this.clock.stop();
      this.state = 500;
      this.sendEvent(new PrefetchCompleteEvent(this, 400, 500, this.getTargetState()));
   }

   protected void completeRealize() {
      synchronized(this){}

      try {
         this.state = 300;
         this.sendEvent(new RealizeCompleteEvent(this, 200, 300, this.getTargetState()));
         if (this.getTargetState() >= 500) {
            this.prefetch();
         }
      } finally {
         ;
      }

   }

   public void configure() {
      synchronized(this){}

      Throwable var10000;
      label269: {
         boolean var10001;
         try {
            if (this.getTargetState() < 180) {
               this.setTargetState(180);
            }
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label269;
         }

         int var1;
         try {
            var1 = this.state;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label269;
         }

         if (var1 != 100) {
            if (var1 != 180 && var1 != 200 && var1 != 300 && var1 != 400 && var1 != 500 && var1 != 600) {
               return;
            }

            try {
               this.sendEvent(new ConfigureCompleteEvent(this, this.state, this.state, this.getTargetState()));
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label269;
            }
         } else {
            try {
               this.state = 140;
               this.sendEvent(new TransitionEvent(this, 100, 140, this.getTargetState()));
               ConfigureWorkThread var24 = new ConfigureWorkThread(this);
               this.configureThread = var24;
               StringBuilder var3 = new StringBuilder();
               var3.append(this.configureThread.getName());
               var3.append("[ ");
               var3.append(this);
               var3.append(" ] ( configureThread)");
               var24.setName(var3.toString());
               this.configureThread.start();
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label269;
            }
         }

         return;
      }

      Throwable var2 = var10000;
      throw var2;
   }

   public final void deallocate() {
      // $FF: Couldn't be decompiled
   }

   protected final void dispatchEvent(ControllerEvent var1) {
      List var2 = this.listenerList;
      if (var2 != null) {
         synchronized(var2){}

         Throwable var10000;
         boolean var10001;
         label216: {
            Iterator var3;
            try {
               var3 = this.listenerList.iterator();
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label216;
            }

            while(true) {
               try {
                  if (var3.hasNext()) {
                     ((ControllerListener)var3.next()).controllerUpdate(var1);
                     continue;
                  }
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break;
               }

               try {
                  return;
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break;
               }
            }
         }

         while(true) {
            Throwable var24 = var10000;

            try {
               throw var24;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               continue;
            }
         }
      }
   }

   protected void doClose() {
   }

   protected boolean doConfigure() {
      return true;
   }

   protected void doDeallocate() {
   }

   protected void doFailedConfigure() {
      this.state = 100;
      this.setTargetState(100);
      String var1 = "Failed to configure";
      if (this.processError != null) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Failed to configure");
         var2.append(": ");
         var2.append(this.processError);
         var1 = var2.toString();
      }

      this.sendEvent(new ResourceUnavailableEvent(this, var1));
      this.processError = null;
   }

   protected void doFailedPrefetch() {
      this.state = 300;
      this.setTargetState(300);
      String var1 = "Failed to prefetch";
      if (this.processError != null) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Failed to prefetch");
         var2.append(": ");
         var2.append(this.processError);
         var1 = var2.toString();
      }

      this.sendEvent(new ResourceUnavailableEvent(this, var1));
      this.processError = null;
   }

   protected void doFailedRealize() {
      this.state = 100;
      this.setTargetState(100);
      String var1 = "Failed to realize";
      if (this.processError != null) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Failed to realize");
         var2.append(": ");
         var2.append(this.processError);
         var1 = var2.toString();
      }

      this.sendEvent(new ResourceUnavailableEvent(this, var1));
      this.processError = null;
   }

   protected abstract boolean doPrefetch();

   protected abstract boolean doRealize();

   protected void doSetMediaTime(Time var1) {
   }

   protected float doSetRate(float var1) {
      return var1;
   }

   protected abstract void doStart();

   protected void doStop() {
   }

   protected Clock getClock() {
      return this.clock;
   }

   public Control getControl(String var1) {
      Class var5;
      try {
         var5 = Class.forName(var1);
      } catch (ClassNotFoundException var4) {
         return null;
      }

      Control[] var3 = this.getControls();

      for(int var2 = 0; var2 < var3.length; ++var2) {
         if (var5.isInstance(var3[var2])) {
            return var3[var2];
         }
      }

      return null;
   }

   public Control[] getControls() {
      return new Control[0];
   }

   public Time getDuration() {
      return Duration.DURATION_UNKNOWN;
   }

   public long getMediaNanoseconds() {
      return this.clock.getMediaNanoseconds();
   }

   public Time getMediaTime() {
      return this.clock.getMediaTime();
   }

   public float getRate() {
      return this.clock.getRate();
   }

   public Time getStartLatency() {
      if (this.state < 300) {
         this.throwError(new NotRealizedError(LatencyError));
      }

      return LATENCY_UNKNOWN;
   }

   public final int getState() {
      return this.state;
   }

   public Time getStopTime() {
      return this.clock.getStopTime();
   }

   public Time getSyncTime() {
      return new Time(0L);
   }

   public final int getTargetState() {
      return this.targetState;
   }

   public TimeBase getTimeBase() {
      if (this.state < 300) {
         this.throwError(new NotRealizedError(GetTimeBaseError));
      }

      return this.clock.getTimeBase();
   }

   protected void interrupt() {
      // $FF: Couldn't be decompiled
   }

   protected abstract boolean isConfigurable();

   protected boolean isInterrupted() {
      return this.interrupted;
   }

   public Time mapToTimeBase(Time var1) throws ClockStoppedException {
      return this.clock.mapToTimeBase(var1);
   }

   public final void prefetch() {
      if (this.getTargetState() <= 300) {
         this.setTargetState(500);
      }

      int var1 = this.state;
      if (var1 != 100 && var1 != 180) {
         if (var1 != 300) {
            if (var1 == 500 || var1 == 600) {
               var1 = this.state;
               this.sendEvent(new PrefetchCompleteEvent(this, var1, var1, this.getTargetState()));
            }
         } else {
            this.state = 400;
            this.sendEvent(new TransitionEvent(this, 300, 400, this.getTargetState()));
            PrefetchWorkThread var2 = new PrefetchWorkThread(this);
            this.prefetchThread = var2;
            StringBuilder var3 = new StringBuilder();
            var3.append(this.prefetchThread.getName());
            var3.append(" ( prefetchThread)");
            var2.setName(var3.toString());
            this.prefetchThread.start();
         }
      } else {
         this.realize();
      }
   }

   public final void realize() {
      synchronized(this){}

      Throwable var10000;
      label345: {
         boolean var10001;
         try {
            if (this.getTargetState() < 300) {
               this.setTargetState(300);
            }
         } catch (Throwable var33) {
            var10000 = var33;
            var10001 = false;
            break label345;
         }

         int var1;
         try {
            var1 = this.state;
         } catch (Throwable var32) {
            var10000 = var32;
            var10001 = false;
            break label345;
         }

         if (var1 != 100) {
            if (var1 != 180) {
               if (var1 != 300 && var1 != 400 && var1 != 500 && var1 != 600) {
                  return;
               }

               try {
                  this.sendEvent(new RealizeCompleteEvent(this, this.state, this.state, this.getTargetState()));
                  return;
               } catch (Throwable var29) {
                  var10000 = var29;
                  var10001 = false;
                  break label345;
               }
            }
         } else {
            try {
               if (this.isConfigurable()) {
                  this.configure();
                  return;
               }
            } catch (Throwable var31) {
               var10000 = var31;
               var10001 = false;
               break label345;
            }
         }

         label329:
         try {
            var1 = this.state;
            this.state = 200;
            this.sendEvent(new TransitionEvent(this, var1, 200, this.getTargetState()));
            RealizeWorkThread var34 = new RealizeWorkThread(this);
            this.realizeThread = var34;
            StringBuilder var3 = new StringBuilder();
            var3.append(this.realizeThread.getName());
            var3.append("[ ");
            var3.append(this);
            var3.append(" ] ( realizeThread)");
            var34.setName(var3.toString());
            this.realizeThread.start();
            return;
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            break label329;
         }
      }

      Throwable var2 = var10000;
      throw var2;
   }

   public final void removeControllerListener(ControllerListener param1) {
      // $FF: Couldn't be decompiled
   }

   protected void resetInterrupt() {
      // $FF: Couldn't be decompiled
   }

   protected final void sendEvent(ControllerEvent var1) {
      SendEventQueue var2 = this.sendEvtQueue;
      if (var2 != null) {
         var2.postEvent(var1);
      }

   }

   protected void setClock(Clock var1) {
      this.clock = var1;
   }

   protected void setMediaLength(long var1) {
      Clock var3 = this.clock;
      if (var3 instanceof BasicClock) {
         ((BasicClock)var3).setMediaLength(var1);
      }

   }

   public void setMediaTime(Time var1) {
      if (this.state < 300) {
         this.throwError(new NotRealizedError(MediaTimeError));
      }

      this.clock.setMediaTime(var1);
      this.doSetMediaTime(var1);
      this.sendEvent(new MediaTimeSetEvent(this, var1));
   }

   public float setRate(float var1) {
      if (this.state < 300) {
         this.throwError(new NotRealizedError(SetRateError));
      }

      float var2 = this.getRate();
      var1 = this.doSetRate(var1);
      var1 = this.clock.setRate(var1);
      if (var1 != var2) {
         this.sendEvent(new RateChangeEvent(this, var1));
      }

      return var1;
   }

   public void setStopTime(Time var1) {
      if (this.state < 300) {
         this.throwError(new NotRealizedError(StopTimeError));
      }

      Time var6 = this.getStopTime();
      this.clock.setStopTime(var1);
      boolean var3 = false;
      boolean var2 = var3;
      if (this.state == 600) {
         label26: {
            long var4 = this.checkStopTime();
            if (var4 >= 0L) {
               var2 = var3;
               if (!this.stopThreadEnabled) {
                  break label26;
               }

               var2 = var3;
               if (!this.activateStopThread(var4)) {
                  break label26;
               }
            }

            var2 = true;
         }
      }

      if (var6.getNanoseconds() != var1.getNanoseconds()) {
         this.sendEvent(new StopTimeChangeEvent(this, var1));
      }

      if (var2) {
         this.stopAtTime();
      }

   }

   protected final void setTargetState(int var1) {
      this.targetState = var1;
   }

   public void setTimeBase(TimeBase var1) throws IncompatibleTimeBaseException {
      if (this.state < 300) {
         this.throwError(new NotRealizedError(TimeBaseError));
      }

      this.clock.setTimeBase(var1);
   }

   public void stop() {
      int var1 = this.state;
      if (var1 == 600 || var1 == 400) {
         this.stopControllerOnly();
         this.doStop();
      }

   }

   protected void stopAtTime() {
      this.stop();
      this.setStopTime(Clock.RESET);
      this.sendEvent(new StopAtTimeEvent(this, 600, 500, this.getTargetState(), this.getMediaTime()));
   }

   protected void stopControllerOnly() {
      int var1 = this.state;
      if (var1 == 600 || var1 == 400) {
         this.clock.stop();
         this.state = 500;
         this.setTargetState(500);
         StopTimeThread var2 = this.stopTimeThread;
         if (var2 != null && var2.isAlive()) {
            Thread var4 = Thread.currentThread();
            StopTimeThread var3 = this.stopTimeThread;
            if (var4 != var3) {
               var3.abort();
            }
         }

         TimedStartThread var5 = this.startThread;
         if (var5 != null && var5.isAlive()) {
            this.startThread.abort();
         }
      }

   }

   public void syncStart(Time var1) {
      if (this.state < 500) {
         this.throwError(new NotPrefetchedError(SyncStartError));
      }

      this.clock.syncStart(var1);
      this.state = 600;
      this.setTargetState(600);
      this.sendEvent(new StartEvent(this, 500, 600, 600, this.getMediaTime(), var1));
      long var2 = this.checkStopTime();
      if (var2 < 0L || this.stopThreadEnabled && this.activateStopThread(var2)) {
         this.stopAtTime();
      } else {
         TimedStartThread var5 = new TimedStartThread(this, var1.getNanoseconds());
         this.startThread = var5;
         StringBuilder var4 = new StringBuilder();
         var4.append(this.startThread.getName());
         var4.append(" ( startThread: ");
         var4.append(this);
         var4.append(" )");
         var5.setName(var4.toString());
         this.startThread.start();
      }
   }

   protected boolean syncStartInProgress() {
      TimedStartThread var1 = this.startThread;
      return var1 != null && var1.isAlive();
   }

   protected void throwError(Error var1) {
      Log.dumpStack(var1);
      throw var1;
   }
}
