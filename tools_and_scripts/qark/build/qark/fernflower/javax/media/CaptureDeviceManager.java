package javax.media;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.fmj.utility.LoggerSingleton;

public class CaptureDeviceManager {
   private static Method addDeviceMethod;
   private static Method commitMethod;
   private static Method getDeviceListMethod;
   private static Method getDeviceMethod;
   private static Class implClass;
   private static final Logger logger;
   private static Method removeDeviceMethod;

   static {
      logger = LoggerSingleton.logger;
   }

   public static boolean addDevice(CaptureDeviceInfo var0) {
      return !init() ? false : (Boolean)callImpl(addDeviceMethod, new Object[]{var0});
   }

   private static Object callImpl(Method var0, Object[] var1) {
      Level var2;
      StringBuilder var3;
      Logger var8;
      try {
         Object var7 = var0.invoke((Object)null, var1);
         return var7;
      } catch (IllegalArgumentException var4) {
         var8 = logger;
         var2 = Level.WARNING;
         var3 = new StringBuilder();
         var3.append("");
         var3.append(var4);
         var8.log(var2, var3.toString(), var4);
         return null;
      } catch (IllegalAccessException var5) {
         var8 = logger;
         var2 = Level.WARNING;
         var3 = new StringBuilder();
         var3.append("");
         var3.append(var5);
         var8.log(var2, var3.toString(), var5);
         return null;
      } catch (InvocationTargetException var6) {
         var8 = logger;
         var2 = Level.WARNING;
         var3 = new StringBuilder();
         var3.append("");
         var3.append(var6);
         var8.log(var2, var3.toString(), var6);
         return null;
      }
   }

   public static void commit() throws IOException {
      if (init()) {
         Method var0 = commitMethod;

         Logger var1;
         Level var2;
         StringBuilder var3;
         try {
            var0.invoke((Object)null);
         } catch (IllegalArgumentException var4) {
            var1 = logger;
            var2 = Level.WARNING;
            var3 = new StringBuilder();
            var3.append("");
            var3.append(var4);
            var1.log(var2, var3.toString(), var4);
         } catch (IllegalAccessException var5) {
            var1 = logger;
            var2 = Level.WARNING;
            var3 = new StringBuilder();
            var3.append("");
            var3.append(var5);
            var1.log(var2, var3.toString(), var5);
         } catch (InvocationTargetException var6) {
            if (!(var6.getCause() instanceof IOException)) {
               var1 = logger;
               var2 = Level.WARNING;
               var3 = new StringBuilder();
               var3.append("");
               var3.append(var6);
               var1.log(var2, var3.toString(), var6);
            } else {
               throw (IOException)var6.getCause();
            }
         }
      }
   }

   public static CaptureDeviceInfo getDevice(String var0) {
      return !init() ? null : (CaptureDeviceInfo)callImpl(getDeviceMethod, new Object[]{var0});
   }

   public static Vector getDeviceList(Format var0) {
      return !init() ? null : (Vector)callImpl(getDeviceListMethod, new Object[]{var0});
   }

   private static Method getStaticMethodOnImplClass(String var0, Class[] var1, Class var2) throws Exception {
      Method var4 = implClass.getMethod(var0, var1);
      if (var4.getReturnType() == var2) {
         if (Modifier.isStatic(var4.getModifiers())) {
            return var4;
         } else {
            StringBuilder var5 = new StringBuilder();
            var5.append("Expected method ");
            var5.append(var0);
            var5.append(" to be static");
            throw new Exception(var5.toString());
         }
      } else {
         StringBuilder var3 = new StringBuilder();
         var3.append("Expected return type of method ");
         var3.append(var0);
         var3.append(" to be ");
         var3.append(var2);
         var3.append(", was ");
         var3.append(var4.getReturnType());
         throw new Exception(var3.toString());
      }
   }

   private static boolean init() {
      // $FF: Couldn't be decompiled
   }

   public static boolean removeDevice(CaptureDeviceInfo var0) {
      return !init() ? false : (Boolean)callImpl(removeDeviceMethod, new Object[]{var0});
   }
}
