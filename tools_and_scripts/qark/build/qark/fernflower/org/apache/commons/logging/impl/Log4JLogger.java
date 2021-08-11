package org.apache.commons.logging.impl;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

public class Log4JLogger implements Log, Serializable {
   private static final String FQCN;
   // $FF: synthetic field
   static Class class$org$apache$commons$logging$impl$Log4JLogger;
   // $FF: synthetic field
   static Class class$org$apache$log4j$Level;
   // $FF: synthetic field
   static Class class$org$apache$log4j$Priority;
   private static final long serialVersionUID = 5160705895411730424L;
   private static final Priority traceLevel;
   private transient volatile Logger logger = null;
   private final String name;

   static {
      Class var1 = class$org$apache$commons$logging$impl$Log4JLogger;
      Class var0 = var1;
      if (var1 == null) {
         var0 = class$("org.apache.commons.logging.impl.Log4JLogger");
         class$org$apache$commons$logging$impl$Log4JLogger = var0;
      }

      FQCN = var0.getName();
      var1 = class$org$apache$log4j$Priority;
      var0 = var1;
      if (var1 == null) {
         var0 = class$("org.apache.log4j.Priority");
         class$org$apache$log4j$Priority = var0;
      }

      Class var2 = class$org$apache$log4j$Level;
      var1 = var2;
      if (var2 == null) {
         var1 = class$("org.apache.log4j.Level");
         class$org$apache$log4j$Level = var1;
      }

      if (!var0.isAssignableFrom(var1)) {
         throw new InstantiationError("Log4J 1.2 not available");
      } else {
         Object var6;
         label42: {
            label41: {
               boolean var10001;
               label52: {
                  try {
                     if (class$org$apache$log4j$Level == null) {
                        var0 = class$("org.apache.log4j.Level");
                        class$org$apache$log4j$Level = var0;
                        break label52;
                     }
                  } catch (Exception var5) {
                     var10001 = false;
                     break label41;
                  }

                  try {
                     var0 = class$org$apache$log4j$Level;
                  } catch (Exception var4) {
                     var10001 = false;
                     break label41;
                  }
               }

               try {
                  var6 = (Priority)var0.getDeclaredField("TRACE").get((Object)null);
                  break label42;
               } catch (Exception var3) {
                  var10001 = false;
               }
            }

            var6 = Level.DEBUG;
         }

         traceLevel = (Priority)var6;
      }
   }

   public Log4JLogger() {
      this.name = null;
   }

   public Log4JLogger(String var1) {
      this.name = var1;
      this.logger = this.getLogger();
   }

   public Log4JLogger(Logger var1) {
      if (var1 != null) {
         this.name = var1.getName();
         this.logger = var1;
      } else {
         throw new IllegalArgumentException("Warning - null logger in constructor; possible log4j misconfiguration.");
      }
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

   public void debug(Object var1) {
      this.getLogger().log(FQCN, Level.DEBUG, var1, (Throwable)null);
   }

   public void debug(Object var1, Throwable var2) {
      this.getLogger().log(FQCN, Level.DEBUG, var1, var2);
   }

   public void error(Object var1) {
      this.getLogger().log(FQCN, Level.ERROR, var1, (Throwable)null);
   }

   public void error(Object var1, Throwable var2) {
      this.getLogger().log(FQCN, Level.ERROR, var1, var2);
   }

   public void fatal(Object var1) {
      this.getLogger().log(FQCN, Level.FATAL, var1, (Throwable)null);
   }

   public void fatal(Object var1, Throwable var2) {
      this.getLogger().log(FQCN, Level.FATAL, var1, var2);
   }

   public Logger getLogger() {
      Logger var1 = this.logger;
      if (var1 == null) {
         synchronized(this){}

         Throwable var10000;
         boolean var10001;
         label276: {
            Logger var2;
            try {
               var2 = this.logger;
            } catch (Throwable var32) {
               var10000 = var32;
               var10001 = false;
               break label276;
            }

            var1 = var2;
            if (var2 == null) {
               try {
                  var2 = Logger.getLogger(this.name);
               } catch (Throwable var31) {
                  var10000 = var31;
                  var10001 = false;
                  break label276;
               }

               var1 = var2;

               try {
                  this.logger = var2;
               } catch (Throwable var30) {
                  var10000 = var30;
                  var10001 = false;
                  break label276;
               }
            }

            label260:
            try {
               return var1;
            } catch (Throwable var29) {
               var10000 = var29;
               var10001 = false;
               break label260;
            }
         }

         while(true) {
            Throwable var33 = var10000;

            try {
               throw var33;
            } catch (Throwable var28) {
               var10000 = var28;
               var10001 = false;
               continue;
            }
         }
      } else {
         return var1;
      }
   }

   public void info(Object var1) {
      this.getLogger().log(FQCN, Level.INFO, var1, (Throwable)null);
   }

   public void info(Object var1, Throwable var2) {
      this.getLogger().log(FQCN, Level.INFO, var1, var2);
   }

   public boolean isDebugEnabled() {
      return this.getLogger().isDebugEnabled();
   }

   public boolean isErrorEnabled() {
      return this.getLogger().isEnabledFor(Level.ERROR);
   }

   public boolean isFatalEnabled() {
      return this.getLogger().isEnabledFor(Level.FATAL);
   }

   public boolean isInfoEnabled() {
      return this.getLogger().isInfoEnabled();
   }

   public boolean isTraceEnabled() {
      return this.getLogger().isEnabledFor(traceLevel);
   }

   public boolean isWarnEnabled() {
      return this.getLogger().isEnabledFor(Level.WARN);
   }

   public void trace(Object var1) {
      this.getLogger().log(FQCN, traceLevel, var1, (Throwable)null);
   }

   public void trace(Object var1, Throwable var2) {
      this.getLogger().log(FQCN, traceLevel, var1, var2);
   }

   public void warn(Object var1) {
      this.getLogger().log(FQCN, Level.WARN, var1, (Throwable)null);
   }

   public void warn(Object var1, Throwable var2) {
      this.getLogger().log(FQCN, Level.WARN, var1, var2);
   }
}
