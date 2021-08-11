package org.apache.commons.lang3;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.lang.reflect.UndeclaredThrowableException;

public class Functions {
   public static void accept(Functions.FailableBiConsumer var0, Object var1, Object var2) {
      try {
         var0.accept(var1, var2);
      } catch (Throwable var4) {
         throw rethrow(var4);
      }
   }

   public static void accept(Functions.FailableConsumer var0, Object var1) {
      try {
         var0.accept(var1);
      } catch (Throwable var3) {
         throw rethrow(var3);
      }
   }

   public static Object apply(Functions.FailableBiFunction var0, Object var1, Object var2) {
      try {
         Object var5 = var0.apply(var1, var2);
         return var5;
      } catch (Throwable var4) {
         throw rethrow(var4);
      }
   }

   public static Object apply(Functions.FailableFunction var0, Object var1) {
      try {
         Object var4 = var0.apply(var1);
         return var4;
      } catch (Throwable var3) {
         throw rethrow(var3);
      }
   }

   public static Object call(Functions.FailableCallable var0) {
      try {
         Object var3 = var0.call();
         return var3;
      } catch (Throwable var2) {
         throw rethrow(var2);
      }
   }

   // $FF: synthetic method
   static void lambda$tryWithResources$0(Throwable var0) throws Throwable {
      rethrow(var0);
   }

   public static RuntimeException rethrow(Throwable var0) {
      if (var0 != null) {
         if (!(var0 instanceof RuntimeException)) {
            if (!(var0 instanceof Error)) {
               if (var0 instanceof IOException) {
                  throw new UncheckedIOException((IOException)var0);
               } else {
                  throw new UndeclaredThrowableException(var0);
               }
            } else {
               throw (Error)var0;
            }
         } else {
            throw (RuntimeException)var0;
         }
      } else {
         throw new NullPointerException("The Throwable must not be null.");
      }
   }

   public static void run(Functions.FailableRunnable var0) {
      try {
         var0.run();
      } catch (Throwable var2) {
         throw rethrow(var2);
      }
   }

   public static boolean test(Functions.FailableBiPredicate var0, Object var1, Object var2) {
      try {
         boolean var3 = var0.test(var1, var2);
         return var3;
      } catch (Throwable var5) {
         throw rethrow(var5);
      }
   }

   public static boolean test(Functions.FailablePredicate var0, Object var1) {
      try {
         boolean var2 = var0.test(var1);
         return var2;
      } catch (Throwable var4) {
         throw rethrow(var4);
      }
   }

   @SafeVarargs
   public static void tryWithResources(Functions.FailableRunnable var0, Functions.FailableConsumer var1, Functions.FailableRunnable... var2) {
      if (var1 == null) {
         var1 = class_6.INSTANCE;
      }

      byte var4 = 0;
      int var3;
      int var5;
      if (var2 != null) {
         var5 = var2.length;

         for(var3 = 0; var3 < var5; ++var3) {
            if (var2[var3] == null) {
               throw new NullPointerException("A resource action must not be null.");
            }
         }
      }

      Object var6 = null;

      label256: {
         try {
            ((Functions.FailableRunnable)var0).run();
         } finally {
            break label256;
         }

      }

      var6 = var0;
      if (var2 != null) {
         var5 = var2.length;
         var3 = var4;

         while(true) {
            var6 = var0;
            if (var3 >= var5) {
               break;
            }

            Functions.FailableRunnable var20 = var2[var3];

            label247: {
               try {
                  var20.run();
               } catch (Throwable var18) {
                  var6 = var0;
                  if (var0 == null) {
                     var6 = var18;
                  }
                  break label247;
               }

               var6 = var0;
            }

            ++var3;
            var0 = var6;
         }
      }

      if (var6 != null) {
         try {
            ((Functions.FailableConsumer)var1).accept(var6);
         } catch (Throwable var17) {
            throw rethrow(var17);
         }
      }
   }

   @SafeVarargs
   public static void tryWithResources(Functions.FailableRunnable var0, Functions.FailableRunnable... var1) {
      tryWithResources(var0, (Functions.FailableConsumer)null, var1);
   }

   @FunctionalInterface
   public interface FailableBiConsumer {
      void accept(Object var1, Object var2) throws Throwable;
   }

   @FunctionalInterface
   public interface FailableBiFunction {
      Object apply(Object var1, Object var2) throws Throwable;
   }

   @FunctionalInterface
   public interface FailableBiPredicate {
      boolean test(Object var1, Object var2) throws Throwable;
   }

   @FunctionalInterface
   public interface FailableCallable {
      Object call() throws Throwable;
   }

   @FunctionalInterface
   public interface FailableConsumer {
      void accept(Object var1) throws Throwable;
   }

   @FunctionalInterface
   public interface FailableFunction {
      Object apply(Object var1) throws Throwable;
   }

   @FunctionalInterface
   public interface FailablePredicate {
      boolean test(Object var1) throws Throwable;
   }

   @FunctionalInterface
   public interface FailableRunnable {
      void run() throws Throwable;
   }
}
