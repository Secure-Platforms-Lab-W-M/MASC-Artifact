package com.lti.utils.synchronization;

public abstract class CloseableThread extends Thread {
   private final SynchronizedBoolean closed = new SynchronizedBoolean(false);
   protected final SynchronizedBoolean closing = new SynchronizedBoolean(false);

   public CloseableThread() {
   }

   public CloseableThread(String var1) {
      super(var1);
   }

   @Deprecated
   public CloseableThread(ThreadGroup var1, String var2) {
      super(var1, var2);
   }

   public void close() {
      this.closing.setValue(true);
      this.interrupt();
   }

   public boolean isClosed() {
      return this.closed.getValue();
   }

   protected boolean isClosing() {
      return this.closing.getValue();
   }

   protected void setClosed() {
      this.closed.setValue(true);
   }

   protected void setClosing() {
      this.closing.setValue(true);
   }

   public void waitUntilClosed() throws InterruptedException {
      this.closed.waitUntil(true);
   }
}
