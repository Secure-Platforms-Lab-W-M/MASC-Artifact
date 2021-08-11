package org.apache.commons.logging.impl;

import java.io.Serializable;
import org.apache.commons.logging.Log;
import org.apache.log.Hierarchy;
import org.apache.log.Logger;

public class LogKitLogger implements Log, Serializable {
   private static final long serialVersionUID = 3768538055836059519L;
   protected transient volatile Logger logger = null;
   protected String name = null;

   public LogKitLogger(String var1) {
      this.name = var1;
      this.logger = this.getLogger();
   }

   public void debug(Object var1) {
      if (var1 != null) {
         this.getLogger().debug(String.valueOf(var1));
      }

   }

   public void debug(Object var1, Throwable var2) {
      if (var1 != null) {
         this.getLogger().debug(String.valueOf(var1), var2);
      }

   }

   public void error(Object var1) {
      if (var1 != null) {
         this.getLogger().error(String.valueOf(var1));
      }

   }

   public void error(Object var1, Throwable var2) {
      if (var1 != null) {
         this.getLogger().error(String.valueOf(var1), var2);
      }

   }

   public void fatal(Object var1) {
      if (var1 != null) {
         this.getLogger().fatalError(String.valueOf(var1));
      }

   }

   public void fatal(Object var1, Throwable var2) {
      if (var1 != null) {
         this.getLogger().fatalError(String.valueOf(var1), var2);
      }

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
                  var2 = Hierarchy.getDefaultHierarchy().getLoggerFor(this.name);
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
      if (var1 != null) {
         this.getLogger().info(String.valueOf(var1));
      }

   }

   public void info(Object var1, Throwable var2) {
      if (var1 != null) {
         this.getLogger().info(String.valueOf(var1), var2);
      }

   }

   public boolean isDebugEnabled() {
      return this.getLogger().isDebugEnabled();
   }

   public boolean isErrorEnabled() {
      return this.getLogger().isErrorEnabled();
   }

   public boolean isFatalEnabled() {
      return this.getLogger().isFatalErrorEnabled();
   }

   public boolean isInfoEnabled() {
      return this.getLogger().isInfoEnabled();
   }

   public boolean isTraceEnabled() {
      return this.getLogger().isDebugEnabled();
   }

   public boolean isWarnEnabled() {
      return this.getLogger().isWarnEnabled();
   }

   public void trace(Object var1) {
      this.debug(var1);
   }

   public void trace(Object var1, Throwable var2) {
      this.debug(var1, var2);
   }

   public void warn(Object var1) {
      if (var1 != null) {
         this.getLogger().warn(String.valueOf(var1));
      }

   }

   public void warn(Object var1, Throwable var2) {
      if (var1 != null) {
         this.getLogger().warn(String.valueOf(var1), var2);
      }

   }
}
