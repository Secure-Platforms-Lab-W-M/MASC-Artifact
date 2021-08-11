package org.apache.commons.logging.impl;

import java.lang.reflect.InvocationTargetException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.apache.commons.logging.LogFactory;

public class ServletContextCleaner implements ServletContextListener {
   private static final Class[] RELEASE_SIGNATURE;
   // $FF: synthetic field
   static Class class$java$lang$ClassLoader;

   static {
      Class var1 = class$java$lang$ClassLoader;
      Class var0 = var1;
      if (var1 == null) {
         var0 = class$("java.lang.ClassLoader");
         class$java$lang$ClassLoader = var0;
      }

      RELEASE_SIGNATURE = new Class[]{var0};
   }

   // $FF: synthetic method
   static Class class$(String var0) {
      try {
         Class var2 = Class.forName(var0);
         return var2;
      } catch (ClassNotFoundException var1) {
         throw new NoClassDefFoundError(var1.getMessage());
      }
   }

   public void contextDestroyed(ServletContextEvent var1) {
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      ClassLoader var7 = var2;

      while(var7 != null) {
         try {
            Class var8 = var7.loadClass("org.apache.commons.logging.LogFactory");
            var8.getMethod("release", RELEASE_SIGNATURE).invoke((Object)null, var2);
            var7 = var8.getClassLoader().getParent();
         } catch (ClassNotFoundException var3) {
            var7 = null;
         } catch (NoSuchMethodException var4) {
            System.err.println("LogFactory instance found which does not support release method!");
            var7 = null;
         } catch (IllegalAccessException var5) {
            System.err.println("LogFactory instance found which is not accessable!");
            var7 = null;
         } catch (InvocationTargetException var6) {
            System.err.println("LogFactory instance release method failed!");
            var7 = null;
         }
      }

      LogFactory.release(var2);
   }

   public void contextInitialized(ServletContextEvent var1) {
   }
}
