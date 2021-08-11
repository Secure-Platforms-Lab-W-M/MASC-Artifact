package util;

import java.util.HashSet;

public class TimoutNotificator implements Runnable {
   public static TimoutNotificator instance = new TimoutNotificator();
   private volatile long curTime = 0L;
   public HashSet listeners = new HashSet();
   private boolean stopped = false;
   public boolean threadAvailable = false;

   public static TimoutNotificator getInstance() {
      return instance;
   }

   public static TimoutNotificator getNewInstance() {
      return new TimoutNotificator();
   }

   public long getCurrentTime() {
      return this.threadAvailable ? this.curTime : System.currentTimeMillis();
   }

   public void register(TimeoutListener var1) {
      synchronized(this){}

      try {
         this.listeners.add(var1);
         if (!this.threadAvailable) {
            this.curTime = System.currentTimeMillis();
            this.threadAvailable = true;
            Thread var4 = new Thread(this);
            var4.setDaemon(true);
            var4.start();
         }
      } finally {
         ;
      }

   }

   public void run() {
      // $FF: Couldn't be decompiled
   }

   public void shutdown() {
      synchronized(this){}

      try {
         this.stopped = true;
         this.notifyAll();
      } finally {
         ;
      }

   }

   public void unregister(TimeoutListener var1) {
      synchronized(this){}

      try {
         this.listeners.remove(var1);
      } finally {
         ;
      }

   }
}
