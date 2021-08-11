package org.apache.http.client.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CloneUtils {
   private CloneUtils() {
   }

   public static Object clone(Object var0) throws CloneNotSupportedException {
      return cloneObject(var0);
   }

   public static Object cloneObject(Object var0) throws CloneNotSupportedException {
      if (var0 == null) {
         return null;
      } else if (var0 instanceof Cloneable) {
         Class var1 = var0.getClass();

         Method var6;
         try {
            var6 = var1.getMethod("clone", (Class[])null);
         } catch (NoSuchMethodException var2) {
            throw new NoSuchMethodError(var2.getMessage());
         }

         try {
            var0 = var6.invoke(var0, (Object[])null);
            return var0;
         } catch (InvocationTargetException var3) {
            Throwable var5 = var3.getCause();
            if (var5 instanceof CloneNotSupportedException) {
               throw (CloneNotSupportedException)var5;
            } else {
               throw new Error("Unexpected exception", var5);
            }
         } catch (IllegalAccessException var4) {
            throw new IllegalAccessError(var4.getMessage());
         }
      } else {
         throw new CloneNotSupportedException();
      }
   }
}
