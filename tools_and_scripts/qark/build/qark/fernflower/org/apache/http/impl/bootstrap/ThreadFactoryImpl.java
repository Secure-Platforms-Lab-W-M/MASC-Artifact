package org.apache.http.impl.bootstrap;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

class ThreadFactoryImpl implements ThreadFactory {
   private final AtomicLong count;
   private final ThreadGroup group;
   private final String namePrefix;

   ThreadFactoryImpl(String var1) {
      this(var1, (ThreadGroup)null);
   }

   ThreadFactoryImpl(String var1, ThreadGroup var2) {
      this.namePrefix = var1;
      this.group = var2;
      this.count = new AtomicLong();
   }

   public Thread newThread(Runnable var1) {
      ThreadGroup var2 = this.group;
      StringBuilder var3 = new StringBuilder();
      var3.append(this.namePrefix);
      var3.append("-");
      var3.append(this.count.incrementAndGet());
      return new Thread(var2, var1, var3.toString());
   }
}
