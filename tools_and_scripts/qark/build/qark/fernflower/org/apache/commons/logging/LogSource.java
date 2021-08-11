package org.apache.commons.logging;

import java.lang.reflect.Constructor;
import java.util.Hashtable;
import org.apache.commons.logging.impl.NoOpLog;

public class LogSource {
   protected static boolean jdk14IsAvailable = false;
   protected static boolean log4jIsAvailable = false;
   protected static Constructor logImplctor = null;
   protected static Hashtable logs = new Hashtable();

   static {
      boolean var1 = true;

      boolean var0;
      boolean var10001;
      label1650: {
         label1649: {
            label1648: {
               label1647: {
                  try {
                     if (Class.forName("org.apache.log4j.Logger") != null) {
                        break label1647;
                     }
                  } catch (Throwable var185) {
                     var10001 = false;
                     break label1649;
                  }

                  var0 = false;
                  break label1648;
               }

               var0 = true;
            }

            label1641:
            try {
               log4jIsAvailable = var0;
               break label1650;
            } catch (Throwable var184) {
               var10001 = false;
               break label1641;
            }
         }

         log4jIsAvailable = false;
      }

      label1637: {
         label1636: {
            label1635: {
               label1634: {
                  try {
                     if (Class.forName("java.util.logging.Logger") != null && Class.forName("org.apache.commons.logging.impl.Jdk14Logger") != null) {
                        break label1634;
                     }
                  } catch (Throwable var183) {
                     var10001 = false;
                     break label1636;
                  }

                  var0 = false;
                  break label1635;
               }

               var0 = var1;
            }

            label1625:
            try {
               jdk14IsAvailable = var0;
               break label1637;
            } catch (Throwable var182) {
               var10001 = false;
               break label1625;
            }
         }

         jdk14IsAvailable = false;
      }

      String var2 = null;

      label1658: {
         String var3;
         try {
            var3 = System.getProperty("org.apache.commons.logging.log");
         } catch (Throwable var181) {
            var10001 = false;
            break label1658;
         }

         var2 = var3;
         if (var3 == null) {
            var2 = var3;

            try {
               var3 = System.getProperty("org.apache.commons.logging.Log");
            } catch (Throwable var180) {
               var10001 = false;
               break label1658;
            }

            var2 = var3;
         }
      }

      if (var2 != null) {
         try {
            setLogImplementation(var2);
         } finally {
            try {
               setLogImplementation("org.apache.commons.logging.impl.NoOpLog");
               return;
            } finally {
               return;
            }
         }
      } else {
         label1606: {
            label1655: {
               try {
                  if (log4jIsAvailable) {
                     setLogImplementation("org.apache.commons.logging.impl.Log4JLogger");
                     break label1655;
                  }
               } catch (Throwable var178) {
                  var10001 = false;
                  break label1606;
               }

               try {
                  if (jdk14IsAvailable) {
                     setLogImplementation("org.apache.commons.logging.impl.Jdk14Logger");
                     break label1655;
                  }
               } catch (Throwable var179) {
                  var10001 = false;
                  break label1606;
               }

               try {
                  setLogImplementation("org.apache.commons.logging.impl.NoOpLog");
               } catch (Throwable var177) {
                  var10001 = false;
                  break label1606;
               }
            }

            label1593:
            try {
               return;
            } catch (Throwable var176) {
               var10001 = false;
               break label1593;
            }
         }

         try {
            setLogImplementation("org.apache.commons.logging.impl.NoOpLog");
         } finally {
            return;
         }
      }
   }

   private LogSource() {
   }

   public static Log getInstance(Class var0) {
      return getInstance(var0.getName());
   }

   public static Log getInstance(String var0) {
      Log var2 = (Log)logs.get(var0);
      Log var1 = var2;
      if (var2 == null) {
         var1 = makeNewLogInstance(var0);
         logs.put(var0, var1);
      }

      return var1;
   }

   public static String[] getLogNames() {
      return (String[])((String[])logs.keySet().toArray(new String[logs.size()]));
   }

   public static Log makeNewLogInstance(String var0) {
      Log var1;
      try {
         var1 = (Log)logImplctor.newInstance(var0);
      } finally {
         ;
      }

      Object var2 = var1;
      if (var1 == null) {
         var2 = new NoOpLog(var0);
      }

      return (Log)var2;
   }

   public static void setLogImplementation(Class var0) throws LinkageError, ExceptionInInitializerError, NoSuchMethodException, SecurityException {
      logImplctor = var0.getConstructor("".getClass());
   }

   public static void setLogImplementation(String var0) throws LinkageError, NoSuchMethodException, SecurityException, ClassNotFoundException {
      try {
         logImplctor = Class.forName(var0).getConstructor("".getClass());
      } finally {
         logImplctor = null;
         return;
      }
   }
}
