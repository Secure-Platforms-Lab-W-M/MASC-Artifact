package org.apache.commons.lang3.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class Memoizer implements Computable {
   private final ConcurrentMap cache;
   private final Computable computable;
   private final boolean recalculate;

   public Memoizer(Computable var1) {
      this(var1, false);
   }

   public Memoizer(Computable var1, boolean var2) {
      this.cache = new ConcurrentHashMap();
      this.computable = var1;
      this.recalculate = var2;
   }

   private RuntimeException launderException(Throwable var1) {
      if (var1 instanceof RuntimeException) {
         return (RuntimeException)var1;
      } else if (var1 instanceof Error) {
         throw (Error)var1;
      } else {
         throw new IllegalStateException("Unchecked exception", var1);
      }
   }

   public Object compute(final Object var1) throws InterruptedException {
      while(true) {
         Future var3 = (Future)this.cache.get(var1);
         Object var2 = var3;
         if (var3 == null) {
            FutureTask var7 = new FutureTask(new Callable() {
               public Object call() throws InterruptedException {
                  return Memoizer.this.computable.compute(var1);
               }
            });
            Future var4 = (Future)this.cache.putIfAbsent(var1, var7);
            var2 = var4;
            if (var4 == null) {
               var2 = var7;
               var7.run();
            }
         }

         try {
            Object var8 = ((Future)var2).get();
            return var8;
         } catch (CancellationException var5) {
            this.cache.remove(var1, var2);
         } catch (ExecutionException var6) {
            if (this.recalculate) {
               this.cache.remove(var1, var2);
            }

            throw this.launderException(var6.getCause());
         }
      }
   }
}
