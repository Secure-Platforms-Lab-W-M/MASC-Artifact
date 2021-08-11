package org.apache.commons.lang3.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class BackgroundInitializer implements ConcurrentInitializer {
   private ExecutorService executor;
   private ExecutorService externalExecutor;
   private Future future;

   protected BackgroundInitializer() {
      this((ExecutorService)null);
   }

   protected BackgroundInitializer(ExecutorService var1) {
      this.setExternalExecutor(var1);
   }

   private ExecutorService createExecutor() {
      return Executors.newFixedThreadPool(this.getTaskCount());
   }

   private Callable createTask(ExecutorService var1) {
      return new BackgroundInitializer.InitializationTask(var1);
   }

   public Object get() throws ConcurrentException {
      try {
         Object var1 = this.getFuture().get();
         return var1;
      } catch (ExecutionException var2) {
         ConcurrentUtils.handleCause(var2);
         return null;
      } catch (InterruptedException var3) {
         Thread.currentThread().interrupt();
         throw new ConcurrentException(var3);
      }
   }

   protected final ExecutorService getActiveExecutor() {
      synchronized(this){}

      ExecutorService var1;
      try {
         var1 = this.executor;
      } finally {
         ;
      }

      return var1;
   }

   public final ExecutorService getExternalExecutor() {
      synchronized(this){}

      ExecutorService var1;
      try {
         var1 = this.externalExecutor;
      } finally {
         ;
      }

      return var1;
   }

   public Future getFuture() {
      synchronized(this){}

      Future var1;
      try {
         if (this.future == null) {
            throw new IllegalStateException("start() must be called first!");
         }

         var1 = this.future;
      } finally {
         ;
      }

      return var1;
   }

   protected int getTaskCount() {
      return 1;
   }

   protected abstract Object initialize() throws Exception;

   public boolean isStarted() {
      synchronized(this){}
      boolean var4 = false;

      Future var2;
      try {
         var4 = true;
         var2 = this.future;
         var4 = false;
      } finally {
         if (var4) {
            ;
         }
      }

      boolean var1;
      if (var2 != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public final void setExternalExecutor(ExecutorService var1) {
      synchronized(this){}

      try {
         if (this.isStarted()) {
            throw new IllegalStateException("Cannot set ExecutorService after start()!");
         }

         this.externalExecutor = var1;
      } finally {
         ;
      }

   }

   public boolean start() {
      synchronized(this){}

      Throwable var10000;
      label227: {
         ExecutorService var1;
         boolean var10001;
         label222: {
            try {
               if (!this.isStarted()) {
                  var1 = this.getExternalExecutor();
                  this.executor = var1;
                  break label222;
               }
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label227;
            }

            return false;
         }

         if (var1 == null) {
            ExecutorService var2;
            try {
               var2 = this.createExecutor();
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label227;
            }

            var1 = var2;

            try {
               this.executor = var2;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label227;
            }
         } else {
            var1 = null;
         }

         try {
            this.future = this.executor.submit(this.createTask(var1));
         } catch (Throwable var19) {
            var10000 = var19;
            var10001 = false;
            break label227;
         }

         return true;
      }

      Throwable var23 = var10000;
      throw var23;
   }

   private class InitializationTask implements Callable {
      private final ExecutorService execFinally;

      InitializationTask(ExecutorService var2) {
         this.execFinally = var2;
      }

      public Object call() throws Exception {
         Object var1;
         try {
            var1 = BackgroundInitializer.this.initialize();
         } finally {
            ExecutorService var2 = this.execFinally;
            if (var2 != null) {
               var2.shutdown();
            }

         }

         return var1;
      }
   }
}
