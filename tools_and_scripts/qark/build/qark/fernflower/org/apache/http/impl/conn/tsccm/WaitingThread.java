package org.apache.http.impl.conn.tsccm;

import java.util.Date;
import java.util.concurrent.locks.Condition;
import org.apache.http.util.Args;

@Deprecated
public class WaitingThread {
   private boolean aborted;
   private final Condition cond;
   private final RouteSpecificPool pool;
   private Thread waiter;

   public WaitingThread(Condition var1, RouteSpecificPool var2) {
      Args.notNull(var1, "Condition");
      this.cond = var1;
      this.pool = var2;
   }

   public boolean await(Date var1) throws InterruptedException {
      if (this.waiter == null) {
         if (this.aborted) {
            throw new InterruptedException("Operation interrupted");
         } else {
            Throwable var10000;
            label220: {
               this.waiter = Thread.currentThread();
               boolean var10001;
               boolean var2;
               if (var1 != null) {
                  try {
                     var2 = this.cond.awaitUntil(var1);
                  } catch (Throwable var23) {
                     var10000 = var23;
                     var10001 = false;
                     break label220;
                  }
               } else {
                  try {
                     this.cond.await();
                  } catch (Throwable var22) {
                     var10000 = var22;
                     var10001 = false;
                     break label220;
                  }

                  var2 = true;
               }

               boolean var3;
               try {
                  var3 = this.aborted;
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label220;
               }

               if (!var3) {
                  this.waiter = null;
                  return var2;
               }

               label202:
               try {
                  throw new InterruptedException("Operation interrupted");
               } catch (Throwable var20) {
                  var10000 = var20;
                  var10001 = false;
                  break label202;
               }
            }

            Throwable var25 = var10000;
            this.waiter = null;
            throw var25;
         }
      } else {
         StringBuilder var24 = new StringBuilder();
         var24.append("A thread is already waiting on this object.\ncaller: ");
         var24.append(Thread.currentThread());
         var24.append("\nwaiter: ");
         var24.append(this.waiter);
         throw new IllegalStateException(var24.toString());
      }
   }

   public final Condition getCondition() {
      return this.cond;
   }

   public final RouteSpecificPool getPool() {
      return this.pool;
   }

   public final Thread getThread() {
      return this.waiter;
   }

   public void interrupt() {
      this.aborted = true;
      this.cond.signalAll();
   }

   public void wakeup() {
      if (this.waiter != null) {
         this.cond.signalAll();
      } else {
         throw new IllegalStateException("Nobody waiting on this object.");
      }
   }
}
