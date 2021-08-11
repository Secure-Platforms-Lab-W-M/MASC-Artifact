package net.sf.fmj.media.util;

import java.io.PrintStream;
import java.util.List;
import java.util.Vector;
import javax.media.ControllerEvent;

public abstract class ThreadedEventQueue extends MediaThread {
   private List eventQueue = new Vector();
   private boolean killed = false;

   public ThreadedEventQueue() {
      this.useControlPriority();
   }

   protected boolean dispatchEvents() {
      ControllerEvent var2 = null;
      synchronized(this){}

      boolean var1;
      while(true) {
         var1 = true;

         Throwable var10000;
         boolean var10001;
         label429: {
            label420: {
               InterruptedException var41;
               try {
                  try {
                     if (!this.killed && this.eventQueue.size() == 0) {
                        this.wait();
                        continue;
                     }
                     break label420;
                  } catch (InterruptedException var39) {
                     var41 = var39;
                  }
               } catch (Throwable var40) {
                  var10000 = var40;
                  var10001 = false;
                  break label429;
               }

               try {
                  PrintStream var3 = System.err;
                  StringBuilder var4 = new StringBuilder();
                  var4.append("MediaNode event thread ");
                  var4.append(var41);
                  var3.println(var4.toString());
                  return true;
               } catch (Throwable var36) {
                  var10000 = var36;
                  var10001 = false;
                  break label429;
               }
            }

            try {
               if (this.eventQueue.size() > 0) {
                  var2 = (ControllerEvent)this.eventQueue.remove(0);
               }
            } catch (Throwable var38) {
               var10000 = var38;
               var10001 = false;
               break label429;
            }

            label411:
            try {
               break;
            } catch (Throwable var37) {
               var10000 = var37;
               var10001 = false;
               break label411;
            }
         }

         while(true) {
            Throwable var42 = var10000;

            try {
               throw var42;
            } catch (Throwable var35) {
               var10000 = var35;
               var10001 = false;
               continue;
            }
         }
      }

      if (var2 != null) {
         this.processEvent(var2);
      }

      if (this.killed) {
         if (this.eventQueue.size() != 0) {
            return true;
         }

         var1 = false;
      }

      return var1;
   }

   public void kill() {
      synchronized(this){}

      try {
         this.killed = true;
         this.notifyAll();
      } finally {
         ;
      }

   }

   public void postEvent(ControllerEvent var1) {
      synchronized(this){}

      try {
         this.eventQueue.add(var1);
         this.notifyAll();
      } finally {
         ;
      }

   }

   protected abstract void processEvent(ControllerEvent var1);

   public void run() {
      while(this.dispatchEvents()) {
      }

   }
}
