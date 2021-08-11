package okhttp3.internal.platform;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class OptionalMethod {
   private final String methodName;
   private final Class[] methodParams;
   private final Class returnType;

   OptionalMethod(Class var1, String var2, Class... var3) {
      this.returnType = var1;
      this.methodName = var2;
      this.methodParams = var3;
   }

   private Method getMethod(Class var1) {
      Method var2 = null;
      String var3 = this.methodName;
      if (var3 != null) {
         Method var4 = getPublicMethod(var1, var3, this.methodParams);
         var2 = var4;
         if (var4 != null) {
            Class var5 = this.returnType;
            var2 = var4;
            if (var5 != null) {
               var2 = var4;
               if (!var5.isAssignableFrom(var4.getReturnType())) {
                  var2 = null;
               }
            }
         }
      }

      return var2;
   }

   private static Method getPublicMethod(Class var0, String var1, Class[] var2) {
      Method var4 = null;

      boolean var10001;
      Method var7;
      try {
         var7 = var0.getMethod(var1, var2);
      } catch (NoSuchMethodException var6) {
         var10001 = false;
         return var4;
      }

      var4 = var7;

      int var3;
      try {
         var3 = var7.getModifiers();
      } catch (NoSuchMethodException var5) {
         var10001 = false;
         return var4;
      }

      if ((var3 & 1) == 0) {
         var7 = null;
      }

      return var7;
   }

   public Object invoke(Object var1, Object... var2) throws InvocationTargetException {
      Method var3 = this.getMethod(var1.getClass());
      StringBuilder var5;
      if (var3 != null) {
         try {
            var1 = var3.invoke(var1, var2);
            return var1;
         } catch (IllegalAccessException var4) {
            var5 = new StringBuilder();
            var5.append("Unexpectedly could not call: ");
            var5.append(var3);
            AssertionError var6 = new AssertionError(var5.toString());
            var6.initCause(var4);
            throw var6;
         }
      } else {
         var5 = new StringBuilder();
         var5.append("Method ");
         var5.append(this.methodName);
         var5.append(" not supported for object ");
         var5.append(var1);
         throw new AssertionError(var5.toString());
      }
   }

   public Object invokeOptional(Object var1, Object... var2) throws InvocationTargetException {
      Method var3 = this.getMethod(var1.getClass());
      if (var3 == null) {
         return null;
      } else {
         try {
            var1 = var3.invoke(var1, var2);
            return var1;
         } catch (IllegalAccessException var4) {
            return null;
         }
      }
   }

   public Object invokeOptionalWithoutCheckedException(Object var1, Object... var2) {
      try {
         var1 = this.invokeOptional(var1, var2);
         return var1;
      } catch (InvocationTargetException var3) {
         Throwable var4 = var3.getTargetException();
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            AssertionError var5 = new AssertionError("Unexpected exception");
            var5.initCause(var4);
            throw var5;
         }
      }
   }

   public Object invokeWithoutCheckedException(Object var1, Object... var2) {
      try {
         var1 = this.invoke(var1, var2);
         return var1;
      } catch (InvocationTargetException var3) {
         Throwable var4 = var3.getTargetException();
         if (var4 instanceof RuntimeException) {
            throw (RuntimeException)var4;
         } else {
            AssertionError var5 = new AssertionError("Unexpected exception");
            var5.initCause(var4);
            throw var5;
         }
      }
   }

   public boolean isSupported(Object var1) {
      return this.getMethod(var1.getClass()) != null;
   }
}
