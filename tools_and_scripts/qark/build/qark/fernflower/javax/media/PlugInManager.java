package javax.media;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.fmj.utility.LoggerSingleton;

public class PlugInManager {
   public static final int CODEC = 2;
   public static final int DEMULTIPLEXER = 1;
   public static final int EFFECT = 3;
   public static final int MULTIPLEXER = 5;
   public static final int RENDERER = 4;
   private static Method addPlugInMethod;
   private static Method commitMethod;
   private static Method getPlugInListMethod;
   private static Method getSupportedInputFormatsMethod;
   private static Method getSupportedOutputFormatsMethod;
   private static Class implClass;
   private static final Logger logger;
   private static Method removePlugInMethod;
   private static Method setPlugInListMethod;

   static {
      logger = LoggerSingleton.logger;
   }

   public static boolean addPlugIn(String var0, Format[] var1, Format[] var2, int var3) {
      return !init() ? false : (Boolean)callImpl(addPlugInMethod, new Object[]{var0, var1, var2, var3});
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
         callImpl(commitMethod, new Object[0]);
      }
   }

   public static Vector getPlugInList(Format var0, Format var1, int var2) {
      return !init() ? null : (Vector)callImpl(getPlugInListMethod, new Object[]{var0, var1, var2});
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

   public static Format[] getSupportedInputFormats(String var0, int var1) {
      return !init() ? null : (Format[])((Format[])callImpl(getSupportedInputFormatsMethod, new Object[]{var0, var1}));
   }

   public static Format[] getSupportedOutputFormats(String var0, int var1) {
      return !init() ? null : (Format[])((Format[])callImpl(getSupportedOutputFormatsMethod, new Object[]{var0, var1}));
   }

   private static boolean init() {
      synchronized(PlugInManager.class){}

      Throwable var10000;
      Throwable var24;
      label234: {
         Class var0;
         boolean var10001;
         try {
            var0 = implClass;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label234;
         }

         if (var0 != null) {
            return true;
         }

         label228: {
            label227: {
               try {
                  var0 = Class.forName("javax.media.pim.PlugInManager");
                  implClass = var0;
                  if (PlugInManager.class.isAssignableFrom(var0)) {
                     getPlugInListMethod = getStaticMethodOnImplClass("getPlugInList", new Class[]{Format.class, Format.class, Integer.TYPE}, Vector.class);
                     var0 = Integer.TYPE;
                     Class var25 = Void.TYPE;
                     setPlugInListMethod = getStaticMethodOnImplClass("setPlugInList", new Class[]{Vector.class, var0}, var25);
                     var0 = Void.TYPE;
                     commitMethod = getStaticMethodOnImplClass("commit", new Class[0], var0);
                     var0 = Integer.TYPE;
                     var25 = Boolean.TYPE;
                     addPlugInMethod = getStaticMethodOnImplClass("addPlugIn", new Class[]{String.class, Format[].class, Format[].class, var0}, var25);
                     var0 = Integer.TYPE;
                     var25 = Boolean.TYPE;
                     removePlugInMethod = getStaticMethodOnImplClass("removePlugIn", new Class[]{String.class, var0}, var25);
                     getSupportedInputFormatsMethod = getStaticMethodOnImplClass("getSupportedInputFormats", new Class[]{String.class, Integer.TYPE}, Format[].class);
                     getSupportedOutputFormatsMethod = getStaticMethodOnImplClass("getSupportedOutputFormats", new Class[]{String.class, Integer.TYPE}, Format[].class);
                     break label227;
                  }
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label228;
               }

               try {
                  StringBuilder var26 = new StringBuilder();
                  var26.append("javax.media.pim.PlugInManager not subclass of ");
                  var26.append(PlugInManager.class.getName());
                  throw new Exception(var26.toString());
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label228;
               }
            }

            return true;
         }

         var24 = var10000;

         try {
            implClass = null;
            Logger var1 = logger;
            Level var2 = Level.SEVERE;
            StringBuilder var3 = new StringBuilder();
            var3.append("Unable to initialize javax.media.pim.PlugInManager: ");
            var3.append(var24);
            var1.log(var2, var3.toString(), var24);
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            break label234;
         }

         return false;
      }

      var24 = var10000;
      throw var24;
   }

   public static boolean removePlugIn(String var0, int var1) {
      return !init() ? false : (Boolean)callImpl(removePlugInMethod, new Object[]{var0, var1});
   }

   public static void setPlugInList(Vector var0, int var1) {
      if (init()) {
         callImpl(setPlugInListMethod, new Object[]{var0, var1});
      }
   }
}
