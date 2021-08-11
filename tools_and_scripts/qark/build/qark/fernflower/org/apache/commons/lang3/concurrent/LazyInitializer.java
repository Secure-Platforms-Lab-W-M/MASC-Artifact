package org.apache.commons.lang3.concurrent;

public abstract class LazyInitializer implements ConcurrentInitializer {
   private static final Object NO_INIT = new Object();
   private volatile Object object;

   public LazyInitializer() {
      this.object = NO_INIT;
   }

   public Object get() throws ConcurrentException {
      Object var1 = this.object;
      if (var1 == NO_INIT) {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label300: {
            Object var2;
            try {
               var2 = this.object;
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label300;
            }

            var1 = var2;

            label301: {
               try {
                  if (var2 != NO_INIT) {
                     break label301;
                  }

                  var2 = this.initialize();
               } catch (Throwable var31) {
                  var10000 = var31;
                  var10001 = false;
                  break label300;
               }

               var1 = var2;

               try {
                  this.object = var2;
               } catch (Throwable var30) {
                  var10000 = var30;
                  var10001 = false;
                  break label300;
               }
            }

            label281:
            try {
               return var1;
            } catch (Throwable var29) {
               var10000 = var29;
               var10001 = false;
               break label281;
            }
         }

         while(true) {
            Throwable var33 = var10000;

            try {
               throw var33;
            } catch (Throwable var28) {
               var10000 = var28;
               var10001 = false;
               continue;
            }
         }
      } else {
         return var1;
      }
   }

   protected abstract Object initialize() throws ConcurrentException;
}
