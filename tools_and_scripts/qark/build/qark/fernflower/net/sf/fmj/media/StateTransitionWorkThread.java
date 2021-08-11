package net.sf.fmj.media;

import net.sf.fmj.media.util.MediaThread;

abstract class StateTransitionWorkThread extends MediaThread {
   boolean allEventsArrived = false;
   BasicController controller;

   StateTransitionWorkThread() {
      this.useControlPriority();
   }

   protected abstract void aborted();

   protected abstract void completed();

   protected abstract void failed();

   protected abstract boolean process();

   public void run() {
      this.controller.resetInterrupt();

      label32: {
         label31: {
            boolean var1;
            boolean var10001;
            try {
               var1 = this.process();
               if (this.controller.isInterrupted()) {
                  this.aborted();
                  break label32;
               }
            } catch (OutOfMemoryError var5) {
               var10001 = false;
               break label31;
            }

            if (var1) {
               try {
                  this.completed();
                  break label32;
               } catch (OutOfMemoryError var3) {
                  var10001 = false;
               }
            } else {
               try {
                  this.failed();
                  break label32;
               } catch (OutOfMemoryError var4) {
                  var10001 = false;
               }
            }
         }

         System.err.println("Out of memory!");
      }

      this.controller.resetInterrupt();
   }
}
