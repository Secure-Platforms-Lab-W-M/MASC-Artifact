package org.apache.http.impl.conn.tsccm;

@Deprecated
public class WaitingThreadAborter {
   private boolean aborted;
   private WaitingThread waitingThread;

   public void abort() {
      this.aborted = true;
      WaitingThread var1 = this.waitingThread;
      if (var1 != null) {
         var1.interrupt();
      }

   }

   public void setWaitingThread(WaitingThread var1) {
      this.waitingThread = var1;
      if (this.aborted) {
         var1.interrupt();
      }

   }
}
