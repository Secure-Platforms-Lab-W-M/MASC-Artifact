package org.apache.commons.logging.impl;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;

public class Jdk13LumberjackLogger implements Log, Serializable {
   protected static final Level dummyLevel;
   private static final long serialVersionUID = -8649807923527610591L;
   private boolean classAndMethodFound = false;
   protected transient Logger logger = null;
   protected String name = null;
   private String sourceClassName = "unknown";
   private String sourceMethodName = "unknown";

   static {
      dummyLevel = Level.FINE;
   }

   public Jdk13LumberjackLogger(String var1) {
      this.name = var1;
      this.logger = this.getLogger();
   }

   private void getClassAndMethod() {
      label49: {
         boolean var10001;
         StringTokenizer var4;
         String var9;
         try {
            Throwable var2 = new Throwable();
            var2.fillInStackTrace();
            StringWriter var3 = new StringWriter();
            var2.printStackTrace(new PrintWriter(var3));
            var4 = new StringTokenizer(var3.getBuffer().toString(), "\n");
            var4.nextToken();
            var9 = var4.nextToken();
         } catch (Exception var6) {
            var10001 = false;
            break label49;
         }

         String var10;
         while(true) {
            var10 = var9;

            try {
               if (var9.indexOf(this.getClass().getName()) != -1) {
                  break;
               }

               var9 = var4.nextToken();
            } catch (Exception var8) {
               var10001 = false;
               break label49;
            }
         }

         while(true) {
            try {
               if (var10.indexOf(this.getClass().getName()) >= 0) {
                  var10 = var4.nextToken();
                  continue;
               }
            } catch (Exception var7) {
               var10001 = false;
               break;
            }

            try {
               var9 = var10.substring(var10.indexOf("at ") + 3, var10.indexOf(40));
               int var1 = var9.lastIndexOf(46);
               this.sourceClassName = var9.substring(0, var1);
               this.sourceMethodName = var9.substring(var1 + 1);
            } catch (Exception var5) {
               var10001 = false;
            }
            break;
         }
      }

      this.classAndMethodFound = true;
   }

   private void log(Level var1, String var2, Throwable var3) {
      if (this.getLogger().isLoggable(var1)) {
         LogRecord var4 = new LogRecord(var1, var2);
         if (!this.classAndMethodFound) {
            this.getClassAndMethod();
         }

         var4.setSourceClassName(this.sourceClassName);
         var4.setSourceMethodName(this.sourceMethodName);
         if (var3 != null) {
            var4.setThrown(var3);
         }

         this.getLogger().log(var4);
      }

   }

   public void debug(Object var1) {
      this.log(Level.FINE, String.valueOf(var1), (Throwable)null);
   }

   public void debug(Object var1, Throwable var2) {
      this.log(Level.FINE, String.valueOf(var1), var2);
   }

   public void error(Object var1) {
      this.log(Level.SEVERE, String.valueOf(var1), (Throwable)null);
   }

   public void error(Object var1, Throwable var2) {
      this.log(Level.SEVERE, String.valueOf(var1), var2);
   }

   public void fatal(Object var1) {
      this.log(Level.SEVERE, String.valueOf(var1), (Throwable)null);
   }

   public void fatal(Object var1, Throwable var2) {
      this.log(Level.SEVERE, String.valueOf(var1), var2);
   }

   public Logger getLogger() {
      if (this.logger == null) {
         this.logger = Logger.getLogger(this.name);
      }

      return this.logger;
   }

   public void info(Object var1) {
      this.log(Level.INFO, String.valueOf(var1), (Throwable)null);
   }

   public void info(Object var1, Throwable var2) {
      this.log(Level.INFO, String.valueOf(var1), var2);
   }

   public boolean isDebugEnabled() {
      return this.getLogger().isLoggable(Level.FINE);
   }

   public boolean isErrorEnabled() {
      return this.getLogger().isLoggable(Level.SEVERE);
   }

   public boolean isFatalEnabled() {
      return this.getLogger().isLoggable(Level.SEVERE);
   }

   public boolean isInfoEnabled() {
      return this.getLogger().isLoggable(Level.INFO);
   }

   public boolean isTraceEnabled() {
      return this.getLogger().isLoggable(Level.FINEST);
   }

   public boolean isWarnEnabled() {
      return this.getLogger().isLoggable(Level.WARNING);
   }

   public void trace(Object var1) {
      this.log(Level.FINEST, String.valueOf(var1), (Throwable)null);
   }

   public void trace(Object var1, Throwable var2) {
      this.log(Level.FINEST, String.valueOf(var1), var2);
   }

   public void warn(Object var1) {
      this.log(Level.WARNING, String.valueOf(var1), (Throwable)null);
   }

   public void warn(Object var1, Throwable var2) {
      this.log(Level.WARNING, String.valueOf(var1), var2);
   }
}
