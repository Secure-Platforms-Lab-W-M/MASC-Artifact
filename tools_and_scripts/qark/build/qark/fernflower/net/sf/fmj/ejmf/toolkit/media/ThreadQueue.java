package net.sf.fmj.ejmf.toolkit.media;

import com.lti.utils.synchronization.CloseableThread;
import java.util.Vector;

public class ThreadQueue extends CloseableThread {
   private Vector queue = new Vector();
   private Thread running;

   public ThreadQueue(String var1) {
      this.setName(var1);
      this.setDaemon(true);
   }

   public void addThread(Thread var1) {
      synchronized(this){}

      try {
         this.queue.addElement(var1);
         this.notify();
      } finally {
         ;
      }

   }

   public void run() {
      // $FF: Couldn't be decompiled
   }

   public void stopThreads() {
      synchronized(this){}

      Throwable var10000;
      label224: {
         boolean var10001;
         try {
            if (this.running != null) {
               this.running.stop();
            }
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label224;
         }

         int var1 = 0;

         int var2;
         try {
            var2 = this.queue.size();
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label224;
         }

         for(; var1 < var2; ++var1) {
            try {
               ((Thread)this.queue.elementAt(var1)).stop();
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label224;
            }
         }

         label205:
         try {
            this.queue.removeAllElements();
            return;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label205;
         }
      }

      Throwable var3 = var10000;
      throw var3;
   }
}
