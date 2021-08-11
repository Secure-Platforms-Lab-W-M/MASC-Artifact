package org.apache.commons.lang3.concurrent;

import java.util.concurrent.atomic.AtomicReference;

public abstract class AtomicInitializer implements ConcurrentInitializer {
   private final AtomicReference reference = new AtomicReference();

   public Object get() throws ConcurrentException {
      Object var2 = this.reference.get();
      Object var1 = var2;
      if (var2 == null) {
         var2 = this.initialize();
         var1 = var2;
         if (!this.reference.compareAndSet((Object)null, var2)) {
            var1 = this.reference.get();
         }
      }

      return var1;
   }

   protected abstract Object initialize() throws ConcurrentException;
}
