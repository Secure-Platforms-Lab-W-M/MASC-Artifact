package net.sf.fmj.ejmf.toolkit.media;

import com.lti.utils.synchronization.CloseableThread;
import java.util.Vector;
import java.util.logging.Logger;
import javax.media.ControllerEvent;
import net.sf.fmj.utility.LoggerSingleton;

public class ControllerEventQueue extends CloseableThread {
   private static final Logger logger;
   Vector eventQueue = new Vector();
   Vector listeners;

   static {
      logger = LoggerSingleton.logger;
   }

   public ControllerEventQueue(Vector var1, String var2) {
      this.setName(var2);
      this.listeners = var1;
      this.setDaemon(true);
   }

   private void dispatchEvent(ControllerEvent param1) {
      // $FF: Couldn't be decompiled
   }

   private void monitorEvents() throws InterruptedException {
      label193:
      while(true) {
         if (!this.isClosing()) {
            synchronized(this){}

            Vector var15;
            while(true) {
               Throwable var10000;
               boolean var10001;
               label185: {
                  try {
                     if (this.eventQueue.size() == 0) {
                        this.wait();
                        continue;
                     }
                  } catch (Throwable var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label185;
                  }

                  label178:
                  try {
                     var15 = (Vector)this.eventQueue.clone();
                     this.eventQueue.removeAllElements();
                     break;
                  } catch (Throwable var13) {
                     var10000 = var13;
                     var10001 = false;
                     break label178;
                  }
               }

               while(true) {
                  Throwable var2 = var10000;

                  try {
                     throw var2;
                  } catch (Throwable var12) {
                     var10000 = var12;
                     var10001 = false;
                     continue;
                  }
               }
            }

            int var1 = 0;

            while(true) {
               if (var1 >= var15.size()) {
                  continue label193;
               }

               this.dispatchEvent((ControllerEvent)var15.elementAt(var1));
               ++var1;
            }
         }

         return;
      }
   }

   public void postEvent(ControllerEvent var1) {
      synchronized(this){}

      try {
         this.eventQueue.addElement(var1);
         this.notify();
      } finally {
         ;
      }

   }

   public void run() {
      try {
         this.monitorEvents();
      } catch (InterruptedException var2) {
      }

      this.setClosed();
   }
}
