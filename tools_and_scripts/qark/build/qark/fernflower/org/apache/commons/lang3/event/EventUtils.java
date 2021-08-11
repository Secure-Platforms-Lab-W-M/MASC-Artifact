package org.apache.commons.lang3.event;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.reflect.MethodUtils;

public class EventUtils {
   public static void addEventListener(Object var0, Class var1, Object var2) {
      StringBuilder var7;
      try {
         StringBuilder var3 = new StringBuilder();
         var3.append("add");
         var3.append(var1.getSimpleName());
         MethodUtils.invokeMethod(var0, var3.toString(), var2);
      } catch (NoSuchMethodException var4) {
         var7 = new StringBuilder();
         var7.append("Class ");
         var7.append(var0.getClass().getName());
         var7.append(" does not have a public add");
         var7.append(var1.getSimpleName());
         var7.append(" method which takes a parameter of type ");
         var7.append(var1.getName());
         var7.append(".");
         throw new IllegalArgumentException(var7.toString());
      } catch (IllegalAccessException var5) {
         var7 = new StringBuilder();
         var7.append("Class ");
         var7.append(var0.getClass().getName());
         var7.append(" does not have an accessible add");
         var7.append(var1.getSimpleName());
         var7.append(" method which takes a parameter of type ");
         var7.append(var1.getName());
         var7.append(".");
         throw new IllegalArgumentException(var7.toString());
      } catch (InvocationTargetException var6) {
         throw new RuntimeException("Unable to add listener.", var6.getCause());
      }
   }

   public static void bindEventsToMethod(Object var0, String var1, Object var2, Class var3, String... var4) {
      ClassLoader var5 = var0.getClass().getClassLoader();
      EventUtils.EventBindingInvocationHandler var6 = new EventUtils.EventBindingInvocationHandler(var0, var1, var4);
      addEventListener(var2, var3, var3.cast(Proxy.newProxyInstance(var5, new Class[]{var3}, var6)));
   }

   private static class EventBindingInvocationHandler implements InvocationHandler {
      private final Set eventTypes;
      private final String methodName;
      private final Object target;

      EventBindingInvocationHandler(Object var1, String var2, String[] var3) {
         this.target = var1;
         this.methodName = var2;
         this.eventTypes = new HashSet(Arrays.asList(var3));
      }

      private boolean hasMatchingParametersMethod(Method var1) {
         return MethodUtils.getAccessibleMethod(this.target.getClass(), this.methodName, var1.getParameterTypes()) != null;
      }

      public Object invoke(Object var1, Method var2, Object[] var3) throws Throwable {
         if (!this.eventTypes.isEmpty() && !this.eventTypes.contains(var2.getName())) {
            return null;
         } else {
            return this.hasMatchingParametersMethod(var2) ? MethodUtils.invokeMethod(this.target, this.methodName, var3) : MethodUtils.invokeMethod(this.target, this.methodName);
         }
      }
   }
}
