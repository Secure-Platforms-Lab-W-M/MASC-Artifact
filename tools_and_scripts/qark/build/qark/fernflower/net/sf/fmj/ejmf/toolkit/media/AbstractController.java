package net.sf.fmj.ejmf.toolkit.media;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.media.ClockStartedError;
import javax.media.ClockStoppedException;
import javax.media.Control;
import javax.media.Controller;
import javax.media.ControllerClosedEvent;
import javax.media.ControllerErrorEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DataStarvedEvent;
import javax.media.DeallocateEvent;
import javax.media.EndOfMediaEvent;
import javax.media.IncompatibleTimeBaseException;
import javax.media.MediaTimeSetEvent;
import javax.media.NotPrefetchedError;
import javax.media.NotRealizedError;
import javax.media.PrefetchCompleteEvent;
import javax.media.RateChangeEvent;
import javax.media.RealizeCompleteEvent;
import javax.media.RestartingEvent;
import javax.media.StartEvent;
import javax.media.StopAtTimeEvent;
import javax.media.StopByRequestEvent;
import javax.media.StopEvent;
import javax.media.StopTimeChangeEvent;
import javax.media.Time;
import javax.media.TimeBase;
import javax.media.TransitionEvent;
import net.sf.fmj.ejmf.toolkit.controls.RateControl;
import net.sf.fmj.utility.LoggerSingleton;

public abstract class AbstractController extends AbstractClock implements Controller {
   private static final Logger logger;
   private Vector controls = new Vector();
   private int currentState = 100;
   private ControllerEventQueue eventqueue;
   private Vector listeners = new Vector();
   private int previousState;
   private StopTimeMonitor stopTimeMonitor;
   private int targetState;
   private ThreadQueue threadqueue;
   private Object threadqueueMutex = new Object();

   static {
      logger = LoggerSingleton.logger;
   }

   public AbstractController() {
      Vector var1 = this.listeners;
      StringBuilder var2 = new StringBuilder();
      var2.append("ControllerEventQueue for ");
      var2.append(this);
      this.eventqueue = new ControllerEventQueue(var1, var2.toString());
      StringBuilder var3 = new StringBuilder();
      var3.append("StopTimeMonitor for ");
      var3.append(this);
      this.stopTimeMonitor = new StopTimeMonitor(this, var3.toString());
      this.eventqueue.start();
      this.stopTimeMonitor.start();
      this.addControl(new RateControl(this));
   }

   public void addControl(Control var1) {
      Vector var2 = this.controls;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (!this.controls.contains(var1)) {
               this.controls.addElement(var1);
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label119;
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

   public void addControllerListener(ControllerListener var1) {
      Vector var2 = this.listeners;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            if (!this.listeners.contains(var1)) {
               this.listeners.addElement(var1);
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label119;
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

   public void blockUntilStart(Time var1) {
      Time var4 = this.getStartLatency();
      long var2;
      if (var4 == LATENCY_UNKNOWN) {
         var2 = 0L;
      } else {
         var2 = var4.getNanoseconds();
      }

      var2 = (var1.getNanoseconds() - var2 - this.getTimeBase().getNanoseconds()) / 1000000L;
      if (var2 > 0L) {
         try {
            Thread.sleep(var2);
            return;
         } catch (InterruptedException var5) {
         }
      }

   }

   public final void close() {
      synchronized(this){}

      try {
         this.stop();
         this.doClose();
         this.controls = null;
         this.postControllerClosedEvent();
      } finally {
         ;
      }

   }

   public final void deallocate() {
      // $FF: Couldn't be decompiled
   }

   public abstract void doClose();

   public abstract boolean doDeallocate();

   public abstract boolean doPrefetch();

   public abstract boolean doRealize();

   public abstract void doSetMediaTime(Time var1);

   public abstract float doSetRate(float var1);

   public abstract boolean doStop();

   public abstract boolean doSyncStart(Time var1);

   protected void endOfMedia() throws ClockStoppedException {
      synchronized(this){}

      try {
         if (this.currentState != 600) {
            throw new ClockStoppedException();
         }

         super.stop();
         this.setState(500);
         this.setTargetState(500);
         this.postEndOfMediaEvent();
      } finally {
         ;
      }

   }

   public Control getControl(String var1) {
      Class var4;
      try {
         var4 = Class.forName(var1);
      } catch (Exception var26) {
         return null;
      }

      Vector var31 = this.controls;
      synchronized(var31){}
      int var2 = 0;

      Throwable var10000;
      boolean var10001;
      label234: {
         int var3;
         try {
            var3 = this.controls.size();
         } catch (Throwable var29) {
            var10000 = var29;
            var10001 = false;
            break label234;
         }

         while(true) {
            if (var2 >= var3) {
               try {
                  return null;
               } catch (Throwable var28) {
                  var10000 = var28;
                  var10001 = false;
                  break;
               }
            }

            try {
               Control var5 = (Control)this.controls.elementAt(var2);
               if (var4.isInstance(var5)) {
                  return var5;
               }
            } catch (Throwable var30) {
               var10000 = var30;
               var10001 = false;
               break;
            }

            ++var2;
         }
      }

      while(true) {
         Throwable var32 = var10000;

         try {
            throw var32;
         } catch (Throwable var27) {
            var10000 = var27;
            var10001 = false;
            continue;
         }
      }
   }

   public Control[] getControls() {
      // $FF: Couldn't be decompiled
   }

   public Time getDuration() {
      return DURATION_UNKNOWN;
   }

   public Time getMediaTime() {
      synchronized(this){}
      boolean var8 = false;

      long var1;
      long var3;
      Time var5;
      Time var6;
      try {
         var8 = true;
         var5 = super.getMediaTime();
         var6 = this.getDuration();
         if (var6 == DURATION_UNKNOWN) {
            var8 = false;
            return var5;
         }

         if (var6 == DURATION_UNBOUNDED) {
            var8 = false;
            return var5;
         }

         var1 = var5.getNanoseconds();
         var3 = var6.getNanoseconds();
         var8 = false;
      } finally {
         if (var8) {
            ;
         }
      }

      if (var1 > var3) {
         return var6;
      } else {
         return var5;
      }
   }

   public int getPreviousState() {
      return this.previousState;
   }

   public Time getStartLatency() {
      int var1 = this.currentState;
      if (var1 != 100 && var1 != 200) {
         return LATENCY_UNKNOWN;
      } else {
         throw new NotRealizedError("Cannot get start latency from an unrealized Controller.");
      }
   }

   public int getState() {
      return this.currentState;
   }

   public int getTargetState() {
      return this.targetState;
   }

   protected ThreadQueue getThreadQueue() {
      Object var1 = this.threadqueueMutex;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label122: {
         ThreadQueue var15;
         try {
            if (this.threadqueue == null) {
               StringBuilder var2 = new StringBuilder();
               var2.append("ThreadQueue for ");
               var2.append(this);
               var15 = new ThreadQueue(var2.toString());
               this.threadqueue = var15;
               var15.start();
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            var15 = this.threadqueue;
            return var15;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var16 = var10000;

         try {
            throw var16;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public TimeBase getTimeBase() {
      synchronized(this){}

      TimeBase var1;
      try {
         if (this.currentState == 100 || this.currentState == 200) {
            throw new NotRealizedError("Cannot get time base from an Unrealized Controller");
         }

         var1 = super.getTimeBase();
      } finally {
         ;
      }

      return var1;
   }

   protected void postControllerClosedEvent() {
      this.postEvent(new ControllerClosedEvent(this));
   }

   protected void postControllerErrorEvent(String var1) {
      this.postEvent(new ControllerErrorEvent(this, var1));
   }

   protected void postDataStarvedEvent() {
      this.postEvent(new DataStarvedEvent(this, this.previousState, this.currentState, this.targetState, this.getMediaTime()));
   }

   protected void postDeallocateEvent() {
      this.postEvent(new DeallocateEvent(this, this.previousState, this.currentState, this.targetState, this.getMediaTime()));
   }

   protected void postEndOfMediaEvent() {
      this.postEvent(new EndOfMediaEvent(this, this.previousState, this.currentState, this.targetState, this.getMediaTime()));
   }

   protected void postEvent(ControllerEvent var1) {
      this.eventqueue.postEvent(var1);
   }

   protected void postPrefetchCompleteEvent() {
      this.postEvent(new PrefetchCompleteEvent(this, this.previousState, this.currentState, this.targetState));
   }

   protected void postRealizeCompleteEvent() {
      this.postEvent(new RealizeCompleteEvent(this, this.previousState, this.currentState, this.targetState));
   }

   protected void postRestartingEvent() {
      this.postEvent(new RestartingEvent(this, this.previousState, this.currentState, this.targetState, this.getMediaTime()));
   }

   protected void postStartEvent() {
      this.postEvent(new StartEvent(this, this.previousState, this.currentState, this.targetState, this.getMediaStartTime(), this.getTimeBaseStartTime()));
   }

   protected void postStopAtTimeEvent() {
      this.postEvent(new StopAtTimeEvent(this, this.previousState, this.currentState, this.targetState, this.getMediaTime()));
   }

   protected void postStopByRequestEvent() {
      this.postEvent(new StopByRequestEvent(this, this.previousState, this.currentState, this.targetState, this.getMediaTime()));
   }

   protected void postStopEvent() {
      this.postEvent(new StopEvent(this, this.previousState, this.currentState, this.targetState, this.getMediaTime()));
   }

   protected void postTransitionEvent() {
      this.postEvent(new TransitionEvent(this, this.previousState, this.currentState, this.targetState));
   }

   public final void prefetch() {
      synchronized(this){}

      try {
         if (this.currentState < 500) {
            if (this.targetState < 500) {
               this.setTargetState(500);
            }

            Thread var1 = new Thread("Controller Prefetch Thread") {
               public void run() {
                  if (AbstractController.this.getState() < 500) {
                     AbstractController.this.synchronousPrefetch();
                  }

               }
            };
            this.getThreadQueue().addThread(var1);
            return;
         }

         this.postPrefetchCompleteEvent();
      } finally {
         ;
      }

   }

   public final void realize() {
      synchronized(this){}

      try {
         if (this.currentState >= 300) {
            this.postRealizeCompleteEvent();
            return;
         }

         if (this.targetState < 300) {
            this.setTargetState(300);
         }

         Thread var1 = new Thread("Controller Realize Thread") {
            public void run() {
               if (AbstractController.this.getState() < 300) {
                  AbstractController.this.synchronousRealize();
               }

            }
         };
         this.getThreadQueue().addThread(var1);
      } finally {
         ;
      }

   }

   public void removeControl(Control var1) {
      this.controls.removeElement(var1);
   }

   public void removeControllerListener(ControllerListener param1) {
      // $FF: Couldn't be decompiled
   }

   public void setMediaTime(Time var1) {
      synchronized(this){}

      Throwable var10000;
      label420: {
         long var2;
         Time var7;
         boolean var10001;
         label415: {
            try {
               if (this.currentState != 100 && this.currentState != 200) {
                  var2 = var1.getNanoseconds();
                  var7 = this.getDuration();
                  break label415;
               }
            } catch (Throwable var49) {
               var10000 = var49;
               var10001 = false;
               break label420;
            }

            try {
               throw new NotRealizedError("Cannot set media time on an Unrealized Controller");
            } catch (Throwable var48) {
               var10000 = var48;
               var10001 = false;
               break label420;
            }
         }

         Time var6 = var1;

         label421: {
            try {
               if (var7 == DURATION_UNKNOWN) {
                  break label421;
               }
            } catch (Throwable var47) {
               var10000 = var47;
               var10001 = false;
               break label420;
            }

            var6 = var1;

            long var4;
            try {
               if (var7 == DURATION_UNBOUNDED) {
                  break label421;
               }

               var4 = var7.getNanoseconds();
            } catch (Throwable var46) {
               var10000 = var46;
               var10001 = false;
               break label420;
            }

            var6 = var1;
            if (var2 > var4) {
               try {
                  var6 = new Time(var4);
               } catch (Throwable var45) {
                  var10000 = var45;
                  var10001 = false;
                  break label420;
               }
            }
         }

         try {
            super.setMediaTime(var6);
            this.doSetMediaTime(var6);
            this.postEvent(new MediaTimeSetEvent(this, var6));
         } catch (Throwable var44) {
            var10000 = var44;
            var10001 = false;
            break label420;
         }

         return;
      }

      Throwable var50 = var10000;
      throw var50;
   }

   public float setRate(float var1) {
      synchronized(this){}

      Throwable var10000;
      label324: {
         float var3;
         float var4;
         float var5;
         boolean var10001;
         label319: {
            try {
               if (this.currentState != 100 && this.currentState != 200) {
                  var4 = this.getRate();
                  var3 = super.setRate(var1);
                  var5 = this.doSetRate(var3);
                  break label319;
               }
            } catch (Throwable var36) {
               var10000 = var36;
               var10001 = false;
               break label324;
            }

            try {
               throw new NotRealizedError("Cannot set rate on an Unrealized Controller.");
            } catch (Throwable var35) {
               var10000 = var35;
               var10001 = false;
               break label324;
            }
         }

         float var2 = var3;
         if (var1 != 1.0F) {
            var2 = var3;
            if (var3 != var5) {
               try {
                  var1 = super.setRate(var5);
               } catch (Throwable var34) {
                  var10000 = var34;
                  var10001 = false;
                  break label324;
               }

               var2 = var1;
               if (var1 != var5) {
                  try {
                     var1 = this.setRate(1.0F);
                  } catch (Throwable var32) {
                     var10000 = var32;
                     var10001 = false;
                     break label324;
                  }

                  return var1;
               }
            }
         }

         if (var2 != var4) {
            try {
               this.postEvent(new RateChangeEvent(this, var2));
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label324;
            }
         }

         return var2;
      }

      Throwable var6 = var10000;
      throw var6;
   }

   protected void setState(int var1) {
      synchronized(this){}

      Throwable var10000;
      label78: {
         boolean var10001;
         int var2;
         try {
            var2 = this.currentState;
         } catch (Throwable var9) {
            var10000 = var9;
            var10001 = false;
            break label78;
         }

         if (var1 == var2) {
            return;
         }

         try {
            this.previousState = this.currentState;
            this.currentState = var1;
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label78;
         }

         return;
      }

      Throwable var3 = var10000;
      throw var3;
   }

   public void setStopTime(Time var1) {
      synchronized(this){}

      try {
         if (this.currentState == 100 || this.currentState == 200) {
            throw new NotRealizedError("Cannot set stop time on an unrealized Controller");
         }

         Time var2 = this.getStopTime();
         if (var1.getNanoseconds() != var2.getNanoseconds()) {
            super.setStopTime(var1);
            this.postEvent(new StopTimeChangeEvent(this, var1));
         }
      } finally {
         ;
      }

   }

   protected void setTargetState(int var1) {
      this.targetState = var1;
   }

   public void setTimeBase(TimeBase var1) throws IncompatibleTimeBaseException {
      synchronized(this){}

      try {
         if (this.currentState == 100 || this.currentState == 200) {
            throw new NotRealizedError("Cannot set TimeBase on an Unrealized Controller.");
         }

         super.setTimeBase(var1);
      } finally {
         ;
      }

   }

   public final void stop() {
      if (this.stopController()) {
         this.postStopByRequestEvent();
      }

   }

   protected void stopAtTime() {
      if (this.stopController()) {
         this.postStopAtTimeEvent();
      }

   }

   protected boolean stopController() {
      // $FF: Couldn't be decompiled
   }

   protected void stopInRestart() {
      if (this.stopController()) {
         this.postRestartingEvent();
      }

   }

   public final void syncStart(final Time var1) {
      synchronized(this){}

      try {
         if (this.currentState == 600) {
            throw new ClockStartedError("syncStart() cannot be called on a started Clock");
         }

         if (this.currentState != 500) {
            throw new NotPrefetchedError("Cannot start the Controller before it has been prefetched");
         }

         this.setTargetState(600);
         Thread var4 = new Thread("Controller Start Thread") {
            public void run() {
               if (AbstractController.this.getState() < 600) {
                  AbstractController.this.synchronousSyncStart(var1);
               }

            }
         };
         this.getThreadQueue().addThread(var4);
      } finally {
         ;
      }

   }

   protected void synchronousPrefetch() {
      if (this.currentState < 300) {
         this.synchronousRealize();
         if (this.currentState < 300) {
            return;
         }
      }

      this.setState(400);
      this.postTransitionEvent();

      boolean var1;
      label42:
      try {
         var1 = this.doPrefetch();
      } catch (Throwable var7) {
         Logger var3 = logger;
         Level var4 = Level.WARNING;
         StringBuilder var5 = new StringBuilder();
         var5.append("");
         var5.append(var7);
         var3.log(var4, var5.toString(), var7);
         StringBuilder var8 = new StringBuilder();
         var8.append("");
         var8.append(var7);
         this.postControllerErrorEvent(var8.toString());
         var1 = false;
         break label42;
      }

      if (var1) {
         this.setState(500);
         this.postPrefetchCompleteEvent();
      } else {
         this.setState(300);
         this.setTargetState(300);
      }
   }

   protected void synchronousRealize() {
      this.setState(200);
      this.postTransitionEvent();

      boolean var1;
      label32:
      try {
         var1 = this.doRealize();
      } catch (Throwable var7) {
         Logger var3 = logger;
         Level var4 = Level.WARNING;
         StringBuilder var5 = new StringBuilder();
         var5.append("");
         var5.append(var7);
         var3.log(var4, var5.toString(), var7);
         StringBuilder var8 = new StringBuilder();
         var8.append("");
         var8.append(var7);
         this.postControllerErrorEvent(var8.toString());
         var1 = false;
         break label32;
      }

      if (var1) {
         this.setState(300);
         this.postRealizeCompleteEvent();
         this.setRate(1.0F);
      } else {
         this.setState(100);
         this.setTargetState(100);
      }
   }

   protected void synchronousSyncStart(Time var1) {
      this.setState(600);
      this.postStartEvent();
      Time var9 = this.getStartLatency();
      long var2;
      if (var9 == LATENCY_UNKNOWN) {
         var2 = 0L;
      } else {
         var2 = var9.getNanoseconds();
      }

      long var4 = var1.getNanoseconds();
      long var6 = this.getTimeBase().getNanoseconds();
      if (var6 + var2 > var4) {
         var1 = new Time(var6 + var2);
      }

      super.syncStart(var1);

      boolean var8;
      label47:
      try {
         var8 = this.doSyncStart(var1);
      } catch (Throwable var13) {
         Logger var14 = logger;
         Level var10 = Level.WARNING;
         StringBuilder var11 = new StringBuilder();
         var11.append("");
         var11.append(var13);
         var14.log(var10, var11.toString(), var13);
         StringBuilder var15 = new StringBuilder();
         var15.append("");
         var15.append(var13);
         this.postControllerErrorEvent(var15.toString());
         var8 = false;
         break label47;
      }

      if (!var8) {
         this.setState(500);
         this.setTargetState(500);
      }

   }
}
