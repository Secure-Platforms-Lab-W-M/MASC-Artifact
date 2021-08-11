package org.apache.commons.logging.impl;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.logging.Log;

public class Jdk14Logger implements Log, Serializable {
   protected static final Level dummyLevel;
   private static final long serialVersionUID = 4784713551416303804L;
   protected transient Logger logger = null;
   protected String name = null;

   static {
      dummyLevel = Level.FINE;
   }

   public Jdk14Logger(String var1) {
      this.name = var1;
      this.logger = this.getLogger();
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

   protected void log(Level var1, String var2, Throwable var3) {
      Logger var5 = this.getLogger();
      if (var5.isLoggable(var1)) {
         StackTraceElement[] var4 = (new Throwable()).getStackTrace();
         String var6 = this.name;
         String var7;
         if (var4 != null && var4.length > 2) {
            var7 = var4[2].getMethodName();
         } else {
            var7 = "unknown";
         }

         if (var3 == null) {
            var5.logp(var1, var6, var7, var2);
            return;
         }

         var5.logp(var1, var6, var7, var2, var3);
      }

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
