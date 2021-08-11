package org.apache.commons.lang3.concurrent;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.Validate;

public class ConcurrentUtils {
   private ConcurrentUtils() {
   }

   static Throwable checkedException(Throwable var0) {
      boolean var1;
      if (var0 != null && !(var0 instanceof RuntimeException) && !(var0 instanceof Error)) {
         var1 = true;
      } else {
         var1 = false;
      }

      StringBuilder var2 = new StringBuilder();
      var2.append("Not a checked exception: ");
      var2.append(var0);
      Validate.isTrue(var1, var2.toString());
      return var0;
   }

   public static Future constantFuture(Object var0) {
      return new ConcurrentUtils.ConstantFuture(var0);
   }

   public static Object createIfAbsent(ConcurrentMap var0, Object var1, ConcurrentInitializer var2) throws ConcurrentException {
      if (var0 != null && var2 != null) {
         Object var3 = var0.get(var1);
         return var3 == null ? putIfAbsent(var0, var1, var2.get()) : var3;
      } else {
         return null;
      }
   }

   public static Object createIfAbsentUnchecked(ConcurrentMap var0, Object var1, ConcurrentInitializer var2) {
      try {
         Object var4 = createIfAbsent(var0, var1, var2);
         return var4;
      } catch (ConcurrentException var3) {
         throw new ConcurrentRuntimeException(var3.getCause());
      }
   }

   public static ConcurrentException extractCause(ExecutionException var0) {
      if (var0 != null && var0.getCause() != null) {
         throwCause(var0);
         return new ConcurrentException(var0.getMessage(), var0.getCause());
      } else {
         return null;
      }
   }

   public static ConcurrentRuntimeException extractCauseUnchecked(ExecutionException var0) {
      if (var0 != null && var0.getCause() != null) {
         throwCause(var0);
         return new ConcurrentRuntimeException(var0.getMessage(), var0.getCause());
      } else {
         return null;
      }
   }

   public static void handleCause(ExecutionException var0) throws ConcurrentException {
      ConcurrentException var1 = extractCause(var0);
      if (var1 != null) {
         throw var1;
      }
   }

   public static void handleCauseUnchecked(ExecutionException var0) {
      ConcurrentRuntimeException var1 = extractCauseUnchecked(var0);
      if (var1 != null) {
         throw var1;
      }
   }

   public static Object initialize(ConcurrentInitializer var0) throws ConcurrentException {
      return var0 != null ? var0.get() : null;
   }

   public static Object initializeUnchecked(ConcurrentInitializer var0) {
      try {
         Object var2 = initialize(var0);
         return var2;
      } catch (ConcurrentException var1) {
         throw new ConcurrentRuntimeException(var1.getCause());
      }
   }

   public static Object putIfAbsent(ConcurrentMap var0, Object var1, Object var2) {
      if (var0 == null) {
         return null;
      } else {
         Object var3 = var0.putIfAbsent(var1, var2);
         return var3 != null ? var3 : var2;
      }
   }

   private static void throwCause(ExecutionException var0) {
      if (!(var0.getCause() instanceof RuntimeException)) {
         if (var0.getCause() instanceof Error) {
            throw (Error)var0.getCause();
         }
      } else {
         throw (RuntimeException)var0.getCause();
      }
   }

   static final class ConstantFuture implements Future {
      private final Object value;

      ConstantFuture(Object var1) {
         this.value = var1;
      }

      public boolean cancel(boolean var1) {
         return false;
      }

      public Object get() {
         return this.value;
      }

      public Object get(long var1, TimeUnit var3) {
         return this.value;
      }

      public boolean isCancelled() {
         return false;
      }

      public boolean isDone() {
         return true;
      }
   }
}
