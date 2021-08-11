package org.apache.commons.lang3.concurrent;

import java.util.concurrent.atomic.AtomicReference;

public abstract class AtomicSafeInitializer implements ConcurrentInitializer {
   private final AtomicReference factory = new AtomicReference();
   private final AtomicReference reference = new AtomicReference();

   public final Object get() throws ConcurrentException {
      while(true) {
         Object var1 = this.reference.get();
         if (var1 != null) {
            return var1;
         }

         if (this.factory.compareAndSet((Object)null, this)) {
            this.reference.set(this.initialize());
         }
      }
   }

   protected abstract Object initialize() throws ConcurrentException;
}
