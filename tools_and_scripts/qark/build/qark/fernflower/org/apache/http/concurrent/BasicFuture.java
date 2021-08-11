package org.apache.http.concurrent;

import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.apache.http.util.Args;

public class BasicFuture implements Future, Cancellable {
   private final FutureCallback callback;
   private volatile boolean cancelled;
   private volatile boolean completed;
   // $FF: renamed from: ex java.lang.Exception
   private volatile Exception field_153;
   private volatile Object result;

   public BasicFuture(FutureCallback var1) {
      this.callback = var1;
   }

   private Object getResult() throws ExecutionException {
      if (this.field_153 == null) {
         if (!this.cancelled) {
            return this.result;
         } else {
            throw new CancellationException();
         }
      } else {
         throw new ExecutionException(this.field_153);
      }
   }

   public boolean cancel() {
      return this.cancel(true);
   }

   public boolean cancel(boolean var1) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label153: {
         try {
            if (this.completed) {
               return false;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label153;
         }

         try {
            this.completed = true;
            this.cancelled = true;
            this.notifyAll();
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label153;
         }

         FutureCallback var15 = this.callback;
         if (var15 != null) {
            var15.cancelled();
         }

         return true;
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

   public boolean completed(Object var1) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label153: {
         try {
            if (this.completed) {
               return false;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label153;
         }

         try {
            this.completed = true;
            this.result = var1;
            this.notifyAll();
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label153;
         }

         FutureCallback var2 = this.callback;
         if (var2 != null) {
            var2.completed(var1);
         }

         return true;
      }

      while(true) {
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean failed(Exception var1) {
      synchronized(this){}

      Throwable var10000;
      boolean var10001;
      label153: {
         try {
            if (this.completed) {
               return false;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label153;
         }

         try {
            this.completed = true;
            this.field_153 = var1;
            this.notifyAll();
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label153;
         }

         FutureCallback var2 = this.callback;
         if (var2 != null) {
            var2.failed(var1);
         }

         return true;
      }

      while(true) {
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public Object get() throws InterruptedException, ExecutionException {
      synchronized(this){}

      while(true) {
         boolean var3 = false;

         try {
            var3 = true;
            if (this.completed) {
               Object var1 = this.getResult();
               var3 = false;
               return var1;
            }

            this.wait();
            var3 = false;
         } finally {
            if (var3) {
               ;
            }
         }
      }
   }

   public Object get(long var1, TimeUnit var3) throws InterruptedException, ExecutionException, TimeoutException {
      synchronized(this){}

      Throwable var10000;
      label512: {
         long var6;
         boolean var10001;
         try {
            Args.notNull(var3, "Time unit");
            var6 = var3.toMillis(var1);
         } catch (Throwable var62) {
            var10000 = var62;
            var10001 = false;
            break label512;
         }

         if (var6 <= 0L) {
            var1 = 0L;
         } else {
            try {
               var1 = System.currentTimeMillis();
            } catch (Throwable var61) {
               var10000 = var61;
               var10001 = false;
               break label512;
            }
         }

         long var4 = var6;

         Object var64;
         try {
            if (this.completed) {
               var64 = this.getResult();
               return var64;
            }
         } catch (Throwable var60) {
            var10000 = var60;
            var10001 = false;
            break label512;
         }

         if (var6 <= 0L) {
            label483:
            try {
               throw new TimeoutException();
            } catch (Throwable var58) {
               var10000 = var58;
               var10001 = false;
               break label483;
            }
         } else {
            while(true) {
               try {
                  this.wait(var4);
                  if (this.completed) {
                     var64 = this.getResult();
                     return var64;
                  }
               } catch (Throwable var63) {
                  var10000 = var63;
                  var10001 = false;
                  break;
               }

               try {
                  var4 = var6 - (System.currentTimeMillis() - var1);
               } catch (Throwable var59) {
                  var10000 = var59;
                  var10001 = false;
                  break;
               }

               if (var4 <= 0L) {
                  try {
                     throw new TimeoutException();
                  } catch (Throwable var57) {
                     var10000 = var57;
                     var10001 = false;
                     break;
                  }
               }
            }
         }
      }

      Throwable var65 = var10000;
      throw var65;
   }

   public boolean isCancelled() {
      return this.cancelled;
   }

   public boolean isDone() {
      return this.completed;
   }
}
