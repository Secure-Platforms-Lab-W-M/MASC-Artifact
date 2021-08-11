package net.sf.fmj.media.util;

public abstract class LoopThread extends MediaThread {
   protected boolean killed = false;
   protected boolean paused = false;
   protected boolean started = false;
   private boolean waitingAtPaused = false;

   public LoopThread() {
      this.setName("Loop thread");
   }

   public void blockingPause() {
      synchronized(this){}

      try {
         if (this.waitingAtPaused || this.killed) {
            return;
         }

         this.paused = true;
         this.waitForCompleteStop();
      } finally {
         ;
      }

   }

   public boolean isPaused() {
      return this.paused;
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

   public void pause() {
      synchronized(this){}

      try {
         this.paused = true;
      } finally {
         ;
      }

   }

   protected abstract boolean process();

   public void run() {
      super.run();

      while(this.waitHereIfPaused()) {
         if (!this.process()) {
            return;
         }
      }

   }

   public void start() {
      synchronized(this){}

      try {
         if (!this.started) {
            super.start();
            this.started = true;
         }

         this.paused = false;
         this.notifyAll();
      } finally {
         ;
      }

   }

   public void waitForCompleteStop() {
      synchronized(this){}

      while(true) {
         boolean var4 = false;

         try {
            try {
               var4 = true;
               if (!this.killed) {
                  if (!this.waitingAtPaused) {
                     if (this.paused) {
                        this.wait();
                        var4 = false;
                        continue;
                     }

                     var4 = false;
                     break;
                  }

                  var4 = false;
                  break;
               }

               var4 = false;
            } catch (InterruptedException var5) {
               var4 = false;
            }
            break;
         } finally {
            if (var4) {
               ;
            }
         }
      }

   }

   public void waitForCompleteStop(int var1) {
      synchronized(this){}

      try {
         if (!this.killed && !this.waitingAtPaused && this.paused) {
            this.wait((long)var1);
         }
      } catch (InterruptedException var5) {
      } finally {
         ;
      }

   }

   public boolean waitHereIfPaused() {
      // $FF: Couldn't be decompiled
   }
}
