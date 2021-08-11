package net.sf.fmj.ejmf.toolkit.util;

import javax.media.Controller;
import javax.media.ControllerClosedEvent;
import javax.media.ControllerEvent;
import javax.media.ControllerListener;
import javax.media.Player;
import javax.media.Processor;
import javax.media.StopEvent;
import javax.media.Time;
import javax.media.TransitionEvent;

public class StateWaiter implements ControllerListener {
   private Controller controller;
   private boolean listening = false;
   private int state;
   private boolean stateReached = false;

   public StateWaiter(Controller var1) {
      this.controller = var1;
   }

   private void addAsListener() {
      if (!this.listening) {
         this.controller.addControllerListener(this);
         this.listening = true;
      }

   }

   private void removeAsListener() {
      this.controller.removeControllerListener(this);
      this.listening = false;
   }

   private void setState(int var1) {
      this.state = var1;
      this.stateReached = false;
      this.addAsListener();
   }

   private boolean waitForState() {
      // $FF: Couldn't be decompiled
   }

   public boolean blockingConfigure() {
      this.setState(180);
      ((Processor)this.controller).configure();
      return this.waitForState();
   }

   public boolean blockingPrefetch() {
      this.setState(500);
      this.controller.prefetch();
      return this.waitForState();
   }

   public boolean blockingRealize() {
      this.setState(300);
      this.controller.realize();
      return this.waitForState();
   }

   public boolean blockingStart() {
      this.setState(600);
      ((Player)this.controller).start();
      return this.waitForState();
   }

   public boolean blockingSyncStart(Time var1) {
      this.setState(600);
      this.controller.syncStart(var1);
      return this.waitForState();
   }

   public boolean blockingWait(int var1) {
      this.setState(var1);
      return this.waitForState();
   }

   public void controllerUpdate(ControllerEvent var1) {
      synchronized(this){}

      Throwable var10000;
      label348: {
         Controller var3;
         Controller var4;
         boolean var10001;
         try {
            var3 = var1.getSourceController();
            var4 = this.controller;
         } catch (Throwable var34) {
            var10000 = var34;
            var10001 = false;
            break label348;
         }

         if (var3 != var4) {
            return;
         }

         label338: {
            boolean var2;
            label337: {
               label336: {
                  try {
                     if (!(var1 instanceof TransitionEvent)) {
                        break label338;
                     }

                     if (((TransitionEvent)var1).getCurrentState() >= this.state) {
                        break label336;
                     }
                  } catch (Throwable var33) {
                     var10000 = var33;
                     var10001 = false;
                     break label348;
                  }

                  var2 = false;
                  break label337;
               }

               var2 = true;
            }

            try {
               this.stateReached = var2;
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label348;
            }
         }

         try {
            if (!(var1 instanceof StopEvent) && !(var1 instanceof ControllerClosedEvent) && !this.stateReached) {
               return;
            }
         } catch (Throwable var31) {
            var10000 = var31;
            var10001 = false;
            break label348;
         }

         label323:
         try {
            this.removeAsListener();
            this.notifyAll();
            return;
         } catch (Throwable var30) {
            var10000 = var30;
            var10001 = false;
            break label323;
         }
      }

      Throwable var35 = var10000;
      throw var35;
   }
}
