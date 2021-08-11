package org.apache.commons.logging.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.AccessController;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Properties;
import org.apache.commons.logging.Log;

public class SimpleLog implements Log, Serializable {
   protected static final String DEFAULT_DATE_TIME_FORMAT = "yyyy/MM/dd HH:mm:ss:SSS zzz";
   public static final int LOG_LEVEL_ALL = 0;
   public static final int LOG_LEVEL_DEBUG = 2;
   public static final int LOG_LEVEL_ERROR = 5;
   public static final int LOG_LEVEL_FATAL = 6;
   public static final int LOG_LEVEL_INFO = 3;
   public static final int LOG_LEVEL_OFF = 7;
   public static final int LOG_LEVEL_TRACE = 1;
   public static final int LOG_LEVEL_WARN = 4;
   // $FF: synthetic field
   static Class class$java$lang$Thread;
   // $FF: synthetic field
   static Class class$org$apache$commons$logging$impl$SimpleLog;
   protected static DateFormat dateFormatter = null;
   protected static volatile String dateTimeFormat = "yyyy/MM/dd HH:mm:ss:SSS zzz";
   private static final long serialVersionUID = 136942970684951178L;
   protected static volatile boolean showDateTime = false;
   protected static volatile boolean showLogName = false;
   protected static volatile boolean showShortName = true;
   protected static final Properties simpleLogProps = new Properties();
   protected static final String systemPrefix = "org.apache.commons.logging.simplelog.";
   protected volatile int currentLogLevel;
   protected volatile String logName = null;
   private volatile String shortLogName = null;

   static {
      InputStream var0 = getResourceAsStream("simplelog.properties");
      if (var0 != null) {
         try {
            simpleLogProps.load(var0);
            var0.close();
         } catch (IOException var1) {
         }
      }

      showLogName = getBooleanProperty("org.apache.commons.logging.simplelog.showlogname", showLogName);
      showShortName = getBooleanProperty("org.apache.commons.logging.simplelog.showShortLogname", showShortName);
      showDateTime = getBooleanProperty("org.apache.commons.logging.simplelog.showdatetime", showDateTime);
      if (showDateTime) {
         dateTimeFormat = getStringProperty("org.apache.commons.logging.simplelog.dateTimeFormat", dateTimeFormat);

         try {
            dateFormatter = new SimpleDateFormat(dateTimeFormat);
            return;
         } catch (IllegalArgumentException var2) {
            dateTimeFormat = "yyyy/MM/dd HH:mm:ss:SSS zzz";
            dateFormatter = new SimpleDateFormat(dateTimeFormat);
         }
      }

   }

   public SimpleLog(String var1) {
      this.logName = var1;
      this.setLevel(3);
      StringBuffer var3 = new StringBuffer();
      var3.append("org.apache.commons.logging.simplelog.log.");
      var3.append(this.logName);
      String var4 = getStringProperty(var3.toString());
      int var2 = String.valueOf(var1).lastIndexOf(".");
      String var6 = var1;

      for(var1 = var4; var1 == null && var2 > -1; var2 = String.valueOf(var6).lastIndexOf(".")) {
         var6 = var6.substring(0, var2);
         StringBuffer var5 = new StringBuffer();
         var5.append("org.apache.commons.logging.simplelog.log.");
         var5.append(var6);
         var1 = getStringProperty(var5.toString());
      }

      var6 = var1;
      if (var1 == null) {
         var6 = getStringProperty("org.apache.commons.logging.simplelog.defaultlog");
      }

      if ("all".equalsIgnoreCase(var6)) {
         this.setLevel(0);
      } else if ("trace".equalsIgnoreCase(var6)) {
         this.setLevel(1);
      } else if ("debug".equalsIgnoreCase(var6)) {
         this.setLevel(2);
      } else if ("info".equalsIgnoreCase(var6)) {
         this.setLevel(3);
      } else if ("warn".equalsIgnoreCase(var6)) {
         this.setLevel(4);
      } else if ("error".equalsIgnoreCase(var6)) {
         this.setLevel(5);
      } else if ("fatal".equalsIgnoreCase(var6)) {
         this.setLevel(6);
      } else {
         if ("off".equalsIgnoreCase(var6)) {
            this.setLevel(7);
         }

      }
   }

   // $FF: synthetic method
   static ClassLoader access$000() {
      return getContextClassLoader();
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

   private static boolean getBooleanProperty(String var0, boolean var1) {
      var0 = getStringProperty(var0);
      return var0 == null ? var1 : "true".equalsIgnoreCase(var0);
   }

   private static ClassLoader getContextClassLoader() {
      // $FF: Couldn't be decompiled
   }

   private static InputStream getResourceAsStream(String var0) {
      return (InputStream)AccessController.doPrivileged(new SimpleLog$1(var0));
   }

   private static String getStringProperty(String var0) {
      String var1 = null;

      String var2;
      try {
         var2 = System.getProperty(var0);
      } catch (SecurityException var3) {
         return var1 == null ? simpleLogProps.getProperty(var0) : var1;
      }

      var1 = var2;
      return var1 == null ? simpleLogProps.getProperty(var0) : var1;
   }

   private static String getStringProperty(String var0, String var1) {
      var0 = getStringProperty(var0);
      return var0 == null ? var1 : var0;
   }

   public final void debug(Object var1) {
      if (this.isLevelEnabled(2)) {
         this.log(2, var1, (Throwable)null);
      }

   }

   public final void debug(Object var1, Throwable var2) {
      if (this.isLevelEnabled(2)) {
         this.log(2, var1, var2);
      }

   }

   public final void error(Object var1) {
      if (this.isLevelEnabled(5)) {
         this.log(5, var1, (Throwable)null);
      }

   }

   public final void error(Object var1, Throwable var2) {
      if (this.isLevelEnabled(5)) {
         this.log(5, var1, var2);
      }

   }

   public final void fatal(Object var1) {
      if (this.isLevelEnabled(6)) {
         this.log(6, var1, (Throwable)null);
      }

   }

   public final void fatal(Object var1, Throwable var2) {
      if (this.isLevelEnabled(6)) {
         this.log(6, var1, var2);
      }

   }

   public int getLevel() {
      return this.currentLogLevel;
   }

   public final void info(Object var1) {
      if (this.isLevelEnabled(3)) {
         this.log(3, var1, (Throwable)null);
      }

   }

   public final void info(Object var1, Throwable var2) {
      if (this.isLevelEnabled(3)) {
         this.log(3, var1, var2);
      }

   }

   public final boolean isDebugEnabled() {
      return this.isLevelEnabled(2);
   }

   public final boolean isErrorEnabled() {
      return this.isLevelEnabled(5);
   }

   public final boolean isFatalEnabled() {
      return this.isLevelEnabled(6);
   }

   public final boolean isInfoEnabled() {
      return this.isLevelEnabled(3);
   }

   protected boolean isLevelEnabled(int var1) {
      return var1 >= this.currentLogLevel;
   }

   public final boolean isTraceEnabled() {
      return this.isLevelEnabled(1);
   }

   public final boolean isWarnEnabled() {
      return this.isLevelEnabled(4);
   }

   protected void log(int param1, Object param2, Throwable param3) {
      // $FF: Couldn't be decompiled
   }

   public void setLevel(int var1) {
      this.currentLogLevel = var1;
   }

   public final void trace(Object var1) {
      if (this.isLevelEnabled(1)) {
         this.log(1, var1, (Throwable)null);
      }

   }

   public final void trace(Object var1, Throwable var2) {
      if (this.isLevelEnabled(1)) {
         this.log(1, var1, var2);
      }

   }

   public final void warn(Object var1) {
      if (this.isLevelEnabled(4)) {
         this.log(4, var1, (Throwable)null);
      }

   }

   public final void warn(Object var1, Throwable var2) {
      if (this.isLevelEnabled(4)) {
         this.log(4, var1, var2);
      }

   }

   protected void write(StringBuffer var1) {
      System.err.println(var1.toString());
   }
}
