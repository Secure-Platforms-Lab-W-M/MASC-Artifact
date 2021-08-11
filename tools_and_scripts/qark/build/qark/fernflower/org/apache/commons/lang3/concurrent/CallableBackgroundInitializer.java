package org.apache.commons.lang3.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import org.apache.commons.lang3.Validate;

public class CallableBackgroundInitializer extends BackgroundInitializer {
   private final Callable callable;

   public CallableBackgroundInitializer(Callable var1) {
      this.checkCallable(var1);
      this.callable = var1;
   }

   public CallableBackgroundInitializer(Callable var1, ExecutorService var2) {
      super(var2);
      this.checkCallable(var1);
      this.callable = var1;
   }

   private void checkCallable(Callable var1) {
      boolean var2;
      if (var1 != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      Validate.isTrue(var2, "Callable must not be null!");
   }

   protected Object initialize() throws Exception {
      return this.callable.call();
   }
}
