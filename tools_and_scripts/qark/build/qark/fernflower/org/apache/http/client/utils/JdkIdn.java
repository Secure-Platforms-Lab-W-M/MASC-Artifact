package org.apache.http.client.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Deprecated
public class JdkIdn implements Idn {
   private final Method toUnicode;

   public JdkIdn() throws ClassNotFoundException {
      Class var1 = Class.forName("java.net.IDN");

      try {
         this.toUnicode = var1.getMethod("toUnicode", String.class);
      } catch (SecurityException var2) {
         throw new IllegalStateException(var2.getMessage(), var2);
      } catch (NoSuchMethodException var3) {
         throw new IllegalStateException(var3.getMessage(), var3);
      }
   }

   public String toUnicode(String var1) {
      try {
         var1 = (String)this.toUnicode.invoke((Object)null, var1);
         return var1;
      } catch (IllegalAccessException var2) {
         throw new IllegalStateException(var2.getMessage(), var2);
      } catch (InvocationTargetException var3) {
         Throwable var4 = var3.getCause();
         throw new RuntimeException(var4.getMessage(), var4);
      }
   }
}
