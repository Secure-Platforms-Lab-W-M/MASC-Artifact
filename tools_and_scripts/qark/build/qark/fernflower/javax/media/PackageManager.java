package javax.media;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.fmj.utility.LoggerSingleton;

public class PackageManager {
   private static Method commitContentPrefixListMethod;
   private static Method commitProtocolPrefixListMethod;
   private static Method getContentPrefixListMethod;
   private static Method getProtocolPrefixListMethod;
   private static Class implClass;
   private static final Logger logger;
   private static Method setContentPrefixListMethod;
   private static Method setProtocolPrefixListMethod;

   static {
      logger = LoggerSingleton.logger;
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

   public static void commitContentPrefixList() {
      if (init()) {
         callImpl(commitContentPrefixListMethod, new Object[0]);
      }
   }

   public static void commitProtocolPrefixList() {
      if (init()) {
         callImpl(commitProtocolPrefixListMethod, new Object[0]);
      }
   }

   public static Vector getContentPrefixList() {
      return !init() ? null : (Vector)callImpl(getContentPrefixListMethod, new Object[0]);
   }

   public static Vector getProtocolPrefixList() {
      return !init() ? null : (Vector)callImpl(getProtocolPrefixListMethod, new Object[0]);
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

   public static void setContentPrefixList(Vector var0) {
      if (init()) {
         callImpl(setContentPrefixListMethod, new Object[]{var0});
      }
   }

   public static void setProtocolPrefixList(Vector var0) {
      if (init()) {
         callImpl(setProtocolPrefixListMethod, new Object[]{var0});
      }
   }
}
