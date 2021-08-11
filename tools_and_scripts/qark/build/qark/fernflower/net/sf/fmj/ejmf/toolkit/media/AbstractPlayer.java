package net.sf.fmj.ejmf.toolkit.media;

import java.io.IOException;
import java.util.Vector;
import javax.media.Clock;
import javax.media.ClockStartedError;
import javax.media.ClockStoppedException;
import javax.media.Controller;
import javax.media.ControllerErrorEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.DurationUpdateEvent;
import javax.media.GainControl;
import javax.media.IncompatibleSourceException;
import javax.media.IncompatibleTimeBaseException;
import javax.media.NotRealizedError;
import javax.media.Player;
import javax.media.ResourceUnavailableEvent;
import javax.media.Time;
import javax.media.TransitionEvent;
import javax.media.protocol.DataSource;
import net.sf.fmj.ejmf.toolkit.media.event.ManagedControllerErrorEvent;
import org.atalk.android.util.java.awt.Component;

public abstract class AbstractPlayer extends AbstractController implements Player, ControllerListener {
   private Component controlPanelComponent;
   private ControllerErrorEvent controllerError;
   private Vector controllers = new Vector();
   private Time duration = new Time(0L);
   private GainControl gainControl;
   private DataSource source;
   private Component visualComponent;

   private boolean areControllersStopped() {
      Vector var3 = this.controllers;
      synchronized(var3){}
      int var1 = 0;

      Throwable var10000;
      boolean var10001;
      label213: {
         int var2;
         try {
            var2 = this.controllers.size();
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label213;
         }

         while(true) {
            if (var1 >= var2) {
               try {
                  return true;
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break;
               }
            }

            try {
               if (((Controller)this.controllers.elementAt(var1)).getState() == 600) {
                  return false;
               }
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break;
            }

            ++var1;
         }
      }

      while(true) {
         Throwable var4 = var10000;

         try {
            throw var4;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            continue;
         }
      }
   }

   private ControllerErrorEvent getControllerError() {
      return this.controllerError;
   }

   private boolean isStateReached(int var1) {
      Vector var4 = this.controllers;
      synchronized(var4){}
      int var2 = 0;

      Throwable var10000;
      boolean var10001;
      label213: {
         int var3;
         try {
            var3 = this.controllers.size();
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label213;
         }

         while(true) {
            if (var2 >= var3) {
               try {
                  return true;
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break;
               }
            }

            try {
               if (((Controller)this.controllers.elementAt(var2)).getState() < var1) {
                  return false;
               }
            } catch (Throwable var25) {
               var10000 = var25;
               var10001 = false;
               break;
            }

            ++var2;
         }
      }

      while(true) {
         Throwable var5 = var10000;

         try {
            throw var5;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            continue;
         }
      }
   }

   private void postManagedControllerErrorEvent() {
      StringBuilder var1 = new StringBuilder();
      var1.append("Managing Player ");
      var1.append(this.getClass().getName());
      var1.append(" received ControllerErrorEvent from ");
      var1.append(this.controllerError.getSourceController().getClass().getName());
      String var2 = var1.toString();
      this.postEvent(new ManagedControllerErrorEvent(this, this.controllerError, var2));
      this.resetControllerError();
   }

   private void resetControllerError() {
      this.setControllerError((ControllerErrorEvent)null);
   }

   private void setControllerError(ControllerErrorEvent var1) {
      this.controllerError = var1;
   }

   private final void updateDuration() {
      synchronized(this){}

      Throwable var10000;
      label1473: {
         Time var3;
         boolean var10001;
         try {
            var3 = this.getPlayerDuration();
         } catch (Throwable var137) {
            var10000 = var137;
            var10001 = false;
            break label1473;
         }

         Time var4 = var3;

         label1474: {
            try {
               if (var3 == DURATION_UNKNOWN) {
                  break label1474;
               }
            } catch (Throwable var136) {
               var10000 = var136;
               var10001 = false;
               break label1473;
            }

            int var1 = 0;

            int var2;
            try {
               var2 = this.controllers.size();
            } catch (Throwable var132) {
               var10000 = var132;
               var10001 = false;
               break label1473;
            }

            while(true) {
               var4 = var3;
               if (var1 >= var2) {
                  break;
               }

               try {
                  var4 = ((Controller)this.controllers.elementAt(var1)).getDuration();
                  if (var4 == DURATION_UNKNOWN) {
                     break;
                  }
               } catch (Throwable var133) {
                  var10000 = var133;
                  var10001 = false;
                  break label1473;
               }

               Time var5 = var3;

               label1455: {
                  label1476: {
                     try {
                        if (var3 == DURATION_UNBOUNDED) {
                           break label1455;
                        }

                        if (var4 == DURATION_UNBOUNDED) {
                           break label1476;
                        }
                     } catch (Throwable var135) {
                        var10000 = var135;
                        var10001 = false;
                        break label1473;
                     }

                     var5 = var3;

                     try {
                        if (var4.getNanoseconds() <= var3.getNanoseconds()) {
                           break label1455;
                        }
                     } catch (Throwable var134) {
                        var10000 = var134;
                        var10001 = false;
                        break label1473;
                     }
                  }

                  var5 = var4;
               }

               ++var1;
               var3 = var5;
            }
         }

         boolean var138 = false;

         label1477: {
            label1482: {
               label1430:
               try {
                  if (var4 != DURATION_UNKNOWN && var4 != DURATION_UNBOUNDED && this.duration != DURATION_UNKNOWN && this.duration != DURATION_UNBOUNDED) {
                     break label1430;
                  }
                  break label1482;
               } catch (Throwable var131) {
                  var10000 = var131;
                  var10001 = false;
                  break label1473;
               }

               label1418: {
                  try {
                     if (this.duration == null) {
                        break label1418;
                     }
                  } catch (Throwable var130) {
                     var10000 = var130;
                     var10001 = false;
                     break label1473;
                  }

                  try {
                     if (var4.getNanoseconds() == this.duration.getNanoseconds()) {
                        break label1477;
                     }
                  } catch (Throwable var129) {
                     var10000 = var129;
                     var10001 = false;
                     break label1473;
                  }
               }

               var138 = true;
               break label1477;
            }

            try {
               if (this.duration == var4) {
                  break label1477;
               }
            } catch (Throwable var128) {
               var10000 = var128;
               var10001 = false;
               break label1473;
            }

            var138 = true;
         }

         if (!var138) {
            return;
         }

         label1405:
         try {
            this.duration = var4;
            this.postEvent(new DurationUpdateEvent(this, var4));
            return;
         } catch (Throwable var127) {
            var10000 = var127;
            var10001 = false;
            break label1405;
         }
      }

      Throwable var139 = var10000;
      throw var139;
   }

   public void addController(Controller var1) throws IncompatibleTimeBaseException {
      synchronized(this){}

      Throwable var10000;
      label1133: {
         boolean var10001;
         try {
            if (this.controllers.contains(var1)) {
               return;
            }
         } catch (Throwable var135) {
            var10000 = var135;
            var10001 = false;
            break label1133;
         }

         if (this == var1) {
            return;
         }

         int var3;
         try {
            var3 = this.getState();
         } catch (Throwable var134) {
            var10000 = var134;
            var10001 = false;
            break label1133;
         }

         if (var3 != 100 && var3 != 200) {
            if (var3 != 600) {
               try {
                  var3 = var1.getState();
               } catch (Throwable var131) {
                  var10000 = var131;
                  var10001 = false;
                  break label1133;
               }

               if (var3 != 100 && var3 != 200) {
                  if (var3 != 600) {
                     label1091: {
                        try {
                           var1.setTimeBase(this.getTimeBase());
                           this.stop();
                           var1.stop();
                           var3 = this.getState();
                           if (var1.getState() >= 500) {
                              break label1091;
                           }
                        } catch (Throwable var128) {
                           var10000 = var128;
                           var10001 = false;
                           break label1133;
                        }

                        if (var3 > 300) {
                           try {
                              this.deallocate();
                           } catch (Throwable var127) {
                              var10000 = var127;
                              var10001 = false;
                              break label1133;
                           }
                        }
                     }

                     try {
                        var1.setMediaTime(this.getMediaTime());
                        var1.setStopTime(Clock.RESET);
                        float var2 = this.getRate();
                        if (var2 != var1.setRate(var2)) {
                           var1.setRate(1.0F);
                           this.setRate(1.0F);
                        }
                     } catch (Throwable var126) {
                        var10000 = var126;
                        var10001 = false;
                        break label1133;
                     }

                     try {
                        this.controllers.addElement(var1);
                        var1.addControllerListener(this);
                        this.updateDuration();
                     } catch (Throwable var125) {
                        var10000 = var125;
                        var10001 = false;
                        break label1133;
                     }

                     return;
                  } else {
                     try {
                        throw new ClockStartedError("Cannot add Started Controller to a Player");
                     } catch (Throwable var129) {
                        var10000 = var129;
                        var10001 = false;
                        break label1133;
                     }
                  }
               } else {
                  try {
                     throw new NotRealizedError("Cannot add Unrealized Controller to a Player");
                  } catch (Throwable var130) {
                     var10000 = var130;
                     var10001 = false;
                     break label1133;
                  }
               }
            } else {
               try {
                  throw new ClockStartedError("Cannot add Controller to a Started Player");
               } catch (Throwable var132) {
                  var10000 = var132;
                  var10001 = false;
                  break label1133;
               }
            }
         } else {
            try {
               throw new NotRealizedError("Cannot add Controller to an Unrealized Player");
            } catch (Throwable var133) {
               var10000 = var133;
               var10001 = false;
               break label1133;
            }
         }
      }

      Throwable var136 = var10000;
      throw var136;
   }

   public final void controllerUpdate(ControllerEvent var1) {
      Vector var2 = this.controllers;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label200: {
         label204: {
            try {
               if (var1 instanceof TransitionEvent) {
                  this.controllers.notifyAll();
                  break label204;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label200;
            }

            try {
               if (var1 instanceof ControllerErrorEvent) {
                  this.setControllerError((ControllerErrorEvent)var1);
                  this.controllers.notifyAll();
               }
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label200;
            }
         }

         label190:
         try {
            return;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label190;
         }
      }

      while(true) {
         Throwable var23 = var10000;

         try {
            throw var23;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            continue;
         }
      }
   }

   public final void doClose() {
      // $FF: Couldn't be decompiled
   }

   public final boolean doDeallocate() {
      this.resetControllerError();
      int var2 = this.controllers.size();
      Thread[] var3 = new Thread[var2];

      int var1;
      for(var1 = 0; var1 < var2; ++var1) {
         var3[var1] = new Thread("Player Deallocate Thread", (Controller)this.controllers.elementAt(var1)) {
            // $FF: synthetic field
            final Controller val$c;

            {
               this.val$c = var3;
            }

            public void run() {
               this.val$c.deallocate();
            }
         };
         var3[var1].start();
      }

      if (!this.doPlayerDeallocate()) {
         return false;
      } else {
         for(var1 = 0; var1 < var2; ++var1) {
            try {
               var3[var1].join();
            } catch (InterruptedException var5) {
            }
         }

         if (this.controllerError != null) {
            this.postManagedControllerErrorEvent();
            return false;
         } else {
            return true;
         }
      }
   }

   public abstract void doPlayerClose();

   public abstract boolean doPlayerDeallocate();

   public abstract boolean doPlayerPrefetch();

   public abstract boolean doPlayerRealize();

   public abstract void doPlayerSetMediaTime(Time var1);

   public abstract float doPlayerSetRate(float var1);

   public abstract boolean doPlayerStop();

   public abstract boolean doPlayerSyncStart(Time var1);

   public final boolean doPrefetch() {
      // $FF: Couldn't be decompiled
   }

   public final boolean doRealize() {
      try {
         this.source.start();
      } catch (IOException var2) {
         this.postEvent(new ResourceUnavailableEvent(this, "Could not start DataSource"));
         return false;
      }

      if (!this.doPlayerRealize()) {
         return false;
      } else {
         this.updateDuration();
         return true;
      }
   }

   public final void doSetMediaTime(Time var1) {
      synchronized(this){}
      int var2 = 0;

      Throwable var10000;
      label120: {
         boolean var10001;
         int var3;
         try {
            var3 = this.controllers.size();
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            break label120;
         }

         while(true) {
            if (var2 >= var3) {
               try {
                  this.doPlayerSetMediaTime(var1);
                  return;
               } catch (Throwable var13) {
                  var10000 = var13;
                  var10001 = false;
                  break;
               }
            }

            try {
               ((Controller)this.controllers.elementAt(var2)).setMediaTime(var1);
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break;
            }

            ++var2;
         }
      }

      Throwable var16 = var10000;
      throw var16;
   }

   public final float doSetRate(float var1) {
      synchronized(this){}
      int var3 = 0;

      Throwable var10000;
      label345: {
         int var4;
         boolean var10001;
         try {
            var4 = this.controllers.size();
         } catch (Throwable var34) {
            var10000 = var34;
            var10001 = false;
            break label345;
         }

         float var2;
         while(var3 < var4) {
            try {
               var2 = ((Controller)this.controllers.elementAt(var3)).setRate(var1);
            } catch (Throwable var33) {
               var10000 = var33;
               var10001 = false;
               break label345;
            }

            if (var1 != 1.0F && var2 != var1) {
               try {
                  this.doSetRate(1.0F);
               } catch (Throwable var31) {
                  var10000 = var31;
                  var10001 = false;
                  break label345;
               }

               return 1.0F;
            }

            ++var3;
         }

         try {
            var2 = this.doPlayerSetRate(var1);
            if (this.controllers.isEmpty()) {
               return var2;
            }
         } catch (Throwable var35) {
            var10000 = var35;
            var10001 = false;
            break label345;
         }

         if (var1 != 1.0F && var2 != var1) {
            try {
               this.doSetRate(1.0F);
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label345;
            }

            return 1.0F;
         }

         return var2;
      }

      Throwable var5 = var10000;
      throw var5;
   }

   public final boolean doStop() {
      this.resetControllerError();
      int var2 = this.controllers.size();
      Thread[] var3 = new Thread[var2];

      int var1;
      for(var1 = 0; var1 < var2; ++var1) {
         var3[var1] = new Thread("Player Stop Thread", (Controller)this.controllers.elementAt(var1)) {
            // $FF: synthetic field
            final Controller val$c;

            {
               this.val$c = var3;
            }

            public void run() {
               this.val$c.stop();
            }
         };
         var3[var1].start();
      }

      if (!this.doPlayerStop()) {
         return false;
      } else {
         for(var1 = 0; var1 < var2; ++var1) {
            try {
               var3[var1].join();
            } catch (InterruptedException var5) {
            }
         }

         if (this.controllerError != null) {
            this.postManagedControllerErrorEvent();
            return false;
         } else {
            return true;
         }
      }
   }

   public final boolean doSyncStart(Time param1) {
      // $FF: Couldn't be decompiled
   }

   protected void endOfMedia() throws ClockStoppedException {
      // $FF: Couldn't be decompiled
   }

   public Component getControlPanelComponent() {
      int var1 = this.getState();
      if (var1 != 100 && var1 != 200) {
         return this.controlPanelComponent;
      } else {
         throw new NotRealizedError("Cannot get control panel Component on an Unrealized Player");
      }
   }

   protected Vector getControllers() {
      return (Vector)this.controllers.clone();
   }

   public final Time getDuration() {
      synchronized(this){}

      Time var1;
      try {
         if (this.duration == null) {
            this.updateDuration();
         }

         var1 = this.duration;
      } finally {
         ;
      }

      return var1;
   }

   public GainControl getGainControl() {
      int var1 = this.getState();
      if (var1 != 100 && var1 != 200) {
         return this.gainControl;
      } else {
         throw new NotRealizedError("Cannot get gain control on an Unrealized Player");
      }
   }

   public abstract Time getPlayerDuration();

   public abstract Time getPlayerStartLatency();

   public DataSource getSource() {
      return this.source;
   }

   public Time getStartLatency() {
      synchronized(this){}

      Throwable var10000;
      label474: {
         int var1;
         boolean var10001;
         try {
            var1 = this.getState();
         } catch (Throwable var50) {
            var10000 = var50;
            var10001 = false;
            break label474;
         }

         if (var1 != 100 && var1 != 200) {
            label475: {
               Time var7;
               try {
                  var7 = this.getPlayerStartLatency();
               } catch (Throwable var48) {
                  var10000 = var48;
                  var10001 = false;
                  break label475;
               }

               var1 = 0;

               int var2;
               try {
                  var2 = this.controllers.size();
               } catch (Throwable var47) {
                  var10000 = var47;
                  var10001 = false;
                  break label475;
               }

               while(true) {
                  if (var1 >= var2) {
                     return var7;
                  }

                  label477: {
                     Time var8;
                     try {
                        var8 = ((Controller)this.controllers.elementAt(var1)).getStartLatency();
                        if (var8 == LATENCY_UNKNOWN) {
                           break label477;
                        }
                     } catch (Throwable var46) {
                        var10000 = var46;
                        var10001 = false;
                        break;
                     }

                     label446: {
                        long var3;
                        long var5;
                        try {
                           if (var7 == LATENCY_UNKNOWN) {
                              break label446;
                           }

                           var3 = var8.getNanoseconds();
                           var5 = var7.getNanoseconds();
                        } catch (Throwable var45) {
                           var10000 = var45;
                           var10001 = false;
                           break;
                        }

                        if (var3 <= var5) {
                           break label477;
                        }
                     }

                     var7 = var8;
                  }

                  ++var1;
               }
            }
         } else {
            label463:
            try {
               throw new NotRealizedError("Cannot get start latency from an Unrealized Controller");
            } catch (Throwable var49) {
               var10000 = var49;
               var10001 = false;
               break label463;
            }
         }
      }

      Throwable var51 = var10000;
      throw var51;
   }

   public Component getVisualComponent() {
      int var1 = this.getState();
      if (var1 != 100 && var1 != 200) {
         return this.visualComponent;
      } else {
         throw new NotRealizedError("Cannot get visual Component on an Unrealized Player");
      }
   }

   public void removeController(Controller param1) {
      // $FF: Couldn't be decompiled
   }

   protected void setControlPanelComponent(Component var1) {
      this.controlPanelComponent = var1;
   }

   protected void setGainControl(GainControl var1) {
      GainControl var2 = this.gainControl;
      if (var2 != null) {
         this.removeControl(var2);
      }

      this.addControl(var1);
      this.gainControl = var1;
   }

   public void setMediaTime(Time var1) {
      synchronized(this){}

      Throwable var10000;
      label220: {
         boolean var10001;
         boolean var2;
         label214: {
            label213: {
               try {
                  if (this.getState() == 600) {
                     break label213;
                  }
               } catch (Throwable var22) {
                  var10000 = var22;
                  var10001 = false;
                  break label220;
               }

               var2 = false;
               break label214;
            }

            var2 = true;
         }

         if (var2) {
            try {
               this.stopInRestart();
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label220;
            }
         }

         try {
            super.setMediaTime(var1);
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label220;
         }

         if (!var2) {
            return;
         }

         label199:
         try {
            this.start();
            return;
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            break label199;
         }
      }

      Throwable var23 = var10000;
      throw var23;
   }

   public float setRate(float var1) {
      synchronized(this){}

      Throwable var10000;
      label220: {
         boolean var10001;
         boolean var2;
         label214: {
            label213: {
               try {
                  if (this.getState() == 600) {
                     break label213;
                  }
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label220;
               }

               var2 = false;
               break label214;
            }

            var2 = true;
         }

         if (var2) {
            try {
               this.stopInRestart();
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label220;
            }
         }

         try {
            var1 = super.setRate(var1);
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            break label220;
         }

         if (!var2) {
            return var1;
         }

         label199:
         try {
            this.start();
            return var1;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label199;
         }
      }

      Throwable var3 = var10000;
      throw var3;
   }

   public void setSource(DataSource var1) throws IncompatibleSourceException {
      if (this.source == null) {
         this.source = var1;
      } else {
         StringBuilder var2 = new StringBuilder();
         var2.append("Datasource already set in MediaHandler ");
         var2.append(this.getClass().getName());
         throw new IncompatibleSourceException(var2.toString());
      }
   }

   protected void setVisualComponent(Component var1) {
      this.visualComponent = var1;
   }

   public final void start() {
      int var1 = this.getState();
      int var2 = this.getTargetState();
      if (var1 == 600) {
         this.postStartEvent();
      } else {
         if (var2 < 600) {
            this.setTargetState(600);
         }

         Thread var3 = new Thread("Player Start Thread") {
            public void run() {
               if (AbstractPlayer.this.getState() < 600) {
                  AbstractPlayer.this.synchronousStart();
               }

            }
         };
         StringBuilder var4 = new StringBuilder();
         var4.append("SynchronousStart Thread for ");
         var4.append(this);
         var3.setName(var4.toString());
         this.getThreadQueue().addThread(var3);
      }
   }

   protected void synchronousStart() {
      if (this.getState() < 500) {
         this.synchronousPrefetch();
         if (this.getState() < 500) {
            return;
         }
      }

      this.synchronousSyncStart(this.getTimeBase().getTime());
   }
}
