package net.sf.fmj.media.datasink;

import java.util.Iterator;
import java.util.Vector;
import javax.media.DataSink;
import javax.media.datasink.DataSinkErrorEvent;
import javax.media.datasink.DataSinkEvent;
import javax.media.datasink.DataSinkListener;
import javax.media.datasink.EndOfStreamEvent;

public abstract class BasicDataSink implements DataSink {
   protected final Vector listeners = new Vector(1);

   public void addDataSinkListener(DataSinkListener var1) {
      if (var1 != null && !this.listeners.contains(var1)) {
         this.listeners.addElement(var1);
      }

   }

   protected void removeAllListeners() {
      this.listeners.removeAllElements();
   }

   public void removeDataSinkListener(DataSinkListener var1) {
      if (var1 != null) {
         this.listeners.removeElement(var1);
      }

   }

   protected final void sendDataSinkErrorEvent(String var1) {
      this.sendEvent(new DataSinkErrorEvent(this, var1));
   }

   protected final void sendEndofStreamEvent() {
      this.sendEvent(new EndOfStreamEvent(this));
   }

   protected void sendEvent(DataSinkEvent var1) {
      if (!this.listeners.isEmpty()) {
         Vector var2 = this.listeners;
         synchronized(var2){}

         Throwable var10000;
         boolean var10001;
         label216: {
            Iterator var3;
            try {
               var3 = this.listeners.iterator();
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label216;
            }

            while(true) {
               try {
                  if (var3.hasNext()) {
                     ((DataSinkListener)var3.next()).dataSinkUpdate(var1);
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
}
